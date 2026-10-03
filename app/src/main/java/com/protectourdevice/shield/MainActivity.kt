package com.protectourdevice.shield

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.io.File

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 80, 50, 50)
        }

        val title = TextView(this).apply {
            text = "Device Shield Security"
            textSize = 24f
        }

        val status = TextView(this).apply {
            text = "Status: File Observer Active\nMonitoring Downloads & Media..."
            textSize = 16f
            setPadding(0, 30, 0, 30)
        }

        val scanButton = Button(this).apply {
            text = "Quick Scan Downloads"
            setOnClickListener {
                scanDownloads(status)
            }
        }

        layout.addView(title)
        layout.addView(status)
        layout.addView(scanButton)

        setContentView(layout)
    }

    private fun scanDownloads(statusView: TextView) {
        val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val apkFiles = downloadFolder.listFiles { file -> file.extension.equals("apk", ignoreCase = true) }

        if (apkFiles.isNullOrEmpty()) {
            statusView.text = "Scan Complete: No suspicious APK files found in Downloads."
        } else {
            statusView.text = "Alert: Found ${apkFiles.size} APK file(s). Inspecting risk..."
        }
    }

    private fun promptUninstall(packageName: String) {
        val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE).apply {
            data = Uri.parse("package:$packageName")
            putExtra(Intent.EXTRA_RETURN_RESULT, true)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }
}
