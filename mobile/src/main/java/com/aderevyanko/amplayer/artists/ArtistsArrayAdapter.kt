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


class ArtistsArrayAdapter(context: Context?, values: List<Artist>) : ArrayAdapter<Artist>(context, R.layout.artist_row, values) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView:View

        if (convertView == null) {
            rowView = LayoutInflater.from(getContext()).inflate(R.layout.artist_row, parent, false)
            val artistNameView = rowView.findViewById(R.id.firstLine) as TextView
            val descriptionView = rowView.findViewById(R.id.secondLine) as TextView

            val imageView = rowView.findViewById(R.id.icon) as ImageView
            rowView.tag = ViewHolder(artistNameView, descriptionView, imageView)
        } else {
            rowView = convertView
        }

        val artist = getItem(position)

        val holder = rowView.tag as ViewHolder

        holder.artistView.text = artist.name

        holder.descriptionView.text =  artist.wikiText?.substring(0, 10) ?: ""

        val image = artist.getImageURL(artist.availableSizes().min())

        ImageViewLoaderTask(holder.imageView).execute(image)

        return rowView
    }

    private data class ViewHolder(val artistView: TextView, val descriptionView: TextView, val imageView: ImageView)
}