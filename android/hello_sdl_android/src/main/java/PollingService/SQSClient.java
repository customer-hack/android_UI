package PollingService;

import com.loopj.android.http.*;


public class SQSClient {
    private static final String BASE_URL = "https://sqs.us-east-2.amazonaws.com/371900921998/camera_queue.fifo";
    private static final String BASE_URL_UPDATE = "https://sqs.us-east-2.amazonaws.com/371900921998/camera_queue.fifo2";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, TextHttpResponseHandler responseHandler) {
        client.get(BASE_URL, params, responseHandler);
    }

    public static void post(String url, RequestParams params, TextHttpResponseHandler responseHandler) {
        client.post(BASE_URL_UPDATE, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
