package com.aderevyanko.amplayer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

internal abstract class ImageDownloaderTask<T> : AsyncTask<T, Void, Bitmap>() {

    protected fun downloadBitmap(url: String): Bitmap? {
        var urlConnection: HttpURLConnection? = null
        try {
            val uri = URL(url)
            urlConnection = uri.openConnection() as HttpURLConnection
            val statusCode = urlConnection.getResponseCode()
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return null
            }

            urlConnection.inputStream.use {
                return BitmapFactory.decodeStream(it)
            }
        } catch (e: Exception) {
            urlConnection!!.disconnect()
            Log.w("ImageDownloader", "Error downloading image from $url")
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect()
            }
        }
        return null
    }
}