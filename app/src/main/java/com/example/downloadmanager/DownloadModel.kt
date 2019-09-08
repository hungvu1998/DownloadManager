package com.example.downloadmanager

import android.app.DownloadManager

class DownloadModel {
    var id: Long? = null
    var url: String? = null
    var filename: String? = null
    var status:Int = DownloadManager.STATUS_PENDING
    var fileSize: Int? = null
    var downloaded: Int? = null
    var diachi:String?=null
    constructor(id:Long,url:String,fileName:String,diachi:String){
        this.id=id
        this.url=url
        this.filename=fileName
        this.diachi=diachi
    }
    constructor(url: String,fileName: String){
        this.url=url
        this.filename=fileName
    }
}