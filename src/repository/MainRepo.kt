package com.pasukanlangit.repository

import com.pasukanlangit.data.model.Note
import com.pasukanlangit.data.model.User
import com.pasukanlangit.data.table.NoteTable
import com.pasukanlangit.data.table.UserTable
import com.pasukanlangit.utils.convertRowToNote
import com.pasukanlangit.utils.convertRowToUser
import com.pasukanlangit.utils.dbQuery
import org.jetbrains.exposed.sql.*

object MainRepo {
    //FOR USER STUFF
    suspend fun addUser(user: User){
        dbQuery {
            UserTable.insert { usr_table ->
                usr_table[email] = user.email
                usr_table[hashPassword] = user.hashPassword
                usr_table[name] = user.username
            }
        }
    }

    suspend fun findUserByEmail(email: String) =
        dbQuery {
            UserTable.select { UserTable.email.eq(email)}
                .map { convertRowToUser(it) }
                .singleOrNull()
        }

    //FOR NOTE STUFF
    suspend fun addNote(note: Note, email: String){
        dbQuery {
            NoteTable.insert { note_table ->
                note_table[userEmail] = email
                note_table[title] = note.title
                note_table[description] = note.description
                note_table[date] = note.date
            }
        }
    }

    suspend fun getAllNotes(email: String)=
        dbQuery {
            NoteTable
                .select { NoteTable.userEmail.eq(email) }
                .mapNotNull { convertRowToNote(it) }
        }

    suspend fun updateNote(note: Note, email: String): Int =
        dbQuery {
             NoteTable.update(
                where =  {
                    NoteTable.id.eq(note.id) and NoteTable.userEmail.eq(email)
                }
            ){ note_table ->
                note_table[title] = note.title
                note_table[description] = note.description
                note_table[date] = note.date
            }
        }


    suspend fun deleteNote(noteId: Int, email: String): Int =
        dbQuery {
            NoteTable.deleteWhere { NoteTable.id.eq(noteId) and NoteTable.userEmail.eq(email) }
        }

}