package com.zaeem.filesdownloader.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.zaeem.filesdownloader.adapters.DownloadingAdapter
import com.zaeem.filesdownloader.databinding.MainActivityBinding
import com.zaeem.filesdownloader.helpers.AppConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityBinding: MainActivityBinding
    lateinit var adapter: DownloadingAdapter


    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        setupAdapter()
        setupUI()
    }

    private fun setupUI() {

        viewModel.data.observe(this, {

            it?.let {
                adapter.updateItems(it)
            }
        })

        /*
        * We will start observing our all enqeued works
        * */
        WorkManager.getInstance().getWorkInfosByTagLiveData(AppConstants.WORK_TAG)
            .observe(this, Observer {

                it?.let { _it ->
                    viewModel.handleWorkState(_it)
                }

            })

    }

    private fun setupAdapter() {
        adapter = DownloadingAdapter()
        mainActivityBinding.itemsRv.adapter = adapter
        mainActivityBinding.itemsRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}