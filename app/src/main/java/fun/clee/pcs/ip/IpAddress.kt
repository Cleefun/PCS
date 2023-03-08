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

    companion object {
        fun create(hostAddress: String?): IpAddress? {
            if (hostAddress == null) {
                return null
            }
            if (hostAddress.contains('.')) {
                return IpAddress4.create(hostAddress)
            }
            if (hostAddress.contains(':')) {
                return IpAddress6.create(hostAddress)
            }
            return null
        }
    }
}