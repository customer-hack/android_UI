package com.smartdevicelink.test.rpc.enums;

import com.smartdevicelink.proxy.rpc.enums.AudioType;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a unit test class for the SmartDeviceLink library project class : 
 * {@link com.smartdevicelink.rpc.enums.AudioType}
 */
public class AudioTypeTests extends TestCase {

	/**
	 * Verifies that the enum values are not null upon valid assignment.
	 */
	public void testValidEnums () {	
		String example = "PCM";
		AudioType enumPcm = AudioType.valueForString(example);
		
		assertNotNull("PCM returned null", enumPcm);
	}

	/**
	 * Verifies that an invalid assignment is null.
	 */
	public void testInvalidEnum () {
		String example = "pCM";
		try {
		    AudioType temp = AudioType.valueForString(example);
            assertNull("Result of valueForString should be null.", temp);
		}
		catch (IllegalArgumentException exception) {
            fail("Invalid enum throws IllegalArgumentException.");
		}
	}

	/**
	 * Verifies that a null assignment is invalid.
	 */
	public void testNullEnum () {
		String example = null;
		try {
		    AudioType temp = AudioType.valueForString(example);
            assertNull("Result of valueForString should be null.", temp);
		}
		catch (NullPointerException exception) {
            fail("Null string throws NullPointerException.");
		}
	}	
	

	/**
	 * Verifies the possible enum values of AudioType.
	 */
	public void testListEnum() {
 		List<AudioType> enumValueList = Arrays.asList(AudioType.values());

		List<AudioType> enumTestList = new ArrayList<AudioType>();
		enumTestList.add(AudioType.PCM);

		assertTrue("Enum value list does not match enum class list", 
				enumValueList.containsAll(enumTestList) && enumTestList.containsAll(enumValueList));
	}	
}