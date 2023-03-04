package `fun`.clee.pcs.dns

import `fun`.clee.pcs.dns.model.AddRecordRequest
import `fun`.clee.pcs.dns.model.RecordItem
import `fun`.clee.pcs.dns.model.Result

interface DnsApi {
    /**
     * Get all valid domains.
     *
     * @return domain list
     */
    fun getAllDomains(): Result<List<String>>

    /**
     * Get all records of the domain specified.
     *
     * @param domain domain name
     * @return Result<List<RecordItem>>
     */
    fun getAllRecords(domain: String): Result<List<RecordItem>>

    /**
     * Get information of an exist record
     *
     * @param domain Domain of the target record.
     * @param recordId ID of the target record.
     * @return Return a [RecordItem] if success.
     */
    fun getRecord(domain: String, recordId: String): Result<RecordItem>

    /**
     * Add a new record.
     *
     * @param request [AddRecordRequest]
     * @return Return a [RecordItem] if success.
     */
    fun addRecord(request: AddRecordRequest): Result<RecordItem>

    /**
     * Delete an exist record.
     *
     * @param domain Domain of the target record.
     * @param recordId ID of the target record.
     * @return Result of deletion.
     */
    fun deleteRecord(domain: String, recordId: String): Result<Boolean>

    /**
     * Modify all information of an exist record.
     *
     * @param request [RecordItem]
     * @return Return a new RecordItem if success.
     */
    fun modifyRecord(request: RecordItem): Result<RecordItem>

    /**
     * Modify status of an exist record.
     *
     * @param domain Domain of the target record.
     * @param recordId ID of the target record.
     * @param status true  - enable, false - disable
     */
    fun modifyRecordStatus(
        domain: String,
        recordId: String,
        status: Boolean,
    ): Result<Boolean>

    /**
     * Modify value of an exist record.
     *
     * @param domain Domain of the target record.
     * @param recordId ID of the target record.
     * @param value Value of the target record.
     */
    fun modifyRecordValue(
        domain: String,
        recordId: String,
        value: String,
    ): Result<Boolean>
}