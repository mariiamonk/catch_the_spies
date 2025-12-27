package ds.utils.db

import liquibase.structure.core.Table
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.rows
import org.jetbrains.kotlinx.dataframe.io.readSqlQuery
import java.io.File
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet


fun Connection.execute(@Language("PostgreSQL") sql: String): Int {
    return this.createStatement().use { statement ->
        statement.executeUpdate(sql)
    }
}

inline fun Connection.execute(@Language("PostgreSQL") sql: String, binder: PreparedStatement.() -> Unit): Int {
    return this.prepareStatement(sql).use { statement ->
        statement.binder()
        statement.executeUpdate()
    }
}

inline fun <T> Connection.select(@Language("PostgreSQL") sql: String, mapper: ResultSet.() -> T): T {
    return this.createStatement().use { statement ->
        statement.executeQuery(sql).use {
            mapper(it)
        }
    }
}

fun DbConfig.execute(@Language("PostgreSQL") sql: String): Int {
    return this.getConnection().use { connection ->
        connection.execute(sql)
    }
}

inline fun DbConfig.execute(@Language("PostgreSQL") sql: String, binder: PreparedStatement.() -> Unit): Int {
    return this.getConnection().use { connection ->
        connection.execute(sql, binder)
    }
}

inline fun <T> DbConfig.select(@Language("PostgreSQL") sql: String, mapper: ResultSet.() -> T): T {
    return this.getConnection().use { connection ->
        connection.select(sql, mapper)
    }
}

fun Connection.executeFile(file: File) {
    this.execute(file.readText())
}

fun DbConfig.executeFile(file: File) {
    this.execute(file.readText())
}

inline fun <T> Connection.selectFile(file: File, mapper: ResultSet.() -> T): T {
    return this.select(file.readText(), mapper)
}

inline fun <T> DbConfig.selectFile(file: File, mapper: ResultSet.() -> T): T {
    return this.select(file.readText(), mapper)
}

inline fun <T> DbConfig.executeBatch(
    @Language("PostgreSQL")
    sql: String,
    data: Iterable<T>,
    binder: PreparedStatement.(T) -> Unit
) {
    this.getConnection().use { connection ->
        connection.prepareStatement(sql).use { statement ->
            for (item in data) {
                statement.binder(item)
            }
            statement.executeBatch()
        }
    }
}

fun DbConfig.selectDf(@Language("PostgreSQL") sql: String): DataFrame<*> {
    return DataFrame.readSqlQuery(this.getConnection(), sql)
}

fun DbConfig.selectTableAsDf(table: String): DataFrame<*> {
    return this.selectDf("SELECT * FROM $table")
}

fun DataFrame<*>.insertTo(table: String, db: DbConfig = DB) {
    val inserter = DbInserter.create(this, table, db)
    inserter.insert(this.rows())
}