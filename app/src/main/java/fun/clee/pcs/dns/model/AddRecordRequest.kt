package `fun`.clee.pcs.dns.model

/**
 * Request model of adding record
 *
 * @author Flente
 * @date 2023-03-04
 */
data class AddRecordRequest(
    val domain: String,
    val subDomain: String,
    val value: String,
    val type: String,
    val remark: String?
)
