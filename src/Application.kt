package com.pasukanlangit

import com.pasukanlangit.authentication.JWTService
import com.pasukanlangit.data.model.SimpleResponse
import com.pasukanlangit.repository.DatabaseFactory
import com.pasukanlangit.repository.MainRepo
import com.pasukanlangit.routes.noteRoute
import com.pasukanlangit.routes.userRegisterRoutes
import com.pasukanlangit.utils.Constants.API_VERSION
import com.pasukanlangit.utils.Constants.AUTH_JWT_NAME
import com.pasukanlangit.utils.Constants.JWT_CLAIM_EMAIL
import com.pasukanlangit.utils.Constants.JWT_REALM
import com.pasukanlangit.utils.hash
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.gson.*
import io.ktor.features.*
import javax.naming.AuthenticationException

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    DatabaseFactory.init()
    val jwtService = JWTService()
    val hashFunction = {s: String -> hash(s)}


    //Dependency for JWT
    install(Authentication) {
        jwt(AUTH_JWT_NAME) {
            verifier(jwtService.verifier)
            realm = JWT_REALM

            validate {
                val payload = it.payload
                val email = payload.getClaim(JWT_CLAIM_EMAIL).asString()
                val user = MainRepo.findUserByEmail(email)
                user
            }

        }
    }

    //Dependency for convert model to json, or use kotlinx.serialization
    install(ContentNegotiation) { gson {} }

    routing {
        get("$API_VERSION/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        userRegisterRoutes(jwtService,hashFunction)
        noteRoute()

    }
}

