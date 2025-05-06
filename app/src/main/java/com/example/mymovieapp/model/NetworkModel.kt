package com.example.mymovieapp.model

import com.example.mymovieapp.utils.GlobalConstant
import com.google.gson.JsonObject

class NetworkModel private constructor(
    val requestType: String?,
    val api: String?,
    val paramsMap: HashMap<String, String>?,
    val paramsMapAny: HashMap<String, Any>?,
    val jsnParam: JsonObject?,
    val commandId: Int,
    val endPointID1: String?,
    val methodType: String?,
    val hostName: String?,
) {
    class Builder {
        private var requestType: String? = null
        private var api: String? = null
        private var paramsMap: HashMap<String, String>? = null
        private var paramsMapAny: HashMap<String, Any>? = null
        private var jsnParam: JsonObject? = null
        private var commandId: Int = 0
        private var endPointID1: String? = null
        private var methodType: String? = "POST"
        private var hostName: String? = "https://base.url"

        fun setRequestType(val_: String) = apply { requestType = val_ }
        fun setApi(val_: String) = apply { api = val_ }
        fun setParamsMap(val_: HashMap<String, String>) = apply { paramsMap = val_ }
        fun setParamsMapAny(val_: HashMap<String, Any>) = apply { paramsMapAny = val_ }
        fun setJsonParam(val_: JsonObject) = apply { jsnParam = val_ }
        fun setCommandId(val_: Int) = apply { commandId = val_ }
        fun setEndPointID1(val_: String) = apply { endPointID1 = val_ }
        fun setMethodType(val_: String) = apply { methodType = val_ }
        fun setHostName(val_: String) = apply { hostName = val_ }

        fun build() : NetworkModel{
            // Ensure base URL ends with slash
            val safeHost = if (hostName?.endsWith("/") == true) hostName else "$hostName/"
            // Ensure endpoint does not start with slash
            val safeEndpoint = endPointID1?.removePrefix("/") ?: ""

            // Construct final URL
            val separator = if (safeEndpoint.contains("?")) "&" else "?"
            val apiKeyPart = "api_key=${GlobalConstant.API_KEY}"

            api = if (safeEndpoint.contains("api_key")) {
                "$safeHost$safeEndpoint"
            } else {
                "$safeHost$safeEndpoint$separator$apiKeyPart"
            }
            var finalUrl = "$safeHost$safeEndpoint$separator$apiKeyPart"
            paramsMap?.let { map ->
                for ((key, value) in map) {
                    finalUrl += "&$key=$value"  // Append each parameter
                }
            }
            api = finalUrl

            return NetworkModel(
                requestType,
                api,
                paramsMap,
                paramsMapAny,
                jsnParam,
                commandId,
                endPointID1,
                methodType,
                hostName
            )
        }
    }
}


