package com.zaeem.filesdownloader.repository

import com.zaeem.filesdownloader.data.DownloadData
import kotlinx.coroutines.flow.Flow

interface IDownloadRepository {

    fun startDownload(downloadData: List<DownloadData>)
}