package ackwire;

import java.util.Random;
import java.util.Scanner;

public class Game {
	
	private Board2 board;
	private Stocks stocks;
	private Player[] players;
	private int[] tileBag = new int[108];
	private String firstMove;
	Scanner scan = new Scanner(System.in);

	public void initializeGame(){
	
		
		System.out.println("How many players? 3-6");
		int n = scan.nextInt();
		scan.nextLine();
		
		board = new Board2();
		board.initializeBoard();
	
		stocks = new Stocks();
		stocks.initializeStocks();
	
		players = new Player[n];
		
	for(int i=0; i<n; i++){
		System.out.println("Enter a name for PLayer " + i + ": ");
		
		players[i]=new Player(scan.nextLine());
		}
	
	
	for(int i=0; i<108; i++){
		tileBag[i]=i;
	}
    for (Player p  : players) {
        p.drawTile(drawTile());
     }
    for (Player p  : players) {
    	int holder = 107;
    	if(p.getTile(0)/12 + p.getTile(0)%12 <holder)
    	{
    		holder = p.getTile(0);
    		firstMove = p.getName();
    	}
        board.placeTile(p.placeTile(0),1);
     }
    
    for (Player p  : players) {
        for(int i=0; i<7; i++){
    	p.drawTile(drawTile());
        }
    }	
}

	
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
	
	public void printPlayers(){
	      for (Player p: players) {
	          p.printPlayer();
	       }
	} 
	public String getFirst(){
		return firstMove;
	}
	public boolean[] updateAvailable(){
		boolean[] available = board.getAvailable();

		return available;
	}
	public void placeTile(Player p, int t)
	{
		if(board.checkMerger(p.peekTile(t))){System.out.println("MERGER!");}
		if(board.tryTile(p.placeTile(t))==true){System.out.println("New Hotel created!  Choose from available hotels");
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

	public void playGame(){
		while(true){
			for(Player p: players){
				System.out.println(p.getName()+": Place a tile please");
				placeTile(p, scan.nextInt());
				p.drawTile(drawTile());
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
/*	game.board.placeTile(2, 2);
	game.board.placeTile(3, 2);
	game.board.placeTile(12, 3);
	game.board.placeTile(13, 3);
	game.board.tryTile(14);*/
	game.board.printBoard();
	}
}
