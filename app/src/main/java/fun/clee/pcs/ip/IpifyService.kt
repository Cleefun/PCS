package `fun`.clee.pcs.ip

import retrofit2.Call
import retrofit2.http.GET

/**
 * IP query services provided by www.ipify.org
 *
 * @author Flente
 * @date 2023-03-08
 */
interface IpifyService {
    @GET("?format=json")
    fun getPublicIpAddress(): Call<IpifyResponse>
}