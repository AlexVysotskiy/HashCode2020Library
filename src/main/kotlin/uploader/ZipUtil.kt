package uploader

import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

val ignore = listOf("./out/", "./.git/", "./inputs/")

fun packToZip(sourceDirPath: String): ByteArray {
    val outputStream = ByteArrayOutputStream()
    ZipOutputStream(outputStream).use { stream ->
        val sourceDir = Paths.get(sourceDirPath)
        Files.walk(sourceDir)
            .filter { path -> !Files.isDirectory(path) }
            .filter { path -> ignore.all { !path.toString().startsWith(it) } }
            .forEach { path ->
                val zipEntry = ZipEntry(path.toString().substring(sourceDir.toString().length + 1))

                stream.putNextEntry(zipEntry)
                stream.write(Files.readAllBytes(path))
                stream.closeEntry()
            }
    }
    return outputStream.toByteArray()
}