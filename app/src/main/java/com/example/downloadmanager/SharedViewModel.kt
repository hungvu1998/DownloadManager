package com.example.downloadmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel(){
    var mutableLiveData: MutableLiveData<DownloadModel>?= MutableLiveData<DownloadModel>()

    public fun setValue(downloadModel: DownloadModel){
        mutableLiveData!!.value=downloadModel
    }
    fun getValue(): LiveData<DownloadModel> {
        return mutableLiveData!!

    }
}