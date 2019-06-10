package com.smartdevicelink.test.rpc.responses;

import com.smartdevicelink.marshal.JsonRPCMarshaller;
import com.smartdevicelink.protocol.enums.FunctionID;
import com.smartdevicelink.proxy.RPCMessage;
import com.smartdevicelink.proxy.rpc.PerformAppServiceInteractionResponse;
import com.smartdevicelink.test.BaseRpcTests;
import com.smartdevicelink.test.JsonUtils;
import com.smartdevicelink.test.Test;
import com.smartdevicelink.test.json.rpc.JsonFileReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

/**
 * This is a unit test class for the SmartDeviceLink library project class :
 * {@link com.smartdevicelink.proxy.rpc.PerformAppServiceInteractionResponse}
 */
public class PerformAppServiceInteractionResponseTests extends BaseRpcTests {

	@Override
	protected RPCMessage createMessage(){

		PerformAppServiceInteractionResponse msg = new PerformAppServiceInteractionResponse();

		msg.setServiceSpecificResult(Test.GENERAL_STRING);

		return msg;
	}

	@Override
	protected String getMessageType(){
		return RPCMessage.KEY_RESPONSE;
	}

	@Override
	protected String getCommandType(){
		return FunctionID.PERFORM_APP_SERVICES_INTERACTION.toString();
	}

	@Override
	protected JSONObject getExpectedParameters(int sdlVersion){
		JSONObject result = new JSONObject();

		try{
			result.put(PerformAppServiceInteractionResponse.KEY_SERVICE_SPECIFIC_RESULT, Test.GENERAL_STRING);
		}catch(JSONException e){
			fail(Test.JSON_FAIL);
		}

		return result;
	}

	/**
	 * Tests the expected values of the RPC message.
	 */
	public void testRpcValues () {
		// Test Values
		String serviceSpecificResult = ( (PerformAppServiceInteractionResponse) msg ).getServiceSpecificResult();

		// Valid Tests
		assertEquals(Test.MATCH, Test.GENERAL_STRING, serviceSpecificResult);

		// Invalid/Null Tests
		PerformAppServiceInteractionResponse msg = new PerformAppServiceInteractionResponse();
		assertNotNull(Test.NOT_NULL, msg);
		testNullBase(msg);

		assertNull(Test.NULL, msg.getServiceSpecificResult());
	}

	/**
	 * Tests a valid JSON construction of this RPC message.
	 */
	public void testJsonConstructor () {
		JSONObject commandJson = JsonFileReader.readId(this.mContext, getCommandType(), getMessageType());
		assertNotNull(Test.NOT_NULL, commandJson);

		try {
			Hashtable<String, Object> hash = JsonRPCMarshaller.deserializeJSONObject(commandJson);
			PerformAppServiceInteractionResponse cmd = new PerformAppServiceInteractionResponse (hash);

			JSONObject body = JsonUtils.readJsonObjectFromJsonObject(commandJson, getMessageType());
			assertNotNull(Test.NOT_NULL, body);

			// Test everything in the json body.
			assertEquals(Test.MATCH, JsonUtils.readStringFromJsonObject(body, RPCMessage.KEY_FUNCTION_NAME), cmd.getFunctionName());
			assertEquals(Test.MATCH, JsonUtils.readIntegerFromJsonObject(body, RPCMessage.KEY_CORRELATION_ID), cmd.getCorrelationID());

			JSONObject parameters = JsonUtils.readJsonObjectFromJsonObject(body, RPCMessage.KEY_PARAMETERS);

			assertEquals(Test.MATCH, JsonUtils.readStringFromJsonObject(parameters, PerformAppServiceInteractionResponse.KEY_SERVICE_SPECIFIC_RESULT), cmd.getServiceSpecificResult());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
