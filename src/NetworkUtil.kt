import okhttp3.*
import java.io.IOException
import java.lang.Exception

object NetworkUtil {

    private const val User_Agent = "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)"
    private val mOkHttpClient: OkHttpClient = OkHttpClient.Builder().build()

    // 异步回调
    fun get(url: String, callback: (state: Boolean, bytes: ByteArray) -> Unit) {
        val request = Request.Builder()
                .url(url)
                .removeHeader("User-Agent")
                .addHeader("User-Agent", User_Agent)
                .addHeader("Referer", "http://www.mmjpg.com/")
                .get()
                .build()

        mOkHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, e.toString().toByteArray())
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback(true, response.body().bytes())
                    return
                }
                callback(false, response.toString().toByteArray())
            }
        })
    }

    // 同步回调
    fun get(url: String): ByteArray {
        val request = Request.Builder()
                .url(url)
                .removeHeader("User-Agent")
                .addHeader("User-Agent", User_Agent)
                .addHeader("Referer", "http://www.mmjpg.com/")
                .get()
                .build()
        try {
            val response = mOkHttpClient.newCall(request).execute()
            if (response.isSuccessful) return response.body().bytes()
        } catch (e: Exception) {
        }

        return ByteArray(0)
    }

}