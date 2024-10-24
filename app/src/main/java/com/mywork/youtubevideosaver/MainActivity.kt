@file:Suppress("DEPRECATION")

package com.mywork.youtubevideosaver

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mywork.youtubevideosaver.Classes.FullPermission
import com.mywork.youtubevideosaver.fragments.Loading_fragment
import com.mywork.youtubevideosaver.fragments.Main_fragment

class MainActivity : AppCompatActivity() {
    lateinit var fragmentManager: FragmentManager
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var
            fullPermission: FullPermission

    private lateinit var mainFragment: Main_fragment
    var s:String = "main"
    @SuppressLint("CommitPrefEdits", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mainFragment = Main_fragment()
        fullPermission = FullPermission(applicationContext,this,this.packageName)
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
        val isNight:Boolean = sharedPreferences.getBoolean("nightMode",false)
        if(isNight){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        transactionF(Loading_fragment())
        Handler().postDelayed({transactionF(Main_fragment())},1000)
        if (fullPermission.checkPermission()){

        }else{
            fullPermission.requestPermission()
        }
    }
    private fun transactionF(fragment: Fragment){
        fragmentManager = supportFragmentManager
        val fragmentTransaction:androidx.fragment.app.FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout, fragment)
        fragmentTransaction.commit()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == fullPermission.STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty()){

                grantResults[1] == PackageManager.PERMISSION_GRANTED
                grantResults[0] == PackageManager.PERMISSION_GRANTED

            }
        }
    }
}