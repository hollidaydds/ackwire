package ackwire;

public class Stocks {

	// 0 - Tower
	// 1 - Luxor
	// 2 - American
	// 3 - Festival 
	// 4 - Worldwide
	// 5 - Continental
	// 6 - Imperial
	
	private int [] stocks = new int[7];
	private boolean [] available = new boolean[7];
	
    //  Creates 25 shares of each of the 6 hotels.
	public void initializeStocks()
	{
	    for (int i = 0; i < 7; i++)
	    {
	    	available[i]=true;
	    	stocks[i]=25;
	    }
	}
    
    // h = hotel t = tier n = number
	//  Purchase n stocks of h type, tier is for pricing.
    public int buyStock(int h, int t, int n){
    	stocks[h]=stocks[h]-n;
    	int basePrice=200;
    	if(h>1 && h<5) basePrice+=100;
    	if(h>4) basePrice+=200;
    	
    	return (basePrice+(100*t))*n;
    }
    
    // h = hotel t = tier n = number
    // Sell  n stocks of hotel type h at tier t price.
    public int sellStock(int h, int t, int n){
    	stocks[h]=stocks[h]+n;
    	int basePrice=200;
    	if(h>1 && h<5) basePrice+=100;
    	if(h>4) basePrice+=200;
    	
    	return (basePrice+(100*t))*n;
    }
    
    //  Pay-out structure based on hotel tier.  Returns amount of majority stake-holder.  Minority
    //  half of that ammount.
    public int payoutStock(int h, int t){
    	int basePrice=2000;
    	if(h>1 && h<5) basePrice+=1000;
    	if(h>4) basePrice+=2000;
    	
    	return basePrice+(t*1000); 
    }
    
    // h1 = defunct hotel h2 = remaining hotel n = number of defunct to trade
    public int tradeStock(int h1, int h2, int n){
    	stocks[h1]=stocks[h1]+n;
    	stocks[h2]=stocks[h2]-n/2;
    	
    	return (n/2);
    }
    
    //  Sets hotel as open and available to be placed when new hotel is created.
    public void openHotel (int h){
    	available[h]=true;
    }
    
    //  Sets hotel as closed so it is not available to be placed when new hotel is created.
    public void closeHotel(int h ){
    	available[h]=false;
    }
    
    //  Prints out the hotels that are available (open) to be placed.
    public String printAvailable(){
    	String toReturn = "";
    	for(int i=0; i<available.length; i++){
    		if(available[i]){System.out.print(i + 2 + " | "); toReturn.concat(i+2 + " | ");}
    	}
    	return toReturn;
    }
    
    //  Prints out the hotels that are not available (closed) to be placed.
    public void printPlaced(){
    	for(int i=0; i<available.length; i++){
    		if(!available[i]){System.out.println(i + ", ");}
    	}
    }
    
    //  Set available hotels.
    public void updateAvailable(boolean[] a){
    	available = a;
    }
    
    //  Prints available stocks of each hotel.  How many of the 25 initial are remaining.
    public void printStocks()
    {
    	for(int i=0; i<7; i++)
    	{
    		System.out.print(stocks[i] + "|");
    	}
    	System.out.println();
    }
    
	public static void main(String[] args) {
		
		//  Stock test cases.
		Stocks stock= new Stocks();
		stock.initializeStocks();
		System.out.println(stock.buyStock(2, 0, 1));
		System.out.println(stock.buyStock(2, 0, 1));
		System.out.println(stock.printAvailable());

		System.out.println(stock.sellStock(2, 0, 1));
		System.out.println(stock.tradeStock(0, 1, 10));
		stock.printStocks();
		
	}

}
