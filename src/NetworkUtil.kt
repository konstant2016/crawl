import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.net.*

object NetworkUtil {

    // 浏览器标识
    private const val User_Agent = "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)"

    // 代理工具
    private val mProxySelector = object :ProxySelector(){
        override fun select(uri: URI?): MutableList<Proxy> = mAgencyList
        override fun connectFailed(uri: URI?, sa: SocketAddress?, ioe: IOException?) {}
    }

    private val mOkHttpClient: OkHttpClient = OkHttpClient.Builder()
            .proxySelector(mProxySelector)
            .build()

    // 代理地址链接库
    private val mAgencyList = mutableListOf(
            Proxy(Proxy.Type.HTTP,InetSocketAddress("182.88.214.145",8123)),
            Proxy(Proxy.Type.HTTP,InetSocketAddress("115.46.78.35",8123)),
            Proxy(Proxy.Type.HTTP,InetSocketAddress("111.194.12.152",8118)),
            Proxy(Proxy.Type.HTTP,InetSocketAddress("117.85.85.253",53128)),
            Proxy(Proxy.Type.HTTP,InetSocketAddress("118.190.95.35",9001)),
            Proxy(Proxy.Type.HTTP,InetSocketAddress("118.190.95.43",9001)),
            Proxy(Proxy.Type.HTTP,InetSocketAddress("61.135.217.7",80)),
            Proxy(Proxy.Type.HTTP,InetSocketAddress("111.226.188.37",8010)),
            Proxy(Proxy.Type.HTTP,InetSocketAddress("121.31.101.206",8123)),
            Proxy(Proxy.Type.HTTP,InetSocketAddress("113.110.44.20",61234)),
            Proxy(Proxy.Type.HTTP,InetSocketAddress("121.31.101.184",8123))
    )


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

    // 同步调用
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