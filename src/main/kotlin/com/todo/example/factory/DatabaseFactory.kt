import com.zaxxer.hikari.*
import org.flywaydb.core.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseFactory {

    fun init() {

        val jdbcUrl = "jdbc:postgresql://localhost:5432/todo"
        val dbUser = "ktoruser"
        val dbPassword = "ktorpass"

        Database.connect(hikari())
        val flyway = Flyway.configure().dataSource(jdbcUrl, dbUser, dbPassword).load()
        flyway.migrate()
    }

    private fun hikari(): HikariDataSource {
        val jdbcUrl = "jdbc:postgresql://localhost:5432/todo"
        val dbUser = "ktoruser"
        val dbPassword = "ktorpass"
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = jdbcUrl
        config.username = dbUser
        config.password = dbPassword
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(
        block: suspend () -> T
    ): T =
        newSuspendedTransaction { block() }
}