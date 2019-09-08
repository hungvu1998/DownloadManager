package com.example.downloadmanager

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.downloadmanager.Fragment.Fragment_ALL
import android.app.Activity
import android.net.Uri


class AdapterLinkDownload() : RecyclerView.Adapter<AdapterLinkDownload.ViewHolder>() {
    var context: Context?=null
    var mData: ArrayList<DownloadModel>? = null
    private var listener: OnDownloadItemActionLink? =null
    val ACTION_DOWNLOAD = 0
    val requestOpenDiaLog=1
    constructor(context: Context, data: ArrayList<DownloadModel>,listener: OnDownloadItemActionLink) : this() {
        this.context = context
        this.mData = data
        this.listener=listener

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_link_download, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val model = mData!![position]


        holder.tvFilename.text=model.filename

        val  typeFile:String = model.filename!!.substring(model.filename!!.lastIndexOf(".")+1)


        if(typeFile == "mp4"|| typeFile == "AVI"){
            holder.imgAnh.setImageDrawable(context!!.resources.getDrawable(R.drawable.video))
        }
        else if(typeFile == "mp3"){
            holder.imgAnh.setImageDrawable(context!!.resources.getDrawable(R.drawable.iconauido))
        }
        else if(typeFile == "jpg" || typeFile == "jpeg" || typeFile == "png" || typeFile == "gif" ){
            holder.imgAnh.setImageDrawable(context!!.resources.getDrawable(R.drawable.iconphoto))
        }
        else if(typeFile == "html" || typeFile == "php"  ){
            holder.imgAnh.setImageDrawable(context!!.resources.getDrawable(R.drawable.iconhtml))
        }
        else{
            holder.imgAnh.setImageDrawable(context!!.resources.getDrawable(R.drawable.iconfile))
        }
        holder.cardView.setOnClickListener {
            listener!!.onAction(ACTION_DOWNLOAD,position)

        }

    }

    class ViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvFilename: TextView
        var imgAnh:ImageView
        var cardView:CardView

        init {
            cardView=itemView.findViewById(R.id.cardview)
            tvFilename = itemView.findViewById(R.id.fileName)
            imgAnh=itemView.findViewById(R.id.imgAnh)

        }
    }
    interface OnDownloadItemActionLink {
        fun onAction(action: Int,position: Int)
    }

}