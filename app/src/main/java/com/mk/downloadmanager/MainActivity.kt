package com.mk.downloadmanager

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mk.library.Manager.DownloadConfig
import com.mk.library.Manager.DownloadPush
import com.mk.library.Manager.core.DownloadListener
import com.mk.library.Manager.utils.LogUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

private val url =
    "https://d3i0sngc65p0xs.cloudfront.net/audio/1639377784992_photo-1639377784981.mp3"

private val url2 =
    "https://d3i0sngc65p0xs.cloudfront.net/audio/1639377784992_photo-1639377784981.mp3"
private var url4 =
    "https://d3i0sngc65p0xs.cloudfront.net/audio/1639377784992_photo-1639377784981.mp3"
private var url5 =
    "https://d3i0sngc65p0xs.cloudfront.net/audio/1639377784992_photo-1639377784981.mp3"


class MainActivity : AppCompatActivity() {
    private val TAG: String = "DownloadActivity"
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initProgressDialog()

        DownloadConfig.newBuilder()
            //Optional,set the maximum number of tasks to run, default 3.
            .setMaxRunningTaskNum(2)
            //Optional,set the minimum available storage space size for downloading to avoid insufficient storage space during downloading, default is 4kb.
            .setMinUsableStorageSpace(4 * 1024L)
            .build()

        add_task.setOnClickListener {
            askForPermission()
        }

        add_download_list.setOnClickListener {
            val file1 = File(externalCacheDir!!.absolutePath, "download1.mp3")
            val file3 = File(externalCacheDir!!.absolutePath, "download2.mp3")
            val file4 = File(externalCacheDir!!.absolutePath, "download3.mp3")
            DownloadPush.newRequest(url, file1.absolutePath)
                .tag(TAG)
                .forceReDownload(true)
                .submit()
            DownloadPush.newRequest(url4, file3.absolutePath)
                .tag(TAG)
                .forceReDownload(true)
                .submit()
            DownloadPush.newRequest(url5, file4.absolutePath)
                .tag(TAG)
                .forceReDownload(true)
                .submit()
        }
    }

    private fun askForPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            downloadList()
        } else {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
                downloadList()
            }
        }
    }

    private fun downloadList() {
        progressDialog.progress = 0
        progressDialog.show()
        DownloadPush.newRequestToDownload(
            "https://d3i0sngc65p0xs.cloudfront.net/audio/1639377784992_photo-1639377784981.mp3",
            "/myTestApp"
        )
            .listener(object : DownloadListener() {

                override fun onProgress(progress: Int) {
                    progressDialog.progress = progress
                }

                override fun onSuccess() {
                    progressDialog.dismiss()
                    val apkPath = downloadInfo.filePath
                    Toast.makeText(this@MainActivity, "Download Finished", Toast.LENGTH_SHORT)
                        .show()
                    LogUtil.e("Download Finished" + downloadInfo.downloadFile.realPath)
                }

                override fun onFailed() {
                    progressDialog.dismiss()
                    Toast.makeText(this@MainActivity, "Download failed", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            //Optionally,Set whether to repeatedly download the downloaded file,default false.
            .forceReDownload(true)
            //Optionally,Set how many threads are used when downloading,default 3.
            .threadNum(3)
            .setRetry(3, 200)
            .submit()
    }


    private fun initProgressDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Downloading")
        //        progressDialog.setMessage("Downloading now...");
        progressDialog.progress = 0
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
    }

    override fun onStop() {
        super.onStop()
        DownloadPush.unSubscribe(url2)
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog.dismiss()
        //shutdown will stop all tasks and release some resource.
        DownloadPush.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadList()
            } else {
                Toast.makeText(this, "Permission not granted.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}