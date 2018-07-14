import java.io.File
import java.io.FileOutputStream


// 主要入口
object MainEntrance {
    @JvmStatic
    fun main(args: Array<String>) {
        val netAddress = "http://www.mmjpg.com/"
        val localAddress = "D:\\Picture"

        val category = Crawl.crawlCategory(netAddress)
        val file = File(localAddress)
        if (!file.exists()) file.mkdir()

        createCategory(localAddress, category)
    }

    // 创建不同分类的文件夹
    private fun createCategory(parentPath: String, category: Map<String, String>) {
        category.forEach { text, url ->
            println("category-->"
                    + "text->" + text
                    + "url->" + url)

            val path = "$parentPath\\$text"
            val file = File(path)
            if (!file.exists()) file.mkdir()
            val cover = Crawl.crawlCover(url)
            createCover(path, cover)
        }
    }

    // 创建封面的文件夹
    private fun createCover(parentPath: String, coverList: Map<String, String>) {
        coverList.forEach { text, url ->
            println("coverList-->"
                    + "text->" + text
                    + "url->" + url)
            val path = "$parentPath\\$text"
            val file = File(path)
            if (!file.exists()) file.mkdir()
            val picList = Crawl.crawlPictureList(url)
            downPicture(path, picList)
        }
    }

    // 下载文件到文件夹内
    private fun downPicture(parentPath: String, picList: List<String>) {
        picList.forEachIndexed { index, url ->
//            Thread.sleep(1000)
            println("picList->$url")
            NetworkUtil.get(url) { state, bytes ->
                if (!state) return@get
                saveBitmap(parentPath, bytes, "$index.jpg")
            }
        }
    }

    // 保存图片到指定位置
    private fun saveBitmap(path: String, bytes: ByteArray, name: String) {
        println("path->$path\\$name")
        val file = File(path, name)
        with(FileOutputStream(file)){
            write(bytes,0,bytes.size)
            flush()
            close()
        }
    }
}