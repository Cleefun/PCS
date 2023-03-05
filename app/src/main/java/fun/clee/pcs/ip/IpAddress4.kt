@file:OptIn(ExperimentalUnsignedTypes::class)

package `fun`.clee.pcs.ip

/**
 * IPV4 implementation of [IpAddress]
 *
 * @author Flente
 * @date 2023-03-05
 */
class IpAddress4 private constructor(private val address: UByteArray) : IpAddress {
    companion object {
        @JvmStatic
        fun create(address: String?): IpAddress4? {
            return address?.split('.')?.let {
                if (it.size != 4) {
                    return null
                }
                it.mapNotNull { str -> str.toUByteOrNull() }
            }?.let {
                create(it.toUByteArray())
            }
        }

        @JvmStatic
        fun create(address: UByteArray?): IpAddress4? {
            return address?.let {
                if (it.size != 4) {
                    return null
                }
                IpAddress4(it)
            }
        }
    }

    override fun getHostAddress(): String {
        return "${address[0]}.${address[1]}.${address[2]}.${address[3]}"
    }

    override fun isMulticastAddress(): Boolean {
        // 224.0.0.0/4
        return address[0].toInt() and 0xf0 == 224
    }

    override fun isAnyLocalAddress(): Boolean {
        // 0.0.0.0
        return address.all { it.toInt() == 0 }
    }

    override fun isLoopbackAddress(): Boolean {
        // 127.0.0.0/8
        return address[0].toInt() == 127
    }

    override fun isLinkLocalAddress(): Boolean {
        // 169.254.0.0/16
        return address[0].toInt() == 169
            && address[1].toInt() == 254
    }

    override fun isSiteLocalAddress(): Boolean {
        // 10.0.0.0/8
        // 172.16.0.0/12
        // 192.168.0.0/16
        return address[0].toInt() == 10
            || address[0].toInt() == 172 && address[1].toInt() and 0xf0 == 16
            || address[0].toInt() == 192 && address[1].toInt() == 168
    }

    override fun equals(other: Any?): Boolean {
        if (other !is IpAddress4) {
            return false
        }
        if (address.size != 4 || other.address.size != 4) {
            return false
        }
        for ((index, num) in address.withIndex()) {
            if (num != other.address[index]) {
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        return address.hashCode()
    }

    override fun toString(): String {
        return getHostAddress()
    }
}