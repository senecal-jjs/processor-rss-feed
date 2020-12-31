package com.rss.data.hikari

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

class HikariDataSourceBuilder {
    private var hostname: String? = null
    private var port: Int? = null
    private var name: String? = null
    private var schema: String? = null
    private var username: String? = null
    private var password: String? = null
    private var connectionPoolSize: Int? = null
    private var properties: MutableMap<String, String> = mutableMapOf()
    private val shutdownHooks: MutableList<Runnable> = mutableListOf()

    fun hostname(hostname: String): HikariDataSourceBuilder {
        this.hostname = hostname
        return this
    }

    fun port(port: Int): HikariDataSourceBuilder {
        this.port = port
        return this
    }

    fun name(name: String): HikariDataSourceBuilder {
        this.name = name
        return this
    }

    fun schema(schema: String): HikariDataSourceBuilder {
        this.schema = schema
        return this
    }

    fun username(username: String): HikariDataSourceBuilder {
        this.username = username
        return this
    }

    fun password(password: String): HikariDataSourceBuilder {
        this.password = password
        return this
    }

    fun connectionPoolSize(connectionPoolSize: Int): HikariDataSourceBuilder {
        this.connectionPoolSize = connectionPoolSize
        return this
    }

    fun properties(properties: MutableMap<String, String>): HikariDataSourceBuilder {
        this.properties = properties
        return this
    }

    fun property(key: String, value: String): HikariDataSourceBuilder {
        this.properties[key] = value
        return this
    }

    fun addShutdownHook(shutdownHook: Runnable): HikariDataSourceBuilder {
        this.shutdownHooks.add(shutdownHook)
        return this
    }

    fun build(): DataSource {
        val config = HikariConfig()

        config.jdbcUrl = "jdbc:postgresql://${this.hostname}:${this.port}/${this.name}?prepareThreshold=0"
        config.username = this.username
        config.password = this.password
        if (this.schema != null)
            config.schema = this.schema
        this.properties.forEach { config.addDataSourceProperty(it.key, it.value) }
        connectionPoolSize?.run {
            val minimumIdle = this.div(2)
            config.minimumIdle = if (minimumIdle > 0) minimumIdle else 1
            config.maximumPoolSize = this
        }

        return ShutdownHookHikariDataSource(shutdownHooks, config)
    }
}

class ShutdownHookHikariDataSource(private val shutdownHooks: List<Runnable>, config: HikariConfig) : HikariDataSource(config) {
    override fun close() {
        shutdownHooks.forEach(Runnable::run)
        super.close()
    }
}
