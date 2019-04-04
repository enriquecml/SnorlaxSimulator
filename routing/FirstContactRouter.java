/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package routing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import core.Connection;
import core.DTNHost;
import core.Message;
import core.Settings;

/**
 * First contact router which uses only a single copy of the message 
 * (or fragments) and forwards it to the first available contact.
 */
public class FirstContactRouter extends ActiveRouter {
	
	private DTNHost dtn;
	/**
	 * Constructor. Creates a new message router based on the settings in
	 * the given Settings object.
	 * @param s The settings object
	 */
	public FirstContactRouter(Settings s) {
		super(s);
	}
	
	/**
	 * Copy constructor.
	 * @param r The router prototype where setting values are copied from
	 */
	protected FirstContactRouter(FirstContactRouter r) {
		super(r);
	}
	
	@Override
	protected int checkReceiving(Message m, DTNHost from) {
		if (isTransferring()) {
			return TRY_LATER_BUSY; // only one connection at a time
		}

		if (m.getTtl() <= 0 && m.getTo() != getHost()) {
			/* TTL has expired and this host is not the final recipient */
			return DENIED_TTL; 
		}

		
		if (!policy.acceptReceiving(from, getHost(), m)) {
			return MessageRouter.DENIED_POLICY;
		}
		
		/* remove oldest messages but not the ones being sent */
		if (!makeRoomForMessage(m.getSize())) {
			return DENIED_NO_SPACE; // couldn't fit into buffer -> reject
		}
		
		return RCV_OK;
	}
	
	
			
	@Override
	public void update() {
		super.update();
		if (isTransferring() || !canStartTransfer()) {
			return; 
		}
		
		if (exchangeDeliverableMessages() != null) {
			return; 
		}
		
		tryAllMessagesToAllConnections();
	}
	
	@Override
	protected void transferDone(Connection con) {
		/* don't leave a copy for the sender */
		String id=con.getMessage().getId();
		Message m = getMessage(id);
		if(m!=null) {
			DTNHost d=con.getToNode();
			if(d!=null && (Integer)d.getAddress()!=null) {
				try {
				m.getSent().add((Integer)d.getAddress());}
				catch (Exception e) {
					// TODO: handle exception
					System.out.println();
				}
			}		
		}

		getHost().setSents(getHost().getSents()+1);
	}
		
	@Override
	public FirstContactRouter replicate() {
		return new FirstContactRouter(this);
	}
	
	@Override
	protected int startTransfer(Message m, Connection con) {
		int retVal;
		
		if (!con.isReadyForTransfer()) {
			return TRY_LATER_BUSY;
		}
		
		if (!super.policy.acceptSending(getHost(), 
				con.getOtherNode(getHost()), con, m)) {
			return MessageRouter.DENIED_POLICY;
		}
		
		retVal = con.startTransfer(getHost(), m);
		if (retVal == RCV_OK) { // started transfer
			addToSendingConnections(con);
		}
		else if (deleteDelivered && retVal == DENIED_OLD && 
				m.getTo() == con.getOtherNode(this.getHost())) {
		}
		
		return retVal;
	}
	
	@Override
	protected Message tryAllMessages(Connection con, List<Message> messages) {
		
		int node_to = con.getToNode().getAddress();
		
		for (Message m : messages) {

			int retVal = startTransfer(m, con); 
			if (retVal == RCV_OK) {
				return m;	// accepted a message, don't try others
			}
			else if (retVal > 0) { 
				return null; // should try later -> don't bother trying others
			}
		}
		
		return null; // no message was accepted		
	}
	
	public void deleteMessagesSent() {
		Collection<Message> messages=this.getMessageCollection();
		ArrayList<String> borrar = new ArrayList<String>();
		for(Message m : messages) {
			if( getHost().canDeleteMessage(m)){
				borrar.add(m.getId());
			}
		}
		
		for(String s : borrar) {
			this.deleteMessage(s, false);
		}

	}
	
}