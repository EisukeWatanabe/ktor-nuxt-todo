import com.zaxxer.hikari.*
import org.flywaydb.core.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.*
import java.net.*

object DatabaseFactory {

    fun init() {

        Database.connect(hikari())
        val flyway = Flyway.configure().dataSource(hikari()).load()
        flyway.migrate()
    }

    private fun hikari(): HikariDataSource {
        val databaseUrl = System.getenv("DATABASE_URL")
        val dataSourceConfig = createDataSourceConfig(databaseUrl)
        return HikariDataSource(dataSourceConfig)
    }

    fun createDataSourceConfig(databaseUrl: String): HikariConfig {
        val config = HikariConfig()
        val dbUri = URI(databaseUrl)

        val username = dbUri.userInfo.split(":")[0]
        val password = dbUri.userInfo.split(":")[1]
        val jdbcUrl = "jdbc:postgresql://${dbUri.host}:${dbUri.port}${dbUri.path}?sslmode=require"

        config.jdbcUrl = jdbcUrl
        config.username = username
        config.password = password
        config.driverClassName = "org.postgresql.Driver"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()

        return config
    }

    suspend fun <T> dbQuery(
        block: suspend () -> T
    ): T =
        newSuspendedTransaction { block() }
}