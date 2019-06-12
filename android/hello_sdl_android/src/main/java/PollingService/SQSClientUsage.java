package PollingService;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.Random;

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

        SQSClient.get("", params, new TextHttpResponseHandler() {


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

//                    data.setLatitude(lat);
//                    data.setLongitude(lon);
//                    data.setUuid(uuid);

                    data.setSpeed(getRandomSpeed());
                    data.setLatitude("1111");
                    data.setLongitude("2222");
                    data.setUuid("12345");
                    data.setText1("Dummy Response");
                    data.setText2("Speed: " + data.getSpeed());
                    data.setTts("Dummy Response");
                    data.setGps("@44.9550378,-92.9899811,17.5z");

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

                    data.setSpeed(getRandomSpeed());
                    data.setLatitude("99");
                    data.setLongitude("88");
                    data.setUuid("12345");
                    data.setText1("No Response");
                    data.setText2("Speed: " + data.getSpeed());
                    data.setTts("No Response");
                    data.setGps("@44.9550378,-92.9899811,17.5z");

                    listener.onAwsDataReady(data);

                    System.out.println("Fake AWS data");
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }

        });

    }

    private int getRandomSpeed(){
        Random rand = new Random();
        int randInt = rand.nextInt(15) + 25;
        return  randInt;
    }

}
