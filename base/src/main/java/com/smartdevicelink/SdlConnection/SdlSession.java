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

package com.smartdevicelink.SdlConnection;

import android.util.Log;

import com.smartdevicelink.exception.SdlException;
import com.smartdevicelink.protocol.ISdlProtocol;
import com.smartdevicelink.protocol.ProtocolMessage;
import com.smartdevicelink.protocol.SdlPacket;
import com.smartdevicelink.protocol.enums.SessionType;
import com.smartdevicelink.protocol.SdlProtocol;
import com.smartdevicelink.proxy.interfaces.ISdlServiceListener;
import com.smartdevicelink.proxy.rpc.VideoStreamingFormat;
import com.smartdevicelink.proxy.rpc.enums.VideoStreamingProtocol;
import com.smartdevicelink.security.ISecurityInitializedListener;
import com.smartdevicelink.security.SdlSecurityBase;
import com.smartdevicelink.streaming.video.VideoStreamingParameters;
import com.smartdevicelink.transport.BaseTransportConfig;
import com.smartdevicelink.transport.enums.TransportType;
import com.smartdevicelink.util.Version;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class SdlSession implements ISdlProtocol, ISdlConnectionListener, ISecurityInitializedListener {

    private static final String TAG = "SdlSession";

    final protected SdlProtocol sdlProtocol;

    protected BaseTransportConfig transportConfig;
    protected ISdlConnectionListener sessionListener;
	protected SdlSecurityBase sdlSecurity = null;
	protected VideoStreamingParameters desiredVideoParams = null;
	protected VideoStreamingParameters acceptedVideoParams = null;

	protected byte sessionId;
	protected int sessionHashId = 0;
	protected HashMap<SessionType, CopyOnWriteArrayList<ISdlServiceListener>> serviceListeners;
	protected CopyOnWriteArrayList<SessionType> encryptedServices = new CopyOnWriteArrayList<SessionType>();


    public SdlSession(ISdlConnectionListener listener, BaseTransportConfig config){
        this.transportConfig = config;
        this.sessionListener = listener;
        this.sdlProtocol = new SdlProtocol(this,config);

    }


    public int getMtu(){
        if(this.sdlProtocol!=null){
            return this.sdlProtocol.getMtu();
        }else{
            return 0;
        }
    }

    public long getMtu(SessionType type) {
        if (this.sdlProtocol != null) {
            return this.sdlProtocol.getMtu(type);
        } else {
            return 0;
        }
    }

    public void close() {
        if (sdlSecurity != null)
        {
            sdlSecurity.resetParams();
            sdlSecurity.shutDown();
        }
        if(sdlProtocol != null){
            sdlProtocol.endSession(sessionId, sessionHashId);
        }
    }


    public void startService (SessionType serviceType, byte sessionID, boolean isEncrypted) {
        if (isEncrypted){
            if (sdlSecurity != null){
                List<SessionType> serviceList = sdlSecurity.getServiceList();
                if (!serviceList.contains(serviceType))
                    serviceList.add(serviceType);

                sdlSecurity.initialize();
            }
            return;
        }
        sdlProtocol.startService(serviceType, sessionID, isEncrypted);
    }

    public void endService (SessionType serviceType, byte sessionID) {
        if (sdlProtocol == null) {
            return;
        }
        sdlProtocol.endService(serviceType,sessionID);
    }


    public void startSession() throws SdlException {
        sdlProtocol.start();
    }


    public void sendMessage(ProtocolMessage msg) {
        if (sdlProtocol == null){
            return;
        }
        sdlProtocol.sendMessage(msg);
    }

    public TransportType getCurrentTransportType() {
        return transportConfig.getTransportType();
    }

    public boolean getIsConnected() {
        return sdlProtocol != null && sdlProtocol.isConnected();
    }


    public void shutdown(String info){
        Log.d(TAG, "Shutdown - " + info);
        this.sessionListener.onTransportDisconnected(info);

    }

    @Override
    public void onTransportDisconnected(String info, boolean altTransportAvailable, BaseTransportConfig transportConfig) {
        this.sessionListener.onTransportDisconnected(info, altTransportAvailable, this.transportConfig);
    }

    /**
     * Get the current protocol version used by this session
     * @return Version that represents the Protocol version being used
     */
    public Version getProtocolVersion(){
        if(sdlProtocol!=null){
            return sdlProtocol.getProtocolVersion();
        }
        return new Version(1,0,0);
    }


    public BaseTransportConfig getTransportConfig() {
        return this.transportConfig;
    }

    public int getSessionHashId() {
        return this.sessionHashId;
    }

    public byte getSessionId() {
        return this.sessionId;
    }

    public void setSdlSecurity(SdlSecurityBase sec) {
        sdlSecurity = sec;
    }

    public SdlSecurityBase getSdlSecurity() {
        return sdlSecurity;
    }


    protected void processControlService(ProtocolMessage msg) {
        if (sdlSecurity == null)
            return;
        int ilen = msg.getData().length - 12;
        byte[] data = new byte[ilen];
        System.arraycopy(msg.getData(), 12, data, 0, ilen);

        byte[] dataToRead = new byte[4096];

        Integer iNumBytes = sdlSecurity.runHandshake(data, dataToRead);

        if (iNumBytes == null || iNumBytes <= 0)
            return;

        byte[] returnBytes = new byte[iNumBytes];
        System.arraycopy(dataToRead, 0, returnBytes, 0, iNumBytes);
        ProtocolMessage protocolMessage = new ProtocolMessage();
        protocolMessage.setSessionType(SessionType.CONTROL);
        protocolMessage.setData(returnBytes);
        protocolMessage.setFunctionID(0x01);
        protocolMessage.setVersion((byte)sdlProtocol.getProtocolVersion().getMajor());
        protocolMessage.setSessionID(getSessionId());

        //sdlSecurity.hs();

        sendMessage(protocolMessage);
    }


    public boolean isServiceProtected(SessionType sType) {
        return encryptedServices.contains(sType);
    }

    @Override
    public void onTransportDisconnected(String info) {
        this.sessionListener.onTransportDisconnected(info);
    }


    @Override
    public void onTransportError(String info, Exception e) {
        this.sessionListener.onTransportError(info, e);
    }

    @Override
    public void onProtocolMessageReceived(ProtocolMessage msg) {
        if (msg.getSessionType().equals(SessionType.CONTROL)) {
            processControlService(msg);
            return;
        }

        this.sessionListener.onProtocolMessageReceived(msg);
    }

    @Override
    public void onHeartbeatTimedOut(byte sessionID) {
        this.sessionListener.onHeartbeatTimedOut(sessionID);

    }


    @Override
    public void onProtocolSessionStarted(SessionType sessionType,
                                         byte sessionID, byte version, String correlationID, int hashID, boolean isEncrypted) {

        Log.i(TAG, "Protocol session started");

        this.sessionId = sessionID;
        if (sessionType.eq(SessionType.RPC)){
            sessionHashId = hashID;
        }
        if (isEncrypted)
            encryptedServices.addIfAbsent(sessionType);
        this.sessionListener.onProtocolSessionStarted(sessionType, sessionID, version, correlationID, hashID, isEncrypted);
        if(serviceListeners != null && serviceListeners.containsKey(sessionType)){
            CopyOnWriteArrayList<ISdlServiceListener> listeners = serviceListeners.get(sessionType);
            for(ISdlServiceListener listener:listeners){
                listener.onServiceStarted(this, sessionType, isEncrypted);
            }
        }

    }

    @Override
    public void onProtocolSessionEnded(SessionType sessionType, byte sessionID,
                                       String correlationID) {
        this.sessionListener.onProtocolSessionEnded(sessionType, sessionID, correlationID);
        if(serviceListeners != null && serviceListeners.containsKey(sessionType)){
            CopyOnWriteArrayList<ISdlServiceListener> listeners = serviceListeners.get(sessionType);
            for(ISdlServiceListener listener:listeners){
                listener.onServiceEnded(this, sessionType);
            }
        }
        encryptedServices.remove(sessionType);
    }

    @Override
    public void onProtocolError(String info, Exception e) {
        this.sessionListener.onProtocolError(info, e);
        Log.e(TAG, "on protocol error", e);
    }




    @Override
    public void onProtocolSessionEndedNACKed(SessionType sessionType,
                                             byte sessionID, String correlationID) {
        this.sessionListener.onProtocolSessionEndedNACKed(sessionType, sessionID, correlationID);
        if(serviceListeners != null && serviceListeners.containsKey(sessionType)){
            CopyOnWriteArrayList<ISdlServiceListener> listeners = serviceListeners.get(sessionType);
            for(ISdlServiceListener listener:listeners){
                listener.onServiceError(this, sessionType, "End "+ sessionType.toString() +" Service NACK'ed");
            }
        }
    }

    @Override
    public void onProtocolServiceDataACK(SessionType sessionType, int dataSize, byte sessionID) {
        this.sessionListener.onProtocolServiceDataACK(sessionType, dataSize, sessionID);
    }



    @Override
    public void onAuthTokenReceived(String token, byte sessionID) {
        //This is not used in the base library. Will only be used in the Android library while it has the SdlConnection class
        //See onAuthTokenReceived(String token) in this class instead

    }

    public void addServiceListener(SessionType serviceType, ISdlServiceListener sdlServiceListener){
        if(serviceListeners == null){
            serviceListeners = new HashMap<>();
        }
        if(serviceType != null && sdlServiceListener != null){
            if(!serviceListeners.containsKey(serviceType)){
                serviceListeners.put(serviceType,new CopyOnWriteArrayList<ISdlServiceListener>());
            }
            serviceListeners.get(serviceType).add(sdlServiceListener);
        }
    }

    public boolean removeServiceListener(SessionType serviceType, ISdlServiceListener sdlServiceListener){
        if(serviceListeners!= null && serviceType != null && sdlServiceListener != null && serviceListeners.containsKey(serviceType)){
            return serviceListeners.get(serviceType).remove(sdlServiceListener);
        }
        return false;
    }


    public HashMap<SessionType, CopyOnWriteArrayList<ISdlServiceListener>> getServiceListeners(){
        return serviceListeners;
    }

    public void setDesiredVideoParams(VideoStreamingParameters params){
        this.desiredVideoParams = params;
    }

    /**
     * Returns the currently set desired video streaming parameters. If there haven't been any set,
     * the default options will be returned and set for this instance.
     * @return the desired video streaming parameters
     */
    public VideoStreamingParameters getDesiredVideoParams(){
        if(desiredVideoParams == null){
            desiredVideoParams = new VideoStreamingParameters();
        }
        return desiredVideoParams;
    }

    public void setAcceptedVideoParams(VideoStreamingParameters params){
        this.acceptedVideoParams = params;
    }

    public VideoStreamingParameters getAcceptedVideoParams(){
        return acceptedVideoParams;
    }

    private VideoStreamingProtocol getAcceptedProtocol() {
        // acquire default protocol (RAW)
        VideoStreamingProtocol protocol = new VideoStreamingParameters().getFormat().getProtocol();

        if (acceptedVideoParams != null) {
            VideoStreamingFormat format = acceptedVideoParams.getFormat();
            if (format != null && format.getProtocol() != null) {
                protocol = format.getProtocol();
            }
        }

        return protocol;
    }




    /* ***********************************************************************************************************************************************************************
     * *****************************************************************  IProtocol Listener  ********************************************************************************
     *************************************************************************************************************************************************************************/

    public void onProtocolMessageBytesToSend(SdlPacket packet) {
        //Log.d(TAG, "onProtocolMessageBytesToSend - " + packet.getTransportType());
        sdlProtocol.sendPacket(packet);
    }


    public void onProtocolSessionStartedNACKed(SessionType sessionType, byte sessionID, byte version, String correlationID, List<String> rejectedParams){
        onProtocolSessionNACKed(sessionType,sessionID,version,correlationID,rejectedParams);
    }

    public void onProtocolSessionNACKed(SessionType sessionType, byte sessionID, byte version, String correlationID, List<String> rejectedParams) {
        this.sessionListener.onProtocolSessionStartedNACKed(sessionType,
                sessionID, version, correlationID, rejectedParams);
        if(serviceListeners != null && serviceListeners.containsKey(sessionType)){
            CopyOnWriteArrayList<ISdlServiceListener> listeners = serviceListeners.get(sessionType);
            for(ISdlServiceListener listener:listeners){
                listener.onServiceError(this, sessionType, "Start "+ sessionType.toString() +" Service NAKed");
            }
        }
    }
    @Override
    public void onAuthTokenReceived(String authToken) {
        this.sessionListener.onAuthTokenReceived(authToken, sessionId);
    }

    /* Not supported methods from IProtocolListener */
    public void onProtocolHeartbeat(SessionType sessionType, byte sessionID) { /* Not supported */}
    public void onProtocolHeartbeatACK(SessionType sessionType, byte sessionID) {/* Not supported */}
    public void onResetOutgoingHeartbeat(SessionType sessionType, byte sessionID) {/* Not supported */}
    public void onResetIncomingHeartbeat(SessionType sessionType, byte sessionID) {/* Not supported */}

    /* ***********************************************************************************************************************************************************************
     * *****************************************************************  Security Listener  *********************************************************************************
     *************************************************************************************************************************************************************************/


    @Override
    public void onSecurityInitialized() {

        if (sdlProtocol != null && sdlSecurity != null)
        {
            List<SessionType> list = sdlSecurity.getServiceList();

            SessionType service;
            ListIterator<SessionType> iter = list.listIterator();

            while (iter.hasNext()) {
                service = iter.next();

                if (service != null)
                    sdlProtocol.startService(service, getSessionId(), true);

                iter.remove();
            }
        }
    }

    @Override
    public void stopStream(SessionType serviceType) {
        //Currently does nothing as streaming is not available. Also should only be managed through managers
    }


    /**
     * Check to see if a transport is available to start/use the supplied service.
     * @param sessionType the session that should be checked for transport availability
     * @return true if there is either a supported
     *         transport currently connected or a transport is
     *         available to connect with for the supplied service type.
     *         <br>false if there is no
     *         transport connected to support the service type in question and
     *          no possibility in the foreseeable future.
     */
    public boolean isTransportForServiceAvailable(SessionType sessionType){
        return sdlProtocol!=null && sdlProtocol.isTransportForServiceAvailable(sessionType);
    }


    @Deprecated
    public void clearConnection(){/* Not supported */}



}