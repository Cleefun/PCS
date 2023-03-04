package `fun`.clee.pcs.dns.model

/**
 * Request result model.
 *
 * @author Flente
 * @date 2023-03-04
 */
data class Result<T>(
    val success: Boolean,
    val errorCode: String? = null,
    val errorMessage: String? = null,
    val data: T? = null
) {
    companion object {
        /**
         * Convenient function to create a success result.
         */
        @JvmStatic
        fun <T> success(data: T): Result<T> =
            Result(
                success = true,
                data = data
            )

        /**
         * Convenient function to create a failure result.
         */
        @JvmStatic
        fun <T> failed(errorCode: String, errorMessage: String?) =
            Result<T>(
                success = false,
                errorCode = errorCode,
                errorMessage = errorMessage
            )
    }
}
