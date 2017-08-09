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

public class randomPlayer extends Player {


    /*Use IDS search to find the best move. The step starts from 1 and keeps incrementing by step 1 until
	 * interrupted by the time limit. The best move found in each step should be stored in the
	 * protected variable move of class Player.
     */
    public void move(GameState state)
    {
    	
    }

    // Return best move for max player. Note that this is a wrapper function created for ease to use.
	// In this function, you may do one step of search. Thus you can decide the best move by comparing the 
	// sbe values returned by maxSBE. This function should call maxAction with 5 parameters.
    public int maxAction(GameState state, int maxDepth)
    {
    	return 0;
    }
    
    // Return best move for min player. Note that this is a wrapper function created for ease to use.
	// In this function, you may do one step of search. Thus you can decide the best move by comparing the 
	// sbe values returned by maxSBE. This function should call minAction with 5 parameters.
   // public moveSBEPair minAction(GameState state, int maxDepth)
   // {
   // 	return minAction(state, 0, maxDepth, -10000, 10000);
    	
   // }
    
	//return sbe value related to the best move for max player
    public int maxAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta)
    {
    	return 0;
    }
    
    //return sbe value related to the best move for min player
    public int minAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta)
    {

    	return 0;
    }

    //the sbe function for game state. Note that in the game state, the bins for current player are always in the bottom row.
    //found SBE function from https://blog.hackerrank.com/mancala/
    private int sbe(GameState state)
    {
    	int myScore = state.stoneCount(6);
    	int theirScore = state.stoneCount(13);
    	
    	int myBins = state.stoneCount(5) + state.stoneCount(4) + state.stoneCount(3) + state.stoneCount(2) + state.stoneCount(1) + state.stoneCount(0);
    	int theirBins = state.stoneCount(12) + state.stoneCount(11) + state.stoneCount(10) + state.stoneCount(9) + state.stoneCount(8) + state.stoneCount(7);
    	
    	int sbe = ((myScore - theirScore) + (myBins - theirBins));
    	
    	return sbe;
    }


}

