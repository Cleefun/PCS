package `fun`.clee.pcs.dns.dnspod.model

import com.google.gson.annotations.SerializedName

data class ErrorInfo(
    @SerializedName("Code") val code: String,
    @SerializedName("Message") val message: String
)