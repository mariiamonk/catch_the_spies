package ds.utils.db

import ds.Config
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.DirectoryResourceAccessor
import liquibase.resource.FileSystemResourceAccessor
import java.io.File


fun DbConfig.drop() {
    this.migrate(changelog = Config.DROP_CHANGELOG)
}

fun DbConfig.migrate(changelog: String = Config.DEFAULT_CHANGELOG) {
    val db = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(JdbcConnection(this.getConnection()))
    val liquibase = Liquibase(changelog, DirectoryResourceAccessor(File(Config.MIGRATIONS_PATH)), db)
    liquibase.update()
}


fun DbConfig.migrate(changelog: String = "changelog.yaml", dir: File) {
    val db = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(JdbcConnection(this.getConnection()))
    val liquibase = Liquibase(changelog, DirectoryResourceAccessor(dir), db)
    liquibase.update()
}

fun DbConfig.reset(changelog: String = "changelog.yaml", dir: File) {
    this.execute("DROP SCHEMA IF EXISTS airlines CASCADE;")
    this.migrate(changelog, dir)
}