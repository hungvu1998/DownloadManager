package com.example.downloadmanager

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import java.io.File

class DownloadMethod {
    var downloadManager:DownloadManager?=null
    var context:Context?=null
    constructor(context:Context,downloadManager: DownloadManager){
        this.context=context
        this.downloadManager=downloadManager
    }
    fun startDownload(url:String,diachi:String):DownloadModel{
        val uri: Uri = Uri.parse(url)
        val filename: String? =uri.lastPathSegment
        val request: DownloadManager.Request=DownloadManager.Request(uri)
        request.setTitle(filename)
        //request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationUri(Uri.fromFile(File(diachi,filename)))
        val id = downloadManager!!.enqueue(request)
        val downloadModel=DownloadModel(id,url,filename!!,diachi)
        return downloadModel
    }

}