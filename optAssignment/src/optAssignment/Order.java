
 
package optAssignment;

/**
 * @author alexa
 *
 */
public class Order {

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
}

