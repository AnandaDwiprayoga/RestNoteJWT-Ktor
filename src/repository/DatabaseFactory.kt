package com.pasukanlangit.repository

import com.pasukanlangit.data.table.NoteTable
import com.pasukanlangit.data.table.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI


object DatabaseFactory {
    fun init() {
       Database.connect(hikari())

        transaction {
            //create table programmatically
            SchemaUtils.create(UserTable)
            SchemaUtils.create(NoteTable)
        }
    }

    private fun hikari(): HikariDataSource {
        HikariConfig().apply {
            driverClassName = System.getenv("JDBC_DRIVER")
            jdbcUrl = this@DatabaseFactory.getJdbcUrl()
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()

            return HikariDataSource(this)
        }
    }

    private fun getJdbcUrl(): String {
        //use this in localhost
//        return System.getenv("DATABASE_URL")

        //use this to get env heroku
        val uri = URI(System.getenv("DATABASE_URL"))
        val username = uri.userInfo.split(":").toTypedArray()[0]
        val password = uri.userInfo.split(":").toTypedArray()[1]
        return "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}?sslmode=require&user=$username&password=$password"
    }
}