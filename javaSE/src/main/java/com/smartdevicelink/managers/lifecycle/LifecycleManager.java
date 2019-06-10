/*
 * Copyright (c) 2019 Livio, Inc.
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

package com.smartdevicelink.managers.lifecycle;

import android.support.annotation.RestrictTo;
import android.util.Log;
import com.smartdevicelink.SdlConnection.ISdlConnectionListener;
import com.smartdevicelink.SdlConnection.SdlSession;
import com.smartdevicelink.exception.SdlException;
import com.smartdevicelink.managers.SdlManager;
import com.smartdevicelink.marshal.JsonRPCMarshaller;
import com.smartdevicelink.protocol.ProtocolMessage;
import com.smartdevicelink.protocol.enums.FunctionID;
import com.smartdevicelink.protocol.enums.MessageType;
import com.smartdevicelink.protocol.enums.SessionType;
import com.smartdevicelink.proxy.*;
import com.smartdevicelink.proxy.interfaces.*;
import com.smartdevicelink.proxy.rpc.*;
import com.smartdevicelink.proxy.rpc.enums.*;
import com.smartdevicelink.proxy.rpc.listeners.*;
import com.smartdevicelink.security.SdlSecurityBase;
import com.smartdevicelink.streaming.audio.AudioStreamingCodec;
import com.smartdevicelink.streaming.audio.AudioStreamingParams;
import com.smartdevicelink.streaming.video.VideoStreamingParameters;
import com.smartdevicelink.transport.BaseTransportConfig;
import com.smartdevicelink.util.CorrelationIdGenerator;
import com.smartdevicelink.util.DebugTool;
import com.smartdevicelink.util.FileUtls;
import com.smartdevicelink.util.Version;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The lifecycle manager creates a centeral point for all SDL session logic to converge. It should only be used by
 * the library itself. Usage outside the library is not permitted and will not be protected for in the future.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class LifecycleManager extends BaseLifecycleManager {

    private static final String TAG = "Lifecycle Manager";

    public static final Version MAX_SUPPORTED_RPC_VERSION = new Version(5, 1, 0);

    // Protected Correlation IDs
    private final int 	REGISTER_APP_INTERFACE_CORRELATION_ID = 65529,
                        UNREGISTER_APP_INTERFACE_CORRELATION_ID = 65530;


    // Sdl Synchronization Objects
    private static final Object  RPC_LISTENER_LOCK = new Object(),
                                 ON_UPDATE_LISTENER_LOCK = new Object(),
                                 ON_REQUEST_LISTENER_LOCK = new Object(),
                                 ON_NOTIFICATION_LISTENER_LOCK = new Object();



    SdlSession session;
    AppConfig appConfig;

    //protected Version protocolVersion = new Version(1,0,0);
    protected Version rpcSpecVersion = MAX_SUPPORTED_RPC_VERSION;


    private final HashMap<Integer,CopyOnWriteArrayList<OnRPCListener>> rpcListeners;
    private final HashMap<Integer, OnRPCResponseListener> rpcResponseListeners;
    private final HashMap<Integer, CopyOnWriteArrayList<OnRPCNotificationListener>> rpcNotificationListeners;
    private final HashMap<Integer, CopyOnWriteArrayList<OnRPCRequestListener>> rpcRequestListeners;

    protected final SystemCapabilityManager systemCapabilityManager;

    protected RegisterAppInterfaceResponse raiResponse = null;

    private OnHMIStatus currentHMIStatus;
    protected boolean firstTimeFull = true;

    final LifecycleListener lifecycleListener;

    private List<Class<? extends SdlSecurityBase>> _secList = null;
    private String authToken;
    private Version minimumProtocolVersion;
    private Version minimumRPCVersion;

    public LifecycleManager(AppConfig appConfig, BaseTransportConfig config, LifecycleListener listener){

        this.lifecycleListener = listener;

        this.rpcListeners = new HashMap<>();
        this.rpcResponseListeners = new HashMap<>();
        this.rpcNotificationListeners = new HashMap<>();
        this.rpcRequestListeners = new HashMap<>();

        this.appConfig = appConfig;
        this.minimumProtocolVersion = appConfig.minimumProtocolVersion;
        this.minimumRPCVersion = appConfig.minimumRPCVersion;
        this.session = new SdlSession(sdlConnectionListener, config);

        this.systemCapabilityManager = new SystemCapabilityManager(internalInterface);

    }

    public void start(){
        try {
            setupInternalRpcListeners();
            session.startSession();
        } catch (SdlException e) {
            e.printStackTrace();
        }

    }

    public void stop(){
        session.close();
    }

    private Version getProtocolVersion(){
        if (session != null){
            return session.getProtocolVersion();
        }
        return new Version(1,0,0);
    }

    private void sendRPCs(List<? extends RPCMessage> messages, OnMultipleRequestListener listener){
        if(messages != null ){
            for(RPCMessage message : messages){
                if(message instanceof RPCRequest){
                    RPCRequest request = ((RPCRequest) message);
                    request.setCorrelationID(CorrelationIdGenerator.generateId());
                    if(listener != null){
                        listener.addCorrelationId(request.getCorrelationID());
                        request.setOnRPCResponseListener(listener.getSingleRpcResponseListener());
                    }
                    this.sendRPCMessagePrivate(request);

                }else{
                    this.sendRPCMessagePrivate(message);
                }
            }
        }
    }

    private void sendSequentialRPCs(final List<? extends RPCMessage> messages, final OnMultipleRequestListener listener){
       if (messages != null){
           int requestCount = messages.size();

           // Break out of recursion, we have finished the requests
           if (requestCount == 0) {
               if(listener != null){
                   listener.onFinished();
               }
               return;
           }

           RPCMessage rpc = messages.remove(0);

           // Request Specifics
           if (rpc.getMessageType().equals(RPCMessage.KEY_REQUEST)) {
               RPCRequest request = (RPCRequest) rpc;
               request.setCorrelationID(CorrelationIdGenerator.generateId());

               request.setOnRPCResponseListener(new OnRPCResponseListener() {
                   @Override
                   public void onResponse(int correlationId, RPCResponse response) {
                       if (response.getSuccess()) {
                           // success
                           if (listener != null) {
                               listener.onUpdate(messages.size());
                           }
                           // recurse after successful response of RPC
                           sendSequentialRPCs(messages, listener);
                       }
                   }

                   @Override
                   public void onError(int correlationId, Result resultCode, String info) {
                       if (listener != null) {
                           listener.onError(correlationId, resultCode, info);
                       }
                   }
               });
               sendRPCMessagePrivate(request);
           } else {
               // Notifications and Responses
               sendRPCMessagePrivate(rpc);
               if (listener != null) {
                   listener.onUpdate(messages.size());
               }
               // recurse after sending a notification or response as there is no response.
               sendSequentialRPCs(messages, listener);
           }
       }
    }

    /**
     * This method is used to ensure all of the methods in this class can remain private and no grantees can be made
     * to the developer what methods are available or not.
     *
     * <b>NOTE: THERE IS NO GURANTEE THIS WILL BE A VALID SYSTEM CAPABILITY MANAGER</b>
     *
     * @param sdlManager this must be a working manager instance
     * @return the system capability manager.
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public SystemCapabilityManager getSystemCapabilityManager(SdlManager sdlManager){
        if(sdlManager != null){
            return systemCapabilityManager;
        }
        return null;
    }

    private boolean isConnected(){
        if(session != null){
            return session.getIsConnected();
        }else{
            return false;
        }
    }

    /**
     * Method to retrieve the RegisterAppInterface Response message that was sent back from the
     * module. It contains various attributes about the connected module and can be used to adapt
     * to different module types and their supported features.
     *
     * @return RegisterAppInterfaceResponse received from the module or null if the app has not yet
     * registered with the module.
     */
    public RegisterAppInterfaceResponse getRegisterAppInterfaceResponse(){
        return this.raiResponse;
    }


    /**
     * Get the current OnHMIStatus
     * @return OnHMIStatus object represents the current OnHMIStatus
	 */
    public OnHMIStatus getCurrentHMIStatus() {
        return currentHMIStatus;
    }

    private void onClose(String info, Exception e){
        Log.i(TAG, "onClose");
        if(lifecycleListener != null){
            lifecycleListener.onProxyClosed(this, info,e,null);
        }
    }

    /**
     * This method is used to ensure all of the methods in this class can remain private and no grantees can be made
     * to the developer what methods are available or not.
     *
     * @param sdlManager this must be a working manager instance
     * @return the internal interface that hooks into this manager
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public ISdl getInternalInterface(SdlManager sdlManager) {
        if (sdlManager != null) {
            return internalInterface;
        }
        return null;
    }


    /* *******************************************************************************************************
     ********************************** INTERNAL - RPC LISTENERS !! START !! *********************************
     *********************************************************************************************************/

    private void setupInternalRpcListeners(){
        addRpcListener(FunctionID.REGISTER_APP_INTERFACE, rpcListener);
        addRpcListener(FunctionID.ON_HMI_STATUS, rpcListener);
        addRpcListener(FunctionID.ON_HASH_CHANGE, rpcListener);
        addRpcListener(FunctionID.ON_SYSTEM_REQUEST, rpcListener);
        addRpcListener(FunctionID.ON_APP_INTERFACE_UNREGISTERED, rpcListener);
        addRpcListener(FunctionID.UNREGISTER_APP_INTERFACE, rpcListener);
    }


    private OnRPCListener rpcListener = new OnRPCListener() {
        @Override
        public void onReceived(RPCMessage message) {
            //Make sure this is a response as expected
            FunctionID functionID = message.getFunctionID();
            if (functionID != null) {
                switch (functionID) {
                    case REGISTER_APP_INTERFACE:
                        //We have begun
                        Log.i(TAG, "RAI Response");
                        raiResponse = (RegisterAppInterfaceResponse) message;
                        SdlMsgVersion rpcVersion = ((RegisterAppInterfaceResponse) message).getSdlMsgVersion();
                        if (rpcVersion != null) {
                            LifecycleManager.this.rpcSpecVersion = new Version(rpcVersion.getMajorVersion(), rpcVersion.getMinorVersion(), rpcVersion.getPatchVersion());
                        } else {
                            LifecycleManager.this.rpcSpecVersion = MAX_SUPPORTED_RPC_VERSION;
                        }
                        if (minimumRPCVersion != null && minimumRPCVersion.isNewerThan(rpcSpecVersion) == 1) {
                            Log.w(TAG, String.format("Disconnecting from head unit, the configured minimum RPC version %s is greater than the supported RPC version %s", minimumRPCVersion, rpcSpecVersion));
                            UnregisterAppInterface msg = new UnregisterAppInterface();
                            msg.setCorrelationID(UNREGISTER_APP_INTERFACE_CORRELATION_ID);
                            sendRPCMessagePrivate(msg);
                            cleanProxy();
                            return;
                        }
                        processRaiResponse(raiResponse);
                        systemCapabilityManager.parseRAIResponse(raiResponse);
                        break;
                    case ON_HMI_STATUS:
                        Log.i(TAG, "on hmi status");
                        boolean shouldInit = currentHMIStatus == null;
                        currentHMIStatus = (OnHMIStatus) message;
                        if (lifecycleListener != null && shouldInit) {
                            lifecycleListener.onProxyConnected(LifecycleManager.this);
                        }
                        break;
                    case ON_HASH_CHANGE:
                        break;
                    case ON_SYSTEM_REQUEST:
                        final OnSystemRequest onSystemRequest = (OnSystemRequest) message;
                        if ((onSystemRequest.getUrl() != null) &&
                                (((onSystemRequest.getRequestType() == RequestType.PROPRIETARY) && (onSystemRequest.getFileType() == FileType.JSON))
                                        || ((onSystemRequest.getRequestType() == RequestType.HTTP) && (onSystemRequest.getFileType() == FileType.BINARY)))) {
                            Thread handleOffboardTransmissionThread = new Thread() {
                                @Override
                                public void run() {
                                    RPCRequest request = PoliciesFetcher.fetchPolicies(onSystemRequest);
                                    if (request != null && isConnected()) {
                                        sendRPCMessagePrivate(request);
                                    }
                                }
                            };
                            handleOffboardTransmissionThread.start();
                        }else if (onSystemRequest.getRequestType() == RequestType.ICON_URL) {
                            //Download the icon file and send SystemRequest RPC
                            Thread handleOffBoardTransmissionThread = new Thread() {
                                @Override
                                public void run() {
                                    byte[] file = FileUtls.downloadFile(onSystemRequest.getUrl());
                                    if (file != null) {
                                        SystemRequest systemRequest = new SystemRequest();
                                        systemRequest.setFileName(onSystemRequest.getUrl());
                                        systemRequest.setBulkData(file);
                                        systemRequest.setRequestType(RequestType.ICON_URL);
                                        if (isConnected()) {
                                            sendRPCMessagePrivate(systemRequest);
                                        }
                                    } else {
                                        DebugTool.logError("File was null at: " + onSystemRequest.getUrl());
                                    }
                                }
                            };
                            handleOffBoardTransmissionThread.start();
                        }
                        break;
                    case ON_APP_INTERFACE_UNREGISTERED:
                        Log.v(TAG, "on app interface unregistered");
                        cleanProxy();
                        break;
                    case UNREGISTER_APP_INTERFACE:
                        Log.v(TAG, "unregister app interface");
                        cleanProxy();
                        break;
                }
            }
        }



    };

    /* *******************************************************************************************************
     ********************************** INTERNAL - RPC LISTENERS !! END !! *********************************
     *********************************************************************************************************/


    /* *******************************************************************************************************
     ********************************** METHODS - RPC LISTENERS !! START !! **********************************
     *********************************************************************************************************/

    private boolean onRPCReceived(final RPCMessage message){
        synchronized(RPC_LISTENER_LOCK){
            if(message == null || message.getFunctionID() == null){
                return false;
            }

            final int id = message.getFunctionID().getId();
            CopyOnWriteArrayList<OnRPCListener> listeners = rpcListeners.get(id);
            if(listeners!=null && listeners.size()>0) {
                for (OnRPCListener listener : listeners) {
                    listener.onReceived(message);
                }
                return true;
            }
            return false;
        }
    }

    private void addRpcListener(FunctionID id, OnRPCListener listener){
        synchronized(RPC_LISTENER_LOCK){
            if (id != null && listener != null) {
                if (!rpcListeners.containsKey(id.getId())) {
                    rpcListeners.put(id.getId(), new CopyOnWriteArrayList<OnRPCListener>());
                }

                rpcListeners.get(id.getId()).add(listener);
            }
        }
    }

    private boolean removeOnRPCListener(FunctionID id, OnRPCListener listener){
        synchronized(RPC_LISTENER_LOCK){
            if(rpcListeners!= null
                    && id != null
                    && listener != null
                    && rpcListeners.containsKey(id.getId())){
                return rpcListeners.get(id.getId()).remove(listener);
            }
        }
        return false;
    }

    /**
     * Only call this method for a PutFile response. It will cause a class cast exception if not.
     * @param correlationId correlation id of the packet being updated
     * @param bytesWritten how many bytes were written
     * @param totalSize the total size in bytes
     */
    @SuppressWarnings("unused")
    private void onPacketProgress(int correlationId, long bytesWritten, long totalSize){
        synchronized(ON_UPDATE_LISTENER_LOCK){
            if(rpcResponseListeners !=null
                    && rpcResponseListeners.containsKey(correlationId)){
                ((OnPutFileUpdateListener)rpcResponseListeners.get(correlationId)).onUpdate(correlationId, bytesWritten, totalSize);
            }
        }

    }

    /**
     * Will provide callback to the listener either onFinish or onError depending on the RPCResponses result code,
     * <p>Will automatically remove the listener for the list of listeners on completion.
     * @param msg The RPCResponse message that was received
     * @return if a listener was called or not
     */
    @SuppressWarnings("UnusedReturnValue")
    private boolean onRPCResponseReceived(RPCResponse msg){
        synchronized(ON_UPDATE_LISTENER_LOCK){
            int correlationId = msg.getCorrelationID();
            if(rpcResponseListeners !=null
                    && rpcResponseListeners.containsKey(correlationId)){
                OnRPCResponseListener listener = rpcResponseListeners.get(correlationId);
                if(msg.getSuccess()){
                    listener.onResponse(correlationId, msg);
                }else{
                    listener.onError(correlationId, msg.getResultCode(), msg.getInfo());
                }
                rpcResponseListeners.remove(correlationId);
                return true;
            }
            return false;
        }
    }

    /**
     * Add a listener that will receive the response to the specific RPCRequest sent with the corresponding correlation id
     * @param listener that will get called back when a response is received
     * @param correlationId of the RPCRequest that was sent
     * @param totalSize only include if this is an OnPutFileUpdateListener. Otherwise it will be ignored.
     */
    private void addOnRPCResponseListener(OnRPCResponseListener listener,int correlationId, int totalSize){
        synchronized(ON_UPDATE_LISTENER_LOCK){
            if(rpcResponseListeners!=null
                    && listener !=null){
                if(listener.getListenerType() == OnRPCResponseListener.UPDATE_LISTENER_TYPE_PUT_FILE){
                    ((OnPutFileUpdateListener)listener).setTotalSize(totalSize);
                }
                listener.onStart(correlationId);
                rpcResponseListeners.put(correlationId, listener);
            }
        }
    }

    @SuppressWarnings("unused")
    private HashMap<Integer, OnRPCResponseListener> getResponseListeners(){
        synchronized(ON_UPDATE_LISTENER_LOCK){
            return this.rpcResponseListeners;
        }
    }

    /**
     * Retrieves the auth token, if any, that was attached to the StartServiceACK for the RPC
     * service from the module. For example, this should be used to login to a user account.
     * @return the string representation of the auth token
     */
    public String getAuthToken(){
        return this.authToken;
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean onRPCNotificationReceived(RPCNotification notification){
        if(notification == null){
            DebugTool.logError("onRPCNotificationReceived - Notification was null");
            return false;
        }
        DebugTool.logInfo("onRPCNotificationReceived - " + notification.getFunctionName() );

        //Before updating any listeners, make sure to do any final updates to the notification RPC now
        if(FunctionID.ON_HMI_STATUS.toString().equals(notification.getFunctionName())){
            OnHMIStatus onHMIStatus = (OnHMIStatus) notification;
            onHMIStatus.setFirstRun(firstTimeFull);
            if (onHMIStatus.getHmiLevel() == HMILevel.HMI_FULL) {
                firstTimeFull = false;
            }
        }

        synchronized(ON_NOTIFICATION_LISTENER_LOCK){
            CopyOnWriteArrayList<OnRPCNotificationListener> listeners = rpcNotificationListeners.get(FunctionID.getFunctionId(notification.getFunctionName()));
            if(listeners!=null && listeners.size()>0) {
                for (OnRPCNotificationListener listener : listeners) {
                    listener.onNotified(notification);
                }
                return true;
            }
            return false;
        }
    }

    /**
     * This will add a listener for the specific type of notification. As of now it will only allow
     * a single listener per notification function id
     * @param notificationId The notification type that this listener is designated for
     * @param listener The listener that will be called when a notification of the provided type is received
     */
    @SuppressWarnings("unused")
    private void addOnRPCNotificationListener(FunctionID notificationId, OnRPCNotificationListener listener){
        synchronized(ON_NOTIFICATION_LISTENER_LOCK){
            if(notificationId != null && listener != null){
                if(!rpcNotificationListeners.containsKey(notificationId.getId())){
                    rpcNotificationListeners.put(notificationId.getId(),new CopyOnWriteArrayList<OnRPCNotificationListener>());
                }
                rpcNotificationListeners.get(notificationId.getId()).add(listener);
            }
        }
    }

    private boolean removeOnRPCNotificationListener(FunctionID notificationId, OnRPCNotificationListener listener){
        synchronized(ON_NOTIFICATION_LISTENER_LOCK){
            if(rpcNotificationListeners!= null
                    && notificationId != null
                    && listener != null
                    && rpcNotificationListeners.containsKey(notificationId.getId())){
                return rpcNotificationListeners.get(notificationId.getId()).remove(listener);
            }
        }
        return false;
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean onRPCRequestReceived(RPCRequest request){
        if(request == null){
            DebugTool.logError("onRPCRequestReceived - request was null");
            return false;
        }
        DebugTool.logInfo("onRPCRequestReceived - " + request.getFunctionName() );

        synchronized(ON_REQUEST_LISTENER_LOCK){
            CopyOnWriteArrayList<OnRPCRequestListener> listeners = rpcRequestListeners.get(FunctionID.getFunctionId(request.getFunctionName()));
            if(listeners!=null && listeners.size()>0) {
                for (OnRPCRequestListener listener : listeners) {
                    listener.onRequest(request);
                }
                return true;
            }
            return false;
        }
    }

    /**
     * This will add a listener for the specific type of request. As of now it will only allow
     * a single listener per request function id
     * @param requestId The request type that this listener is designated for
     * @param listener The listener that will be called when a request of the provided type is received
     */
    @SuppressWarnings("unused")
    private void addOnRPCRequestListener(FunctionID requestId, OnRPCRequestListener listener){
        synchronized(ON_REQUEST_LISTENER_LOCK){
            if(requestId != null && listener != null){
                if(!rpcRequestListeners.containsKey(requestId.getId())){
                    rpcRequestListeners.put(requestId.getId(),new CopyOnWriteArrayList<OnRPCRequestListener>());
                }
                rpcRequestListeners.get(requestId.getId()).add(listener);
            }
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean removeOnRPCRequestListener(FunctionID requestId, OnRPCRequestListener listener){
        synchronized(ON_REQUEST_LISTENER_LOCK){
            if(rpcRequestListeners!= null
                    && requestId != null
                    && listener != null
                    && rpcRequestListeners.containsKey(requestId.getId())){
                return rpcRequestListeners.get(requestId.getId()).remove(listener);
            }
        }
        return false;
    }

    /* *******************************************************************************************************
     **************************************** RPC LISTENERS !! END !! ****************************************
     *********************************************************************************************************/



    private void sendRPCMessagePrivate(RPCMessage message){
        try {
            //FIXME this is temporary until the next major release of the library where OK is removed
            if (message.getMessageType().equals(RPCMessage.KEY_REQUEST)) {
                RPCRequest request = (RPCRequest) message;
                if(FunctionID.SUBSCRIBE_BUTTON.toString().equals(request.getFunctionName())
                        || FunctionID.UNSUBSCRIBE_BUTTON.toString().equals(request.getFunctionName())
                        || FunctionID.BUTTON_PRESS.toString().equals(request.getFunctionName())) {

                    ButtonName buttonName = (ButtonName) request.getObject(ButtonName.class, SubscribeButton.KEY_BUTTON_NAME);


                    if (rpcSpecVersion != null) {
                        if (rpcSpecVersion.getMajor() < 5) {

                            if (ButtonName.PLAY_PAUSE.equals(buttonName)) {
                                request.setParameters(SubscribeButton.KEY_BUTTON_NAME, ButtonName.OK);
                            }
                        } else { //Newer than version 5.0.0
                            if (ButtonName.OK.equals(buttonName)) {
                                RPCRequest request2 = new RPCRequest(request);
                                request2.setParameters(SubscribeButton.KEY_BUTTON_NAME, ButtonName.PLAY_PAUSE);
                                request2.setOnRPCResponseListener(request.getOnRPCResponseListener());
                                sendRPCMessagePrivate(request2);
                                return;
                            }
                        }
                    }

                }
            }


            message.format(rpcSpecVersion,true);
            byte[] msgBytes = JsonRPCMarshaller.marshall(message, (byte)getProtocolVersion().getMajor());

            ProtocolMessage pm = new ProtocolMessage();
            pm.setData(msgBytes);
            if (session != null){
                pm.setSessionID(session.getSessionId());
            }

            pm.setMessageType(MessageType.RPC);
            pm.setSessionType(SessionType.RPC);
            pm.setFunctionID(FunctionID.getFunctionId(message.getFunctionName()));
            pm.setPayloadProtected(message.isPayloadProtected());

            if(RPCMessage.KEY_REQUEST.equals(message.getMessageType())){ // Request Specifics
                pm.setRPCType((byte)0x00);
                Integer corrId = ((RPCRequest)message).getCorrelationID();
                if( corrId== null) {
                    Log.e(TAG, "No correlation ID attached to request. Not sending");
                    return;
                }else{
                    pm.setCorrID(corrId);

                    OnRPCResponseListener listener = ((RPCRequest)message).getOnRPCResponseListener();
                    if(listener != null){
                        addOnRPCResponseListener(listener, corrId, msgBytes.length);
                    }
                }
            }else if (RPCMessage.KEY_RESPONSE.equals(message.getMessageType())){ // Response Specifics
                RPCResponse response = (RPCResponse) message;
                pm.setRPCType((byte)0x01);
                if (response.getCorrelationID() == null) {
                    //Log error here
                    //throw new SdlException("CorrelationID cannot be null. RPC: " + response.getFunctionName(), SdlExceptionCause.INVALID_ARGUMENT);
                    Log.e(TAG, "No correlation ID attached to response. Not sending");
                    return;
                } else {
                    pm.setCorrID(response.getCorrelationID());
                }
            }else if (message.getMessageType().equals(RPCMessage.KEY_NOTIFICATION)) { // Notification Specifics
                pm.setRPCType((byte)0x02);
            }

            if (message.getBulkData() != null){
                pm.setBulkData(message.getBulkData());
            }

            if(message.getFunctionName().equalsIgnoreCase(FunctionID.PUT_FILE.name())){
                pm.setPriorityCoefficient(1);
            }

            session.sendMessage(pm);

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }



    /* *******************************************************************************************************
     *************************************** ISdlConnectionListener START ************************************
     *********************************************************************************************************/

    final ISdlConnectionListener sdlConnectionListener = new ISdlConnectionListener() {
        @Override
        public void onTransportDisconnected(String info) {
            onClose(info, null);

        }

        @Override
        public void onTransportDisconnected(String info, boolean availablePrimary, BaseTransportConfig transportConfig) {
            if (!availablePrimary) {
                onClose(info, null);
            }

        }

        @Override
        public void onTransportError(String info, Exception e) {
            onClose(info, e);

        }

        @Override
        public void onProtocolMessageReceived(ProtocolMessage msg) {
            //Incoming message
            if (SessionType.RPC.equals(msg.getSessionType())
                    || SessionType.BULK_DATA.equals(msg.getSessionType())) {

                RPCMessage rpc = RpcConverter.extractRpc(msg, session.getProtocolVersion());
                if (rpc != null) {
                    String messageType = rpc.getMessageType();
                    Log.v(TAG, "RPC received - " + messageType);

                    rpc.format(rpcSpecVersion, true);

                    onRPCReceived(rpc);

                    if (RPCMessage.KEY_RESPONSE.equals(messageType)) {

                        onRPCResponseReceived((RPCResponse) rpc);

                    } else if (RPCMessage.KEY_NOTIFICATION.equals(messageType)) {
                        FunctionID functionID = rpc.getFunctionID();
                        if (functionID != null && (functionID.equals(FunctionID.ON_BUTTON_PRESS)) || functionID.equals(FunctionID.ON_BUTTON_EVENT)) {
                            RPCNotification notificationCompat = handleButtonNotificationFormatting(rpc);
                            if(notificationCompat != null){
                                onRPCNotificationReceived((notificationCompat));
                            }
                        }

                        onRPCNotificationReceived((RPCNotification) rpc);

                    } else if (RPCMessage.KEY_REQUEST.equals(messageType)) {

                        onRPCRequestReceived((RPCRequest) rpc);

                    }
                } else {
                    Log.w(TAG, "Shouldn't be here");
                }
            }

        }

        @Override
        public void onProtocolSessionStartedNACKed(SessionType sessionType, byte sessionID, byte version, String correlationID, List<String> rejectedParams) {
            Log.w(TAG, "onProtocolSessionStartedNACKed " + sessionID);
        }

        @Override
        public void onProtocolSessionStarted(SessionType sessionType, byte sessionID, byte version, String correlationID, int hashID, boolean isEncrypted) {

            Log.i(TAG, "on protocol session started");
            if (sessionType != null) {
                if (minimumProtocolVersion != null && minimumProtocolVersion.isNewerThan(getProtocolVersion()) == 1) {
                    Log.w(TAG, String.format("Disconnecting from head unit, the configured minimum protocol version %s is greater than the supported protocol version %s", minimumProtocolVersion, getProtocolVersion()));
                    session.endService(sessionType, session.getSessionId());
                    cleanProxy();
                    return;
                }

                if (sessionType.equals(SessionType.RPC)) {
                    if (appConfig != null) {

                        appConfig.prepare();

                        SdlMsgVersion sdlMsgVersion = new SdlMsgVersion();
                        sdlMsgVersion.setMajorVersion(MAX_SUPPORTED_RPC_VERSION.getMajor());
                        sdlMsgVersion.setMinorVersion(MAX_SUPPORTED_RPC_VERSION.getMinor());
                        sdlMsgVersion.setPatchVersion(MAX_SUPPORTED_RPC_VERSION.getPatch());

                        RegisterAppInterface rai = new RegisterAppInterface(sdlMsgVersion,
                                appConfig.getAppName(), appConfig.isMediaApp(), appConfig.getLanguageDesired(),
                                appConfig.getHmiDisplayLanguageDesired(), appConfig.getAppID());
                        rai.setCorrelationID(REGISTER_APP_INTERFACE_CORRELATION_ID);

                        rai.setTtsName(appConfig.getTtsName());
                        rai.setNgnMediaScreenAppName(appConfig.getNgnMediaScreenAppName());
                        rai.setVrSynonyms(appConfig.getVrSynonyms());
                        rai.setAppHMIType(appConfig.getAppType());
                        rai.setDayColorScheme(appConfig.getDayColorScheme());
                        rai.setNightColorScheme(appConfig.getNightColorScheme());

                        //Add device/system info in the future
                        //TODO attach previous hash id

                        sendRPCMessagePrivate(rai);
                    } else {
                        Log.e(TAG, "App config was null, soo...");
                    }


                } else {
                    lifecycleListener.onServiceStarted(sessionType);
                }
            }
        }

        @Override
        public void onProtocolSessionEnded(SessionType sessionType, byte sessionID, String correlationID) {

        }

        @Override
        public void onProtocolSessionEndedNACKed(SessionType sessionType, byte sessionID, String correlationID) {

        }

        @Override
        public void onProtocolError(String info, Exception e) {
            DebugTool.logError("Protocol Error - " + info, e);
        }

        @Override
        public void onHeartbeatTimedOut(byte sessionID) { /* Deprecated */ }

        @Override
        public void onProtocolServiceDataACK(SessionType sessionType, int dataSize, byte sessionID) {/* Unused */ }


        @Override
        public void onAuthTokenReceived(String token, byte sessionID) {
            LifecycleManager.this.authToken = token;
        }

    };
    /* *******************************************************************************************************
     *************************************** ISdlConnectionListener END ************************************
     *********************************************************************************************************/


    /* *******************************************************************************************************
     ******************************************** ISdl - START ***********************************************
     *********************************************************************************************************/

    final ISdl internalInterface = new ISdl() {
        @Override
        public void start() {
            LifecycleManager.this.start();
        }

        @Override
        public void stop() {
            LifecycleManager.this.stop();
        }

        @Override
        public boolean isConnected() {
            return LifecycleManager.this.session.getIsConnected();
        }

        @Override
        public void addServiceListener(SessionType serviceType, ISdlServiceListener sdlServiceListener) {
            LifecycleManager.this.session.addServiceListener(serviceType,sdlServiceListener);
        }

        @Override
        public void removeServiceListener(SessionType serviceType, ISdlServiceListener sdlServiceListener) {
            LifecycleManager.this.session.removeServiceListener(serviceType,sdlServiceListener);

        }

        @Override
        public void startVideoService(VideoStreamingParameters parameters, boolean encrypted) {
            DebugTool.logWarning("startVideoService is not currently implemented");

        }

        @Override
        public void stopVideoService() {
            DebugTool.logWarning("stopVideoService is not currently implemented");

        }

        @Override
        public IVideoStreamListener startVideoStream(boolean isEncrypted, VideoStreamingParameters parameters) {
            DebugTool.logWarning("startVideoStream is not currently implemented");
            return null;
        }

        @Override
        public void startAudioService(boolean encrypted, AudioStreamingCodec codec, AudioStreamingParams params) {
            DebugTool.logWarning("startAudioService is not currently implemented");
        }

        @Override
        public void startAudioService(boolean encrypted) {
            DebugTool.logWarning("startAudioService is not currently implemented");

        }

        @Override
        public void stopAudioService() {
            DebugTool.logWarning("stopAudioService is not currently implemented");
        }

        @Override
        public IAudioStreamListener startAudioStream(boolean isEncrypted, AudioStreamingCodec codec, AudioStreamingParams params) {
            DebugTool.logWarning("startAudioStream is not currently implemented");
            return null;
        }

        @Override
        public void sendRPCRequest(RPCRequest message) {
            LifecycleManager.this.sendRPCMessagePrivate(message);

        }

        @Override
        public void sendRPC(RPCMessage message) {
            if(isConnected()) {
                LifecycleManager.this.sendRPCMessagePrivate(message);
            }
        }

        @Override
        public void sendRequests(List<? extends RPCRequest> rpcs, OnMultipleRequestListener listener) {
            LifecycleManager.this.sendRPCs(rpcs,listener);
        }

        @Override
        public void sendSequentialRPCs(List<? extends RPCMessage> rpcs, OnMultipleRequestListener listener) {
            LifecycleManager.this.sendSequentialRPCs(rpcs,listener);
        }

        @Override
        public void addOnRPCNotificationListener(FunctionID notificationId, OnRPCNotificationListener listener) {
            LifecycleManager.this.addOnRPCNotificationListener(notificationId,listener);
        }

        @Override
        public boolean removeOnRPCNotificationListener(FunctionID notificationId, OnRPCNotificationListener listener) {
            return LifecycleManager.this.removeOnRPCNotificationListener(notificationId,listener);
        }

        @Override
        public void addOnRPCRequestListener(FunctionID notificationId, OnRPCRequestListener listener) {
            LifecycleManager.this.addOnRPCRequestListener(notificationId, listener);
        }

        @Override
        public boolean removeOnRPCRequestListener(FunctionID notificationId, OnRPCRequestListener listener) {
            return LifecycleManager.this.removeOnRPCRequestListener(notificationId, listener);
        }

        @Override
        public void addOnRPCListener(FunctionID responseId, OnRPCListener listener) {
            LifecycleManager.this.addRpcListener(responseId,listener);
        }

        @Override
        public boolean removeOnRPCListener(FunctionID responseId, OnRPCListener listener) {
            return LifecycleManager.this.removeOnRPCListener(responseId,listener);
        }

        @Override
        public Object getCapability(SystemCapabilityType systemCapabilityType) {
            return LifecycleManager.this.systemCapabilityManager.getCapability(systemCapabilityType);
        }

        @Override
        public void getCapability(SystemCapabilityType systemCapabilityType, OnSystemCapabilityListener scListener) {
            LifecycleManager.this.systemCapabilityManager.getCapability(systemCapabilityType,scListener);

        }

        @Override
        public boolean isCapabilitySupported(SystemCapabilityType systemCapabilityType) {
            return LifecycleManager.this.systemCapabilityManager.isCapabilitySupported(systemCapabilityType);
        }

        @Override
        public void addOnSystemCapabilityListener(SystemCapabilityType systemCapabilityType, OnSystemCapabilityListener listener) {
            LifecycleManager.this.systemCapabilityManager.addOnSystemCapabilityListener(systemCapabilityType,listener);

        }

        @Override
        public boolean removeOnSystemCapabilityListener(SystemCapabilityType systemCapabilityType, OnSystemCapabilityListener listener) {
            return LifecycleManager.this.systemCapabilityManager.removeOnSystemCapabilityListener(systemCapabilityType,listener);
        }

        @Override
        public boolean isTransportForServiceAvailable(SessionType serviceType) {
            return LifecycleManager.this.session.isTransportForServiceAvailable(serviceType);
        }

        @Override
        public SdlMsgVersion getSdlMsgVersion() {
            SdlMsgVersion msgVersion = new SdlMsgVersion(rpcSpecVersion.getMajor(), rpcSpecVersion.getMinor());
            msgVersion.setPatchVersion(rpcSpecVersion.getPatch());
            return msgVersion;
        }

        @Override
        public Version getProtocolVersion() {
            return LifecycleManager.this.getProtocolVersion();
        }
    };

    /* *******************************************************************************************************
     ********************************************* ISdl - END ************************************************
     *********************************************************************************************************/

    public interface LifecycleListener{
        void onProxyConnected(LifecycleManager lifeCycleManager);
        void onProxyClosed(LifecycleManager lifeCycleManager, String info, Exception e, SdlDisconnectedReason reason);
        void onServiceStarted(SessionType sessionType);
        void onServiceEnded(SessionType sessionType);
        void onError(LifecycleManager lifeCycleManager, String info, Exception e);
    }

    public static class AppConfig{
        private String appID, appName, ngnMediaScreenAppName;
        private Vector<TTSChunk> ttsName;
        private Vector<String> vrSynonyms;
        private boolean isMediaApp = false;
        private Language languageDesired, hmiDisplayLanguageDesired;
        private Vector<AppHMIType> appType;
        private TemplateColorScheme dayColorScheme, nightColorScheme;
        private Version minimumProtocolVersion;
        private Version minimumRPCVersion;

        private void prepare(){
            if (getNgnMediaScreenAppName() == null) {
                setNgnMediaScreenAppName(getAppName());
            }

            if (getLanguageDesired() == null) {
                setLanguageDesired(Language.EN_US);
            }

            if (getHmiDisplayLanguageDesired() == null) {
                setHmiDisplayLanguageDesired(Language.EN_US);
            }

            if (getVrSynonyms() == null) {
                setVrSynonyms(new Vector<String>());
                getVrSynonyms().add(getAppName());
            }
        }

        public String getAppID() {
            return appID;
        }

        public void setAppID(String appID) {
            this.appID = appID;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getNgnMediaScreenAppName() {
            return ngnMediaScreenAppName;
        }

        public void setNgnMediaScreenAppName(String ngnMediaScreenAppName) {
            this.ngnMediaScreenAppName = ngnMediaScreenAppName;
        }

        public Vector<TTSChunk> getTtsName() {
            return ttsName;
        }

        public void setTtsName(Vector<TTSChunk> ttsName) {
            this.ttsName = ttsName;
        }

        public Vector<String> getVrSynonyms() {
            return vrSynonyms;
        }

        public void setVrSynonyms(Vector<String> vrSynonyms) {
            this.vrSynonyms = vrSynonyms;
        }

        public boolean isMediaApp() {
            return isMediaApp;
        }

        public void setMediaApp(boolean mediaApp) {
            isMediaApp = mediaApp;
        }

        public Language getLanguageDesired() {
            return languageDesired;
        }

        public void setLanguageDesired(Language languageDesired) {
            this.languageDesired = languageDesired;
        }

        public Language getHmiDisplayLanguageDesired() {
            return hmiDisplayLanguageDesired;
        }

        public void setHmiDisplayLanguageDesired(Language hmiDisplayLanguageDesired) {
            this.hmiDisplayLanguageDesired = hmiDisplayLanguageDesired;
        }

        public Vector<AppHMIType> getAppType() {
            return appType;
        }

        public void setAppType(Vector<AppHMIType> appType) {
            this.appType = appType;
        }

        public TemplateColorScheme getDayColorScheme() {
            return dayColorScheme;
        }

        public void setDayColorScheme(TemplateColorScheme dayColorScheme) {
            this.dayColorScheme = dayColorScheme;
        }

        public TemplateColorScheme getNightColorScheme() {
            return nightColorScheme;
        }

        public void setNightColorScheme(TemplateColorScheme nightColorScheme) {
            this.nightColorScheme = nightColorScheme;
        }

        public Version getMinimumProtocolVersion() {
            return minimumProtocolVersion;
        }

        /**
         * Sets the minimum protocol version that will be permitted to connect.
         * If the protocol version of the head unit connected is below this version,
         * the app will disconnect with an EndService protocol message and will not register.
         *
         * @param minimumProtocolVersion a Version object with the minimally accepted Protocol version
         */
        public void setMinimumProtocolVersion(Version minimumProtocolVersion) {
            this.minimumProtocolVersion = minimumProtocolVersion;
        }

        public Version getMinimumRPCVersion() {
            return minimumRPCVersion;
        }

        /**
         * The minimum RPC version that will be permitted to connect.
         * If the RPC version of the head unit connected is below this version, an UnregisterAppInterface will be sent.
         *
         * @param minimumRPCVersion a Version object with the minimally accepted RPC spec version
         */
        public void setMinimumRPCVersion(Version minimumRPCVersion) {
            this.minimumRPCVersion = minimumRPCVersion;
        }
    }


    /**
     * Temporary method to bridge the new PLAY_PAUSE and OKAY button functionality with the old
     * OK button name. This should be removed during the next major release
     * @param notification an RPC message object that should be either an ON_BUTTON_EVENT or ON_BUTTON_PRESS otherwise
     *                     it will be ignored
     */
    private RPCNotification handleButtonNotificationFormatting(RPCMessage notification){
        if(FunctionID.ON_BUTTON_EVENT.toString().equals(notification.getFunctionName())
                || FunctionID.ON_BUTTON_PRESS.toString().equals(notification.getFunctionName())){

            ButtonName buttonName = (ButtonName)notification.getObject(ButtonName.class, OnButtonEvent.KEY_BUTTON_NAME);
            ButtonName compatBtnName = null;

            if(rpcSpecVersion != null && rpcSpecVersion.getMajor() >= 5){
                if(ButtonName.PLAY_PAUSE.equals(buttonName)){
                    compatBtnName =  ButtonName.OK;
                }
            }else{ // rpc spec version is either null or less than 5
                if(ButtonName.OK.equals(buttonName)){
                    compatBtnName = ButtonName.PLAY_PAUSE;
                }
            }

            try {
                if (compatBtnName != null) { //There is a button name that needs to be swapped out
                    RPCNotification notification2;
                    //The following is done because there is currently no way to make a deep copy
                    //of an RPC. Since this code will be removed, it's ugliness is borderline acceptable.
                    if (notification instanceof OnButtonEvent) {
                        OnButtonEvent onButtonEvent = new OnButtonEvent();
                        onButtonEvent.setButtonEventMode(((OnButtonEvent) notification).getButtonEventMode());
                        onButtonEvent.setCustomButtonID(((OnButtonEvent) notification).getCustomButtonID());
                        notification2 = onButtonEvent;
                    } else if (notification instanceof OnButtonPress) {
                        OnButtonPress onButtonPress = new OnButtonPress();
                        onButtonPress.setButtonPressMode(((OnButtonPress) notification).getButtonPressMode());
                        onButtonPress.setCustomButtonName(((OnButtonPress) notification).getCustomButtonName());
                        notification2 = onButtonPress;
                    } else {
                        return null;
                    }

                    notification2.setParameters(OnButtonEvent.KEY_BUTTON_NAME, compatBtnName);
                    return notification2;
                }
            }catch (Exception e){
                //Should never get here
            }
        }
        return null;
    }

    private void cleanProxy(){
        if (rpcListeners != null) {
            rpcListeners.clear();
        }
        if (rpcResponseListeners != null) {
            rpcResponseListeners.clear();
        }
        if (rpcNotificationListeners != null) {
            rpcNotificationListeners.clear();
        }
        if (rpcRequestListeners != null) {
            rpcRequestListeners.clear();
        }
        if (session != null && session.getIsConnected()) {
            session.close();
        }
    }

    public void setSdlSecurityClassList(List<Class<? extends SdlSecurityBase>> list) {
        _secList = list;
    }

    private void processRaiResponse(RegisterAppInterfaceResponse rai) {
        if (rai == null) return;

        this.raiResponse = rai;

        VehicleType vt = rai.getVehicleType();
        if (vt == null) return;

        String make = vt.getMake();
        if (make == null) return;

        if (_secList == null) return;

        SdlSecurityBase sec;

        for (Class<? extends SdlSecurityBase> cls : _secList) {
            try {
                sec = cls.newInstance();
            } catch (Exception e) {
                continue;
            }

            if ((sec != null) && (sec.getMakeList() != null)) {
                if (sec.getMakeList().contains(make)) {
                    sec.setAppId(appConfig.getAppID());
                    if (session != null) {
                        session.setSdlSecurity(sec);
                        sec.handleSdlSession(session);
                    }
                    return;
                }
            }
        }
    }
}
