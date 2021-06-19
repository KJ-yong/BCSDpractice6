package com.example.bcsdpractice6

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.databinding.DataBindingUtil
import com.example.bcsdpractice6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    var musicService : ServiceMusic? = null
    var isBound : Boolean = false
    val connection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ServiceMusic.musicBinder
            musicService = binder.getService()
            isBound = true
            musicService?.mediaPlayer?.setOnCompletionListener {
                binding.buttonPlayPause.setBackgroundResource(R.drawable.playicon)
                binding.buttonPlayPause.isChecked = false
            }
            if(musicService?.mediaPlayer?.isPlaying ==true){
                binding.buttonPlayPause.setBackgroundResource(R.drawable.pauseicon)
                binding.buttonPlayPause.isChecked = true
            }
            else {
                binding.buttonPlayPause.setBackgroundResource(R.drawable.playicon)
                binding.buttonPlayPause.isChecked = false
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }
    fun serviceUnbind(){
        if(isBound){
            unbindService(connection)
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if(!isBound) {
            val intent = Intent(this, ServiceMusic::class.java)
            startService(intent)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)

        }



        binding.buttonPlayPause.setOnClickListener {
            if(binding.buttonPlayPause.isChecked) {
                binding.buttonPlayPause.setBackgroundResource(R.drawable.pauseicon)
                musicService?.playMusic()
            }else{
                binding.buttonPlayPause.setBackgroundResource(R.drawable.playicon)
                musicService?.pauseMusic()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(musicService?.mediaPlayer?.isPlaying ==true){
            binding.buttonPlayPause.setBackgroundResource(R.drawable.pauseicon)
            binding.buttonPlayPause.isChecked = true
        }
        else {
            binding.buttonPlayPause.setBackgroundResource(R.drawable.playicon)
            binding.buttonPlayPause.isChecked = false
        }
    }
    override fun onStop() {
        super.onStop()
        serviceUnbind()
    }
}