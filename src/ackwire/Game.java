package ackwire;

import java.util.Random;
import java.util.Scanner;

public class Game {
	
	private Board2 board;
	private Stocks stocks;
	private Player[] players;
	private int[] tileBag = new int[108];
	private String firstMove;
	private boolean hotelOpen=false;
	Scanner scan = new Scanner(System.in);
	
	//  Sets up the game.  Initializes the board, the players and the stocks.
	public void initializeGame(){
	
		//  Ask how many players.  Replace with number of players in the queue.
		System.out.println("How many players? 3-6");
		int n = scan.nextInt();
		scan.nextLine();
		
		board = new Board2();
		board.initializeBoard();
	
		stocks = new Stocks();
		stocks.initializeStocks();
	
		players = new Player[n];
		
	//  Ask for name for all the players.  Replace with getter from the queue.
	for(int i=0; i<n; i++){
		System.out.println("Enter a name for PLayer " + i + ": ");
		
		players[i]=new Player(scan.nextLine());
		}
	
	//  Creates all the tiles for the board.
	for(int i=0; i<108; i++){
		tileBag[i]=i;
	}
	//  Deal out single tile to each of the players.  
    for (Player p  : players) {
        p.drawTile(drawTile());
     }
    
    //  Looks for lowest tile to decide who plays first and places the initial tiles on the board.
    for (Player p  : players) {
    	int holder = 107;
    	if(p.getTile(0)/12 + p.getTile(0)%12 <holder)
    	{
    		holder = p.getTile(0);
    		firstMove = p.getName();
    	}
        board.placeTile(p.placeTile(0),1);
     }
    
    //  Deals full hands out to each player.
    for (Player p  : players) {
        for(int i=0; i<7; i++){
    	p.drawTile(drawTile());
        }
    }	
}

	//  Draws a random tile from the tile bag, no duplicate tiles should exist.
	public int drawTile(){
		Random r = new Random();
		int drawn = r.nextInt(107);
		int given;
		if(tileBag[drawn]!=-1){
			given = tileBag[drawn];
			 tileBag[drawn]=-1;
			return given;
		}
		else{
			while(tileBag[drawn]==-1){
				if(drawn==107){drawn=0;}
				drawn++;
			}
			given = tileBag[drawn];	
			tileBag[drawn]=-1;
			return given;
		}
	}
	//  Prints out player names.
	public void printPlayers(){
	      for (Player p: players) {
	          p.printPlayer();
	       }
	} 
	//  Gets name of player who drew the lowest tile and goes first.
	public String getFirst(){
		return firstMove;
	}

	//  Places a tile at position t(0-6) in from player p's hand.
	public void placeTile(Player p, int t)
	{
		//  If placing this tile creates a merger, process it.
		if(board.checkMerger(p.peekTile(t))){
			System.out.println("MERGER!");
			processMerger(p.peekTile(t));
		}
		//  If placing a tile creates a new hotel prompt user for which one they want to create.
		//  Replace with JSON message prompt to front end.
		if(board.tryTile(p.placeTile(t))==true){System.out.println("New Hotel created!  Choose from available hotels");
		hotelOpen=true;
		stocks.updateAvailable(updateAvailable());
		stocks.printAvailable();
		int hotel = scan.nextInt();
		board.replaceTiles(board.getHotel()-1, hotel);
		p.addShares(hotel-2, 1);
		stocks.closeHotel(hotel-2);
		stocks.buyStock(hotel-2, 1, 1);
		p.printPlayer();
		stocks.printStocks();
		};
	}
	//  After tile is placed move on to stock purchase phase.  Player may buy up to 3 of the available stocks.
	//  As of now it allows for input of stocks that have not yet been created.
	public int buyStocks(Player p){
		int[] purchase = new int[3]; 
		int s;
		int total = 0;
		System.out.println("Please select your stocks. or enter -1 to skip");
		stocks.printPlaced();
		for(int i = 0; i<purchase.length; i++){
			s=scan.nextInt();
			if(s==-1)break;
			p.addShares(s, 1);
		    total+=stocks.buyStock(s, getTier(s+2), 1);
		}
		return total;
	}
	//  Returns the level (number of tiles in the chain) of the hotel chain for pay-out purposes.
	public int getTier(int h){
		int c = board.count(h)-2;
		if(c<7){return c;}
		if(c>6 && c<11){return 6;}
		if(c>10 && c<21){return 7;}
		if(c>20 && c<31){return 8;}
		if(c>30 && c<41){return 9;}
		if(c>40){return 10;}
		return 0;
	}
	//TODO Finish this after buy stocks complete.
	//  Creates a holder duplicate holder board for processing the merger.
	public void processMerger(int x){

		Board2 boardCopy = new Board2();
    	boardCopy.setBoard(board.getBoard());
    	int winner=-1;

    	if(boardCopy.getUniqueAdjacents(x).length>1 && boardCopy.checkMerger(x)==true){
			Boolean equal = true;
			System.out.println("2");
			int[]hotels = boardCopy.getUniqueAdjacents(x);
    		
		for(int i=0; i< hotels.length; i++){
    		for(int j = 0; j<hotels.length; j++){
    			if(i!=j && boardCopy.count(hotels[i])!=boardCopy.count(hotels[j])){equal=false;}
    			if(i!=j && boardCopy.count(hotels[i]) > boardCopy.count(hotels[j])){winner=i;}
    			}
    		}
		//  If the two hotel chains are equal prompt user for which chain the would like to 
		//  remain on the board.
    	if(equal){
    		System.out.println("Which hotel would you like to keep?");
    		for(int h : hotels){
    			System.out.print(h +", ");
    		}
    		
    		int pick = scan.nextInt();
    		board.placeTile(108, pick);
    		
    		for(int h: hotels){
    			if(h!=pick)
    				mergePayout(h);
    		}
    		}
    	else{
    		System.out.println("Payout for discarded hotels");
    		for(int h:hotels){
    			if(h!=winner){
    				mergePayout(h);
    			}
    		}
    	}
    	}
		
	}
	//  Pays out first majority share holder and second majority share holder of dissolved stock.
	public void mergePayout(int h){
		int firstMaj=0;
		int secMaj=0;
		int payout = 0;
		int[]shares = new int[players.length];
		for(int i = 0; i<shares.length; i++){
			shares[i] = players[i].shareCount(h-2);
		}
		for(int i =0; i<shares.length; i++){
			if(shares[i]>firstMaj){
				firstMaj = i;
			}
			else if(shares[i]==firstMaj || shares[i]>secMaj){
				secMaj = i;
			}
		}
		
		if(firstMaj == secMaj){
			payout = stocks.payoutStock(h, getTier(h));
			players[firstMaj].addCash(payout/2);
			players[secMaj].addCash(payout/2);
		}
		
		if(shares[secMaj]==0){
			payout = stocks.payoutStock(h, getTier(h));
			players[firstMaj].addCash(payout + (payout/2));
		}
		
		else 	players[firstMaj].addCash(payout);
		 		players[secMaj].addCash(payout/2);
	}
	
	//  Prompt user after merger occurs how many shares they would like to trade, sell or keep.
	public int[] mergerStockOptions(Player p, int h){
		int[] result = {0,0,0};
		if(p.shareCount(h)==0){
			System.out.println("You have no shares of this hotel");
			return result;
		}
		else {
			System.out.println("trade?");
			result[0]=scan.nextInt();
			System.out.println("sell");
			result[1]=scan.nextInt();
			System.out.println("keep");
			result[2]=scan.nextInt();
			return result;
		}
		
	}
	
	//  Keeps track of which hotels are available to be placed.
	public boolean[] updateAvailable(){
		boolean[] available = new boolean[7];
		for(int i=0; i<available.length; i++){
			available[i]=true;
			}
		for(int i: board.getBoard()){
			if(i>1 && i<9){
				available[i-2]=false;
			}
		}
		return available;
	}
	
	//  Plays the game.  Need to add win and exit conditions.
	public void playGame(){
		while(true){
			for(Player p: players){
				System.out.println(p.getName()+": Place a tile please");
				placeTile(p, scan.nextInt());
				p.drawTile(drawTile());
				if(hotelOpen)p.spendCash(buyStocks(p));
				p.printPlayer();
			}
			board.printBoard();
		}
	}
public static void main(String[] args) {

	
	Game game = new Game();
		
	game.initializeGame();
	game.printPlayers();
	game.board.printBoard();
	System.out.println(game.getFirst());
	game.playGame();
//	game.board.tryTile(2);
//	game.board.tryTile(3);
//	game.board.tryTile(12);
//	game.board.tryTile(13);
//
//	game.players[0].addShares(2, 5);
//	game.players[1].addShares(2, 7);
//	game.printPlayers();
//	game.mergePayout(2);
//	game.printPlayers();
	//game.board.placeTile(14, 3);
//	game.board.placeTile(26, 4);
//	game.board.placeTile(38, 4);
//	game.board.printBoard();
//	System.out.println(game.board.count(2));
//	System.out.println(game.board.count(3));
//	game.processMerger(14);
//	game.board.printBoard();
//	
//	game.players[0].setTile(14);
//	game.placeTile(game.players[0], 0);
//	game.board.printBoard();
//	
//	System.out.println(game.board.count(2));
//	System.out.println(game.board.count(3));
//	System.out.println(game.board.count(4));
	}
}
