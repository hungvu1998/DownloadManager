package com.example.downloadmanager.Fragment

import android.app.DownloadManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.downloadmanager.*


class Fragment_Downloading : Fragment(),DownloadAdapter.OnDownloadItemAction {

    override fun onAction(action: Int, id: Long) {
        when (action) {
            downloadAdapter!!.ACTION_DELETE -> {

                for(i in arrayListDownload!!.indices){
                    if (arrayListDownload!![i].id == id){
                        arrayListDownload!!.removeAt(i)
                        break
                    }
                }
                downloadAdapter!!.updateDataSet(arrayListDownload!!)
                PreferencesUtility.setDownloadList(context, arrayListDownload!!,DOWNLOADING)
            }
        }
    }
    val DOWNLOADING = "com.example.downloadmanager.Downloading"
    val sharedViewModel by lazy {
        ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
    }
    private val handler = Handler()
    var arrayListDownload:MutableList<DownloadModel> ?=null
    var downloadAdapter: DownloadAdapter?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view:View=inflater.inflate(R.layout.fragment_favorite,container,false)
        arrayListDownload=PreferencesUtility.getDownloadList(context,DOWNLOADING)
        if(arrayListDownload == null){

            arrayListDownload= ArrayList<DownloadModel>()
        }
        view.findViewById<RecyclerView>(R.id.recyclerviewDownloaded).layoutManager= LinearLayoutManager(context)
        downloadAdapter= DownloadAdapter(context!!, arrayListDownload!!,this)
        view.findViewById<RecyclerView>(R.id.recyclerviewDownloaded).adapter=downloadAdapter
        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel.getValue().observe(viewLifecycleOwner,object : Observer<DownloadModel>
        {
            override fun onChanged(t: DownloadModel?) {
                var tontai=false
                Log.d("kiemtra",""+t!!.status)

                    if(arrayListDownload!!.size==0){
                        arrayListDownload!!.add(t)
                        downloadAdapter!!.updateDataSet(arrayListDownload!!)
                    }
                    else{
                        for (i in arrayListDownload!!.indices){
                            if(arrayListDownload!![i].id==t.id){
                               tontai=true
                                break
                            }
                        }
                        if(tontai==true){
                            updateItem(t)
                        }
                        else{
                            arrayListDownload!!.add(t)
                            downloadAdapter!!.updateDataSet(arrayListDownload!!)
                        }
                    }






            }

        })
    }
    fun updateItem(t:DownloadModel){

        val updateDownloadItem = object : Runnable {
            override fun run() {
                for (i in arrayListDownload!!.indices) {
                    if (arrayListDownload!![i].id == t!!.id) {
                        if (arrayListDownload!![i].status == DownloadManager.STATUS_SUCCESSFUL  || arrayListDownload!![i].status == DownloadManager.STATUS_FAILED   ) {
                            arrayListDownload!!.removeAt(i)
                            downloadAdapter!!.updateDataSet(arrayListDownload!!)
                            break
                        } else {
                            downloadAdapter!!.updateItem(i, t)
                            break
                        }


                    }
                }
                handler.postDelayed(this, 1000)
                PreferencesUtility.setDownloadList(context, arrayListDownload!!,DOWNLOADING)
            }
        }
        handler.post(updateDownloadItem)
    }
}