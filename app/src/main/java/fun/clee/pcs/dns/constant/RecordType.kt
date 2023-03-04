package `fun`.clee.pcs.dns.constant

/**
 * Valid record type.
 *
 * @author Flente
 * @date 2023-03-04
 */
object RecordType {
    const val IPV4: String = "A"
    const val IPV6: String = "AAAA"

    /**
     * Judge the type specified valid or not.
     */
    @JvmStatic
    fun isTypeValid(type: String?): Boolean {
        return type != null && (IPV4 == type || IPV6 == type)
    }
}