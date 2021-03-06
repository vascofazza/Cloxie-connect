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


import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response

import retrofit2.http.*

interface EspService {
    @POST("/{path}")
    @FormUrlEncoded
    fun sendConfiguration(
            @Path("path") path: String,
            @Field("timezone_field") timezone: String,
            @Field("h24_field") h24: String,
            @Field("blink_field") blink_field: String,
            @Field("temp_field") temp_field: String,
            @Field("adaptive_field") adaptive_field: String,
            @Field("leds_field") leds_field: String,
            @Field("leds_mode_field") leds_mode_field: String,
            @Field("brightness_offset") brightness_offset: String,
            @Field("shutdown_threshold") shutdown_threshold: String,
            @Field("sleep_hour") sleep_hour: String,
            @Field("wake_hour") wake_hour: String,
            @Field("shutdown_delay") shutdown_delay: String,
            @Field("termometer_field") termometer: String,
            @Field("date_field") date: String,
            @Field("depoisoning_field") depoisoning: String,
            @Field("clock_cycle") clock_cycle: String,
            @Field("led_threshold") led_threshold: String,
            @Field("transition_time") transition_time: String,
            @Field("min_led_brightness") min_led_brightness: String,
            @Field("max_led_brightness") max_led_brightness: String,
            @Field("min_tube_brightness") min_tube_brightness: String,
            @Field("max_tube_brightness") max_tube_brightness: String
    ): Observable<String>// LiveData<ApiResponse<Message>>

    @GET("/{path}")
    fun getParameters(@Path("path") state: String): Observable<ParameterResponse>

    @POST("/timer_start")
    @FormUrlEncoded
    fun sendTimerStart(@Field("interval") interval: Int): Observable<String>

    @POST("/timer_pause")
    fun sendTimerPause(): Observable<String>

    @POST("/timer_stop")
    fun sendTimerStop(): Observable<String>

    @GET("/calibrate")
    fun sendCalibrate(): Observable<String>

    @GET("/cycle")
    fun sendNextCycle(): Observable<String>

    @POST("/stopwatch_start")
    fun sendStopWatchStart(): Observable<String>

    @POST("/stopwatch_pause")
    fun sendStopWatchPause(): Observable<String>

    @POST("/stopwatch_stop")
    fun sendStopWatchStop(): Observable<String>

    @GET("/timezones")
    fun getTimezones(): Observable<Response<ResponseBody>>
}