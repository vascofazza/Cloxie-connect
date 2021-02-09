package com.thanksmister.iot.esp8266.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParameterResponse {

    @SerializedName("timezone")
    @Expose
    private String timezone = null;

    @SerializedName("h24")
    @Expose
    private String h24 = null;

    @SerializedName("blink_mode")
    @Expose
    private String blink_mode = null;

    @SerializedName("celsius")
    @Expose
    private String temp = null;

    @SerializedName("adaptive_brightness")
    @Expose
    private String adaptive_brightness = null;

    @SerializedName("brightness_offset")
    @Expose
    private String brightness_offset = null;

    @SerializedName("shutdown_delay")
    @Expose
    private String shutdown_delay = null;

    @SerializedName("shutdown_threshold")
    @Expose
    private String shutdown_threshold = null;

    @SerializedName("leds")
    @Expose
    private String leds = null;

    @SerializedName("led_configuration")
    @Expose
    private String led_configuration = null;

    @SerializedName("sleep_hour")
    @Expose
    private String sleep_hour = null;


    @SerializedName("wake_hour")
    @Expose
    private String wake_hour = null;

    @SerializedName("termometer")
    @Expose
    private String termometer = null;

    @SerializedName("date")
    @Expose
    private String date = null;

    @SerializedName("depoisoning_field")
    @Expose
    private String depoisoning = null;

    @SerializedName("clock_cycle")
    @Expose
    private String clock_cycle = null;

    @SerializedName("uptime")
    @Expose
    private String upTime = null;

    @SerializedName("led_threshold")
    @Expose
    private String ledThreshold = null;

    @SerializedName("transition_time")
    @Expose
    private String transitionTime = null;

    @SerializedName("min_led_brightness")
    @Expose
    private String min_led_brightness = null;

    @SerializedName("max_led_brightness")
    @Expose
    private String max_led_brightness = null;

    @SerializedName("min_tube_brightness")
    @Expose
    private String min_tube_brightness = null;

    @SerializedName("max_tube_brightness")
    @Expose
    private String max_tube_brightness = null;

    @SerializedName("fw_ver")
    @Expose
    private String firmwareVersion = null;

    public String getClock_cycle() {
        return clock_cycle;
    }

    public void setClock_cycle(String clock_cycle) {
        this.clock_cycle = clock_cycle;
    }

    public String getTermometer() {
        return termometer;
    }

    public void setTermometer(String termometer) {
        this.termometer = termometer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getH24() {
        return h24;
    }

    public void setH24(String h24) {
        this.h24 = h24;
    }

    public String getBlink_mode() {
        return blink_mode;
    }

    public void setBlink_mode(String blink_mode) {
        this.blink_mode = blink_mode;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getAdaptive_brightness() {
        return adaptive_brightness;
    }

    public void setAdaptive_brightness(String adaptive_brightness) {
        this.adaptive_brightness = adaptive_brightness;
    }

    public String getBrightness_offset() {
        return brightness_offset;
    }

    public void setBrightness_offset(String brightness_offset) {
        this.brightness_offset = brightness_offset;
    }

    public String getShutdown_delay() {
        return shutdown_delay;
    }

    public void setShutdown_delay(String shutdown_delay) {
        this.shutdown_delay = shutdown_delay;
    }

    public String getShutdown_threshold() {
        return shutdown_threshold;
    }

    public void setShutdown_threshold(String shutdown_threshold) {
        this.shutdown_threshold = shutdown_threshold;
    }

    public String getLeds() {
        return leds;
    }

    public void setLeds(String leds) {
        this.leds = leds;
    }

    public String getLed_configuration() {
        return led_configuration;
    }

    public void setLed_configuration(String led_configuration) {
        this.led_configuration = led_configuration;
    }

    public String getSleep_hour() {
        return sleep_hour;
    }

    public void setSleep_hour(String sleep_hour) {
        this.sleep_hour = sleep_hour;
    }

    public String getWake_hour() {
        return wake_hour;
    }

    public void setWake_hour(String wake_hour) {
        this.wake_hour = wake_hour;
    }

    public String getDepoisoning() {
        return depoisoning;
    }

    public void setDepoisoning(String depoisoning) {
        this.depoisoning = depoisoning;
    }

    public String getLedThreshold() {
        return ledThreshold;
    }

    public void setLedThreshold(String ledThreshold) {
        this.ledThreshold = ledThreshold;
    }

    public String getTransitionTime() {
        return transitionTime;
    }

    public void setTransitionTime(String transitionTime) {
        this.transitionTime = transitionTime;
    }

    public String getMin_led_brightness() {
        return min_led_brightness;
    }

    public void setMin_led_brightness(String min_led_brightness) {
        this.min_led_brightness = min_led_brightness;
    }

    public String getMax_led_brightness() {
        return max_led_brightness;
    }

    public void setMax_led_brightness(String max_led_brightness) {
        this.max_led_brightness = max_led_brightness;
    }

    public String getMin_tube_brightness() {
        return min_tube_brightness;
    }

    public void setMin_tube_brightness(String min_tube_brightness) {
        this.min_tube_brightness = min_tube_brightness;
    }

    public String getMax_tube_brightness() {
        return max_tube_brightness;
    }

    public void setMax_tube_brightness(String max_tube_brightness) {
        this.max_tube_brightness = max_tube_brightness;
    }
}
