import org.jsoup.Jsoup
import java.lang.Exception

object Crawl {

    // 获取所有类别
    fun crawlCategory(url: String): Map<String, String> {
        val map = LinkedHashMap<String, String>()
        try {
            val document = Jsoup.connect(url).get()
            val elements = document.select("div.subnav > a")
            elements.map {
                map.put(it.text(), it.attr("href"))
            }
        }catch (e:Exception){}

        return map
    }

    // 根据传入的类别地址获取具体人物封面地址和描述
    fun crawlCover(url: String): Map<String, String> {
        val map = LinkedHashMap<String, String>()
        var page = 1

        try {
            var address = "$url/$page"
            var elements = Jsoup.connect(address).get().select("div.pic > ul > li > span.title > a")
            println("elements size " + elements.size)
            println("address " + address)

            while (elements.isNotEmpty()) {
                elements.map {
                    map.put(it.text(), it.attr("href"))
                }
                page += 1; address = "$url$page"
                elements = Jsoup.connect(address).get().select("div.pic > ul > li > span.title > a")
            }
        }catch (e:Exception){}

        return map
    }

    // 根据传入的封面地址，获取对应的套图地址列表
    fun crawlPictureList(url: String): List<String> {
        val list = mutableListOf<String>()
        try {
            val document = Jsoup.connect(url).get()
            val elements = document.select("div.page > a")  // 获取所有的标签a的元素
            val size = elements[elements.size - 2].text().toInt()   // 获取元素列表中的倒数第二个元素，此元素中包含着当前图片的总数目

            // 取得该套图下所有图片的链接地址
            for (index in 1..size) {
                val pictureUrl = crawlPictureUrl("$url/$index")
                list.add(pictureUrl)
            }
        }catch (e:Exception){}

        return list
    }

    // 访问网页获取图片地址
    private fun crawlPictureUrl(url: String): String {
        return try {
            val document = Jsoup.connect(url).get()
            document.select("div.content > a")[0].select("img").attr("src")
        } catch (ex: Exception) {
            ""
        }
    }
}