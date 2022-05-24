package io.birota.zoov.sdkble

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

object FakeSdkBLE {

    private val mBikeData = MutableLiveData<BikeData?>(null)
    var bikeData: LiveData<BikeData?> = mBikeData
        private set
        get() = mBikeData

    fun connect(serialNumber: Int, onSuccess: (() -> Unit), onError: ((ErrorSdkBle) -> Unit)) {
        val handler = Handler(Looper.getMainLooper())
        val r = Runnable {
            val bikeDataValue = mBikeData.value
            if (bikeDataValue != null)
                onError.invoke(ErrorSdkBle.AlreadyConnected)
            else {
                val random = Random(System.currentTimeMillis()).nextInt(1, 11)
                if (random < 3)
                    onError.invoke(ErrorSdkBle.BluetoothError)
                else {
                    mBikeData.postValue(BikeData(
                        serialNumber = serialNumber,
                        batteryLevel = (5..100).random(),
                        isConnected = true,
                        inTrip = false
                    ))
                    onSuccess.invoke()
                }
            }
        }
        handler.postDelayed(r, 1000)
    }

    fun disconnect() {
        mBikeData.postValue(null)
    }

    fun startTrip(onSuccess: (() -> Unit), onError: ((ErrorSdkBle) -> Unit)) {
        val handler = Handler(Looper.getMainLooper())
        val r = Runnable {
            val bikeDataValue = mBikeData.value
            if (bikeDataValue?.isConnected != true)
                onError.invoke(ErrorSdkBle.NotConnected)
            else if (bikeDataValue.inTrip)
                onError.invoke(ErrorSdkBle.AlreadyInTrip)
            else {
                val random = Random(System.currentTimeMillis()).nextInt(1, 11)
                if (random < 2)
                    onError.invoke(ErrorSdkBle.BluetoothError)
                else {
                    mBikeData.postValue(BikeData(
                        serialNumber = bikeDataValue.serialNumber,
                        batteryLevel = bikeDataValue.batteryLevel,
                        isConnected = true,
                        inTrip = true
                    ))
                    onSuccess.invoke()
                }
            }
        }
        handler.postDelayed(r, 1000)
    }

    fun endTrip(onSuccess: (() -> Unit), onError: ((ErrorSdkBle) -> Unit)) {
        val handler = Handler(Looper.getMainLooper())
        val r = Runnable {
            val bikeDataValue = mBikeData.value
            if (bikeDataValue?.isConnected != true)
                onError.invoke(ErrorSdkBle.NotConnected)
            else if (!bikeDataValue.inTrip)
                onError.invoke(ErrorSdkBle.NotInTrip)
            else {
                val random = Random(System.currentTimeMillis()).nextInt(1, 11)
                if (random < 2)
                    onError.invoke(ErrorSdkBle.BluetoothError)
                else {
                    mBikeData.postValue(BikeData(
                        serialNumber = bikeDataValue.serialNumber,
                        batteryLevel = bikeDataValue.batteryLevel,
                        isConnected = true,
                        inTrip = false
                    ))
                    onSuccess.invoke()
                }
            }
        }
        handler.postDelayed(r, 1000)
    }

    enum class ErrorSdkBle {
        AlreadyConnected,
        NotConnected,
        AlreadyInTrip,
        NotInTrip,
        BluetoothError
    }
}