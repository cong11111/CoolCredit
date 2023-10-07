package com.tiny.cash.loan.card.mgr

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.google.android.play.core.integrity.IntegrityTokenResponse

object SafetyNetMgr {

    //    fun checkSafetyNet(context : Context) : Boolean {
//        return (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
//                == ConnectionResult.SUCCESS)
//    }
//
    fun getId(context: Activity) {
        val nonce: String = ""

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
        integrityTokenResponse.addOnSuccessListener(context) {
            // Indicates communication with the service was successful.
            // Use response.getJwsResult() to get the result data.
                val token = it.token()
                Log.e("Test", "token = " + token)
//            val jwe: JsonWebEncryption =
//                JsonWebStructure.fromCompactSerialization(integrityToken) as JsonWebEncryption
//            jwe.setKey(decryptionKey)
//
//// This also decrypts the JWE token.
//            val compactJws: String = jwe.getPayload()
//
//            val jws: JsonWebSignature =
//                JsonWebStructure.fromCompactSerialization(compactJws) as JsonWebSignature
//            jws.setKey(verificationKey)
//
//// This also verifies the signature.
//            val payload: String = jws.getPayload()
             }
            .addOnFailureListener(context) { e ->
                // An error occurred while communicating with the service.
                if (e is ApiException) {
                    val apiException = e as ApiException
                } else {
                    // A different, unknown type of error occurred.
                    Log.d("Test", "Error: " + e.message)
                }
            }
    }
}
