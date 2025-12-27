package ds.utils.db

import java.sql.DriverManager

val DB = DbConfig(
    jdbcUrl = "jdbc:postgresql://localhost:5433/airlines",
    username = "airlines",
    password = "password",
)

data class DbConfig(
    val jdbcUrl: String,
    val username: String,
    val password: String,
) {
    fun getConnection() = DriverManager.getConnection(jdbcUrl, username, password)
}

