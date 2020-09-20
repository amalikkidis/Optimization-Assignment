package optAssignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class main {
	
	/*this method prints the queue of machine
	 * the queue consists only from the id's of each order
	 */
	
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


	/*
	 * method that finds the machine that spends the most time
	 * (finishes last)
	 */
	public static double maxWorkTime(Machine m1, Machine m2, Machine m3, Machine m4, Machine m5) {
		double max = m1.workTime;
		
		if (m2.workTime > max) max = m2.workTime;
		if (m3.workTime > max) max = m3.workTime;
		if (m4.workTime > max) max = m4.workTime;
		if (m5.workTime > max) max = m5.workTime;
		return max;
	}
	
	/*
	 * method that finds the machine that spends the most time and returns the time
	 * (finishes last)
	 */
	public static ArrayList<Order> SlowestMachine(Machine m1, Machine m2, Machine m3, Machine m4, Machine m5) {
		Machine max = m1;
		
		if (m2.workTime > max.workTime) max = m2;
		if (m3.workTime > max.workTime) max = m3;
		if (m4.workTime > max.workTime) max = m4;
		if (m5.workTime > max.workTime) max = m5;
		return max.queue;
	}

	/*
	 * finds the machine that spends the least time and returns the machine
	 * (finishes first)
	 */
	public static Machine shortestWorkTime(Machine m1, Machine m2, Machine m3, Machine m4, Machine m5) {
		Machine minMachine = m1;
		if(m2.workTime < minMachine.workTime) minMachine = m2;
		if(m3.workTime < minMachine.workTime) minMachine = m3;
		if(m4.workTime < minMachine.workTime) minMachine = m4;
		if(m5.workTime < minMachine.workTime) minMachine = m5;
		return minMachine;
	}
		
	/*
	 * method that gets the current transition time and adds 15minutes when we
	 * go from a dark to bright color , or a bright to dark color
	 */
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
	/*
	 * method that gets the list with the orders left,
	 * and removes the first one(currently the biggest order), and 
	 * assigns it to a specific machine
	 */
	 public static void insertBiggestOrder(ArrayList<Order> list, ArrayList<Order> machine, double[][] transitionTime){
     	Order o = list.get(0);
     	machine.add(o);
     	setZeroTT(o.ID - 1, transitionTime);
     	list.remove(0);
     }
	 //method that print a list of orders
	public static void printList(ArrayList<Order> list){
	    for(Order x : list){
	        System.out.println(x+"  ");
	    }
	}
	
	//method that prints the transition table 
	public static void printTransition(double [][] transitionTime) {
		for(int x = 0 ; x < transitionTime.length; x++) {
        	for(int y = 0 ; y < transitionTime.length; y++) {
        		System.out.print(transitionTime[x][y] + ",");
        	}
        	System.out.println();
		}
	}
	
	//method that updates the total time that a specific machine works
	public static void updateWorkTime(Machine m,double minTT) {
		double quantity = m.queue.get(m.queue.size()-1).quantity;
		m.workTime = Math.round((minTT + m.workTime + (quantity * 0.1)) * 100.0) / 100.0;
	}
	
	//method that finds the order with the lowest transition time for a specific machine 
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
	
	//method that removes an order 
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

	//make a specific transition equal to zero 
	public static void setZeroTT(int toBeRemoved,double[][] transitionTime) {
		for(int i = 0 ; i < transitionTime.length; i++) {
			transitionTime[i][toBeRemoved] = 0.0;
		}
	}
	
	
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
        for (int i = 0 ; i < totOrders; i++){ 
            for (int j = 0 ; j < totOrders; j++){ 
                double randTime = 10 + 20 * ran.nextDouble(); 
                randTime = Math.round(randTime * 100.0) / 100.0; 
                if (i == j){ 
                    randTime = 0; 
                } 
                transitionTime[i][j] = randTime; 
            } 
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

        //for the D part 2
        for(int i=0;i<transitionTime.length;i++) {
        	for(int j=0;j<transitionTime[0].length;j++) {
        		distanceMatrix[i][j] = transitionTime[i][j];
        	}
        }
        
        Solution s = new Solution();
        s.cost = maxWT;
        s.orders = SlowestMachine(m1,m2,m3,m4,m5);
        Solution bestSolution = VND(s);
        System.out.println(bestSolution.cost);
        System.out.print("[");
        for(int i=0;i<bestSolution.orders.size();i++)
        	System.out.print(bestSolution.orders.get(i).ID + ",");
        System.out.print("]");
}
	
	//////////////////code for the VND 
	static double [][] distanceMatrix = new double[100][100];
    private static class RelocationMove 
    {
        int positionOfRelocated;
        int positionToBeInserted;
        double moveCost;
        
        public RelocationMove() 
        {
        }
    }

    private static class SwapMove 
    {
        int positionOfFirst;
        int positionOfSecond;
        
        double moveCost;
        
        public SwapMove() 
        {
            
        }
    }
    
    private static class TwoOpt {
    
        int positionOfFirst;
        int positionOfSecond;
        double moveCost;
    }
    
    private static class Solution 
    {
       double cost;
       ArrayList <Order> orders = new ArrayList<Order>();
       
       public Solution() 
       {
           orders = new ArrayList<Order>();
    	   cost = 0;
       }
    }
    
    private static Solution cloneSolution(Solution sol) 
    {
        Solution out = new Solution();
        
        out.cost = sol.cost;

        
        //Need to clone: arraylists are objects
        for (int i = 0 ; i < sol.orders.size(); i++)
        {
            Order o = sol.orders.get(i);
            out.orders.add(o);
        }
        
        return out;
    }
    
	private static Solution VND(Solution s) {
	        
        // What would happen if Solution bestSolution = s;
        Solution bestSolution = cloneSolution(s);

        boolean terminationCondition = false;
        int localSearchIterator = 0;
        
        RelocationMove rm = new RelocationMove();
        SwapMove sm = new SwapMove();
        TwoOpt top = new TwoOpt();
        
        int k = 1;
        int kmax = 3;

        while (k <= kmax)
        {
            InitializeMoves(rm, sm, top);
            FindBestNeighbor(k, s, rm, sm, top);
            
            if (MoveIsImproving(k, rm, sm, top))
            {
                ApplyMove(k, s, rm, sm, top);
                k = 1;
            }
            else
            {
                k = k + 1;
            }
        }
        
        return bestSolution;
    }

    private static void InitializeMoves(RelocationMove rm, SwapMove sm, TwoOpt top) {
        //Initialize the relocation move rm
        InitializeTheRelocationMove(rm);
        //Initialize the swap move sm
        InitializeTheSwapMove(sm);
        //Initialize the 2 opt move
        InitializeTheTwoOptMove(top);
    }

    private static void InitializeTheRelocationMove(RelocationMove rm) {
        rm.positionOfRelocated = -1;
        rm.positionToBeInserted = -1;
        rm.moveCost = Double.MAX_VALUE;
    }

    private static void InitializeTheSwapMove(SwapMove sm) {
        sm.positionOfFirst = -1;
        sm.positionOfSecond = -1;
        sm.moveCost = Double.MAX_VALUE;
    }

    private static void InitializeTheTwoOptMove(TwoOpt top) 
    {
        top.positionOfFirst = -1;
        top.positionOfSecond = -1;
        top.moveCost = Double.MAX_VALUE;
    }
    
    private static void FindBestNeighbor(int k, Solution s, RelocationMove rm, SwapMove sm, TwoOpt top) 
    {
        if (k == 1)
        {
            findBestRelocationMove(rm, s);
        }
        else if (k == 2)
        {
            findBestSwapMove(sm, s);
        }
        else if (k == 3)
        {
            findBestTwoOptMove(top, s);
        }
    }
    
    private static void findBestRelocationMove(RelocationMove rm, Solution s) 
    {
        double bestMoveCost = Double.MAX_VALUE;
        
        for (int relIndex = 1; relIndex < s.orders.size()- 1; relIndex++)
        {
            Order A = s.orders.get(relIndex - 1);
            Order B = s.orders.get(relIndex);
            Order C = s.orders.get(relIndex + 1);
            
            for (int afterInd = 0; afterInd < s.orders.size() -1; afterInd ++)
            {
                // Why do we have to write this line?
                // This line has to do with the nature of the 1-0 reliocation
                // If afterInd == relIndex -> this wouls mean the solution remains unaffected
                // If afterInd == relIndex - 1 -> this wouls mean the solution remains unaffected
                if (afterInd != relIndex && afterInd != relIndex - 1)
                {
                    Order F = s.orders.get(afterInd);
                    Order G = s.orders.get(afterInd + 1);
                    
                    double costRemoved1 = distanceMatrix[A.ID][B.ID] + distanceMatrix[B.ID][C.ID];
                    double costRemoved2 = distanceMatrix[F.ID][G.ID];
                    double costRemoved = costRemoved1 + costRemoved2;
                    
                    double costAdded1 = distanceMatrix[A.ID][C.ID];
                    double costAdded2 = distanceMatrix[F.ID][B.ID] + distanceMatrix[B.ID][G.ID];
                    double costAdded = costAdded1 + costAdded2;
                    
                    double moveCost = costAdded - costRemoved;
                    
                    if (moveCost < bestMoveCost)
                    {
                        bestMoveCost = moveCost;
                        
                        rm.positionOfRelocated = relIndex;
                        rm.positionToBeInserted = afterInd;
                        rm.moveCost = moveCost;
                    }
                }
            }
        }
    }
    
    private static void findBestSwapMove(SwapMove sm, Solution s) 
    {
        double bestMoveCost = Double.MAX_VALUE;
        
        for (int firstIndex = 1; firstIndex < s.orders.size() - 1; firstIndex++)
        {
            Order A = s.orders.get(firstIndex - 1);
            Order B = s.orders.get(firstIndex);
            Order C = s.orders.get(firstIndex + 1);
            
            //Why do we have selected secIndex to start from firstIndex + 1?
            //Symmetric move!!! --- No reason to swap pair B and E and then E and B !!! --- It's the same thing!!!
            for (int secondInd = firstIndex + 1; secondInd < s.orders.size() -1; secondInd ++)
            {
                Order D = s.orders.get(secondInd - 1);
                Order E = s.orders.get(secondInd);
                Order F = s.orders.get(secondInd + 1);
                
                //Based on the mechanics of the move two cases may arise
                //1. the swapped are consecutive Orders (secondInd == firstIndex + 1), in other words B == D and C == E
                //2. the swapped are non-consecutive Orders (secondInd > firstIndex + 1)

                double costRemoved = 0; 
                double costAdded = 0;
                
                if (secondInd == firstIndex + 1)
                {
                    costRemoved =  distanceMatrix[A.ID][B.ID] + distanceMatrix[B.ID][C.ID] +  distanceMatrix[C.ID][F.ID];
                    costAdded = distanceMatrix[A.ID][C.ID] + distanceMatrix[C.ID][B.ID] +  distanceMatrix[B.ID][F.ID] ;
                }
                else
                {
                    double costRemoved1 =  distanceMatrix[A.ID][B.ID] + distanceMatrix[B.ID][C.ID] ;
                    double costRemoved2 =  distanceMatrix[D.ID][E.ID] + distanceMatrix[E.ID][F.ID] ;
                    costRemoved = costRemoved1 + costRemoved2;
                    
                    double costAdded1 =  distanceMatrix[A.ID][E.ID] + distanceMatrix[E.ID][C.ID] ;
                    double costAdded2 =  distanceMatrix[D.ID][B.ID] + distanceMatrix[B.ID][F.ID] ;
                    costAdded = costAdded1 + costAdded2 ;
                }
                
                double moveCost = costAdded - costRemoved;
                    
                if (moveCost < bestMoveCost)
                {
                    bestMoveCost = moveCost;

                    sm.positionOfFirst = firstIndex;
                    sm.positionOfSecond = secondInd;
                    sm.moveCost = moveCost;
                }
            }
        }
    }

    private static void findBestTwoOptMove(TwoOpt top, Solution s) 
    {     
        for (int firstIndex = 0; firstIndex < s.orders.size() - 1; firstIndex++)
        {
            Order A = s.orders.get(firstIndex);
            Order B = s.orders.get(firstIndex + 1);
            
            for (int secondIndex = firstIndex + 2; secondIndex < s.orders.size() - 1; secondIndex ++)
            {
                Order K = s.orders.get(secondIndex);
                Order L = s.orders.get(secondIndex + 1);

                if (firstIndex == 0 && secondIndex == s.orders.size() - 2)
                {
                    continue;
                }
                
                double costAdded = distanceMatrix[A.ID][K.ID] + distanceMatrix[B.ID][L.ID];
                double costRemoved = distanceMatrix[A.ID][B.ID] + distanceMatrix[K.ID][L.ID];
                
                double moveCost = costAdded - costRemoved;
                
                if (moveCost < top.moveCost)
                {
                    top.moveCost = moveCost;
                    top.positionOfFirst = firstIndex;
                    top.positionOfSecond = secondIndex;
                }
            }
        }
    }
	
    private static boolean MoveIsImproving(int k, RelocationMove rm, SwapMove sm, TwoOpt top) 
    {
        if (k == 1)
        {
            if (rm.moveCost < 0)
            {
                return true;
            }
        }
        else if (k == 2)
        {
            if (sm.moveCost < 0)
            {
                return true;
            }
        }
        else if (k == 3)
        {
            if (top.moveCost < 0)
            {
                return true;
            }
        }
        
        return false;
    }

    private static void ApplyMove(int k, Solution s, RelocationMove rm, SwapMove sm, TwoOpt top) {
        
        if (k == 1)
        {
            applyRelocationMove(rm, s);
        }
        else if (k == 2)
        {
            applySwapMove(sm, s);
        }
        if (k == 3)
        {
            applyTwoOptMove(top, s);
        }
    }

    private static void applyTwoOptMove(TwoOpt top, Solution s) 
    {
        ArrayList<Order> modifiedRt = new ArrayList<Order>();
        
        for (int i = 0; i <= top.positionOfFirst; i++)
        {
            modifiedRt.add(s.orders.get(i));
        }
        for (int i = top.positionOfSecond; i > top.positionOfFirst; i--)
        {
            modifiedRt.add(s.orders.get(i));
        }
        for (int i = top.positionOfSecond + 1; i < s.orders.size(); i++)
        {
            modifiedRt.add(s.orders.get(i));
        }
        
        s.orders = modifiedRt;
        
        
        double newSolutionCost = 0;
        for (int i = 0 ; i < s.orders.size() - 1; i++)
        {
            Order A = s.orders.get(i);
            Order B = s.orders.get(i + 1);
            newSolutionCost = newSolutionCost + distanceMatrix[A.ID][B.ID];
        }
        if (s.cost + top.moveCost != newSolutionCost)
        {
            System.out.println("Something Went wrong with the cost calculations !!!!");
        }

        s.cost += top.moveCost;
        s.cost += top.moveCost;
        
        
        
    }

    private static void applySwapMove(SwapMove sm, Solution s) 
    {
        Order swapped1 = s.orders.get(sm.positionOfFirst);
        Order swapped2 = s.orders.get(sm.positionOfSecond);
        
        //Simple Way
        //set the element in the sm.positionOfFirst of the route to be swapped2 and 
        //set the element in the sm.positionOfSecond of the route to be swapped1  
        s.orders.set(sm.positionOfFirst, swapped2);
        s.orders.set(sm.positionOfSecond, swapped1);
           
        // just for debugging purposes
        // to test if everything is OK
        double newSolutionCost = 0;
        for (int i = 0 ; i < s.orders.size() - 1; i++)
        {
            Order A = s.orders.get(i);
            Order B = s.orders.get(i + 1);
            newSolutionCost = newSolutionCost + distanceMatrix[A.ID][B.ID];
        }
        
        if (s.cost + sm.moveCost != newSolutionCost)
        {
            System.out.println("Something Went wrong with the cost calculations !!!!");
        }
        
        //update the cost of the solution and the corresponding cost of the route object in the solution
        s.cost = s.cost + sm.moveCost;
        s.cost = s.cost + sm.moveCost;
    }

    private static void applyRelocationMove(RelocationMove rm, Solution s) 
    {
        Order relocatedOrder = s.orders.get(rm.positionOfRelocated);
        
        //Take out the relocated Order
        s.orders.remove(rm.positionOfRelocated);
        
        //Reinsert the relocated Order into the appropriarte position
        //Where??? -> after the Order that WAS (!!!!) located in the rm.positionToBeInserted of the route
        
        //Watch out!!! 
        //If the relocated customer is reinserted backwards we have to re-insert it in (rm.positionToBeInserted + 1)
        if (rm.positionToBeInserted < rm.positionOfRelocated)
        {
            s.orders.add(rm.positionToBeInserted + 1, relocatedOrder);
        }
        ////else (if it is reinserted forward) we have to re-insert it in (rm.positionToBeInserted)
        else
        {
            s.orders.add(rm.positionToBeInserted, relocatedOrder);
        }
        
        //just for debugging purposes
        // to test if everything is OK
        double newSolutionCost = 0;
        for (int i = 0 ; i < s.orders.size() - 1; i++)
        {
            Order A = s.orders.get(i);
            Order B = s.orders.get(i + 1);
            newSolutionCost = newSolutionCost + distanceMatrix[A.ID][B.ID];
        }
        
        if (s.cost + rm.moveCost != newSolutionCost)
        {
            System.out.println("Something Went wrong with the cost calculations !!!!");
        }
        
        
        s.cost = s.cost + rm.moveCost;
        s.cost = s.cost + rm.moveCost;
    }

	

} 