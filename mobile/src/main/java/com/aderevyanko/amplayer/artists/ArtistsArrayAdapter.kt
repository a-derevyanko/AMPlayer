package com.aderevyanko.amplayer.artists

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.aderevyanko.amplayer.ImageViewLoaderTask
import com.aderevyanko.amplayer.R
import de.umass.lastfm.Artist

class ArtistsArrayAdapter(context: Context?, values: List<Artist>) : ArrayAdapter<Artist>(context, R.layout.row_with_image, values) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView:View

        if (convertView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.row_with_image, parent, false)
            val artistNameView = rowView.findViewById(R.id.firstLine) as TextView

            val imageView = rowView.findViewById(R.id.icon) as ImageView
            rowView.tag = ViewHolder(artistNameView, imageView)
        } else {
            rowView = convertView
        }

        with(getItem(position)) {
            val holder = rowView.tag as ViewHolder

            holder.artistView.text = name

            val image = getImageURL(availableSizes().min())

            if (!image.isNullOrBlank()) {
                ImageViewLoaderTask(holder.imageView).execute(image)
            }
        }

        return rowView
    }

    private data class ViewHolder(val artistView: TextView, val imageView: ImageView)
}