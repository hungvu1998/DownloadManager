package com.example.downloadmanager.Fragment

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.downloadmanager.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import lib.folderpicker.FolderPicker
import java.io.File


class Fragment_ALL : Fragment(),DownloadAdapter.OnDownloadItemAction{


    override fun onAction(action: Int, id: Long) {
        when (action) {
            downloadAdapter!!.ACTION_STOP -> {
                downloadManager!!.remove(id)
            }
            downloadAdapter!!.ACTION_DELETE -> {

                for(i in arrayListDownload!!.indices){
                    if (arrayListDownload!![i].id == id){
                        arrayListDownload!!.removeAt(i)
                        downloadManager!!.remove(id)
                        break
                    }
                }
                downloadAdapter!!.updateDataSet(arrayListDownload!!)
                PreferencesUtility.setDownloadList(context, arrayListDownload!!,DOWNLOADALL)
            }
            downloadAdapter!!.ACTION_RETRY -> {
                for (i in arrayListDownload!!.indices) {
                    if (arrayListDownload!![i].id == id) {
                       downloadMethod!!.startDownload(arrayListDownload!![i].url!!,arrayListDownload!![i].diachi!!)
                        arrayListDownload!!.removeAt(i)
                        break
                    }
                }
            }
            downloadAdapter!!.ACTION_PAUSE -> {
                for (i in arrayListDownload!!.indices) {
                    if (arrayListDownload!![i].id == id) {
                        arrayListDownload!![i].status=DownloadManager.STATUS_PAUSED
                        downloadAdapter!!.updateDataSet(arrayListDownload!!)
                        downloadManager!!.remove(id)
                        break
                    }
                }
            }
//            downloadAdapter!!.ACTION_RESUME -> {
//                for (i in arrayListDownload!!.indices) {
//                    if (arrayListDownload!!.get(i).id == id) {
//                        resume(arrayListDownload!![i].url!!, arrayListDownload!![i].downloaded!!)
//                        arrayListDownload!![i].status=DownloadManager.STATUS_FAILED
//                        arrayListDownload!!.removeAt(i)
//                        break
//                    }
//                }
//            }
        }
    }
     val DOWNLOADALL = "com.example.downloadmanager.DownloadALL"
    val requestOpenDiaLog=1
    var downloadManager: DownloadManager? =null
    var arrayListDownload:MutableList<DownloadModel> ?=null
    var downloadAdapter:DownloadAdapter?=null
    var button:FloatingActionButton?=null
    var downloadMethod:DownloadMethod?=null
    var recycleview:RecyclerView?=null

    val ACTION_DELETE = 0

    val sharedViewModel by lazy {
        ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        arrayListDownload=PreferencesUtility.getDownloadList(context,DOWNLOADALL)
        if(arrayListDownload == null){
            arrayListDownload= ArrayList<DownloadModel>()
        }
        Log.d("kiemtra",""+arrayListDownload!!.size)
        downloadAdapter= DownloadAdapter(context!!, arrayListDownload!!,this)
        recycleview!!.adapter=downloadAdapter
        for (m: DownloadModel in arrayListDownload!!) {
            createDownloadItemView(m.id!!, m.url!!)
        }
    }
    private val handler = Handler()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View=inflater.inflate(R.layout.activity_main,container,false)
        //init
        downloadManager=  activity!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager



        view.findViewById<RecyclerView>(R.id.recycleview).layoutManager= LinearLayoutManager(context)
        recycleview=view.findViewById<RecyclerView>(R.id.recycleview)

        //end
        button=view.findViewById<FloatingActionButton>(R.id.btnAddLink)
        downloadMethod= DownloadMethod(context!!,downloadManager!!)
        return view
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==requestOpenDiaLog){
            if (resultCode==Activity.RESULT_OK){
                val diachi=data!!.getStringExtra("diachi")
               val url=data!!.getStringExtra("url")
                val downloadModel=downloadMethod!!.startDownload(url,diachi)
                arrayListDownload!!.add(downloadModel)
                downloadAdapter!!.updateDataSet(arrayListDownload!!)
                createDownloadItemView(downloadModel.id!!, downloadModel.url!!)
            }
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button!!.setOnClickListener {

            val intent:Intent=Intent(context,PopUpAddLink::class.java)
            intent.putExtra("trangthai",false)
            startActivityForResult(intent,requestOpenDiaLog)
        }




    }
    private fun createDownloadItemView(id: Long, url: String) {
        val q = DownloadManager.Query().setFilterById(id)
        val updateDownloadItem = object : Runnable {
            override fun run() {
                val cursor = downloadManager!!.query(q)
                if (cursor.moveToFirst()) {
                    val bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    val totalBytes = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    val statusCode = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    val reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                    var statusText = ""
                    var reasonText = ""
                    when (statusCode) {
                        DownloadManager.STATUS_FAILED -> {
                            statusText = "STATUS_FAILED"
                            when (reason) {
                                DownloadManager.ERROR_CANNOT_RESUME -> reasonText = "ERROR_CANNOT_RESUME"
                                DownloadManager.ERROR_DEVICE_NOT_FOUND -> reasonText = "ERROR_DEVICE_NOT_FOUND"
                                DownloadManager.ERROR_FILE_ALREADY_EXISTS -> reasonText = "ERROR_FILE_ALREADY_EXISTS"
                                DownloadManager.ERROR_FILE_ERROR -> reasonText = "ERROR_FILE_ERROR"
                                DownloadManager.ERROR_HTTP_DATA_ERROR -> reasonText = "ERROR_HTTP_DATA_ERROR"
                                DownloadManager.ERROR_INSUFFICIENT_SPACE -> reasonText = "ERROR_INSUFFICIENT_SPACE"
                                DownloadManager.ERROR_TOO_MANY_REDIRECTS -> reasonText = "ERROR_TOO_MANY_REDIRECTS"
                                DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> reasonText = "ERROR_UNHANDLED_HTTP_CODE"
                                DownloadManager.ERROR_UNKNOWN -> reasonText = "ERROR_UNKNOWN"
                            }
                        }
                        DownloadManager.STATUS_PAUSED -> {
                            statusText = "STATUS_PAUSED"
                            when (reason) {
                                DownloadManager.PAUSED_QUEUED_FOR_WIFI -> reasonText = "PAUSED_QUEUED_FOR_WIFI"
                                DownloadManager.PAUSED_UNKNOWN -> reasonText = "PAUSED_UNKNOWN"
                                DownloadManager.PAUSED_WAITING_FOR_NETWORK -> reasonText = "PAUSED_WAITING_FOR_NETWORK"
                                DownloadManager.PAUSED_WAITING_TO_RETRY -> reasonText = "PAUSED_WAITING_TO_RETRY"
                            }
                        }
                        DownloadManager.STATUS_PENDING -> statusText = "STATUS_PENDING"
                        DownloadManager.STATUS_RUNNING -> statusText = "STATUS_RUNNING"
                        DownloadManager.STATUS_SUCCESSFUL -> statusText = "STATUS_SUCCESSFUL"
                    }
                    var done:Boolean=false
                    for (i in arrayListDownload!!.indices) {
                        if (arrayListDownload!![i].id == id  ) {
                            if(arrayListDownload!![i].status ==DownloadManager.STATUS_SUCCESSFUL || arrayListDownload!![i].status ==DownloadManager.STATUS_FAILED){
                                sharedViewModel!!.setValue(arrayListDownload!![i])
                                done=true
                                break
                            }
                            else{
                                done=false
                                arrayListDownload!![i].status=(statusCode)
                                arrayListDownload!![i].downloaded=(bytesDownloaded)
                                arrayListDownload!![i].fileSize=(totalBytes)
                                sharedViewModel!!.setValue(arrayListDownload!![i])
                                downloadAdapter!!.updateDataSet(arrayListDownload!!)
                                break
                            }
                        }
                    }
                    if(!done){
                        handler.postDelayed(this,1000)
                    }
                }
                else {
                    for (i in arrayListDownload!!.indices) {
                        if (arrayListDownload!![i].id === id && arrayListDownload!!.get(i).status !== DownloadManager.STATUS_PAUSED) {
                            downloadAdapter!!.updateDataSet(arrayListDownload!!)
                            break
                        }
                    }
                    handler.removeCallbacks(this)
                }
                cursor.close()
                PreferencesUtility.setDownloadList(context, arrayListDownload!!,DOWNLOADALL)
            }
        }
        handler.post(updateDownloadItem)
    }

//    private fun resume(url: String, downloaded: Int) {
//        Log.d("kiemtra",""+downloaded)
//        val uri = Uri.parse(url)
//        val fileName = uri.lastPathSegment
//
//
//        val request = DownloadManager.Request(uri)
//          request.addRequestHeader("Range", "bytes="+downloaded+"-")
//        request.setTitle(fileName)
//        request.allowScanningByMediaScanner()
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
//
//        val id = downloadManager!!.enqueue(request)
//        // downloadManager.
//        arrayListDownload!!.add(DownloadModel(id, url, fileName!!))
//        downloadAdapter!!.updateDataSet(arrayListDownload!!)
//        createDownloadItemView(id, url)
//    }

   public interface OnDownloadAllFragmentItemAction {
        fun onAction(action: Int, id: Long)
    }

}