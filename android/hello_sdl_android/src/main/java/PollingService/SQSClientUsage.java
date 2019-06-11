package PollingService;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import cz.msebera.android.httpclient.Header;

public class SQSClientUsage {

    private RequestParams params = new RequestParams();

    private String KEY_ITEM = "MessageAttribute";
    final private SignData data = new SignData();


    private ReceivedAwsDataListener listener;

    public interface ReceivedAwsDataListener {
        void onAwsDataReady(SignData data);
    }

    public SQSClientUsage(){
        this.listener = null;
    }

    public void setReceivedAwsDataListener(ReceivedAwsDataListener listener) {
        this.listener = listener;
    }

    public void getSQSmsg(){
        params.put("Version", "2012-11-05");
        params.put("Action", "ReceiveMessage");
        params.put("MessageAttributeName","All");

        SQSClient.post("", params, new TextHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                try {
                    System.out.println(response);
                    XMLParser parser = new XMLParser();
                    Document doc = parser.getDomElement(response);
                    NodeList nl = doc.getElementsByTagName(KEY_ITEM);

                    String lat = nl.item(1).getLastChild().getFirstChild().getFirstChild().getNodeValue();
                    String lon = nl.item(2).getLastChild().getFirstChild().getFirstChild().getNodeValue();
                    String uuid = nl.item(3).getLastChild().getFirstChild().getFirstChild().getNodeValue();

                    data.setLatitude(lat);
                    data.setLongitude(lon);
                    data.setUuid(uuid);

                    listener.onAwsDataReady(data);

                    System.out.println(doc.toString());
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
