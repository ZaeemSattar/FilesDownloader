package com.zaeem.filesdownloader.repository

import com.zaeem.filesdownloader.data.DownloadData
import com.zaeem.filesdownloader.workmanagers.DownloadWorker
import javax.inject.Inject

class DownloadRepository @Inject constructor() : IDownloadRepository {


    /*
    * Start work manager on the basis of provided input Data
    * */
    override fun startDownload(downloadData: List<DownloadData>) {


        DownloadWorker.run(downloadData)


    }
}