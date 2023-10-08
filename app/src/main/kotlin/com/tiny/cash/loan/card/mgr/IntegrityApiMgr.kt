package com.tiny.cash.loan.card.mgr

import android.app.Activity
import android.util.Base64
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.google.android.play.core.integrity.IntegrityTokenResponse
import org.jose4j.jwe.JsonWebEncryption
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwx.JsonWebStructure
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.UUID
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


object IntegrityApiMgr {
//
    fun getId(context: Activity) {
        val nonce: String = "" + UUID.randomUUID() + "_" + System.currentTimeMillis()
// Create an instance of a manager.
        val integrityManager =
            IntegrityManagerFactory.create(context.applicationContext)
// Request the integrity token by providing a nonce.
        val integrityTokenResponse: Task<IntegrityTokenResponse> =
            integrityManager.requestIntegrityToken(
                IntegrityTokenRequest.builder()
                    .setNonce(nonce)
                    .build()
            )
        Log.e("SafetyNetMgr", "start get id =")
        integrityTokenResponse.addOnSuccessListener(context) {
            // Indicates communication with the service was successful.
                val token = it.token()
                Log.e("SafetyNetMgr", "token 111111111 = $token")
                parseJson(token)
             }
            .addOnFailureListener(context) { e ->
                // An error occurred while communicating with the service.
                if (e is ApiException) {
                    val apiException = e as ApiException
                } else {
                    // A different, unknown type of error occurred.
                    Log.d("SafetyNetMgr", "Error: " + e.message)
                }
            }
    }

    private fun parseJson(integrityToken : String){
//        OfflineVerify.process(integrityToken)
        val base64OfEncodedVerificationKey = "11111111".toByteArray()
        val base64OfEncodedDecryptionKey = "22222222".toByteArray()
        // base64OfEncodedDecryptionKey is provided through Play Console.
        var decryptionKeyBytes: ByteArray =
            Base64.decode(base64OfEncodedDecryptionKey, Base64.DEFAULT)
// Deserialized encryption (symmetric) key.
        var decryptionKey: SecretKey = SecretKeySpec(decryptionKeyBytes,
            0, decryptionKeyBytes.size,"AES"
        )
//        decryptionKeyBytes.length,
        // base64OfEncodedVerificationKey is provided through Play Console.
        val encodedVerificationKey: ByteArray =
            Base64.decode(base64OfEncodedVerificationKey, Base64.DEFAULT)
        // Deserialized verification (public) key.
        val verificationKey = KeyFactory.getInstance("EC")
            .generatePublic(X509EncodedKeySpec(encodedVerificationKey))

        val jwe: JsonWebEncryption =
            JsonWebStructure.fromCompactSerialization(integrityToken) as JsonWebEncryption
        jwe.key = decryptionKey
// This also decrypts the JWE token.
        val compactJws: String = jwe.payload
        Log.e("SafetyNetMgr", "token 2222222222 = $compactJws")
        val jws: JsonWebSignature =
            JsonWebStructure.fromCompactSerialization(compactJws) as JsonWebSignature
        jws.key = verificationKey
// This also verifies the signature.
        val payload: String = jws.payload
        Log.e("SafetyNetMgr", "token 33333333333 = $payload")
    }
}
