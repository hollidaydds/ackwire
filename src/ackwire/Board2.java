package ackwire;


public class Board2 {
	private int[] gameBoard = new int[108];
	private int hotel = 2;
	
    /* initializes board set all locations to 0 */
    public void initializeBoard()
	{
	    for (int i = 0; i < 108; i++)
	    {
	    	gameBoard[i]=0;
	    }
	}
    
    public void setHotel(int h){
    	hotel=h;
    }
    
    public int getHotel(){
    	return hotel;
    }
    public void printBoard(){
    	for (int i=0; i<108; i++){
    		if(i%12==0){System.out.println();}
    		System.out.print("[ " + gameBoard[i] + " ]");
    	}
    	System.out.println();
    }
    
    public void placeTile(int x, int n){gameBoard[x]=n;}
    
    public int checkTile(int x){
    	if(x<0 || x>107){return 0;}
    	
    	return gameBoard[x];
    }
    
    public boolean tryTile(int x){
    	if(checkTile(x)!=0){System.out.println("Cannot place tile:  Illegal move or tile already placed.");}
    	if((checkTile(x-1)==0||checkLeft(x)) && (checkTile(x+1)==0 || checkRight(x)) && checkTile(x-12)==0  && checkTile(x-12)==0){
    		placeTile(x,1);
    	};
    	if(checkOnes(x)){
    		hotel++;
    		return true;
    	}
    	
    	if(getAdjacents(x)==1){
    		if(checkTile(x-1)!=0 && !checkLeft(x)) placeTile(x, checkTile(x-1));
        	if(checkTile(x+1)!=0 && !checkRight(x)) placeTile(x, checkTile(x+1));
        	if(checkTile(x-12)!=0) placeTile(x, checkTile(x-12));
        	if(checkTile(x+12)!=0) placeTile(x, checkTile(x+12));
    		    		
    	};
    	
    	if(getAdjacents(x)>1 && checkMerger(x) == false){
        	if(checkTile(x-1)!=0) placeTile(x, checkTile(x-1));
        	if(checkTile(x+1)!=0) placeTile(x, checkTile(x+1));
        	if(checkTile(x-12)!=0) placeTile(x, checkTile(x-12));
        	if(checkTile(x+12)!=0) placeTile(x, checkTile(x+12));
    	}
    	if(checkMerger(x)==true){
    		int[]hotels = getHotels(x);
    		placeTile(x, merge(hotels[0], hotels[1]));
    		}
		return false;
    	
    } 
    	
    public int getAdjacents(int x){
    	int i = 0;
    	if(checkTile(x-1)!=0 && !checkLeft(x)) i++;
    	if(checkTile(x+1)!=0 && !checkRight(x)) i++;
    	if(checkTile(x-12)!=0) i++;
    	if(checkTile(x+12)!=0) i++;
    	
    	return i;

    }
    public int[] getHotels(int x){
    	int j = 0;
    	int k = 0;
    	int[]tiles = new int[4];
    	tiles[0]=checkTile(x-1);
    	tiles[1]=checkTile(x+1);	
    	tiles[2]=checkTile(x-12);	
    	tiles[3]=checkTile(x+12);	
    	
    	int[]hotels = new int[2];

    	for(int i=0; i<4; i++){
    		if (tiles[i]!=0) j = tiles[i];
       	}
    	for(int i=0; i<4; i++){
    		if (tiles[i]!=0 && tiles[i]!=j) k = tiles[i];
       	}
    	hotels[0]=j;
    	hotels[1]=k;
    	return hotels;
    }
    public boolean checkMerger(int x){
    	boolean flag = false;
    	int j = 0;
    	int [] adj = {0,0,0,0};
    	if(!checkLeft(x))adj[0]=checkTile(x-1);
    	if(!checkRight(x))adj[1]=checkTile(x+1);	
    	adj[2]=checkTile(x-12);	
    	adj[3]=checkTile(x+12);	

    	for(int i=0; i<4; i++){
    		if (adj[i]!=0) j = adj[i];
       	}
    	for(int i=0; i<4; i++){
    		if (adj[i]!=0 && adj[i]!=j){flag = true;}
       	}
    	return flag;
    }
    
    public boolean checkOnes(int x){
    	boolean flag = false;
    	
    	if(checkTile(x-1)==1 && !checkLeft(x)){placeTile(x-1,hotel); placeTile(x,hotel); flag=true;}
    	if(checkTile(x+1)==1 && !checkRight(x)){placeTile(x+1,hotel); placeTile(x,hotel); flag=true;}
    	if(checkTile(x-12)==1){placeTile(x-12,hotel); placeTile(x,hotel); flag=true;}
    	if(checkTile(x+12)==1){placeTile(x+12,hotel); placeTile(x,hotel); flag=true;}
    	
    	return flag;
    }
    
    public boolean checkLeft(int x){
    	boolean flag= false;
    	if(x%12==0)flag=true;
    	return flag;
    }
    
    public boolean checkRight(int x){
    	boolean flag= false;
    	if((x+1)%12==0)flag=true;
    	return flag;
    }
    public int count (int n){
    	int j = 0;
    	for(int i=0; i<108; i++){
    		if (gameBoard[i]==n)j++;
    	}
    	return j;
    }
    
    public int merge(int n, int m){
    	if(count(n)>count(m)){
    		replaceTiles(m,n);
    		return n;
    	}    	
    	replaceTiles(n,m);
    	return m;
    }
    
    public void replaceTiles(int o, int n){
    	for(int i=0; i<108; i++){
    		if(gameBoard[i]==o){gameBoard[i]=n;}
    	}
    }
    
    public void checkAvailable(int o, int n){
    	for(int i=0; i<108; i++){
    		if(gameBoard[i]==o){gameBoard[i]=n;}
    	}
    }
    
	public static void main(String[] args) {
		
		Board2 board = new Board2();
		board.initializeBoard();
		board.tryTile(3);
		board.tryTile(23);
	
		board.tryTile(25);
/*		board.tryTile(17);

		board.tryTile(12);
		board.tryTile(0);
		board.tryTile(24);
		
		board.tryTile(14);
		board.tryTile(2);
		board.tryTile(26);
		
		board.tryTile(13);*/
		System.out.println(board.tryTile(25));
		board.replaceTiles(0, 5);
		board.printBoard();
		System.out.println(board.hotel);
		
	}
}
