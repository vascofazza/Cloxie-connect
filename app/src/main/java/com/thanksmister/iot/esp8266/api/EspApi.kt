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

package com.thanksmister.iot.esp8266.api

import com.google.gson.GsonBuilder
import com.thanksmister.iot.esp8266.api.adapters.DataTypeAdapterFactory
import io.reactivex.Observable

import okhttp3.OkHttpClient
import okhttp3.ResponseBody

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class EspApi(private val address: String) {

    private val service: EspService

    init {

        val base_url = "http://$address/"
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addNetworkInterceptor(logging)
                .build()

        val gson = GsonBuilder()
                .setLenient()
                .registerTypeAdapterFactory(DataTypeAdapterFactory())
                .create()

        val retrofit = Retrofit.Builder()
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(base_url)
                .build()

        service = retrofit.create(EspService::class.java)
    }

    fun sendStopWatchStart(): Observable<String> {
        return service.sendStopWatchStart();
    }

    fun sendStopWatchPause(): Observable<String> {
        return service.sendStopWatchPause();
    }

    fun sendStopWatchStop(): Observable<String> {
        return service.sendStopWatchStop();
    }

    fun sendTimerStart(interval: Int): Observable<String> {
        return service.sendTimerStart(interval);
    }

    fun sendTimerPause(): Observable<String> {
        return service.sendTimerPause();
    }

    fun sendTimerStop(): Observable<String> {
        return service.sendTimerStop();
    }

    fun getParameters(): Observable<ParameterResponse> {
        return service.getParameters("param_json");
    }

    fun getTimezones(): Observable<Response<ResponseBody>> {
        return service.getTimezones();
    }

    fun sendParameters(timezone: String, h24: String, blink: String, temp: String, adaptive: String, leds: String, leds_mode: String, brightness_offset: String, shutdown_th: String, sleep_hour: String, wake_hour: String, shutdown_delay: String, termometer: String, date: String, depoisoning: String, clock_cycle: String): Observable<String> {//LiveData<ApiResponse<Message>> {
        return service.sendConfiguration("paramsave", timezone, h24, blink, temp, adaptive, leds, leds_mode, brightness_offset, shutdown_th, sleep_hour, wake_hour, shutdown_delay, termometer, date, depoisoning, clock_cycle)
    }
}