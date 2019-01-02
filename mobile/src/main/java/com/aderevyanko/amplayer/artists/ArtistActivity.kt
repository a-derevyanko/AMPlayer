package com.aderevyanko.amplayer.artists

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aderevyanko.amplayer.AsyncTaskExecutor
import com.aderevyanko.amplayer.ImageViewLoaderTask
import com.aderevyanko.amplayer.LastFMCaller
import com.aderevyanko.amplayer.R
import kotlinx.android.synthetic.main.activity_artist.*

class ArtistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        handleIntent()
    }

    private fun handleIntent() {
        val artist = intent.getStringExtra("artist")
        val bigImage = intent.getStringExtra("image")

        supportActionBar?.title = artist
        ImageViewLoaderTask(header).execute(bigImage)

        AsyncTaskExecutor({
            LastFMCaller.findArtistAlbums(artist)
        }, {

        })
    }
}
