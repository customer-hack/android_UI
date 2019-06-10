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
import com.smartdevicelink.proxy.rpc.enums.AppHMIType;
import com.smartdevicelink.proxy.rpc.enums.Language;
import com.smartdevicelink.util.Version;

import java.util.Hashtable;
import java.util.List;
/**
 * Registers the application's interface with SDL&reg;, declaring properties of
 * the registration, including the messaging interface version, the app name,
 * etc. The mobile application must establish its interface registration with
 * SDL&reg; before any other interaction with SDL&reg; can take place. The
 * registration lasts until it is terminated either by the application calling
 * the <i> {@linkplain UnregisterAppInterface}</i> method, or by SDL&reg;
 * sending an <i> {@linkplain OnAppInterfaceUnregistered}</i> notification, or
 * by loss of the underlying transport connection, or closing of the underlying
 * message transmission protocol RPC session
 * <p></p>
 * Until the application receives its first <i>{@linkplain OnHMIStatus}</i>
 * Notification, its HMI Status is assumed to be: <i>
 * {@linkplain com.smartdevicelink.proxy.rpc.enums.HMILevel}</i>=NONE, <i>
 * {@linkplain com.smartdevicelink.proxy.rpc.enums.AudioStreamingState}
 * </i>=NOT_AUDIBLE, <i>
 * {@linkplain com.smartdevicelink.proxy.rpc.enums.SystemContext}</i>=MAIN
 * <p></p>
 * All SDL&reg; resources which the application creates or uses (e.g. Choice
 * Sets, Command Menu, etc.) are associated with the application's interface
 * registration. Therefore, when the interface registration ends, the SDL&reg;
 * resources associated with the application are disposed of. As a result, even
 * though the application itself may continue to run on its host platform (e.g.
 * mobile device) after the interface registration terminates, the application
 * will not be able to use the SDL&reg; HMI without first establishing a new
 * interface registration and re-creating its required SDL&reg; resources. That
 * is, SDL&reg; resources created by (or on behalf of) an application do not
 * persist beyond the life-span of the interface registration
 * <p></p>
 * Resources and settings whose lifespan is tied to the duration of an
 * application's interface registration:
 * <ul>
 * <li>Choice Sets</li>
 * <li>Command Menus (built by successive calls to <i>{@linkplain AddCommand}
 * </i>)</li>
 * <li>Media clock timer display value</li>
 * <li>Media clock timer display value</li>
 * <li>Media clock timer display value</li>
 * </ul>
 * <p></p>
 * The autoActivateID is used to grant an application the HMILevel and
 * AudioStreamingState it had when it last disconnected
 * <p></p>
 * <b>Notes: </b>The autoActivateID parameter, and associated behavior, is
 * currently ignored by SDL&reg;
 * <p></p>
 * When first calling this method (i.e. first time within life cycle of mobile
 * app), an autoActivateID should not be included. After successfully
 * registering an interface, an autoActivateID is returned to the mobile
 * application for it to use in subsequent connections. If the connection
 * between SDL&reg; and the mobile application is lost, such as the vehicle is
 * turned off while the application is running, the autoActivateID can then be
 * passed in another call to RegisterAppInterface to re-acquire <i>
 * {@linkplain com.smartdevicelink.proxy.rpc.enums.HMILevel}</i>=FULL
 * <p></p>
 * If the application intends to stream audio it is important to indicate so via
 * the isMediaApp parameter. When set to true, audio will reliably stream
 * without any configuration required by the user. When not set, audio may
 * stream, depending on what the user might have manually configured as a media
 * source on SDL&reg;
 * <p></p>
 * There is no time limit for how long the autoActivateID is "valid" (i.e. would
 * confer focus and opt-in)
 * 
 *<p> <b>HMILevel is not defined before registering</b></p>
 * 
 * 
 * <p><b>Parameter List</b></p>
 * <table border="1" rules="all">
 * 		<tr>
 * 			<th>Name</th>
 * 			<th>Type</th>
 * 			<th>Description</th>
 *                 <th>Reg.</th>
 *               <th>Notes</th>
 * 			<th>Version</th>
 * 		</tr>
 * 		<tr>
 * 			<td>MsgVersion</td>
 * 			<td>MsgVersion</td>
 * 			<td>Declares what version of the SDL interface the application expects to use with SDL</td>
 *                 <td>Y</td>
 *                 <td>To be compatible, app msg major version number must be less than or equal to SDL major version number. <p>If msg versions are incompatible, app has 20 seconds to attempt successful RegisterAppInterface (w.r.t. msg version) on underlying protocol session, else will be terminated. Major version number is a compatibility declaration. Minor version number indicates minor functional variations (e.g. features, capabilities, bug fixes) when sent from SDL to app (in RegisterAppInterface response).</p>However, the minor version number sent from the app to SDL (in RegisterAppInterface request) is ignored by SDL.</td>
 * 			<td>SmartDeviceLink 1.0 </td>
 * 		</tr>
 * 		<tr>
 * 			<td>appName</td>
 * 			<td>String</td>
 * 			<td>The mobile application's name. This name is displayed in the SDL Mobile Applications menu. It also serves as the unique identifier of the application for SDL .</td>
 *                 <td>Y</td>
 *                 <td><p> Must be 1-100 characters in length. Must consist of following characters: </p><p>May not be the same (by case insensitive comparison) as the name or any synonym of any currently registered application.</p> </td>
 * 			<td>SmartDeviceLink 1.0 </td>
 * 		</tr>
 * 		<tr>
 * 			<td>ttsName</td>
 * 			<td>TTSChunk</td>
 * 			<td>TTS string for VR recognition of the mobile application name. Meant to overcome any failing on speech engine in properly pronouncing / understanding app name.</td>
 *                 <td>N</td>
 *                 <td><p>Size must be 1-100 Needs to be unique over all applications. May not be empty.<p>May not start with a new line character.</p></td>
 * 			<td>SmartDeviceLink 2.0</td>
 * 		</tr>
 * 		<tr>
 * 			<td>ngnMediaScreenAppName</td>
 * 			<td>String</td>
 * 			<td>Provides an abbreviated version of the app name (if necessary) that will be displayed on the NGN media screen.</td>
 *                 <td>N</td>
 *                 <td>- Must be 1-5 characters. If not provided, value will be derived from appName truncated to 5 characters.</td>
 * 			<td>SmartDeviceLink 1.0 </td>
 * 		</tr>
 * 		<tr>
 * 			<td>vrSynonyms</td>
 * 			<td>String</td>
 * 			<td>An array of 1-100 elements, each element containing a voice-recognition synonym by which this app can be called when being addressed in the mobile applications menu.</td>
 *                 <td>N</td>
 *                 <td>Each vr synonym is limited to 40 characters, and there can be 1-100 synonyms in array. May not be the same (by case insensitive comparison) as the name or any synonym of any currently-registered application.</td>
 * 			<td>SmartDeviceLink 1.0 </td>
 * 		</tr>
 * 		<tr>
 * 			<td>isMediaApplication</td>
 * 			<td>Boolean</td>
 * 			<td>Indicates that the application will be streaming audio to SDL (via A2DP) that is audible outside of the BT media source.</td>
 *                 <td>Y</td>
 *                 <td></td>
 * 			<td>SmartDeviceLink 1.0 </td>
 * 		</tr>
 * 		<tr>
 * 			<td>languageDesired</td>
 * 			<td>Language</td>
 * 			<td>An enumeration indicating what language the application intends to use for user interaction (Display, TTS and VR).</td>
 *                 <td>Y</td>
 *                 <td>If the language indicated does not match the active language on SDL, the interface registration will be rejected.If the user changes the SDL language while this interface registration is active, the interface registration will be terminated. </td>
 * 			<td>SmartDeviceLink 1.0</td>
 * 		</tr>
 * 		<tr>
 * 			<td>hmiDisplayLanguageDesired</td>
 * 			<td>Language</td>
 * 			<td>An enumeration indicating what language the application intends to use for user interaction ( Display).</td>
 *                 <td>Y</td>
 *                 <td></td>
 * 			<td>SmartDeviceLink 2.0 </td>
 * 		</tr>
 * 		<tr>
 * 			<td>appHMIType</td>
 * 			<td>AppHMIType</td>
 * 			<td>List of all applicable app types stating which classifications to be given to the app.e.g. for platforms , like GEN2, this will determine which "corner(s)" the app can populate</td>
 *                 <td>N</td>
 *                 <td>Array Minsize: 1; Array Maxsize: 100</td>
 * 			<td>SmartDeviceLink 2.0 </td>
 * 		</tr>
 * 		<tr>
 * 			<td>hashID</td>
 * 			<td>String</td>
 * 			<td>ID used to uniquely identify current state of all app data that can persist through connection cycles (e.g. ignition cycles).This registered data (commands, submenus, choice sets, etc.) can be reestablished without needing to explicitly reregister each piece. If omitted, then the previous state of an app's commands, etc. will not be restored.When sending hashID, all RegisterAppInterface parameters should still be provided (e.g. ttsName, etc.). </td>
 *                 <td>N</td>
 *                 <td>maxlength:100</td>
 * 			<td>SmartDeviceLink 2.3.1 </td>
 * 		</tr>
 * 		<tr>
 * 			<td>deviceInfo</td>
 * 			<td>DeviceInfo</td>
 * 			<td>Various information abount connecting device.</td>
 *                 <td>N</td>
 *                 <td></td>
 * 			<td>SmartDeviceLink 2.3.1 </td>
 * 		</tr>
 * 		<tr>
 * 			<td>appID</td>
 * 			<td>String</td>
 * 			<td>ID used to validate app with policy table entries</td>
 *                 <td>Y</td>
 *                 <td>Maxlength: 100</td>
 * 			<td>SmartDeviceLink 2.0 </td>
 * 		</tr>
 * 		<tr>
 * 			<td>fullAppID</td>
 * 			<td>String</td>
 * 			<td>ID used to validate app with policy table entries</td>
 *                 <td>N</td>
 *                 <td>Maxlength: 100</td>
 * 			<td>SmartDeviceLink 5.0</td>
 * 		</tr>
 * 		<tr>
 * 			<td>hmiCapabilities</td>
 * 			<td>HMICapabilities</td>
 * 			<td>Specifies the HMI capabilities.</td>
 *                 <td>N</td>
 *                 <td></td>
 * 			<td>SmartDeviceLink 2.3.2.2 </td>
 * 		</tr>
 * 
 * 		<tr>
 * 			<td>sdlVersion</td>
 * 			<td>String</td>
 * 			<td>The SmartDeviceLink version.</td>
 *                 <td>N</td>
 *                 <td>Maxlength: 100</td>
 * 			<td>SmartDeviceLink 2.3.2.2</td>
 * 		</tr>
 *
 * 		<tr>
 * 			<td>systemSoftwareVersion</td>
 * 			<td>String</td>
 * 			<td>The software version of the system that implements the SmartDeviceLink core.</td>
 *                 <td>N</td>
 *                 <td>Maxlength: 100</td>
 * 			<td>SmartDeviceLink 2.3.2.2</td>
 * 		</tr>
 *
 * 		<tr>
 * 			<td>dayColorScheme</td>
 * 			<td>TemplateColorScheme</td>
 * 			<td>The color scheme that is used for day.</td>
 *                 <td>N</td>
 *                 <td></td>
 * 			<td>SmartDeviceLink 5.0</td>
 * 		</tr>
 *
 * 		<tr>
 * 			<td>nightColorScheme</td>
 * 			<td>TemplateColorScheme</td>
 * 			<td>The color scheme that is used for night.</td>
 *                 <td>N</td>
 *                 <td></td>
 * 			<td>SmartDeviceLink 5.0</td>
 * 		</tr>
 *  </table>
 *  <p></p>
 * @since SmartDeviceLink 1.0
 * @see UnregisterAppInterface
 * @see OnAppInterfaceUnregistered
 */
public class RegisterAppInterface extends RPCRequest {
	public static final String KEY_TTS_NAME = "ttsName";
	public static final String KEY_HMI_DISPLAY_LANGUAGE_DESIRED = "hmiDisplayLanguageDesired";
	public static final String KEY_APP_HMI_TYPE = "appHMIType";
	public static final String KEY_APP_ID = "appID";
	public static final String KEY_FULL_APP_ID = "fullAppID";
	public static final String KEY_LANGUAGE_DESIRED = "languageDesired";
	public static final String KEY_DEVICE_INFO = "deviceInfo";
	public static final String KEY_APP_NAME = "appName";
	public static final String KEY_NGN_MEDIA_SCREEN_APP_NAME = "ngnMediaScreenAppName";
	public static final String KEY_IS_MEDIA_APPLICATION = "isMediaApplication";
	public static final String KEY_VR_SYNONYMS = "vrSynonyms";
	public static final String KEY_SDL_MSG_VERSION = "syncMsgVersion";
	public static final String KEY_HASH_ID = "hashID";
	public static final String KEY_DAY_COLOR_SCHEME = "dayColorScheme";
	public static final String KEY_NIGHT_COLOR_SCHEME = "nightColorScheme";
	private static final int APP_ID_MAX_LENGTH = 10;

	/**
	 * Constructs a new RegisterAppInterface object
	 */
    public RegisterAppInterface() {
        super(FunctionID.REGISTER_APP_INTERFACE.toString());
    }
	/**
	 * Constructs a new RegisterAppInterface object indicated by the Hashtable
	 * parameter
	 * <p></p>
	 * 
	 * @param hash
	 *            The Hashtable to use
	 */    
    public RegisterAppInterface(Hashtable<String, Object> hash) {
        super(hash);
    }
	/**
	 * Constructs a new RegisterAppInterface object
	 * @param syncMsgVersion a SdlMsgVersion object representing version of the SDL&reg; SmartDeviceLink interface <br>
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
	 * @param appName a String value representing the Mobile Application's Name <br>
	 *            <b>Notes: </b>
	 *            <ul>
	 *            <li>Must be 1-100 characters in length</li>
	 *            <li>May not be the same (by case insensitive comparison) as
	 *            the name or any synonym of any currently-registered
	 *            application</li>
	 *            </ul>
	 * @param isMediaApplication a Boolean value
	 * @param languageDesired a Language Enumeration
	 * @param hmiDisplayLanguageDesired the requested language to be used on the HMI/Display
	 * @param fullAppID a String value representing a unique ID, which an app will be given when approved <br>
	 *            <b>Notes: </b>Maxlength = 100
	 */
	public RegisterAppInterface(@NonNull SdlMsgVersion syncMsgVersion, @NonNull String appName, @NonNull Boolean isMediaApplication,
								@NonNull Language languageDesired, @NonNull Language hmiDisplayLanguageDesired, @NonNull String fullAppID) {
		this();
		setSdlMsgVersion(syncMsgVersion);
		setAppName(appName);
		setIsMediaApplication(isMediaApplication);
		setLanguageDesired(languageDesired);
		setHmiDisplayLanguageDesired(hmiDisplayLanguageDesired);
		setFullAppID(fullAppID);
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
	 *
	 */    
    public void setSdlMsgVersion(@NonNull SdlMsgVersion sdlMsgVersion) {
        setParameters(KEY_SDL_MSG_VERSION, sdlMsgVersion);
    }
    
    @SuppressWarnings("unchecked")
    public DeviceInfo getDeviceInfo() {
        return (DeviceInfo) getObject(DeviceInfo.class, KEY_DEVICE_INFO);
    }    
    
    public void setDeviceInfo(DeviceInfo deviceInfo) {
		setParameters(KEY_DEVICE_INFO, deviceInfo);
    }    
	/**
	 * Gets Mobile Application's Name
	 * 
	 * @return String -a String representing the Mobile Application's Name
	 */    
    public String getAppName() {
        return getString(KEY_APP_NAME);
    }
	/**
	 * Sets Mobile Application's Name, This name is displayed in the SDL&reg;
	 * Mobile Applications menu. It also serves as the unique identifier of the
	 * application for SmartDeviceLink
	 * 
	 * @param appName
	 *            a String value representing the Mobile Application's Name
	 *            <p></p>
	 *            <b>Notes: </b>
	 *            <ul>
	 *            <li>Must be 1-100 characters in length</li>
	 *            <li>May not be the same (by case insensitive comparison) as
	 *            the name or any synonym of any currently-registered
	 *            application</li>
	 *            </ul>
	 */    
    public void setAppName(@NonNull String appName) {
		setParameters(KEY_APP_NAME, appName);
    }

	/**
	 * Gets TTS string for VR recognition of the mobile application name
	 * 
	 * @return List<TTSChunk> -List value representing the TTS string
	 * @since SmartDeviceLink 2.0
	 */
    @SuppressWarnings("unchecked")
    public List<TTSChunk> getTtsName() {
        return (List<TTSChunk>) getObject(TTSChunk.class, KEY_TTS_NAME);
    }

	/**
	 * 
	 * @param ttsName
	 *            a List<TTSChunk> value represeting the TTS Name
	 *            <p></p>
	 *            <b>Notes: </b>
	 *            <ul>
	 *            <li>Size must be 1-100</li>
	 *            <li>Needs to be unique over all applications</li>
	 *            <li>May not be empty</li>
	 *            <li>May not start with a new line character</li>
	 *            <li>May not interfere with any name or synonym of previously
	 *            registered applications and the following list of words</li>
	 *            <li>Needs to be unique over all applications. Applications
	 *            with the same name will be rejected</li>
	 *            </ul>
	 * @since SmartDeviceLink 2.0
	 */
    public void setTtsName(List<TTSChunk> ttsName) {
		setParameters(KEY_TTS_NAME, ttsName);
    }
	/**
	 * Gets a String representing an abbreviated version of the mobile
	 * applincation's name (if necessary) that will be displayed on the NGN
	 * media screen
	 * 
	 * @return String -a String value representing an abbreviated version of the
	 *         mobile applincation's name
	 */    
    public String getNgnMediaScreenAppName() {
        return getString(KEY_NGN_MEDIA_SCREEN_APP_NAME);
    }
	/**
	 * Sets a String representing an abbreviated version of the mobile
	 * applincation's name (if necessary) that will be displayed on the NGN
	 * media screen
	 * 
	 * @param ngnMediaScreenAppName
	 *            a String value representing an abbreviated version of the
	 *            mobile applincation's name
	 *            <p></p>
	 *            <b>Notes: </b>
	 *            <ul>
	 *            <li>Must be 1-5 characters</li>
	 *            <li>If not provided, value will be derived from appName
	 *            truncated to 5 characters</li>
	 *            </ul>
	 */    
    public void setNgnMediaScreenAppName(String ngnMediaScreenAppName) {
		setParameters(KEY_NGN_MEDIA_SCREEN_APP_NAME, ngnMediaScreenAppName);
    }
	/**
	 * Gets the List<String> representing the an array of 1-100 elements, each
	 * element containing a voice-recognition synonym
	 * 
	 * @return List<String> -a List value representing the an array of
	 *         1-100 elements, each element containing a voice-recognition
	 *         synonym
	 */    
    @SuppressWarnings("unchecked")
    public List<String> getVrSynonyms() {
        return (List<String>) getObject(String.class, KEY_VR_SYNONYMS);
    }
	/**
	 * Sets a vrSynonyms representing the an array of 1-100 elements, each
	 * element containing a voice-recognition synonym
	 * 
	 * @param vrSynonyms
	 *            a List<String> value representing the an array of 1-100
	 *            elements
	 *            <p></p>
	 *            <b>Notes: </b>
	 *            <ul>
	 *            <li>Each vr synonym is limited to 40 characters, and there can
	 *            be 1-100 synonyms in array</li>
	 *            <li>May not be the same (by case insensitive comparison) as
	 *            the name or any synonym of any currently-registered
	 *            application</li>
	 *            </ul>
	 */    
    public void setVrSynonyms(List<String> vrSynonyms) {
		setParameters(KEY_VR_SYNONYMS, vrSynonyms);
    }
	/**
	 * Gets a Boolean representing MediaApplication
	 * 
	 * @return Boolean -a Boolean value representing a mobile application that is
	 *         a media application or not
	 */    
    public Boolean getIsMediaApplication() {
        return getBoolean(KEY_IS_MEDIA_APPLICATION);
    }
	/**
	 * Sets a Boolean to indicate a mobile application that is a media
	 * application or not
	 * 
	 * @param isMediaApplication
	 *            a Boolean value
	 */    
    public void setIsMediaApplication(@NonNull Boolean isMediaApplication) {
		setParameters(KEY_IS_MEDIA_APPLICATION, isMediaApplication);
    }
	/**
	 * Gets a Language enumeration indicating what language the application
	 * intends to use for user interaction (Display, TTS and VR)
	 * 
	 * @return Enumeration -a language enumeration
	 */    
    public Language getLanguageDesired() {
        return (Language) getObject(Language.class, KEY_LANGUAGE_DESIRED);
    }
	/**
	 * Sets an enumeration indicating what language the application intends to
	 * use for user interaction (Display, TTS and VR)
	 * 
	 * @param languageDesired
	 *            a Language Enumeration
	 *            
	 * 
	 */    
    public void setLanguageDesired(@NonNull Language languageDesired) {
		setParameters(KEY_LANGUAGE_DESIRED, languageDesired);
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
    public Language getHmiDisplayLanguageDesired() {
        return (Language) getObject(Language.class, KEY_HMI_DISPLAY_LANGUAGE_DESIRED);
    }

	/**
	 * Sets an enumeration indicating what language the application intends to
	 * use for user interaction ( Display)
	 * 
	 * @param hmiDisplayLanguageDesired the requested language to be used on the HMI/Display
	 * @since SmartDeviceLink 2.0
	 */
    public void setHmiDisplayLanguageDesired(@NonNull Language hmiDisplayLanguageDesired) {
		setParameters(KEY_HMI_DISPLAY_LANGUAGE_DESIRED, hmiDisplayLanguageDesired);
    }

	/**
	 * Gets a list of all applicable app types stating which classifications to
	 * be given to the app.e.g. for platforms , like GEN2, this will determine
	 * which "corner(s)" the app can populate
	 * 
	 * @return List<AppHMIType> - a List value representing a list of all
	 *         applicable app types stating which classifications to be given to
	 *         the app
	 * @since SmartDeviceLinke 2.0
	 */
    @SuppressWarnings("unchecked")
    public List<AppHMIType> getAppHMIType() {
        return (List<AppHMIType>) getObject(AppHMIType.class, KEY_APP_HMI_TYPE);
    }

	/**
	 * Sets a a list of all applicable app types stating which classifications
	 * to be given to the app. e.g. for platforms , like GEN2, this will
	 * determine which "corner(s)" the app can populate
	 * 
	 * @param appHMIType
	 *            a List<AppHMIType>
	 *            <p></p>
	 *            <b>Notes: </b>
	 *            <ul>
	 *            <li>Array Minsize: = 1</li>
	 *            <li>Array Maxsize = 100</li>
	 *            </ul>
	 * @since SmartDeviceLink 2.0
	 */
    public void setAppHMIType(List<AppHMIType> appHMIType) {
		setParameters(KEY_APP_HMI_TYPE, appHMIType);
    }
    
    public String getHashID() {
        return getString(KEY_HASH_ID);
    }
   
    public void setHashID(String hashID) {
		setParameters(KEY_HASH_ID, hashID);
    }

	/**
	 * Gets the unique ID, which an app will be given when approved
	 *
	 * @return String - a String value representing the unique ID, which an app
	 *         will be given when approved
	 * @since SmartDeviceLink 2.0
	 */
	public String getAppID() {
		return getString(KEY_APP_ID);
	}

	/**
	 * Sets a unique ID, which an app will be given when approved
	 *
	 * @param appID
	 *            a String value representing a unique ID, which an app will be
	 *            given when approved
	 *            <p></p>
	 *            <b>Notes: </b>Maxlength = 100
	 * @since SmartDeviceLink 2.0
	 */
	public void setAppID(@NonNull String appID) {
		if (appID != null) {
			setParameters(KEY_APP_ID, appID.toLowerCase());
		} else {
			setParameters(KEY_APP_ID, appID);
		}
	}

	/**
	 * Gets the unique ID, which an app will be given when approved
	 *
	 * @return String - a String value representing the unique ID, which an app
	 *         will be given when approved
	 * @since SmartDeviceLink 5.0
	 */
	public String getFullAppID() {
		return getString(KEY_FULL_APP_ID);
	}

	/**
	 * Sets a unique ID, which an app will be given when approved <br>
	 * Note: this will automatically parse the fullAppID into the smaller appId and set the appId value as well
	 * @param fullAppID
	 *            a String value representing a unique ID, which an app will be
	 *            given when approved
	 *            <p></p>
	 *            <b>Notes: </b>Maxlength = 100
	 * @since SmartDeviceLink 5.0
	 */
	public void setFullAppID(String fullAppID) {
		if (fullAppID != null) {
			fullAppID = fullAppID.toLowerCase();
			setParameters(KEY_FULL_APP_ID, fullAppID);
			String appID;
			if (fullAppID.length() <= APP_ID_MAX_LENGTH) {
				appID = fullAppID;
			} else {
				appID = fullAppID.replace("-", "").substring(0, APP_ID_MAX_LENGTH);
			}
			setAppID(appID);
		} else {
			setParameters(KEY_FULL_APP_ID, null);
		}
	}

	@Override
	public void format(Version rpcVersion, boolean formatParams) {
		if(rpcVersion == null || rpcVersion.getMajor() >= 5) {
			if (getFullAppID() == null) {
				setFullAppID(getAppID());
			}
		}
		super.format(rpcVersion, formatParams);
	}

	/**
	 * Gets the color scheme that is currently used for day
	 *
	 * @return TemplateColorScheme - a TemplateColorScheme object representing the colors that are used
	 * for day color scheme
	 * @since SmartDeviceLink 5.0
	 */
    public TemplateColorScheme getDayColorScheme(){
		return (TemplateColorScheme) getObject(TemplateColorScheme.class, KEY_DAY_COLOR_SCHEME);
	}

	/**
	 * Sets the color scheme that is intended to be used for day
	 *
	 * @param templateColorScheme a TemplateColorScheme object representing the colors that will be
	 * used for day color scheme
	 * @since SmartDeviceLink 5.0
	 */
    public void setDayColorScheme(TemplateColorScheme templateColorScheme){
		setParameters(KEY_DAY_COLOR_SCHEME, templateColorScheme);
	}

	/**
	 * Gets the color scheme that is currently used for night
	 *
	 * @return TemplateColorScheme - a TemplateColorScheme object representing the colors that are used
	 * for night color scheme
	 * @since SmartDeviceLink 5.0
	 */
	public TemplateColorScheme getNightColorScheme(){
		return (TemplateColorScheme) getObject(TemplateColorScheme.class, KEY_NIGHT_COLOR_SCHEME);
	}

	/**
	 * Sets the color scheme that is intended to be used for night
	 *
	 * @param templateColorScheme a TemplateColorScheme object representing the colors that will be
	 * used for night color scheme
	 * @since SmartDeviceLink 5.0
	 */
	public void setNightColorScheme(TemplateColorScheme templateColorScheme){
		setParameters(KEY_NIGHT_COLOR_SCHEME, templateColorScheme);
	}
}
