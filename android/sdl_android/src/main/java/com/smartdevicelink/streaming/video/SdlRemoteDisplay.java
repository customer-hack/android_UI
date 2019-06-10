/*
 * Copyright (c) 2017 Livio, Inc.
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

package com.smartdevicelink.streaming.video;

import android.annotation.TargetApi;
import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;

/**
 * SdlRemoteDisplay is an abstract class that should be extended by developers to creat their remote displays.
 * All logic for UI events can be stored in their extension.
 *
 * <br><br> <b>NOTE:</b> When the UI changes (buttons appear, layouts change, etc) the developer should call {@link #invalidate()} to alert any
 * other interfaces that are listening for those types of events.
 */
@TargetApi(17)
public abstract class SdlRemoteDisplay extends Presentation {
    private static final String TAG = "SdlRemoteDisplay";
    private static final int REFRESH_RATE_MS = 50;

    protected Window w;
    protected View mainView;
    protected Handler handler = new Handler();
    protected Handler uiHandler = new Handler(Looper.getMainLooper());
    protected Callback callback;

    public SdlRemoteDisplay(Context context, Display display) {
        super(context, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle(TAG);

        w  = getWindow();

        startRefreshTask();

        w.setType(WindowManager.LayoutParams.TYPE_PRIVATE_PRESENTATION);
    }

    protected void startRefreshTask() {
        handler.postDelayed(mStartRefreshTaskCallback, REFRESH_RATE_MS);
    }

    protected void stopRefreshTask() {
        handler.removeCallbacks(mStartRefreshTaskCallback);
    }

    protected Runnable mStartRefreshTaskCallback = new Runnable() {
        public void run() {
            if(mainView == null){
                mainView = w.getDecorView().findViewById(android.R.id.content);
            }
            if (mainView != null) {
                mainView.invalidate();
            }

            handler.postDelayed(this, REFRESH_RATE_MS);
        }
    };

    @SuppressWarnings("unused")
    public View getMainView(){
        if(mainView == null){
            mainView = w.getDecorView().findViewById(android.R.id.content);
        }
        return this.mainView;
    }

    @SuppressWarnings("unused")
    public void invalidate(){
        // let listeners know the view has been invalidated
        if(callback != null){
            callback.onInvalidated(this);
        }
    }

    public void handleMotionEvent(final MotionEvent motionEvent){
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                mainView.dispatchTouchEvent(motionEvent);
            }
        });
    }

    public void stop(){
        stopRefreshTask();
        dismissPresentation();
    }

    public void dismissPresentation() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        });
    }
    public interface Callback{
        void onCreated(SdlRemoteDisplay remoteDisplay);
        void onInvalidated(SdlRemoteDisplay remoteDisplay);
    }

    public static class Creator implements Callable<Boolean> {
        private Context context;
        private Display mDisplay;
        boolean presentationShowError = false;
        SdlRemoteDisplay remoteDisplay;
        Class<? extends SdlRemoteDisplay> remoteDisplayClass;
        private Handler uiHandler = new Handler(Looper.getMainLooper());
        private Callback callback;


        public Creator(Context context, Display display, SdlRemoteDisplay remoteDisplay, Class<? extends SdlRemoteDisplay> remoteDisplayClass, Callback callback){
            this.context = context;
            this.mDisplay = display;
            this.remoteDisplay = remoteDisplay;
            this.remoteDisplayClass = remoteDisplayClass;
            this.callback = callback;
        }

        @Override
        public Boolean call() {

            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Want to create presentation on UI thread so it finds the right Looper
                    // when setting up the Dialog.
                    if ((remoteDisplay == null) && (mDisplay != null))
                    {
                        try {
                            Constructor constructor = remoteDisplayClass.getConstructor(Context.class, Display.class);
                            remoteDisplay = (SdlRemoteDisplay) constructor.newInstance(context, mDisplay);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "Unable to create Presentation Class");
                            presentationShowError = true;
                            return;
                        }

                        try {
                            remoteDisplay.show();
                            remoteDisplay.callback = callback;
                            if(callback!=null){
                                callback.onCreated(remoteDisplay);
                            }

                        } catch (WindowManager.InvalidDisplayException ex) {
                            Log.e(TAG, "Couldn't show presentation! Display was removed in the meantime.", ex);
                            remoteDisplay = null;
                            presentationShowError = true;
                        }
                    }
                }
            });

            return presentationShowError;
        }

    }
}
