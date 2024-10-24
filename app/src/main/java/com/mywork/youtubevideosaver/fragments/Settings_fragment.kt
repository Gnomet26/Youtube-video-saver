package com.mywork.youtubevideosaver.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mywork.youtubevideosaver.MainActivity
import com.mywork.youtubevideosaver.R

class Settings_fragment : Fragment() {
    lateinit var sharedPreferences:SharedPreferences

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).s = "SETTINGS"

        val view:View = inflater.inflate(R.layout.fragment_settings_fragment, container, false)
        sharedPreferences = (activity as MainActivity).sharedPreferences
        saveThemePrefrence("is_s",false,false)
        val switch:Switch = view.findViewById(R.id.switch1)
        switch.isChecked = sharedPreferences.getBoolean("nightMode",false)
        switch.setOnCheckedChangeListener { _, check ->
            if (check){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            saveThemePrefrence("is_s",true,false)
            saveThemePrefrence("nightMode",check,true)
        }
        return view
    }
    @SuppressLint("CommitPrefEdits")
    private fun saveThemePrefrence(key_:String,checked: Boolean,reflesh:Boolean) {
        var editor:SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(key_,checked)
        editor.apply()
        if (reflesh){
            transactionF(R.id.framelayout,Main_fragment())
        }
    }

    private fun transactionF(id_:Int,fragment: Fragment){
        var a:FragmentManager = (activity as MainActivity).fragmentManager
        var fragmentTransaction:androidx.fragment.app.FragmentTransaction = a.beginTransaction()
        fragmentTransaction.replace(id_, fragment)
        fragmentTransaction.commit()
    }
}