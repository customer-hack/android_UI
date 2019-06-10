/*
 * Copyright (c) 2018 Livio, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of the Livio Inc. nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.smartdevicelink.test.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;

import com.smartdevicelink.AndroidTestCase2;
import com.smartdevicelink.R;
import com.smartdevicelink.util.SdlAppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Joey Grover on 2/20/18.
 */

public class SdlAppInfoTests extends AndroidTestCase2 {

    Context context;
    ResolveInfo defaultResolveInfo;
    ServiceInfo defaultServiceInfo;
    Bundle defaultBundle;
    PackageInfo defaultPackageInfo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getContext();
        defaultResolveInfo =  createResolveInfo(context.getResources().getInteger(R.integer.sdl_router_service_version_value), "com.smartdevicelink.test", "com.smartdevicelink.test.SdlRouterService",false);
        defaultServiceInfo = defaultResolveInfo.serviceInfo;
        defaultBundle = defaultServiceInfo.metaData;

        defaultPackageInfo = new PackageInfo();
        defaultPackageInfo.lastUpdateTime = System.currentTimeMillis() - 1000;
        defaultPackageInfo.firstInstallTime = System.currentTimeMillis() - 1000000;
    }

    public void testConstructorWithDefaultData(){

        SdlAppInfo info = new SdlAppInfo(defaultResolveInfo,defaultPackageInfo);

        assertNotNull(info);

        ComponentName  componentName = info.getRouterServiceComponentName();

        assertNotNull(componentName);

        assertEquals(defaultServiceInfo.name,componentName.getClassName());

        assertEquals(context.getResources().getInteger(R.integer.sdl_router_service_version_value),info.getRouterServiceVersion());

        assertFalse(info.isCustomRouterService());
    }

    /**
     * If an app is found to have a newer service the compare should put it at the top of the list
     */
    public void testCompareVersion(){
        SdlAppInfo defaultInfo = new SdlAppInfo(defaultResolveInfo,defaultPackageInfo);

        int newVersion = context.getResources().getInteger(R.integer.sdl_router_service_version_value) + 1;
        SdlAppInfo  testInfo = new SdlAppInfo(createResolveInfo(newVersion,"com.smartdevicelink.test2", "com.smartdevicelink.test2.SdlRouterService", false), defaultPackageInfo);

        List<SdlAppInfo> infos = new ArrayList<>();
        infos.add(defaultInfo);
        infos.add(testInfo);
        assertEquals(defaultInfo,infos.get(0));

        Collections.sort(infos, new SdlAppInfo.BestRouterComparator());

        assertEquals(testInfo, infos.get(0));

    }

    /**
     * Regardless of version, if the router service is custom it should be towards the end of the list
     */
    public void testCompareVersionAndCustom(){
        SdlAppInfo defaultInfo = new SdlAppInfo(defaultResolveInfo,defaultPackageInfo);

        int newVersion = context.getResources().getInteger(R.integer.sdl_router_service_version_value) + 1;
        SdlAppInfo  testInfo = new SdlAppInfo(createResolveInfo(newVersion,"com.smartdevicelink.test2", "com.smartdevicelink.test2.SdlRouterService",true ), defaultPackageInfo);

        List<SdlAppInfo> infos = new ArrayList<>();
        infos.add(defaultInfo);
        infos.add(testInfo);
        assertEquals(defaultInfo,infos.get(0));

        Collections.sort(infos, new SdlAppInfo.BestRouterComparator());

        assertEquals(defaultInfo, infos.get(0));

    }

    /**
     * If two services have the same version and are not custom, we need to check which app has been updated the most recently
     */
    public void testCompareUpdatedTime(){
        SdlAppInfo defaultInfo = new SdlAppInfo(defaultResolveInfo,defaultPackageInfo);

        PackageInfo packageInfo = new PackageInfo();
        packageInfo.firstInstallTime = defaultPackageInfo.firstInstallTime;
        packageInfo.lastUpdateTime = defaultPackageInfo.lastUpdateTime + 500;
        SdlAppInfo  testInfo = new SdlAppInfo(defaultResolveInfo, packageInfo);

        List<SdlAppInfo> infos = new ArrayList<>();
        infos.add(defaultInfo);
        infos.add(testInfo);
        assertEquals(defaultInfo,infos.get(0));

        Collections.sort(infos, new SdlAppInfo.BestRouterComparator());

        assertEquals(testInfo, infos.get(0));

    }

    public ResolveInfo  createResolveInfo(int routerServiceVersion,String packageName, String className, boolean isCustom){
        ResolveInfo info = new ResolveInfo();
        ServiceInfo serviceInfo = new ServiceInfo();
        defaultBundle = new Bundle();
        defaultBundle.putInt(context.getString(R.string.sdl_router_service_version_name),routerServiceVersion);
        defaultBundle.putBoolean(context.getString(R.string.sdl_router_service_is_custom_name), isCustom);
        serviceInfo.metaData = defaultBundle;
        serviceInfo.packageName = packageName;
        serviceInfo.name = className;
        info.serviceInfo = serviceInfo;

        return info;
    }


}
