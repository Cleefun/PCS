package `fun`.clee.pcs

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import `fun`.clee.pcs.ip.IpUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.parse_button).setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                findViewById<EditText>(R.id.hostname_text).text.toString().run {
                    val ipAddress = IpUtils.getIpByHostname(this)
                    GlobalScope.launch(Dispatchers.Main) {
                        findViewById<TextView>(R.id.parse_result).text = "Parse result: $ipAddress"
                    }
                }
            }
        }

        findViewById<Button>(R.id.get_ip_button).setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val ipAddress4 = IpUtils.getPublicIpAddress4()
                val ipAddress6 = IpUtils.getPublicIpAddress6()
                GlobalScope.launch(Dispatchers.Main) {
                    findViewById<TextView>(R.id.parse_result).text =
                        "IPv4: $ipAddress4\nIPv6: $ipAddress6"
                }
            }
        }
    }
}