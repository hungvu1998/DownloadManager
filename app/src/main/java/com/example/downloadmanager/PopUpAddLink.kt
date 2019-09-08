package com.example.downloadmanager


import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.downloadmanager.Fragment.Fragment_ALL
import lib.folderpicker.FolderPicker



class PopUpAddLink : AppCompatActivity() {


   // var listener:DialogListener?=null
     var txtdiachi:TextView?=null
    var diachi:String?=null
    var btnOkeDialog:Button?=null
    var btnCancel:Button?=null
    var url:EditText?=null
    var txtPhoto:TextView?=null
    var txtVideo:TextView?=null
    var txtHTML:TextView?=null
    var txtFile:TextView?=null
    var txtAudio:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog)
        btnOkeDialog=findViewById(R.id.btnOke)
        btnCancel=findViewById(R.id.btnCancel)
        txtdiachi=findViewById(R.id.txtDiaChi)
        txtHTML=findViewById(R.id.txtHTML)
        url=findViewById(R.id.edtTenURI)
        txtPhoto=findViewById(R.id.txtPhoto)
        txtFile=findViewById(R.id.txtFile)
        txtAudio=findViewById(R.id.txtAudio)
        txtVideo=findViewById(R.id.txtVideo)
        val intent = intent
        val boolean:Boolean=intent.getBooleanExtra("trangthai",false)
        var position:String?=null
        if(boolean){
            url!!.isEnabled = false
            val link:String=intent.getStringExtra("url")
           position=intent.getStringExtra("position")
            url!!.setText(link)
        }
        else{
            url!!.isEnabled=true
        }
        txtdiachi!!.setOnClickListener{
            val inTentDiaChi= Intent(this, FolderPicker::class.java)
            startActivityForResult(inTentDiaChi,10)
        }

        btnOkeDialog!!.setOnClickListener {
            if(!txtdiachi!!.text.toString().trim().equals( "...../"))
            {
                if(DMTextUtil.isValidUrl(url!!.text.toString().trim()))
                {
                    diachi= txtdiachi!!.text.trim().toString()
                  //  startDownload(url!!.text.toString().trim())
                    val intentBack:Intent=Intent()
                    intentBack.putExtra("diachi",""+diachi)
                    if(position!=null){
                        intentBack.putExtra("position",""+position)
                    }
                    intentBack.putExtra("url",""+url!!.text.toString().trim())
                    setResult(Activity.RESULT_OK,intentBack)
                    finish()
                }
                else{
                    Toast.makeText(this,"Link không cụ thể ",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Vui lòng chọn đường dẫn",Toast.LENGTH_SHORT).show()
            }

        }
        btnCancel!!.setOnClickListener {
            finish()
        }
        txtPhoto!!.setOnClickListener{
            url!!.setText("https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500&fbclid=IwAR2c0nV1fnnmXJczxSCfWrQEE4pHlHXlVoAcnwzo5UhaeQrrVNjLaAUgecQ")
        }
        txtVideo!!.setOnClickListener {
            url!!.setText("https://vredir.nixcdn.com/PreNCT16/OldTownRoadRemix-LilNasXBillyRayCyrus-5976886.mp4?st=XfTN6-lKeAaeW5sDCYkGnw&e=1567322491")
        }
        txtHTML!!.setOnClickListener {
            url!!.setText("https://www.nhaccuatui.com/video/anh-do-roi-day-trong-hieu.qDFUjGGkbkIBS.html")
        }
        txtFile!!.setOnClickListener {

            url!!.setText("http://fa.getpedia.net/data?q==YDN3MzMwADM0EjNyQjMwczM2wXM0YDO8VGel5Cc1RXZTVmbvxWYk5WY0NVZt9mcoN0L3AzL4AzL5EDMy8SZslmZvEGdhR2L")
        }
        txtAudio!!.setOnClickListener {
            url!!.setText(" https://aredir.nixcdn.com/NhacCuaTui986/MuaNhiemMau-2Shy-6037273.mp3?st=i9rs0HLMi7lZUie6Fs3LbQ&e=1566635225")
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            val folderLocation = data!!.extras!!.getString("data")
            txtdiachi!!.text=folderLocation.toString()
            diachi=folderLocation.toString()

        }
    }


//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//        if(context is DialogListener){
//            listener=context
//        }
//        else{
//            Toast.makeText(context,"must implement DialogCancelListener",Toast.LENGTH_SHORT)
//        }
//    }

//    interface  DialogListener{
//        fun applyData(fileName:String,path: String)
//    }

}