import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
 
/**
 * Depth-First Search (DFS)
 * 
 * You should fill the search() method of this class.
 */
public class DepthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public DepthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main depth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	//think that output keys are wrong, should be 94 explored, maxdepth should be 44
	//how to keep track of correct path?
	//have correct expanded nodes for input2 and 3 and correct size of frontier
	//USE HASHMAP TO RECORD CORRECT PATH
	
	public boolean search() {		
		// explored list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
			
		// Players starting spot S
		State initial = new State(maze.getPlayerSquare(), null, 0, 0);
		
		
		// Stack implementing the Frontier list
		LinkedList<State> stack = new LinkedList<State>();
		
		// pushing initial player square S onto stack
		stack.push(initial);
		
		
		// SETTING MAX SIZE/MAX DEPTH/NODES EXPANDED CURRENTLY
		maxSizeOfFrontier = 0;
		maxDepthSearched = 0;
		int globalchildren = 0;
		
		
		while (!stack.isEmpty()) {
					
			State cur = stack.pop();
			
			//hash.put(cur.getParent(), cur.getSquare());
			
			//System.out.print("Popped: " + cur.getX() + " " + cur.getY()+ " || Stack: " );
			
			//for(int f = 0; f < stack.size(); f++){
			//	System.out.print(" " + stack.get(f).getX() + " " +stack.get(f).getY() +" ");
			//}
			//System.out.println("");
			//System.out.println("");
			
			//Return true if found the goal
			if(cur.isGoal(maze)){
				if(maxDepthSearched < cur.getDepth()){
					maxDepthSearched = cur.getDepth();
				}
				noOfNodesExpanded++;
				
				LinkedList<State> correctpath = new LinkedList<State>();
				State itr = cur;				
				
				while(itr.getParent()!=null){
					correctpath.add(itr);
					itr = itr.getParent();
					cost++;
					maze.setOneSquare(itr.getSquare(), '.');
					maze.setOneSquare(maze.getPlayerSquare(), 'S');
				}
				//System.out.println("iterator: "+ itr.getX() +" , " + itr.getY());
				//System.out.println("Cost = " + cost);
				return true;
			}
			
			explored[cur.getX()][cur.getY()] = true;
			noOfNodesExpanded++;
			
			
			ArrayList<State> children = cur.getSuccessors(explored, maze);
			
			
			boolean add;
			for(int k =0; k<children.size(); k++){	
				add = true;
				for(int e = 0; e < stack.size(); e++){		
					if(stack.get(e).checker(children.get(k))){
						add = false;
					}
				}
				if(add){
					stack.push(children.get(k));
				}
			}
			
			
			//ADDING CHILDREN TO THE FRONTIER
			//int i;
			//for(i=0; i<children.size(); i++){
			//	stack.push(children.get(i));
			//}
			
			//if(children.size() != 0){
			//	noOfNodesExpanded++;
			//}
			
			// sets max size of frontier
			if (stack.size() > maxSizeOfFrontier) {
				maxSizeOfFrontier = stack.size();
			}
			if(maxDepthSearched < cur.getDepth()){
				maxDepthSearched = cur.getDepth();
			}
			
			
			// TODO return true if find a solution
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found
		}


		// TODO return false if no solution
		return false;
	}
}
