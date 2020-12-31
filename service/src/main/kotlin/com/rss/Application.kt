package com.rss

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import java.util.*

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
open class Application

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    SpringApplication.run(Application::class.java, *args)
}