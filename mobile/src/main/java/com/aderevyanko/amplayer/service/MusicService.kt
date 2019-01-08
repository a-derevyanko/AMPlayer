package com.aderevyanko.amplayer.service

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.os.PowerManager
import android.preference.PreferenceManager
import java.util.concurrent.atomic.AtomicInteger


class MusicService : Service(),
MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
MediaPlayer.OnCompletionListener {

    //media player
    private val player = MediaPlayer()
    //song list
    private var songs = ArrayList<Song>()
    //current position
    private val songPosition = AtomicInteger(0)


    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onPrepared(mp: MediaPlayer) {
        mp.start();
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        stop()
        return true
    }

    override fun onCompletion(mp: MediaPlayer?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (MusicAction.from(intent.action)) {
            MusicAction.ACTION_TOGGLE_PLAYBACK -> processTogglePlaybackRequest()
            MusicAction.ACTION_PLAY -> processPlayRequest(intent)
            MusicAction.ACTION_PAUSE -> processPauseRequest()
            MusicAction.ACTION_SKIP -> processSkipRequest()
            MusicAction.ACTION_STOP -> processStopRequest()
            MusicAction.ACTION_REWIND -> processRewindRequest()
            MusicAction.ACTION_URL -> processAddRequest(intent)
        }

        return Service.START_NOT_STICKY // Means we started the service, but don't want it to
        // restart in case it's killed.
    }

    private fun processPauseRequest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun processRewindRequest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun processStopRequest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun processPlayRequest(intent: Intent) {
        setList(intent.getParcelableArrayListExtra("songs"))
        setSong(intent.getIntExtra("position", 0))

        playSong()
    }

    private fun processAddRequest(intent: Intent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun processSkipRequest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun processTogglePlaybackRequest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        stop()
        super.onDestroy()
    }

    fun stop() {
        stopForeground(true)

        player.stop()
        player.release()
    }

    override fun onCreate() {
        super.onCreate()

        player.setWakeMode(applicationContext,
                PowerManager.PARTIAL_WAKE_LOCK)
        player.setAudioAttributes(AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build())
        player.setOnPreparedListener(this)
        player.setOnCompletionListener(this)
        player.setOnErrorListener(this)

        //todo восстанавливаем данные последнего состояния
        val preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    fun setList(theSongs: List<Song>) {
        songs.clear()
        songs.addAll(theSongs)
    }

    fun setSong(songIndex: Int) {
        songPosition.set(songIndex)
    }

    fun playSong() {
        player.reset();
        val playSong = songs[songPosition.get()]
        val currSong = playSong.file

        player.setDataSource(currSong.path)
        player.prepareAsync()
    }
}