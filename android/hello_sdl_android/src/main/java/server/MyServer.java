package server;

import android.util.Log;

import java.util.Map;

import PollingService.SQSClientUsage;
import PollingService.SignData;
import fi.iki.elonen.NanoHTTPD;

import com.sdl.hellosdlandroid.R;

import com.smartdevicelink.managers.CompletionListener;
import com.smartdevicelink.managers.SdlManager;
import com.smartdevicelink.managers.file.filetypes.SdlArtwork;
import com.smartdevicelink.proxy.TTSChunkFactory;
import com.smartdevicelink.proxy.rpc.Speak;
import com.smartdevicelink.proxy.rpc.enums.FileType;


public class MyServer extends NanoHTTPD {
    private static final String WARNING_ZONE = "Welcome to Hello Gary and Tyler you are interrupting the music again";
    private static final String DANGER_ZONE = "I feel the need, for speed!";

    private static final String TAG 					= "SDL Service";

    private static final String APP_NAME 				= "Hello Sdl";
    private static final String APP_ID 					= "8678309";

    private static final String ICON_FILENAME 			= "hello_sdl_icon.png";
    private static final String SDL_IMAGE_FILENAME  	= "sdl_full_image.png";

    private static final String WELCOME_SHOW 			= "Welcome to HelloSDL";
    private static final String WELCOME_SPEAK 			= "Welcome to SDL Server";

    private static final String TEST_COMMAND_NAME 		= "Test Command";
    private static final int TEST_COMMAND_ID 			= 1;


    private final static int PORT = 8080;
    private SdlManager _sdlmgr;


    public MyServer(SdlManager sdlmgr) {
        super(PORT);
        _sdlmgr = sdlmgr;
        System.out.println( "\nRunning! Point your browsers to http://localhost:8080/ \n" );
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String,String> params = session.getParms();
        Method meth = session.getMethod();

        if (meth.name().equals("GET")){
            if (uri.contains("/showtest")){
                showTest();
                return newFixedLengthResponse(Response.Status.OK, "text/plain","Called showtest");
            }
            else if (uri.contains("/performwelcomeshow")) {
                performWelcomeShow();
                return newFixedLengthResponse(Response.Status.OK, "text/plain","Called performwelcometest");
            }
        }
        else if (meth.name().equals("POST")){
            if (uri.contains("/obscura")){
//                showText("Received Unable to get data","Unable to get data");
//                return newFixedLengthResponse(Response.Status.OK, "text/plain","Unable to get data");


                String uuid = params.get("data");
                showText("Received " + uuid, uuid);
                return newFixedLengthResponse(Response.Status.OK, "text/plain","Received " + uuid);
            }

        }

        return newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/plain","Unrecognized route");
    }

    private void performWelcomeShow() {
        _sdlmgr.getScreenManager().beginTransaction();
        _sdlmgr.getScreenManager().setTextField1(APP_NAME);
        _sdlmgr.getScreenManager().setTextField2(WELCOME_SHOW);
        _sdlmgr.getScreenManager().setPrimaryGraphic(new SdlArtwork(SDL_IMAGE_FILENAME, FileType.GRAPHIC_PNG, R.drawable.sdl, true));
        _sdlmgr.getScreenManager().commit(new CompletionListener() {
            @Override
            public void onComplete(boolean success) {
                if (success){
                    Log.i(TAG, "welcome show successful");
                }
            }
        });
    }

    private void showTest(){
        _sdlmgr.getScreenManager().beginTransaction();
        _sdlmgr.getScreenManager().setTextField1("Command has been selected");
        _sdlmgr.getScreenManager().setTextField2("");
        _sdlmgr.getScreenManager().commit(null);

        _sdlmgr.sendRPC(new Speak(TTSChunkFactory.createSimpleTTSChunks(TEST_COMMAND_NAME)));
    }

    private void showText(String txtmsg, String voicemsg ){
        _sdlmgr.getScreenManager().beginTransaction();
        _sdlmgr.getScreenManager().setTextField1(txtmsg);
        _sdlmgr.getScreenManager().setTextField2("");
        _sdlmgr.getScreenManager().commit(null);

        _sdlmgr.sendRPC(new Speak(TTSChunkFactory.createSimpleTTSChunks(voicemsg)));
    }
}
