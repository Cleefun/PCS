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
                        list.mapNotNull { str -> str.toUShortOrNull(16) }
                    }
                }

                val left = it[0].split(':')
                if (left.size > 6) {
                    return null
                }
                val right = it[1].split(':')
                if (left.size + right.size > 7) {
                    return null
                }
                MutableList(8) { "0" }.apply {
                    for ((index, str) in left.withIndex()) {
                        this[index] = str.ifEmpty { "0" }
                    }
                    for ((index, str) in right.reversed().withIndex()) {
                        this[7 - index] = str.ifEmpty { "0" }
                    }
                }.mapNotNull { str -> str.toUShortOrNull(16) }
            }?.let {
                create(it.toUShortArray())
            }
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

    override fun getHostAddress(): String {
        return address.joinToString(separator = ":") { it.toString(16) }
    }

    override fun isMulticastAddress(): Boolean {
        return address[0].toInt() and 0xff00 == 0xff00
    }

    override fun isAnyLocalAddress(): Boolean {
        return address.all { it.toInt() == 0x0 }
    }

    override fun isLoopbackAddress(): Boolean {
        return address[7].toInt() == 0x1
    }

    override fun isLinkLocalAddress(): Boolean {
        return address[0].toInt() and 0xffc0 == 0xfe80
    }

    override fun isSiteLocalAddress(): Boolean {
        return address[0].toInt() and 0xffc0 == 0xfec0
    }

    override fun equals(other: Any?): Boolean {
        if (other !is IpAddress6) {
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