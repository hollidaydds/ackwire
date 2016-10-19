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
	
    public void initializeStocks()
	{
	    for (int i = 0; i < 7; i++)
	    {
	    	available[i]=true;
	    	stocks[i]=25;
	    }
	}
    
    // h = hotel t = tier n = number
    public int buyStock(int h, int t, int n){
    	stocks[h]=stocks[h]-n;
    	int basePrice=200;
    	if(h>1 && h<5) basePrice+=100;
    	if(h>4) basePrice+=200;
    	
    	return (basePrice+(100*t))*n;
    }
    
    // h = hotel t = tier n = number
    public int sellStock(int h, int t, int n){
    	stocks[h]=stocks[h]+n;
    	int basePrice=200;
    	if(h>1 && h<5) basePrice+=100;
    	if(h>4) basePrice+=200;
    	
    	return (basePrice+(100*t))*n;
    }
    
    // h1 = defunct hotel h2 = remaining hotel n = number of defunct to trade
    public int tradeStock(int h1, int h2, int n){
    	stocks[h1]=stocks[h1]+n;
    	stocks[h2]=stocks[h2]-n/2;
    	
    	return (n/2);
    }
    
    public void openHotel (int h){
    	available[h]=true;
    }
    
    public void closeHotel(int h ){
    	available[h]=false;
    }
    public void printAvailable(){
    	for(int i=0; i<available.length; i++){
    		if(available[i]){System.out.println(i + 2 + ", ");}
    	}
    }
    public void printPlaced(){
    	for(int i=0; i<available.length; i++){
    		if(!available[i]){System.out.println(i + ", ");}
    	}
    }
    public void updateAvailable(boolean[] a){
    	available = a;
    }
    
    public void printStocks()
    {
    	for(int i=0; i<7; i++)
    	{
    		System.out.println(stocks[i]);
    	}
    }
    
	public static void main(String[] args) {
		
		Stocks stock= new Stocks();
		stock.initializeStocks();
		System.out.println(stock.buyStock(2, 0, 1));
		System.out.println(stock.buyStock(2, 0, 1));
		System.out.println(stock.buyStock(2, 0, 1));

		System.out.println(stock.sellStock(2, 0, 1));
		System.out.println(stock.tradeStock(0, 1, 10));
		stock.printStocks();
		
	}

}
