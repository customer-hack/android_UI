package com.smartdevicelink.test.rpc.responses;

import com.smartdevicelink.marshal.JsonRPCMarshaller;
import com.smartdevicelink.protocol.enums.FunctionID;
import com.smartdevicelink.proxy.RPCMessage;
import com.smartdevicelink.proxy.rpc.GetWayPointsResponse;
import com.smartdevicelink.proxy.rpc.LocationDetails;
import com.smartdevicelink.test.BaseRpcTests;
import com.smartdevicelink.test.JsonUtils;
import com.smartdevicelink.test.Test;
import com.smartdevicelink.test.json.rpc.JsonFileReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by austinkirk on 6/6/17.
 */

public class GetWayPointsResponseTests extends BaseRpcTests {
    List<LocationDetails> waypoints = new ArrayList<LocationDetails>();

    @Override
    protected RPCMessage createMessage() {

        waypoints.add(Test.GENERAL_LOCATIONDETAILS);
        waypoints.add(Test.GENERAL_LOCATIONDETAILS);

        GetWayPointsResponse getWayPointsResponse = new GetWayPointsResponse();
        getWayPointsResponse.setWayPoints(waypoints);

        return getWayPointsResponse;
    }

    @Override
    protected String getMessageType() {
        return RPCMessage.KEY_RESPONSE;
    }

    @Override
    protected String getCommandType() {
        return FunctionID.GET_WAY_POINTS.toString();
    }

    @Override
    protected JSONObject getExpectedParameters(int sdlVersion) {
        JSONObject result = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray.put(JsonRPCMarshaller.serializeHashtable(Test.GENERAL_LOCATIONDETAILS.getStore()));
            jsonArray.put(JsonRPCMarshaller.serializeHashtable(Test.GENERAL_LOCATIONDETAILS.getStore()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            result.put(GetWayPointsResponse.KEY_WAY_POINTS, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Tests the expected values of the RPC message.
     */
    public void testRpcValues () {

        // Test Values
        List<LocationDetails> testWPs = ( (GetWayPointsResponse) msg ).getWayPoints();

        // Valid Tests
        assertEquals(Test.MATCH, waypoints, testWPs);

        // Invalid/Null Tests
        GetWayPointsResponse msg = new GetWayPointsResponse();
        assertNotNull(Test.NOT_NULL, msg);
        testNullBase(msg);

        assertNull(Test.NULL, msg.getWayPoints());
    }

    /**
     * Tests a valid JSON construction of this RPC message.
     */
    public void testJsonConstructor () {
        JSONObject commandJson = JsonFileReader.readId(this.mContext, getCommandType(), getMessageType());
        assertNotNull(Test.NOT_NULL, commandJson);

        try {
            Hashtable<String, Object> hash = JsonRPCMarshaller.deserializeJSONObject(commandJson);
            GetWayPointsResponse cmd = new GetWayPointsResponse(hash);

            JSONObject body = JsonUtils.readJsonObjectFromJsonObject(commandJson, getMessageType());
            assertNotNull(Test.NOT_NULL, body);

            // Test everything in the json body.
            assertEquals(Test.MATCH, JsonUtils.readStringFromJsonObject(body, RPCMessage.KEY_FUNCTION_NAME), cmd.getFunctionName());
            assertEquals(Test.MATCH, JsonUtils.readIntegerFromJsonObject(body, RPCMessage.KEY_CORRELATION_ID), cmd.getCorrelationID());

            JSONObject parameters = JsonUtils.readJsonObjectFromJsonObject(body, RPCMessage.KEY_PARAMETERS);

            JSONArray locArray = JsonUtils.readJsonArrayFromJsonObject(parameters, GetWayPointsResponse.KEY_WAY_POINTS);
            List<LocationDetails> locationList = new ArrayList<LocationDetails>();
            for (int index = 0; index < locArray.length(); index++) {
                LocationDetails det = new LocationDetails(JsonRPCMarshaller.deserializeJSONObject( (JSONObject) locArray.get(index)));
                locationList.add(det);
            }
            List<LocationDetails> dets = cmd.getWayPoints();
            assertEquals(Test.MATCH,  locationList.size(), dets.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
