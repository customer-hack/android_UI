package server;

import java.io.IOException;
import fi.iki.elonen.NanoHTTPD;

public class MyServer extends NanoHTTPD {
    private final static int PORT = 8080;

    public MyServer() {
        super(PORT);
//        start();
        System.out.println( "\nRunning! Point your browsers to http://localhost:8080/ \n" );
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        msg += "<p>We serve " + session.getUri() + " !</p>";
        return newFixedLengthResponse( msg + "</body></html>\n" );
    }
}
