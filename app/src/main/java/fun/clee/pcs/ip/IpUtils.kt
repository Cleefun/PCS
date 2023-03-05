@file:OptIn(ExperimentalUnsignedTypes::class)

package `fun`.clee.pcs.ip

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
    /**
     * Get all [IpAddress], include v4 and v6.
     */
    fun getIpAddresses(): List<IpAddress> {
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
}