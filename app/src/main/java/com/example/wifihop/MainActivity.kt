//package com.example.wifihop
//
//import android.Manifest
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.content.pm.PackageManager
//import android.net.wifi.WifiManager
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var wifiManager: WifiManager
//    private lateinit var statusText: TextView
//    private lateinit var outputText: TextView
//
//    private var currentLocation = ""
//    private val scanLimit = 100
//    private val collectedData = mutableMapOf<String, MutableList<Int>>()
//
//    // WiFi scan broadcast receiver
//    private val wifiReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            val results = wifiManager.scanResults
//
//            // Pick the strongest AP (you could modify to average multiple)
//            val signal = results.firstOrNull()?.level
//
//            if (signal == null) {
//                statusText.text = "Scan failed. No APs found!"
//                return
//            }
//
//            collectedData[currentLocation]?.add(signal)
//
//            val count = collectedData[currentLocation]?.size ?: 0
//            statusText.text = "📡 Scanning $currentLocation... [$count / $scanLimit]"
//
//            if (count < scanLimit) {
//                wifiManager.startScan()
//            } else {
//                statusText.text = "✅ Completed scan for $currentLocation"
//                showResult(currentLocation)
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//
//        val btnA = findViewById<Button>(R.id.btnLocationA)
//        val btnB = findViewById<Button>(R.id.btnLocationB)
//        val btnC = findViewById<Button>(R.id.btnLocationC)
//        statusText = findViewById(R.id.statusText)
//        outputText = findViewById(R.id.outputText)
//
//        checkPermission()
//
//        btnA.setOnClickListener { startScanning("Location A") }
//        btnB.setOnClickListener { startScanning("Location B") }
//        btnC.setOnClickListener { startScanning("Location C") }
//
//        registerReceiver(wifiReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
//    }
//
//    private fun checkPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                1001
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 1001 && (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
//            Toast.makeText(this, "Permission required to scan WiFi", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    private fun startScanning(location: String) {
//        currentLocation = location
//        collectedData[location] = mutableListOf()
//        outputText.append("\n\n📍 Starting scan for $location...\n")
//        statusText.text = "Starting scan for $location..."
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            wifiManager.startScan()
//        } else {
//            checkPermission()
//        }
//    }
//
//    private fun showResult(location: String) {
//        val data = collectedData[location] ?: return
//        val chunks = data.chunked(10).joinToString("\n") { it.joinToString(", ") }
//
//        outputText.append("\n\n📌 $location Results:\n$chunks\n")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(wifiReceiver)
//    }
//}
package com.example.wifihop

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var wifiManager: WifiManager
    private lateinit var statusText: TextView
    private lateinit var outputText: TextView

    private var currentLocation = ""
    private val scanLimit = 100
    private val collectedData = mutableMapOf<String, MutableList<Int>>()
    private var completedLocations = mutableSetOf<String>()

    private val permissionRequestCode = 1001

    private val wifiReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // ✅ First, check if Location permission is granted
            if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                statusText.text = "❌ Location permission not granted. Cannot scan WiFi."
                return
            }

            val results = wifiManager.scanResults

            // Pick the strongest AP
            val signal = results.firstOrNull()?.level

            if (signal == null) {
                statusText.text = "⚠️ Scan failed. No APs found!"
                return
            }

            collectedData[currentLocation]?.add(signal)

            val count = collectedData[currentLocation]?.size ?: 0
            statusText.text = "📡 Scanning $currentLocation... [$count / $scanLimit]"

            if (count < scanLimit) {
                wifiManager.startScan()
            } else {
                statusText.text = "✅ Completed scan for $currentLocation"
                showResult(currentLocation)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val btnA = findViewById<Button>(R.id.btnLocationA)
        val btnB = findViewById<Button>(R.id.btnLocationB)
        val btnC = findViewById<Button>(R.id.btnLocationC)
        statusText = findViewById(R.id.statusText)
        outputText = findViewById(R.id.outputText)

        checkPermission()

        btnA.setOnClickListener { startScanning("Location A") }
        btnB.setOnClickListener { startScanning("Location B") }
        btnC.setOnClickListener { startScanning("Location C") }

        registerReceiver(wifiReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                permissionRequestCode
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission required to scan WiFi", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission granted. You can now scan WiFi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startScanning(location: String) {
        currentLocation = location
        collectedData[location] = mutableListOf()
        outputText.append("\n\n📍 Starting scan for $location...\n")
        statusText.text = "Starting scan for $location..."

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            wifiManager.startScan()
        } else {
            checkPermission()
        }
    }

    private fun showResult(location: String) {
        val data = collectedData[location] ?: return
        val chunks = data.chunked(10).joinToString("\n") { it.joinToString(", ") }

        outputText.append("\n\n📌 $location Results:\n$chunks\n")

        completedLocations.add(location)

        if (completedLocations.containsAll(listOf("Location A", "Location B", "Location C"))) {
            calculateRanges()
        }
    }

    private fun calculateRanges() {
        outputText.append("\n\n📊 Signal Strength Ranges:")

        for ((location, data) in collectedData) {
            if (data.isNotEmpty()) {
                val maxSignal = data.maxOrNull() ?: continue
                val minSignal = data.minOrNull() ?: continue
                val range = maxSignal - minSignal
                outputText.append("\n🔹 $location: Range = $range dBm (Max: $maxSignal, Min: $minSignal)")
            }
        }

        Toast.makeText(this, "All locations scanned. Ranges calculated!", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(wifiReceiver)
    }
}
