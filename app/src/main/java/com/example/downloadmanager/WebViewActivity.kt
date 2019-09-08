package com.example.downloadmanager

import android.app.Activity
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.webkit.*
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.view.*
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_webactivity.*
import android.webkit.MimeTypeMap
import com.example.downloadmanager.Fragment.Fragment_ALL


class WebViewActivity : AppCompatActivity(),AdapterLinkDownload.OnDownloadItemActionLink {
    override fun onAction(action: Int, position: Int) {
        when(action){
            adapterLinkDownload!!.ACTION_DOWNLOAD ->{
                val intent:Intent=Intent(this,PopUpAddLink::class.java)
                intent.putExtra("trangthai",true)
                intent.putExtra("url",""+array!![position].url)
                intent.putExtra("position",""+position)
                startActivityForResult(intent,requestOpenDiaLog)
            }
        }
    }



    var browser:WebView?=null
    var link:EditText?=null
    var button:Button?=null
    var progressBar:ProgressBar?=null
    var dialog: Dialog?=null
    var recyclerView:RecyclerView?=null
    var adapterLinkDownload:AdapterLinkDownload?=null
    var array:ArrayList<DownloadModel>?=null
    val requestOpenDiaLog=1
    var downloadManager: DownloadManager? =null
    var downloadMethod:DownloadMethod?=null
    var arrayListDownload:MutableList<DownloadModel> ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_webactivity)
        browser = findViewById(R.id.webview)
        link = findViewById(R.id.edtLink)
        button = findViewById(R.id.btnEnter)

        progressBar = findViewById(R.id.myProgressBar)
        progressBar!!.max = 100
        downloadManager=  this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadMethod= DownloadMethod(this,downloadManager!!)


        arrayListDownload=PreferencesUtility.getDownloadList(this,"com.example.downloadmanager.DownloadALL")
        if(arrayListDownload == null){
            arrayListDownload= ArrayList<DownloadModel>()
        }



        btnBack.setOnClickListener {
            onBackPressed()
        }
        btnForward.setOnClickListener {
            GoForward()
        }
        browser!!.settings.loadsImagesAutomatically = true
        browser!!.settings.javaScriptEnabled = true
        browser!!.settings.setAppCacheEnabled(true)
       // browser!!.settings.builtInZoomControls = true
        browser!!.settings.saveFormData = true
        browser!!.settings.pluginState = WebSettings.PluginState.ON
        browser!!.settings.domStorageEnabled = true


        button!!.setOnClickListener {
            progressBar!!.visibility = View.VISIBLE
            progressBar!!.progress = 0
            val url: String = link!!.text.toString()
            browser!!.loadUrl(url)

        }
        browser!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar!!.progress = newProgress
                if(newProgress==100){
                    progressBar!!.visibility=View.GONE
                }
                else{
                    progressBar!!.visibility=View.VISIBLE
                }

            }
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                txtTitle.text = title.toString()
            }
        }
        browser!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                edtLink.setText(url.toString())
                return false
            }
            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                if (view!!.url.toString().toLowerCase().contains("zing")) {
                      if(url!!.toString().toLowerCase().contains("hmac")){
                          val uri: Uri = Uri.parse(url)
                          var filename: String? =uri.lastPathSegment
                          if(!filename!!.toString().toLowerCase().contains(".")){
                              filename += ".mp3"
                          }
                          var exist:Boolean=false
                          for(item in array!!){
                              if(filename == item.filename){
                                  exist=true
                                  break
                              }
                          }
                          if(!exist ){
                              val downloadModel=DownloadModel(url,filename!!)
                              array!!.add(downloadModel)
                          }
                    }
                }
               else if(url!!.toString().toLowerCase().contains(".mp3") || url!!.toString().toLowerCase().contains(".mp4"))
               {
                   val uri: Uri = Uri.parse(url)
                   val filename: String? =uri.lastPathSegment
                   var exist:Boolean=false
                        for(item in array!!){
                            if(filename == item.filename){
                                exist=true
                                break
                            }
                        }
                        if(!exist ){
                            val downloadModel=DownloadModel(url,filename!!)
                            array!!.add(downloadModel)
                        }
               }
            }
        }

        dialog = Dialog(this)
        dialog!!.setContentView(R.layout.layout_dialog_download)
        recyclerView = dialog!!.findViewById(R.id.dsFileDownload)
        recyclerView!!.layoutManager = LinearLayoutManager(dialog!!.context)
        array = ArrayList<DownloadModel>()
        adapterLinkDownload = AdapterLinkDownload(dialog!!.context, array!!,this)
        recyclerView!!.adapter = adapterLinkDownload

        btnDownload.setOnClickListener { dialog!!.show() }




    }


    private fun GoForward() {
        if (browser!!.canGoForward()) {
            browser!!.goForward()
        } else {
            Toast.makeText(this, "Can't go further!", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==requestOpenDiaLog){
            if (resultCode== Activity.RESULT_OK){
                val diachi=data!!.getStringExtra("diachi")
                val url=data!!.getStringExtra("url")
                val position=(data!!.getStringExtra("position")).toInt()
                val downloadModel=downloadMethod!!.startDownload(url,diachi)
                downloadModel.diachi=diachi
                array!!.removeAt(position)
                adapterLinkDownload!!.notifyDataSetChanged()
                //  dialog!!.dismiss()
                arrayListDownload!!.add(downloadModel)
                PreferencesUtility.setDownloadList(this, arrayListDownload!!, "com.example.downloadmanager.DownloadALL")
            }
        }
    }
    override fun onBackPressed() {
        if (browser!!.canGoBack()) {
            browser!!.goBack()
        } else {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Exit App")
            dialog.setMessage("Browser has nothing to go back, so what next?")
            dialog.setPositiveButton("EXIT ME", DialogInterface.OnClickListener { dialog, which -> finish() })
            dialog.setCancelable(false)
            dialog.setNegativeButton("STAY HERE", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }).show()

        }
    }


}