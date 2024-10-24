package com.mywork.youtubevideosaver.Classes

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class FullPermission (var context: Context,var activity: AppCompatActivity,var packageName: String){
    val STORAGE_PERMISSION_CODE = 100

    fun requestPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){

            try{
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package",this.packageName,null)
                intent.data = uri
                storageActivityResultLauncher.launch(intent)

            }catch (e :Exception){
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        }
        else{
            ActivityCompat.requestPermissions(
                this.activity,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION_CODE
            )
        }
    }
    private val storageActivityResultLauncher = this.activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){

            if (Environment.isExternalStorageManager()){

            }
        }
    }
    fun checkPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){

            Environment.isExternalStorageManager()
        }else{

            val write = ContextCompat.checkSelfPermission(this.context,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this.context,Manifest.permission.READ_EXTERNAL_STORAGE)

            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }
}