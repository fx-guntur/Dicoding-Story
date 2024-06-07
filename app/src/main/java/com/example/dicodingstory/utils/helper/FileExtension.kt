package com.example.dicodingstory.utils.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import com.example.dicodingstory.utils.constant.AppConstants.MAX_SIZE
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val BUFFER_SIZE: Int = 1024 * 2
private const val TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(TIMESTAMP_FORMAT, Locale.US).format(Date())

fun Context.getImageUri(img: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    img.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(this.contentResolver, img, "Title", null)
    return Uri.parse(path)
}

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun Uri.uriToFile(context: Context): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(this) as InputStream
    val outputStream = FileOutputStream(myFile)

    val buffer = ByteArray(BUFFER_SIZE)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun Context.getFileFromUri(contentUri: Uri?): File? {
    val fileName: String = getFileName(contentUri) ?: ""
    val dir = File(
        this.externalCacheDir.toString()
    )
    if (!dir.exists()) {
        dir.mkdirs()
    }
    if (!TextUtils.isEmpty(fileName)) {
        val copyFile = File(dir.toString() + File.separator + fileName)
        copy(this, contentUri, copyFile)
        return copyFile
    }
    return null
}

private fun getFileName(uri: Uri?): String? {
    if (uri == null) return null
    var fileName: String? = null
    val path = uri.path
    val cut = path!!.lastIndexOf('/')
    if (cut != -1) {
        fileName = path.substring(cut + 1)
    }
    return fileName
}

private fun copy(context: Context, srcUri: Uri?, dstFile: File?) {
    try {
        val inputStream = context.contentResolver.openInputStream(srcUri!!) ?: return
        val outputStream: OutputStream = FileOutputStream(dstFile)
        copyStream(inputStream, outputStream)
        inputStream.close()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Throws(java.lang.Exception::class, IOException::class)
private fun copyStream(input: InputStream?, output: OutputStream?): Int {
    val buffer = ByteArray(BUFFER_SIZE)
    val `in` = BufferedInputStream(input, BUFFER_SIZE)
    val out = BufferedOutputStream(output, BUFFER_SIZE)
    var count = 0
    var n: Int
    try {
        while (`in`.read(buffer, 0, BUFFER_SIZE).also { n = it } != -1) {
            out.write(buffer, 0, n)
            count += n
        }
        out.flush()
    } finally {
        try {
            out.close()
        } catch (e: IOException) {
            Timber.e(e.toString())
        }
        try {
            `in`.close()
        } catch (e: IOException) {
            Timber.e(e.toString())
        }
    }
    return count
}

fun File.compressImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int

    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAX_SIZE)

    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}