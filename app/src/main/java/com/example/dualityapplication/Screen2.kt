package com.example.dualityapplication

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Screen2 : AppCompatActivity() {

    lateinit var textView :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home_page_screen)
        Log.d("onCheckingResponse", "2 on create")
        textView = findViewById(R.id.textView)
    }
    override fun onStart() {
        super.onStart()


        Log.d("onCheckingResponse","2 on start")
    }

    override fun onRestart() {
        super.onRestart()


        Log.d("onCheckingResponse","2 on restart")


    }


    override fun onResume() {
        super.onResume()


        Log.d("onCheckingResponse","2 on resume")
        textView.setOnClickListener(){
//            startActivity(Intent(this,Screen2::class.java))

            this.finish()

        }
    }

    override fun onPause() {
        super.onPause()


        Log.d("onCheckingResponse","2 on onPause")
    }

    override fun onStop() {
        super.onStop()

        Log.d("onCheckingResponse","2 on stop")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("onCheckingResponse","2 on destroy")
    }





}