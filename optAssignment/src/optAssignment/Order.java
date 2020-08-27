
package optAssignment;

public class Order implements Comparable <Order>{

	boolean dark; 
	double quantity;
	int ID; 
	  
    public Order(int idd, double quant, boolean drk) 
    { 
        ID = idd;
        quantity = quant; 
        dark = drk; 
    }

	@Override
	public String toString() {
		return "Order [dark=" + dark + ", quantity=" + quantity + ", ID=" + ID + "]";
	}

	@Override
	public int compareTo(Order o) {
		if(this.quantity < o.quantity) 
		{
			return 1;
		}
		else if(this.quantity > o.quantity)
		{
			return -1;
		}
		else
			return 0;
	} 
}

