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
package com.smartdevicelink.proxy.constants;

@Deprecated
public class Names {
	public static final String request = "request";
	public static final String response = "response";
	public static final String notification = "notification";
	public static final String function_name = "name";
	public static final String parameters = "parameters";
	public static final String bulkData = "bulkData";

	public static final String RegisterAppInterface = "RegisterAppInterface";
	public static final String UnregisterAppInterface = "UnregisterAppInterface";
	public static final String Alert = "Alert";
	public static final String Show = "Show";
	public static final String Speak = "Speak";
	public static final String AddCommand = "AddCommand";
	public static final String DeleteCommand = "DeleteCommand";
	public static final String AddSubMenu = "AddSubMenu";
	public static final String DeleteSubMenu = "DeleteSubMenu";
	public static final String CreateInteractionChoiceSet = "CreateInteractionChoiceSet";
	public static final String DeleteInteractionChoiceSet = "DeleteInteractionChoiceSet";
	public static final String PerformInteraction = "PerformInteraction";
	public static final String DialNumber = "DialNumber";
	public static final String EncodedSyncPData = "EncodedSyncPData";
	public static final String SyncPData = "SyncPData";
	public static final String SubscribeButton = "SubscribeButton";
	public static final String UnsubscribeButton = "UnsubscribeButton";
	public static final String SubscribeVehicleData = "SubscribeVehicleData";
	public static final String UnsubscribeVehicleData = "UnsubscribeVehicleData";
	public static final String SetMediaClockTimer = "SetMediaClockTimer";
	public static final String SetGlobalProperties = "SetGlobalProperties";
	public static final String GenericResponse = "GenericResponse";
	public static final String ScrollableMessage = "ScrollableMessage";
	public static final String GetDID = "GetDID";
	public static final String GetDTCs = "GetDTCs";
	public static final String DiagnosticMessage = "DiagnosticMessage";
	public static final String SystemRequest = "SystemRequest";
	public static final String ReadDID = "ReadDID";
	public static final String OnVehicleData = "OnVehicleData";
	public static final String GetFile = "GetFile";
	public static final String PutFile = "PutFile";
	public static final String DeleteFile = "DeleteFile";
	public static final String ListFiles = "ListFiles";
	public static final String EndAudioCapture = "EndAudioCapture";
	public static final String GetVehicleData = "GetVehicleData";
	public static final String ResetGlobalProperties = "ResetGlobalProperties";
	public static final String PerformAudioCapture = "PerformAudioCapture";
	public static final String SetAppIcon = "SetAppIcon";
	public static final String ChangeRegistration = "ChangeRegistration";
	public static final String SetDisplayLayout = "SetDisplayLayout";	
	public static final String keypressMode = "keypressMode";
	public static final String keyboardLayout = "keyboardLayout";
	public static final String limitedCharacterList = "limitedCharacterList";
	public static final String autoCompleteText = "autoCompleteText";
	public static final String OnLanguageChange = "OnLanguageChange";	
	public static final String hmiDisplayLanguage = "hmiDisplayLanguage";
	public static final String displayLayout = "displayLayout";
	public static final String ttsName = "ttsName";
	public static final String hmiDisplayLanguageDesired = "hmiDisplayLanguageDesired";
	public static final String appHMIType = "appHMIType";
	public static final String hashID = "hashID";
	public static final String appID = "appID";
	public static final String vrHelpTitle = "vrHelpTitle";
	public static final String graphic = "graphic";
	public static final String customPresets = "customPresets";
	public static final String softButtonCapabilities = "softButtonCapabilities";
	public static final String presetBankCapabilities = "presetBankCapabilities";
	public static final String vehicleType = "vehicleType";
	public static final String make = "make";
	public static final String model = "model";
	public static final String modelYear = "modelYear";
	public static final String trim = "trim";
	public static final String allowed = "allowed";
	public static final String userDisallowed = "userDisallowed";
	public static final String rpcName = "rpcName";
	public static final String hmiPermissions = "hmiPermissions";
	public static final String parameterPermissions = "parameterPermissions";
	public static final String permissionItem = "permissionItem";
	public static final String numTicks = "numTicks";
	public static final String sliderHeader = "sliderHeader";
	public static final String sliderFooter = "sliderFooter";
	public static final String PerformAudioPassThru = "PerformAudioPassThru";
	public static final String PerformAudioPassThruResponse = "PerformAudioPassThruResponse";
	public static final String EndAudioPassThru = "EndAudioPassThru";
	public static final String EndAudioPassThruResponse = "EndAudioPassThruResponse";
	public static final String OnAudioPassThru = "OnAudioPassThru";
	public static final String ShowConstantTBT = "ShowConstantTBT";
	public static final String AlertManeuver = "AlertManeuver";
	public static final String UpdateTurnList = "UpdateTurnList";
	
	public static final String OnCommand = "OnCommand";
	public static final String OnDataPublished = "OnDataPublished";
	public static final String OnButtonPress = "OnButtonPress";
	public static final String OnButtonEvent = "OnButtonEvent";
	public static final String OnHMIStatus = "OnHMIStatus";
	public static final String OnTBTClientState = "OnTBTClientState";
	public static final String OnEncodedSyncPData = "OnEncodedSyncPData";
	public static final String onEncodedSyncPDataResponse = "onEncodedSyncPDataResponse";
	public static final String OnSyncPData = "OnSyncPData";
	public static final String onOnSyncPData = "onOnSyncPData";
	public static final String OnDriverDistraction = "OnDriverDistraction";
	public static final String OnAppInterfaceUnregistered = "OnAppInterfaceUnregistered";
	public static final String OnKeyboardInput = "OnKeyboardInput";
	public static final String OnTouchEvent = "OnTouchEvent";
	public static final String OnSystemRequest = "OnSystemRequest";
	public static final String OnHashChange = "OnHashChange";
	public static final String OnProxyClosed = "OnProxyClosed";
	public static final String OnProxyError = "OnProxyError";
	public static final String OnProxyOpened = "OnProxyOpened";
	public static final String OnProxyUnusable = "OnProxyUnusable";
	public static final String OnHMILevelChange = "OnHMILevelChange";
	public static final String OnSdlChoiceChosen = "OnSdlChoiceChosen";
	public static final String OnPermissionsChange = "OnPermissionsChange";
	public static final String OnScreenPresetsAvailable = "OnScreenPresetsAvailable";
	public static final String isHighlighted = "isHighlighted";
	public static final String softButtonID = "softButtonID";
	public static final String fileType = "fileType";
	public static final String url = "url";
	public static final String requestType = "requestType";
	public static final String fileName = "fileName";
	public static final String persistentFile = "persistentFile";
	public static final String spaceAvailable = "spaceAvailable";
	public static final String filenames = "filenames";
	public static final String cmdIcon = "cmdIcon";
	public static final String Slider = "Slider";
	public static final String sliderPosition = "sliderPosition";
	public static final String samplingRate = "samplingRate";
	public static final String audioType = "audioType";
	public static final String satRadioESN = "satRadioESN";
	public static final String dtc = "dtc";
	public static final String tryAgainTime = "tryAgainTime";

	public static final String success = "success";
	public static final String resultCode = "resultCode";
	public static final String info = "info";
	
	public static final String payload = "payload";
	public static final String reason = "reason";
	public static final String state = "state";
	public static final String cmdID = "cmdID";
	public static final String menuParams = "menuParams";
	public static final String parentID = "parentID";
	public static final String position = "position";
	public static final String menuName = "menuName";
	public static final String vrCommands = "vrCommands";
	public static final String language = "language";
	public static final String languageDesired = "languageDesired";
	public static final String triggerSource = "triggerSource";
	public static final String subscriptionType = "subscriptionType";
	public static final String data = "data";
	public static final String event = "event";
	public static final String correlationID = "correlationID";
	public static final String sdlMsgVersion = "syncMsgVersion";
	public static final String deviceInfo = "deviceInfo";	
	public static final String majorVersion = "majorVersion";
	public static final String minorVersion = "minorVersion";
	public static final String appName = "appName";
	public static final String ngnMediaScreenAppName = "ngnMediaScreenAppName";
	public static final String isMediaApplication = "isMediaApplication";
	public static final String vrSynonyms = "vrSynonyms";
	public static final String usesVehicleData = "usesVehicleData";
	public static final String text = "text";
	public static final String type = "type";
	public static final String ttsChunks = "ttsChunks";
	public static final String playTone = "playTone";
	public static final String duration = "duration";
	public static final String mainField1 = "mainField1";
	public static final String mainField2 = "mainField2";
	public static final String mainField3 = "mainField3";
	public static final String mainField4 = "mainField4";
	public static final String statusBar = "statusBar";
	public static final String name = "name";
	public static final String menuID = "menuID";
	public static final String longPress = "longPress";
	public static final String shortPress = "shortPress";
	public static final String buttonName = "buttonName";
	public static final String buttonPressMode = "buttonPressMode";
	public static final String buttonEventMode = "buttonEventMode";
	public static final String minutes = "minutes";
	public static final String seconds = "seconds";
	public static final String startTime = "startTime";
	public static final String endTime = "endTime";
	public static final String updateMode = "updateMode";
	public static final String mediaClock = "mediaClock";
	public static final String initialText = "initialText";
	public static final String initialPrompt = "initialPrompt";
	public static final String helpPrompt = "helpPrompt";
	public static final String timeoutPrompt = "timeoutPrompt";
	public static final String timeout = "timeout";
	public static final String choiceSet = "choiceSet";
	public static final String interactionMode = "interactionMode";
	public static final String result = "result";
	public static final String alertText1 = "alertText1";
	public static final String alertText2 = "alertText2";
	public static final String alertText3 = "alertText3";
	public static final String shortPressAvailable = "shortPressAvailable";
	public static final String longPressAvailable = "longPressAvailable";
	public static final String upDownAvailable = "upDownAvailable";
	public static final String width = "width";
	public static final String height = "height";
	public static final String resolutionWidth = "resolutionWidth";
	public static final String resolutionHeight = "resolutionHeight";
	public static final String characterSet = "characterSet";
	public static final String displayType = "displayType";
	public static final String mediaClockFormats = "mediaClockFormats";
	public static final String textFields = "textFields";
	public static final String imageFields = "imageFields";
	public static final String autoActivateID = "autoActivateID";
	public static final String vehicleDataCapabilities = "vehicleDataCapabilities";
	public static final String speechCapabilities = "speechCapabilities";
	public static final String vrCapabilities = "vrCapabilities";
	public static final String audioPassThruCapabilities = "audioPassThruCapabilities";
	public static final String buttonCapabilities = "buttonCapabilities";
	public static final String displayCapabilities = "displayCapabilities";
	public static final String hmiZoneCapabilities = "hmiZoneCapabilities";
	public static final String interactionChoiceSetID = "interactionChoiceSetID";
	public static final String interactionChoiceSetIDList = "interactionChoiceSetIDList";
	public static final String audioFileName = "audioFileName";
	public static final String gpsPositionValid = "gpsPositionValid";
	public static final String longitudeDegrees = "longitudeDegrees";
	public static final String latitudeDegrees = "latitudeDegrees";
	public static final String utcYear = "utcYear";
	public static final String utcMonth = "utcMonth";
	public static final String utcDay = "utcDay";
	public static final String utcHours = "utcHours";
	public static final String utcMinutes = "utcMinutes";
	public static final String utcSeconds = "utcSeconds";
	public static final String compassDirection = "compassDirection";
	public static final String pdop = "pdop";
	public static final String vdop = "vdop";
	public static final String hdop = "hdop";
	public static final String actual = "actual";
	public static final String satellites = "satellites";
	public static final String dimension = "dimension";
	public static final String altitude = "altitude";
	public static final String heading = "heading";
	public static final String speed = "speed";
	public static final String number = "number";
	public static final String smartDeviceLinkFileName = "syncFileName";
	public static final String localFileName = "localFileName";
	public static final String maxDuration = "maxDuration";
	public static final String timerMode = "timerMode";
	public static final String status = "status";
	public static final String pressure = "pressure";
	public static final String hours = "hours";
	public static final String rows = "rows";
	public static final String pressureTellTale = "pressureTellTale";
	public static final String leftFront = "leftFront";
	public static final String rightFront = "rightFront";
	public static final String leftRear = "leftRear";
	public static final String rightRear = "rightRear";
	public static final String innerLeftRear = "innerLeftRear";
	public static final String innerRightRear = "innerRightRear";
	public static final String VehicleData = "VehicleData";
	public static final String alignment = "alignment";
	public static final String mediaTrack = "mediaTrack";
	public static final String properties = "properties";
	public static final String choiceID = "choiceID";
	public static final String bitsPerSample = "bitsPerSample";
	public static final String hmiLevel = "hmiLevel";
	public static final String audioStreamingState = "audioStreamingState";
	public static final String systemContext = "systemContext";
	public static final String sdlChoice = "sdlChoice";
	public static final String sdlCommand = "sdlCommand";
	public static final String URL = "URL";
	public static final String Timeout = "Timeout";
	public static final String PermissionGroupName = "PermissionGroupName";
	public static final String PermissionGroupStatus = "PermissionGroupStatus";
	public static final String PermissionGroupItems = "PermissionGroupItems";
	public static final String audioPacket = "audioPacket";
	public static final String audioPassThruDisplayText1 = "audioPassThruDisplayText1";
	public static final String audioPassThruDisplayText2 = "audioPassThruDisplayText2";
	public static final String bitRate = "bitRate";
	public static final String rpm = "rpm";
	public static final String fuelLevel = "fuelLevel";
	public static final String avgFuelEconomy = "avgFuelEconomy";
	public static final String batteryVoltage = "batteryVoltage";
	public static final String externalTemperature = "externalTemperature";
	public static final String vin = "vin";
	public static final String prndl = "prndl";
	public static final String tirePressure = "tirePressure";
	public static final String batteryPackVoltage = "batteryPackVoltage";
	public static final String batteryPackCurrent = "batteryPackCurrent";
	public static final String batteryPackTemperature = "batteryPackTemperature";
	public static final String engineTorque = "engineTorque";
	public static final String odometer = "odometer";
	public static final String tripOdometer = "tripOdometer";
	public static final String genericbinary = "genericbinary";
	public static final String GPSData = "GPSData";
	public static final String gps = "gps";
	public static final String fuelLevel_State = "fuelLevel_State";
	public static final String instantFuelConsumption = "instantFuelConsumption";
	public static final String beltStatus = "beltStatus";
	public static final String bodyInformation = "bodyInformation";
	public static final String deviceStatus = "deviceStatus";
	public static final String driverBraking = "driverBraking";
	public static final String wiperStatus = "wiperStatus";
	public static final String fuelEconomy = "fuelEconomy";
	public static final String engineOilLife = "engineOilLife";
	public static final String headLampStatus = "headLampStatus";
	public static final String brakeTorque = "brakeTorque";
	public static final String turboBoost = "turboBoost";
	public static final String coolantTemp = "coolantTemp";
	public static final String airFuelRatio = "airFuelRatio";
	public static final String coolingHeadTemp = "coolingHeadTemp";
	public static final String oilTemp = "oilTemp";
	public static final String intakeAirTemp = "intakeAirTemp";
	public static final String gearShiftAdvice = "gearShiftAdvice";
	public static final String acceleration = "acceleration";
	public static final String accPedalPosition = "accPedalPosition";
	public static final String clutchPedalPosition = "clutchPedalPosition";
	public static final String reverseGearStatus = "reverseGearStatus";
	public static final String accTorque = "accTorque";
	public static final String ambientLightStatus = "ambientLightStatus";
	public static final String ambientLightSensorStatus = "ambientLightSensorStatus";	
	public static final String dataType = "dataType";
	public static final String identifier = "identifier";
	public static final String statusByte = "statusByte";
	public static final String didResult = "didResult";
	public static final String ecuName = "ecuName";
	public static final String didLocation = "didLocation";
	public static final String value = "value";
	public static final String softButtonName = "softButtonName";
	public static final String imageSupported = "imageSupported";
	public static final String systemAction = "systemAction";
	public static final String image = "image";
	public static final String secondaryText = "secondaryText";
	public static final String tertiaryText = "tertiaryText";
	public static final String secondaryImage = "secondaryImage";	
	public static final String imageType = "imageType";
	public static final String fileData = "fileData";
	public static final String scrollableMessageBody = "scrollableMessageBody";
	public static final String softButtons = "softButtons";
	public static final String customButtonID = "customButtonID";
	public static final String vrHelp = "vrHelp";
	public static final String interactionLayout = "interactionLayout";	
	public static final String customButtonName = "customButtonName";
	public static final String navigationText = "navigationText";
	public static final String turnIcon = "turnIcon";
	public static final String nextTurnIcon = "nextTurnIcon";
	public static final String navigationText1 = "navigationText1";
	public static final String navigationText2 = "navigationText2";
	public static final String eta = "eta";
	public static final String totalDistance = "totalDistance";
	public static final String distanceToManeuver = "distanceToManeuver";
	public static final String distanceToManeuverScale = "distanceToManeuverScale";
	public static final String maneuverComplete = "maneuverComplete";
	public static final String turnList = "turnList";
	public static final String steeringWheelAngle = "steeringWheelAngle";	
	public static final String menuTitle = "menuTitle";
	public static final String menuIcon = "menuIcon";
	public static final String keyboardProperties = "keyboardProperties";
    public static final String driverBeltDeployed = "driverBeltDeployed";
    public static final String passengerBeltDeployed = "passengerBeltDeployed";
    public static final String passengerBuckleBelted = "passengerBuckleBelted";
    public static final String driverBuckleBelted = "driverBuckleBelted";
    public static final String leftRow2BuckleBelted = "leftRow2BuckleBelted";
    public static final String passengerChildDetected = "passengerChildDetected";
    public static final String rightRow2BuckleBelted = "rightRow2BuckleBelted";
    public static final String middleRow2BuckleBelted = "middleRow2BuckleBelted";
    public static final String middleRow3BuckleBelted = "middleRow3BuckleBelted";
    public static final String leftRow3BuckleBelted = "leftRow3BuckleBelted";
    public static final String rightRow3BuckleBelted = "rightRow3BuckleBelted";
    public static final String rearInflatableBelted = "rearInflatableBelted";
    public static final String leftRearInflatableBelted = "leftRearInflatableBelted";
    public static final String rightRearInflatableBelted = "rightRearInflatableBelted";
    public static final String middleRow1BeltDeployed = "middleRow1BeltDeployed";
    public static final String middleRow1BuckleBelted = "middleRow1BuckleBelted";

    public static final String graphicSupported = "graphicSupported";
    public static final String screenParams = "screenParams";
    public static final String muteAudio = "muteAudio";
    public static final String parkBrakeActive = "parkBrakeActive";
    public static final String ignitionStableStatus = "ignitionStableStatus";
    public static final String ignitionStatus = "ignitionStatus";
    public static final String driverDoorAjar = "driverDoorAjar";    
    public static final String passengerDoorAjar = "passengerDoorAjar";
    public static final String rearLeftDoorAjar = "rearLeftDoorAjar";
    public static final String rearRightDoorAjar = "rearRightDoorAjar";
    public static final String systemFile = "systemFile";
        
    public static final String voiceRecOn = "voiceRecOn";
    public static final String btIconOn = "btIconOn";
    public static final String callActive = "callActive";
    public static final String phoneRoaming = "phoneRoaming";
    public static final String textMsgAvailable = "textMsgAvailable";
    public static final String battLevelStatus = "battLevelStatus";
    public static final String stereoAudioOutputMuted = "stereoAudioOutputMuted";
    public static final String monoAudioOutputMuted = "monoAudioOutputMuted";
    public static final String signalLevelStatus = "signalLevelStatus";
    public static final String primaryAudioSource = "primaryAudioSource";
    public static final String eCallEventActive = "eCallEventActive";

    public static final String fuelEconomySinceLastReset = "fuelEconomySinceLastReset";
    public static final String currentTripFuelEconomy = "currentTripFuelEconomy";
    public static final String averageTripFuelEconomy = "averageTripFuelEconomy";
    public static final String currentCycleFuelEconomy = "currentCycleFuelEconomy";

    public static final String lightSwitchStatus = "lightSwitchStatus";
    public static final String highBeamsOn = "highBeamsOn";
    public static final String lowBeamsOn = "lowBeamsOn";

    public static final String electricFuelConsumption = "electricFuelConsumption";
    public static final String stateOfCharge = "stateOfCharge";
    public static final String fuelMaintenanceMode = "fuelMaintenanceMode";
    public static final String distanceToEmpty = "distanceToEmpty";
    
    public static final String dtcMask = "dtcMask";
    public static final String targetID = "targetID";
    public static final String messageLength = "messageLength";
    public static final String messageData = "messageData";
    public static final String messageDataResult = "messageDataResult";
        
    public static final String imageTypeSupported = "imageTypeSupported";
    public static final String imageResolution = "imageResolution";
    public static final String x = "x";
    public static final String y = "y";
    public static final String id = "id";
    public static final String ts = "ts";
    public static final String c = "c";
    public static final String resolution = "resolution";
    public static final String touchEventAvailable = "touchEventAvailable";    
    
    public static final String pressAvailable = "pressAvailable";
    public static final String multiTouchAvailable = "multiTouchAvailable";
    public static final String doublePressAvailable = "doublePressAvailable";
    public static final String templatesAvailable = "templatesAvailable";
    public static final String numCustomPresetsAvailable = "numCustomPresetsAvailable";
    public static final String prerecordedSpeech = "prerecordedSpeech";
    public static final String manualTextEntry = "manualTextEntry";
    public static final String progressIndicator = "progressIndicator";
    public static final String secondaryGraphic = "secondaryGraphic";
    public static final String offset = "offset";
    public static final String length = "length";    
    
    public static final String hardware = "hardware";
    public static final String firmwareRev = "firmwareRev";
    public static final String os = "os";
    public static final String osVersion = "osVersion";
    public static final String carrier = "carrier";
    public static final String maxNumberRFCOMMPorts = "maxNumberRFCOMMPorts";
    
    public static final String onReadDIDResponse = "onReadDIDResponse";    
    public static final String onGetDTCsResponse = "onGetDTCsResponse";
    public static final String onOnKeyboardInput = "onOnKeyboardInput";
    public static final String onOnTouchEvent = "onOnTouchEvent";
    public static final String onOnSystemRequest = "onOnSystemRequest";
    
    public static final String onDiagnosticMessageResponse = "onDiagnosticMessageResponse";
    public static final String onSystemRequestResponse = "onSystemRequestResponse";
    public static final String onGetVehicleDataResponse = "onGetVehicleDataResponse";
    public static final String getSupportedDiagModes = "getSupportedDiagModes";
    public static final String supportedDiagModes = "supportedDiagModes";
    
    public static final String driverAirbagDeployed = "driverAirbagDeployed";
    public static final String driverSideAirbagDeployed = "driverSideAirbagDeployed";
    public static final String driverCurtainAirbagDeployed = "driverCurtainAirbagDeployed";
    public static final String passengerAirbagDeployed = "passengerAirbagDeployed";
    public static final String passengerCurtainAirbagDeployed = "passengerCurtainAirbagDeployed";
    public static final String driverKneeAirbagDeployed = "driverKneeAirbagDeployed";
    public static final String passengerSideAirbagDeployed = "passengerSideAirbagDeployed";
    public static final String passengerKneeAirbagDeployed = "passengerKneeAirbagDeployed";

    public static final String powerModeActive = "powerModeActive";
    public static final String powerModeQualificationStatus = "powerModeQualificationStatus";
    public static final String carModeStatus = "carModeStatus";
    public static final String powerModeStatus = "powerModeStatus";
    
    public static final String eCallNotificationStatus = "eCallNotificationStatus";
    public static final String auxECallNotificationStatus = "auxECallNotificationStatus";
    public static final String eCallConfirmationStatus = "eCallConfirmationStatus";
    public static final String e911Override = "e911Override";
    
    public static final String emergencyEventType = "emergencyEventType";
    public static final String fuelCutoffStatus = "fuelCutoffStatus";
    public static final String rolloverEvent = "rolloverEvent";
    public static final String maximumChangeVelocity = "maximumChangeVelocity";
    public static final String multipleEvents = "multipleEvents";

	public static final String eCallInfo = "eCallInfo";
	public static final String airbagStatus = "airbagStatus";
	public static final String emergencyEvent = "emergencyEvent";
	public static final String clusterModeStatus = "clusterModeStatus";
	public static final String myKey = "myKey";
	public static final String timeToDestination = "timeToDestination";
	
	public static final String driverDistraction = "driverDistraction";
	public static final String showLockScreen = "showLockScreen";
	public static final String userSelected = "userSelected";
	public static final String notSet = "notSet";
	
	public static final String headers = "headers";
	public static final String body = "body";
	
	public static final String ContentType = "ContentType";
	public static final String ConnectTimeout = "ConnectTimeout";
	public static final String DoOutput = "DoOutput";
	public static final String DoInput  = "DoInput";
	public static final String UseCaches = "UseCaches";
	public static final String RequestMethod = "RequestMethod";
	public static final String ReadTimeout = "ReadTimeout";
	public static final String InstanceFollowRedirects = "InstanceFollowRedirects";
	public static final String charset = "charset";
	public static final String ContentLength = "Content-Length";
}
