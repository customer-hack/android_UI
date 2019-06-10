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

import java.util.Hashtable;
import java.util.List;

/**
 * Contains information about this system's video streaming capabilities.
 */

public class VideoStreamingCapability extends RPCStruct {
	public static final String KEY_PREFERRED_RESOLUTION = "preferredResolution";
	public static final String KEY_MAX_BITRATE = "maxBitrate";
	public static final String KEY_SUPPORTED_FORMATS = "supportedFormats";
	public static final String KEY_HAPTIC_SPATIAL_DATA_SUPPORTED = "hapticSpatialDataSupported";

	public VideoStreamingCapability(){}
	public VideoStreamingCapability(Hashtable<String, Object> hash){super(hash);}

	public void setPreferredResolution(ImageResolution res){
		setValue(KEY_PREFERRED_RESOLUTION, res);
	}

	public ImageResolution getPreferredResolution(){
		return (ImageResolution) getObject(ImageResolution.class, KEY_PREFERRED_RESOLUTION);
	}

	/**
	 * Set the max bitrate supported by this module.
	 *
	 * <b>NOTE: </b> Unit is in kbps.
	 * @param maxBitrate in kbps
	 */
	public void setMaxBitrate(Integer maxBitrate){
		setValue(KEY_MAX_BITRATE, maxBitrate);
	}

	/**
	 * Retrieves the max bitrate supported by this module.
	 *
	 * <b>NOTE: </b> Unit is in kbps.
	 * @return max bitrate in kbps
	 */
	public Integer getMaxBitrate(){
		return getInteger(KEY_MAX_BITRATE);
	}

	public void setSupportedFormats(List<VideoStreamingFormat> formats){
		setValue(KEY_SUPPORTED_FORMATS, formats);
	}

	public List<VideoStreamingFormat> getSupportedFormats(){
		return (List<VideoStreamingFormat>) getObject(VideoStreamingFormat.class, KEY_SUPPORTED_FORMATS);
	}

	public Boolean getIsHapticSpatialDataSupported() {
		return getBoolean(KEY_HAPTIC_SPATIAL_DATA_SUPPORTED);
	}

	public void setIsHapticSpatialDataSupported(Boolean hapticSpatialDataSupported) {
		setValue(KEY_HAPTIC_SPATIAL_DATA_SUPPORTED, hapticSpatialDataSupported);
	}
}
