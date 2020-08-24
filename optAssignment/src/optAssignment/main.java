/*
 * 
 */
package optAssignment;

import java.util.ArrayList;
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
            if (ran.nextDouble()<0.15) 
            { 
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
                System.out.print(randTime + ",");
            } 
            System.out.println();
        } 

        printList(orders);
	}

	public static void printList(ArrayList<Order> list){
	    for(Order x : list){
	        System.out.println(x+"  ");
	    }
	}
	
}