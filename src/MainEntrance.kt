import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors


// 主要入口
object MainEntrance {
    @JvmStatic
    fun main(args: Array<String>) {
        val netAddress = "http://www.mmjpg.com/"
        val localAddress = "D:\\picture"

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

            val path = "$parentPath${File.separatorChar}$text"
            val file = File(path)
            if (!file.exists()) file.mkdir()
            val cover = Crawl.crawlCover(url)
            Executors.newCachedThreadPool().execute { createCover(path, cover) }
        }
    }

    // 创建封面的文件夹
    private fun createCover(parentPath: String, coverList: Map<String, String>) {
        coverList.forEach { text, url ->
            println("创建封面文件夹："
                    + "封面->" + text
                    + "，链接->" + url)
            val path = "$parentPath${File.separatorChar}$text"
            val file = File(path)
            if (!file.exists()) file.mkdir()
            val picList = Crawl.crawlPictureList(url)
            Executors.newCachedThreadPool().execute { downPicture(path, picList) }
        }
    }

    // 下载文件到文件夹内
    private fun downPicture(parentPath: String, picList: List<String>) {
        println("---开始下载当前套图图片---")
        picList.forEachIndexed { index, url ->
            if (url.isEmpty()) return@forEachIndexed
            val path = "$parentPath${File.separator}$index.jpg"
            if (File(path).exists()) return@forEachIndexed
            Executors.newCachedThreadPool().execute {
                val bytes = NetworkUtil.get(url)
                if (bytes.isEmpty()) return@execute
                println("保存图片：位置->$parentPath${File.separator}$index.jpg，链接->$url")
                saveBitmap(path, bytes) }
        }
    }

    // 保存图片到指定位置
    private fun saveBitmap(path: String, bytes: ByteArray) {
        val file = File(path)
        if (!file.exists()) file.createNewFile()
        with(FileOutputStream(file)) {
            write(bytes, 0, bytes.size)
            flush()
            close()
        }
    }
}