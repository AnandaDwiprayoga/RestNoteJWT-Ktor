package com.pasukanlangit.repository

import com.pasukanlangit.data.table.NoteTable
import com.pasukanlangit.data.table.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


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
            jdbcUrl = System.getenv("DATABASE_URL")
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()

            return HikariDataSource(this)
        }
    }
}