package uz.mobilDev.my_library

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.my_library.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import uz.mobilDev.internetspeed.InternetSpeedUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            btnPress.setOnClickListener {
                lifecycleScope.launch {
                    InternetSpeedUtil.measureSpeed(
                        context = this@MainActivity,
                        minSpeedKbps = 100,
                        onSpeedMeasured = { speed ->
                            Toast.makeText(this@MainActivity, "Internet tezligi: $speed kbps", Toast.LENGTH_SHORT).show()
                        },
                        onLowSpeedDetected = { lowSpeed ->
                            // Internet past bo'lsa shu yerda Toast chiqaramiz
                            Toast.makeText(this@MainActivity, "Internet past: $lowSpeed kbps", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}