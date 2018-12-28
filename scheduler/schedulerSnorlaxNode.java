package scheduler;

import core.DTNHost;
import util.StatesMachine;

public class schedulerSnorlaxNode {

	private DTNHost dtn;
	private StatesMachine sm;
	
	public schedulerSnorlaxNode() {
	
		sm = new StatesMachine();
	}
	
	private boolean timeOfMainFunction() {
		return true;
	}
	
	private boolean timeOfScan() {
		return true;
	}
	
	private boolean timeOfSend() {
		return true;
	}
	
	private boolean timeOfOtherTasks() {
		return true;
	}
	
	private boolean timeOfAnnounceAndReceive() {
		return true;
	}
	
	private boolean timeOfSleep() {
		return true;
	}
	
	public void execute() {
		
		if(timeOfMainFunction()) {
			sm.next(0);
			sm.execute(dtn);
		}
		else if (timeOfSend()) {
			sm.next(1);
			sm.execute(dtn);	
		}
		else if(timeOfAnnounceAndReceive()) {
			sm.next(2);
			sm.execute(dtn);
		}
		else if(timeOfScan()) {
			sm.next(3);
			sm.execute(dtn);
		}
		else if(timeOfOtherTasks()) {
			sm.next(4);
			sm.execute(dtn);
		}
		else if(timeOfSleep()) {
			sm.next(5);
			sm.execute(dtn);
		}
		
	}

}
