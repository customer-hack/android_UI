package PollingService;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;


import cz.msebera.android.httpclient.Header;

public class SQSClientUsage {

    private RequestParams params = new RequestParams();

    public void getSQSmsg(){
        params.put("Version", "2012-11-05");
        params.put("Action", "ReceiveMessage");
        params.put("MessageAttributeName","All");

        SQSClient.post("", params, new TextHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                try {
                    System.out.println(response);
                }
                catch(Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }


            @Override
            public final void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
                try {
                    System.out.println(errorResponse);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

        });
    }

}
