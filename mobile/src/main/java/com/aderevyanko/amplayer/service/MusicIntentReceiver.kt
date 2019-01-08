package com.aderevyanko.amplayer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.widget.Toast

/**
 * Receives broadcasted intents. In particular, we are interested in the
 * android.media.AUDIO_BECOMING_NOISY and android.intent.action.MEDIA_BUTTON intents, which is
 * broadcast, for example, when the user disconnects the headphones. This class works because we are
 * declaring it in a &lt;receiver&gt; tag in AndroidManifest.xml.
 */
class MusicIntentReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when {
            intent.action == android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY -> {
                Toast.makeText(context, "Headphones disconnected.", Toast.LENGTH_SHORT).show()

                // send an intent to our MusicService to telling it to pause the audio
                context.startService(Intent(MusicAction.ACTION_PAUSE.key))
            }
            intent.action == Intent.ACTION_MEDIA_BUTTON -> {
                val keyEvent = intent.extras.get(Intent.EXTRA_KEY_EVENT) as KeyEvent
                if (keyEvent.action != KeyEvent.ACTION_DOWN)
                    return

                when (keyEvent.keyCode) {
                    KeyEvent.KEYCODE_HEADSETHOOK, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> context.startService(Intent(MusicAction.ACTION_TOGGLE_PLAYBACK.key))
                    KeyEvent.KEYCODE_MEDIA_PLAY -> context.startService(Intent(MusicAction.ACTION_PLAY.key))
                    KeyEvent.KEYCODE_MEDIA_PAUSE -> context.startService(Intent(MusicAction.ACTION_PAUSE.key))
                    KeyEvent.KEYCODE_MEDIA_STOP -> context.startService(Intent(MusicAction.ACTION_STOP.key))
                    KeyEvent.KEYCODE_MEDIA_NEXT -> context.startService(Intent(MusicAction.ACTION_SKIP.key))
                    KeyEvent.KEYCODE_MEDIA_PREVIOUS ->
                        // TODO: ensure that doing this in rapid succession actually plays the
                        // previous song
                        context.startService(Intent(MusicAction.ACTION_REWIND.key))
                }
            }
            intent.action == Intent.ACTION_BOOT_COMPLETED -> {
                val serviceIntent = Intent(context, MusicService::class.java)
                context.startService(serviceIntent);
            }
        }
    }
}
