package ackwire;

public class mergerCheck {

	static int[] tiles = {2,2,4,4};
	
	
	public static int unique(int[] tiles){
		int num=tiles.length;
		
		for(int i=0; i<tiles.length; i++){
			for(int j=0; j<tiles.length; j++){
				if(i!=j)
				{
					if(tiles[i]==tiles[j])tiles[j]=-1;
				}
			}
		}
		for(int i : tiles){
			if (i==-1) num--;
		}
		return num;
	}
	
    public static int[] getUniqueAdjacents(int [] a){
    	
    	int [] adjacents = {1, 5, 3, 2};
    	int i =0;
    	int numUnique = 0;
    	int count = 0;

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
    
	
	public static void main(String[] args) {

		
		int[]b=getUniqueAdjacents(tiles);
		for(int i : b){
		System.out.println(i);
		}
	}

}
