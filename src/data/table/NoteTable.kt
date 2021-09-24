package com.pasukanlangit.data.table

import org.jetbrains.exposed.sql.Table

object NoteTable: Table() {
    val id = integer("id").uniqueIndex().autoIncrement()
    //to add foreign key use references keyword
    val userEmail = varchar("user_email", 512).references(UserTable.email)
    val title = text("title")
    val description = text("description")
    val date = long("date")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}