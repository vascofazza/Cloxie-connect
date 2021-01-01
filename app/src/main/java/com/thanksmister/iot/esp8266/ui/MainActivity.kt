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

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.thanksmister.iot.esp8266.BaseActivity
import com.thanksmister.iot.esp8266.R
import com.thanksmister.iot.esp8266.manager.WiFiReceiverManager
import com.thanksmister.iot.esp8266.manager.WiFiStatus
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.jmdns.JmDNS

class MainActivity : BaseActivity(), TransmitFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener, ViewPager.OnPageChangeListener {

    private var connectionLiveData: ConnectionLiveData? = null
    private lateinit var pagerAdapter: PagerAdapter
    private var wiFiReceiverManager: WiFiReceiverManager? = null
    private var waitingForConnection: Boolean = false;
    private var wifiStatus: WiFiStatus? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        setSupportActionBar(toolbar)

        val thread = Thread {
            try {
                val bonjourServiceType = "_http._tcp.local."
                val bonjourService = JmDNS.create()
                val serviceInfos: Array<out javax.jmdns.ServiceInfo>? = bonjourService.list(bonjourServiceType)
                if (serviceInfos != null) {
                    for (info in serviceInfos) {
                        if (info.getName().toString().equals("cloxie") && info.hostAddresses.size > 0)
                            preferences.address(info.hostAddresses[0])
                    }
                }
                bonjourService.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        thread.start()
        thread.join(5000)

        pagerAdapter = MainSlidePagerAdapter(supportFragmentManager)
        view_pager.adapter = pagerAdapter
        view_pager.addOnPageChangeListener(this)
        view_pager.setPagingEnabled(false)
    }

    @LayoutRes
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                SettingsActivity.start(this@MainActivity)
                true
            }
            R.id.action_logs -> {
                LogActivity.start(this@MainActivity)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (view_pager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            view_pager.currentItem = view_pager.currentItem - 1
        }
    }

    override fun disconnectFromWifi() {
        if(wifiStatus == WiFiStatus.CONNECTED) {
            wiFiReceiverManager?.disconnectFromWifi()
        }
    }

    override fun connectWifi() {
        try{
            if(wiFiReceiverManager != null && wifiStatus != WiFiStatus.CONNECTED && wifiStatus != WiFiStatus.CONNECTING ) {
                wiFiReceiverManager?.connectWifi(preferences.ssID()!!, preferences.password()!!)
            }
        } catch (e: NullPointerException) {
            Timber.e(e.message)
        }
    }

    override fun wifiConnected() {
        Timber.d("wifiConnected")
        progressbar.visibility = View.INVISIBLE
        if(view_pager.currentItem == 0) {
            view_pager.currentItem = 1
        }
    }

    override fun wifiDisconnected() {
        Timber.d("wifiDisconnected")
        progressbar.visibility = View.INVISIBLE
        if(view_pager.currentItem != 0) {
            view_pager.currentItem = 0
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    private inner class MainSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            when (position) {
                //0 -> return MainFragment.newInstance()
                0 -> return TransmitFragment.newInstance()
                else -> return MainFragment.newInstance()
            }
        }
        override fun getCount(): Int {
            return 2
        }
    }
}