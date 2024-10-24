package com.mywork.youtubevideosaver.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.mywork.youtubevideosaver.Classes.Communication
import com.mywork.youtubevideosaver.Classes.DownloadM
import com.mywork.youtubevideosaver.MainActivity
import com.mywork.youtubevideosaver.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class Home_fragment : Fragment() {

    lateinit var search_btn:ImageButton
    lateinit var editText: EditText
    lateinit var linear_l:LinearLayout
    lateinit var comData:Communication
    lateinit var video_btn:Button
    lateinit var audio_btn:Button

    lateinit var selector:LinearLayout
    lateinit var scrollView:ScrollView
    lateinit var image:ImageView
    lateinit var text:TextView
    lateinit var downloading:LinearLayout

    lateinit var cancel_btn:Button
    lateinit var progressBar: ProgressBar
    lateinit var progress_p:TextView
    lateinit var name:String

    lateinit var progressBar_: ProgressBar

    lateinit var downloadM:DownloadM

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view:View = inflater.inflate(R.layout.fragment_home_fragment, container, false)
        (activity as MainActivity).s = "HOME"
        search_btn = view.findViewById(R.id.app_c_button)
        editText = view.findViewById(R.id.editTextText)
        linear_l = view.findViewById(R.id.select_list)
        video_btn = view.findViewById(R.id.button)
        audio_btn = view.findViewById(R.id.button2)

        selector = view.findViewById(R.id.linearlayout1)
        scrollView = view.findViewById(R.id.scrollView)
        image = view.findViewById(R.id.imageView)
        text = view.findViewById(R.id.textView5)
        downloading = view.findViewById(R.id.downloading)

        cancel_btn = view.findViewById(R.id.button3)
        progressBar = view.findViewById(R.id.progressBar2)
        progress_p = view.findViewById(R.id.progress_p)
        progress_p.text = progressBar.progress.toString()
        progressBar_ = view.findViewById(R.id.prgress_)

        downloadM = DownloadM(requireContext(),activity,progressBar,progress_p,cancel_btn,selector,scrollView,image,text,downloading)

        cancel_btn.setOnClickListener {
            downloadM.is_hide(0)
        }
        downloadM.is_hide(1)

        video_btn.setOnClickListener {
            result_generate(true)
            downloadM.is_hide(0)
        }
        audio_btn.setOnClickListener {
            result_generate(false)
            downloadM.is_hide(0)
        }
        search_btn.setOnClickListener {
            progressBar_.visibility = View.VISIBLE
            fetchData()
            search_btn.isEnabled = false
            downloadM.is_hide(1)
        }
        return view
    }
    @SuppressLint("SetTextI18n")
    private suspend fun setData(com: Communication){
        withContext(Main){
            try{
                text.text = com.dData.title.toString()
                name = text.text.toString()
                Picasso.get()
                    .load(com.dData.thumbnailUrl)
                    .into(image)
                search_btn.isEnabled = true
                result_generate(true)
                downloadM.is_hide(0)
                progressBar_.visibility = View.INVISIBLE
            }catch(ignored:Exception){
                text.text = "Communication error"
                image.setImageResource(R.drawable.baseline_report_gmailerrorred_24)
                search_btn.isEnabled = true
                Toast.makeText(requireContext(),"Communication error",Toast.LENGTH_SHORT).show()
                progressBar_.visibility = View.INVISIBLE
            }
        }
    }
    private fun fetchData() {
        CoroutineScope(Dispatchers.IO).launch {
            val com:Communication = fetchFromNetwork()
            setData(com)
        }
    }
    private fun fetchFromNetwork(): Communication {
        try{
            val communication = Communication(editText.text.toString())
            comData = communication
            return communication
        }catch(ignored:Exception){
            lateinit var communication: Communication
            comData = communication
            return communication
        }
    }
    @SuppressLint("SetTextI18n")
    private fun result_generate(must:Boolean){
        try{
            var final_must by Delegates.notNull<Boolean>()
            linear_l.removeAllViews()
            comData.formats.forEach {formats->
                if (must){
                    final_must = (formats.get("ext").toString().hashCode() == "\"mp4\"".hashCode())
                }else{
                    final_must = (formats.get("ext").toString().hashCode() != "\"mp4\"".hashCode() && formats.get("ext").toString().hashCode() != "\"webm\"".hashCode())
                }
                if (final_must){
                    val btn = Button(requireContext()).apply {
                        val params = LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT

                        ).apply{
                            setMargins(0,0,0,20)
                            setGravity(Gravity.CENTER)
                        }
                        layoutParams = params
                        textSize = 16f
                        setBackgroundColor(Color.RED)
                        setTextColor(Color.WHITE)
                        setPadding(20,30,20,30)
                        typeface = ResourcesCompat.getFont(context, R.font.robotoslab_bold)

                        if (formats.get("audioQuality").toString().hashCode() == 3392903){
                            text=formats.get("label").toString().replace("\"","") +"  "+"\uD83D\uDD07"
                        }else{
                            text=formats.get("label").toString().replace("\"","")
                        }
                        setOnClickListener {
                            downloadM.is_hide(2)
                            downloadM.downloadMedia(formats.get("url").toString().replace("\"",""),name,formats.get("ext").toString().replace("\"",""))
                        }
                    }
                    linear_l.addView(btn)
                }
            }
        }catch (ignored:Exception){ }
    }
}