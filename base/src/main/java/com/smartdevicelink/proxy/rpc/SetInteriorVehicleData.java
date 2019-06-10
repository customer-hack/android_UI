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

import android.support.annotation.NonNull;

import com.smartdevicelink.protocol.enums.FunctionID;
import com.smartdevicelink.proxy.RPCRequest;

import java.util.Hashtable;

/**
 * This function allows a remote control type mobile application change the settings
 * of a specific remote control module.
 */
public class SetInteriorVehicleData extends RPCRequest {
	public static final String KEY_MODULE_DATA = "moduleData";

    /**
     * Constructs a new SetInteriorVehicleData object
     */
    public SetInteriorVehicleData() {
        super(FunctionID.SET_INTERIOR_VEHICLE_DATA.toString());
    }

    /**
     * <p>Constructs a new SetInteriorVehicleData object indicated by the
     * Hashtable parameter</p>
     *
     *
     * @param hash
     * The Hashtable to use
     */
    public SetInteriorVehicleData(Hashtable<String, Object> hash) {
        super(hash);
    }

    /**
     * Constructs a new SetInteriorVehicleData object
     * @param moduleData
     */
    public SetInteriorVehicleData(@NonNull ModuleData moduleData) {
        this();
        setModuleData(moduleData);
    }

    /**
     * Sets the moduleData
     *
     * @param moduleData
     */
    public void setModuleData(@NonNull ModuleData moduleData) {
        setParameters(KEY_MODULE_DATA, moduleData);
    }

    /**
     * Gets the moduleData
     *
     * @return ModuleData
     */
    public ModuleData getModuleData() {
        return (ModuleData) getObject(ModuleData.class, KEY_MODULE_DATA);
    }
}
