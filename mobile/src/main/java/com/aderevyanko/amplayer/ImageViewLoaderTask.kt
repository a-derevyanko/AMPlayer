package com.aderevyanko.amplayer

import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import java.lang.ref.WeakReference

internal class ImageViewLoaderTask(imageView: ImageView) : ImageDownloaderTask<String>() {
    private val imageViewReference = WeakReference(imageView)

    override fun doInBackground(vararg params: String): Bitmap? {
        return downloadBitmap(params[0])
    }

    override fun onPostExecute(bitmap: Bitmap?) {
        if (!isCancelled) {
            val imageView = imageViewReference.get()
            if (imageView != null) {
                if (bitmap == null) {
                    val placeholder = ContextCompat.getDrawable(imageView.context, R.drawable.ed_logo);
                    imageView.setImageDrawable(placeholder)
                } else {
                    imageView.setImageBitmap(bitmap)
                }
            }
        }
    }
}
