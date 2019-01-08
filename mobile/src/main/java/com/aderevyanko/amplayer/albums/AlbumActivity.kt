package com.aderevyanko.amplayer.albums

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.aderevyanko.amplayer.AsyncTaskExecutor
import com.aderevyanko.amplayer.ImageViewLoaderTask
import com.aderevyanko.amplayer.LastFMCaller
import com.aderevyanko.amplayer.R
import kotlinx.android.synthetic.main.activity_artist.*



class AlbumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        albumsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        handleIntent()
    }

    private fun handleIntent() {
        val artist = intent.getStringExtra("artist")
        val album = intent.getStringExtra("album")
        val mbid = intent.getStringExtra("mbid")
        val bigImage = intent.getStringExtra("image")
        val minImage = intent.getStringExtra("minImage")

        supportActionBar?.title = album
        if (!bigImage.isNullOrBlank()) {
            ImageViewLoaderTask(header).execute(bigImage)
        }

        AsyncTaskExecutor({
            LastFMCaller.getAlbumInfo(artist, album, mbid)
        }, {
            albumsList.adapter = TracksAdapter(applicationContext, it, minImage)
        })
    }
}
