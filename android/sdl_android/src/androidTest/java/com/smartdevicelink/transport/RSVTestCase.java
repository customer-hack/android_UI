package com.smartdevicelink.transport;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.smartdevicelink.AndroidTestCase2;
import com.smartdevicelink.transport.RouterServiceValidator.TrustedAppStore;
import com.smartdevicelink.util.HttpRequestTask.HttpRequestTaskCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Semaphore;

public class RSVTestCase extends AndroidTestCase2 {
	private static final String TAG = "RSVTestCase";
	
	private static final long REFRESH_TRUSTED_APP_LIST_TIME_DAY 	= 3600000 * 24; // A day in ms
	private static final long REFRESH_TRUSTED_APP_LIST_TIME_WEEK 	= REFRESH_TRUSTED_APP_LIST_TIME_DAY * 7; // A week in ms
	private static final long REFRESH_TRUSTED_APP_LIST_TIME_MONTH 	= REFRESH_TRUSTED_APP_LIST_TIME_DAY * 30; // A ~month in ms
	private static final String TEST =  "{\"response\": {\"com.livio.sdl\" : { \"versionBlacklist\":[] }, \"com.lexus.tcapp\" : { \"versionBlacklist\":[] }, \"com.toyota.tcapp\" : { \"versionBlacklist\": [] } , \"com.sdl.router\":{\"versionBlacklist\": [] },\"com.ford.fordpass\" : { \"versionBlacklist\":[] } }}";
	RouterServiceValidator rsvp;
	/**
	 * Set this boolean if you want to test the actual validation of router service
	 */
	boolean liveTest = false;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		rsvp = new RouterServiceValidator(this.mContext);
		
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private static final Semaphore TRUSTED_LIST_LOCK = new Semaphore(1);

	private void requestTListLock(){
		try {
			TRUSTED_LIST_LOCK.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void releaseTListLock(){
		TRUSTED_LIST_LOCK.release();
	}

	private RouterServiceValidator.TrustedListCallback trustedListCallback = new RouterServiceValidator.TrustedListCallback(){
		@Override
		public void onListObtained(boolean successful) {
			releaseTListLock();
		}
	};
	
/*
 * These tests are a little strange because they don't test the logic behind the validation of each piece.
 * However, they allow us to test
 */
	
	public void testInstalledFrom(){
		if(liveTest){
			rsvp.setFlags(RouterServiceValidator.FLAG_DEBUG_INSTALLED_FROM_CHECK);
			assertTrue(rsvp.validate());
		}
	}
	
	public void testPackageCheck(){
		if(liveTest){
			rsvp.setFlags(RouterServiceValidator.FLAG_DEBUG_PACKAGE_CHECK);
			assertTrue(rsvp.validate());
		}
	}
	
	public void testVersionCheck(){
		if(liveTest){
			rsvp.setFlags(RouterServiceValidator.FLAG_DEBUG_VERSION_CHECK);
			assertTrue(rsvp.validate());
		}
	}
	
	public void testNoFlags(){
		if(liveTest){
			rsvp.setFlags(RouterServiceValidator.FLAG_DEBUG_NONE);
			assertTrue(rsvp.validate());
		}
	}
	
	public void testAllFlags(){
		if(liveTest){
			rsvp.setFlags(RouterServiceValidator.FLAG_DEBUG_PERFORM_ALL_CHECKS);
			assertTrue(rsvp.validate());
		}
	}
	
	public void testSecuritySetting(){
		
		RouterServiceValidator rsvp = new RouterServiceValidator(this.mContext); //Use a locally scoped instance
		rsvp.setSecurityLevel(MultiplexTransportConfig.FLAG_MULTI_SECURITY_HIGH);
		
		try{
			Field securityLevelField =  RouterServiceValidator.class.getDeclaredField("securityLevel");
			securityLevelField.setAccessible(true);
			assertEquals(securityLevelField.get(rsvp),MultiplexTransportConfig.FLAG_MULTI_SECURITY_HIGH);
		}catch(NoSuchFieldException e1){
			fail(e1.getMessage());
		}catch( IllegalAccessException e2){
			fail(e2.getMessage());
		}
		assertEquals(RouterServiceValidator.getSecurityLevel(mContext), MultiplexTransportConfig.FLAG_MULTI_SECURITY_HIGH);
	}
	
	public void testHighSecurity(){
		requestTListLock();

		RouterServiceValidator rsvp = new RouterServiceValidator(this.mContext); //Use a locally scoped instance
		rsvp.setSecurityLevel(MultiplexTransportConfig.FLAG_MULTI_SECURITY_HIGH);
		rsvp.setFlags(RouterServiceValidator.FLAG_DEBUG_INSTALLED_FROM_CHECK);
		
		assertTrue(checkShouldOverrideInstalledFrom(rsvp,false));
		
		assertEquals(RouterServiceValidator.getRefreshRate(), REFRESH_TRUSTED_APP_LIST_TIME_WEEK);
		
		assertTrue(RouterServiceValidator.createTrustedListRequest(mContext, true, null, trustedListCallback));
		
	}
	
	public void testMediumSecurity(){
		requestTListLock();

		RouterServiceValidator rsvp = new RouterServiceValidator(this.mContext); //Use a locally scoped instance
		rsvp.setSecurityLevel(MultiplexTransportConfig.FLAG_MULTI_SECURITY_MED);
		rsvp.setFlags(RouterServiceValidator.FLAG_DEBUG_INSTALLED_FROM_CHECK);
		
		assertTrue(checkShouldOverrideInstalledFrom(rsvp,true));
		
		assertEquals(RouterServiceValidator.getRefreshRate(), REFRESH_TRUSTED_APP_LIST_TIME_WEEK);
		
		assertTrue(RouterServiceValidator.createTrustedListRequest(mContext, true, null, trustedListCallback));
		
	}
	
	public void testLowSecurity(){
		requestTListLock();

		RouterServiceValidator rsvp = new RouterServiceValidator(this.mContext); //Use a locally scoped instance
		rsvp.setSecurityLevel(MultiplexTransportConfig.FLAG_MULTI_SECURITY_LOW);
		rsvp.setFlags(RouterServiceValidator.FLAG_DEBUG_INSTALLED_FROM_CHECK);
		
		assertTrue(checkShouldOverrideInstalledFrom(rsvp,true));
		
		assertEquals(RouterServiceValidator.getRefreshRate(), REFRESH_TRUSTED_APP_LIST_TIME_MONTH);
		
		assertTrue(RouterServiceValidator.createTrustedListRequest(mContext, true, null, trustedListCallback));
		
	}
	
	public void testNoSecurity(){
		requestTListLock();

		RouterServiceValidator rsvp = new RouterServiceValidator(this.mContext); //Use a locally scoped instance
		rsvp.setSecurityLevel(MultiplexTransportConfig.FLAG_MULTI_SECURITY_OFF);
		rsvp.setFlags(RouterServiceValidator.FLAG_DEBUG_INSTALLED_FROM_CHECK);
		
		assertTrue(checkShouldOverrideInstalledFrom(rsvp,true));
		
		assertEquals(RouterServiceValidator.getRefreshRate(), REFRESH_TRUSTED_APP_LIST_TIME_WEEK);
		
		assertFalse(RouterServiceValidator.createTrustedListRequest(mContext, true, null, trustedListCallback));
		
		//This should always return true
		assertTrue(rsvp.validate());
		
	}
	
	public boolean checkShouldOverrideInstalledFrom(RouterServiceValidator rsvp, boolean shouldOverride){
		try{
			Method shouldOverrideInstalledFrom = RouterServiceValidator.class.getDeclaredMethod("shouldOverrideInstalledFrom");
			shouldOverrideInstalledFrom.setAccessible(true);
			boolean should = (Boolean)shouldOverrideInstalledFrom.invoke(rsvp);
			
			return shouldOverride == should;
		
		}catch(NoSuchMethodException e1){
			fail(e1.getMessage());
		}catch( IllegalAccessException e2){
			fail(e2.getMessage());
		}catch( InvocationTargetException e3){
			fail(e3.getMessage());
		}
		return false;
	}
	
	public void testJsonRecovery(){
		assertNotNull(rsvp.stringToJson(null));
		assertNotNull(rsvp.stringToJson("asdf235vq32{]]"));

	}
	
	public void testInvalidateList(){
		requestTListLock();

		assertFalse(RouterServiceValidator.invalidateList(null));
		assertTrue(RouterServiceValidator.invalidateList(mContext));

		releaseTListLock();
	}
	
	public void testGetTrustedList(){
		requestTListLock();

		assertNull(RouterServiceValidator.getTrustedList(null));
		assertNotNull(RouterServiceValidator.getTrustedList(mContext));

		releaseTListLock();
	}
	
	public void testSetTrustedList(){
		requestTListLock();

		assertFalse(RouterServiceValidator.setTrustedList(null,null));
		assertFalse(RouterServiceValidator.setTrustedList(mContext,null));
		assertFalse(RouterServiceValidator.setTrustedList(null,"test"));
		assertTrue(RouterServiceValidator.setTrustedList(mContext,"test"));
		assertTrue(RouterServiceValidator.setTrustedList(mContext,TEST));
		assertTrue(RouterServiceValidator.setTrustedList(mContext,TEST+TEST+TEST+TEST+TEST));
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i<1000; i++){
			builder.append(TEST);
		}
		assertTrue(RouterServiceValidator.setTrustedList(mContext,builder.toString()));

		releaseTListLock();
	}
	
	public void testTrustedListSetAndGet(){
		requestTListLock();

		assertTrue(RouterServiceValidator.setTrustedList(mContext,TEST));
		String retVal = RouterServiceValidator.getTrustedList(mContext);
		assertNotNull(retVal);
		assertTrue(TEST.equals(retVal));

		StringBuilder builder = new StringBuilder();
		for(int i = 0; i<1000; i++){
			builder.append(TEST);
		}
		assertTrue(RouterServiceValidator.setTrustedList(mContext,builder.toString()));
		retVal = RouterServiceValidator.getTrustedList(mContext);
		assertNotNull(retVal);
		assertTrue(builder.toString().equals(retVal));

		releaseTListLock();
	}
	
	public void testInvalidationSequence(){
		requestTListLock();

		assertTrue(RouterServiceValidator.invalidateList(mContext));
		assertTrue(RouterServiceValidator.createTrustedListRequest(mContext, false, null, trustedListCallback));
	}
	
	public void testAppStorePackages(){
		assertTrue(TrustedAppStore.isTrustedStore(TrustedAppStore.PLAY_STORE.packageString));
		assertTrue(TrustedAppStore.isTrustedStore("com.xiaomi.market"));
		assertFalse(TrustedAppStore.isTrustedStore("test"));
		assertFalse(TrustedAppStore.isTrustedStore(null));
		
		rsvp = new RouterServiceValidator(this.mContext);
		rsvp.setFlags(RouterServiceValidator.FLAG_DEBUG_INSTALLED_FROM_CHECK);
		rsvp.setSecurityLevel(MultiplexTransportConfig.FLAG_MULTI_SECURITY_HIGH);
		
		PackageManager packageManager = mContext.getPackageManager();
		List<PackageInfo> packages = packageManager.getInstalledPackages(0);
		String appStore;
		for(PackageInfo info: packages){
			appStore = packageManager.getInstallerPackageName(info.packageName);
			if(TrustedAppStore.isTrustedStore(appStore)){
				assertTrue(rsvp.wasInstalledByAppStore(info.packageName));
			}
		}
		
		assertFalse(rsvp.wasInstalledByAppStore(null));
	}
	
	public void testVersionBlackList(){
		rsvp = new RouterServiceValidator(this.mContext);
		JSONArray array = new JSONArray();
		for(int i=0; i<25; i++){
			if(i%3 == 0){
				array.put(i);
			}
		}
		assertTrue(rsvp.verifyVersion(1, null));
		assertTrue(rsvp.verifyVersion(1, array));
		assertTrue(rsvp.verifyVersion(100, array));
		assertFalse(rsvp.verifyVersion(3, array));
		assertFalse(rsvp.verifyVersion(-3, array));

	}
	
	static boolean didFinish = false;
	public void  testGetAndCheckList(){
		requestTListLock();

		final Object REQUEST_LOCK = new Object();
		didFinish = false;
		HttpRequestTaskCallback cb = new HttpRequestTaskCallback(){
			
			@Override
			public void httpCallComplete(String response) {
				//Might want to check if this list is ok
				Log.d(TAG, "APPS! " + response);
				synchronized(REQUEST_LOCK){
					didFinish = true;
					REQUEST_LOCK.notify();
				}
				releaseTListLock();
			}
			@Override
			public void httpFailure(int statusCode) {
				Log.e(TAG, "Error while requesting trusted app list: " + statusCode);
				synchronized(REQUEST_LOCK){
					didFinish = true;
					REQUEST_LOCK.notify();
				}
				releaseTListLock();
			}
		};
		
		assertTrue(RouterServiceValidator.createTrustedListRequest(mContext,true, cb));
		//Now wait for call to finish
		synchronized(REQUEST_LOCK){
			try {
				REQUEST_LOCK.wait();
				assertTrue(didFinish);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	/**
	 * Test to check that we can save our last request which actually houses all the previous known sdl enabled apps
	 */
	public void testRequestChange(){
		requestTListLock();

		RouterServiceValidator.setLastRequest(mContext, null);
		assertNull(RouterServiceValidator.getLastRequest(mContext));

		JSONObject object = null;
		try {
			object = new JSONObject(TEST);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		assertNotNull(object);
		assertFalse(object.equals(RouterServiceValidator.getLastRequest(mContext)));
		
		assertTrue(RouterServiceValidator.setLastRequest(mContext, object.toString()));
		
		String oldRequest = RouterServiceValidator.getLastRequest(mContext);
		assertNotNull(oldRequest);
		assertTrue(object.toString().equals(oldRequest));
		
		//Now test a new list
		String test = "{\"response\": {\"com.livio.sdl\" : { \"versionBlacklist\":[] }, \"com.lexus.tcapp\" : { \"versionBlacklist\":[] }, \"com.test.test\" : { \"versionBlacklist\":[] },\"com.toyota.tcapp\" : { \"versionBlacklist\": [] } , \"com.sdl.router\":{\"versionBlacklist\": [] },\"com.ford.fordpass\" : { \"versionBlacklist\":[] } }}";
		object = null;
		try {
			object = new JSONObject(test);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		assertNotNull(object);
		assertFalse(object.equals(RouterServiceValidator.getLastRequest(mContext)));
		//Clear it for next test
		RouterServiceValidator.setLastRequest(mContext, null);

		releaseTListLock();
	}

	/**
	 * Test app's router validation. Validation should fail when the given context and ComponentName object are from different packages and security setting is not OFF
	 * and app is not on trusted list. Validation should pass when the given context and ComponentName object are from the same package.
	 */
	public void testAppSelfValidation() {

		class RouterServiceValidatorTest extends RouterServiceValidator{
			public RouterServiceValidatorTest(Context context){
				super(context);
			}

			public RouterServiceValidatorTest(Context context, ComponentName service){
				super(context, service);
			}

			// Override this method and simply returning true for the purpose of this test
			protected boolean isServiceRunning(Context context, ComponentName service){
				return true;
			}
		}

		// Fail, different package name for context and service and app security setting is not OFF and app is not on trusted list
		RouterServiceValidatorTest rsvpFail = new RouterServiceValidatorTest(this.mContext, new ComponentName("anything", mContext.getClass().getSimpleName()));
		rsvpFail.setSecurityLevel(MultiplexTransportConfig.FLAG_MULTI_SECURITY_HIGH);
		assertFalse(rsvpFail.validate());

		// Success, same package name for context and service
		RouterServiceValidatorTest rsvpPass = new RouterServiceValidatorTest(this.mContext, new ComponentName(mContext.getPackageName(), mContext.getClass().getSimpleName()));
		rsvpPass.setSecurityLevel(MultiplexTransportConfig.FLAG_MULTI_SECURITY_HIGH);
		assertTrue(rsvpPass.validate());
	}

	 
}
