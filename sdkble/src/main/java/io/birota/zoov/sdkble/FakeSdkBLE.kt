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

    fun connect(serialNumber: String, onSuccess: (() -> Unit), onError: ((ErrorSdkBle) -> Unit)) {
        val bikeDataValue = mBikeData.value
        if (bikeDataValue != null)
            return onError.invoke(ErrorSdkBle.AlreadyConnected)
        val handler = Handler(Looper.getMainLooper())
        val r = Runnable {
            val random = Random(System.currentTimeMillis()).nextInt(1, 11)
            if (random < 3)
                onError.invoke(ErrorSdkBle.BluetoothError)
            else {
                mBikeData.postValue(BikeData(
                    serialNumber = serialNumber,
                    batteryLevel = (5..100).random(),
                    isConnected = true,
                    isLocked = true
                ))
                onSuccess.invoke()
            }
        }
        handler.postDelayed(r, 1000)
    }

    fun disconnect() {
        mBikeData.postValue(null)
    }

    fun unlockBike(onSuccess: (() -> Unit), onError: ((ErrorSdkBle) -> Unit)) {
        val bikeDataValue = mBikeData.value
        if (bikeDataValue?.isConnected != true)
            return onError.invoke(ErrorSdkBle.NotConnected)
        else if (!bikeDataValue.isLocked)
            return onError.invoke(ErrorSdkBle.AlreadyUnlocked)

        val handler = Handler(Looper.getMainLooper())
        val r = Runnable {
            val random = Random(System.currentTimeMillis()).nextInt(1, 11)
            if (random < 2)
                onError.invoke(ErrorSdkBle.BluetoothError)
            else {
                mBikeData.postValue(BikeData(
                    serialNumber = bikeDataValue.serialNumber,
                    batteryLevel = bikeDataValue.batteryLevel,
                    isConnected = true,
                    isLocked = false
                ))
                onSuccess.invoke()
            }
        }
        handler.postDelayed(r, 1000)
    }

    fun lockBike(onSuccess: (() -> Unit), onError: ((ErrorSdkBle) -> Unit)) {
        val bikeDataValue = mBikeData.value
        if (bikeDataValue?.isConnected != true)
            return onError.invoke(ErrorSdkBle.NotConnected)
        else if (bikeDataValue.isLocked)
            return onError.invoke(ErrorSdkBle.AlreadyLocked)

        val handler = Handler(Looper.getMainLooper())
        val r = Runnable {
            val random = Random(System.currentTimeMillis()).nextInt(1, 11)
            if (random < 2)
                onError.invoke(ErrorSdkBle.BluetoothError)
            else {
                mBikeData.postValue(BikeData(
                    serialNumber = bikeDataValue.serialNumber,
                    batteryLevel = bikeDataValue.batteryLevel,
                    isConnected = true,
                    isLocked = true
                ))
                onSuccess.invoke()
            }
        }
        handler.postDelayed(r, 1000)
    }

    enum class ErrorSdkBle {
        AlreadyConnected,
        NotConnected,
        AlreadyUnlocked,
        AlreadyLocked,
        BluetoothError
    }
}