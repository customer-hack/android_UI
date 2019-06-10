package com.smartdevicelink.test.rpc.requests;

import com.smartdevicelink.marshal.JsonRPCMarshaller;
import com.smartdevicelink.protocol.enums.FunctionID;
import com.smartdevicelink.proxy.RPCMessage;
import com.smartdevicelink.proxy.rpc.HapticRect;
import com.smartdevicelink.proxy.rpc.SendHapticData;
import com.smartdevicelink.test.BaseRpcTests;
import com.smartdevicelink.test.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brettywhite on 8/9/17.
 */

public class SendHapticDataTests extends BaseRpcTests {

	private SendHapticData msg;

	@Override
	protected RPCMessage createMessage(){
		msg = new SendHapticData();

		List<HapticRect> list = new ArrayList<>();
		list.add(Test.GENERAL_HAPTIC_RECT);

		msg.setHapticRectData(list);

		return msg;
	}

	@Override
	protected String getMessageType(){
		return RPCMessage.KEY_REQUEST;
	}

	@Override
	protected String getCommandType(){
		return FunctionID.SEND_HAPTIC_DATA.toString();
	}

	@Override
	protected JSONObject getExpectedParameters(int sdlVersion){
		JSONObject result = new JSONObject();

		JSONArray jsonArray = new JSONArray();
		try {
			jsonArray.put(JsonRPCMarshaller.serializeHashtable(Test.GENERAL_HAPTIC_RECT.getStore()));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			result.put(SendHapticData.KEY_HAPTIC_RECT_DATA, jsonArray);
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
		List<HapticRect> list = msg.getHapticRectData();

		// Valid Tests
		assertEquals(Test.MATCH, Test.GENERAL_HAPTIC_RECT, list.get(0));

		// Invalid/Null Tests
		SendHapticData msg = new SendHapticData();
		assertNotNull(Test.NOT_NULL, msg);
		testNullBase(msg);

		assertNull(Test.NULL, msg.getHapticRectData());
	}

}
