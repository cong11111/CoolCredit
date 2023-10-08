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

    private fun parseJson(integrityToken: String) {
//        OfflineVerify.process(integrityToken)
        val verificationKeyStr =
            "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEWyZRDumYDWZ8PWfIK+4TxDQ+Ge0yNIea+QS3eSCoLx+DNlywy0I+fUR7lC8WD2/oD21KhIl+h4PXxKhZHEImgA=="
        val base64OfEncodedVerificationKey = verificationKeyStr.toByteArray()
        val decryptionKeyStr = "mXdGyKnQnwL0y3z0G2hDNiP+luhUqlCTo1Bytp+Fgp4="
        val base64OfEncodedDecryptionKey = decryptionKeyStr.toByteArray()
        // base64OfEncodedDecryptionKey is provided through Play Console.
        var decryptionKeyBytes: ByteArray =
            Base64.decode(base64OfEncodedDecryptionKey, Base64.DEFAULT)
// Deserialized encryption (symmetric) key.
        var decryptionKey: SecretKey = SecretKeySpec(
            decryptionKeyBytes,
            0, decryptionKeyBytes.size, "AES"
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
        val jws: JsonWebSignature =
            JsonWebStructure.fromCompactSerialization(compactJws) as JsonWebSignature
        jws.key = verificationKey
// This also verifies the signature.
        val payload: String = jws.payload
        Log.e("SafetyNetMgr", "token 33333333333 = $payload")
    }

    //  https://developer.android.com/google/play/integrity/verdicts?hl=zh-cn
//   ----------- appRecognitionVerdict 应用完整性 三个状态--------
//    PLAY_RECOGNIZED
//    应用和证书与 Google Play 分发的版本相符。
//    UNRECOGNIZED_VERSION
//    证书或软件包名称与 Google Play 记录不符。
//    UNEVALUATED
//    未评估应用完整性。未达成必要条件，例如设备不够可信。


//   ----------- deviceRecognitionVerdict 设备完整性 两个状态--------
//    MEETS_DEVICE_INTEGRITY
//    应用正在搭载了 Google Play 服务的 Android 设备上运行。设备通过了系统完整性检查，并且满足 Android 兼容性要求。
//    无标签（空白值）
//    运行应用的设备存在遭受攻击（如 API 挂接）或系统被入侵（如取得 root 权限后入侵）的迹象，或者不是实体设备（如未通过 Google Play 完整性检查的模拟器）
// 如果您选择在完整性判定结果中接收其他标签，则 deviceRecognitionVerdict 可能会具有以下额外标签:
//MEETS_BASIC_INTEGRITY
//应用正在已通过基本系统完整性检查的设备上运行。设备可能不满足 Android 兼容性要求，也可能未被批准运行 Google Play 服务。例如，设备运行的可能是无法识别的 Android 版本、设备的引导加载程序可能已遭解锁，或者设备可能没有经过制造商的认证。
//MEETS_STRONG_INTEGRITY
//应用正在搭载了 Google Play 服务且具有强有力的系统完整性保证（如由硬件提供支持的启动完整性保证）的 Android 设备上运行。设备通过了系统完整性检查，并且满足 Android 兼容性要求。
//此外，如果您的应用将向获得批准的模拟器发布，deviceRecognitionVerdict 可能还会具有以下标签：
//
//MEETS_VIRTUAL_INTEGRITY
//应用正在搭载了 Google Play 服务的 Android 模拟器上运行。模拟器通过了系统完整性检查，并且满足核心 Android 兼容性要求。



// ------------ appLicensingVerdict------------
//    LICENSED
//    用户拥有应用使用权。换句话说，用户从 Google Play 安装或购买了您的应用。
//    UNLICENSED
//    用户没有应用使用权。例如，如果用户旁加载了您的应用，或不是通过 Google Play 获取您的应用，就会发生这种情况。
//    UNEVALUATED
//    由于未达成必要条件，系统未能评估许可详情。
//
//    导致这种情况的原因可能有多种，其中包括以下原因：
//
//    设备不够可信。
//    设备上安装的应用是 Google Play 未知的版本。
//    用户未登录 Google Play。

    /**
     * "requestDetails": {
    "requestPackageName": "com.cash.loan.kudicredit",
    "timestampMillis": "1696753454918",
    "nonce": "09ebdab1-07c8-41fd-b7bf-c9f2f0e7d0a8_169675345151w\u003d\u003d"
    },
    "appIntegrity": {
    "appRecognitionVerdict": "UNRECOGNIZED_VERSION",
    "packageName": "com.cash.loan.kudicredit",
    "certificateSha256Digest": ["nDMqG1wVE6oVM7pDc5pLwCLYyC5cLHxx3C4E9TEmLiw"],
    "versionCode": "30100"
    },
    "deviceIntegrity": {
    "deviceRecognitionVerdict": ["MEETS_DEVICE_INTEGRITY"]
    },
    "accountDetails": {
    "appLicensingVerdict": "UNEVALUATED"
    }
    }
     */
}
