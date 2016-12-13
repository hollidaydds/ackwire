package ackwire;

public class Board2 {
	//  The board only needs 108 tile locations the 109th is for merge try breakers
	//  Where hotel chains are of equal size.
	private int[] gameBoard = new int[109];
	private int hotel = 10;
	
    /* initializes board set all locations to 0 */
    public void initializeBoard()
	{
	    for (int i = 0; i < 109; i++)
	    {
	    	gameBoard[i]=0;
	    }
	}
    
    //  Sets tile number, 0 is unplaced 1 is placed and 2-9 are hotel chains. 
    public void setHotel(int h){
    	hotel=h;
    }
    
    //  Returns current hotel number(name)
    public int getHotel(){
    	return hotel;
    }
    
    //  Prints out current state of the board.
    public void printBoard(){
    	for (int i=0; i<108; i++){
    		if(i%12==0){System.out.println();}
    		System.out.print("[ " + gameBoard[i] + " ]");
    	}
    	System.out.println();
    }
    
    //  Prints out the available hotels.
    public boolean[] getAvailable(){
    	int j=0;
    	boolean[]available = new boolean[7];
    	for(int i=0; i< gameBoard.length; i++){
    		if(gameBoard[i]!=0){
    			j=gameBoard[i];
    			available[j]=false;
    		}
    	}
    	return available;
    }
    
    //  Places the hotel (int x) at the board location (int n)
    public void placeTile(int x, int n){gameBoard[x]=n;}
    
    //  Returns the tile at board location x.
    public int checkTile(int x){
    	if(x<0 || x>107){return 0;}
    	
    	return gameBoard[x];
    }
    
    //  Tries to place a tile at this location.  If its a legal move it places the tile.
    public boolean tryTile(int x){
    	if(checkTile(x)!=0){System.out.println("Cannot place tile:  Illegal move or tile already placed.");}
    	if((checkTile(x-1)==0||checkLeft(x)) && (checkTile(x+1)==0 || checkRight(x)) && checkTile(x-12)==0  && checkTile(x-12)==0){
    		placeTile(x,1);
    	};
    	if(getAdjacents(x)==0 && checkOnes(x, hotel)){
    		hotel++;
    		return true;
    	}
    	
    	//  If only one adjacent tile check the adjacent tiles and place the appropriate tile.
    	if(getAdjacents(x)==1){
    		if(checkTile(x-1)!=0 && checkTile(x-1)!=1 && !checkLeft(x)){ 
    			placeTile(x, checkTile(x-1));
    			checkOnes(x, checkTile(x-1));
    		}
        	if(checkTile(x+1)!=0 && checkTile(x+1)!=0 && !checkRight(x)){
        		placeTile(x, checkTile(x+1));
        		checkOnes(x, checkTile(x+1));
        	}
        	if(checkTile(x-12)!=0 && checkTile(x-12)!=1){
        		placeTile(x, checkTile(x-12));
        		checkOnes(x, checkTile(x-12));
        	}
        	if(checkTile(x+12)!=0 && checkTile(x+12)!=1){
        		placeTile(x, checkTile(x+12));
        		checkOnes(x, checkTile(x+12));
        	}
    	};
    	
    	//  If more than one adjacent but not a merger check adjacent tiles and place appropriate tile.
    	if(getAdjacents(x)>1 && checkMerger(x) == false){
        	if(checkTile(x-1)!=0) placeTile(x, checkTile(x-1));
        	if(checkTile(x+1)!=0) placeTile(x, checkTile(x+1));
        	if(checkTile(x-12)!=0) placeTile(x, checkTile(x-12));
        	if(checkTile(x+12)!=0) placeTile(x, checkTile(x+12));
    	}
    	
    	//  If placing tile creates a merger process it.  
    	if(getAdjacents(x)>1 && checkMerger(x)==true){
    		processMerger(x);
    	}
		return false;
    } 
    	
    //  Checks tiles adjacent to tile location x and returns how many are non 0 tiles. 
    public int getAdjacents(int x){
    	int i = 0;
    	if(checkTile(x-1)!=0 && checkTile(x-1)!=1 && !checkLeft(x)) i++;
    	if(checkTile(x+1)!=0 && checkTile(x+1)!=1 && !checkRight(x)) i++;
    	if(checkTile(x-12)!=0 && checkTile(x-12)!=1) i++;
    	if(checkTile(x+12)!=0 && checkTile(x+12)!=1) i++;
    	
    	return i;

    }
    
    //  Checks adjacent tiles for uniqueness.  For checking for mergers or placed tiles that need 
    //  to be integrated into a chain.
    public int[] getUniqueAdjacents(int x){
    	
    	int [] adjacents = {-1,-1,-1,-1};
    	int i =0;
    	int numUnique = 0;
    	int count = 0;
    	if(checkTile(x-1)!=0 && checkTile(x-1)!=1 && !checkLeft(x)){adjacents[i]=checkTile(x-1); i++;} ;
    	if(checkTile(x+1)!=0 && checkTile(x+1)!=1 && !checkRight(x)){adjacents[i]=checkTile(x+1); i++;};
    	if(checkTile(x-12)!=0 && checkTile(x-12)!=1) {adjacents[i]=checkTile(x-12); i++;};
    	if(checkTile(x+12)!=0 && checkTile(x+12)!=1) {adjacents[i]=checkTile(x+12); i++;};

		for(i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(i!=j && adjacents[i]==adjacents[j])
				{
					adjacents[j]=-1;
				}
			}
		}
		for(int u:adjacents)
		{
			if(u!=-1)numUnique++;
		}
		int[] unique = new int[numUnique];
		
		for(int u:adjacents)
		{
			if(u!=-1){
				unique[count]=u;
				count++;
				}
		}
		
    	return unique;
    }
    
    //  Returns the hotels involved in the merger and sorts them based on chain size.
    //  The largest chain remains on the board the others are absolved.
    public int[] getHotels(int x){
    	int max = 0;
    	int maxCount = 0;
    	int min = 60;
    	
    	
    	int[]tiles = new int[4];
    	tiles[0]=checkTile(x-1);
    	tiles[1]=checkTile(x+1);	
    	tiles[2]=checkTile(x-12);	
    	tiles[3]=checkTile(x+12);	
    	
    	int[]hotels = new int[2];
    	for(int i : tiles)
    	{
    		if(i!=0 && i!=1 && count(i)>maxCount){
    			max=i;
    			maxCount=count(i);
    		}
       	}
    	for(int i : tiles){
    		if(i!=0 && i!=1 && i!=max){
    			min=i;
    		}
    	}
    	
    	hotels[0]=max;
    	hotels[1]=min;
    	return hotels;
    }
    
    //  Checks board position x to see if placing a tile there will result in a merger.
    public boolean checkMerger(int x){
    	boolean flag = false;
    	int j = 0;
    	int [] adj = {0,0,0,0};
    	if(!checkLeft(x))adj[0]=checkTile(x-1);
    	if(!checkRight(x))adj[1]=checkTile(x+1);	
    	adj[2]=checkTile(x-12);	
    	adj[3]=checkTile(x+12);	

    	for(int i=0; i<4; i++){
    		if (adj[i]!=0 && adj[i]!=1) j = adj[i];
       	}
    	for(int i=0; i<4; i++){
    		if (adj[i]!=0 && adj[i]!=1 && adj[i]!=j){flag = true;}
       	}
    	return flag;
    }
    
    //  If placing a hotel adjacent to a 1 that 1 is converted to the same number as the hotel being placed.
    public boolean checkOnes(int x, int h){
    	boolean flag = false;
    	
    	if(checkTile(x-1)==1 && !checkLeft(x)){placeTile(x-1,h); placeTile(x,h); flag=true;}
    	if(checkTile(x+1)==1 && !checkRight(x)){placeTile(x+1,h); placeTile(x,h); flag=true;}
    	if(checkTile(x-12)==1){placeTile(x-12,h); placeTile(x,h); flag=true;}
    	if(checkTile(x+12)==1){placeTile(x+12,h); placeTile(x,h); flag=true;}
    	
    	return flag;
    }
    
    //  Checks if tile location x is on the left edge of the board.  If this is true than x-1 is not adjacent.
    public boolean checkLeft(int x){
    	boolean flag= false;
    	if(x%12==0)flag=true;
    	return flag;
    }
    
    //  Checks if tile location is on the right edge of the board.  If this is true than x+1 is not adjacent.
    public boolean checkRight(int x){
    	boolean flag= false;
    	if((x+1)%12==0)flag=true;
    	return flag;
    }
    
    //  Processes the merger.  Converts smaller hotel or hotels into the larger hotel and runs the pay-out method.
    public void processMerger(int x){
    	if(getAdjacents(x)>1 && checkMerger(x)==true){
    		int[]hotels = getHotels(x);
    		int h = hotels[0];
    		placeTile(x, merge(hotels[0], hotels[1]));
    		checkOnes(x, h);
    		processMerger(x);
    	}
    }

    //  Counts the number of tiles on the board of type n.
    public int count (int n){
    	int j = 0;
    	for(int i=0; i<109; i++){
    		if (gameBoard[i]==n)j++;
    	}
    	return j;
    }
    
    //  Replaces tiles of defunct hotel with those of the new one.  Returns the surviving hotels tile number.
    public int merge(int n, int m){
    	if(count(n)>count(m)){
    		replaceTiles(m,n);
    		return n;
    	}    	
    	replaceTiles(n,m);
    	return m;
    }
    
    //  Compares tile count of two types and returns the larger.
    public int compareSize(int n, int m){
    	if(count(n)>count(m)){
    		return n;
    	}    	
    	   	return m;
    }
    
    //  Replaces all tiles on the board of type o with type n.
    public void replaceTiles(int o, int n){
    	for(int i=0; i<108; i++){
    		if(gameBoard[i]==o){gameBoard[i]=n;}
    	}
    }
    
    //  Returns current board.
    public int[] getBoard(){
    	return gameBoard;
    }
    
    //  Sets current board.
    public void setBoard(int[] b){
    	this.gameBoard=b;
    }
    
	public static void main(String[] args) {
		
		//  Board function tests.
		Board2 board = new Board2();
		board.initializeBoard();
		board.tryTile(1);
		board.tryTile(13);
	
		board.tryTile(49);
		board.tryTile(37);

		board.tryTile(26);
		board.tryTile(0);
		//board.tryTile(24);
		
/*		board.tryTile(14);
		board.tryTile(2);
		board.tryTile(26);*/
		
		board.tryTile(27);
		//board.tryTile(12);
		
		board.tryTile(24);
		//board.tryTile(36);
		board.printBoard();
		System.out.println(board.getAdjacents(25));
		//board.tryTile(12);
		//board.tryTile(30);
		//board.tryTile(14);
		System.out.println(board.tryTile(25));
		board.printBoard();
		System.out.println(board.hotel);
		
	}
}
