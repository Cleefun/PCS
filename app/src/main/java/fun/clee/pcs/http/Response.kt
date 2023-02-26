package `fun`.clee.pcs.http

data class Response<T>(
    val code: Int = CODE_SUCCESS,
    val message: String? = null,
    val data: T? = null
) {
   companion object {
       const val CODE_SUCCESS = 0
       const val CODE_HTTP_ERROR = 1
       const val CODE_UNKNOWN_HOST = 2
       const val CODE_TIME_OUT = 3
       const val CODE_IO_ERROR = 4
       const val CODE_OTHER_ERROR = 999
   }
}
