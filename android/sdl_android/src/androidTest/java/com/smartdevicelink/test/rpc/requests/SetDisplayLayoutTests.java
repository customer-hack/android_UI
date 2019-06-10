package com.smartdevicelink.test.rpc.requests;

import com.smartdevicelink.marshal.JsonRPCMarshaller;
import com.smartdevicelink.protocol.enums.FunctionID;
import com.smartdevicelink.proxy.RPCMessage;
import com.smartdevicelink.proxy.rpc.SetDisplayLayout;
import com.smartdevicelink.proxy.rpc.TemplateColorScheme;
import com.smartdevicelink.test.BaseRpcTests;
import com.smartdevicelink.test.JsonUtils;
import com.smartdevicelink.test.Test;
import com.smartdevicelink.test.Validator;
import com.smartdevicelink.test.json.rpc.JsonFileReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;


/**
 * This is a unit test class for the SmartDeviceLink library project class : 
 * {@link com.smartdevicelink.rpc.SetDisplayLayout}
 */
public class SetDisplayLayoutTests extends BaseRpcTests {
	
	@Override
	protected RPCMessage createMessage() {
		SetDisplayLayout msg = new SetDisplayLayout();

		msg.setDisplayLayout(Test.GENERAL_STRING);
		msg.setDayColorScheme(Test.GENERAL_DAYCOLORSCHEME);
		msg.setNightColorScheme(Test.GENERAL_NIGHTCOLORSCHEME);

		return msg;
	}

	@Override
	protected String getMessageType() {
		return RPCMessage.KEY_REQUEST;
	}

	@Override
	protected String getCommandType() {
		return FunctionID.SET_DISPLAY_LAYOUT.toString();
	}

	@Override
	protected JSONObject getExpectedParameters(int sdlVersion) {
		JSONObject result = new JSONObject();

		try {
			result.put(SetDisplayLayout.KEY_DISPLAY_LAYOUT, Test.GENERAL_STRING);
			result.put(SetDisplayLayout.KEY_DAY_COLOR_SCHEME, Test.JSON_DAYCOLORSCHEME);
			result.put(SetDisplayLayout.KEY_NIGHT_COLOR_SCHEME, Test.JSON_NIGHTCOLORSCHEME);
		} catch (JSONException e) {
			fail(Test.JSON_FAIL);
		}

		return result;
	}

	/**
	 * Tests the expected values of the RPC message.
	 */
    public void testRpcValues () {   
    	// Test Values
		String testDisplayLayout = ( (SetDisplayLayout) msg ).getDisplayLayout();
		TemplateColorScheme testDayColorScheme = ( (SetDisplayLayout) msg).getDayColorScheme();
		TemplateColorScheme testNightColorScheme = ( (SetDisplayLayout) msg).getNightColorScheme();
		
		// Valid Tests
		assertEquals(Test.MATCH, Test.GENERAL_STRING, testDisplayLayout);
		assertTrue(Test.TRUE, Validator.validateTemplateColorScheme(Test.GENERAL_DAYCOLORSCHEME, testDayColorScheme));
		assertTrue(Test.TRUE, Validator.validateTemplateColorScheme(Test.GENERAL_NIGHTCOLORSCHEME, testNightColorScheme));
		
		// Invalid/Null Tests
		SetDisplayLayout msg = new SetDisplayLayout();
		assertNotNull(Test.NOT_NULL, msg);
		testNullBase(msg);

		assertNull(Test.NULL, msg.getDisplayLayout());
		assertNull(Test.NULL, msg.getDayColorScheme());
		assertNull(Test.NULL, msg.getNightColorScheme());
	}
	
	/**
     * Tests a valid JSON construction of this RPC message.
     */
    public void testJsonConstructor () {
    	JSONObject commandJson = JsonFileReader.readId(this.mContext, getCommandType(), getMessageType());
    	assertNotNull(Test.NOT_NULL, commandJson);
    	
		try {
			Hashtable<String, Object> hash = JsonRPCMarshaller.deserializeJSONObject(commandJson);
			SetDisplayLayout cmd = new SetDisplayLayout(hash);
			
			JSONObject body = JsonUtils.readJsonObjectFromJsonObject(commandJson, getMessageType());
			assertNotNull(Test.MATCH, body);
			
			// Test everything in the json body.
			assertEquals(Test.MATCH, JsonUtils.readStringFromJsonObject(body, RPCMessage.KEY_FUNCTION_NAME), cmd.getFunctionName());
			assertEquals(Test.MATCH, JsonUtils.readIntegerFromJsonObject(body, RPCMessage.KEY_CORRELATION_ID), cmd.getCorrelationID());

			JSONObject parameters = JsonUtils.readJsonObjectFromJsonObject(body, RPCMessage.KEY_PARAMETERS);
			assertEquals(Test.MATCH, JsonUtils.readStringFromJsonObject(parameters, SetDisplayLayout.KEY_DISPLAY_LAYOUT), cmd.getDisplayLayout());


			JSONObject dayColorSchemeObj = JsonUtils.readJsonObjectFromJsonObject(parameters, SetDisplayLayout.KEY_DAY_COLOR_SCHEME);
			TemplateColorScheme dayColorScheme = new TemplateColorScheme(JsonRPCMarshaller.deserializeJSONObject(dayColorSchemeObj));
			assertTrue(Test.TRUE,  Validator.validateTemplateColorScheme(dayColorScheme, cmd.getDayColorScheme()) );

			JSONObject nightColorSchemeObj = JsonUtils.readJsonObjectFromJsonObject(parameters, SetDisplayLayout.KEY_DAY_COLOR_SCHEME);
			TemplateColorScheme nightColorScheme = new TemplateColorScheme(JsonRPCMarshaller.deserializeJSONObject(nightColorSchemeObj));
			assertTrue(Test.TRUE,  Validator.validateTemplateColorScheme(nightColorScheme, cmd.getDayColorScheme()) );

			
		} catch (JSONException e) {
			fail(Test.JSON_FAIL);
		}    	
    }
}