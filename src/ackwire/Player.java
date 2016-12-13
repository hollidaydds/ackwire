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
	
	//  Each player has a name, 3000 cash to start a set of stocks and hand of tiles.
	public Player(String player) {
	    name = player;
	    cash = 3000;
	    holdings = stocks;
	    currentTiles = tiles;
	}
	
	//  Adds m money to players cash total.
	public int addCash(int m){
		return cash+=m;
	}
	
	//  Subtracts m money from players cash total.
	public int spendCash(int m){
		return cash-=m;
	}
	
	//  Adds n shares of hotel h to players holdings.
	public void addShares(int h, int n){
		holdings[h]+=n;
	}
	
	//  Subtracts n shares of hotel h from players holdings.
	public void removeShares(int h, int n){
		holdings[h]-=n;
	}
	
	//  Returns players current holdings.
	public int shareCount(int h){
		return holdings[h];
	}
	
	//  Adds a tile to players hand.  After placing a tile on the board that tile location
	//  in the players hand is set to -1.  This finds that location and replaces it with an 
	//  available tile from the tile bag.
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
	
	//  Returns players name.
	public String getName(){
		return name;
	}
	
	//  Gets tile from players hand at location i.
	public int getTile(int i){
		return currentTiles[i];
	}
	
	//  Returns the number of the tile at hand location t. 
	public int peekTile(int t){
		
		int holder = currentTiles[t];
		return holder;
	}
	
	//  Sets tile at location 0 in hand to t.
	public void setTile(int t){
		
		currentTiles[0]=t;
			
	}
	
	//  Sets tile at location t to -1 so it can be replaced with a new tile and returns the 
	//  tile location to be placed by the board class.
	public int placeTile(int t){
		
		int holder = currentTiles[t];
		currentTiles[t]=-1;
		return holder;
		
	}

	//  Print current player status.
	public void printPlayer(){
		System.out.println("Name: "+ name);
		System.out.println("Total: "+ cash);
		System.out.print("Tiles: ");
		for(int i=0; i< currentTiles.length; i++){
			System.out.print(" | "+ currentTiles[i]);
		}
		System.out.println();
		for(int i =0; i<7; i++){
			System.out.print("Hotel " + i +": " + holdings[i]+ "\t");
		}
		System.out.println("");
	}
	public static void main(String[] args) {
		//  Player class test casses.
		
		//int[] stocks = {0,0,0,0,0,0,0};
		Player player= new Player("Tom");
		
		player.spendCash(2700);
		player.addShares(0, 3);
		player.printPlayer();
	}

}
