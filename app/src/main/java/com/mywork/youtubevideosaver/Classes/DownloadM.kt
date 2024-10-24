package com.mywork.youtubevideosaver.Classes

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mywork.youtubevideosaver.MainActivity
import com.mywork.youtubevideosaver.R
import com.mywork.youtubevideosaver.fragments.Home_fragment

class DownloadM(val context:Context,val activity: FragmentActivity?,val progressBar: ProgressBar,val progress_p:TextView,val cancel_btn:Button,val selector: LinearLayout,val scrollView: ScrollView,val image: ImageView,val text:TextView,val downloading: LinearLayout) {
    init {

    }
    fun downloadMedia(download_url: String, file_n: String, file_type: String) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(download_url))
            .apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                setTitle("${file_n}.${file_type}")
                setDescription("Please wait...")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                if (file_type.hashCode() == "mp4".hashCode()){
                    setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS, "YVS_video.mp4")
                }else if (file_type.hashCode() == "m4a".hashCode()){
                    setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS, "YVS_audio.m4a")
                }else if(file_type.hashCode() == "opus".hashCode()){
                    setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS, "YVS_audio.opus")
                }
            }
        val id = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(id)
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                val cursor = downloadManager.query(query)
                if (cursor != null && cursor.moveToFirst()) {
                    val downloaded = cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                            DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR
                        )
                    )
                    val total = cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                            DownloadManager.COLUMN_TOTAL_SIZE_BYTES
                        )
                    )
                    val prosent = (downloaded * 100) / total

                    progressBar.progress = prosent.toInt()
                    progress_p.text = buildString {
                        append(progressBar.progress.toString())
                        append("%")
                    }
                    if (progress_p.text.hashCode() == "100%".hashCode()){
                        Toast.makeText(context,"${file_n}.${file_type}",Toast.LENGTH_SHORT).show()
                        save_data(context,"dkey", arrayOf(get_data(context,"dkey").toString(),"${file_n}.${file_type}"))
                        val a: FragmentManager = (activity as MainActivity).fragmentManager
                        val fragmentTransaction: FragmentTransaction = a.beginTransaction()
                        fragmentTransaction.replace(R.id.frame_l, Home_fragment())
                        fragmentTransaction.commit()
                    }
                }
            }
        }
        context.contentResolver.registerContentObserver(
            Uri.parse("content://downloads/my_downloads"), true, observer
        )
        cancel_btn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Warning")
            builder.setMessage("Really cancel it?")
            builder.setPositiveButton("Yes") { _, _ ->
                downloadManager.remove(id)
                is_hide(0)
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    fun is_hide(value: Int) {
        if (value == 0) {
            selector.visibility = View.VISIBLE
            scrollView.visibility = View.VISIBLE
            image.visibility = View.VISIBLE
            text.visibility = View.VISIBLE
            downloading.visibility = View.INVISIBLE
        } else if (value == 1) {
            selector.visibility = View.INVISIBLE
            scrollView.visibility = View.INVISIBLE
            image.visibility = View.INVISIBLE
            text.visibility = View.INVISIBLE
            downloading.visibility = View.INVISIBLE

        } else if (value == 2) {
            selector.visibility = View.INVISIBLE
            scrollView.visibility = View.INVISIBLE
            image.visibility = View.VISIBLE
            text.visibility = View.VISIBLE
            downloading.visibility = View.VISIBLE
        }
    }
    private fun save_data(context: Context,key:String,array: Array<String>){
        val sharedPreferences = context.getSharedPreferences("DList",Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(array)
        editor.putString(key,json)
        editor.apply()
    }
    private fun get_data(context: Context,key: String){
        val sharedPreferences = context.getSharedPreferences("DList",Context.MODE_PRIVATE)

        val gson = Gson()
        val json = sharedPreferences.getString(key,null)
        val type = object : TypeToken<Array<String>>(){}.type

        return gson.fromJson(json,type)

    }
}