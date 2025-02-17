package uz.mobilDev.internetspeed

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.net.URLConnection

object InternetSpeedUtil {

    suspend fun measureSpeed(
        context: Context,
        minSpeedKbps: Int,
        onSpeedMeasured: (Int) -> Unit,
        onLowSpeedDetected: (Int) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val startTime = System.currentTimeMillis()
                val connection: URLConnection = URL("https://www.google.com").openConnection()
                connection.connect()
                connection.getInputStream().use { inputStream ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    var totalBytes = 0

                    while (inputStream.read(buffer).also { bytesRead = it } != -1 && totalBytes < 1024 * 1024) {
                        totalBytes += bytesRead
                    }

                    val endTime = System.currentTimeMillis()
                    val duration = (endTime - startTime) / 1000.0
                    val speedKbps = ((totalBytes / 1024) / duration).toInt()

                    withContext(Dispatchers.Main) {
                        onSpeedMeasured(speedKbps)
                        if (speedKbps < minSpeedKbps) {
                            onLowSpeedDetected(speedKbps)
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Internet tezligini o'lchashda xato: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}