package com.smartdevicelink.test;

import android.graphics.Color;
import android.util.Log;

import com.smartdevicelink.R;
import com.smartdevicelink.SdlConnection.SdlSession2;
import com.smartdevicelink.managers.lockscreen.LockScreenConfig;
import com.smartdevicelink.protocol.SdlProtocol;
import com.smartdevicelink.protocol.enums.FunctionID;
import com.smartdevicelink.proxy.SdlProxyBase;
import com.smartdevicelink.proxy.TTSChunkFactory;
import com.smartdevicelink.proxy.rpc.AppServiceCapability;
import com.smartdevicelink.proxy.rpc.AppServiceData;
import com.smartdevicelink.proxy.rpc.AppServiceManifest;
import com.smartdevicelink.proxy.rpc.AppServiceRecord;
import com.smartdevicelink.proxy.rpc.AppServicesCapabilities;
import com.smartdevicelink.proxy.rpc.AudioControlCapabilities;
import com.smartdevicelink.proxy.rpc.AudioControlData;
import com.smartdevicelink.proxy.rpc.AudioPassThruCapabilities;
import com.smartdevicelink.proxy.rpc.ButtonCapabilities;
import com.smartdevicelink.proxy.rpc.Choice;
import com.smartdevicelink.proxy.rpc.ClimateControlCapabilities;
import com.smartdevicelink.proxy.rpc.ClimateControlData;
import com.smartdevicelink.proxy.rpc.CloudAppProperties;
import com.smartdevicelink.proxy.rpc.Coordinate;
import com.smartdevicelink.proxy.rpc.DIDResult;
import com.smartdevicelink.proxy.rpc.DateTime;
import com.smartdevicelink.proxy.rpc.DeviceInfo;
import com.smartdevicelink.proxy.rpc.DisplayCapabilities;
import com.smartdevicelink.proxy.rpc.EqualizerSettings;
import com.smartdevicelink.proxy.rpc.HMICapabilities;
import com.smartdevicelink.proxy.rpc.HMIPermissions;
import com.smartdevicelink.proxy.rpc.HMISettingsControlCapabilities;
import com.smartdevicelink.proxy.rpc.HMISettingsControlData;
import com.smartdevicelink.proxy.rpc.HapticRect;
import com.smartdevicelink.proxy.rpc.Image;
import com.smartdevicelink.proxy.rpc.ImageField;
import com.smartdevicelink.proxy.rpc.ImageResolution;
import com.smartdevicelink.proxy.rpc.KeyboardProperties;
import com.smartdevicelink.proxy.rpc.LightCapabilities;
import com.smartdevicelink.proxy.rpc.LightControlCapabilities;
import com.smartdevicelink.proxy.rpc.LightControlData;
import com.smartdevicelink.proxy.rpc.LightState;
import com.smartdevicelink.proxy.rpc.LocationDetails;
import com.smartdevicelink.proxy.rpc.MassageCushionFirmness;
import com.smartdevicelink.proxy.rpc.MassageModeData;
import com.smartdevicelink.proxy.rpc.MediaServiceData;
import com.smartdevicelink.proxy.rpc.MediaServiceManifest;
import com.smartdevicelink.proxy.rpc.MenuParams;
import com.smartdevicelink.proxy.rpc.MetadataTags;
import com.smartdevicelink.proxy.rpc.ModuleData;
import com.smartdevicelink.proxy.rpc.NavigationCapability;
import com.smartdevicelink.proxy.rpc.NavigationInstruction;
import com.smartdevicelink.proxy.rpc.NavigationServiceData;
import com.smartdevicelink.proxy.rpc.NavigationServiceManifest;
import com.smartdevicelink.proxy.rpc.OasisAddress;
import com.smartdevicelink.proxy.rpc.ParameterPermissions;
import com.smartdevicelink.proxy.rpc.PermissionItem;
import com.smartdevicelink.proxy.rpc.PhoneCapability;
import com.smartdevicelink.proxy.rpc.PresetBankCapabilities;
import com.smartdevicelink.proxy.rpc.RGBColor;
import com.smartdevicelink.proxy.rpc.RadioControlCapabilities;
import com.smartdevicelink.proxy.rpc.RadioControlData;
import com.smartdevicelink.proxy.rpc.RdsData;
import com.smartdevicelink.proxy.rpc.Rectangle;
import com.smartdevicelink.proxy.rpc.RemoteControlCapabilities;
import com.smartdevicelink.proxy.rpc.ScreenParams;
import com.smartdevicelink.proxy.rpc.SdlMsgVersion;
import com.smartdevicelink.proxy.rpc.SeatControlCapabilities;
import com.smartdevicelink.proxy.rpc.SeatControlData;
import com.smartdevicelink.proxy.rpc.SeatMemoryAction;
import com.smartdevicelink.proxy.rpc.SingleTireStatus;
import com.smartdevicelink.proxy.rpc.SisData;
import com.smartdevicelink.proxy.rpc.SoftButton;
import com.smartdevicelink.proxy.rpc.SoftButtonCapabilities;
import com.smartdevicelink.proxy.rpc.StartTime;
import com.smartdevicelink.proxy.rpc.StationIDNumber;
import com.smartdevicelink.proxy.rpc.SystemCapability;
import com.smartdevicelink.proxy.rpc.TTSChunk;
import com.smartdevicelink.proxy.rpc.Temperature;
import com.smartdevicelink.proxy.rpc.TemplateColorScheme;
import com.smartdevicelink.proxy.rpc.TextField;
import com.smartdevicelink.proxy.rpc.TouchCoord;
import com.smartdevicelink.proxy.rpc.TouchEvent;
import com.smartdevicelink.proxy.rpc.TouchEventCapabilities;
import com.smartdevicelink.proxy.rpc.Turn;
import com.smartdevicelink.proxy.rpc.VehicleDataResult;
import com.smartdevicelink.proxy.rpc.VehicleType;
import com.smartdevicelink.proxy.rpc.VideoStreamingCapability;
import com.smartdevicelink.proxy.rpc.VideoStreamingFormat;
import com.smartdevicelink.proxy.rpc.VrHelpItem;
import com.smartdevicelink.proxy.rpc.WeatherAlert;
import com.smartdevicelink.proxy.rpc.WeatherData;
import com.smartdevicelink.proxy.rpc.WeatherServiceData;
import com.smartdevicelink.proxy.rpc.WeatherServiceManifest;
import com.smartdevicelink.proxy.rpc.enums.AmbientLightStatus;
import com.smartdevicelink.proxy.rpc.enums.AppHMIType;
import com.smartdevicelink.proxy.rpc.enums.AppInterfaceUnregisteredReason;
import com.smartdevicelink.proxy.rpc.enums.AppServiceType;
import com.smartdevicelink.proxy.rpc.enums.AudioStreamingIndicator;
import com.smartdevicelink.proxy.rpc.enums.AudioStreamingState;
import com.smartdevicelink.proxy.rpc.enums.AudioType;
import com.smartdevicelink.proxy.rpc.enums.BitsPerSample;
import com.smartdevicelink.proxy.rpc.enums.ButtonEventMode;
import com.smartdevicelink.proxy.rpc.enums.ButtonName;
import com.smartdevicelink.proxy.rpc.enums.ButtonPressMode;
import com.smartdevicelink.proxy.rpc.enums.CarModeStatus;
import com.smartdevicelink.proxy.rpc.enums.CharacterSet;
import com.smartdevicelink.proxy.rpc.enums.CompassDirection;
import com.smartdevicelink.proxy.rpc.enums.ComponentVolumeStatus;
import com.smartdevicelink.proxy.rpc.enums.DefrostZone;
import com.smartdevicelink.proxy.rpc.enums.DeviceLevelStatus;
import com.smartdevicelink.proxy.rpc.enums.Dimension;
import com.smartdevicelink.proxy.rpc.enums.Direction;
import com.smartdevicelink.proxy.rpc.enums.DisplayMode;
import com.smartdevicelink.proxy.rpc.enums.DisplayType;
import com.smartdevicelink.proxy.rpc.enums.DistanceUnit;
import com.smartdevicelink.proxy.rpc.enums.DriverDistractionState;
import com.smartdevicelink.proxy.rpc.enums.ECallConfirmationStatus;
import com.smartdevicelink.proxy.rpc.enums.EmergencyEventType;
import com.smartdevicelink.proxy.rpc.enums.FileType;
import com.smartdevicelink.proxy.rpc.enums.FuelCutoffStatus;
import com.smartdevicelink.proxy.rpc.enums.FuelType;
import com.smartdevicelink.proxy.rpc.enums.GlobalProperty;
import com.smartdevicelink.proxy.rpc.enums.HMILevel;
import com.smartdevicelink.proxy.rpc.enums.HmiZoneCapabilities;
import com.smartdevicelink.proxy.rpc.enums.HybridAppPreference;
import com.smartdevicelink.proxy.rpc.enums.IgnitionStableStatus;
import com.smartdevicelink.proxy.rpc.enums.IgnitionStatus;
import com.smartdevicelink.proxy.rpc.enums.ImageFieldName;
import com.smartdevicelink.proxy.rpc.enums.ImageType;
import com.smartdevicelink.proxy.rpc.enums.InteractionMode;
import com.smartdevicelink.proxy.rpc.enums.KeyboardEvent;
import com.smartdevicelink.proxy.rpc.enums.KeyboardLayout;
import com.smartdevicelink.proxy.rpc.enums.KeypressMode;
import com.smartdevicelink.proxy.rpc.enums.Language;
import com.smartdevicelink.proxy.rpc.enums.LayoutMode;
import com.smartdevicelink.proxy.rpc.enums.LightName;
import com.smartdevicelink.proxy.rpc.enums.LightStatus;
import com.smartdevicelink.proxy.rpc.enums.LockScreenStatus;
import com.smartdevicelink.proxy.rpc.enums.MassageCushion;
import com.smartdevicelink.proxy.rpc.enums.MassageMode;
import com.smartdevicelink.proxy.rpc.enums.MassageZone;
import com.smartdevicelink.proxy.rpc.enums.MediaClockFormat;
import com.smartdevicelink.proxy.rpc.enums.MediaType;
import com.smartdevicelink.proxy.rpc.enums.MetadataType;
import com.smartdevicelink.proxy.rpc.enums.ModuleType;
import com.smartdevicelink.proxy.rpc.enums.NavigationAction;
import com.smartdevicelink.proxy.rpc.enums.NavigationJunction;
import com.smartdevicelink.proxy.rpc.enums.PowerModeQualificationStatus;
import com.smartdevicelink.proxy.rpc.enums.PowerModeStatus;
import com.smartdevicelink.proxy.rpc.enums.PrerecordedSpeech;
import com.smartdevicelink.proxy.rpc.enums.PrimaryAudioSource;
import com.smartdevicelink.proxy.rpc.enums.RadioBand;
import com.smartdevicelink.proxy.rpc.enums.RadioState;
import com.smartdevicelink.proxy.rpc.enums.RequestType;
import com.smartdevicelink.proxy.rpc.enums.Result;
import com.smartdevicelink.proxy.rpc.enums.SamplingRate;
import com.smartdevicelink.proxy.rpc.enums.SeatMemoryActionType;
import com.smartdevicelink.proxy.rpc.enums.ServiceUpdateReason;
import com.smartdevicelink.proxy.rpc.enums.SoftButtonType;
import com.smartdevicelink.proxy.rpc.enums.SpeechCapabilities;
import com.smartdevicelink.proxy.rpc.enums.SupportedSeat;
import com.smartdevicelink.proxy.rpc.enums.SystemAction;
import com.smartdevicelink.proxy.rpc.enums.SystemCapabilityType;
import com.smartdevicelink.proxy.rpc.enums.SystemContext;
import com.smartdevicelink.proxy.rpc.enums.TBTState;
import com.smartdevicelink.proxy.rpc.enums.TPMS;
import com.smartdevicelink.proxy.rpc.enums.TemperatureUnit;
import com.smartdevicelink.proxy.rpc.enums.TextAlignment;
import com.smartdevicelink.proxy.rpc.enums.TextFieldName;
import com.smartdevicelink.proxy.rpc.enums.TouchType;
import com.smartdevicelink.proxy.rpc.enums.TriggerSource;
import com.smartdevicelink.proxy.rpc.enums.UpdateMode;
import com.smartdevicelink.proxy.rpc.enums.VehicleDataEventStatus;
import com.smartdevicelink.proxy.rpc.enums.VehicleDataNotificationStatus;
import com.smartdevicelink.proxy.rpc.enums.VehicleDataResultCode;
import com.smartdevicelink.proxy.rpc.enums.VehicleDataStatus;
import com.smartdevicelink.proxy.rpc.enums.VehicleDataType;
import com.smartdevicelink.proxy.rpc.enums.VentilationMode;
import com.smartdevicelink.proxy.rpc.enums.VideoStreamingCodec;
import com.smartdevicelink.proxy.rpc.enums.VideoStreamingProtocol;
import com.smartdevicelink.proxy.rpc.enums.VideoStreamingState;
import com.smartdevicelink.proxy.rpc.enums.VrCapabilities;
import com.smartdevicelink.proxy.rpc.enums.WarningLightStatus;
import com.smartdevicelink.proxy.rpc.enums.WayPointType;
import com.smartdevicelink.util.Version;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Test {

	//Versions
	public static final Version MAX_RPC_VERSION_SUPPORTED		= SdlProxyBase.MAX_SUPPORTED_RPC_VERSION;
	/**
	 * @see SdlProtocol
	 */
	public static final Version MAX_PROTOCOL_VERSION_SUPPORTED 	= new Version(5, 2, 0);


	// Test Failure Messages
	public static final String NULL      = "Value should be null.";
	public static final String MATCH     = "Values should match.";
	public static final String ARRAY     = "Array values should match.";
	public static final String TRUE      = "Value should be true.";
	public static final String FALSE     = "Value should be false.";
	public static final String NOT_NULL  = "Value should not be null.";
	public static final String JSON_FAIL = "Json testing failed.";

	// RPC Request/Response/Notification/Datatype Test Values
	public static final int                            GENERAL_INT                            = 100;
	public static final Integer                        GENERAL_INTEGER                        = 100;
	public static final Long                           GENERAL_LONG                           = 100L;
	public static final Turn                           GENERAL_TURN                           = new Turn();
	public static final Float                          GENERAL_FLOAT                          = 100f;
	public static final Image                          GENERAL_IMAGE                          = new Image();
	public static final Choice                         GENERAL_CHOICE                         = new Choice();
	public static final String                         GENERAL_STRING                         = "test";
	public static final Double                         GENERAL_DOUBLE                         = 10.01;
	public static final boolean                        GENERAL_BOOLEAN                        = true;
	public static final byte[]                         GENERAL_BYTE_ARRAY                     = new byte[0];
	public static final TPMS                           GENERAL_TPMS                           = TPMS.UNKNOWN;
	public static final TBTState                       GENERAL_TBTSTATE                       = TBTState.NEXT_TURN_REQUEST;
	public static final FileType                       GENERAL_FILETYPE                       = FileType.BINARY;
	public static final Language                       GENERAL_LANGUAGE                       = Language.EN_US;
	public static final HMILevel                       GENERAL_HMILEVEL                       = HMILevel.HMI_FULL;
	public static final DIDResult                      GENERAL_DIDRESULT                      = new DIDResult();
	public static final TextField                      GENERAL_TEXTFIELD                      = new TextField();
	public static final OasisAddress                   GENERAL_OASISADDRESS                   = new OasisAddress();
	public static final Coordinate                     GENERAL_COORDINATE                     = new Coordinate();
	public static final LocationDetails                GENERAL_LOCATIONDETAILS                = new LocationDetails();
	public static final Dimension                      GENERAL_DIMENSION                      = Dimension._2D;
	public static final ImageType                      GENERAL_IMAGETYPE                      = ImageType.DYNAMIC;
	public static final AudioType                      GENERAL_AUDIOTYPE                      = AudioType.PCM;
	public static final StartTime                      GENERAL_STARTTIME                      = new StartTime();
	public static final TouchType                      GENERAL_TOUCHTYPE                      = TouchType.BEGIN;
	public static final TouchEvent                     GENERAL_TOUCHEVENT                     = new TouchEvent();
	public static final VrHelpItem                     GENERAL_VRHELPITEM                     = new VrHelpItem();
	public static final ImageField                     GENERAL_IMAGEFIELD                     = new ImageField();
	public static final DeviceInfo	                   GENERAL_DEVICEINFO	                  = new DeviceInfo();
	public static final LayoutMode                     GENERAL_LAYOUTMODE                     = LayoutMode.LIST_ONLY;
	public static final MenuParams                     GENERAL_MENUPARAMS                     = new MenuParams();
	public static final SoftButton                     GENERAL_SOFTBUTTON                     = new SoftButton();
	public static final ButtonName                     GENERAL_BUTTONNAME                     = ButtonName.OK;
	public static final UpdateMode                     GENERAL_UPDATEMODE                     = UpdateMode.RESUME;
	public static final TouchCoord                     GENERAL_TOUCHCOORD                     = new TouchCoord();
	public static final MassageModeData                GENERAL_MASSAGEMODEDATA                = new MassageModeData();
	public static final MassageCushionFirmness         GENERAL_MASSAGECUSHIONFIRMNESS         = new MassageCushionFirmness();
	public static final DisplayType                    GENERAL_DISPLAYTYPE                    = DisplayType.CID;
	public static final VehicleType                    GENERAL_VEHICLETYPE                    = new VehicleType();
	public static final RequestType                    GENERAL_REQUESTTYPE                    = RequestType.AUTH_REQUEST;
	public static final SystemAction                   GENERAL_SYSTEMACTION                   = SystemAction.DEFAULT_ACTION;
	public static final CharacterSet                   GENERAL_CHARACTERSET                   = CharacterSet.CID1SET;
	public static final SamplingRate                   GENERAL_SAMPLINGRATE                	  = SamplingRate._8KHZ;
	public static final ScreenParams                   GENERAL_SCREENPARAMS		        	  = new ScreenParams();
	public static final KeypressMode                   GENERAL_KEYPRESSMODE                   = KeypressMode.QUEUE_KEYPRESSES;
	public static final SystemContext                  GENERAL_SYSTEMCONTEXT                  = SystemContext.SYSCTXT_MAIN;
	public static final KeyboardEvent                  GENERAL_KEYBOARDEVENT                  = KeyboardEvent.ENTRY_SUBMITTED;
	public static final CarModeStatus                  GENERAL_CARMODESTATUS                  = CarModeStatus.NORMAL;
	public static final TextFieldName                  GENERAL_TEXTFIELDNAME                  = TextFieldName.ETA;
	public static final TriggerSource                  GENERAL_TRIGGERSOURCE              	  = TriggerSource.TS_VR;
	public static final BitsPerSample                  GENERAL_BITSPERSAMPLE                  = BitsPerSample._8_BIT;
	public static final TextAlignment                  GENERAL_TEXTALIGNMENT                  = TextAlignment.CENTERED;
	public static final SdlMsgVersion                  GENERAL_SDLMSGVERSION                  = new SdlMsgVersion();
	public static final PermissionItem                 GENERAL_PERMISSIONITEM                 = new PermissionItem();
	public static final SoftButtonType                 GENERAL_SOFTBUTTONTYPE                 = SoftButtonType.SBT_BOTH;
	public static final MassageZone                    GENERAL_MASSAGEZONE                    = MassageZone.LUMBAR;
	public static final MassageMode                    GENERAL_MASSAGEMODE                    = MassageMode.HIGH;
	public static final MassageCushion                 GENERAL_MASSAGECUSHION                 = MassageCushion.BACK_BOLSTERS;
	public static final SeatMemoryActionType           GENERAL_SEATMEMORYACTIONTYPE           = SeatMemoryActionType.SAVE;
	public static final SupportedSeat           	   GENERAL_SUPPORTEDSEAT                  = SupportedSeat.DRIVER;
	public static final KeyboardLayout                 GENERAL_KEYBOARDLAYOUT                 = KeyboardLayout.QWERTY;
	public static final ImageFieldName                 GENERAL_IMAGEFIELDNAME                 = ImageFieldName.graphic;
	public static final HMIPermissions                 GENERAL_HMIPERMISSIONS                 = new HMIPermissions();
	public static final IgnitionStatus                 GENERAL_IGNITIONSTATUS                 = IgnitionStatus.RUN;
	public static final ButtonEventMode                GENERAL_BUTTONEVENTMODE                = ButtonEventMode.BUTTONUP;
    public static final ButtonPressMode                GENERAL_BUTTONPRESSMODE                = ButtonPressMode.SHORT;
	public static final PowerModeStatus                GENERAL_POWERMODESTATUS                = PowerModeStatus.RUNNING_2;
	public static final VehicleDataType                GENERAL_VEHICLEDATATYPE                = VehicleDataType.VEHICLEDATA_BRAKING;
	public static final InteractionMode                GENERAL_INTERACTIONMODE                = InteractionMode.BOTH;
	public static final ImageResolution                GENERAL_IMAGERESOLUTION                = new ImageResolution();
	public static final FuelCutoffStatus               GENERAL_FUELCUTOFFSTATUS               = FuelCutoffStatus.NORMAL_OPERATION;
	public static final CompassDirection               GENERAL_COMPASSDIRECTION               = CompassDirection.EAST;
	public static final LockScreenStatus               GENERAL_LOCKSCREENSTATUS               = LockScreenStatus.REQUIRED;
	public static final VehicleDataStatus              GENERAL_VEHICLEDATASTATUS              = VehicleDataStatus.ON;
	public static final DeviceLevelStatus              GENERAL_DEVICELEVELSTATUS              = DeviceLevelStatus.FOUR_LEVEL_BARS;
	public static final ButtonCapabilities             GENERAL_BUTTONCAPABILITIES             = new ButtonCapabilities();
	public static final EmergencyEventType             GENERAL_EMERGENCYEVENTTYPE             = EmergencyEventType.FAULT;
	public static final AmbientLightStatus             GENERAL_AMBIENTLIGHTSTATUS             = AmbientLightStatus.NIGHT;
	public static final SpeechCapabilities             GENERAL_SPEECHCAPABILITIES             = SpeechCapabilities.TEXT;
	public static final WarningLightStatus        	   GENERAL_WARNINGLIGHTSTATUS        	  = WarningLightStatus.OFF;
	public static final KeyboardProperties             GENERAL_KEYBOARDPROPERTIES             = new KeyboardProperties();
	public static final PrimaryAudioSource             GENERAL_PRIMARYAUDIOSOURCE             = PrimaryAudioSource.BLUETOOTH_STEREO_BTST;
	public static final AudioStreamingState            GENERAL_AUDIOSTREAMINGSTATE            = AudioStreamingState.AUDIBLE;
	public static final VideoStreamingState            GENERAL_VIDEOSTREAMINGSTATE            = VideoStreamingState.STREAMABLE;
	public static final DisplayCapabilities            GENERAL_DISPLAYCAPABILITIES            = new DisplayCapabilities();
	public static final ParameterPermissions           GENERAL_PARAMETERPERMISSIONS           = new ParameterPermissions();
	public static final IgnitionStableStatus           GENERAL_IGNITIONSTABLESTATUS           = IgnitionStableStatus.IGNITION_SWITCH_STABLE;
	public static final VehicleDataResultCode          GENERAL_VEHICLEDATARESULTCODE          = VehicleDataResultCode.IGNORED;
	public static final ComponentVolumeStatus          GENERAL_COMPONENTVOLUMESTATUS          = ComponentVolumeStatus.LOW;
	public static final PresetBankCapabilities         GENERAL_PRESETBANKCAPABILITIES         = new PresetBankCapabilities();
	public static final VehicleDataEventStatus         GENERAL_VEHCILEDATAEVENTSTATUS         = VehicleDataEventStatus.YES;
	public static final VehicleDataEventStatus         GENERAL_VEHICLEDATAEVENTSTATUS         = VehicleDataEventStatus.YES;
	public static final TouchEventCapabilities         GENERAL_TOUCHEVENTCAPABILITIES         = new TouchEventCapabilities();
	public static final SeatMemoryAction               GENERAL_SEATMEMORYACTION               = new SeatMemoryAction();
	public static final SoftButtonCapabilities         GENERAL_SOFTBUTTONCAPABILITIES         = new SoftButtonCapabilities();
	public static final ECallConfirmationStatus        GENERAL_ECALLCONFIRMATIONSTATUS        = ECallConfirmationStatus.CALL_IN_PROGRESS;
	public static final AudioPassThruCapabilities      GENERAL_AUDIOPASSTHRUCAPABILITIES      = new AudioPassThruCapabilities();
	public static final PowerModeQualificationStatus   GENERAL_POWERMODEQUALIFICATIONSTATUS   = PowerModeQualificationStatus.POWER_MODE_OK;
	public static final VehicleDataNotificationStatus  GENERAL_VEHICLEDATANOTIFICATIONSTATUS  = VehicleDataNotificationStatus.NORMAL;
	public static final AppInterfaceUnregisteredReason GENERAL_APPINTERFACEUNREGISTEREDREASON = AppInterfaceUnregisteredReason.BLUETOOTH_OFF;
	public static final SystemCapabilityType           GENERAL_SYSTEMCAPABILITYTYPE           = SystemCapabilityType.NAVIGATION;
	public static final NavigationCapability           GENERAL_NAVIGATIONCAPABILITY           = new NavigationCapability();
	public static final PhoneCapability                GENERAL_PHONECAPABILITY                = new PhoneCapability();
	public static final RemoteControlCapabilities      GENERAL_REMOTECONTROLCAPABILITIES      = new RemoteControlCapabilities();
	public static final SystemCapability               GENERAL_SYSTEMCAPABILITY               = new SystemCapability();
	public static final VideoStreamingProtocol         GENERAL_VIDEOSTREAMINGPROTOCOL         = VideoStreamingProtocol.RAW;
	public static final VideoStreamingCodec            GENERAL_VIDEOSTREAMINGCODEC            = VideoStreamingCodec.H264;
	public static final VideoStreamingCapability       GENERAL_VIDEOSTREAMINGCAPABILITY       = new VideoStreamingCapability();
	public static final VideoStreamingFormat           GENERAL_VIDEOSTREAMINGFORMAT           = new VideoStreamingFormat();
	public static final RGBColor                       GENERAL_RGBCOLOR                       = new RGBColor();
	public static final TemplateColorScheme            GENERAL_DAYCOLORSCHEME                 = new TemplateColorScheme();
	public static final TemplateColorScheme            GENERAL_NIGHTCOLORSCHEME               = new TemplateColorScheme();
	public static final Result                         GENERAL_RESULT                         = Result.SUCCESS;
	public static final WayPointType                   GENERAL_WAYPOINTTYPE                   = WayPointType.DESTINATION;
	public static final SingleTireStatus               GENERAL_SINGLETIRESTATUS               = new SingleTireStatus();
	public static final DriverDistractionState         GENERAL_DRIVERDISTRACTIONSTATE         = DriverDistractionState.DD_ON;
	public static final List<LocationDetails>          GENERAL_LOCATIONDETAILS_LIST           = Arrays.asList(new LocationDetails[] { Test.GENERAL_LOCATIONDETAILS, Test.GENERAL_LOCATIONDETAILS});
	public static final AudioStreamingIndicator        GENERAL_AUDIO_STREAMING_INDICATOR      = AudioStreamingIndicator.PLAY;
	public static final String                         GENERAL_APP_ID                         = "123e4567e8";
	public static final String                         GENERAL_FULL_APP_ID                    = "123e4567-e89b-12d3-a456-426655440000";
	public static final HybridAppPreference 		   GENERAL_HYBRID_APP_PREFERENCE          = HybridAppPreference.CLOUD;
	public static final CloudAppProperties             GENERAL_CLOUDAPPPROPERTIES             = new CloudAppProperties();
	public static final AppServiceType                 GENERAL_APP_SERVICE_TYPE               = AppServiceType.MEDIA;
	public static final List<Integer>                  GENERAL_FUNCTION_ID_LIST               = Arrays.asList(FunctionID.GET_VEHICLE_DATA.getId(), FunctionID.SEND_HAPTIC_DATA.getId());
	public static final AppServiceManifest             GENERAL_APPSERVICEMANIFEST             = new AppServiceManifest(AppServiceType.MEDIA.name());
	public static final MediaServiceManifest           GENERAL_MEDIA_SERVICE_MANIFEST         = new MediaServiceManifest();
	public static final WeatherServiceManifest         GENERAL_WEATHER_SERVICE_MANIFEST       = new WeatherServiceManifest();
	public static final NavigationServiceManifest      GENERAL_NAVIGATION_SERVICE_MANIFEST    = new NavigationServiceManifest();
	public static final AppServiceRecord               GENERAL_APPSERVICERECORD               = new AppServiceRecord();
	public static final AppServiceCapability           GENERAL_APP_SERVICE_CAPABILITY         = new AppServiceCapability();
	public static final AppServicesCapabilities        GENERAL_APP_SERVICE_CAPABILITIES       = new AppServicesCapabilities();
	public static final ServiceUpdateReason            GENERAL_SERVICE_UPDATE_REASON          = ServiceUpdateReason.MANIFEST_UPDATE;
	public static final DateTime					   GENERAL_DATETIME                       = new DateTime();
	public static final WeatherData 				   GENERAL_WEATHERDATA                    = new WeatherData();
	public static final WeatherAlert                   GENERAL_WEATHERALERT                   = new WeatherAlert();
	public static final MediaType                      GENERAL_MEDIATYPE                      = MediaType.MUSIC;
	public static final MediaServiceData               GENERAL_MEDIASERVICEDATA               = new MediaServiceData();
	public static final WeatherServiceData             GENERAL_WEATHERSERVICEDATA             = new WeatherServiceData();
	public static final NavigationServiceData          GENERAL_NAVIGATIONSERVICEDATA          = new NavigationServiceData();
	public static final AppServiceData                 GENERAL_APPSERVICEDATA                 = new AppServiceData();
	public static final NavigationAction               GENERAL_NAVIGATIONACTION               = NavigationAction.STAY;
	public static final NavigationJunction             GENERAL_NAVIGATION_JUNCTION            = NavigationJunction.BIFURCATION;
	public static final Direction                      GENERAL_DIRECTION                      = Direction.RIGHT;
	public static final NavigationInstruction          GENERAL_NAVIGATION_INSTRUCTION         = new NavigationInstruction();
	public static final Version                        GENERAL_VERSION                        = new Version("4.0.0");
	public static final ModuleType 					   GENERAL_MODULETYPE           		  = ModuleType.CLIMATE;
	public static final Temperature 				   GENERAL_TEMPERATURE                	  = new Temperature();
	public static final TemperatureUnit 			   GENERAL_TEMPERATUREUNIT                = TemperatureUnit.CELSIUS;
	public static final DefrostZone 				   GENERAL_DEFROSTZONE               	  = DefrostZone.ALL;
	public static final VentilationMode 			   GENERAL_VENTILATIONMODE                = VentilationMode.BOTH;
	public static final LightName                      GENERAL_LIGHTNAME            		  = LightName.AMBIENT_LIGHTS;
	public static final DisplayMode                    GENERAL_DISPLAYMODE            		  = DisplayMode.AUTO;
	public static final DistanceUnit                   GENERAL_DISTANCEUNIT           		  = DistanceUnit.KILOMETERS;
	public static final LightStatus                    GENERAL_LIGHTSTATUS           		  = LightStatus.OFF;
	public static final RadioBand 				       GENERAL_RADIOBAND               	      = RadioBand.AM;
	public static final ClimateControlData             GENERAL_CLIMATECONTROLDATA             = new ClimateControlData();
	public static final SeatControlData                GENERAL_SEATCONTROLDATA                = new SeatControlData();
	public static final RdsData                        GENERAL_RDSDATA                        = new RdsData();
	public static final StationIDNumber                GENERAL_STATIONIDNUMBER                = new StationIDNumber();
	public static final SisData                        GENERAL_SISDATA                        = new SisData();
	public static final RadioState                     GENERAL_RADIOSTATE              	      = RadioState.ACQUIRED;
	public static final RadioControlData               GENERAL_RADIOCONTROLDATA               = new RadioControlData();
	public static final ModuleData 					   GENERAL_MODULEDATA                     = new ModuleData();
	public static final ClimateControlCapabilities     GENERAL_CLIMATECONTROLCAPABILITIES     = new ClimateControlCapabilities();
	public static final RadioControlCapabilities       GENERAL_RADIOCONTROLCAPABILITIES       = new RadioControlCapabilities();
	public static final SeatControlCapabilities        GENERAL_SEATCONTROLCAPABILITIES        = new SeatControlCapabilities();
	public static final EqualizerSettings              GENERAL_EQUALIZERSETTINGS              = new EqualizerSettings();
	public static final LightCapabilities              GENERAL_LIGHTCAPABILITIES              = new LightCapabilities();
	public static final LightState                     GENERAL_LIGHTSTATE                     = new LightState();
	public static final AudioControlCapabilities       GENERAL_AUDIOCONTROLCAPABILITIES       = new AudioControlCapabilities();
	public static final HMISettingsControlCapabilities GENERAL_HMISETTINGSCONTROLCAPABILITIES = new HMISettingsControlCapabilities();
	public static final LightControlCapabilities       GENERAL_LIGHTCONTROLCAPABILITIES       = new LightControlCapabilities();
	public static final AudioControlData               GENERAL_AUDIOCONTROLDATA               = new AudioControlData();
	public static final LightControlData               GENERAL_LIGHTCONTROLDATA               = new LightControlData();
	public static final HMISettingsControlData         GENERAL_HMISETTINGSCONTROLDATA         = new HMISettingsControlData();

	public static final HMICapabilities                GENERAL_HMICAPABILITIES                = new HMICapabilities();

	public static final MetadataTags                   GENERAL_METADATASTRUCT                 = new MetadataTags();
	public static final Rectangle                      GENERAL_RECTANGLE                      = new Rectangle();
	public static final HapticRect                     GENERAL_HAPTIC_RECT                    = new HapticRect();
	public static final FuelType                       GENERAL_FUELTYPE                       = FuelType.GASOLINE;
	public static final LockScreenConfig               GENERAL_LOCKSCREENCONFIG               = new LockScreenConfig();

	public static final List<Long>                      GENERAL_LONG_LIST                      = Arrays.asList(new Long[]{ 1L, 2L });
	public static final List<Turn>                      GENERAL_TURN_LIST                      = new ArrayList<Turn>();
	public static final List<Choice>                    GENERAL_CHOICE_LIST                    = new ArrayList<Choice>();
	public static final List<String>                    GENERAL_STRING_LIST                    = Arrays.asList(new String[] { "a", "b"});
	public static final List<Integer>                   GENERAL_INTEGER_LIST                   = Arrays.asList(new Integer[]{ -1, -2});
	public static final List<TTSChunk>                  GENERAL_TTSCHUNK_LIST                  = new ArrayList<TTSChunk>(2);
	public static final List<HMILevel>                  GENERAL_HMILEVEL_LIST                  = Arrays.asList(new HMILevel[]{HMILevel.HMI_FULL, HMILevel.HMI_BACKGROUND});
	public static final List<FileType>                  GENERAL_FILETYPE_LIST                  = new ArrayList<FileType>(1);
	public static final List<TextField>                 GENERAL_TEXTFIELD_LIST                 = new ArrayList<TextField>(1);
	public static final List<DIDResult>                 GENERAL_DIDRESULT_LIST                 = new ArrayList<DIDResult>(1);
	public static final List<TouchCoord>                GENERAL_TOUCHCOORD_LIST                = new ArrayList<TouchCoord>(1);
	public static final List<MassageModeData>           GENERAL_MASSAGEMODEDATA_LIST           = new ArrayList<MassageModeData>(1);
	public static final List<MassageCushionFirmness>    GENERAL_MASSAGECUSHIONFIRMNESS_LIST    = new ArrayList<MassageCushionFirmness>(1);
	public static final List<AppHMIType>                GENERAL_APPHMITYPE_LIST                = new ArrayList<AppHMIType>(2);
	public static final List<VrHelpItem>                GENERAL_VRHELPITEM_LIST                = new ArrayList<VrHelpItem>(2);
	public static final List<SoftButton>                GENERAL_SOFTBUTTON_LIST                = new ArrayList<SoftButton>(1);
	public static final List<ImageField> 			    GENERAL_IMAGEFIELD_LIST				   = new ArrayList<ImageField>(1);
	public static final List<TouchEvent>			    GENERAL_TOUCHEVENT_LIST                = new ArrayList<TouchEvent>(1);
	public static final List<PermissionItem>            GENERAL_PERMISSIONITEM_LIST            = new ArrayList<PermissionItem>(1);
	public static final List<GlobalProperty>            GENERAL_GLOBALPROPERTY_LIST            = new ArrayList<GlobalProperty>(2);
	public static final List<VrCapabilities>		    GENERAL_VRCAPABILITIES_LIST            = new ArrayList<VrCapabilities>(1);
	public static final List<MediaClockFormat>          GENERAL_MEDIACLOCKFORMAT_LIST		   = new ArrayList<MediaClockFormat>(2);
	public static final List<VehicleDataResult>         GENERAL_VEHICLEDATARESULT_LIST         = new ArrayList<VehicleDataResult>(VehicleDataType.values().length);
	public static final List<PrerecordedSpeech>         GENERAL_PRERECORDEDSPEECH_LIST		   = new ArrayList<PrerecordedSpeech>(2);
	public static final List<SpeechCapabilities>        GENERAL_SPEECHCAPABILITIES_LIST        = new ArrayList<SpeechCapabilities>(2);
	public static final List<ButtonCapabilities>        GENERAL_BUTTONCAPABILITIES_LIST        = new ArrayList<ButtonCapabilities>(2);
	public static final List<HmiZoneCapabilities>       GENERAL_HMIZONECAPABILITIES_LIST       = new ArrayList<HmiZoneCapabilities>(2);
	public static final List<SoftButtonCapabilities>    GENERAL_SOFTBUTTONCAPABILITIES_LIST    = new ArrayList<SoftButtonCapabilities>(1);
	public static final List<AudioPassThruCapabilities> GENERAL_AUDIOPASSTHRUCAPABILITIES_LIST = new ArrayList<AudioPassThruCapabilities>(1);
	public static final List<VideoStreamingFormat>      GENERAL_VIDEOSTREAMINGFORMAT_LIST      = new ArrayList<VideoStreamingFormat>(2);
	public static final List<DefrostZone>               GENERAL_DEFROSTZONE_LIST               = Arrays.asList(new DefrostZone[]{DefrostZone.FRONT, DefrostZone.REAR});
	public static final List<VentilationMode>           GENERAL_VENTILATIONMODE_LIST           = Arrays.asList(new VentilationMode[]{VentilationMode.LOWER, VentilationMode.UPPER});
	public static final List<ClimateControlCapabilities> GENERAL_CLIMATECONTROLCAPABILITIES_LIST = new ArrayList<ClimateControlCapabilities>(1);
	public static final List<RadioControlCapabilities>   GENERAL_RADIOCONTROLCAPABILITIES_LIST   = new ArrayList<RadioControlCapabilities>(1);
	public static final Vector<String>                   GENERAL_VECTOR_STRING                   = new Vector<>(Arrays.asList(new String[] { "a", "b"}));
	public static final Vector<TTSChunk>                 GENERAL_VECTOR_TTS_CHUNKS               = new Vector<>(Arrays.asList(TTSChunkFactory.createChunk(SpeechCapabilities.TEXT, "Welcome to the jungle")));
	public static final List<SeatControlCapabilities>   GENERAL_SEATCONTROLCAPABILITIES_LIST   = new ArrayList<SeatControlCapabilities>(1);
	public static final List<EqualizerSettings>         GENERAL_EQUALIZERSETTINGS_LIST         = new ArrayList<EqualizerSettings>(1);
	public static final List<LightCapabilities>         GENERAL_LIGHTCAPABILITIES_LIST         = new ArrayList<LightCapabilities>(1);
	public static final List<LightState>                GENERAL_LIGHTSTATE_LIST                = new ArrayList<LightState>(1);
	public static final List<AudioControlCapabilities>  GENERAL_AUDIOCONTROLCAPABILITIES_LIST  = new ArrayList<AudioControlCapabilities>(1);
	public static final List<ModuleData>                GENERAL_MODULEDATA_LIST  = Collections.singletonList(GENERAL_MODULEDATA);
	public static final List<AppServiceType>            GENERAL_APPSERVICETYPE_LIST            = Arrays.asList(AppServiceType.MEDIA, AppServiceType.NAVIGATION);
	public static final List<AppServiceCapability>      GENERAL_APPSERVICECAPABILITY_LIST      = Arrays.asList(GENERAL_APP_SERVICE_CAPABILITY);
	public static final List<WeatherData>               GENERAL_WEATHERDATA_LIST               = Arrays.asList(GENERAL_WEATHERDATA);
	public static final List<WeatherAlert>              GENERAL_WEATHERALERT_LIST              = Arrays.asList(GENERAL_WEATHERALERT);
	public static final List<NavigationInstruction>     GENERAL_NAVIGATION_INSTRUCTION_LIST    = Arrays.asList(GENERAL_NAVIGATION_INSTRUCTION);


	public static final JSONArray  JSON_TURNS                     = new JSONArray();
	public static final JSONArray  JSON_CHOICES                   = new JSONArray();
	public static final JSONArray  JSON_HMILEVELS                 = new JSONArray();
	public static final JSONArray  JSON_TTSCHUNKS                 = new JSONArray();
	public static final JSONArray  JSON_DIDRESULTS                = new JSONArray();
	public static final JSONArray  JSON_TEXTFIELDS                = new JSONArray();
	public static final JSONArray  JSON_TOUCHCOORDS               = new JSONArray();
	public static final JSONArray  JSON_VRHELPITEMS               = new JSONArray();
	public static final JSONArray  JSON_SOFTBUTTONS               = new JSONArray();
	public static final JSONArray  JSON_IMAGEFIELDS				  = new JSONArray();
	public static final JSONArray  JSON_TOUCHEVENTS               = new JSONArray();
	public static final JSONArray  JSON_PERMISSIONITEMS           = new JSONArray();
	public static final JSONArray  JSON_BUTTONCAPABILITIES        = new JSONArray();
	public static final JSONArray  JSON_SOFTBUTTONCAPABILITIES    = new JSONArray();
	public static final JSONArray  JSON_AUDIOPASSTHRUCAPABILITIES = new JSONArray();
	public static final JSONArray JSON_RADIOCONTROLCAPABILITIES   = new JSONArray();
	public static final JSONArray JSON_CLIMATECONTROLCAPABILITIES = new JSONArray();
	public static final JSONArray  JSON_TEXTFIELDTYPES            = new JSONArray();
	public static final JSONObject JSON_TURN                      = new JSONObject();
	public static final JSONObject JSON_IMAGE                     = new JSONObject();
	public static final JSONObject JSON_CHOICE                    = new JSONObject();
	public static final JSONObject JSON_DIDRESULT                 = new JSONObject();
	public static final JSONObject JSON_STARTTIME                 = new JSONObject();
	public static final JSONObject JSON_TEXTFIELD                 = new JSONObject();
	public static final JSONObject JSON_TOUCHCOORD                = new JSONObject();
	public static final JSONObject JSON_TOUCHEVENT                = new JSONObject();
	public static final JSONObject JSON_IMAGEFIELD                = new JSONObject();
	public static final JSONObject JSON_SOFTBUTTON                = new JSONObject();
	public static final JSONObject JSON_MENUPARAMS                = new JSONObject();
	public static final JSONObject JSON_DEVICEINFO                = new JSONObject();
	public static final JSONObject JSON_VRHELPITEM                = new JSONObject();
	public static final JSONObject JSON_SCREENPARAMS              = new JSONObject();
	public static final JSONObject JSON_SDLMSGVERSION             = new JSONObject();
	public static final JSONObject JSON_PERMISSIONITEM            = new JSONObject();
	public static final JSONObject JSON_HMIPERMISSIONS            = new JSONObject();
	public static final JSONObject JSON_IMAGERESOLUTION           = new JSONObject();
	public static final JSONObject JSON_KEYBOARDPROPERTIES        = new JSONObject();
	public static final JSONObject JSON_DISPLAYCAPABILITIES       = new JSONObject();
	public static final JSONObject JSON_PARAMETERPERMISSIONS      = new JSONObject();
	public static final JSONObject JSON_PRESETBANKCAPABILITIES    = new JSONObject();
	public static final JSONObject JSON_TOUCHEVENTCAPABILITIES    = new JSONObject();
	public static final JSONObject JSON_PCMSTREAMCAPABILITIES     = new JSONObject();
	public static final JSONObject JSON_RGBCOLOR                  = new JSONObject();
	public static final JSONObject JSON_DAYCOLORSCHEME            = new JSONObject();
	public static final JSONObject JSON_NIGHTCOLORSCHEME          = new JSONObject();

	static {
		GENERAL_TOUCHEVENTCAPABILITIES.setDoublePressAvailable(GENERAL_BOOLEAN);
		GENERAL_TOUCHEVENTCAPABILITIES.setMultiTouchAvailable(GENERAL_BOOLEAN);
		GENERAL_TOUCHEVENTCAPABILITIES.setPressAvailable(GENERAL_BOOLEAN);

		GENERAL_SEATMEMORYACTION.setAction(GENERAL_SEATMEMORYACTIONTYPE);
		GENERAL_SEATMEMORYACTION.setLabel(GENERAL_STRING);
		GENERAL_SEATMEMORYACTION.setId(GENERAL_INT);

		GENERAL_IMAGERESOLUTION.setResolutionHeight(GENERAL_INT);
		GENERAL_IMAGERESOLUTION.setResolutionWidth(GENERAL_INT);

		GENERAL_TEMPERATURE.setUnit(GENERAL_TEMPERATUREUNIT);
		GENERAL_TEMPERATURE.setValue(GENERAL_FLOAT);

		GENERAL_CLIMATECONTROLDATA.setFanSpeed(GENERAL_INT);
		GENERAL_CLIMATECONTROLDATA.setCurrentTemperature(GENERAL_TEMPERATURE);
		GENERAL_CLIMATECONTROLDATA.setDesiredTemperature(GENERAL_TEMPERATURE);
		GENERAL_CLIMATECONTROLDATA.setAcEnable(GENERAL_BOOLEAN);
		GENERAL_CLIMATECONTROLDATA.setCirculateAirEnable(GENERAL_BOOLEAN);
		GENERAL_CLIMATECONTROLDATA.setAutoModeEnable(GENERAL_BOOLEAN);
		GENERAL_CLIMATECONTROLDATA.setDualModeEnable(GENERAL_BOOLEAN);
		GENERAL_CLIMATECONTROLDATA.setAcMaxEnable(GENERAL_BOOLEAN);
		GENERAL_CLIMATECONTROLDATA.setDefrostZone(GENERAL_DEFROSTZONE);
		GENERAL_CLIMATECONTROLDATA.setVentilationMode(GENERAL_VENTILATIONMODE);

		GENERAL_SEATCONTROLDATA.setMemory(GENERAL_SEATMEMORYACTION);
		GENERAL_SEATCONTROLDATA.setMassageCushionFirmness(GENERAL_MASSAGECUSHIONFIRMNESS_LIST);
		GENERAL_SEATCONTROLDATA.setMassageMode(GENERAL_MASSAGEMODEDATA_LIST);
		GENERAL_SEATCONTROLDATA.setMassageEnabled(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLDATA.setHeadSupportHorizontalPosition(GENERAL_INT);
		GENERAL_SEATCONTROLDATA.setHeadSupportVerticalPosition(GENERAL_INT);
		GENERAL_SEATCONTROLDATA.setBackTiltAngle(GENERAL_INT);
		GENERAL_SEATCONTROLDATA.setBackVerticalPosition(GENERAL_INT);
		GENERAL_SEATCONTROLDATA.setFrontVerticalPosition(GENERAL_INT);
		GENERAL_SEATCONTROLDATA.setVerticalPosition(GENERAL_INT);
		GENERAL_SEATCONTROLDATA.setHorizontalPosition(GENERAL_INT);
		GENERAL_SEATCONTROLDATA.setCoolingLevel(GENERAL_INT);
		GENERAL_SEATCONTROLDATA.setHeatingLevel(GENERAL_INT);
		GENERAL_SEATCONTROLDATA.setHeatingEnabled(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLDATA.setCoolingEnabled(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLDATA.setId(GENERAL_SUPPORTEDSEAT);

		GENERAL_AUDIOCONTROLDATA.setSource(GENERAL_PRIMARYAUDIOSOURCE);
		GENERAL_AUDIOCONTROLDATA.setVolume(GENERAL_INT);
		GENERAL_AUDIOCONTROLDATA.setKeepContext(GENERAL_BOOLEAN);
		GENERAL_AUDIOCONTROLDATA.setEqualizerSettings(GENERAL_EQUALIZERSETTINGS_LIST);

		GENERAL_HMISETTINGSCONTROLDATA.setDistanceUnit(GENERAL_DISTANCEUNIT);
		GENERAL_HMISETTINGSCONTROLDATA.setTemperatureUnit(GENERAL_TEMPERATUREUNIT);
		GENERAL_HMISETTINGSCONTROLDATA.setDisplayMode(GENERAL_DISPLAYMODE);

		GENERAL_LIGHTCONTROLDATA.setLightState(GENERAL_LIGHTSTATE_LIST);

		GENERAL_STATIONIDNUMBER.setCountryCode(GENERAL_INT);
		GENERAL_STATIONIDNUMBER.setFccFacilityId(GENERAL_INT);

		GENERAL_SISDATA.setStationMessage(GENERAL_STRING);
		GENERAL_SISDATA.setStationLocation(VehicleDataHelper.GPS);

		GENERAL_SISDATA.setStationLongName(GENERAL_STRING);
		GENERAL_SISDATA.setStationIDNumber(GENERAL_STATIONIDNUMBER);
		GENERAL_SISDATA.setStationShortName(GENERAL_STRING);

		GENERAL_RDSDATA.setProgramService(GENERAL_STRING);
		GENERAL_RDSDATA.setRadioText(GENERAL_STRING);
		GENERAL_RDSDATA.setClockText(GENERAL_STRING);
		GENERAL_RDSDATA.setProgramIdentification(GENERAL_STRING);
		GENERAL_RDSDATA.setRegion(GENERAL_STRING);
		GENERAL_RDSDATA.setTrafficProgram(GENERAL_BOOLEAN);
		GENERAL_RDSDATA.setTrafficAnnouncement(GENERAL_BOOLEAN);
		GENERAL_RDSDATA.setProgramType(GENERAL_INT);

		GENERAL_RADIOCONTROLDATA.setFrequencyInteger(GENERAL_INT);
		GENERAL_RADIOCONTROLDATA.setFrequencyFraction(GENERAL_INT);
		GENERAL_RADIOCONTROLDATA.setBand(GENERAL_RADIOBAND);
		GENERAL_RADIOCONTROLDATA.setRdsData(GENERAL_RDSDATA);
		GENERAL_RADIOCONTROLDATA.setAvailableHDs(GENERAL_INT);
		GENERAL_RADIOCONTROLDATA.setHdChannel(GENERAL_INT);
		GENERAL_RADIOCONTROLDATA.setSignalStrength(GENERAL_INT);
		GENERAL_RADIOCONTROLDATA.setSignalChangeThreshold(GENERAL_INT);
		GENERAL_RADIOCONTROLDATA.setRadioEnable(GENERAL_BOOLEAN);
		GENERAL_RADIOCONTROLDATA.setState(GENERAL_RADIOSTATE);

		GENERAL_MODULEDATA.setModuleType(GENERAL_MODULETYPE);
		GENERAL_MODULEDATA.setClimateControlData(GENERAL_CLIMATECONTROLDATA);

		GENERAL_CHOICE.setMenuName(GENERAL_STRING);
		GENERAL_CHOICE.setSecondaryText(GENERAL_STRING);
		GENERAL_CHOICE.setTertiaryText(GENERAL_STRING);
		GENERAL_CHOICE.setChoiceID(GENERAL_INT);
		GENERAL_CHOICE.setImage(GENERAL_IMAGE);
		GENERAL_CHOICE.setSecondaryImage(GENERAL_IMAGE);
		GENERAL_CHOICE.setVrCommands(GENERAL_STRING_LIST);

		GENERAL_MASSAGEMODEDATA.setMassageMode(GENERAL_MASSAGEMODE);
		GENERAL_MASSAGEMODEDATA.setMassageZone(GENERAL_MASSAGEZONE);
		GENERAL_MASSAGEMODEDATA_LIST.add(GENERAL_MASSAGEMODEDATA);

		GENERAL_MASSAGECUSHIONFIRMNESS.setCushion(GENERAL_MASSAGECUSHION);
		GENERAL_MASSAGECUSHIONFIRMNESS.setFirmness(GENERAL_INT);
		GENERAL_MASSAGECUSHIONFIRMNESS_LIST.add(GENERAL_MASSAGECUSHIONFIRMNESS);

		GENERAL_TOUCHCOORD.setX(GENERAL_INT);
		GENERAL_TOUCHCOORD.setY(GENERAL_INT);
		GENERAL_TOUCHCOORD_LIST.add(GENERAL_TOUCHCOORD);

		GENERAL_TOUCHEVENT.setId(GENERAL_INT);
		GENERAL_TOUCHEVENT.setTs(GENERAL_LONG_LIST);
		GENERAL_TOUCHEVENT.setC(GENERAL_TOUCHCOORD_LIST);
		GENERAL_TOUCHEVENT_LIST.add(GENERAL_TOUCHEVENT);

		GENERAL_TEXTFIELD.setName(GENERAL_TEXTFIELDNAME);
		GENERAL_TEXTFIELD.setRows(GENERAL_INT);
		GENERAL_TEXTFIELD.setWidth(GENERAL_INT);
		GENERAL_TEXTFIELD.setCharacterSet(GENERAL_CHARACTERSET);
		GENERAL_TEXTFIELD_LIST.add(GENERAL_TEXTFIELD);

		GENERAL_COORDINATE.setLongitudeDegrees(GENERAL_FLOAT);
		GENERAL_COORDINATE.setLatitudeDegrees(GENERAL_FLOAT);

		GENERAL_OASISADDRESS.setCountryName(GENERAL_STRING);
		GENERAL_OASISADDRESS.setThoroughfare(GENERAL_STRING);
		GENERAL_OASISADDRESS.setSubThoroughfare(GENERAL_STRING);
		GENERAL_OASISADDRESS.setCountryCode(GENERAL_STRING);
		GENERAL_OASISADDRESS.setPostalCode(GENERAL_STRING);
		GENERAL_OASISADDRESS.setLocality(GENERAL_STRING);
		GENERAL_OASISADDRESS.setSubLocality(GENERAL_STRING);
		GENERAL_OASISADDRESS.setAdministrativeArea(GENERAL_STRING);
		GENERAL_OASISADDRESS.setSubAdministrativeArea(GENERAL_STRING);

		GENERAL_LOCATIONDETAILS.setAddressLines(GENERAL_STRING_LIST);
		GENERAL_LOCATIONDETAILS.setCoordinate(GENERAL_COORDINATE);
		GENERAL_LOCATIONDETAILS.setLocationDescription(GENERAL_STRING);
		GENERAL_LOCATIONDETAILS.setLocationImage(GENERAL_IMAGE);
		GENERAL_LOCATIONDETAILS.setLocationName(GENERAL_STRING);
		GENERAL_LOCATIONDETAILS.setSearchAddress(GENERAL_OASISADDRESS);
		GENERAL_LOCATIONDETAILS.setPhoneNumber(GENERAL_STRING);

		GENERAL_FILETYPE_LIST.add(GENERAL_FILETYPE);

		GENERAL_IMAGEFIELD.setImageResolution(GENERAL_IMAGERESOLUTION);
		GENERAL_IMAGEFIELD.setName(GENERAL_IMAGEFIELDNAME);
		GENERAL_IMAGEFIELD.setImageTypeSupported(GENERAL_FILETYPE_LIST);
		GENERAL_IMAGEFIELD_LIST.add(GENERAL_IMAGEFIELD);

		GENERAL_SCREENPARAMS.setImageResolution(GENERAL_IMAGERESOLUTION);
		GENERAL_SCREENPARAMS.setTouchEventAvailable(GENERAL_TOUCHEVENTCAPABILITIES);

		GENERAL_MEDIACLOCKFORMAT_LIST.add(MediaClockFormat.CLOCK1);
		GENERAL_MEDIACLOCKFORMAT_LIST.add(MediaClockFormat.CLOCK2);

		GENERAL_IMAGE.setValue(GENERAL_STRING);
		GENERAL_IMAGE.setImageType(GENERAL_IMAGETYPE);
		GENERAL_IMAGE.setIsTemplate(GENERAL_BOOLEAN);

		GENERAL_SOFTBUTTON.setIsHighlighted(GENERAL_BOOLEAN);
		GENERAL_SOFTBUTTON.setSoftButtonID(GENERAL_INT);
		GENERAL_SOFTBUTTON.setSystemAction(SystemAction.STEAL_FOCUS);
		GENERAL_SOFTBUTTON.setText(GENERAL_STRING);
		GENERAL_SOFTBUTTON.setType(SoftButtonType.SBT_TEXT);
		GENERAL_SOFTBUTTON.setImage(GENERAL_IMAGE);
		GENERAL_SOFTBUTTON_LIST.add(GENERAL_SOFTBUTTON);

		GENERAL_TURN.setNavigationText(GENERAL_STRING);
		GENERAL_TURN.setTurnIcon(GENERAL_IMAGE);
		GENERAL_TURN_LIST.add(GENERAL_TURN);

		GENERAL_MENUPARAMS.setMenuName(GENERAL_STRING);
		GENERAL_MENUPARAMS.setParentID(GENERAL_INT);
		GENERAL_MENUPARAMS.setPosition(GENERAL_INT);

		GENERAL_VRHELPITEM.setText(GENERAL_STRING);
		GENERAL_VRHELPITEM.setImage(GENERAL_IMAGE);
		GENERAL_VRHELPITEM.setPosition(100);
    	GENERAL_VRHELPITEM_LIST.add(GENERAL_VRHELPITEM);

    	GENERAL_TTSCHUNK_LIST.add(TTSChunkFactory.createChunk(SpeechCapabilities.TEXT, "Welcome to the jungle"));
    	GENERAL_TTSCHUNK_LIST.add(TTSChunkFactory.createChunk(SpeechCapabilities.TEXT, "Say a command"));

    	GENERAL_KEYBOARDPROPERTIES.setAutoCompleteText(GENERAL_STRING);
    	GENERAL_KEYBOARDPROPERTIES.setKeypressMode(KeypressMode.SINGLE_KEYPRESS);
    	GENERAL_KEYBOARDPROPERTIES.setKeyboardLayout(KeyboardLayout.QWERTY);
    	GENERAL_KEYBOARDPROPERTIES.setLanguage(Language.EN_US);
    	GENERAL_KEYBOARDPROPERTIES.setLimitedCharacterList(Test.GENERAL_STRING_LIST);

    	GENERAL_STARTTIME.setHours(GENERAL_INT);
		GENERAL_STARTTIME.setMinutes(GENERAL_INT);
		GENERAL_STARTTIME.setSeconds(GENERAL_INT);

        GENERAL_CHOICE_LIST.add(GENERAL_CHOICE);

        GENERAL_DEVICEINFO.setCarrier(GENERAL_STRING);
        GENERAL_DEVICEINFO.setFirmwareRev(GENERAL_STRING);
        GENERAL_DEVICEINFO.setHardware(GENERAL_STRING);
        GENERAL_DEVICEINFO.setMaxNumberRFCOMMPorts(GENERAL_INT);
        GENERAL_DEVICEINFO.setOs(GENERAL_STRING);
        GENERAL_DEVICEINFO.setOsVersion(GENERAL_STRING);

        GENERAL_SDLMSGVERSION.setMajorVersion(GENERAL_INT);
        GENERAL_SDLMSGVERSION.setMinorVersion(GENERAL_INT);

        GENERAL_APPHMITYPE_LIST.add(AppHMIType.BACKGROUND_PROCESS);
        GENERAL_APPHMITYPE_LIST.add(AppHMIType.COMMUNICATION);

        GENERAL_GLOBALPROPERTY_LIST.add(GlobalProperty.HELPPROMPT);
        GENERAL_GLOBALPROPERTY_LIST.add(GlobalProperty.MENUICON);

        for (VehicleDataType data : VehicleDataType.values()) {
        	VehicleDataResult result = new VehicleDataResult();
            result.setResultCode(VehicleDataResultCode.SUCCESS);
            result.setDataType(data);
        	GENERAL_VEHICLEDATARESULT_LIST.add(result);
        }

        GENERAL_DIDRESULT.setData(GENERAL_STRING);
        GENERAL_DIDRESULT.setDidLocation(GENERAL_INT);
        GENERAL_DIDRESULT.setResultCode(VehicleDataResultCode.SUCCESS);
        GENERAL_DIDRESULT_LIST.add(GENERAL_DIDRESULT);

        GENERAL_DISPLAYCAPABILITIES.setDisplayType(GENERAL_DISPLAYTYPE);
        GENERAL_DISPLAYCAPABILITIES.setDisplayName(GENERAL_STRING);
        GENERAL_DISPLAYCAPABILITIES.setGraphicSupported(GENERAL_BOOLEAN);
        GENERAL_DISPLAYCAPABILITIES.setImageFields(GENERAL_IMAGEFIELD_LIST);
        GENERAL_DISPLAYCAPABILITIES.setMediaClockFormats(GENERAL_MEDIACLOCKFORMAT_LIST);
        GENERAL_DISPLAYCAPABILITIES.setNumCustomPresetsAvailable(GENERAL_INT);
        GENERAL_DISPLAYCAPABILITIES.setScreenParams(GENERAL_SCREENPARAMS);
        GENERAL_DISPLAYCAPABILITIES.setTemplatesAvailable(GENERAL_STRING_LIST);
        GENERAL_DISPLAYCAPABILITIES.setTextFields(GENERAL_TEXTFIELD_LIST);

        GENERAL_PRESETBANKCAPABILITIES.setOnScreenPresetsAvailable(GENERAL_BOOLEAN);

        GENERAL_BUTTONCAPABILITIES.setLongPressAvailable(false);
        GENERAL_BUTTONCAPABILITIES.setShortPressAvailable(true);
        GENERAL_BUTTONCAPABILITIES.setUpDownAvailable(true);
        GENERAL_BUTTONCAPABILITIES.setName(ButtonName.SEEKRIGHT);
        GENERAL_BUTTONCAPABILITIES_LIST.add(GENERAL_BUTTONCAPABILITIES);

        GENERAL_SOFTBUTTONCAPABILITIES.setLongPressAvailable(GENERAL_BOOLEAN);
        GENERAL_SOFTBUTTONCAPABILITIES.setShortPressAvailable(GENERAL_BOOLEAN);
        GENERAL_SOFTBUTTONCAPABILITIES.setUpDownAvailable(GENERAL_BOOLEAN);
        GENERAL_SOFTBUTTONCAPABILITIES.setImageSupported(GENERAL_BOOLEAN);
        GENERAL_SOFTBUTTONCAPABILITIES_LIST.add(GENERAL_SOFTBUTTONCAPABILITIES);

        GENERAL_VEHICLETYPE.setMake(GENERAL_STRING);
        GENERAL_VEHICLETYPE.setModel(GENERAL_STRING);
        GENERAL_VEHICLETYPE.setModelYear(GENERAL_STRING);
        GENERAL_VEHICLETYPE.setTrim(GENERAL_STRING);

        GENERAL_AUDIOPASSTHRUCAPABILITIES.setAudioType(GENERAL_AUDIOTYPE);
        GENERAL_AUDIOPASSTHRUCAPABILITIES.setBitsPerSample(GENERAL_BITSPERSAMPLE);
        GENERAL_AUDIOPASSTHRUCAPABILITIES.setSamplingRate(GENERAL_SAMPLINGRATE);
        GENERAL_AUDIOPASSTHRUCAPABILITIES_LIST.add(GENERAL_AUDIOPASSTHRUCAPABILITIES);

        GENERAL_PRERECORDEDSPEECH_LIST.add(PrerecordedSpeech.HELP_JINGLE);
        GENERAL_PRERECORDEDSPEECH_LIST.add(PrerecordedSpeech.INITIAL_JINGLE);

        GENERAL_HMIZONECAPABILITIES_LIST.add(HmiZoneCapabilities.BACK);
        GENERAL_HMIZONECAPABILITIES_LIST.add(HmiZoneCapabilities.FRONT);

        GENERAL_SPEECHCAPABILITIES_LIST.add(SpeechCapabilities.SILENCE);
        GENERAL_SPEECHCAPABILITIES_LIST.add(SpeechCapabilities.TEXT);

        GENERAL_VRCAPABILITIES_LIST.add(VrCapabilities.TEXT);

        GENERAL_HMIPERMISSIONS.setAllowed(GENERAL_HMILEVEL_LIST);
        GENERAL_HMIPERMISSIONS.setUserDisallowed(GENERAL_HMILEVEL_LIST);

        GENERAL_PARAMETERPERMISSIONS.setAllowed(GENERAL_STRING_LIST);
        GENERAL_PARAMETERPERMISSIONS.setUserDisallowed(GENERAL_STRING_LIST);

        GENERAL_PERMISSIONITEM.setRpcName(GENERAL_STRING);
        GENERAL_PERMISSIONITEM.setHMIPermissions(GENERAL_HMIPERMISSIONS);
        GENERAL_PERMISSIONITEM.setParameterPermissions(GENERAL_PARAMETERPERMISSIONS);
        GENERAL_PERMISSIONITEM_LIST.add(GENERAL_PERMISSIONITEM);

		GENERAL_SYSTEMCAPABILITY.setSystemCapabilityType(GENERAL_SYSTEMCAPABILITYTYPE);

		GENERAL_NAVIGATIONCAPABILITY.setSendLocationEnabled(GENERAL_BOOLEAN);
		GENERAL_NAVIGATIONCAPABILITY.setWayPointsEnabled(GENERAL_BOOLEAN);

		GENERAL_PHONECAPABILITY.setDialNumberEnabled(GENERAL_BOOLEAN);

		GENERAL_VIDEOSTREAMINGFORMAT.setProtocol(GENERAL_VIDEOSTREAMINGPROTOCOL);
		GENERAL_VIDEOSTREAMINGFORMAT.setCodec(GENERAL_VIDEOSTREAMINGCODEC);

		GENERAL_VIDEOSTREAMINGFORMAT_LIST.add(GENERAL_VIDEOSTREAMINGFORMAT);
		GENERAL_VIDEOSTREAMINGFORMAT_LIST.add(GENERAL_VIDEOSTREAMINGFORMAT);

		GENERAL_VIDEOSTREAMINGCAPABILITY.setMaxBitrate(GENERAL_INT);
		GENERAL_VIDEOSTREAMINGCAPABILITY.setPreferredResolution(GENERAL_IMAGERESOLUTION);
		GENERAL_VIDEOSTREAMINGCAPABILITY.setSupportedFormats(GENERAL_VIDEOSTREAMINGFORMAT_LIST);
		GENERAL_VIDEOSTREAMINGCAPABILITY.setIsHapticSpatialDataSupported(GENERAL_BOOLEAN);

		GENERAL_CLIMATECONTROLCAPABILITIES.setModuleName(GENERAL_STRING);
		GENERAL_CLIMATECONTROLCAPABILITIES.setFanSpeedAvailable(GENERAL_BOOLEAN);
		GENERAL_CLIMATECONTROLCAPABILITIES.setDesiredTemperatureAvailable(GENERAL_BOOLEAN);
		GENERAL_CLIMATECONTROLCAPABILITIES.setAcEnableAvailable(GENERAL_BOOLEAN);
		GENERAL_CLIMATECONTROLCAPABILITIES.setAcMaxEnableAvailable(GENERAL_BOOLEAN);
		GENERAL_CLIMATECONTROLCAPABILITIES.setCirculateAirEnableAvailable(GENERAL_BOOLEAN);
		GENERAL_CLIMATECONTROLCAPABILITIES.setAutoModeEnableAvailable(GENERAL_BOOLEAN);
		GENERAL_CLIMATECONTROLCAPABILITIES.setDualModeEnableAvailable(GENERAL_BOOLEAN);
		GENERAL_CLIMATECONTROLCAPABILITIES.setDefrostZoneAvailable(GENERAL_BOOLEAN);
		GENERAL_CLIMATECONTROLCAPABILITIES.setDefrostZone(GENERAL_DEFROSTZONE_LIST);
		GENERAL_CLIMATECONTROLCAPABILITIES.setVentilationModeAvailable(GENERAL_BOOLEAN);
		GENERAL_CLIMATECONTROLCAPABILITIES.setVentilationMode(GENERAL_VENTILATIONMODE_LIST);
		GENERAL_CLIMATECONTROLCAPABILITIES_LIST.add(GENERAL_CLIMATECONTROLCAPABILITIES);

		GENERAL_RADIOCONTROLCAPABILITIES.setModuleName(GENERAL_STRING);
		GENERAL_RADIOCONTROLCAPABILITIES.setRadioEnableAvailable(GENERAL_BOOLEAN);
		GENERAL_RADIOCONTROLCAPABILITIES.setRadioBandAvailable(GENERAL_BOOLEAN);
		GENERAL_RADIOCONTROLCAPABILITIES.setRadioFrequencyAvailable(GENERAL_BOOLEAN);
		GENERAL_RADIOCONTROLCAPABILITIES.setHdChannelAvailable(GENERAL_BOOLEAN);
		GENERAL_RADIOCONTROLCAPABILITIES.setRdsDataAvailable(GENERAL_BOOLEAN);
		GENERAL_RADIOCONTROLCAPABILITIES.setAvailableHDsAvailable(GENERAL_BOOLEAN);
		GENERAL_RADIOCONTROLCAPABILITIES.setStateAvailable(GENERAL_BOOLEAN);
		GENERAL_RADIOCONTROLCAPABILITIES.setSignalStrengthAvailable(GENERAL_BOOLEAN);
		GENERAL_RADIOCONTROLCAPABILITIES.setSignalChangeThresholdAvailable(GENERAL_BOOLEAN);
		GENERAL_RADIOCONTROLCAPABILITIES_LIST.add(GENERAL_RADIOCONTROLCAPABILITIES);

		GENERAL_SEATCONTROLCAPABILITIES.setMemoryAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setMassageCushionFirmnessAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setMassageModeAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setMassageEnabledAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setHeadSupportVerticalPositionAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setHeadSupportHorizontalPositionAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setBackTiltAngleAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setBackVerticalPositionAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setFrontVerticalPositionAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setVerticalPositionAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setHorizontalPositionAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setCoolingLevelAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setHeatingLevelAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setCoolingEnabledAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setHeatingEnabledAvailable(GENERAL_BOOLEAN);
		GENERAL_SEATCONTROLCAPABILITIES.setModuleName(GENERAL_STRING);
		GENERAL_SEATCONTROLCAPABILITIES_LIST.add(GENERAL_SEATCONTROLCAPABILITIES);

		GENERAL_AUDIOCONTROLCAPABILITIES.setEqualizerMaxChannelId(GENERAL_INT);
		GENERAL_AUDIOCONTROLCAPABILITIES.setEqualizerAvailable(GENERAL_BOOLEAN);
		GENERAL_AUDIOCONTROLCAPABILITIES.setVolumeAvailable(GENERAL_BOOLEAN);
		GENERAL_AUDIOCONTROLCAPABILITIES.setSourceAvailable(GENERAL_BOOLEAN);
		GENERAL_AUDIOCONTROLCAPABILITIES.setKeepContextAvailable(GENERAL_BOOLEAN);
		GENERAL_AUDIOCONTROLCAPABILITIES.setModuleName(GENERAL_STRING);
		GENERAL_AUDIOCONTROLCAPABILITIES_LIST.add(GENERAL_AUDIOCONTROLCAPABILITIES);

		GENERAL_HMISETTINGSCONTROLCAPABILITIES.setDisplayModeUnitAvailable(GENERAL_BOOLEAN);
		GENERAL_HMISETTINGSCONTROLCAPABILITIES.setDistanceUnitAvailable(GENERAL_BOOLEAN);
		GENERAL_HMISETTINGSCONTROLCAPABILITIES.setTemperatureUnitAvailable(GENERAL_BOOLEAN);
		GENERAL_HMISETTINGSCONTROLCAPABILITIES.setModuleName(GENERAL_STRING);

		GENERAL_LIGHTCONTROLCAPABILITIES.setSupportedLights(GENERAL_LIGHTCAPABILITIES_LIST);
		GENERAL_LIGHTCONTROLCAPABILITIES.setModuleName(GENERAL_STRING);

		GENERAL_EQUALIZERSETTINGS.setChannelSetting(GENERAL_INT);
		GENERAL_EQUALIZERSETTINGS.setChannelName(GENERAL_STRING);
		GENERAL_EQUALIZERSETTINGS.setChannelId(GENERAL_INT);
		GENERAL_EQUALIZERSETTINGS_LIST.add(GENERAL_EQUALIZERSETTINGS);

		GENERAL_LIGHTCAPABILITIES.setName(GENERAL_LIGHTNAME);
		GENERAL_LIGHTCAPABILITIES.setDensityAvailable(GENERAL_BOOLEAN);
		GENERAL_LIGHTCAPABILITIES.setRGBColorSpaceAvailable(GENERAL_BOOLEAN);
		GENERAL_LIGHTCAPABILITIES_LIST.add(GENERAL_LIGHTCAPABILITIES);

		GENERAL_LIGHTSTATE.setId(GENERAL_LIGHTNAME);
		GENERAL_LIGHTSTATE.setDensity(GENERAL_FLOAT);
		GENERAL_LIGHTSTATE.setStatus(GENERAL_LIGHTSTATUS);
		GENERAL_LIGHTSTATE.setColor(GENERAL_RGBCOLOR);
		GENERAL_LIGHTSTATE_LIST.add(GENERAL_LIGHTSTATE);

		GENERAL_REMOTECONTROLCAPABILITIES.setButtonCapabilities(GENERAL_BUTTONCAPABILITIES_LIST);
		GENERAL_REMOTECONTROLCAPABILITIES.setClimateControlCapabilities(GENERAL_CLIMATECONTROLCAPABILITIES_LIST);
		GENERAL_REMOTECONTROLCAPABILITIES.setRadioControlCapabilities(GENERAL_RADIOCONTROLCAPABILITIES_LIST);

		GENERAL_HMICAPABILITIES.setNavigationAvilable(GENERAL_BOOLEAN);
		GENERAL_HMICAPABILITIES.setVideoStreamingAvailable(GENERAL_BOOLEAN);
		GENERAL_HMICAPABILITIES.setPhoneCallAvilable(GENERAL_BOOLEAN);

		List<MetadataType> exampleList = new ArrayList<>();
		exampleList.add(0, MetadataType.CURRENT_TEMPERATURE);
		exampleList.add(1, MetadataType.MEDIA_ALBUM);
		exampleList.add(2, MetadataType.MEDIA_ARTIST);

		GENERAL_METADATASTRUCT.setMainField1(exampleList);
		GENERAL_METADATASTRUCT.setMainField2(exampleList);
		GENERAL_METADATASTRUCT.setMainField3(exampleList);
		GENERAL_METADATASTRUCT.setMainField4(exampleList);

		GENERAL_RECTANGLE.setX(GENERAL_FLOAT);
		GENERAL_RECTANGLE.setY(GENERAL_FLOAT);
		GENERAL_RECTANGLE.setWidth(GENERAL_FLOAT);
		GENERAL_RECTANGLE.setHeight(GENERAL_FLOAT);

		GENERAL_HAPTIC_RECT.setId(GENERAL_INTEGER);
		GENERAL_HAPTIC_RECT.setRect(GENERAL_RECTANGLE);

		GENERAL_RGBCOLOR.setRed(GENERAL_INTEGER);
		GENERAL_RGBCOLOR.setGreen(GENERAL_INTEGER);
		GENERAL_RGBCOLOR.setBlue(GENERAL_INTEGER);

		GENERAL_NIGHTCOLORSCHEME.setPrimaryColor(GENERAL_RGBCOLOR);
		GENERAL_NIGHTCOLORSCHEME.setSecondaryColor(GENERAL_RGBCOLOR);
		GENERAL_NIGHTCOLORSCHEME.setBackgroundColor(GENERAL_RGBCOLOR);

		GENERAL_DAYCOLORSCHEME.setPrimaryColor(GENERAL_RGBCOLOR);
		GENERAL_DAYCOLORSCHEME.setSecondaryColor(GENERAL_RGBCOLOR);
		GENERAL_DAYCOLORSCHEME.setBackgroundColor(GENERAL_RGBCOLOR);

		GENERAL_LOCKSCREENCONFIG.setAppIcon(R.drawable.sdl_lockscreen_icon);
		GENERAL_LOCKSCREENCONFIG.setBackgroundColor(Color.BLUE);
		GENERAL_LOCKSCREENCONFIG.setEnabled(true);
		GENERAL_LOCKSCREENCONFIG.setCustomView(R.layout.activity_sdllock_screen);
		GENERAL_CLOUDAPPPROPERTIES.setNicknames(GENERAL_STRING_LIST);
		GENERAL_CLOUDAPPPROPERTIES.setAppID(GENERAL_STRING);
		GENERAL_CLOUDAPPPROPERTIES.setEnabled(GENERAL_BOOLEAN);
		GENERAL_CLOUDAPPPROPERTIES.setAuthToken(GENERAL_STRING);
		GENERAL_CLOUDAPPPROPERTIES.setCloudTransportType(GENERAL_STRING);
		GENERAL_CLOUDAPPPROPERTIES.setHybridAppPreference(GENERAL_HYBRID_APP_PREFERENCE);
		GENERAL_CLOUDAPPPROPERTIES.setEndpoint(GENERAL_STRING);
		GENERAL_WEATHER_SERVICE_MANIFEST.setWeatherForLocationSupported(GENERAL_BOOLEAN);
		GENERAL_WEATHER_SERVICE_MANIFEST.setCurrentForecastSupported(GENERAL_BOOLEAN);
		GENERAL_WEATHER_SERVICE_MANIFEST.setMaxMultidayForecastAmount(GENERAL_INTEGER);
		GENERAL_WEATHER_SERVICE_MANIFEST.setMaxMinutelyForecastAmount(GENERAL_INTEGER);
		GENERAL_WEATHER_SERVICE_MANIFEST.setMaxHourlyForecastAmount(GENERAL_INTEGER);

		GENERAL_APPSERVICEMANIFEST.setWeatherServiceManifest(GENERAL_WEATHER_SERVICE_MANIFEST);
		GENERAL_APPSERVICEMANIFEST.setServiceName(GENERAL_STRING);
		GENERAL_APPSERVICEMANIFEST.setServiceIcon(GENERAL_IMAGE);
		GENERAL_APPSERVICEMANIFEST.setRpcSpecVersion(GENERAL_SDLMSGVERSION);
		GENERAL_APPSERVICEMANIFEST.setMediaServiceManifest(GENERAL_MEDIA_SERVICE_MANIFEST);
		GENERAL_APPSERVICEMANIFEST.setHandledRpcs(GENERAL_FUNCTION_ID_LIST);
		GENERAL_APPSERVICEMANIFEST.setAllowAppConsumers(GENERAL_BOOLEAN);
		GENERAL_APPSERVICEMANIFEST.setServiceType(GENERAL_STRING);

		GENERAL_NAVIGATION_SERVICE_MANIFEST.setAcceptsWayPoints(GENERAL_BOOLEAN);

		GENERAL_APPSERVICERECORD.setServiceID(GENERAL_STRING);
		GENERAL_APPSERVICERECORD.setServiceManifest(GENERAL_APPSERVICEMANIFEST);
		GENERAL_APPSERVICERECORD.setServiceActive(GENERAL_BOOLEAN);
		GENERAL_APPSERVICERECORD.setServicePublished(GENERAL_BOOLEAN);

		GENERAL_APP_SERVICE_CAPABILITY.setUpdatedAppServiceRecord(GENERAL_APPSERVICERECORD);
		GENERAL_APP_SERVICE_CAPABILITY.setUpdateReason(GENERAL_SERVICE_UPDATE_REASON);

		GENERAL_APP_SERVICE_CAPABILITIES.setAppServices(GENERAL_APPSERVICECAPABILITY_LIST);

		GENERAL_DATETIME.setDay(Test.GENERAL_INT);
		GENERAL_DATETIME.setHour(Test.GENERAL_INT);
		GENERAL_DATETIME.setMilliSecond(Test.GENERAL_INT);
		GENERAL_DATETIME.setMinute(Test.GENERAL_INT);
		GENERAL_DATETIME.setMonth(Test.GENERAL_INT);
		GENERAL_DATETIME.setSecond(Test.GENERAL_INT);
		GENERAL_DATETIME.setTzHour(Test.GENERAL_INT);
		GENERAL_DATETIME.setTzMinute(Test.GENERAL_INT);
		GENERAL_DATETIME.setYear(Test.GENERAL_INT);

		GENERAL_WEATHERDATA.setCurrentTemperature(GENERAL_TEMPERATURE);
		GENERAL_WEATHERDATA.setTemperatureHigh(GENERAL_TEMPERATURE);
		GENERAL_WEATHERDATA.setTemperatureLow(GENERAL_TEMPERATURE);
		GENERAL_WEATHERDATA.setApparentTemperature(GENERAL_TEMPERATURE);
		GENERAL_WEATHERDATA.setWeatherSummary(GENERAL_STRING);
		GENERAL_WEATHERDATA.setTime(GENERAL_DATETIME);
		GENERAL_WEATHERDATA.setHumidity(GENERAL_FLOAT);
		GENERAL_WEATHERDATA.setCloudCover(GENERAL_FLOAT);
		GENERAL_WEATHERDATA.setMoonPhase(GENERAL_FLOAT);
		GENERAL_WEATHERDATA.setWindBearing(GENERAL_INTEGER);
		GENERAL_WEATHERDATA.setWindGust(GENERAL_FLOAT);
		GENERAL_WEATHERDATA.setWindSpeed(GENERAL_FLOAT);
		GENERAL_WEATHERDATA.setNearestStormBearing(GENERAL_INTEGER);
		GENERAL_WEATHERDATA.setNearestStormDistance(GENERAL_INTEGER);
		GENERAL_WEATHERDATA.setPrecipAccumulation(GENERAL_FLOAT);
		GENERAL_WEATHERDATA.setPrecipIntensity(GENERAL_FLOAT);
		GENERAL_WEATHERDATA.setPrecipProbability(GENERAL_FLOAT);
		GENERAL_WEATHERDATA.setPrecipType(GENERAL_STRING);
		GENERAL_WEATHERDATA.setVisibility(GENERAL_FLOAT);
		GENERAL_WEATHERDATA.setWeatherIcon(GENERAL_IMAGE);

		GENERAL_WEATHERALERT.setTitle(GENERAL_STRING);
		GENERAL_WEATHERALERT.setSummary(GENERAL_STRING);
		GENERAL_WEATHERALERT.setExpires(GENERAL_DATETIME);
		GENERAL_WEATHERALERT.setRegions(GENERAL_STRING_LIST);
		GENERAL_WEATHERALERT.setSeverity(GENERAL_STRING);
		GENERAL_WEATHERALERT.setTimeIssued(GENERAL_DATETIME);

		GENERAL_WEATHERSERVICEDATA.setLocation(GENERAL_LOCATIONDETAILS);
		GENERAL_WEATHERSERVICEDATA.setCurrentForecast(GENERAL_WEATHERDATA);
		GENERAL_WEATHERSERVICEDATA.setMinuteForecast(GENERAL_WEATHERDATA_LIST);
		GENERAL_WEATHERSERVICEDATA.setHourlyForecast(GENERAL_WEATHERDATA_LIST);
		GENERAL_WEATHERSERVICEDATA.setMultidayForecast(GENERAL_WEATHERDATA_LIST);
		GENERAL_WEATHERSERVICEDATA.setAlerts(GENERAL_WEATHERALERT_LIST);

		GENERAL_MEDIASERVICEDATA.setMediaType(GENERAL_MEDIATYPE);
		GENERAL_MEDIASERVICEDATA.setMediaTitle(GENERAL_STRING);
		GENERAL_MEDIASERVICEDATA.setMediaArtist(GENERAL_STRING);
		GENERAL_MEDIASERVICEDATA.setMediaAlbum(GENERAL_STRING);
		GENERAL_MEDIASERVICEDATA.setPlaylistName(GENERAL_STRING);
		GENERAL_MEDIASERVICEDATA.setIsExplicit(GENERAL_BOOLEAN);
		GENERAL_MEDIASERVICEDATA.setTrackPlaybackProgress(GENERAL_INTEGER);
		GENERAL_MEDIASERVICEDATA.setTrackPlaybackDuration(GENERAL_INTEGER);
		GENERAL_MEDIASERVICEDATA.setQueuePlaybackProgress(GENERAL_INTEGER);
		GENERAL_MEDIASERVICEDATA.setQueuePlaybackDuration(GENERAL_INTEGER);
		GENERAL_MEDIASERVICEDATA.setQueueCurrentTrackNumber(GENERAL_INTEGER);
		GENERAL_MEDIASERVICEDATA.setQueueTotalTrackCount(GENERAL_INTEGER);

		GENERAL_APPSERVICEDATA.setServiceType(GENERAL_STRING);
		GENERAL_APPSERVICEDATA.setServiceID(GENERAL_STRING);
		GENERAL_APPSERVICEDATA.setWeatherServiceData(GENERAL_WEATHERSERVICEDATA);
		GENERAL_APPSERVICEDATA.setMediaServiceData(GENERAL_MEDIASERVICEDATA);

		GENERAL_NAVIGATION_INSTRUCTION.setLocationDetails(GENERAL_LOCATIONDETAILS);
		GENERAL_NAVIGATION_INSTRUCTION.setAction(GENERAL_NAVIGATIONACTION);
		GENERAL_NAVIGATION_INSTRUCTION.setEta(GENERAL_DATETIME);
		GENERAL_NAVIGATION_INSTRUCTION.setBearing(GENERAL_INTEGER);
		GENERAL_NAVIGATION_INSTRUCTION.setJunctionType(GENERAL_NAVIGATION_JUNCTION);
		GENERAL_NAVIGATION_INSTRUCTION.setDrivingSide(GENERAL_DIRECTION);
		GENERAL_NAVIGATION_INSTRUCTION.setDetails(GENERAL_STRING);
		GENERAL_NAVIGATION_INSTRUCTION.setImage(GENERAL_IMAGE);


		try {
			JSON_HMIPERMISSIONS.put(HMIPermissions.KEY_ALLOWED, GENERAL_HMILEVEL_LIST);
			JSON_HMIPERMISSIONS.put(HMIPermissions.KEY_USER_DISALLOWED, GENERAL_HMILEVEL_LIST);

			JSON_PCMSTREAMCAPABILITIES.put(AudioPassThruCapabilities.KEY_AUDIO_TYPE, GENERAL_AUDIOTYPE);
			JSON_PCMSTREAMCAPABILITIES.put(AudioPassThruCapabilities.KEY_BITS_PER_SAMPLE, GENERAL_BITSPERSAMPLE);
			JSON_PCMSTREAMCAPABILITIES.put(AudioPassThruCapabilities.KEY_SAMPLING_RATE, GENERAL_SAMPLINGRATE);

			JSON_TOUCHEVENTCAPABILITIES.put(TouchEventCapabilities.KEY_DOUBLE_PRESS_AVAILABLE, GENERAL_BOOLEAN);
			JSON_TOUCHEVENTCAPABILITIES.put(TouchEventCapabilities.KEY_MULTI_TOUCH_AVAILABLE, GENERAL_BOOLEAN);
			JSON_TOUCHEVENTCAPABILITIES.put(TouchEventCapabilities.KEY_PRESS_AVAILABLE, GENERAL_BOOLEAN);

			JSON_IMAGERESOLUTION.put(ImageResolution.KEY_RESOLUTION_HEIGHT, GENERAL_INT);
			JSON_IMAGERESOLUTION.put(ImageResolution.KEY_RESOLUTION_WIDTH, GENERAL_INT);

			JSON_CHOICE.put(Choice.KEY_MENU_NAME, GENERAL_STRING);
			JSON_CHOICE.put(Choice.KEY_SECONDARY_TEXT, GENERAL_STRING);
			JSON_CHOICE.put(Choice.KEY_TERTIARY_TEXT, GENERAL_STRING);
			JSON_CHOICE.put(Choice.KEY_CHOICE_ID, GENERAL_INT);
			JSON_CHOICE.put(Choice.KEY_IMAGE, JSON_IMAGE);
			JSON_CHOICE.put(Choice.KEY_SECONDARY_IMAGE, JSON_IMAGE);
			JSON_CHOICE.put(Choice.KEY_VR_COMMANDS, JsonUtils.createJsonArray(GENERAL_STRING_LIST));

			JSON_HMILEVELS.put(HMILevel.HMI_FULL);
			JSON_HMILEVELS.put(HMILevel.HMI_BACKGROUND);

			JSON_HMIPERMISSIONS.put(HMIPermissions.KEY_ALLOWED, JSON_HMILEVELS);
			JSON_HMIPERMISSIONS.put(HMIPermissions.KEY_USER_DISALLOWED, JSON_HMILEVELS);

			JSON_PARAMETERPERMISSIONS.put(ParameterPermissions.KEY_ALLOWED, JsonUtils.createJsonArray(GENERAL_STRING_LIST));
			JSON_PARAMETERPERMISSIONS.put(ParameterPermissions.KEY_USER_DISALLOWED, JsonUtils.createJsonArray(GENERAL_STRING_LIST));

			JSON_PERMISSIONITEM.put(PermissionItem.KEY_HMI_PERMISSIONS, JSON_HMIPERMISSIONS);
			JSON_PERMISSIONITEM.put(PermissionItem.KEY_PARAMETER_PERMISSIONS, JSON_PARAMETERPERMISSIONS);
			JSON_PERMISSIONITEM.put(PermissionItem.KEY_RPC_NAME, GENERAL_STRING);
			JSON_PERMISSIONITEMS.put(JSON_PERMISSIONITEM);

			JSON_IMAGE.put(Image.KEY_IMAGE_TYPE, GENERAL_IMAGETYPE);
			JSON_IMAGE.put(Image.KEY_VALUE, GENERAL_STRING);
			JSON_IMAGE.put(Image.KEY_IS_TEMPLATE, GENERAL_BOOLEAN);

			JSON_SOFTBUTTON.put(SoftButton.KEY_IS_HIGHLIGHTED , GENERAL_BOOLEAN);
			JSON_SOFTBUTTON.put(SoftButton.KEY_SOFT_BUTTON_ID, GENERAL_INT);
			JSON_SOFTBUTTON.put(SoftButton.KEY_SYSTEM_ACTION, SystemAction.STEAL_FOCUS);
			JSON_SOFTBUTTON.put(SoftButton.KEY_TEXT, GENERAL_STRING);
			JSON_SOFTBUTTON.put(SoftButton.KEY_TYPE, SoftButtonType.SBT_TEXT);
			JSON_SOFTBUTTON.put(SoftButton.KEY_IMAGE, GENERAL_IMAGE.serializeJSON());
			JSON_SOFTBUTTONS.put(JSON_SOFTBUTTON);

			JSON_TURN.put(Turn.KEY_NAVIGATION_TEXT, GENERAL_STRING);
			JSON_TURN.put(Turn.KEY_TURN_IMAGE, GENERAL_IMAGE.serializeJSON());
			JSON_TURNS.put(JSON_TURN);

			JSON_MENUPARAMS.put(MenuParams.KEY_MENU_NAME, GENERAL_STRING);
			JSON_MENUPARAMS.put(MenuParams.KEY_PARENT_ID, GENERAL_INT);
			JSON_MENUPARAMS.put(MenuParams.KEY_POSITION, GENERAL_INT);

	    	JSON_VRHELPITEM.put(VrHelpItem.KEY_TEXT, GENERAL_STRING);
	    	JSON_VRHELPITEM.put(VrHelpItem.KEY_IMAGE, JSON_IMAGE);
	    	JSON_VRHELPITEM.put(VrHelpItem.KEY_POSITION, GENERAL_INT);
	    	JSON_VRHELPITEMS.put(JSON_VRHELPITEM);

	    	JSONObject jsonTtsChunk = new JSONObject();
	    	jsonTtsChunk.put(TTSChunk.KEY_TEXT, "Welcome to the jungle");
	    	jsonTtsChunk.put(TTSChunk.KEY_TYPE, SpeechCapabilities.TEXT);
	    	JSON_TTSCHUNKS.put(jsonTtsChunk);
	    	jsonTtsChunk = new JSONObject();
	    	jsonTtsChunk.put(TTSChunk.KEY_TEXT, "Say a command");
	    	jsonTtsChunk.put(TTSChunk.KEY_TYPE, SpeechCapabilities.TEXT);
	    	JSON_TTSCHUNKS.put(jsonTtsChunk);

	    	JSON_KEYBOARDPROPERTIES.put(KeyboardProperties.KEY_AUTO_COMPLETE_TEXT, GENERAL_STRING);
	    	JSON_KEYBOARDPROPERTIES.put(KeyboardProperties.KEY_KEYPRESS_MODE, KeypressMode.SINGLE_KEYPRESS);
	    	JSON_KEYBOARDPROPERTIES.put(KeyboardProperties.KEY_KEYBOARD_LAYOUT, KeyboardLayout.QWERTY);
	    	JSON_KEYBOARDPROPERTIES.put(KeyboardProperties.KEY_LANGUAGE, Language.EN_US);
	    	JSON_KEYBOARDPROPERTIES.put(KeyboardProperties.KEY_LIMITED_CHARACTER_LIST, JsonUtils.createJsonArray(GENERAL_STRING_LIST));

			JSON_STARTTIME.put(StartTime.KEY_HOURS, GENERAL_STARTTIME.getHours());
			JSON_STARTTIME.put(StartTime.KEY_MINUTES, GENERAL_STARTTIME.getMinutes());
			JSON_STARTTIME.put(StartTime.KEY_SECONDS, GENERAL_STARTTIME.getSeconds());

			JSON_CHOICES.put(JSON_CHOICE);

			JSON_DEVICEINFO.put(DeviceInfo.KEY_CARRIER, GENERAL_STRING);
			JSON_DEVICEINFO.put(DeviceInfo.KEY_FIRMWARE_REV, GENERAL_STRING);
			JSON_DEVICEINFO.put(DeviceInfo.KEY_HARDWARE, GENERAL_STRING);
			JSON_DEVICEINFO.put(DeviceInfo.KEY_MAX_NUMBER_RFCOMM_PORTS, GENERAL_INT);
			JSON_DEVICEINFO.put(DeviceInfo.KEY_OS, GENERAL_STRING);
			JSON_DEVICEINFO.put(DeviceInfo.KEY_OS_VERSION, GENERAL_STRING);

			JSON_RGBCOLOR.put(RGBColor.KEY_RED, GENERAL_INT);
			JSON_RGBCOLOR.put(RGBColor.KEY_GREEN, GENERAL_INT);
			JSON_RGBCOLOR.put(RGBColor.KEY_BLUE, GENERAL_INT);

			JSON_DAYCOLORSCHEME.put(TemplateColorScheme.KEY_PRIMARY_COLOR, JSON_RGBCOLOR);
			JSON_DAYCOLORSCHEME.put(TemplateColorScheme.KEY_SECONDARY_COLOR, JSON_RGBCOLOR);
			JSON_DAYCOLORSCHEME.put(TemplateColorScheme.KEY_BACKGROUND_COLOR, JSON_RGBCOLOR);

			JSON_NIGHTCOLORSCHEME.put(TemplateColorScheme.KEY_PRIMARY_COLOR, JSON_RGBCOLOR);
			JSON_NIGHTCOLORSCHEME.put(TemplateColorScheme.KEY_SECONDARY_COLOR, JSON_RGBCOLOR);
			JSON_NIGHTCOLORSCHEME.put(TemplateColorScheme.KEY_BACKGROUND_COLOR, JSON_RGBCOLOR);

			JSON_SDLMSGVERSION.put(SdlMsgVersion.KEY_MAJOR_VERSION, GENERAL_INT);
			JSON_SDLMSGVERSION.put(SdlMsgVersion.KEY_MINOR_VERSION, GENERAL_INT);

			JSON_DIDRESULT.put(DIDResult.KEY_DATA, GENERAL_STRING);
			JSON_DIDRESULT.put(DIDResult.KEY_DID_LOCATION, GENERAL_INT);
			JSON_DIDRESULT.put(DIDResult.KEY_RESULT_CODE, VehicleDataResultCode.SUCCESS);
			JSON_DIDRESULTS.put(JSON_DIDRESULT);

			JSON_PRESETBANKCAPABILITIES.put(PresetBankCapabilities.KEY_ON_SCREEN_PRESETS_AVAILABLE, GENERAL_BOOLEAN);

			JSONObject jsonButton = new JSONObject();
			jsonButton.put(ButtonCapabilities.KEY_LONG_PRESS_AVAILABLE, false);
			jsonButton.put(ButtonCapabilities.KEY_SHORT_PRESS_AVAILABLE, GENERAL_BOOLEAN);
			jsonButton.put(ButtonCapabilities.KEY_UP_DOWN_AVAILABLE, GENERAL_BOOLEAN);
			jsonButton.put(ButtonCapabilities.KEY_NAME, ButtonName.SEEKRIGHT);
			JSON_BUTTONCAPABILITIES.put(jsonButton);

			JSONObject jsonRadioControlCapabilities = new JSONObject();
			jsonRadioControlCapabilities.put(RadioControlCapabilities.KEY_MODULE_NAME, GENERAL_STRING);
			jsonRadioControlCapabilities.put(RadioControlCapabilities.KEY_RADIO_ENABLE_AVAILABLE, GENERAL_BOOLEAN);
			jsonRadioControlCapabilities.put(RadioControlCapabilities.KEY_RADIO_BAND_AVAILABLE, GENERAL_BOOLEAN);
			jsonRadioControlCapabilities.put(RadioControlCapabilities.KEY_RADIO_FREQUENCY_AVAILABLE, GENERAL_BOOLEAN);
			jsonRadioControlCapabilities.put(RadioControlCapabilities.KEY_HD_CHANNEL_AVAILABLE, GENERAL_BOOLEAN);
			jsonRadioControlCapabilities.put(RadioControlCapabilities.KEY_RDS_DATA_AVAILABLE, GENERAL_BOOLEAN);
			jsonRadioControlCapabilities.put(RadioControlCapabilities.KEY_AVAILABLE_HDS_AVAILABLE, GENERAL_BOOLEAN);
			jsonRadioControlCapabilities.put(RadioControlCapabilities.KEY_STATE_AVAILABLE, GENERAL_BOOLEAN);
			jsonRadioControlCapabilities.put(RadioControlCapabilities.KEY_SIGNAL_STRENGTH_AVAILABLE, GENERAL_BOOLEAN);
			jsonRadioControlCapabilities.put(RadioControlCapabilities.KEY_SIGNAL_CHANGE_THRESHOLD_AVAILABLE, GENERAL_BOOLEAN);
			JSON_RADIOCONTROLCAPABILITIES.put(jsonRadioControlCapabilities);

			JSONObject jsonClimateControlCapabilities = new JSONObject();
			jsonClimateControlCapabilities.put(ClimateControlCapabilities.KEY_MODULE_NAME, GENERAL_STRING);
			jsonClimateControlCapabilities.put(ClimateControlCapabilities.KEY_FAN_SPEED_AVAILABLE, GENERAL_BOOLEAN);
			jsonClimateControlCapabilities.put(ClimateControlCapabilities.KEY_DESIRED_TEMPERATURE_AVAILABLE, GENERAL_BOOLEAN);
			jsonClimateControlCapabilities.put(ClimateControlCapabilities.KEY_AC_ENABLE_AVAILABLE, GENERAL_BOOLEAN);
			jsonClimateControlCapabilities.put(ClimateControlCapabilities.KEY_AC_MAX_ENABLE_AVAILABLE, GENERAL_BOOLEAN);
			jsonClimateControlCapabilities.put(ClimateControlCapabilities.KEY_CIRCULATE_AIR_ENABLE_AVAILABLE, GENERAL_BOOLEAN);
			jsonClimateControlCapabilities.put(ClimateControlCapabilities.KEY_AUTO_MODE_ENABLE_AVAILABLE, GENERAL_BOOLEAN);
			jsonClimateControlCapabilities.put(ClimateControlCapabilities.KEY_DUAL_MODE_ENABLE_AVAILABLE, GENERAL_BOOLEAN);
			jsonClimateControlCapabilities.put(ClimateControlCapabilities.KEY_DEFROST_ZONE_AVAILABLE, GENERAL_BOOLEAN);
			jsonClimateControlCapabilities.put(ClimateControlCapabilities.KEY_DEFROST_ZONE, GENERAL_DEFROSTZONE_LIST);
			jsonClimateControlCapabilities.put(ClimateControlCapabilities.KEY_VENTILATION_MODE_AVAILABLE, GENERAL_BOOLEAN);
			jsonClimateControlCapabilities.put(ClimateControlCapabilities.KEY_VENTILATION_MODE, GENERAL_VENTILATIONMODE_LIST);
			JSON_CLIMATECONTROLCAPABILITIES.put(jsonClimateControlCapabilities);

			jsonButton = new JSONObject();
			jsonButton.put(SoftButtonCapabilities.KEY_LONG_PRESS_AVAILABLE, GENERAL_BOOLEAN);
			jsonButton.put(SoftButtonCapabilities.KEY_SHORT_PRESS_AVAILABLE, GENERAL_BOOLEAN);
			jsonButton.put(SoftButtonCapabilities.KEY_UP_DOWN_AVAILABLE, GENERAL_BOOLEAN);
			jsonButton.put(SoftButtonCapabilities.KEY_IMAGE_SUPPORTED, GENERAL_BOOLEAN);
			JSON_SOFTBUTTONCAPABILITIES.put(jsonButton);

			jsonButton = new JSONObject();
			jsonButton.put(AudioPassThruCapabilities.KEY_AUDIO_TYPE, GENERAL_AUDIOTYPE);
			jsonButton.put(AudioPassThruCapabilities.KEY_BITS_PER_SAMPLE, GENERAL_BITSPERSAMPLE);
			jsonButton.put(AudioPassThruCapabilities.KEY_SAMPLING_RATE, GENERAL_SAMPLINGRATE);
			JSON_AUDIOPASSTHRUCAPABILITIES.put(jsonButton);

			JSON_TEXTFIELD.put(TextField.KEY_CHARACTER_SET, CharacterSet.CID1SET);
			JSON_TEXTFIELD.put(TextField.KEY_NAME, TextFieldName.ETA);
			JSON_TEXTFIELD.put(TextField.KEY_ROWS, GENERAL_INT);
			JSON_TEXTFIELD.put(TextField.KEY_WIDTH, GENERAL_INT);
			JSON_TEXTFIELDS.put(JSON_TEXTFIELD);

			JSON_IMAGEFIELD.put(ImageField.KEY_IMAGE_RESOLUTION, JSON_IMAGERESOLUTION);
			JSON_IMAGEFIELD.put(ImageField.KEY_IMAGE_TYPE_SUPPORTED, JsonUtils.createJsonArray(Test.GENERAL_FILETYPE_LIST));
			JSON_IMAGEFIELD.put(ImageField.KEY_NAME, ImageFieldName.graphic);
			JSON_IMAGEFIELDS.put(JSON_IMAGEFIELD);

			JSONObject jsonTEC = new JSONObject();
			jsonTEC.put(TouchEventCapabilities.KEY_DOUBLE_PRESS_AVAILABLE, GENERAL_BOOLEAN);
			jsonTEC.put(TouchEventCapabilities.KEY_MULTI_TOUCH_AVAILABLE, GENERAL_BOOLEAN);
			jsonTEC.put(TouchEventCapabilities.KEY_PRESS_AVAILABLE, GENERAL_BOOLEAN);

			JSON_SCREENPARAMS.put(ScreenParams.KEY_RESOLUTION, JSON_IMAGERESOLUTION);
			JSON_SCREENPARAMS.put(ScreenParams.KEY_TOUCH_EVENT_AVAILABLE, jsonTEC);

			JSON_DISPLAYCAPABILITIES.put(DisplayCapabilities.KEY_DISPLAY_TYPE, GENERAL_DISPLAYTYPE);
			JSON_DISPLAYCAPABILITIES.put(DisplayCapabilities.KEY_DISPLAY_NAME, GENERAL_STRING);
			JSON_DISPLAYCAPABILITIES.put(DisplayCapabilities.KEY_GRAPHIC_SUPPORTED, GENERAL_BOOLEAN);
			JSON_DISPLAYCAPABILITIES.put(DisplayCapabilities.KEY_IMAGE_FIELDS, JSON_IMAGEFIELDS);
			JSON_DISPLAYCAPABILITIES.put(DisplayCapabilities.KEY_MEDIA_CLOCK_FORMATS, JsonUtils.createJsonArray(GENERAL_MEDIACLOCKFORMAT_LIST));
			JSON_DISPLAYCAPABILITIES.put(DisplayCapabilities.KEY_NUM_CUSTOM_PRESETS_AVAILABLE, GENERAL_INT);
			JSON_DISPLAYCAPABILITIES.put(DisplayCapabilities.KEY_SCREEN_PARAMS, JSON_SCREENPARAMS);
			JSON_DISPLAYCAPABILITIES.put(DisplayCapabilities.KEY_TEMPLATES_AVAILABLE, JsonUtils.createJsonArray(GENERAL_STRING_LIST));
			JSON_DISPLAYCAPABILITIES.put(DisplayCapabilities.KEY_TEXT_FIELDS, JSON_TEXTFIELDS);

			JSON_TOUCHCOORD.put(TouchCoord.KEY_X, GENERAL_INT);
			JSON_TOUCHCOORD.put(TouchCoord.KEY_Y, GENERAL_INT);
			JSON_TOUCHCOORDS.put(JSON_TOUCHCOORD);

			JSON_TOUCHEVENT.put(TouchEvent.KEY_C, JSON_TOUCHCOORDS);
			JSON_TOUCHEVENT.put(TouchEvent.KEY_ID, GENERAL_INT);
			JSON_TOUCHEVENT.put(TouchEvent.KEY_TS, JsonUtils.createJsonArray(GENERAL_LONG_LIST));
			JSON_TOUCHEVENTS.put(JSON_TOUCHEVENT);

			JSON_TEXTFIELDTYPES.put(MetadataType.CURRENT_TEMPERATURE);
			JSON_TEXTFIELDTYPES.put(MetadataType.MEDIA_ALBUM);
			JSON_TEXTFIELDTYPES.put(MetadataType.MEDIA_ARTIST);

		} catch (JSONException e) {
			Log.e("Test", "Static Json Construction Failed.", e);
		}
	}
}
