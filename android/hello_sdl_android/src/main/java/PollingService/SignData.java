package PollingService;

public class SignData {
    private String _latitude = "";
    private String _longitude = "";
    private String _uuid = "";
    private String _text1 = "";
    private String _text2 = "";
    private String _tts = "";
    private int _speed = -1;
    private String _gps = "";

    public void setLatitude(String latitude)
    {
        _latitude = latitude;
    }
    public String getLatitude() {
        return _latitude;
    }

    public void setLongitude(String longitude) {
        _longitude = longitude;
    }
    public String getLongitude() {
        return _longitude;
    }

    public void setUuid(String uuid) {
        _uuid = uuid;
    }
    public String getUuid() {
        return _uuid;
    }

    public void setText1(String text1) {
        _text1 = text1;
    }
    public String getText1() {
        return _text1;
    }

    public void setText2(String text2) {
        _text2 = text2;
    }
    public String getText2() {
        return _text2;
    }

    public void setTts(String tts) {
        _tts = tts;
    }
    public String getTts() {
        return _tts;
    }

    public void setSpeed(int speed) {
        _speed = speed;
    }
    public int getSpeed() {
        return _speed;
    }

    public void setGps(String gps) {
        _gps = gps;
    }
    public String getGps() {
        return _gps;
    }
}
