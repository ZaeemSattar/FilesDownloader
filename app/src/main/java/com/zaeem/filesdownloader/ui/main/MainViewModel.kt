package com.zaeem.filesdownloader.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import com.zaeem.filesdownloader.data.*
import com.zaeem.filesdownloader.helpers.AppConstants
import com.zaeem.filesdownloader.repository.DownloadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: DownloadRepository) : ViewModel() {


    private var _data = MutableLiveData<List<DownloadData>>()
    var data: LiveData<List<DownloadData>> = _data

    init {

        _data.value = getData()

    }

    fun startDownload(downloadData: List<DownloadData>) {

        repository.startDownload(downloadData)
    }

    fun updateState(id: Int, state: State) {

        _data.value?.get(id)!!.downloaded = state
        _data.postValue(data.value)

    }


    private fun getData(): List<DownloadData> {

        return mutableListOf(
            DownloadData(0, "https://www.gstatic.com/devrel-devsite/prod/vbd904f2719533e871e3800dda1bebc56aa0bc95c3c9d01c4d7cebcf129bdf26c/android/images/touchicon-180.png", STARTED),
            DownloadData(1, "https://www.gstatic.com/devrel-devsite/prod/vbd904f2719533e871e3800dda1bebc56aa0bc95c3c9d01c4d7cebcf129bdf26c/android/images/touchicon-180.png", STARTED),
            DownloadData(2, "https://www.gstatic.com/devrel-devsite/prod/vbd904f2719533e871e3800dda1bebc56aa0bc95c3c9d01c4d7cebcf129bdf26c/android/images/touchicon-180.png", STARTED),
            DownloadData(3, "https://www.gstatic.com/devrel-devsite/prod/vbd904f2719533e871e3800dda1bebc56aa0bc95c3c9d01c4d7cebcf129bdf26c/android/images/touchicon-180.png", STARTED),
            DownloadData(4, "https://www.gstatic.com/devrel-devsite/prod/vbd904f2719533e871e3800dda1bebc56aa0bc95c3c9d01c4d7cebcf129bdf26c/android/images/touchicon-180.png", STARTED),
            DownloadData(5, "https://www.gstatic.com/devrel-devsite/prod/vbd904f2719533e871e3800dda1bebc56aa0bc95c3c9d01c4d7cebcf129bdf26c/android/images/touchicon-180.png", NOT_STARTED),
            DownloadData(6, "https://www.gstatic.com/devrel-devsite/prod/vbd904f2719533e871e3800dda1bebc56aa0bc95c3c9d01c4d7cebcf129bdf26c/android/images/touchicon-180.png", NOT_STARTED),
            DownloadData(7, "https://www.gstatic.com/devrel-devsite/prod/vbd904f2719533e871e3800dda1bebc56aa0bc95c3c9d01c4d7cebcf129bdf26c/android/images/touchicon-180.png", NOT_STARTED),
            DownloadData(8, "https://www.gstatic.com/devrel-devsite/prod/vbd904f2719533e871e3800dda1bebc56aa0bc95c3c9d01c4d7cebcf129bdf26c/android/images/touchicon-180.png", NOT_STARTED),
            DownloadData(9, "https://www.gstatic.com/devrel-devsite/prod/vbd904f2719533e871e3800dda1bebc56aa0bc95c3c9d01c4d7cebcf129bdf26c/android/images/touchicon-180.png", NOT_STARTED)
        )
    }

    fun handleWorkState(it: List<WorkInfo>) {

        /*
        * If our work status are empty means we don't have any work schedule
        * We pick out first 5 items to enque
        *
        * */

        if (it.isEmpty()) {
            startDownload(data.value!!.take(5))
        } else {

            /*
            *  Loop through every element and find out element who is back from work manager
            * and its value is started
            * if its state is STARTED then we will update its state to COMPLETED
            * */
            it.filter { _it -> _it.state == WorkInfo.State.SUCCEEDED }.let { filteredItems ->
                /*
                *
                * Loop through succeeded works and update its value in local UI
                * */
                filteredItems.forEach { _it ->

                    val indexToCheck = _it.outputData.getInt(AppConstants.WORK_OUTPUT, -1)
                    if (indexToCheck != -1) {
                        if (data.value!![indexToCheck].downloaded == STARTED) {

                            updateState(indexToCheck, COMPLETED)
                        }
                    }

                }
            }


            /*
            * filter and count started items if they are not 5 or more
            * and find its first item and pass to work manager to start downloading
            * */
            if (data.value!!.filter { it.downloaded == STARTED }.size <= 4) {
                val remaining = data.value!!.filter { it.downloaded == NOT_STARTED }
                if (remaining.isNotEmpty()) {
                    val nextItemToStart = remaining.take(1)
                    /*
                    * Update state on UI that new task is started
                    * */
                    updateState(nextItemToStart[0].id, STARTED)
                    startDownload(nextItemToStart)


                }

            }

        }

    }

}