package ackwire;

public class Player {
	
	private String name;
	private int cash;
	private int[] currentTiles= new int[7]; 
	private int[] holdings = new int[7]; 
	private int[] stocks = {0,0,0,0,0,0,0};
	private int[] tiles = {-1,-1,-1,-1,-1,-1,-1};
	
	public Player(String player, int money, int[]shares) {
	    name = player;
	    cash = money;
	    holdings = shares;
	    currentTiles = tiles;
	}
	
	public Player(String player) {
	    name = player;
	    cash = 3000;
	    holdings = stocks;
	    currentTiles = tiles;
	}
	
	public int addCash(int m){
		return cash+=m;
	}
	
	public int spendCash(int m){
		return cash-=m;
	}
	
	public void addShares(int h, int n){
		holdings[h]+=n;
	}
	
	public void removeShares(int h, int n){
		holdings[h]-=n;
	}
	
	public void drawTile(int t){
		int i = 0;
		for(int tile : currentTiles)
		{
			if(tile==-1){
				currentTiles[i]=t;
				break;
			}
			i++;
		}
	}
	
	public String getName(){
		return name;
	}
	public int getTile(int i){
		return currentTiles[i];
	}
	
	public int placeTile(int t){
		
		int holder = currentTiles[t];
		currentTiles[t]=-1;
		return holder;
		
	}
	public void printPlayer(){
		System.out.println("Name: "+ name);
		System.out.println("Total: "+ cash);
		
		for(int i=0; i< currentTiles.length; i++){
			System.out.println("Tile: "+ currentTiles[i]);
		}
		for(int i =0; i<7; i++){
			System.out.print("Hotel " + i +": " + holdings[i]+ "\t");
		}
		System.out.println("");
	}
	public static void main(String[] args) {
		//int[] stocks = {0,0,0,0,0,0,0};
		Player player= new Player("Tom");
		
		player.addCash(2700);
		player.addShares(0, 3);
		player.printPlayer();
	}

}