import okhttp3.*
import java.io.IOException

object NetworkUtil {

    private val User_Agent = "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)"
    private val mOkHttpClient: OkHttpClient = OkHttpClient.Builder().build()

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
                callback(false,e.toString().toByteArray())
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful){
                    callback(true,response.body().bytes())
                    return
                }
                callback(false,response.toString().toByteArray())
            }
        })
    }

}