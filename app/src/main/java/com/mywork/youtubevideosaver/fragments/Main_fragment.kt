package com.mywork.youtubevideosaver.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mywork.youtubevideosaver.MainActivity
import com.mywork.youtubevideosaver.R

class Main_fragment : Fragment() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var bottomnavigatinview:BottomNavigationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val view:View = inflater.inflate(R.layout.fragment_main_fragment,container,false)
        sharedPreferences = (activity as MainActivity).sharedPreferences
        val isSetting:Boolean = sharedPreferences.getBoolean("is_s",false)

        bottomnavigatinview = view.findViewById(R.id.bottomNavigationView)
        bottomnavigatinview.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    if ((activity as MainActivity).s.hashCode() != "HOME".hashCode()){
                        transaction_f(Home_fragment())
                    }
                    true
                }
                R.id.settings -> {
                    if ((activity as MainActivity).s.hashCode() != "SETTINGS".hashCode()){
                        transaction_f(Settings_fragment())
                    }
                    true
                }
                else -> false
            }
        }

        if (isSetting){
            transaction_f(Settings_fragment())
            bottomnavigatinview.selectedItemId = R.id.settings
        }
        else{
            transaction_f(Home_fragment())
            bottomnavigatinview.selectedItemId = R.id.home
        }
        return view
    }
    private fun transaction_f(fragment: Fragment){
        val a:FragmentManager = (activity as MainActivity).fragmentManager
        val fragmentTransaction:FragmentTransaction = a.beginTransaction()
        fragmentTransaction.replace(R.id.frame_l, fragment)
        fragmentTransaction.commit()
    }
}