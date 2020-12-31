package com.rss.config

import com.rss.data.hikari.HikariDataSourceBuilder
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.sql.Connection
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(value = [DatabaseProperties::class])
open class DataConfig {
    @Bean
    open fun dataSource(databaseProperties: DatabaseProperties): DataSource {
        return HikariDataSourceBuilder()
            .name(databaseProperties.name)
            .hostname(databaseProperties.hostname)
            .port(databaseProperties.port.toInt())
            .username(databaseProperties.username)
            .password(databaseProperties.password)
            .schema(databaseProperties.schema)
            .connectionPoolSize(databaseProperties.connectionPoolSize.toInt())
            .build()
    }
}

@Component
class DataMigration(dataSource: DataSource) {
    init {
        Database.connect(dataSource)
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_READ_COMMITTED
    }
}