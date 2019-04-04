package util;

import core.DTNHost;
import core.Message;
import routing.FirstContactRouter;

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
    	if(!dtn.exit()) {
    		dtn.scan();
    	}
        System.out.println("Scanning");
    }
}

class Send extends State {
	private boolean connect;
    public void execute(DTNHost dtn) {
        System.out.println("Sending Messages");
        if(!dtn.exit()) {

            int next_node = dtn.getSchedulerNode().getNext_node();
            if(connect==false)
            	connect=dtn.tryConnect(next_node);
            if(connect) {
            	dtn.getRouter().update();
            }
        }
        else { 
        	if(!connect) {
        		dtn.deleteEncounterNode(dtn.getSchedulerNode().getNext_node());
        	}
        	connect=false;
        }
        

    }
}

class mainFunction extends State {
	private int i=1;
    public void execute(DTNHost dtn) {
        System.out.println("executing main function");
        if(!dtn.exit()) {
            System.out.println("mensaje creado");
            Message message = new Message(dtn,null,dtn.getAddress()+":"+i,1024);
            
            dtn.getMessages().add(message);
            
            dtn.createNewMessage(message);
            i++;
        }
    }
}

class otherTasks extends State {
    public void execute(DTNHost dtn) {
        System.out.println("executing tasks");
        FirstContactRouter fr = (FirstContactRouter) dtn.getRouter();
        fr.deleteMessagesSent();
    }
}

class Receive extends State {
    public void execute(DTNHost dtn) {
        System.out.println("executing announce & receive function");
        if(!dtn.exit())
        	dtn.up_first_interface();
        else
        	dtn.down_first_interface();
    }
}


