package com.zaeem.filesdownloader.workmanagers

import android.content.Context
import androidx.work.*
import com.zaeem.filesdownloader.data.DownloadData
import com.zaeem.filesdownloader.helpers.AppConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.net.URL

class DownloadWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    companion object {

        fun run(data: List<DownloadData>) {


            if (data.size == 1) {
                val workData = Data.Builder().put(AppConstants.WORK_INPUT, data[0].id)
                    .put(AppConstants.WORK_INPUT_URL, data[0].url).build()
                val work = OneTimeWorkRequestBuilder<DownloadWorker>().setInputData(workData)
                    .addTag(AppConstants.WORK_TAG).build()
                WorkManager.getInstance().enqueue(work)

            } else {
                val list = mutableListOf<OneTimeWorkRequest>()
                for (element in data) {
                    val workData = Data.Builder().put(AppConstants.WORK_INPUT, element.id)
                        .put(AppConstants.WORK_INPUT_URL, element.url).build()
                    val work = OneTimeWorkRequestBuilder<DownloadWorker>().setInputData(workData)
                        .addTag(AppConstants.WORK_TAG).build()
                    list.add(work)
                }
                WorkManager.getInstance().beginWith(list).enqueue()

            }


        }


    }


    override suspend fun doWork(): Result = coroutineScope {


        val output = Data.Builder()
            .putInt(AppConstants.WORK_OUTPUT, inputData.getInt(AppConstants.WORK_INPUT, 0))
            .build()

        downloadWebPage(urlString = inputData.getString(AppConstants.WORK_INPUT_URL) ?: "")
        Result.success(output)
    }


    /* Download file and save in cache storage with temp file*/
    suspend fun downloadWebPage(
        urlString: String
    ) {
        val outputDir: File = applicationContext.cacheDir // context being the Activity pointer

        val outputFile =
            File.createTempFile(System.currentTimeMillis().toString(), ".png", outputDir)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Create a URL object
                val url = URL(urlString)
                // Create a buffered reader object using the url object
                val reader = url.openStream().bufferedReader()

                // Enter filename in which you want to download
                val downloadFile = outputFile.also { it.createNewFile() }
                // Create a buffered writer object for the file
                val writer = FileWriter(downloadFile).buffered()

                // read and write each line from the stream till the end
                var line: String
                while (reader.readLine().also { line = it?.toString() ?: "" } != null)
                    writer.write(line)

                // Close all open streams
                reader.close()
                writer.close()


            } catch (e: Exception) {

                /*
                * We can provide our retry policy
                * */

            }
        }
    }
}