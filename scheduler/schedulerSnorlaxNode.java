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
	private int next_node;
	
	public schedulerSnorlaxNode(DTNHost _dtn) {
	
		sm = new StatesMachine();
		dtn = _dtn;
		n_messages_created = 1;
	}
	
	private boolean timeOfMainFunction() {
		if(dtn.getInternal_node_time()/n_messages_created < 15000) {
			n_messages_created++;
			return true;
		}
		return false;
	}
	
	private boolean timeOfScan() {
		//cuando se hayan perdido todos los encuentros posibles
		List<Integer> nodes = dtn.getEncounter_node();
		return nodes.size()==0;
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
					int time_elapsed = SimClock.getInstance().getIntTime() - encounter_time.get(i).intValue();
					int time_from_start_period = time_elapsed % details_list.get(j).getBroadcast_periode();
					if(time_from_start_period<=details_list.get(j).getTime_on()) {
						Random aleatorio = new Random(System.currentTimeMillis());
						if(0==aleatorio.nextInt(rates_node.get(i))) {
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

	public int getNext_node() {
		return next_node;
	}

	public void setNext_node(int next_node) {
		this.next_node = next_node;
	}

}
