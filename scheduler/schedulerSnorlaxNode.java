package scheduler;

import java.util.List;
import java.util.Random;

import core.DTNHost;
import core.Detail;
import core.SimClock;
import util.StatesMachine;

public class schedulerSnorlaxNode {

	private DTNHost dtn;
	private StatesMachine sm;
	private int n_messages_created;
	private int counter_announce_and_receive;
	private int next_node;
	private int timescanned;
	Random aleatorio; 
	public schedulerSnorlaxNode(DTNHost _dtn) {
	
		sm = new StatesMachine();
		dtn = _dtn;
		n_messages_created = 0;
		counter_announce_and_receive=0;
		aleatorio= new Random(System.currentTimeMillis()*dtn.getAddress());
		timescanned=60;
	}
	
	private int internal_time_elapsed() {
		return SimClock.getInstance().getIntTime()-dtn.getInternal_node_time();
	}
	
	private boolean timeOfMainFunction() {
		if(internal_time_elapsed() > (5 * n_messages_created)) {
			n_messages_created++;
			return true;
		}
		return false;
	}
	
	private boolean timeOfScan() {
		//cuando se hayan perdido todos los encuentros posibles
		List<Integer> nodes = dtn.getEncounter_node();
		if(nodes.size()==0)
			timescanned=0;
		return nodes.size()==0 || timescanned<60;
	}
	
	private boolean timeOfSend() {
		//lanzar algoritmo de aleatoriedad sobre los nodos disponibles (para ese momento) y que aún no se haya conectado simclock time
		List<Integer> encounter_node = dtn.getEncounter_node();
		List<Integer> encounter_time = dtn.getEncounter_time();
		List<Integer> rates_node = dtn.getRates_node();

		List<Detail> details_list = dtn.getDetails_list();
		for(int i=0;i<encounter_node.size();i++) {
			for(int j=0;j<details_list.size();j++) {
				if(details_list.get(j).getNode_address() == encounter_node.get(i).intValue()) {
					int time_elapsed = internal_time_elapsed() - encounter_time.get(i).intValue();
					int time_from_start_period = time_elapsed % details_list.get(j).getBroadcast_periode();
					if(time_from_start_period<=details_list.get(j).getTime_on()) {
						if(0==aleatorio.nextInt(rates_node.get(i)+1)) {
							setNext_node(details_list.get(j).getNode_address());
							return true;
						}
					}
						
				}
			}
		}
		return false;
	}
	
	private boolean timeOfOtherTasks() {
		//Aquí hay que desarrollar listas , ver ¿como mover los mensajes recibidos por the simulator one?
		//eliminar mensajes duplicados y borrar aquellos que han sido enviados a toda la lista de vecinos
		return true;
	}
	
	private boolean timeOfAnnounceAndReceive() {
		
		if(counter_announce_and_receive >= 60) {
			counter_announce_and_receive=0;
			return true;
		}
		return false;
	}
	
	private boolean timeOfSleep() {
		return true;
	}
	
	public void execute() {
		counter_announce_and_receive++;
		if(dtn.getExit_state()<internal_time_elapsed()) {
		
		
		if(timeOfMainFunction()) {
			dtn.setExit_state(internal_time_elapsed()+1);
			sm.next(0);
			sm.execute(dtn);
		}
		else if (timeOfSend()) {
			dtn.setExit_state(internal_time_elapsed()+8);
			sm.next(1);
			sm.execute(dtn);	
		}
		else if(timeOfAnnounceAndReceive()) {
			sm.next(4);
			sm.execute(dtn);
			dtn.setExit_state(internal_time_elapsed()+8+8*aleatorio.nextInt(2));
			sm.next(2);
			sm.execute(dtn);
		}
		else if(timeOfScan()) {
			dtn.setExit_state(internal_time_elapsed()+1);
			timescanned++;
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
		else {
			sm.execute(dtn);
		}
	}

	public int getNext_node() {
		return next_node;
	}

	public void setNext_node(int next_node) {
		this.next_node = next_node;
	}

}
