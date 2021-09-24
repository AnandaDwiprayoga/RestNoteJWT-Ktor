package com.pasukanlangit.routes

import com.pasukanlangit.data.model.Note
import com.pasukanlangit.data.model.SimpleResponse
import com.pasukanlangit.data.model.User
import com.pasukanlangit.repository.MainRepo
import com.pasukanlangit.utils.Constants.AUTH_JWT_NAME
import com.pasukanlangit.utils.Constants.NOTES_ENDPOINT
import com.pasukanlangit.utils.Constants.NOTE_ENDPOINT
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.noteRoute(){
    authenticate(AUTH_JWT_NAME){

        //GET ALL NOTES BY EMAIL USER
        get(NOTES_ENDPOINT){
            try {
                val email = call.principal<User>()?.email
                if(!email.isNullOrEmpty()){
                    val notes = MainRepo.getAllNotes(email)
                    call.respond(HttpStatusCode.OK, notes)
                }else{
                    call.respond(HttpStatusCode.Forbidden, SimpleResponse(false, "Authentication failed"))
                }
            }catch(e: Exception){
                call.respond(HttpStatusCode.Conflict, emptyList<Note>())
            }
        }

        route(NOTE_ENDPOINT){
            //CRATE NOTE
            post {
                val note = try {
                    call.receive<Note>()
                }catch (e: Exception){
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing some fields"))
                    return@post
                }

                try {
                    val email = call.principal<User>()?.email
                    if(!email.isNullOrEmpty()){
                        MainRepo.addNote(note,email)
                        call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Successfully Added"))
                    }else{
                        call.respond(HttpStatusCode.Forbidden, SimpleResponse(false, "Authentication failed"))
                    }
                }catch (e: Exception){
                    call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred"))
                }
            }

            //UPDATE NOTE
            put {
                val note = try {
                    call.receive<Note>()
                }catch (e: Exception){
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing some fields"))
                    return@put
                }

                if(note.id == 0){
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Note id required"))
                    return@put
                }

                try {
                    val email = call.principal<User>()?.email
                    if(!email.isNullOrEmpty()){
                        val effectedRow = MainRepo.updateNote(note,email)
                        if(effectedRow == 0){
                           call.respond(HttpStatusCode.NotFound, SimpleResponse(false, "Note id not found"))
                        }else{
                            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Successfully Updated"))
                        }
                    }else{
                        call.respond(HttpStatusCode.Forbidden, SimpleResponse(false, "Authentication failed"))
                    }
                }catch (e: Exception){
                    call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred"))
                }
            }

            delete {
                val noteId = call.request.queryParameters["id"]
                if(!noteId.isNullOrEmpty()){
                    try {
                        val email = call.principal<User>()?.email
                        if(!email.isNullOrEmpty()){
                            val effectedRow = MainRepo.deleteNote(noteId.toInt(),email)
                            if(effectedRow == 0){
                                call.respond(HttpStatusCode.NotFound, SimpleResponse(false, "Note id not found"))
                            }else{
                                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Successfully Deleted"))
                            }

                        }else{
                            call.respond(HttpStatusCode.Forbidden, SimpleResponse(false, "Authentication failed"))
                        }
                    }catch (e: Exception){
                        call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred"))
                    }
                }else{
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Required id note"))
                }
            }
        }
    }
}