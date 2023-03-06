@file:OptIn(ExperimentalUnsignedTypes::class)

package `fun`.clee.pcs.ip

/**
 * IPV6 implementation of [IpAddress]
 *
 * @author Flente
 * @date 2023-03-05
 */
class IpAddress6 private constructor(private val address: UShortArray) : IpAddress {
    companion object {
        @JvmStatic
        fun create(address: String?): IpAddress6? {
            return address?.split("::")?.let {
                if (it.size > 2) {
                    return null
                }

                if (it.size == 1) {
                    return@let it[0].split(':').let {  list ->
                        if (list.size != 8) {
                            return null
                        }
                        list.map { str ->
                            str.toUShortOrNull(16) ?: return null
                        }.toUShortArray()
                    }
                }

                // case: more than 2 consecutive ":"
                if (it[0].startsWith(':') || it[0].endsWith(':')
                    || it[1].startsWith(':') || it[1].endsWith(':')) {
                    return null
                }

                val leftStrs = it[0].split(':')
                if (leftStrs.size > 6) {
                    return null
                }
                val rightStrs = it[1].split(':')

                // case 1: "::" on the edge, max size should <=7, (x:x:x:x:x:x:: or ::x:x:x:x:x:x)
                // case 2: "::" is inside, max size should <=6, (x:x:x:x:x::x)
                if (((it[0].isEmpty() || it[1].isEmpty()) && leftStrs.size + rightStrs.size > 7)
                    || !(it[0].isEmpty() || it[1].isEmpty()) && leftStrs.size + rightStrs.size > 6) {
                    return null
                }

                // String map to Short
                val leftShorts = leftStrs.map { str ->
                    if (str.isEmpty()) 0u else str.toUShortOrNull(16) ?: return null
                }
                val rightShorts = rightStrs.map { str ->
                    if (str.isEmpty()) 0u else str.toUShortOrNull(16) ?: return null
                }

                // add the extra 0 and transform to UShortArray
                UShortArray(8) { 0u }.apply {
                    for ((index, number) in leftShorts.withIndex()) {
                        this[index] = number
                    }
                    for ((index, number) in rightShorts.reversed().withIndex()) {
                        this[7 - index] = number
                    }
                }
            }?.let { create(it) }
        }

        @JvmStatic
        fun create(address: UShortArray?): IpAddress6? {
            return address?.let {
                if (it.size != 8) {
                    return null
                }
                IpAddress6(it)
            }
        }
    }

    private val hostAddress = address.joinToString(separator = ":") { it.toString(16) }

    override fun getHostAddress(): String {
        return hostAddress
    }

    override fun isMulticastAddress(): Boolean {
        return address[0].toUInt() and 0xff00u == 0xff00u
    }

    override fun isAnyLocalAddress(): Boolean {
        return address.all { it.toUInt() == 0x0u }
    }

    override fun isLoopbackAddress(): Boolean {
        return address.sum() == 1u && address[7].toUInt() == 0x1u
    }

    override fun isLinkLocalAddress(): Boolean {
        return address[0].toUInt() and 0xffc0u == 0xfe80u
    }

    override fun isSiteLocalAddress(): Boolean {
        return address[0].toUInt() and 0xffc0u == 0xfec0u
    }

    override fun equals(other: Any?): Boolean {
        if (other !is IpAddress6) {
            return false
        }
        return hostAddress == other.hostAddress
    }

    override fun hashCode(): Int {
        return hostAddress.hashCode()
    }

    override fun toString(): String {
        return hostAddress
    }
}