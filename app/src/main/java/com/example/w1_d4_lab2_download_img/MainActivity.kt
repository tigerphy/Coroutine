package com.example.w1_d4_lab2_download_img

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import android.graphics.BitmapFactory
import java.io.BufferedInputStream
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity() {

    inner class GetConn : AsyncTask<Unit, Unit, Bitmap>() {

        override fun doInBackground(vararg params: Unit?): Bitmap {
            lateinit var img : Bitmap
            try {
                val url = URL("https://users.metropolia.fi/~youqins/HTML_CSS_CV/bg.png")
                val conn = url.openConnection() as HttpURLConnection
                conn.connect()
                val istream: InputStream = conn.getInputStream()
                val bis = BufferedInputStream(istream)
                img = BitmapFactory.decodeStream(bis)
                bis.close()
                istream.close()
            } catch (e: Exception) {
                Log.e("Connection", "Reading error", e)
            }
            return img
        }
        override fun onPostExecute(result:Bitmap?) {
            image.setImageBitmap(result)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isNetworkAvailable()) {
            //DOES NOT MAKE USE OF MULTI-THREADING
            GetConn().execute() //==> SpecialThread done
//            GetConn().execute() //==> SpecialThread running
//            GetConn().execute() //==> SpecialThread waiting
//
            //MAKE USE OF MULTI-THREADING
//            GetConn().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR) ==> T1
//            GetConn().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR) ==> T2
//            GetConn().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR) ==> T3
        }
    }


    private fun isNetworkAvailable(): Boolean {
        val cm = this.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        return cm.activeNetworkInfo?.isConnected == true
    }
}
