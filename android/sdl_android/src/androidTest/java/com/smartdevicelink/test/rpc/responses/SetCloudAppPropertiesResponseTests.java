package com.smartdevicelink.test.rpc.responses;

import com.smartdevicelink.protocol.enums.FunctionID;
import com.smartdevicelink.proxy.RPCMessage;
import com.smartdevicelink.proxy.rpc.SetCloudAppPropertiesResponse;
import com.smartdevicelink.test.BaseRpcTests;
import com.smartdevicelink.test.Test;

import org.json.JSONObject;

public class SetCloudAppPropertiesResponseTests extends BaseRpcTests {
    @Override
    protected RPCMessage createMessage(){
        return new SetCloudAppPropertiesResponse();
    }

    @Override
    protected String getMessageType(){
        return RPCMessage.KEY_RESPONSE;
    }

    @Override
    protected String getCommandType(){
        return FunctionID.SET_CLOUD_APP_PROPERTIES.toString();
    }

    @Override
    protected JSONObject getExpectedParameters(int sdlVersion){
        return new JSONObject();
    }

    /**
     * Tests the expected values of the RPC message.
     */
    public void testRpcValues () {
        // Invalid/Null Tests
        SetCloudAppPropertiesResponse msg = new SetCloudAppPropertiesResponse();
        assertNotNull(Test.NOT_NULL, msg);
        testNullBase(msg);
    }

}
