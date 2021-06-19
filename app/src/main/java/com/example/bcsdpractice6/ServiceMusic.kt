package com.example.bcsdpractice6

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class ServiceMusic : Service() {

    lateinit var mediaPlayer : MediaPlayer

    inner class musicBinder : Binder(){
        fun getService() : ServiceMusic{
            return this@ServiceMusic
        }
    }
    val binder = musicBinder()
    override fun onBind(intent: Intent): IBinder {
        mediaPlayer = MediaPlayer.create(this, R.raw.eight)
        return binder
    }

    fun playMusic(){
        if(!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }
    fun pauseMusic(){
        if(mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

}