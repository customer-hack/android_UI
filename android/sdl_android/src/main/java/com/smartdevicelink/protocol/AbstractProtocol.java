/*
 * Copyright (c) 2017 - 2019, SmartDeviceLink Consortium, Inc.
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
 * Neither the name of the SmartDeviceLink Consortium, Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from this 
 * software without specific prior written permission.
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
package com.smartdevicelink.protocol;


import com.smartdevicelink.protocol.WiProProtocol.MessageFrameAssembler;
import com.smartdevicelink.protocol.enums.SessionType;

import java.util.List;

/**
 * @see SdlProtocol
 */
@Deprecated
public abstract class AbstractProtocol {
	private static final String SDL_LIB_TRACE_KEY = "42baba60-eb57-11df-98cf-0800200c9a66";
	
	private IProtocolListener _protocolListener = null;
	//protected IProtocolListener ProtocolListener() { return _protocolListener; }
	
	// Lock to ensure all frames are sent uninterupted 
	private Object _frameLock = new Object();

	// Caller must provide a non-null IProtocolListener interface reference.
	public AbstractProtocol(IProtocolListener protocolListener) {
		if (protocolListener == null) {
    		throw new IllegalArgumentException("Provided protocol listener interface reference is null");
		} // end-if
		
		_protocolListener = protocolListener;
	} // end-ctor


	// This method receives a protocol message (e.g. RPC, BULK, etc.) and processes
	// it for transmission over the transport.  The results of this processing will
	// be sent to the onProtocolMessageBytesToSend() method on protocol listener
	// interface.  Note that the ProtocolMessage itself contains information
	// about the type of message (e.g. RPC, BULK, etc.) and the protocol session
	// over which to send the message, etc.
	public abstract void SendMessage(ProtocolMessage msg);

	public abstract int getMtu();
	public abstract long getMtu(SessionType type);
	
	public abstract void handlePacketReceived(SdlPacket packet);
	
	// This method starts a protocol session.  A corresponding call to the protocol
	// listener onProtocolSessionStarted() method will be made when the protocol
	// session has been established.
	public abstract void StartProtocolSession(SessionType sessionType);
	
	public abstract void StartProtocolService(SessionType sessionType, byte sessionID, boolean isEncrypted);

	public abstract void EndProtocolService(SessionType serviceType, byte sessionID);
	// This method ends a protocol session.  A corresponding call to the protocol
	// listener onProtocolSessionEnded() method will be made when the protocol
	// session has ended.
	public abstract void EndProtocolSession(SessionType sessionType, byte sessionID, int hashID);
	
    // TODO REMOVE
    // This method sets the interval at which heartbeat protocol messages will be
    // sent to SDL.
    public abstract void SetHeartbeatSendInterval(int heartbeatSendInterval_ms);

    // This method sets the interval at which heartbeat protocol messages are
    // expected to be received from SDL.
    public abstract void SetHeartbeatReceiveInterval(int heartbeatReceiveInterval_ms);
    
    public abstract void SendHeartBeat(byte sessionID);

	public abstract void SendHeartBeatACK(byte sessionID);
	
	// This method is called whenever the protocol receives a complete frame
	protected void handleProtocolFrameReceived(SdlPacket packet, MessageFrameAssembler assembler) {
	//FIXME	SdlTrace.logProtocolEvent(InterfaceActivityDirection.Receive, header, data, 
	//			0, packet.dataSize, SDL_LIB_TRACE_KEY);
		
		assembler.handleFrame(packet);
	}
	
    private synchronized void resetOutgoingHeartbeat(SessionType sessionType, byte sessionID) {
        if (_protocolListener != null) {
            _protocolListener.onResetOutgoingHeartbeat(sessionType,sessionID);
        }
    }
	
    private synchronized void resetIncomingHeartbeat(SessionType sessionType, byte sessionID) {
        if (_protocolListener != null) {
            _protocolListener.onResetIncomingHeartbeat(sessionType,sessionID);
        }
    }

	// This method is called whenever a protocol has an entire frame to send
    /**
     * SdlPacket should have included payload at this point.
     * @param header
     */
	protected void handlePacketToSend(SdlPacket header) {
	//FIXME	SdlTrace.logProtocolEvent(InterfaceActivityDirection.Transmit, header, data, 
	//			offset, length, SDL_LIB_TRACE_KEY);
	resetOutgoingHeartbeat(SessionType.valueOf((byte)header.getServiceType()), (byte)header.getSessionId());

		synchronized(_frameLock) {
			
			//byte[] frameHeader = header.constructPacket();
			if(header!=null){
				_protocolListener.onProtocolMessageBytesToSend(header);
			}//TODO else log out error
			
		}
	}

	
	// This method handles received protocol messages. 
	protected void handleProtocolMessageReceived(ProtocolMessage message) {
		_protocolListener.onProtocolMessageReceived(message);
	}
	
	// This method handles the end of a protocol session. A callback is 
	// sent to the protocol listener.
	protected void handleProtocolSessionEndedNACK(SessionType sessionType,
			byte sessionID, String correlationID) {
		_protocolListener.onProtocolSessionEndedNACKed(sessionType, sessionID, correlationID);
	}	
	
	// This method handles the end of a protocol session. A callback is 
	// sent to the protocol listener.
	protected void handleProtocolSessionEnded(SessionType sessionType,
			byte sessionID, String correlationID) {
		_protocolListener.onProtocolSessionEnded(sessionType, sessionID, correlationID);
	}
	
	// This method handles the startup of a protocol session. A callback is sent
	// to the protocol listener.
	protected void handleProtocolSessionStarted(SessionType sessionType,
			byte sessionID, byte version, String correlationID, int hashID, boolean isEncrypted) {
		_protocolListener.onProtocolSessionStarted(sessionType, sessionID, version, correlationID, hashID, isEncrypted);
	}

	protected void handleProtocolSessionNACKed(SessionType sessionType,
			byte sessionID, byte version, String correlationID, List<String> rejectedParams) {
		_protocolListener.onProtocolSessionNACKed(sessionType, sessionID, version, correlationID, rejectedParams);
	}
	
	// This method handles protocol errors. A callback is sent to the protocol
	// listener.
	protected void handleProtocolError(String string, Exception ex) {
		_protocolListener.onProtocolError(string, ex);
	}
    protected void handleProtocolHeartbeat(SessionType sessionType, byte sessionID) {
    	SendHeartBeatACK(sessionID);
    	_protocolListener.onProtocolHeartbeat(sessionType, sessionID);
    }
    protected void handleProtocolHeartbeatACK(SessionType sessionType, byte sessionID) {
        _protocolListener.onProtocolHeartbeatACK(sessionType, sessionID);
    }
    protected void handleProtocolServiceDataACK(SessionType sessionType, int dataSize, byte sessionID) {
        _protocolListener.onProtocolServiceDataACK(sessionType, dataSize, sessionID);
    }
    protected void onResetIncomingHeartbeat(SessionType sessionType, byte sessionID) {
		resetIncomingHeartbeat(sessionType, sessionID);
    }

} // end-class
