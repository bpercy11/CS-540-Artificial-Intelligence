/****************************************************************
 * bpercyPlayer.java
 * Implements MiniMax search with A-B pruning and iterative deepening search (IDS). The static board
 * evaluator (SBE) function is simple: the # of stones in studPlayer's
 * mancala minus the # in opponent's mancala.
 * -----------------------------------------------------------------------------------------------------------------
 * Licensing Information: You are free to use or extend these projects for educational purposes provided that
 * (1) you do not distribute or publish solutions, (2) you retain the notice, and (3) you provide clear attribution to UW-Madison
 *
 * Attribute Information: The Mancala Game was developed at UW-Madison.
 *
 * The initial project was developed by Chuck Dyer(dyer@cs.wisc.edu) and his TAs.
 *
 * Current Version with GUI was developed by Fengan Li(fengan@cs.wisc.edu).
 * Some GUI components are from Mancala Project in Google code.
 */




//################################################################
// bpercyPlayer class
//################################################################

public class bpercyPlayer extends Player {

    int min = Integer.MIN_VALUE;
   	int max = Integer.MAX_VALUE;
    /*Use IDS search to find the best move. The step starts from 1 and keeps incrementing by step 1 until
	 * interrupted by the time limit. The best move found in each step should be stored in the
	 * protected variable move of class Player.
     */
    public void move(GameState state)
    {
    	int counter = 1;
    	boolean bool = true;
    	while(bool == true)
    	{	
    		moveSBEPair newMove = maxAction(state, counter);
    		this.move = newMove.getbin();	
    		counter++;
    	}
    	
    }

    // Return best move for max player. Note that this is a wrapper function created for ease to use.
	// In this function, you may do one step of search. Thus you can decide the best move by comparing the 
	// sbe values returned by maxSBE. This function should call maxAction with 5 parameters.
    public moveSBEPair maxAction(GameState state, int maxDepth)
    {
    	
    	moveSBEPair best =  maxAction(state, 0, maxDepth, min, max);
    	return best;
    }
    
    
	//return sbe value related to the best move for max player
    public moveSBEPair maxAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta)
    {
      	int value = -999999;
    	moveSBEPair max = new moveSBEPair(-1000000, 1);
    	
    	//check if game is over or if search has reached maxDepth
    	if(currentDepth == maxDepth){
    		max.setval(sbe(state));
    		max.setbin(-1);
    		
    		return max;
    	}
    	if(state.gameOver()){
    		max.setval(sbe(state));
    		max.setbin(-1);
    		return max;
    	}
    	
    	//game is not over, continue searching

    	
    	//searches max players bins (mine)
    	for(int i = 0; i <= 5; i++){
    		//copy state of the board
    		GameState gameStateCopy = new GameState(state);
    		if(!(state.illegalMove(i))){
    			
    			//boolean to see if player gets another turn or not
    			//applyMove returns true if there is another move, false if not
    			boolean moveAgain = gameStateCopy.applyMove(i);
    			int vtemp = value; //place hold v value to compare later
    			
    			//checking whether to consider Min or Max based on if we get an extra move
    			//took equations from 2-6 lecture slide
    			if (moveAgain == true){
    				int maxv =  Math.max(value, maxAction(gameStateCopy, 
    						currentDepth+1, maxDepth, alpha, beta).getval()); 
    				value = maxv;
    			}
    			else {
    				
    				int minv = Math.max(value, minAction(gameStateCopy, 
    						currentDepth+1, maxDepth, alpha, beta).getval());
    				value = minv;
    			}
    			
    			// no valued move found better than -1000000
    			// use v instead
    			if(value > vtemp){
    				max.setbin(i);
    				
    				max.setval(value);
    			}
    			
    			//determine alpha cutoff based on max of predetermined alpha value and calculated v value
    			alpha = Math.max(alpha, value);
    			
    		if(value>beta){

    			return max;
    		}
    		}
    	}

    	return max;
    	
    }
    
    //return sbe value related to the best move for min player
    //same as maxAction but for min player
    public moveSBEPair minAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta)
    {
    	int value = 999999;
    	moveSBEPair min = new moveSBEPair(1000000, 1);
    	if(currentDepth == maxDepth){
    		min.setval(sbe(state));
    		min.setbin(-1);
    		
    		return min;
    	}
    	if(state.gameOver()){
    		min.setval(sbe(state));
    		min.setbin(-1);
    		
    		return min;
    		
    	}

    	
    	//searches min players bins (theirs)
    	for(int i = 7; i <= 12; i++){
    		//copies the current state of the game as directed
    		GameState gameStateCopy = new GameState(state); 
    		if(!(state.illegalMove(i))){
    			
    			int vtemp = value; //placehold v value to compare later

    			//boolean to see if player gets another turn or not
    			//applyMove returns true if there is another move, false if not
    			boolean moveAgain = gameStateCopy.applyMove(i); 
    			if(moveAgain == true){
    				
    				int minv = Math.min(value, minAction(gameStateCopy, 
    						currentDepth+1, maxDepth, alpha, beta).getval());
    				value = minv;
    			}
    			else {
    				
    				int maxv = Math.min(value, maxAction(gameStateCopy, 
    						currentDepth+1, maxDepth, alpha, beta).getval());
    				value = maxv;
    			}

    			if(vtemp > value){
    				min.setval(value);
    				min.setbin(i);
    			}

    			//determines beta cutoff based on predetermined beta value or calculated v value
    			beta = Math.min(beta, value);
    			if(value < alpha){
    				return min;
    			}
    		}
    	}

    	return min;
    	
    }
    
    
    
    public void extraMove(boolean moveAgain){
    	if(moveAgain == false){
    		//v = Math.min(v, maxAction(gameStateCopy, currentDepth + 1, maxDepth, alpha, beta).getValue());
    	}
    	else {
    		//v = Math.min(v, minAction(gameStateCopy, currentDepth + 1, maxDepth, alpha, beta).getValue());
    	}
    	
    }

    // the sbe function for game state. Note that in the game state, the bins for current player are always in the bottom row.
    // found SBE function from https://blog.hackerrank.com/mancala/
    // seems to be a consensus that this is one of the most effected SBE functions accompanied with minimax
    // ((myMancalaStones - yourMancalaStones) + (myBinsSummed - yourBinsSummed))
    private int sbe(GameState state)
    {
    	int myScore = state.stoneCount(6);
    	int theirScore = state.stoneCount(13);
    	
    	int myBins = state.stoneCount(5) + state.stoneCount(4) + 
    			state.stoneCount(3) + state.stoneCount(2) + state.stoneCount(1)
    			+ state.stoneCount(0);
    	int theirBins = state.stoneCount(12) + state.stoneCount(11)
    			+ state.stoneCount(10) + state.stoneCount(9)
    			+ state.stoneCount(8) + state.stoneCount(7);
    	
    	int sbe = ((myScore - theirScore) + (myBins - theirBins));
    	
    	return sbe;
    }
}

