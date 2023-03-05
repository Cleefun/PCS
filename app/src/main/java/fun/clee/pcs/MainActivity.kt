package `fun`.clee.pcs

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import `fun`.clee.pcs.ip.IpAddress4
import `fun`.clee.pcs.ip.IpAddress6
import `fun`.clee.pcs.ip.IpUtils

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.parse_button).setOnClickListener { _ ->
            findViewById<EditText>(R.id.ip_text).text.toString().let {  ipStr ->
                if (ipStr.contains('.'))
                    IpAddress4.create(ipStr)
                else
                    IpAddress6.create(ipStr)
            }.apply {
                findViewById<TextView>(R.id.parse_result).text = "IP: ${this ?: "parse error"}\nPublic: ${this?.isPublicAddress() ?: ""}"
            }
        }

        findViewById<Button>(R.id.get_ip_button).setOnClickListener { _ ->
            val text = StringBuilder()
            IpUtils.getIpAddresses().forEach {
                text.append("IP: ${it}\nPublic: ${it.isPublicAddress()}\n")
            }
            findViewById<TextView>(R.id.parse_result).text = text
        }
    }
}