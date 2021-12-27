# DownloadManager for Android 10 and later

DownloadManager is the manage all types of the download task, it support latest Android OS also 

## Library Setup

Use the below gradle dependency for the implementation of the document picker library.

Add the dependency into you app level gradle file(build.gradle):

	dependencies {
	        implementation project(':library')
	}

## Usage

Add Below code is for the download task that you want single download or multiple.
but before that user you need to ask for the file access permission
```
// Rquest permission for the file access to create a folder into the storage
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
            } 
```
Put below code into your permission result override method to handle the library result:
```
   override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadTask()
            } else {
                Toast.makeText(this, "Permission not granted.", Toast.LENGTH_SHORT).show()
            }
        }
    }
```

Put the below code to start your downloading, you have multiple options to download.
single file download or multiple files download
```
private val url =
    "Your file url"
private var url1 =
    "Your file url"
private var url2 =
    "Your file url"
 
// Request for the multiple files download 

val file1 = File(externalCacheDir!!.absolutePath, "download1.jpg")
            val file3 = File(externalCacheDir!!.absolutePath, "download2.mp3")
            val file4 = File(externalCacheDir!!.absolutePath, "download3.mp4")
            DownloadPush.newRequest(url, file1.absolutePath)
                .tag(TAG)
                .forceReDownload(true)
                .submit()
            DownloadPush.newRequest(url1, file3.absolutePath)
                .tag(TAG)
                .forceReDownload(true)
                .submit()
            DownloadPush.newRequest(url2, file4.absolutePath)
                .tag(TAG)
                .forceReDownload(true)
                .submit() 
                
// Request for the one file download

DownloadPush.newRequestToDownload(
            url,
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
 
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Contribute your ideas and logic for the people like us.

Please make sure to update tests as appropriate.

