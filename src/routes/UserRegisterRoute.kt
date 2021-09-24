package com.pasukanlangit.routes

import com.pasukanlangit.authentication.JWTService
import com.pasukanlangit.data.model.LoginRequest
import com.pasukanlangit.data.model.RegisterRequest
import com.pasukanlangit.data.model.SimpleResponse
import com.pasukanlangit.data.model.User
import com.pasukanlangit.repository.MainRepo
import com.pasukanlangit.utils.Constants.API_VERSION
import com.pasukanlangit.utils.Constants.USERS_LOGIN_REQUEST
import com.pasukanlangit.utils.Constants.USERS_REGISTER_REQUEST
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.userRegisterRoutes(
    jwtService: JWTService,
    hashFunction: (String) -> String
){
    get(API_VERSION){
        call.respondText("Fly to the moon!!")
    }

    post(USERS_REGISTER_REQUEST){
        val registerRequest = try {
            call.receive<RegisterRequest>()
        }catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing some fields"))
            return@post
        }

        if(registerRequest.password.isNullOrEmpty() || registerRequest.email.isNullOrEmpty() || registerRequest.username.isNullOrEmpty()){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "email, username, and password required"))
            return@post
        }

        try {
            val user = User(
                email = registerRequest.email,
                hashPassword = hashFunction(registerRequest.password),
                username = registerRequest.username
            )
            MainRepo.addUser(user)
            call.respond(HttpStatusCode.OK, SimpleResponse(true,jwtService.generateToken(user)))
        }catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred"))
        }
    }

    post(USERS_LOGIN_REQUEST){
        val loginRequest = try {
            call.receive<LoginRequest>()
        }catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Some Fields"))
            return@post
        }


        try {
            val user = MainRepo.findUserByEmail(loginRequest.email)

            if(user == null){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false, "Email not registered"))
            }else{
                if(user.hashPassword == hashFunction(loginRequest.password)){
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
                }else{
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false,"Password Incorrect!"))
                }
            }
        }catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred"))
        }
    }
}