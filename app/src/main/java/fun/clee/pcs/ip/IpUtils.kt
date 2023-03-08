@file:OptIn(ExperimentalUnsignedTypes::class)

package `fun`.clee.pcs.ip

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface

/**
 * IP related functions
 *
 * @author Flente
 * @date 2023-03-05
 */
object IpUtils {
    private const val TAG = "IpUtils";

    /**
     * Get all [IpAddress], include v4 and v6.
     */
    fun getLocalIpAddresses(): List<IpAddress> {
        return NetworkInterface.getNetworkInterfaces()
            ?.toList()
            ?.mapNotNull(NetworkInterface::getInetAddresses)
            ?.flatMap { it.asSequence() }
            ?.mapNotNull { inetAddress2IpAddress(it) }
            ?: ArrayList()
    }

    private fun inetAddress2IpAddress(address: InetAddress): IpAddress? {
        return when (address) {
            is Inet4Address -> IpAddress4.create(address.address.toUByteArray())
            is Inet6Address -> inet6Address2IpAddress6(address)
            else -> null
        }
    }

    private fun inet6Address2IpAddress6(orgAddress: Inet6Address): IpAddress? {
        return orgAddress.address.let {
            if (it.size != 16) {
                return null
            }
            val address = UShortArray(8)
            for (i in 0..14 step 2) {
                address[i / 2] = ((it[i].toUByte().toUInt() shl 8) or (it[i + 1].toUByte().toUInt()))
                    .toUShort()
            }
            IpAddress6.create(address)
        }
    }

    /**
     * Ipify service base URL
     */
    private const val IPIFY_IPV4_URL: String = "https://api.ipify.org/"
    private const val IPIFY_IPV6_URL: String = "https://api6.ipify.org/"

    fun getPublicIpAddress4(): IpAddress4? {
        return try {
            getIpifyService(IPIFY_IPV4_URL).getPublicIpAddress().execute()
                .takeIf { it.isSuccessful }
                ?.body()
                ?.let { IpAddress4.create(it.ip) }
        } catch (e: Exception) {
            Log.i(TAG, "getPublicIpAddress4 error: " + e.message)
            null
        }
    }

    fun getPublicIpAddress6(): IpAddress6? {
        return try {
            getIpifyService(IPIFY_IPV6_URL).getPublicIpAddress().execute()
                .takeIf { it.isSuccessful }
                ?.body()
                ?.let { IpAddress6.create(it.ip) }
        } catch (e: Exception) {
            Log.i(TAG, "getPublicIpAddress6 error: $e")
            null
        }
    }

    private fun getIpifyService(baseUrl: String): IpifyService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IpifyService::class.java)
    }

    fun getIpByHostname(host: String?): IpAddress? {
        if (host == null) {
            return null
        }
        return try {
            inetAddress2IpAddress(InetAddress.getByName(host))
        } catch (e: Exception) {
            Log.i(TAG, "getIpByHostname error: $e")
            null
        }
    }
}