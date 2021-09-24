package com.pasukanlangit.utils

object Constants {
    const val API_VERSION = "/v1"
    private const val USERS_ENDPOINT = "$API_VERSION/users"
    const val USERS_REGISTER_REQUEST = "$USERS_ENDPOINT/register"
    const val USERS_LOGIN_REQUEST = "$USERS_ENDPOINT/login"
    const val NOTES_ENDPOINT = "$API_VERSION/notes"
    const val NOTE_ENDPOINT = "$API_VERSION/note"

    const val AUTH_JWT_NAME = "jwt"
    const val JWT_REALM = "note server"
    const val JWT_CLAIM_EMAIL = "email"
}