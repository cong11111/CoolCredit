package com.tiny.cash.loan.card.bean.integrity

class IntergrityResponse {
//    * "requestDetails": {
//        "requestPackageName": "com.cash.loan.kudicredit",
//        "timestampMillis": "1696753454918",
//        "nonce": "09ebdab1-07c8-41fd-b7bf-c9f2f0e7d0a8_169675345151w\u003d\u003d"
//    },
//    "appIntegrity": {
//        "appRecognitionVerdict": "UNRECOGNIZED_VERSION",
//        "packageName": "com.cash.loan.kudicredit",
//        "certificateSha256Digest": ["nDMqG1wVE6oVM7pDc5pLwCLYyC5cLHxx3C4E9TEmLiw"],
//        "versionCode": "30100"
//    },
//    "deviceIntegrity": {
//        "deviceRecognitionVerdict": ["MEETS_DEVICE_INTEGRITY"]
//    },
//    "accountDetails": {
//        "appLicensingVerdict": "UNEVALUATED"
//    }
//}
    var requestDetails : RequestDetail? = null
    var appIntegrity : AppIntegrity? = null
    var deviceIntegrity : DeviceIntegrity? = null
    var accountDetails : AccountDetails? = null

    class RequestDetail {
        var requestPackageName : String? = null
        var timestampMillis : String? = null
        var nonce : String? = null
    }

    class AppIntegrity {
        var appRecognitionVerdict : String? = null
        var packageName : String? = null
        var certificateSha256Digest : List<String>? = null
        var versionCode : String? = null
    }

    class DeviceIntegrity {
        var deviceRecognitionVerdict : List<String>? = null
    }

    class AccountDetails {
        var appLicensingVerdict : String? = null
    }
}