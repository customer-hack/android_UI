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
import com.smartdevicelink.proxy.RPCResponse;
import com.smartdevicelink.proxy.rpc.enums.ButtonName;
import com.smartdevicelink.proxy.rpc.enums.HmiZoneCapabilities;
import com.smartdevicelink.proxy.rpc.enums.Language;
import com.smartdevicelink.proxy.rpc.enums.PrerecordedSpeech;
import com.smartdevicelink.proxy.rpc.enums.Result;
import com.smartdevicelink.proxy.rpc.enums.SpeechCapabilities;
import com.smartdevicelink.proxy.rpc.enums.VrCapabilities;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Register AppInterface Response is sent, when RegisterAppInterface has been called
 * 
 * @since SmartDeviceLink 1.0
 */
public class RegisterAppInterfaceResponse extends RPCResponse {
	public static final String KEY_VEHICLE_TYPE 				= "vehicleType";
	public static final String KEY_SPEECH_CAPABILITIES 			= "speechCapabilities";
	public static final String KEY_VR_CAPABILITIES 				= "vrCapabilities";
	public static final String KEY_AUDIO_PASS_THRU_CAPABILITIES = "audioPassThruCapabilities";
	public static final String KEY_HMI_ZONE_CAPABILITIES 		= "hmiZoneCapabilities";
    public static final String KEY_PRERECORDED_SPEECH 			= "prerecordedSpeech";
    public static final String KEY_SUPPORTED_DIAG_MODES 		= "supportedDiagModes";
    public static final String KEY_SDL_MSG_VERSION 				= "syncMsgVersion";
    public static final String KEY_LANGUAGE 					= "language";
    public static final String KEY_BUTTON_CAPABILITIES 			= "buttonCapabilities";
    public static final String KEY_DISPLAY_CAPABILITIES 		= "displayCapabilities";
    public static final String KEY_HMI_DISPLAY_LANGUAGE 		= "hmiDisplayLanguage";
    public static final String KEY_SOFT_BUTTON_CAPABILITIES 	= "softButtonCapabilities";
    public static final String KEY_PRESET_BANK_CAPABILITIES 	= "presetBankCapabilities";
    public static final String KEY_HMI_CAPABILITIES 			= "hmiCapabilities"; //As of v4.0
    public static final String KEY_SDL_VERSION 					= "sdlVersion"; //As of v4.0
    public static final String KEY_SYSTEM_SOFTWARE_VERSION		= "systemSoftwareVersion"; //As of v4.0
    public static final String KEY_ICON_RESUMED 				= "iconResumed";
    public static final String KEY_PCM_STREAM_CAPABILITIES      = "pcmStreamCapabilities";
    
	/**
	 * Constructs a new RegisterAppInterfaceResponse object
	 */
    public RegisterAppInterfaceResponse() {
        super(FunctionID.REGISTER_APP_INTERFACE.toString());
    }

	/**
	 * Constructs a new RegisterAppInterfaceResponse object indicated by the Hashtable
	 * parameter
	 * <p>
	 * 
	 * @param hash
	 *            The Hashtable to use
	 */
    public RegisterAppInterfaceResponse(Hashtable<String, Object> hash) {
        super(hash);
    }

	/**
	 * Constructs a new RegisterAppInterfaceResponse object
	 * @param success whether the request is successfully processed
	 * @param resultCode whether the request is successfully processed
	 */
	public RegisterAppInterfaceResponse(@NonNull Boolean success, @NonNull Result resultCode) {
		this();
		setSuccess(success);
		setResultCode(resultCode);
	}

	@Override
	public void format(com.smartdevicelink.util.Version rpcVersion, boolean formatParams){
		//Add in 5.0.0 of the rpc spec
		if(getIconResumed() == null){
			setIconResumed(Boolean.FALSE);
		}

		List<ButtonCapabilities> capabilities = getButtonCapabilities();
		if(capabilities != null){
			List<ButtonCapabilities> additions = new ArrayList<>();
			for(ButtonCapabilities capability : capabilities){
				if(ButtonName.OK.equals(capability.getName())){
					if(rpcVersion == null || rpcVersion.getMajor() < 5){
						//If version is < 5, the play pause button must also be added
						additions.add(new ButtonCapabilities(ButtonName.PLAY_PAUSE, capability.getShortPressAvailable(), capability.getLongPressAvailable(), capability.getUpDownAvailable()));
					}
				}
			}
			capabilities.addAll(additions);
			setButtonCapabilities(capabilities);
		}


		super.format(rpcVersion,formatParams);
	}


	/**
	 * Gets the version of the SDL&reg; SmartDeviceLink interface
	 *
	 * @return SdlMsgVersion -a SdlMsgVersion object representing version of
	 *         the SDL&reg; SmartDeviceLink interface
	 */
    @SuppressWarnings("unchecked")
    public SdlMsgVersion getSdlMsgVersion() {
		return (SdlMsgVersion) getObject(SdlMsgVersion.class, KEY_SDL_MSG_VERSION);
    }

	/**
	 * Sets the version of the SDL&reg; SmartDeviceLink interface
	 * 
	 * @param sdlMsgVersion
	 *            a SdlMsgVersion object representing version of the SDL&reg;
	 *            SmartDeviceLink interface
	 *            <p></p>
	 *            <b>Notes: </b>To be compatible, app msg major version number
	 *            must be less than or equal to SDL&reg; major version number.
	 *            If msg versions are incompatible, app has 20 seconds to
	 *            attempt successful RegisterAppInterface (w.r.t. msg version)
	 *            on underlying protocol session, else will be terminated. Major
	 *            version number is a compatibility declaration. Minor version
	 *            number indicates minor functional variations (e.g. features,
	 *            capabilities, bug fixes) when sent from SDL&reg; to app (in
	 *            RegisterAppInterface response). However, the minor version
	 *            number sent from the app to SDL&reg; (in RegisterAppInterface
	 *            request) is ignored by SDL&reg;
	 */
    public void setSdlMsgVersion(SdlMsgVersion sdlMsgVersion) {
		setParameters(KEY_SDL_MSG_VERSION, sdlMsgVersion);
    }

	/**
	 * Gets a Language enumeration indicating what language the application
	 * intends to use for user interaction (Display, TTS and VR)
	 * 
	 * @return Enumeration -a language enumeration
	 */
    public Language getLanguage() {
		return (Language) getObject(Language.class, KEY_LANGUAGE);
    }

	/**
	 * Sets an enumeration indicating what language the application intends to
	 * use for user interaction (Display, TTS and VR)
	 * 
	 * @param language
	 *            a Language Enumeration
	 *           
	 * 
	 */
    public void setLanguage(Language language) {
		setParameters(KEY_LANGUAGE, language);
    }

	/**
	 * Gets an enumeration indicating what language the application intends to
	 * use for user interaction ( Display)
	 * 
	 * @return Language - a Language value representing an enumeration
	 *         indicating what language the application intends to use for user
	 *         interaction ( Display)
	 * @since SmartDeviceLink 2.0
	 */
    public Language getHmiDisplayLanguage() {
		return (Language) getObject(Language.class, KEY_HMI_DISPLAY_LANGUAGE);
    }

	/**
	 * Sets an enumeration indicating what language the application intends to
	 * use for user interaction ( Display)
	 * 
	 * @param hmiDisplayLanguage
	 * @since SmartDeviceLink 2.0
	 */
    public void setHmiDisplayLanguage(Language hmiDisplayLanguage) {
		setParameters(KEY_HMI_DISPLAY_LANGUAGE, hmiDisplayLanguage);
    }

	/**
	 * Gets getDisplayCapabilities set when application interface is registered.
	 * 
	 * @return DisplayCapabilities
	 */
    @SuppressWarnings("unchecked")
    public DisplayCapabilities getDisplayCapabilities() {
		return (DisplayCapabilities) getObject(DisplayCapabilities.class, KEY_DISPLAY_CAPABILITIES);
    }
    /**
     * Sets Display Capabilities
     * @param displayCapabilities
     */
    public void setDisplayCapabilities(DisplayCapabilities displayCapabilities) {
		setParameters(KEY_DISPLAY_CAPABILITIES, displayCapabilities);
    }

	/**
	 * Gets buttonCapabilities set when application interface is registered.
	 * 
	 * @return buttonCapabilities
	 */
    @SuppressWarnings("unchecked")
    public List<ButtonCapabilities> getButtonCapabilities() {
		return (List<ButtonCapabilities>) getObject(ButtonCapabilities.class, KEY_BUTTON_CAPABILITIES);
    }
    /**
     * Sets Button Capabilities
     * @param buttonCapabilities
     */
    public void setButtonCapabilities(List<ButtonCapabilities> buttonCapabilities) {
		setParameters(KEY_BUTTON_CAPABILITIES, buttonCapabilities);
    }
    /**
	 * Gets getSoftButtonCapabilities set when application interface is registered.
	 * 
	 * @return SoftButtonCapabilities 
	 */
    @SuppressWarnings("unchecked")
    public List<SoftButtonCapabilities> getSoftButtonCapabilities() {
		return (List<SoftButtonCapabilities>) getObject(SoftButtonCapabilities.class, KEY_SOFT_BUTTON_CAPABILITIES);
    }
    /**
     * Sets softButtonCapabilities
     * @param softButtonCapabilities
     */
    public void setSoftButtonCapabilities(List<SoftButtonCapabilities> softButtonCapabilities) {
		setParameters(KEY_SOFT_BUTTON_CAPABILITIES, softButtonCapabilities);
    }

	/**
	 * Gets getPresetBankCapabilities set when application interface is registered.
	 * 
	 * @return PresetBankCapabilities 
	 */
    @SuppressWarnings("unchecked")
    public PresetBankCapabilities getPresetBankCapabilities() {
		return (PresetBankCapabilities) getObject(PresetBankCapabilities.class, KEY_PRESET_BANK_CAPABILITIES);
    }
    /**
     * Sets presetBankCapabilities
     * @param	presetBankCapabilities
     */
    public void setPresetBankCapabilities(PresetBankCapabilities presetBankCapabilities) {
		setParameters(KEY_PRESET_BANK_CAPABILITIES, presetBankCapabilities);
    }
	
	/**
	 * Gets hmiZoneCapabilities set when application interface is registered.
	 * 
	 * @return HmiZoneCapabilities
	 */
    @SuppressWarnings("unchecked")
    public List<HmiZoneCapabilities> getHmiZoneCapabilities() {
		return (List<HmiZoneCapabilities>) getObject(HmiZoneCapabilities.class, KEY_HMI_ZONE_CAPABILITIES);
    }
    /**
     * Sets hmiZoneCapabilities
     * @param hmiZoneCapabilities
     */
    public void setHmiZoneCapabilities(List<HmiZoneCapabilities> hmiZoneCapabilities) {
		setParameters(KEY_HMI_ZONE_CAPABILITIES, hmiZoneCapabilities);
    }
	
	/**
	 * Gets speechCapabilities set when application interface is registered.
	 * 
	 * @return SpeechCapabilities
	 */
    @SuppressWarnings("unchecked")
    public List<SpeechCapabilities> getSpeechCapabilities() {
    	Object speechCapabilities = getObject(SpeechCapabilities.class, KEY_SPEECH_CAPABILITIES);
		if (speechCapabilities instanceof List<?>) {
			return (List<SpeechCapabilities>) speechCapabilities;
		} else if (speechCapabilities instanceof SpeechCapabilities) {
			// this is a known issue observed with some core implementations
			List<SpeechCapabilities> newSpeechCapList = new ArrayList<>();
			newSpeechCapList.add((SpeechCapabilities) speechCapabilities);
			return newSpeechCapList;
		}
		return null;
    }
    /**
     * Sets speechCapabilities
     * @param speechCapabilities
     */
    public void setSpeechCapabilities(List<SpeechCapabilities> speechCapabilities) {
		setParameters(KEY_SPEECH_CAPABILITIES, speechCapabilities);
    }

    
    @SuppressWarnings("unchecked")
    public List<PrerecordedSpeech> getPrerecordedSpeech() {
		return (List<PrerecordedSpeech>) getObject(PrerecordedSpeech.class, KEY_PRERECORDED_SPEECH);
    }

    public void setPrerecordedSpeech(List<PrerecordedSpeech> prerecordedSpeech) {
		setParameters(KEY_PRERECORDED_SPEECH, prerecordedSpeech);
    }
 
    
	/**
	 * Gets vrCapabilities set when application interface is registered.
	 * 
	 * @return VrCapabilities
	 */
    @SuppressWarnings("unchecked")
    public List<VrCapabilities> getVrCapabilities() {
		return (List<VrCapabilities>) getObject(VrCapabilities.class, KEY_VR_CAPABILITIES);
    }
    /**
     * Sets VrCapabilities
     * @param vrCapabilities
     */
    public void setVrCapabilities(List<VrCapabilities> vrCapabilities) {
		setParameters(KEY_VR_CAPABILITIES, vrCapabilities);
    }
	
	/**
	 * Gets getVehicleType set when application interface is registered.
	 * 
	 * @return vehicleType 
	 */
    @SuppressWarnings("unchecked")
    public VehicleType getVehicleType() {
		return (VehicleType) getObject(VehicleType.class, KEY_VEHICLE_TYPE);
    }
    /**
     * Sets vehicleType
     * @param vehicleType
     */
    public void setVehicleType(VehicleType vehicleType) {
		setParameters(KEY_VEHICLE_TYPE, vehicleType);
    }
	
	/**
	 * Gets AudioPassThruCapabilities set when application interface is registered.
	 * 
	 * @return AudioPassThruCapabilities 
	 */
    @SuppressWarnings("unchecked")
    public List<AudioPassThruCapabilities> getAudioPassThruCapabilities() {
		return (List<AudioPassThruCapabilities>) getObject(AudioPassThruCapabilities.class, KEY_AUDIO_PASS_THRU_CAPABILITIES);
    }
    /**
     * Sets AudioPassThruCapabilities
     * @param audioPassThruCapabilities
     */
    public void setAudioPassThruCapabilities(List<AudioPassThruCapabilities> audioPassThruCapabilities) {
		setParameters(KEY_AUDIO_PASS_THRU_CAPABILITIES, audioPassThruCapabilities);
    }

	/**
	 * Gets pcmStreamingCapabilities set when application interface is registered.
	 *
	 * @return pcmStreamingCapabilities
	 */
	@SuppressWarnings("unchecked")
	public AudioPassThruCapabilities getPcmStreamingCapabilities() {
		return (AudioPassThruCapabilities) getObject(AudioPassThruCapabilities.class, KEY_PCM_STREAM_CAPABILITIES);
	}
	/**
	 * Sets pcmStreamingCapabilities
	 * @param pcmStreamingCapabilities
	 */
	public void setPcmStreamingCapabilities(AudioPassThruCapabilities pcmStreamingCapabilities) {
		setParameters(KEY_PCM_STREAM_CAPABILITIES, pcmStreamingCapabilities);
	}
	@Deprecated
    public String getProxyVersionInfo() {
		return null;
    }
    public void setSupportedDiagModes(List<Integer> supportedDiagModes) {
		setParameters(KEY_SUPPORTED_DIAG_MODES, supportedDiagModes);
    }

    @SuppressWarnings("unchecked")
    public List<Integer> getSupportedDiagModes() {
		return (List<Integer>) getObject(Integer.class, KEY_SUPPORTED_DIAG_MODES);
    }
    
    public void setHmiCapabilities(HMICapabilities hmiCapabilities) {
		setParameters(KEY_HMI_CAPABILITIES, hmiCapabilities);
    }

    @SuppressWarnings("unchecked")
    public HMICapabilities getHmiCapabilities() {
		return (HMICapabilities) getObject(HMICapabilities.class, KEY_HMI_CAPABILITIES);
    }  
    
    public void setSdlVersion(String sdlVersion) {
		setParameters(KEY_SDL_VERSION, sdlVersion);
    }

    public String getSdlVersion() {    
    	 return getString(KEY_SDL_VERSION);
    } 
    
    public void setSystemSoftwareVersion(String systemSoftwareVersion) {
		setParameters(KEY_SYSTEM_SOFTWARE_VERSION, systemSoftwareVersion);
    }

    public String getSystemSoftwareVersion() {
        return getString(KEY_SYSTEM_SOFTWARE_VERSION);
    }

	/**
	 * Sets Icon Resumed Boolean
	 * @param iconResumed - if param not included, set to false
	 */
	public void setIconResumed(Boolean iconResumed){
		if(iconResumed == null){
			iconResumed = false;
		}
		setParameters(KEY_ICON_RESUMED, iconResumed);
	}

	/**
	 * Tells developer whether or not their app icon has been resumed on core.
	 * @return boolean - true if icon was resumed, false if not
	 */
	public Boolean getIconResumed() {
		return getBoolean(KEY_ICON_RESUMED);
	}
}
