package ackwire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONObject;


public class OnlineGame {

	private Board2 board;
	private Stocks stocks;
	private Player[] players;
	private int[] tileBag = new int[108];
	private String firstMove;
	private boolean hotelOpen=false;
	Scanner scan = new Scanner(System.in);
	 ServerSocket localSocket;
	 Socket userConnection = null;
	 BufferedReader in;
	 PrintWriter out;
	 JSONObject message = new JSONObject();
	 String s;
	 
public void run(){
	try{
        // Set up the ServerSocket
        localSocket = new ServerSocket(8484);
        
        // Listen for a connection
        System.out.println("Waiting for client");
        userConnection = localSocket.accept();
        
        System.out.println("Connection received from " + userConnection.getInetAddress().getHostName());
        
        //  Setup ObjectInput and ObjectOutput streams
	    in = new BufferedReader(new InputStreamReader(userConnection.getInputStream()));
	    out = new PrintWriter(userConnection.getOutputStream(), true);
        //sendMessage("Connection successful");
  		initializeOnline();
  		playGame();
	}
        // Game communication occurs here after connection is established. 
        
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    
}

private void sendMessage(String string) {
	message.put("Server", string);
	out.write(message.toString());
	out.flush();
	System.out.println("server>" + message.toString());
	} 

private void sendText(String string) {
	out.println(string);
	out.flush();
	System.out.println("server>" + message.toString());
	}

//  Sets up the game.  Initializes the board, the players and the stocks.
public void initializeOnline() throws NumberFormatException, IOException{

	//  Ask how many players.  Replace with number of players in the queue.
	out.println("How many players? 3-6");
	out.flush();
	int n = Integer.parseInt(in.readLine());
	
	board = new Board2();
	board.initializeBoard();

	stocks = new Stocks();
	stocks.initializeStocks();

	players = new Player[n];
	
//  Ask for name for all the players.  Replace with getter from the queue.
for(int i=0; i<n; i++){
	out.println("Enter a name for PLayer " + i + ": ");
	out.flush();
	players[i]=new Player(in.readLine());
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
public void placeTile(Player p, int t) throws NumberFormatException, IOException
{
	//  If placing this tile creates a merger, process it.
	if(board.checkMerger(p.peekTile(t))){
		System.out.println("MERGER!");
		processMerger(p.peekTile(t));
	}
	//  If placing a tile creates a new hotel prompt user for which one they want to create.
	//  Replace with JSON message prompt to front end.
	if(board.tryTile(p.placeTile(t))==true){out.println(stocks.getAvailable());
	out.flush();
	hotelOpen=true;
	stocks.updateAvailable(updateAvailable());
	int hotel = Integer.parseInt(in.readLine());
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
public int buyStocks(Player p) throws NumberFormatException, IOException{
	int[] purchase = new int[3]; 
	int s;
	int total = 0;
	System.out.println("got here too!");
	out.println(stocks.getPlaced());
	out.flush();
	
		s=Integer.parseInt(in.readLine());
		if(s==-1)return 0;
		p.addShares(s, 1);
		p.addShares(s, 1);
		p.addShares(s, 1);
	    total+=(stocks.buyStock(s, getTier(s+2), 1)*3);
	
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
public void processMerger(int x) throws NumberFormatException, IOException{

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
		out.println("Which hotel would you like to keep?");
		out.flush();
		for(int h : hotels){
			out.print(h +", ");
			out.flush();
		}
		
		int pick = Integer.parseInt(in.readLine());
		board.placeTile(108, pick);
		
		for(int h: hotels){
			if(h!=pick)
				mergePayout(h);
		}
		}
	else{
		out.println("Payout for discarded hotels");
		out.flush();
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
public int[] mergerStockOptions(Player p, int h) throws NumberFormatException, IOException{
	int[] result = {0,0,0};
	if(p.shareCount(h)==0){
		out.println("You have no shares of this hotel");
		out.flush();
		return result;
	}
	else {
		System.out.println("trade?");
		result[0]=Integer.parseInt(in.readLine());
		System.out.println("sell");
		result[1]=Integer.parseInt(in.readLine());
		System.out.println("keep");
		result[2]=Integer.parseInt(in.readLine());
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
public void playGame() throws NumberFormatException, IOException{
	while(true){
		for(Player p: players){
			out.println(p.getName()+": Place a tile please (0-6)");
			out.flush();
			placeTile(p, Integer.parseInt(in.readLine()));
			p.drawTile(drawTile());
			System.out.println("here!");
			if(hotelOpen)p.spendCash(buyStocks(p));
			p.printPlayer();
		}
		board.printBoard();
	}
}
public static void main(String args[]){
    OnlineGame ackwire = new OnlineGame();
   
        ackwire.run();
//	String s = "{\"hi\":\"there\"}";
//	JSONObject test = new JSONObject(s);
//	test.put("hi", "there");
//	System.out.println(test);
   
}
}