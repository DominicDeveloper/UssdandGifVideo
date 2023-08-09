package com.asadbek.video

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService

class MainActivity : AppCompatActivity() {
    lateinit var plGifImageView: ImageView
    lateinit var txt: TextView
    lateinit var progress:ProgressBar
    lateinit var button: Button
    var phoneNumbersInPhone = "unknown"
    var ussdBeeline = "*303#"
    var ussdUcell = "*450#"
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.btn)
        txt = findViewById(R.id.txt)
        progress = findViewById(R.id.progress_circular)

        // plGifImageView = findViewById(R.id.giffer)
        //plGifImageView.animate()
        /*
         videoView = findViewById(R.id.video)
        val uri:Uri = Uri.parse("android.resource://"+packageName+"/"+R.raw.sd)
        videoView.setVideoURI(uri)
        videoView.start()

         */

        button.setOnClickListener {
          //  calling()
            progress.visibility = VideoView.VISIBLE
            progress.setProgress(100,true)
            getNumberInTheNumber()
        }


    }
    private fun calling(){
        ussdBeeline = ussdBeeline.substring(0,ussdBeeline.length-1)

        ussdBeeline += Uri.encode("#")


        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$ussdBeeline#"))
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNumberInTheNumber() {
        val manager = applicationContext.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        manager.sendUssdRequest("$ussdBeeline",@RequiresApi(Build.VERSION_CODES.O)
            object :TelephonyManager.UssdResponseCallback(){
                override fun onReceiveUssdResponse(
                    telephonyManager: TelephonyManager?,
                    request: String?,
                    response: CharSequence?
                ) {
                    super.onReceiveUssdResponse(telephonyManager, request, response)
                    txt.text = response.toString()
                    progress.visibility = View.INVISIBLE
                    Log.d("MainActivity", "onReceiveUssdResponse: $response")

                }

                override fun onReceiveUssdResponseFailed(
                    telephonyManager: TelephonyManager?,
                    request: String?,
                    failureCode: Int
                ) {
                    super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode)
                    txt.text = request
                    progress.visibility = View.INVISIBLE
                    Log.d("MainActivity", "onReceiveUssdResponseFailed: $failureCode")
                }
                                                           },
            Handler()
        )
    }

}


