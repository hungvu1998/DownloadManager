package com.example.downloadmanager

import android.app.DownloadManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.net.Uri


class DownloadAdapter() : RecyclerView.Adapter<DownloadAdapter.ViewHolder>() {
    var context: Context?=null
    var mData: MutableList<DownloadModel>? = null
    private var listener: OnDownloadItemAction? =null
    val ACTION_RESUME = 1
    val ACTION_PAUSE = 2
    val ACTION_STOP = 3
    val ACTION_DELETE = 0
    val ACTION_RETRY = 4
    fun updateItem(position: Int, model: DownloadModel) {
        mData!![position] = model
        notifyItemChanged(position)

    }

    fun updateDataSet(data: MutableList<DownloadModel>) {

        this.mData = data
        notifyDataSetChanged()
    }
    constructor(context: Context, data: MutableList<DownloadModel>, listener: OnDownloadItemAction) : this() {
        this.context = context
        this.mData = data
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_download, parent, false)
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
        holder.imgCancel.setOnClickListener {
            listener!!.onAction(ACTION_DELETE,model.id!!)

        }
            when (model.status)
            {
                DownloadManager.STATUS_FAILED -> {
                    holder.imgStart.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_replay_black_24dp))
                    holder.imgStart.setOnClickListener {
                        listener!!.onAction(ACTION_RETRY,model.id!!)
                    }
                    holder.pb.visibility = View.INVISIBLE
                    holder.tvPercentProgress.visibility = View.INVISIBLE
                    holder.tvSizeProgress.text = getStatusString(model.status)
                }
                DownloadManager.STATUS_SUCCESSFUL -> {
                    holder.imgStart.visibility = View.INVISIBLE
                    holder.pb.visibility = View.INVISIBLE
                    holder.tvPercentProgress.visibility = View.INVISIBLE
                    holder.tvSizeProgress.text = getStatusString(model.status)
                    holder.imgAnh.setOnClickListener {
                        Log.d("kiemtra",""+model.diachi)
                        openFolder(model.diachi.toString()+"/"+model.filename)

                    }
                }
                DownloadManager.STATUS_RUNNING -> {
                    holder.imgStart.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_pause_circle_filled_black_24dp))
                    holder.imgStart.setOnClickListener {
                        listener!!.onAction(ACTION_PAUSE,model.id!!)
                    }
                    holder.pb.progress = DMTextUtil.getPercentProgress(model.downloaded!!, model.fileSize!!)
                    holder.tvPercentProgress.text = DMTextUtil.getPercentProgressString(model.downloaded!!, model.fileSize!!)
                    holder.tvSizeProgress.text = DMTextUtil.getSizeProgressString(model.downloaded!!, model.fileSize!!)

                }
                DownloadManager.STATUS_PAUSED -> {
                    holder.imgStart.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_play_circle_filled_black_24dp))
                    holder.imgStart.setOnClickListener {
                        listener!!.onAction(ACTION_RESUME,model.id!!)
                    }
                    holder.pb.progress = DMTextUtil.getPercentProgress(model.downloaded!!, model.fileSize!!)
                    holder.tvPercentProgress.text = DMTextUtil.getPercentProgressString(model.downloaded!!, model.fileSize!!)
                    holder.tvSizeProgress.text = DMTextUtil.getSizeProgressString(model.downloaded!!, model.fileSize!!)

                }
            }

    }
    fun openFolder(location: String) {
        // location = "/sdcard/my_folder";
        val intentr = Intent(Intent.ACTION_VIEW)
        val mydir = Uri.parse("file://$location")
        intentr.setDataAndType(mydir, "application/*")    // or use */*
        context!!.startActivity(intentr)
    }
    class ViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvFilename: TextView
        var tvSizeProgress: TextView
        var tvPercentProgress: TextView
        var pb: ProgressBar
        var imgStart:ImageView
        var imgCancel:ImageView
        var imgAnh:ImageView

       init {

           tvFilename = itemView.findViewById(R.id.fileName)
           tvPercentProgress = itemView.findViewById(R.id.percentProgress)
           tvSizeProgress = itemView.findViewById(R.id.sizeProgress)
           pb = itemView.findViewById(R.id.progress)
           imgCancel=itemView.findViewById(R.id.btnStop)
           imgStart=itemView.findViewById(R.id.btnAction)
           imgAnh=itemView.findViewById(R.id.imgAnh)

       }


    }


    private fun getStatusString(code: Int): String {
        when (code) {
            DownloadManager.STATUS_PAUSED -> {
                return "PAUSED"
            }
            DownloadManager.STATUS_FAILED -> {
                return "Failed"
            }
            DownloadManager.STATUS_RUNNING -> {
                return "Downloading"
            }
            DownloadManager.STATUS_SUCCESSFUL -> {
                return "Successful"
            }
            else -> return "Pending"
        }
    }
    interface OnDownloadItemAction {
        fun onAction(action: Int, id: Long)
    }
}