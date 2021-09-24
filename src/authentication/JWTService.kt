package com.pasukanlangit.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.pasukanlangit.data.model.User
import com.pasukanlangit.utils.Constants.JWT_CLAIM_EMAIL

class JWTService {
    private val issuer = "NoteServer"
    private val jwtSecret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: User): String {
        return JWT.create()
            .withSubject("NoteAuthentication")
            .withIssuer(issuer)
            .withClaim(JWT_CLAIM_EMAIL, user.email)
            .sign(algorithm)

    }
}