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
        println("访问的网址为：$netAddress")
        createCategory(localAddress, category)
    }

    // 创建不同分类的文件夹
    private fun createCategory(parentPath: String, category: Map<String, String>) {
        category.forEach { text, url ->
            println("创建文件分类："
                    + "类名->" + text
                    + "，链接->" + url)

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
            println("创建封面文件夹-->"
                    + "封面->" + text
                    + "，链接->" + url)
            val path = "$parentPath\\$text"
            val file = File(path)
            if (!file.exists()) file.mkdir()
            val picList = Crawl.crawlPictureList(url)
            downPicture(path, picList)
        }
    }

    // 下载文件到文件夹内
    private fun downPicture(parentPath: String, picList: List<String>) {
        println("---开始下载当前套图图片---")
        picList.forEachIndexed { index, url ->
            val random = (Math.random() * 5000 + 1000).toLong()
            println("随机延迟，避免被封，延迟时间->${random / 1000}S")
            Thread.sleep(random)
            if (url.isEmpty()) return
            val bytes = NetworkUtil.get(url)
            if (bytes.isEmpty()) return
            println("保存图片：位置->$parentPath\\$index.jpg，链接->$url")
            saveBitmap(parentPath, bytes, "$index.jpg")
        }
    }

    // 保存图片到指定位置
    private fun saveBitmap(path: String, bytes: ByteArray, name: String) {
        val file = File(path, name)
        with(FileOutputStream(file)) {
            write(bytes, 0, bytes.size)
            flush()
            close()
        }
    }
}