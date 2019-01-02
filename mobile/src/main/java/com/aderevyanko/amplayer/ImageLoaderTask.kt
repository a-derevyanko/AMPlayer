package com.aderevyanko.amplayer

import android.graphics.Bitmap

internal class ImageLoaderTask(private val callback:(Bitmap?)-> Unit) : ImageDownloaderTask<String>() {
    override fun doInBackground(vararg params: String): Bitmap? {
        val url = params[0]
        return downloadBitmap(url)
    }

    override fun onPostExecute(bitmap: Bitmap?) {
        callback.invoke(bitmap)
    }
}
