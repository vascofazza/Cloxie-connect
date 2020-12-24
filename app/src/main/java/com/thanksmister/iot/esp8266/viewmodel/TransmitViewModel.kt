/*
 * Copyright (c) 2018 ThanksMister LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thanksmister.iot.esp8266.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.thanksmister.iot.esp8266.R
import com.thanksmister.iot.esp8266.api.EspApi
import com.thanksmister.iot.esp8266.api.NetworkResponse
import com.thanksmister.iot.esp8266.api.ParameterResponse
import com.thanksmister.iot.esp8266.persistence.MessageDao
import com.thanksmister.iot.esp8266.persistence.Preferences
import com.thanksmister.iot.esp8266.util.DateUtils
import com.thanksmister.iot.esp8266.vo.Message
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class TransmitViewModel @Inject
constructor(
        application: Application, private val dataSource: MessageDao,
        private val configuration: Preferences
) : AndroidViewModel(application) {

    private val toastText = ToastMessage()
    private val snackbarText = SnackbarMessage()
    private val alertText = AlertMessage()
    private val networkResponse = MutableLiveData<NetworkResponse<ParameterResponse>>()
    private val disposable = CompositeDisposable()

    fun networkResponse(): MutableLiveData<NetworkResponse<ParameterResponse>> {
        return networkResponse
    }

    fun getToastMessage(): ToastMessage {
        return toastText
    }

    fun getAlertMessage(): AlertMessage {
        return alertText
    }

    fun getSnackbarMessage(): SnackbarMessage {
        return snackbarText
    }

    init {
        // na-da
    }

    private fun showSnackbarMessage(message: Int?) {
        snackbarText.value = message
    }

    private fun showAlertMessage(message: String?) {
        toastText.value = message
    }

    private fun showToastMessage(message: String?) {
        toastText.value = message
    }

    fun readParameters() {
        if (!TextUtils.isEmpty(configuration.address())) {
            val api = EspApi(configuration.address()!!)
            disposable.add(api.getParameters()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { networkResponse.value = NetworkResponse.loading() }
                    .subscribeWith(object : DisposableObserver<ParameterResponse>() {
                        override fun onNext(response: ParameterResponse) {

                            networkResponse.value = NetworkResponse.success(response)
                        }

                        override fun onComplete() {
                            Timber.d("complete");
                            //networkResponse.value = NetworkResponse.success(null)
                        }

                        override fun onError(error: Throwable) {
                            networkResponse.value = NetworkResponse.error(error)
                            var errorMessage: String? = "Server error"
                            if (!TextUtils.isEmpty(error.message)) {
                                errorMessage = error.message
                            }
                            insertMessageResponse("error", errorMessage!!)
                            Timber.e("error: " + error.message);
                        }
                    })
            )
        } else {
            showAlertMessage(getApplication<Application>().getString(R.string.error_empty_address))
        }
    }

    fun sendTimerStop() {
        if (!TextUtils.isEmpty(configuration.address())) {
            val api = EspApi(configuration.address()!!)
            disposable.add(api.sendTimerStop()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { networkResponse.value = NetworkResponse.loading() }
                    .subscribeWith(object : DisposableObserver<String>() {
                        override fun onNext(response: String) {

                        }

                        override fun onComplete() {
                            Timber.d("complete");
                            //networkResponse.value = NetworkResponse.success(null)
                        }

                        override fun onError(error: Throwable) {
                            networkResponse.value = NetworkResponse.error(error)
                            var errorMessage: String? = "Server error"
                            if (!TextUtils.isEmpty(error.message)) {
                                errorMessage = error.message
                            }
                            insertMessageResponse("error", errorMessage!!)
                            Timber.e("error: " + error.message);
                        }
                    })
            )
        } else {
            showAlertMessage(getApplication<Application>().getString(R.string.error_empty_address))
        }
    }

    fun sendTimerPause() {
        if (!TextUtils.isEmpty(configuration.address())) {
            val api = EspApi(configuration.address()!!)
            disposable.add(api.sendTimerPause()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { networkResponse.value = NetworkResponse.loading() }
                    .subscribeWith(object : DisposableObserver<String>() {
                        override fun onNext(response: String) {

                        }

                        override fun onComplete() {
                            Timber.d("complete");
                            //networkResponse.value = NetworkResponse.success(null)
                        }

                        override fun onError(error: Throwable) {
                            networkResponse.value = NetworkResponse.error(error)
                            var errorMessage: String? = "Server error"
                            if (!TextUtils.isEmpty(error.message)) {
                                errorMessage = error.message
                            }
                            insertMessageResponse("error", errorMessage!!)
                            Timber.e("error: " + error.message);
                        }
                    })
            )
        } else {
            showAlertMessage(getApplication<Application>().getString(R.string.error_empty_address))
        }
    }

    fun sendStopWatchStart() {
        if (!TextUtils.isEmpty(configuration.address())) {
            val api = EspApi(configuration.address()!!)
            disposable.add(api.sendStopWatchStart()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { networkResponse.value = NetworkResponse.loading() }
                    .subscribeWith(object : DisposableObserver<String>() {
                        override fun onNext(response: String) {

                        }

                        override fun onComplete() {
                            Timber.d("complete");
                            //networkResponse.value = NetworkResponse.success(null)
                        }

                        override fun onError(error: Throwable) {
                            networkResponse.value = NetworkResponse.error(error)
                            var errorMessage: String? = "Server error"
                            if (!TextUtils.isEmpty(error.message)) {
                                errorMessage = error.message
                            }
                            insertMessageResponse("error", errorMessage!!)
                            Timber.e("error: " + error.message);
                        }
                    })
            )
        } else {
            showAlertMessage(getApplication<Application>().getString(R.string.error_empty_address))
        }
    }

    fun sendStopWatchPause() {
        if (!TextUtils.isEmpty(configuration.address())) {
            val api = EspApi(configuration.address()!!)
            disposable.add(api.sendStopWatchPause()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { networkResponse.value = NetworkResponse.loading() }
                    .subscribeWith(object : DisposableObserver<String>() {
                        override fun onNext(response: String) {

                        }

                        override fun onComplete() {
                            Timber.d("complete");
                            //networkResponse.value = NetworkResponse.success(null)
                        }

                        override fun onError(error: Throwable) {
                            networkResponse.value = NetworkResponse.error(error)
                            var errorMessage: String? = "Server error"
                            if (!TextUtils.isEmpty(error.message)) {
                                errorMessage = error.message
                            }
                            insertMessageResponse("error", errorMessage!!)
                            Timber.e("error: " + error.message);
                        }
                    })
            )
        } else {
            showAlertMessage(getApplication<Application>().getString(R.string.error_empty_address))
        }
    }

    fun sendStopWatchStop() {
        if (!TextUtils.isEmpty(configuration.address())) {
            val api = EspApi(configuration.address()!!)
            disposable.add(api.sendStopWatchStop()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { networkResponse.value = NetworkResponse.loading() }
                    .subscribeWith(object : DisposableObserver<String>() {
                        override fun onNext(response: String) {

                        }

                        override fun onComplete() {
                            Timber.d("complete");
                            //networkResponse.value = NetworkResponse.success(null)
                        }

                        override fun onError(error: Throwable) {
                            networkResponse.value = NetworkResponse.error(error)
                            var errorMessage: String? = "Server error"
                            if (!TextUtils.isEmpty(error.message)) {
                                errorMessage = error.message
                            }
                            insertMessageResponse("error", errorMessage!!)
                            Timber.e("error: " + error.message);
                        }
                    })
            )
        } else {
            showAlertMessage(getApplication<Application>().getString(R.string.error_empty_address))
        }
    }

    fun sendTimerStart(interval: Int) {
        if (!TextUtils.isEmpty(configuration.address())) {
            val api = EspApi(configuration.address()!!)
            disposable.add(api.sendTimerStart(interval)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { networkResponse.value = NetworkResponse.loading() }
                    .subscribeWith(object : DisposableObserver<String>() {
                        override fun onNext(response: String) {

                        }

                        override fun onComplete() {
                            Timber.d("complete");
                            //networkResponse.value = NetworkResponse.success(null)
                        }

                        override fun onError(error: Throwable) {
                            networkResponse.value = NetworkResponse.error(error)
                            var errorMessage: String? = "Server error"
                            if (!TextUtils.isEmpty(error.message)) {
                                errorMessage = error.message
                            }
                            insertMessageResponse("error", errorMessage!!)
                            Timber.e("error: " + error.message);
                        }
                    })
            )
        } else {
            showAlertMessage(getApplication<Application>().getString(R.string.error_empty_address))
        }
    }

    fun sendParameters(
            timezone: String,
            h24: String,
            blink: String,
            temp: String,
            adaptive: String,
            leds: String,
            leds_mode: String,
            brightness_offset: String,
            shutdown_th: String,
            sleep_hour: String,
            wake_hour: String,
            shutdown_delay: String,
            termometer: String,
            date: String,
            depoisoning: String,
            clock_cycle: String
    ) {
        if (!TextUtils.isEmpty(configuration.address())) {
            val api = EspApi(configuration.address()!!)
            disposable.add(api.sendParameters(
                    timezone,
                    h24,
                    blink,
                    temp,
                    adaptive,
                    leds,
                    leds_mode,
                    brightness_offset,
                    shutdown_th,
                    sleep_hour,
                    wake_hour,
                    shutdown_delay,
                    termometer,
                    date,
                    depoisoning,
                    clock_cycle
            )
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    //.doOnSubscribe { networkResponse.value = NetworkResponse.loading() }
                    .subscribeWith(object : DisposableObserver<String>() {
                        override fun onNext(response: String) {
                            //networkResponse.value = NetworkResponse.success(null)
                        }

                        override fun onComplete() {
                            Timber.d("complete");
                            //networkResponse.value = NetworkResponse.success("complete")
                        }

                        override fun onError(error: Throwable) {
                            networkResponse.value = NetworkResponse.error(error)
                            var errorMessage: String? = "Server error"
                            if (!TextUtils.isEmpty(error.message)) {
                                errorMessage = error.message
                            }
                            insertMessageResponse("error", errorMessage!!)
                            Timber.e("error: " + error.message);
                        }
                    })
            );
        } else {
            showAlertMessage(getApplication<Application>().getString(R.string.error_empty_address))
        }
    }

    /**
     * Insert new message into the database.
     */
    private fun insertMessageResponse(msg: String, value: String) {
        disposable.add(Completable.fromAction {
            val createdAt = DateUtils.generateCreatedAtDate()
            val message = Message()
            message.value = value
            message.message = msg
            message.createdAt = createdAt
            dataSource.insertMessage(message)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                }, { error -> Timber.e("Database error" + error.message) }))
    }

    public override fun onCleared() {
        //prevents memory leaks by disposing pending observable objects
        if (!disposable.isDisposed) {
            disposable.clear()
        }
    }

    /**
     * Network connectivity receiver to notify client of the network disconnect issues and
     * to clear any network notifications when reconnected. It is easy for network connectivity
     * to run amok that is why we only notify the user once for network disconnect with
     * a boolean flag.
     */
    companion object {

    }
}