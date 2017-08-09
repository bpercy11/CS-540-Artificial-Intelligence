
/*
 * Helper function for bpercyPlayer 
 * 
 * 
 */

public class moveSBEPair {
	
		private int binNum;
		private int num;
	
	//constructor
	public moveSBEPair(int val, int bin1){
		num = val;
		binNum = bin1;
	}
	
	///
	public int getbin(){
		return binNum;
	}
	
	public void setbin(int bin1){
		binNum = bin1;
	}
	

	public void setval(int val){
		num = val;
	}
	
	public int getval(){
		return num;
	}
}
