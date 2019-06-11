package PollingService;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class PollSrv {
    public void callsqs_endpoint(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://www.google.com", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

}























//import android.os.Debug;
//import android.preference.PreferenceActivity;
//import android.util.Log;
//
//import com.loopj.android.http.RequestParams;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.loopj.android.http.*;
//import com.loopj.android.http.JsonHttpResponseHandler;
//
//import cz.msebera.android.httpclient.entity.mime.Header;
//
//import static com.smartdevicelink.proxy.constants.Names.headers;
//import static com.smartdevicelink.proxy.constants.Names.response;
//
//public class PollSrv {
//
//    private final int WAIT_TIME = 1000; //msec
//    private boolean START_STOP = false;
//    private final String URL_FEED = "https://sqs.us-east-2.amazonaws.com/371900921998/camera_queue.fifo?Version=2012-11-05&Action=ReceiveMessage&MessageAttributeName=All";
//
//    public void PollSrv() {
//        rp = new RequestParams();
//    }
//
//    private RequestParams rp;
//
//    public void PollSrv_Start() {
//        START_STOP = true;
//        rp.add("username", "aaa"); rp.add("password", "aaa@123");
//    }
//
//    public void PollSrv_Stop() {
//        START_STOP = false;
//    }
//
//
//
//
//    private void pollsqs(){
//        while (START_STOP){
//            try {
//                HTTPUtils.post(URL_FEED, rp, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        // If the response is JSONObject instead of expected JSONArray
//                        Log.d("asd", "---------------- this is response : " + response);
//                        try {
//                            JSONObject serverResp = new JSONObject(response.toString());
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//
////                    @Override
////                    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
////                        // Pull out the first event on the public timeline
////
////                    }
//                });
//
//                Thread.sleep(WAIT_TIME);
//            }
//            catch (InterruptedException ex){
//            }
//        }
//    }
//}
