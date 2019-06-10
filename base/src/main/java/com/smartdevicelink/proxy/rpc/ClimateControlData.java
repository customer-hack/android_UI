/*
 * Copyright (c) 2017 - 2019, SmartDeviceLink Consortium, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of the SmartDeviceLink Consortium, Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from this 
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.smartdevicelink.proxy.rpc;

import com.smartdevicelink.proxy.RPCStruct;
import com.smartdevicelink.proxy.rpc.enums.DefrostZone;
import com.smartdevicelink.proxy.rpc.enums.VentilationMode;

import java.util.Hashtable;

public class ClimateControlData extends RPCStruct{
    public static final String KEY_FAN_SPEED= "fanSpeed";
    public static final String KEY_CURRENT_TEMPERATURE= "currentTemperature";
    public static final String KEY_DESIRED_TEMPERATURE= "desiredTemperature";
    public static final String KEY_AC_ENABLE= "acEnable";
    public static final String KEY_CIRCULATE_AIR_ENABLE= "circulateAirEnable";
    public static final String KEY_AUTO_MODE_ENABLE= "autoModeEnable";
    public static final String KEY_DEFROST_ZONE= "defrostZone";
    public static final String KEY_DUAL_MODE_ENABLE= "dualModeEnable";
    public static final String KEY_AC_MAX_ENABLE= "acMaxEnable";
    public static final String KEY_VENTILATION_MODE= "ventilationMode";
    public static final String KEY_HEATED_STEERING_WHEEL_ENABLE = "heatedSteeringWheelEnable";
    public static final String KEY_HEATED_WIND_SHIELD_ENABLE = "heatedWindshieldEnable";
    public static final String KEY_HEATED_REAR_WINDOW_ENABLE = "heatedRearWindowEnable";
    public static final String KEY_HEATED_MIRRORS_ENABLE = "heatedMirrorsEnable";

    public ClimateControlData() {
    }

    public ClimateControlData(Hashtable<String, Object> hash) {
        super(hash);
    }

    public void setFanSpeed(Integer fanSpeed) {
        setValue(KEY_FAN_SPEED, fanSpeed);
    }

    public Integer getFanSpeed() {
        return getInteger(KEY_FAN_SPEED);
    }

    public void setCurrentTemperature(Temperature currentTemperature) {
        setValue(KEY_CURRENT_TEMPERATURE, currentTemperature);
    }

    public Temperature getCurrentTemperature() {
        return (Temperature) getObject(Temperature.class, KEY_CURRENT_TEMPERATURE);
    }

    public void setDesiredTemperature(Temperature desiredTemperature) {
        setValue(KEY_DESIRED_TEMPERATURE, desiredTemperature);
    }

    public Temperature getDesiredTemperature() {
        return (Temperature) getObject(Temperature.class, KEY_DESIRED_TEMPERATURE);
    }

    public void setAcEnable(Boolean acEnable) {
        setValue(KEY_AC_ENABLE, acEnable);
    }

    public Boolean getAcEnable() {
        return getBoolean(KEY_AC_ENABLE);
    }

    public void setCirculateAirEnable(Boolean circulateAirEnable) {
        setValue(KEY_CIRCULATE_AIR_ENABLE, circulateAirEnable);
    }

    public Boolean getCirculateAirEnable() {
        return getBoolean(KEY_CIRCULATE_AIR_ENABLE);
    }

    public void setAutoModeEnable(Boolean autoModeEnable) {
        setValue(KEY_AUTO_MODE_ENABLE, autoModeEnable);
    }

    public Boolean getAutoModeEnable() {
        return getBoolean(KEY_AUTO_MODE_ENABLE);
    }

    public void setDefrostZone(DefrostZone defrostZone) {
        setValue(KEY_DEFROST_ZONE, defrostZone);
    }

    public DefrostZone getDefrostZone() {
        return (DefrostZone) getObject(DefrostZone.class, KEY_DEFROST_ZONE);
    }

    public void setDualModeEnable(Boolean dualModeEnable) {
        setValue(KEY_DUAL_MODE_ENABLE, dualModeEnable);
    }

    public Boolean getDualModeEnable() {
        return getBoolean(KEY_DUAL_MODE_ENABLE);
    }

    public void setAcMaxEnable(Boolean acMaxEnable) {
        setValue(KEY_AC_MAX_ENABLE, acMaxEnable);
    }

    public Boolean getAcMaxEnable() {
        return getBoolean(KEY_AC_MAX_ENABLE);
    }

    public void setVentilationMode(VentilationMode ventilationMode) {
        setValue(KEY_VENTILATION_MODE, ventilationMode);
    }

    public VentilationMode getVentilationMode() {
        return (VentilationMode) getObject(VentilationMode.class, KEY_VENTILATION_MODE);
    }

    /**
     * Sets the heatedSteeringWheelEnable portion of the ClimateControlCapabilities class
     *
     * @param heatedSteeringWheelEnable Value false means disabled/turn off, value true means enabled/turn on.
     */
    public void setHeatedSteeringWheelEnable(Boolean heatedSteeringWheelEnable) {
        setValue(KEY_HEATED_STEERING_WHEEL_ENABLE, heatedSteeringWheelEnable);
    }

    /**
     * Gets the heatedSteeringWheelEnable portion of the ClimateControlCapabilities class
     *
     * @return Boolean - Value false means disabled/turn off, value true means enabled/turn on.
     */
    public Boolean getHeatedSteeringWheelEnable() {
        return getBoolean(KEY_HEATED_STEERING_WHEEL_ENABLE);
    }

    /**
     * Sets the heatedWindshieldEnable portion of the ClimateControlCapabilities class
     *
     * @param heatedWindshieldEnable Value false means disabled, value true means enabled.
     */
    public void setHeatedWindshieldEnable(Boolean heatedWindshieldEnable) {
        setValue(KEY_HEATED_WIND_SHIELD_ENABLE, heatedWindshieldEnable);
    }

    /**
     * Gets the heatedWindshieldEnable portion of the ClimateControlCapabilities class
     *
     * @return Boolean - Value false means disabled, value true means enabled.
     */
    public Boolean getHeatedWindshieldEnable() {
        return getBoolean(KEY_HEATED_WIND_SHIELD_ENABLE);
    }

    /**
     * Sets the heatedRearWindowEnable portion of the ClimateControlCapabilities class
     *
     * @param heatedRearWindowEnable Value false means disabled, value true means enabled.
     */
    public void setHeatedRearWindowEnable(Boolean heatedRearWindowEnable) {
        setValue(KEY_HEATED_REAR_WINDOW_ENABLE, heatedRearWindowEnable);
    }

    /**
     * Gets the heatedRearWindowEnable portion of the ClimateControlCapabilities class
     *
     * @return Boolean - Value false means disabled, value true means enabled.
     */
    public Boolean getHeatedRearWindowEnable() {
        return getBoolean(KEY_HEATED_REAR_WINDOW_ENABLE);
    }

    /**
     * Sets the heatedMirrorsEnable portion of the ClimateControlCapabilities class
     *
     * @param heatedMirrorsEnable Value false means disabled, value true means enabled.
     */
    public void setHeatedMirrorsEnable(Boolean heatedMirrorsEnable) {
        setValue(KEY_HEATED_MIRRORS_ENABLE, heatedMirrorsEnable);
    }

    /**
     * Gets the heatedMirrorsEnable portion of the ClimateControlCapabilities class
     *
     * @return Boolean - Value false means disabled, value true means enabled.
     */
    public Boolean getHeatedMirrorsEnable() {
        return getBoolean(KEY_HEATED_MIRRORS_ENABLE);
    }
}
