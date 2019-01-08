package com.aderevyanko.amplayer.albums

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aderevyanko.amplayer.ImageViewLoaderTask
import com.aderevyanko.amplayer.R
import de.umass.lastfm.Album


class AlbumsAdapter(val context : Context, val values :List<Album>) : RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.row_with_image, parent, false)

        return AlbumViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {

        val album = values[position]

        val albumNameView = holder.view.findViewById(R.id.firstLine) as TextView
        val imageView = holder.view.findViewById(R.id.icon) as ImageView

        albumNameView.text = album.name

        val image = album.getImageURL(album.availableSizes().min())

        if (!image.isNullOrBlank()) {
            ImageViewLoaderTask(imageView).execute(image)
        }

        holder.view.setOnClickListener {
            val intent = Intent(context, AlbumActivity::class.java)
            intent.putExtra("artist", album.artist)
            intent.putExtra("album", album.name)
            intent.putExtra("mbid", album.mbid)
            intent.putExtra("image", album.getImageURL(album.availableSizes().max()))
            intent.putExtra("minImage", album.getImageURL(album.availableSizes().min()))

            ContextCompat.startActivity(context, intent, null)
        }
    }

    override fun getItemCount() = values.size

    data class AlbumViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}