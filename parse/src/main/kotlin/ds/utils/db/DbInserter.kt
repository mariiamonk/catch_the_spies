package ds.utils.db

import ds.utils.camelToSnakeCase
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.DataRow
import org.jetbrains.kotlinx.dataframe.api.rows
import org.jetbrains.kotlinx.dataframe.name
import org.jetbrains.kotlinx.dataframe.type
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.util.GregorianCalendar
import java.util.UUID
import kotlin.collections.get
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf

interface ConvertableToDbString {
    fun dbString(): String
}

annotation class Table(val value: String)
annotation class Column(val value: String)

typealias ParameterSetter = (PreparedStatement, Int, Any?) -> Unit

interface FieldGetter<T> {
    fun get(obj: T): Any?
}

class PropertyFieldGetter<T>(
    val prop: KProperty1<T, Any?>
): FieldGetter<T> {
    override fun get(obj: T): Any? {
        return prop.getter.call(obj)
    }
}

class DataRowFieldGetter<T : DataRow<*>>(
    val col: String
): FieldGetter<T> {
    override fun get(obj: T): Any? {
        return obj[col]
    }
}

class DbInserter <T : Any>(
    val sql: String,
    private val parametrSetters: List<ParameterSetter>,
    private val fieldGetters: List<FieldGetter<T>>,

    private val dbConfig: DbConfig,
) {
    private fun bindTo(ps: PreparedStatement, obj: T) {
        parametrSetters.forEachIndexed { index, setter ->
            setter(ps, index + 1, fieldGetters[index].get(obj))
        }
    }

    fun insert(obj: T) {
        dbConfig.getConnection().use { connection ->
            connection.prepareStatement(sql).use {
                bindTo(it, obj)
                it.executeUpdate()
            }
        }
    }

    fun insert(objs: Sequence<T>) {
        dbConfig.getConnection().use { connection ->
            connection.prepareStatement(sql).use { ps ->
                objs.forEach { obj ->
                    bindTo(ps, obj)
                    ps.addBatch()
                }
                ps.executeBatch()
            }
        }
    }

    fun insert(objs: Iterable<T>) {
        insert(objs.asSequence())
    }

    companion object {
        fun <T : Any> create(cls: KClass<T>, dbConfig: DbConfig): DbInserter<T> {
            val parametrSetters = ArrayList<ParameterSetter>()
            val fieldGetters = ArrayList<FieldGetter<T>>()
            val fieldNames = ArrayList<String>()
            val tblName = cls.findAnnotation<Table>()?.value ?: cls.simpleName!!.camelToSnakeCase()

            cls.declaredMemberProperties.forEach { prop ->
                val name = prop.findAnnotation<Column>()?.value ?: prop.name.camelToSnakeCase()
                fieldNames.add(name)
                parametrSetters.add(getParameterSetter(prop.returnType.classifier as KClass<*>))
                fieldGetters.add(PropertyFieldGetter(prop))
            }

            val sql = """
                INSERT INTO $tblName (${fieldNames.joinToString(",") { "\"$it\"" }})
                VALUES (${generateSequence { "?" }.take(fieldNames.size).joinToString(",")});
            """.trimIndent()

            return DbInserter(
                sql = sql,
                parametrSetters = parametrSetters,
                fieldGetters = fieldGetters,
                dbConfig = dbConfig,
            )
        }

        private fun getParameterSetter(cls: KClass<*>): ParameterSetter {
            if (cls.isSubclassOf(ConvertableToDbString::class)) {
                return { ps, i, obj ->
                    ps.setString(i, (obj as ConvertableToDbString?)?.dbString())
                }
            }
            if (cls.isSubclassOf(Enum::class)) {
                return { ps, i, obj ->
                    ps.setString(i, (obj as Enum<*>?)?.name)
                }
            }
            return standardSetters[cls] ?: throw IllegalArgumentException("No parameter setter found for $cls")
        }

        fun <T> create(df: DataFrame<T>, table: String, db: DbConfig): DbInserter<DataRow<T>> {
            val parametrSetters = ArrayList<ParameterSetter>()
            val fieldGetters = ArrayList<FieldGetter<DataRow<T>>>()
            val columnsNames = ArrayList<String>()

            df.columns().forEach { column ->
                columnsNames.add(column.name)
                parametrSetters.add(getParameterSetter(column.type.classifier as KClass<*>))
                fieldGetters.add(DataRowFieldGetter(column.name))
            }

            val sql = """
                INSERT INTO $table (${columnsNames.joinToString(",")})
                VALUES (${generateSequence { "?" }.take(columnsNames.size).joinToString(",")});
            """.trimIndent()

            return DbInserter(
                sql = sql,
                parametrSetters = parametrSetters,
                fieldGetters = fieldGetters,
                dbConfig = db,
            )
        }

        private val standardSetters = mapOf<KClass<*>, ParameterSetter>(
            Int::class to { ps, i, obj -> ps.setInt(i, obj as Int) },
            Long::class to { ps, i, obj -> ps.setLong(i, obj as Long) },
            Double::class to { ps, i, obj -> ps.setDouble(i, obj as Double) },
            Boolean::class to { ps, i, obj -> ps.setBoolean(i, obj as Boolean) },
            String::class to { ps, i, obj -> ps.setString(i, obj as String?) },
            UUID::class to { ps, i, obj -> ps.setObject(i, obj as UUID?) },
            LocalDate::class to { ps, i, obj -> ps.setDate(i, obj?.let { Date.valueOf(obj as LocalDate) }) },
            LocalTime::class to { ps, i, obj -> ps.setTime(i, obj?.let{ Time.valueOf(obj as LocalTime) }) },
            LocalDateTime::class to { ps, i, obj -> ps.setTimestamp(i, obj.let{ Timestamp.valueOf(obj as LocalDateTime) }) },
            java.util.Date::class to { ps, i, obj -> ps.setDate(i, obj?.let { Date((obj as java.util.Date).time) }) },
            OffsetDateTime::class to { ps, i, obj ->
                if (obj == null) {
                    ps.setTimestamp(i, null, null)
                } else {
                    obj as OffsetDateTime
                    val ts = Timestamp.from(obj.toInstant())
                    val cal = GregorianCalendar.from(obj.toZonedDateTime())
                    ps.setTimestamp(i, ts, cal)
                }
            },
        )
    }
}

inline fun <reified T : Any> inserter(): DbInserter<T> {
    return DbInserter.create(T::class, DB)
}