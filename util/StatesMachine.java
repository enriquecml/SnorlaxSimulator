package util;

import core.DTNHost;

public class StatesMachine {

    private State[] states = {new mainFunction(),new Send(), new Receive(), new Scan(),new otherTasks(),new Sleep()};

    private int current = 0;

    public void next(int index) {
        current = index;
    }
	
    public void execute(DTNHost dtn) {
    	states[current].execute(dtn);
    }
    
	public StatesMachine() {
		// TODO Auto-generated constructor stub
	}

}
