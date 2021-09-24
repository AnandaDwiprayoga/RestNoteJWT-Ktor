package com.pasukanlangit.utils

import com.pasukanlangit.data.model.Note
import com.pasukanlangit.data.model.User
import com.pasukanlangit.data.table.NoteTable
import com.pasukanlangit.data.table.UserTable
import org.jetbrains.exposed.sql.ResultRow

fun convertRowToUser(row: ResultRow?): User? =
    if(row == null){
        null
    }else{
        User(
            email = row[UserTable.email],
            hashPassword = row[UserTable.hashPassword],
            username = row[UserTable.name]
        )
    }

fun convertRowToNote(row: ResultRow?): Note? =
    if(row == null){
        null
    }else{
        Note(
            id = row[NoteTable.id],
            userEmail = row[NoteTable.userEmail],
            title = row[NoteTable.title],
            description = row[NoteTable.description],
            date = row[NoteTable.date]
        )
    }