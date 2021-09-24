package com.pasukanlangit.utils

import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

suspend fun <T> dbQuery(block: () -> T): T =
    withContext(Dispatchers.IO){
        transaction { block() }
    }

private val hashKey = System.getenv("HASH_SECRET_KEY").toByteArray()
private const val hMacAlgorithm = "HmacSHA1"
private val hMacKey = SecretKeySpec(hashKey, hMacAlgorithm)

fun hash(password: String): String{
    val hMac = Mac.getInstance(hMacAlgorithm)
    hMac.init(hMacKey)
    return hex(hMac.doFinal(password.toByteArray(Charsets.UTF_8)))
}

