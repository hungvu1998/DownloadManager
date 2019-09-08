package com.example.downloadmanager

import android.Manifest
import android.app.Activity

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.layout_main_chinh.*


class TrangChuActivity : AppCompatActivity(), ViewPager.OnPageChangeListener{


    var btnWebView: ImageView?=null

    override fun onPageSelected(position: Int) {
        var menu: Menu =bottom_navigation2.menu
        when (position) {
            0 -> menu.getItem(0).isChecked=true
            1 -> menu.getItem(1).isChecked=true
            2 -> menu.getItem(2).isChecked=true
        }
    }


    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }


    var adapter_main: AdapterMain?=null
    var url:String?=null
    var diachi:String?=null
    val REQUEST_PERMISSION_STORAGE=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main_chinh)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Download Manager"


        val checkPermissionExternalStorage:Int=ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(checkPermissionExternalStorage!=PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION_STORAGE)
        }
        else{
           thucHien()
        }
    }
    fun thucHien(){
        fragment_container2.visibility= View.GONE
        fragment_container.visibility=View.VISIBLE
        adapter_main= AdapterMain(supportFragmentManager)
        fragment_container.adapter=adapter_main
        fragment_container.offscreenPageLimit=3
        fragment_container.addOnPageChangeListener(this)

        bottom_navigation2.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottom_navigation2.menu

        btnWebView=findViewById(R.id.btnWebView)
        btnWebView!!.setOnClickListener {
            val intentWeb:Intent=Intent(this,WebViewActivity::class.java)
            startActivity(intentWeb)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_PERMISSION_STORAGE -> {
                if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    thucHien()
                }
            }
        }
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.nav_all -> {
                fragment_container.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_downloading -> {
                fragment_container.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_downloadfail -> {
                fragment_container.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }

        }
        true
    }







}
