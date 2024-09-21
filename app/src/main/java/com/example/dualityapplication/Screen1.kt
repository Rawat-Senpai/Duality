package com.example.dualityapplication

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Screen1:AppCompatActivity() {

    lateinit var textView:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home_page_screen)
        Log.d("onCheckingResponse", "on create")
        textView = findViewById(R.id.textView)



    }
    override fun onStart() {
        super.onStart()


        Log.d("onCheckingResponse","on start")
    }

    override fun onRestart() {
        super.onRestart()


        Log.d("onCheckingResponse","on restart")


    }


    override fun onResume() {
        super.onResume()


        Log.d("onCheckingResponse","on resume")
        textView.setOnClickListener(){
            startActivity(Intent(this,Screen2::class.java))
        }
    }

    override fun onPause() {
        super.onPause()


        Log.d("onCheckingResponse","on onPause")
    }

    override fun onStop() {
        super.onStop()

        Log.d("onCheckingResponse","on stop")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("onCheckingResponse","on destroy")
    }





}