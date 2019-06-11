package PollingService;

public class SignData {
    private String _latitude = "";
    private String _longitude = "";
    private String _uuid = "";

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
}
