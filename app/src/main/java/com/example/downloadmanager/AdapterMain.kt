package com.example.downloadmanager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.downloadmanager.Fragment.Fragment_Downloading
import com.example.downloadmanager.Fragment.Fragment_ALL
import com.example.downloadmanager.Fragment.Fragment_DownloadFail


class AdapterMain : FragmentStatePagerAdapter {
    var fragment_ALL: Fragment_ALL?=null
    var fragment_Downloading: Fragment_Downloading?=null
    var fragment_DownloadFail: Fragment_DownloadFail?=null
    constructor(fm:FragmentManager) : super(fm) {

        fragment_ALL= Fragment_ALL()
        fragment_Downloading= Fragment_Downloading()
        fragment_DownloadFail= Fragment_DownloadFail()
    }
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return fragment_ALL
            1 -> return fragment_Downloading
            2 -> return fragment_DownloadFail
            else -> return null
        }
    }

    override fun getCount(): Int {
        return 3
    }

}