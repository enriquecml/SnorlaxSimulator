package core;

public class Detail {
	private int node_address;
	private int time_on;
	private int broadcast_periode;
	
	public Detail(int node_address, int time_on, int broadcast_periode) {
		this.node_address = node_address;
		this.time_on = time_on;
		this.broadcast_periode = 60;
	}
	
	public int getNode_address() {
		return node_address;
	}
	public void setNode_address(int node_address) {
		this.node_address = node_address;
	}
	public int getTime_on() {
		return time_on;
	}
	public void setTime_on(int time_on) {
		this.time_on = time_on;
	}
	public int getBroadcast_periode() {
		return broadcast_periode;
	}
	public void setBroadcast_periode(int broadcast_periode) {
		this.broadcast_periode = broadcast_periode;
	}
	
	
}