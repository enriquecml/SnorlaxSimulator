package util;

import core.DTNHost;
import core.Message;

public class State {

	
	public State() {
		// TODO Auto-generated constructor stub
	}
	
	public void execute(DTNHost dtn) {
		
	}
	

}

class Sleep extends State {
    public void execute(DTNHost dtn) {
        System.out.println("Sleeping");
    }
}

class Scan extends State {
    public void execute(DTNHost dtn) {
    	dtn.scan();
        System.out.println("Scanning");
    }
}

class Send extends State {
    public void execute(DTNHost dtn) {
        System.out.println("Sending Messages");
        int next_node = dtn.getSchedulerNode().getNext_node();
        
    }
}

class mainFunction extends State {
	private int i=1;
    public void execute(DTNHost dtn) {
        System.out.println("executing main function");
        dtn.createNewMessage(new Message(dtn,null,dtn.getAddress()+":"+i,1024));
    }
}

class otherTasks extends State {
    public void execute(DTNHost dtn) {
        System.out.println("executing tasks");
    }
}

class Receive extends State {
    public void execute(DTNHost dtn) {
        System.out.println("executing announce & receive function");
    }
}


