package com.oving7.managers

import androidx.appcompat.app.AppCompatActivity
import java.io.*

class MyFileManager(private val activity: AppCompatActivity) {
    private var dir: File = activity.filesDir

    fun createFile(str: String, fileName: String) {
        val file = File(dir, fileName)
        file.createNewFile()
        write(str, file)
    }

    fun write(str: String, file: File) {
        PrintWriter(file).use { writer ->
            writer.println(str)
        }
    }

    fun readLine(file: File): String? {
        BufferedReader(FileReader(file)).use { reader ->
            return reader.readLine()
        }
    }

    fun readFileFromResFolder(fileId: Int): String {
        val content = StringBuffer("")
        try {
            val inputStream: InputStream = activity.resources.openRawResource(fileId)

            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line = reader.readLine()
                while (line != null) {
                    content.append(line)
                    content.append("\n")
                    line = reader.readLine()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return content.toString()
    }
}