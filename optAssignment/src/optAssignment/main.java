package optAssignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class main {

	
	public static void main(String[] args) {
		
		ArrayList<Order> orders = new ArrayList<>(); 
        int bday = 19092000; 
        Random ran = new Random(bday);
        int totOrders = 100; 
        for (int i = 0 ; i < totOrders; i++) 
        { 
            int qq = 100 + ran.nextInt(401);             
            boolean drk = false;            
            if (ran.nextDouble()<0.15) { 
                drk = true; 
            } 
            Order o = new Order(i + 1, qq, drk); 
            orders.add(o); 
        } 

       
        double [][] transitionTime = new double[totOrders][totOrders]; 
        for (int i = 0 ; i < totOrders; i++) 
        { 
            for (int j = 0 ; j < totOrders; j++) 
            { 
                double randTime = 10 + 20 * ran.nextDouble(); 
                randTime = Math.round(randTime * 100.0) / 100.0; 
                if (i == j) 
                { 
                    randTime = 0; 
                } 
                transitionTime[i][j] = randTime; 
                //System.out.print(randTime + ",");
            } 
           // System.out.println();
        } 
                
        Collections.sort(orders);
        
        add15minsToTransitionTable(orders, transitionTime);
        
        //init Machines
        Machine m1 = new Machine(1);
        insertBiggestOrder(orders, m1.queue, transitionTime);
        updateWorkTime(m1,0);
        Machine m2 = new Machine(2);
        insertBiggestOrder(orders, m2.queue, transitionTime);
        updateWorkTime(m2,0);
        Machine m3 = new Machine(3);
        insertBiggestOrder(orders, m3.queue, transitionTime);
        updateWorkTime(m3,0);
        Machine m4 = new Machine(4);
        insertBiggestOrder(orders, m4.queue, transitionTime);
        updateWorkTime(m4,0);
        Machine m5 = new Machine(5);
        insertBiggestOrder(orders, m5.queue, transitionTime);
        updateWorkTime(m5,0);
        //end init Machines
        
        while(orders.size() > 0) {
        	Machine freeMachine = shortestWorkTime(m1,m2,m3,m4,m5);
        	shortestTT(freeMachine,transitionTime,orders);
        }
    	
        double maxWT = maxWorkTime(m1,m2,m3,m4,m5);
        System.out.println("Result(min) is: " + maxWT);
       
        printQueueMachines(m1);
        printQueueMachines(m2);
        printQueueMachines(m3);
        printQueueMachines(m4);
        printQueueMachines(m5);

	}
	
	public static void printQueueMachines(Machine m){
		ArrayList<Order> queue = m.queue;
		ArrayList<Integer> queueOnlyID = new ArrayList<>();
		int i = 0;
		while(i < queue.size()) {
			Order o = queue.get(i);
			queueOnlyID.add(o.ID);;
			++i;
		}
		System.out.println("Machine " + m.id + ": " + queueOnlyID);
	}



	public static double maxWorkTime(Machine m1, Machine m2, Machine m3, Machine m4, Machine m5) {
		double max = m1.workTime;
		if (m2.workTime > max) max = m2.workTime;
		if (m3.workTime > max) max = m3.workTime;
		if (m4.workTime > max) max = m4.workTime;
		if (m5.workTime > max) max = m5.workTime;
		return max;
	}

	public static Machine shortestWorkTime(Machine m1, Machine m2, Machine m3, Machine m4, Machine m5) {
		Machine minMachine = m1;
		if(m2.workTime < minMachine.workTime) minMachine = m2;
		if(m3.workTime < minMachine.workTime) minMachine = m3;
		if(m4.workTime < minMachine.workTime) minMachine = m4;
		if(m5.workTime < minMachine.workTime) minMachine = m5;
		return minMachine;
	}

	public static void add15minsToTransitionTable(ArrayList<Order> orders, double [][]transitionTime) 
	{
		for(int i=0; i < orders.size(); i++){
			Order o1 = orders.get(i);
			for(int j=0; j < orders.size(); j++){
				Order o2 = orders.get(j);
				if (o1.dark != o2.dark) {
					transitionTime[i][j] = Math.round((transitionTime[i][j] + 15) * 100.0) / 100.0;
				}
			}
		}
	}
	
	 public static void insertBiggestOrder(ArrayList<Order> list, ArrayList<Order> machine, double[][] transitionTime){
     	Order o = list.get(0);
     	machine.add(o);
     	setZeroTT(o.ID - 1, transitionTime);
     	list.remove(0);
     }

	public static void printList(ArrayList<Order> list){
	    for(Order x : list){
	        System.out.println(x+"  ");
	    }
	}
	
	public static void printTransition(double [][] transitionTime) {
		for(int x = 0 ; x < transitionTime.length; x++) {
        	for(int y = 0 ; y < transitionTime.length; y++) {
        		System.out.print(transitionTime[x][y] + ",");
        	}
        	System.out.println();
		}
	}
	
	public static void updateWorkTime(Machine m,double minTT) {
		double quantity = m.queue.get(m.queue.size()-1).quantity;
		m.workTime = Math.round((minTT + m.workTime + (quantity * 0.1)) * 100.0) / 100.0;
	}
	
	public static void shortestTT(Machine m, double[][] transitionTime, ArrayList<Order> orders) {
		int lastOrderID = m.queue.get(m.queue.size()-1).ID - 1;

		int toBeRemoved = 0;
		double minTT = Double.MAX_VALUE;
		
		for (int j = 0 ; j < transitionTime.length; j++) {
			if (transitionTime[lastOrderID][j] < minTT && transitionTime[lastOrderID][j] != 0) {
				minTT = transitionTime[lastOrderID][j];
				toBeRemoved = j;
			}
		}
		Order nextOrder = removeOrder(toBeRemoved + 1, orders);
		setZeroTT(toBeRemoved, transitionTime);
		m.queue.add(nextOrder);
		updateWorkTime(m,minTT);
		
	}
	

	private static Order removeOrder(int toBeRemoved,ArrayList<Order> o) {
		Order tempOrder = o.get(0);
		int i = 0;
		
		while(i < o.size()) {
			if(tempOrder.ID == toBeRemoved) {
				o.remove(i);
				break;
			}
			
			tempOrder = o.get(++i);
		}
		return tempOrder;
	}

	public static void setZeroTT(int toBeRemoved,double[][] transitionTime) {
		for(int i = 0 ; i < transitionTime.length; i++) {
			transitionTime[i][toBeRemoved] = 0.0;
		}
	}
}