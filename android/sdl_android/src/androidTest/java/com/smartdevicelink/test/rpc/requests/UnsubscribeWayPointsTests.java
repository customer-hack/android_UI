package com.smartdevicelink.test.rpc.requests;

import com.smartdevicelink.marshal.JsonRPCMarshaller;
import com.smartdevicelink.protocol.enums.FunctionID;
import com.smartdevicelink.proxy.RPCMessage;
import com.smartdevicelink.proxy.rpc.UnsubscribeWayPoints;
import com.smartdevicelink.test.BaseRpcTests;
import com.smartdevicelink.test.JsonUtils;
import com.smartdevicelink.test.Test;
import com.smartdevicelink.test.json.rpc.JsonFileReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

/**
 * Created by austinkirk on 6/7/17.
 */

public class UnsubscribeWayPointsTests extends BaseRpcTests {
    @Override
    protected RPCMessage createMessage() {
        return new UnsubscribeWayPoints();
    }

    @Override
    protected String getMessageType() {
        return RPCMessage.KEY_REQUEST;
    }

    @Override
    protected String getCommandType() {
        return FunctionID.UNSUBSCRIBE_WAY_POINTS.toString();
    }

    @Override
    protected JSONObject getExpectedParameters(int sdlVersion) {
        return new JSONObject();
    }

    /**
     * Tests the expected values of the RPC message.
     */
    public void testRpcValues () {
        // Invalid/Null Tests
        UnsubscribeWayPoints msg = new UnsubscribeWayPoints();
        assertNotNull(Test.NOT_NULL, msg);
        testNullBase(msg);
    }

    /**
     * Tests a valid JSON construction of this RPC message.
     */
    public void testJsonConstructor () {
        JSONObject commandJson = JsonFileReader.readId(this.mContext, getCommandType(), getMessageType());
        assertNotNull(Test.NOT_NULL, commandJson);

        try {
            Hashtable<String, Object> hash = JsonRPCMarshaller.deserializeJSONObject(commandJson);
            UnsubscribeWayPoints cmd = new UnsubscribeWayPoints(hash);

            JSONObject body = JsonUtils.readJsonObjectFromJsonObject(commandJson, getMessageType());
            assertNotNull(Test.NOT_NULL, body);

            // Test everything in the json body.
            assertEquals(Test.MATCH, JsonUtils.readStringFromJsonObject(body, RPCMessage.KEY_FUNCTION_NAME), cmd.getFunctionName());
            assertEquals(Test.MATCH, JsonUtils.readIntegerFromJsonObject(body, RPCMessage.KEY_CORRELATION_ID), cmd.getCorrelationID());
        } catch (JSONException e) {
            fail(Test.JSON_FAIL);
        }
    }
}
