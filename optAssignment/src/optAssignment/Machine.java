package optAssignment;

import java.util.ArrayList;

public class Machine {

	int id;
	double workTime;
	ArrayList<Order> queue = new ArrayList<Order>();
	
	public Machine() {
		this.id = 0;
		this.workTime = 0;
		this.queue = new ArrayList<Order>();
	}
	
	public Machine(int id) {
		this.id = id;
		this.workTime = 0;
		this.queue = new ArrayList<Order>();
	}

	public ArrayList<Order> getQueueById(int id) {
		Machine m = new Machine();
		if(m.id == 1) return m.queue;
		if(m.id == 2) return m.queue;
		if(m.id == 3) return m.queue;
		if(m.id == 4) return m.queue;
		if(m.id == 5) return m.queue;
		return queue;
		



	}

}

