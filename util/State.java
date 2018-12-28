package util;

import core.DTNHost;

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
        System.out.println("Scanning");
    }
}

class Send extends State {
    public void execute(DTNHost dtn) {
        System.out.println("Sending Messages");
    }
}

class mainFunction extends State {
    public void execute(DTNHost dtn) {
        System.out.println("executing main function");
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


