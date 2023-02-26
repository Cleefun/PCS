package `fun`.clee.pcs.dns.dnspod.model

import com.google.gson.annotations.SerializedName

open class ErrorResponse(
    @SerializedName("RequestId") val requestId: String,
    @SerializedName("Error") val errorInfo: ErrorInfo
)
