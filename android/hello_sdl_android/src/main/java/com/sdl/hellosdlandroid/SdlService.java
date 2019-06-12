package com.sdl.hellosdlandroid;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.smartdevicelink.managers.CompletionListener;
import com.smartdevicelink.managers.SdlManager;
import com.smartdevicelink.managers.SdlManagerListener;
import com.smartdevicelink.managers.file.filetypes.SdlArtwork;
import com.smartdevicelink.protocol.enums.FunctionID;
import com.smartdevicelink.proxy.RPCNotification;
import com.smartdevicelink.proxy.RPCResponse;
import com.smartdevicelink.proxy.TTSChunkFactory;
import com.smartdevicelink.proxy.rpc.AddCommand;
import com.smartdevicelink.proxy.rpc.MenuParams;
import com.smartdevicelink.proxy.rpc.OnCommand;
import com.smartdevicelink.proxy.rpc.OnHMIStatus;
import com.smartdevicelink.proxy.rpc.SetDisplayLayout;
import com.smartdevicelink.proxy.rpc.SetDisplayLayoutResponse;
import com.smartdevicelink.proxy.rpc.Speak;
import com.smartdevicelink.proxy.rpc.enums.AppHMIType;
import com.smartdevicelink.proxy.rpc.enums.FileType;
import com.smartdevicelink.proxy.rpc.enums.HMILevel;
import com.smartdevicelink.proxy.rpc.enums.PredefinedLayout;
import com.smartdevicelink.proxy.rpc.listeners.OnRPCNotificationListener;
import com.smartdevicelink.proxy.rpc.listeners.OnRPCResponseListener;
import com.smartdevicelink.transport.BaseTransportConfig;
import com.smartdevicelink.transport.MultiplexTransportConfig;
import com.smartdevicelink.transport.TCPTransportConfig;
import com.smartdevicelink.util.DebugTool;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Vector;

import PollingService.PollingService;
import PollingService.SQSClientUsage;
import PollingService.SignData;
import server.MyServer;

import static com.smartdevicelink.proxy.rpc.enums.PredefinedLayout.LARGE_GRAPHIC_ONLY;

public class SdlService extends Service {

	private static final String TAG 					= "SDL Service";

	private static final String APP_NAME 				= "Hello Sdl";
	private static final String APP_ID 					= "8678309";

	private static final String ICON_FILENAME 			= "hello_sdl_icon.png";
	private static final String SDL_IMAGE_FILENAME  	= "sdl_full_image.png";

	private static final String WELCOME_SHOW 			= "Welcome to HelloSDL";
	private static final String WELCOME_SPEAK 			= "Welcome to SDL Server";

	private static final String TEST_COMMAND_NAME 		= "Test Command";
	private static final int TEST_COMMAND_ID 			= 1;

	private static final int FOREGROUND_SERVICE_ID = 111;

	// TCP/IP transport config
	// The default port is 12345
	// The IP is of the machine that is running SDL Core
	private static final int TCP_PORT = 12345;
	private static final String DEV_MACHINE_IP_ADDRESS = "192.168.1.78";

	// variable to create and call functions of the SyncProxy
	private SdlManager sdlManager = null;

	private MyServer wbsvr;

	private PollingService poller;

	private boolean START_AWS_POOL = true;

	SQSClientUsage sqscu;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		super.onCreate();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			enterForeground();
		}

		sqscu = new SQSClientUsage();
	}

	// Helper method to let the service enter foreground mode
	@SuppressLint("NewApi")
	public void enterForeground() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(APP_ID, "SdlService", NotificationManager.IMPORTANCE_DEFAULT);
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			if (notificationManager != null) {
				notificationManager.createNotificationChannel(channel);
				Notification serviceNotification = new Notification.Builder(this, channel.getId())
						.setContentTitle("Connected through SDL")
						.setSmallIcon(R.drawable.ic_sdl)
						.build();
				startForeground(FOREGROUND_SERVICE_ID, serviceNotification);
			}
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startProxy();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			stopForeground(true);
		}

		if (sdlManager != null) {
			sdlManager.dispose();
		}

		super.onDestroy();
	}

	public void startProxy() {
		// This logic is to select the correct transport and security levels defined in the selected build flavor
		// Build flavors are selected by the "build variants" tab typically located in the bottom left of Android Studio
		// Typically in your app, you will only set one of these.
		if (sdlManager == null) {
			Log.i(TAG, "Starting SDL Proxy");
			// Enable DebugTool for debug build type
			if (BuildConfig.DEBUG){
				DebugTool.enableDebugTool();
			}
			BaseTransportConfig transport = null;
			if (BuildConfig.TRANSPORT.equals("MULTI")) {
				int securityLevel;
				if (BuildConfig.SECURITY.equals("HIGH")) {
					securityLevel = MultiplexTransportConfig.FLAG_MULTI_SECURITY_HIGH;
				} else if (BuildConfig.SECURITY.equals("MED")) {
					securityLevel = MultiplexTransportConfig.FLAG_MULTI_SECURITY_MED;
				} else if (BuildConfig.SECURITY.equals("LOW")) {
					securityLevel = MultiplexTransportConfig.FLAG_MULTI_SECURITY_LOW;
				} else {
					securityLevel = MultiplexTransportConfig.FLAG_MULTI_SECURITY_OFF;
				}
				transport = new MultiplexTransportConfig(this, APP_ID, securityLevel);
			} else if (BuildConfig.TRANSPORT.equals("TCP")) {
				transport = new TCPTransportConfig(TCP_PORT, DEV_MACHINE_IP_ADDRESS, true);
			} else if (BuildConfig.TRANSPORT.equals("MULTI_HB")) {
				MultiplexTransportConfig mtc = new MultiplexTransportConfig(this, APP_ID, MultiplexTransportConfig.FLAG_MULTI_SECURITY_OFF);
				mtc.setRequiresHighBandwidth(true);
				transport = mtc;
			}

			// The app type to be used
			Vector<AppHMIType> appType = new Vector<>();
			appType.add(AppHMIType.MEDIA);

			// The manager listener helps you know when certain events that pertain to the SDL Manager happen
			// Here we will listen for ON_HMI_STATUS and ON_COMMAND notifications
			SdlManagerListener listener = new SdlManagerListener() {
				@Override
				public void onStart() {
					// HMI Status Listener
					sdlManager.addOnRPCNotificationListener(FunctionID.ON_HMI_STATUS, new OnRPCNotificationListener() {
						@Override
						public void onNotified(RPCNotification notification) {
							OnHMIStatus status = (OnHMIStatus) notification;
							if (status.getHmiLevel() == HMILevel.HMI_FULL && ((OnHMIStatus) notification).getFirstRun()) {
//								sendCommands();
//								performWelcomeSpeak();
//								performWelcomeShow();
							}
						}
					});

					// Menu Selected Listener
					sdlManager.addOnRPCNotificationListener(FunctionID.ON_COMMAND, new OnRPCNotificationListener() {
						@Override
						public void onNotified(RPCNotification notification) {
							OnCommand command = (OnCommand) notification;
							Integer id = command.getCmdID();
							if(id != null){
								switch(id){
									case TEST_COMMAND_ID:
										showTest();
										break;
								}
							}
						}
					});

					// Pre-upload all possible images
					uploadImage("do_not_enter.jpg", R.drawable.do_not_enter, FileType.GRAPHIC_JPEG);
					uploadImage("ingalls_historical_marker.jpg", R.drawable.ingalls_historical_marker, FileType.GRAPHIC_JPEG);
					uploadImage("no_right_turn.jpg", R.drawable.no_right_turn, FileType.GRAPHIC_JPEG);
					uploadImage("road_work_ahead.jpg", R.drawable.road_work_ahead, FileType.GRAPHIC_JPEG);
					uploadImage("speed_limit_30.jpg", R.drawable.speed_limit_30, FileType.GRAPHIC_JPEG);
					uploadImage("detroitchimera.jpg", R.drawable.detroitchimera, FileType.GRAPHIC_JPEG);
					uploadImage("sad_face.jpg", R.drawable.sad_face, FileType.GRAPHIC_JPEG);
					System.out.println("All img files uploaded");

					try
					{
						sqscu.setReceivedAwsDataListener(new SQSClientUsage.ReceivedAwsDataListener() {
							@Override
							public void onAwsDataReady(SignData data) {
								System.out.println(data.getUuid());

								SdlArtwork uuidSdlArt = getSdlArtworkfromUuid(data.getUuid());

								showTextArt(data.getText1(),data.getText2(),data.getTts(), uuidSdlArt);
							}
						});

						while(START_AWS_POOL) {
							sqscu.getSQSmsg();
							Thread.sleep(10000);
							START_AWS_POOL = false;
						}

					}
					catch (Exception ex) {
						System.out.println((ex.getMessage()));
					}
				}

				@Override
				public void onDestroy() {

					SdlService.this.stopSelf();
					START_AWS_POOL = false;
				}

				@Override
				public void onError(String info, Exception e) {
					START_AWS_POOL = false;
				}
			};

			// Create App Icon, this is set in the SdlManager builder
			SdlArtwork appIcon = new SdlArtwork(ICON_FILENAME, FileType.GRAPHIC_PNG, R.mipmap.ic_launcher, true);

			// The manager builder sets options for your session
			SdlManager.Builder builder = new SdlManager.Builder(this, APP_ID, APP_NAME, listener);
			builder.setAppTypes(appType);
			builder.setTransportType(transport);
			builder.setAppIcon(appIcon);
			sdlManager = builder.build();
			sdlManager.start();

			wbsvr = new MyServer(sdlManager);
			try {
				wbsvr.start();
			}
			catch (IOException ex){
				Log.w ("httpd","server started");
			}
		}
	}

	private SdlArtwork getSdlArtworkfromUuid (String uuid){
//		final String imgName = FilenameUtils.getBaseName(fileName);

		String fileName = "";
		int resourceID = 0;
		FileType fileType = null;

		if (uuid.equals("12345")){
			fileName = "sad_face.jpg";
			resourceID = R.drawable.sad_face;
			fileType = FileType.GRAPHIC_JPEG;
		}
		else if (uuid.equals("E53EEB38DE48")){
			fileName = "no_right_turn.jpg";
			resourceID = R.drawable.no_right_turn;
			fileType = FileType.GRAPHIC_JPEG;
		}
		else if (uuid.equals("B9378CE25608")){
			fileName = "road_work_ahead.jpg";
			resourceID = R.drawable.road_work_ahead;
			fileType = FileType.GRAPHIC_JPEG;
		}
		else if (uuid.equals("03B84453676B")){
			fileName = "do_not_enter.jpg";
			resourceID = R.drawable.do_not_enter;
			fileType = FileType.GRAPHIC_JPEG;
		}
		else if (uuid.equals("749324AD9639")){
			fileName = "speed_limit_30.jpg";
			resourceID = R.drawable.speed_limit_30;
			fileType = FileType.GRAPHIC_JPEG;
		}
		else if (uuid.equals("B0EA8B0B1223")){
			fileName = "ingalls_historical_marker.jpg";
			resourceID = R.drawable.ingalls_historical_marker;
			fileType = FileType.GRAPHIC_JPEG;
		}
		else if (uuid.equals("0A8E4C5B4892")){
			fileName = "detroitchimera.jpg";
			resourceID = R.drawable.detroitchimera;
			fileType = FileType.GRAPHIC_JPEG;
		}

		SdlArtwork sdlArtwork = new SdlArtwork(fileName, fileType, resourceID, true);

		return sdlArtwork;
	}



	/**
	 *  Add commands for the app on SDL.
	 */
	private void sendCommands(){
		AddCommand command = new AddCommand();
		MenuParams params = new MenuParams();
		params.setMenuName(TEST_COMMAND_NAME);
		command.setCmdID(TEST_COMMAND_ID);
		command.setMenuParams(params);
		command.setVrCommands(Collections.singletonList(TEST_COMMAND_NAME));
		sdlManager.sendRPC(command);
	}

	/**
	 * Will speak a sample welcome message
	 */
	private void performWelcomeSpeak(){
		sdlManager.sendRPC(new Speak(TTSChunkFactory.createSimpleTTSChunks(WELCOME_SPEAK)));
	}

	/**
	 * Use the Screen Manager to set the initial screen text and set the image.
	 * Because we are setting multiple items, we will call beginTransaction() first,
	 * and finish with commit() when we are done.
	 */
	private void performWelcomeShow() {
		sdlManager.getScreenManager().beginTransaction();
		sdlManager.getScreenManager().setTextField1(APP_NAME);
		sdlManager.getScreenManager().setTextField2(WELCOME_SHOW);
		sdlManager.getScreenManager().setPrimaryGraphic(new SdlArtwork(SDL_IMAGE_FILENAME, FileType.GRAPHIC_PNG, R.drawable.sdl, true));
		sdlManager.getScreenManager().commit(new CompletionListener() {
			@Override
			public void onComplete(boolean success) {
				if (success){
					Log.i(TAG, "welcome show successful");
				}
			}
		});
	}

	public void uploadImage(final String fileName, int resourceID, FileType fileType) {
		SdlArtwork sdlArtwork = new SdlArtwork(fileName, fileType, resourceID, true);
		final String imgName = FilenameUtils.getBaseName(fileName);
		sdlManager.getFileManager().uploadArtwork(sdlArtwork, new CompletionListener() {
			@Override
			public void onComplete(boolean success) {
				System.out.println("Artwork uploaded");
			}
		});
	}

	/**
	 * Will show a sample test message on screen as well as speak a sample test message
	 */
	public void showTest(){
		sdlManager.getScreenManager().beginTransaction();
		sdlManager.getScreenManager().setTextField1("Command has been selected");
		sdlManager.getScreenManager().setTextField2("");
		sdlManager.getScreenManager().commit(null);

		sdlManager.sendRPC(new Speak(TTSChunkFactory.createSimpleTTSChunks(TEST_COMMAND_NAME)));
	}

	private void showText(String txtmsg, String voicemsg ){
		sdlManager.getScreenManager().beginTransaction();
		sdlManager.getScreenManager().setTextField1(txtmsg);
		sdlManager.getScreenManager().setTextField2("");
		sdlManager.getScreenManager().commit(null);

		sdlManager.sendRPC(new Speak(TTSChunkFactory.createSimpleTTSChunks(voicemsg)));
	}

	private void showTextArt(String txtmsg1, String txtmsg2, String voicemsg, SdlArtwork sdlArtwork){

		// Set Screen Layout
		SetDisplayLayout setDisplayLayoutRequest = new SetDisplayLayout();
		setDisplayLayoutRequest.setDisplayLayout(PredefinedLayout.GRAPHIC_WITH_TEXT.toString());
		setDisplayLayoutRequest.setOnRPCResponseListener(new OnRPCResponseListener() {
			@Override
			public void onResponse(int correlationId, RPCResponse response) {
				if(((SetDisplayLayoutResponse) response).getSuccess()){
					Log.i("SdlService", "Display layout set successfully.");
					// Proceed with more user interface RPCs
				}else{
					Log.i("SdlService", "Display layout request rejected.");
				}
			}
		});

		sdlManager.sendRPC(setDisplayLayoutRequest);

		// Set Screen Content
//		uploadImage(fileName, R.drawable.detroitchimera, FileType.GRAPHIC_PNG);
//		int resID = getResId(imgName, R.drawable.class);
//		SdlArtwork sdlArtwork = new SdlArtwork(fileName, FileType.GRAPHIC_PNG, resID, true);

		sdlManager.getScreenManager().beginTransaction();
		sdlManager.getScreenManager().setPrimaryGraphic(sdlArtwork);
		sdlManager.getScreenManager().setTextField1(txtmsg1);
		sdlManager.getScreenManager().setTextField2(txtmsg2);
		sdlManager.getScreenManager().commit(null);

//		sdlManager.sendRPC(new Speak(TTSChunkFactory.createSimpleTTSChunks(voicemsg)));
	}

	public static int getResId(String resName, Class<?> c) {

		try {
			Field idField = c.getDeclaredField(resName);
			return idField.getInt(idField);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

}
