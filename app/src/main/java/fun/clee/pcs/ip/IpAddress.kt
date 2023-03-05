package `fun`.clee.pcs.ip

/**
 * IP address model.
 *
 * @author Flente
 * @date 2023-03-05
 */
interface IpAddress {
    fun getHostAddress(): String

    fun isPublicAddress(): Boolean {
        return !isAnyLocalAddress()
            && !isLoopbackAddress()
            && !isLinkLocalAddress()
            && !isSiteLocalAddress()
            && !isMulticastAddress()
    }

    fun isMulticastAddress(): Boolean

    fun isAnyLocalAddress(): Boolean

    fun isLoopbackAddress(): Boolean

    fun isLinkLocalAddress(): Boolean

    fun isSiteLocalAddress(): Boolean
}