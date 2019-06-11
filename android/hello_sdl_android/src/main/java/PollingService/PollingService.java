package PollingService;

import com.smartdevicelink.managers.SdlManager;
import com.smartdevicelink.proxy.TTSChunkFactory;
import com.smartdevicelink.proxy.rpc.Speak;

public class PollingService {
    private boolean CONTINUE_LOOPING = true;
    private int POLL_TIME = 10000;

    SignData _data;
    private SQSClientUsage sqscu = new SQSClientUsage();

    private SdlManager _sdlmgr;


    public PollingService(SdlManager sdlmgr) {
        _sdlmgr = sdlmgr;

        sqscu.setReceivedAwsDataListener(new SQSClientUsage.ReceivedAwsDataListener() {
            @Override
            public void onAwsDataReady(SignData data) {
                _data = data;
                System.out.println(data.getUuid());

                showTextData(data);
            }
        });
    }

    public void startPollingService() {
        sqscu.getSQSmsg();

//        while(CONTINUE_LOOPING) {
//            sqscu.getSQSmsg();
//
//            try {
//                Thread.sleep(POLL_TIME);
//            }
//            catch (Exception ex){
//                System.out.println(ex.getMessage());
//            }
//
//        }
    }

//
//    private void showText(String txtmsg, String voicemsg ){
//        _sdlmgr.getScreenManager().beginTransaction();
//        _sdlmgr.getScreenManager().setTextField1(txtmsg);
//        _sdlmgr.getScreenManager().setTextField2("");
//        _sdlmgr.getScreenManager().commit(null);
//
//        _sdlmgr.sendRPC(new Speak(TTSChunkFactory.createSimpleTTSChunks(voicemsg)));
//    }

    private void showTextData(SignData data){
        _sdlmgr.getScreenManager().beginTransaction();
        _sdlmgr.getScreenManager().setTextField1(data.getUuid());
        _sdlmgr.getScreenManager().setTextField2("");
        _sdlmgr.getScreenManager().commit(null);

        _sdlmgr.sendRPC(new Speak(TTSChunkFactory.createSimpleTTSChunks(data.getUuid())));
    }
}
