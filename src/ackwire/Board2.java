package ackwire;

public class Board2 {
	private int[] gameBoard = new int[108];
	private int hotel = 10;
	
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
    	if(getAdjacents(x)==0 && checkOnes(x, hotel)){
    		hotel++;
    		return true;
    	}
    	
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
    	
    	if(getAdjacents(x)>1 && checkMerger(x) == false){
        	if(checkTile(x-1)!=0) placeTile(x, checkTile(x-1));
        	if(checkTile(x+1)!=0) placeTile(x, checkTile(x+1));
        	if(checkTile(x-12)!=0) placeTile(x, checkTile(x-12));
        	if(checkTile(x+12)!=0) placeTile(x, checkTile(x+12));
    	}
    	if(getAdjacents(x)>1 && checkMerger(x)==true){
    		processMerger(x);
//    		int[]hotels = getHotels(x);
//    		int h = compareSize(hotels[0], hotels[1]);
//    		placeTile(x, merge(hotels[0], hotels[1]));
//    		checkOnes(x, h);
//    		if(getAdjacents(x)>1 && checkMerger(x)==true){
//    			hotels = getHotels(x);
//    			placeTile(x, merge(hotels[0], hotels[1]));
//    			checkOnes(x, h);
//    		}
    	}
		return false;
    	
    } 
    	
    public int getAdjacents(int x){
    	int i = 0;
    	if(checkTile(x-1)!=0 && checkTile(x-1)!=1 && !checkLeft(x)) i++;
    	if(checkTile(x+1)!=0 && checkTile(x+1)!=1 && !checkRight(x)) i++;
    	if(checkTile(x-12)!=0 && checkTile(x-12)!=1) i++;
    	if(checkTile(x+12)!=0 && checkTile(x+12)!=1) i++;
    	
    	return i;

    }
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
    
    public boolean checkOnes(int x, int h){
    	boolean flag = false;
    	
    	if(checkTile(x-1)==1 && !checkLeft(x)){placeTile(x-1,h); placeTile(x,h); flag=true;}
    	if(checkTile(x+1)==1 && !checkRight(x)){placeTile(x+1,h); placeTile(x,h); flag=true;}
    	if(checkTile(x-12)==1){placeTile(x-12,h); placeTile(x,h); flag=true;}
    	if(checkTile(x+12)==1){placeTile(x+12,h); placeTile(x,h); flag=true;}
    	
    	return flag;
    }
    
    public boolean checkLeft(int x){
    	boolean flag= false;
    	if(x%12==0)flag=true;
    	return flag;
    }
    public void processMerger(int x){
    	if(getAdjacents(x)>1 && checkMerger(x)==true){
    		int[]hotels = getHotels(x);
    		int h = hotels[0];
    		placeTile(x, merge(hotels[0], hotels[1]));
    		checkOnes(x, h);
    		processMerger(x);
    	}
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
    
    public int compareSize(int n, int m){
    	if(count(n)>count(m)){
    		return n;
    	}    	
    	   	return m;
    }
    
    public void replaceTiles(int o, int n){
    	for(int i=0; i<108; i++){
    		if(gameBoard[i]==o){gameBoard[i]=n;}
    	}
    }
    
    public int[] getBoard(){
    	return gameBoard;
    }
    
	public static void main(String[] args) {
		
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
