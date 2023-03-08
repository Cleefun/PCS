package `fun`.clee.pcs.ip

import com.google.gson.annotations.SerializedName

/**
 * Ipify service response model.
 *
 * @author Flente
 * @date 2023-03-08
 */
data class IpifyResponse(
    @SerializedName("ip")
    val ip: String,
)