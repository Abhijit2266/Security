package com.example.security

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val permission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_CONTACTS,

    )
    val permissionCode = 22

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askForPermission()

        val bottomBar = findViewById<BottomNavigationView>(R.id.bottom_bar)

        bottomBar.setOnItemSelectedListener { menuItem->
            if (menuItem.itemId == R.id.nav_home) {
                inflateFragment(HomeFragment.newInstance())
            }
            else if (menuItem.itemId == R.id.nav_dashboard) {
                inflateFragment(MapsFragment())

            }
            else if (menuItem.itemId == R.id.nav_guard) {
                inflateFragment(GuardFragment.newInstance())

            }
            else if (menuItem.itemId == R.id.nav_profile) {
                inflateFragment(ProfileFragment.newInstance())

            }
            true
        }
        bottomBar.selectedItemId = R.id.nav_home
    }

    private fun askForPermission() {
        ActivityCompat.requestPermissions(this,permission,permissionCode)
    }

    private fun inflateFragment(newInstance: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,newInstance)
        transaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == permissionCode){
            if (allPermissionGranted()){
                openCamera()
            }else {

            }
        }

    }

    private fun openCamera() {
       val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent,null)
    }

    private fun allPermissionGranted(): Boolean {
        for (item in permission){
            if (ContextCompat.checkSelfPermission(this,item)!= PackageManager.PERMISSION_GRANTED){
             return false
            }
        }
        return true

    }
}