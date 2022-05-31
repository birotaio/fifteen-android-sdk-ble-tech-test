package io.birota.zoov.technicaltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.birota.zoov.sdkble.FakeSdkBLE
import io.birota.zoov.technicaltest.databinding.ActivitySampleBinding

class SampleActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonConnect.setOnClickListener {
            val serialNumber = getSerialNumber()
            serialNumber?.let {
                FakeSdkBLE.connect(serialNumber = it, onSuccess = {
                    Toast.makeText(this, "Connection Success", Toast.LENGTH_LONG).show()
                }, onError = {
                    Toast.makeText(this, "Connection Fail ${it.name}", Toast.LENGTH_LONG).show()
                })
            }
        }

        binding.buttonStart.setOnClickListener {
            FakeSdkBLE.unlockBike(onSuccess = {
                Toast.makeText(this, "Start Success", Toast.LENGTH_LONG).show()
            }, onError = {
                Toast.makeText(this, "Start Fail ${it.name}", Toast.LENGTH_LONG).show()
            })
        }

        binding.buttonEnd.setOnClickListener {
            FakeSdkBLE.lockBike(onSuccess = {
                Toast.makeText(this, "Lock Success", Toast.LENGTH_LONG).show()
            }, onError = {
                Toast.makeText(this, "Lock Fail ${it.name}", Toast.LENGTH_LONG).show()
            })
        }

        binding.buttonDisconnect.setOnClickListener {
            FakeSdkBLE.disconnect()
        }

        FakeSdkBLE.bikeData.observe(this) { bikeData ->
            bikeData?.let {
                binding.textViewInfo.text = "Serial number = ${it.serialNumber}, Battery Level = ${it.batteryLevel}, Is Locked = ${it.isLocked}, Is Connected = ${it.isConnected}"
            } ?: run {
                binding.textViewInfo.text = "No bike connected"
            }
        }
    }

    private fun getSerialNumber(): String? {
        val serialNumber = binding.editTextNumber.text.toString()

        return if (serialNumber.isNullOrBlank()) {
            Toast.makeText(this, "Serial number not valid", Toast.LENGTH_LONG).show()
            null
        } else
            serialNumber
    }
}