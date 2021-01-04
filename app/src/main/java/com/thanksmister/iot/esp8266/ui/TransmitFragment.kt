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

package com.thanksmister.iot.esp8266.ui

import android.app.Application
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.thanksmister.iot.esp8266.BaseFragment
import com.thanksmister.iot.esp8266.R
import com.thanksmister.iot.esp8266.api.EspApi
import com.thanksmister.iot.esp8266.api.NetworkResponse
import com.thanksmister.iot.esp8266.api.ParameterResponse
import com.thanksmister.iot.esp8266.api.Status
import com.thanksmister.iot.esp8266.persistence.Preferences
import com.thanksmister.iot.esp8266.util.DialogUtils
import com.thanksmister.iot.esp8266.viewmodel.TransmitViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_transmit.*
import timber.log.Timber
import javax.inject.Inject
import javax.jmdns.JmDNS


class TransmitFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: TransmitViewModel
    private var listener: OnFragmentInteractionListener? = null
    private var networkStatus: Status = Status.START

    private var displayModeMap: HashMap<String, String> = hashMapOf("24H" to "1", "12H" to "0")
    private var timezoneMap: MutableList<String> = ArrayList()
    private var blinkModeMap: HashMap<String, String> =
            linkedMapOf("off" to "0", "static" to "1", "double static" to "2", "fade out" to "3")
    private var temperatureScaleMap: HashMap<String, String> = linkedMapOf("C°" to "1", "F°" to "0")
    private var onOffMap: HashMap<String, String> =
            linkedMapOf("off" to "0", "on" to "1")
    private var ledsMap: HashMap<String, String> = linkedMapOf("off" to "0", "on" to "1")
    private var ledsModeMap: HashMap<String, String> =
            linkedMapOf("static" to "0", "rotating" to "1", "random" to "2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transmit, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    private fun readTimezoneArray() {
        if (timezoneMap.size < 2) {
            timezoneMap.clear()
            for (value: String in preferences.timezones(preferences.fwVersion()!!)!!.split("\n")) {
                timezoneMap.add(value)
            }

            val dataAdapter = ArrayAdapter<String>(
                    this.context!!,
                    android.R.layout.simple_spinner_item,
                    timezoneMap
            )
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            timezone.setAdapter(dataAdapter)
            viewModel.readParameters()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TransmitViewModel::class.java)
        observeViewModel(viewModel)
        viewModel.readParameters()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var dataAdapter = ArrayAdapter<String>(
                this.context!!,
                android.R.layout.simple_spinner_item,
                timezoneMap
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timezone.setAdapter(dataAdapter)

        dataAdapter = ArrayAdapter<String>(
                this.context!!,
                android.R.layout.simple_spinner_item,
                displayModeMap.keys.toTypedArray()
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        display_mode.setAdapter(dataAdapter)

        dataAdapter = ArrayAdapter<String>(
                this.context!!,
                android.R.layout.simple_spinner_item,
                blinkModeMap.keys.toTypedArray()
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        blink_mode.setAdapter(dataAdapter)

        dataAdapter = ArrayAdapter<String>(
                this.context!!,
                android.R.layout.simple_spinner_item,
                temperatureScaleMap.keys.toTypedArray()
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        temp_scale.setAdapter(dataAdapter)

        dataAdapter = ArrayAdapter<String>(
                this.context!!,
                android.R.layout.simple_spinner_item,
                onOffMap.keys.toTypedArray()
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adaptive_brightness.setAdapter(dataAdapter)

        dataAdapter = ArrayAdapter<String>(
                this.context!!,
                android.R.layout.simple_spinner_item,
                onOffMap.keys.toTypedArray()
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        termometer.setAdapter(dataAdapter)

        dataAdapter = ArrayAdapter<String>(
                this.context!!,
                android.R.layout.simple_spinner_item,
                onOffMap.keys.toTypedArray()
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        date.setAdapter(dataAdapter)

        dataAdapter = ArrayAdapter<String>(
                this.context!!,
                android.R.layout.simple_spinner_item,
                ledsMap.keys.toTypedArray()
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        leds.setAdapter(dataAdapter)

        dataAdapter = ArrayAdapter<String>(
                this.context!!,
                android.R.layout.simple_spinner_item,
                ledsModeMap.keys.toTypedArray()
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        leds_mode.setAdapter(dataAdapter)

        buttonRead.setOnClickListener {
            viewModel.readParameters()
        }

        buttonCalibrate.setOnClickListener {
            viewModel.sendCalibrate()
        }

        buttonStart.setOnClickListener {
            if (TextUtils.isEmpty(timer_interval.text) ||
                    !TextUtils.isDigitsOnly(timer_interval.text)
            ) {
                Toast.makeText(activity!!, getString(R.string.toast_blak_value), Toast.LENGTH_SHORT)
                        .show()
            } else {

                viewModel.sendTimerStart(Integer.parseInt(timer_interval.text.toString()));
            }
        }

        buttonStop.setOnClickListener {
            viewModel.sendTimerStop();
        }

        buttonStopWatchStart.setOnClickListener {
            viewModel.sendStopWatchStart();
        }

        buttonStopWatchPause.setOnClickListener {
            viewModel.sendStopWatchPause();
        }

        buttonStopWatchStop.setOnClickListener {
            viewModel.sendStopWatchStop();
        }

        buttonSend.setOnClickListener {
            if (TextUtils.isEmpty(brightness_offset.text) || (!TextUtils.isDigitsOnly(
                            brightness_offset.text
                    ) && (brightness_offset.text[0] == '-' && !TextUtils.isDigitsOnly(
                            brightness_offset.text.substring(1)
                    )))
            ) {
                Toast.makeText(activity!!, getString(R.string.toast_blak_value), Toast.LENGTH_SHORT)
                        .show()
            } else if (TextUtils.isEmpty(shutdown_threshold.text) || !TextUtils.isDigitsOnly(
                            shutdown_threshold.text
                    )
            ) {
                Toast.makeText(activity!!, getString(R.string.toast_blak_value), Toast.LENGTH_SHORT)
                        .show()
            } else if (TextUtils.isEmpty(sleep_hour.text) || !TextUtils.isDigitsOnly(
                            sleep_hour.text
                    )
            ) {
                Toast.makeText(activity!!, getString(R.string.toast_blak_value), Toast.LENGTH_SHORT)
                        .show()
            } else if (TextUtils.isEmpty(wake_hour.text) || !TextUtils.isDigitsOnly(
                            wake_hour.text
                    )
            ) {
                Toast.makeText(activity!!, getString(R.string.toast_blak_value), Toast.LENGTH_SHORT)
                        .show()
            } else if (TextUtils.isEmpty(shutdown_delay.text) || !TextUtils.isDigitsOnly(
                            shutdown_delay.text
                    )
            ) {
                Toast.makeText(activity!!, getString(R.string.toast_blak_value), Toast.LENGTH_SHORT)
                        .show()
            } else if (TextUtils.isEmpty(depoisoning_interval.text) || !TextUtils.isDigitsOnly(
                            depoisoning_interval.text
                    )
            ) {
                Toast.makeText(activity!!, getString(R.string.toast_blak_value), Toast.LENGTH_SHORT)
                        .show()
            } else if (TextUtils.isEmpty(clock_cycle.text) || !TextUtils.isDigitsOnly(
                            clock_cycle.text
                    )
            ) {
                Toast.makeText(activity!!, getString(R.string.toast_blak_value), Toast.LENGTH_SHORT)
                        .show()
            } else if (networkStatus != Status.LOADING) {
                viewModel.sendParameters(
                        h24 = displayModeMap.get(display_mode.selectedItem.toString())!!,
                        timezone = timezone.selectedItem.toString(),
                        blink = blinkModeMap.get(blink_mode.selectedItem.toString())!!,
                        temp = temperatureScaleMap.get(temp_scale.selectedItem.toString())!!,
                        adaptive = onOffMap.get(adaptive_brightness.selectedItem.toString())!!,
                        leds = ledsMap.get(leds.selectedItem.toString())!!,
                        leds_mode = ledsModeMap.get(leds_mode.selectedItem.toString())!!,
                        brightness_offset = brightness_offset.text.toString(),
                        shutdown_th = shutdown_threshold.text.toString(),
                        sleep_hour = sleep_hour.text.toString(),
                        wake_hour = wake_hour.text.toString(),
                        shutdown_delay = shutdown_delay.text.toString(),
                        termometer = onOffMap.get(termometer.selectedItem.toString())!!,
                        date = onOffMap.get(date.selectedItem.toString())!!,
                        depoisoning = depoisoning_interval.text.toString(),
                        clock_cycle = clock_cycle.text.toString(),
                )
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun disconnectFromWifi()
        fun wifiDisconnected()
    }

    private fun observeViewModel(viewModel: TransmitViewModel) {
        viewModel.networkResponse()
                .observe(this, Observer { response -> processNetworkResponse(response) })
        viewModel.timezoneResponse()
                .observe(this, Observer { response -> readTimezoneArray() })

        viewModel.getToastMessage().observe(
                this,
                Observer { message -> Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show() })
        viewModel.getAlertMessage().observe(this, Observer { message ->
            DialogUtils.dialogMessage(
                    activity!!,
                    getString(R.string.alert_title_error),
                    message!!,
                    DialogInterface.OnClickListener { _, _ -> SettingsActivity.start(activity!!) })
        })
    }

    private fun getIndex(map: HashMap<String, String>, value: String): Int {
        for ((i, obj) in map.entries.withIndex()) {
            if (obj.value.equals(value))
                return i
        }
        return 0;
    }

    private fun processNetworkResponse(response: NetworkResponse<ParameterResponse>?) {
        if (response?.status != null) {
            networkStatus = response.status
            when (networkStatus) {
                Status.LOADING -> {
                    //
                }
                Status.SUCCESS -> {
                    preferences.fwVersion(response.data!!.firmwareVersion)
                    viewModel.getTimezones()
                    if (timezoneMap.size > 1)
                        timezone.setSelection(timezoneMap.indexOf(response.data?.timezone!!), true)
                    display_mode.setSelection(getIndex(displayModeMap, response.data?.h24!!), true)
                    blink_mode.setSelection(
                            getIndex(blinkModeMap, response.data?.blink_mode!!),
                            true
                    )
                    temp_scale.setSelection(
                            getIndex(temperatureScaleMap, response.data?.temp!!),
                            true
                    )
                    adaptive_brightness.setSelection(
                            getIndex(
                                    onOffMap,
                                    response.data?.adaptive_brightness!!
                            ), true
                    )
                    termometer.setSelection(
                            getIndex(
                                    onOffMap,
                                    response.data?.termometer!!
                            ), true
                    )
                    date.setSelection(
                            getIndex(
                                    onOffMap,
                                    response.data?.date!!
                            ), true
                    )
                    leds.setSelection(getIndex(ledsMap, response.data?.leds!!), true)
                    leds_mode.setSelection(
                            getIndex(
                                    ledsModeMap,
                                    response.data?.led_configuration!!
                            ), true
                    )
                    brightness_offset.setText(response.data.brightness_offset)
                    shutdown_threshold.setText(response.data.shutdown_threshold)
                    sleep_hour.setText(response.data.sleep_hour)
                    wake_hour.setText(response.data.wake_hour)
                    shutdown_delay.setText(response.data.shutdown_delay)
                    depoisoning_interval.setText(response.data.depoisoning)
                    clock_cycle.setText(response.data.clock_cycle)
                    val split = response.data.upTime.split(":")
                    val uptime = String.format("%s days, %s hours, %s minutes, %s seconds", split[0], split[1], split[2], split[3])
                    preferences.upTime(uptime)
                }
                Status.ERROR -> {
                    val message = response.error?.message.toString()
                    Snackbar.make(
                            activity!!.findViewById(android.R.id.content),
                            message,
                            Snackbar.LENGTH_LONG
                    ).show()
                }
                else -> {
                }
            }
        }
    }

    companion object {
        fun newInstance(): TransmitFragment {
            return TransmitFragment()
        }
    }
}// Required empty public constructor
