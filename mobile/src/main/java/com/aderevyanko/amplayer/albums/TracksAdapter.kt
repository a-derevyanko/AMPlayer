package com.aderevyanko.amplayer.albums

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aderevyanko.amplayer.ImageViewLoaderTask
import com.aderevyanko.amplayer.R
import com.aderevyanko.amplayer.ZaycevNetMusicDownloader
import com.aderevyanko.amplayer.service.MusicAction
import com.aderevyanko.amplayer.service.Song
import de.umass.lastfm.Album
import de.umass.lastfm.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


class TracksAdapter(private val context: Context,
                    private val album: Album,
                    private val imageUrl: String?) : RecyclerView.Adapter<TracksAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.track_row, parent, false)

        return TrackViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        val track = album.tracks.elementAt(position)

        val artistNameView = holder.view.findViewById(R.id.artist) as TextView
        val albumNameView = holder.view.findViewById(R.id.title) as TextView
        val durationView = holder.view.findViewById(R.id.duration) as TextView
        val imageView = holder.view.findViewById(R.id.list_image) as ImageView

        artistNameView.text = track.artist
        durationView.text = String.format("%02d:%02d", track.duration / 60, track.duration % 60)
        albumNameView.text = track.name

        if (!imageUrl.isNullOrBlank()) {
            ImageViewLoaderTask(imageView).execute(imageUrl)
        }

        holder.view.setOnClickListener {
            GlobalScope.launch {
                //musicService.setList(getSongList())
                //val trackInfo = LastFMCaller.getTrackInfo(track.artist, track.name, track.mbid)

                val directory = context.filesDir.absolutePath + File.separator +
                        "tracks/${album.artist}/${album.name}/${track.name}".replace(" ","")

                val file = File(directory, position.toString())

                if (file.exists()) {
                    playSelected(position)
                } else {
                    val mp3 = ZaycevNetMusicDownloader.download(track)

                    if (mp3 == null) {
                        val k = 99
                    } else {

                        if (!file.parentFile.exists() && !file.parentFile.mkdirs()) {
                            throw IllegalStateException()
                        }

                        FileOutputStream(file).use { outputStream ->
                            outputStream.write(mp3)
                        }

                        playSelected(position)
                    }
                }
            }
        }
    }

    override fun getItemCount() = album.tracks.size

    private fun playSelected(selectedIndex: Int) {
        context.startService(Intent( MusicAction.ACTION_PLAY.key)
                .setPackage(context.packageName)
                .putParcelableArrayListExtra("songs", getSongList())
                .putExtra("position", selectedIndex))
    }

    private fun getSongList() : ArrayList<Song> {
        return ArrayList(album.tracks.mapIndexed { index: Int, track: Track ->
            val directory = context.filesDir.absolutePath + File.separator +
                    "tracks/${album.artist}/${album.name}/${track.name}".replace(" ", "")
            Song(track.name, album.artist, File(directory, index.toString()))
        })
    }

    data class TrackViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}