package `fun`.clee.pcs.dns.impl.dnspod

import android.annotation.SuppressLint
import android.util.Log
import com.tencentcloudapi.common.Credential
import com.tencentcloudapi.common.exception.TencentCloudSDKException
import com.tencentcloudapi.dnspod.v20210323.DnspodClient
import com.tencentcloudapi.dnspod.v20210323.models.*
import `fun`.clee.pcs.dns.DnsApi
import `fun`.clee.pcs.dns.constant.RecordType
import `fun`.clee.pcs.dns.model.AddRecordRequest
import `fun`.clee.pcs.dns.model.RecordItem
import `fun`.clee.pcs.dns.model.Result
import java.text.SimpleDateFormat

/**
 * Implementation of [DnsApi] on Dnspod.
 *
 * @author Flente
 * @date 2023-03-04
 */
class DnspodApi(
    private val secretId: String,
    private val secretKey: String,
) : DnsApi {
    companion object {
        private const val TAG = "DnspodApi"

        private const val STATUS_ENABLED_STRING = "ENABLE"

        private const val STATUS_DISABLED_STRING = "DISABLE"

        private const val STATUS_ENABLED_LONG = 1L

        private const val DEFAULT_RECORD_LINE = "默认"

        private const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    private val client: DnspodClient by lazy {
        DnspodClient(Credential(secretId, secretKey), "")
    }

    override fun getAllDomains(): Result<List<String>> {
        Log.i(TAG, "getAllDomains")
        return handleException {
            client.DescribeDomainList(DescribeDomainListRequest())
                .domainList
                .filter { item -> item.status == STATUS_ENABLED_STRING }
                .map(DomainListItem::getName)
        }
    }

    override fun getAllRecords(domain: String): Result<List<RecordItem>> {
        Log.i(TAG, "getAllRecords")
        return handleException {
            val request = DescribeRecordListRequest().apply {
                this.domain = domain
            }
            client.DescribeRecordList(request)
                .recordList
                .filter { item -> RecordType.isTypeValid(item.type) }
                .map { item -> recordItemMapper(item, domain) }
        }
    }

    override fun getRecord(domain: String, recordId: String): Result<RecordItem> {
        Log.i(TAG, "getRecord")
        return handleException {
            val request = DescribeRecordRequest().apply {
                this.domain = domain
                this.recordId = recordId.toLong()
            }
            recordItemMapper(client.DescribeRecord(request).recordInfo, domain)
        }
    }

    override fun addRecord(request: AddRecordRequest): Result<RecordItem> {
        Log.i(TAG, "addRecord")
        return handleException {
            val realRequest = CreateRecordRequest().apply {
                domain = request.domain
                subDomain = request.subDomain
                recordType = request.type
                recordLine = DEFAULT_RECORD_LINE
                value = request.value
            }
            client.CreateRecord(realRequest).recordId.let { id ->
                ModifyRecordRemarkRequest().apply {
                    domain = request.domain
                    recordId = id
                    remark = request.remark
                }.let {
                    client.ModifyRecordRemark(it)
                }
                return getRecord(request.domain, id.toString())
            }
        }
    }

    override fun deleteRecord(domain: String, recordId: String): Result<Boolean> {
        Log.i(TAG, "deleteRecord")
        return handleException {
            DeleteRecordRequest().apply {
                this.domain = domain
                this.recordId = recordId.toLong()
            }.let {
                client.DeleteRecord(it)
            }
            true
        }
    }

    override fun modifyRecord(request: RecordItem): Result<RecordItem> {
        Log.i(TAG, "modifyRecord")
        return handleException {
            ModifyRecordRemarkRequest().apply {
                domain = request.domain
                recordId = request.id.toLong()
                remark = request.remark
            }.let {
                client.ModifyRecordRemark(it)
            }

            recordItemMapper(request).let {
                val response = client.ModifyRecord(it)
                return getRecord(request.domain, response.recordId.toString())
            }
        }
    }

    override fun modifyRecordStatus(
        domain: String,
        recordId: String,
        status: Boolean
    ): Result<Boolean> {
        Log.i(TAG, "modifyRecordStatus")
        return handleException {
            ModifyRecordStatusRequest().apply {
                this.domain = domain
                this.recordId = recordId.toLong()
                this.status = statusBool2String(status)
            }.let {
                client.ModifyRecordStatus(it)
            }
            true
        }
    }

    override fun modifyRecordValue(
        domain: String,
        recordId: String,
        value: String
    ): Result<Boolean> {
        Log.i(TAG, "modifyRecordValue")
        return handleException {
            ModifyDynamicDNSRequest().apply {
                this.domain = domain
                this.recordId = recordId.toLong()
                this.value = value
                this.recordLine = DEFAULT_RECORD_LINE
            }.let {
                client.ModifyDynamicDNS(it)
            }
            true
        }
    }

    /**
     * Convenient inline function for exception handling and create [Result].
     */
    private inline fun <T> handleException(block: () -> T): Result<T> {
        return try {
            block.invoke().let { Result.success(it) }
        } catch (e: TencentCloudSDKException) {
            Log.e(TAG, "$e")
            Result.failed(e.errorCode, e.message)
        }
    }

    /**
     * Map RecordListItem to RecordItem
     */
    private fun recordItemMapper(item: RecordListItem, domain: String): RecordItem {
        return RecordItem(
            item.recordId.toString(), domain, item.name, item.value, item.type,
            item.status == STATUS_ENABLED_STRING, dateString2Timestamp(item.updatedOn),
            item.remark
        )
    }

    /**
     * Map RecordInfo to RecordItem
     */
    private fun recordItemMapper(info: RecordInfo, domain: String): RecordItem {
        return RecordItem(
            info.id.toString(), domain, info.subDomain, info.value, info.recordType,
            info.enabled == STATUS_ENABLED_LONG, dateString2Timestamp(info.updatedOn),
            info.remark
        )
    }

    /**
     * Map RecordItem to ModifyRecordRequest
     */
    private fun recordItemMapper(item: RecordItem): ModifyRecordRequest {
        return ModifyRecordRequest().apply {
            recordId = item.id.toLong()
            domain = item.domain
            subDomain = item.subDomain
            value = item.value
            recordType = item.type
            recordLine = DEFAULT_RECORD_LINE
            status = statusBool2String(item.enabled)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateString2Timestamp(date: String): Long {
        return try {
            SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(date)?.time ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun statusBool2String(enabled: Boolean): String {
        return if (enabled) STATUS_ENABLED_STRING else STATUS_DISABLED_STRING
    }
}