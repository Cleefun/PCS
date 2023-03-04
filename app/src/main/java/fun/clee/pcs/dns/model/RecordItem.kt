package `fun`.clee.pcs.dns.model

/**
 * Record item model.
 *
 * @author Flente
 * @date 2023-03-04
 */
data class RecordItem(
    val id: String,
    val domain: String,
    val subDomain: String,
    val value: String,
    val type: String,
    val enabled: Boolean,
    /**
     * Timestamp in milliseconds
     */
    val lastUpdateTime: Long,
    val remark: String?,
)
