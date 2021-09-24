package com.pasukanlangit.data.model

import io.ktor.auth.*

data class User(
    val email:String,
    val hashPassword: String,
    val username: String
): Principal
//Principal to make this model implement call.principal and get the property
