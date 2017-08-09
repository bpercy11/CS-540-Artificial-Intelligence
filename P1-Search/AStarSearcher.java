import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}
	
	public double assignF(State cur, State goal){
		double u = cur.getX();
		double v = cur.getY();
		double p = goal.getX();
		double q = goal.getY();
		
		double x = Math.abs(u - p);
		double y = Math.abs(v - q);
		
		double hval = Math.sqrt((x)*(x) + (y)*(y));

		double distance = hval + cur.getGValue();	
		//System.out.println("hval, gval = " + hval + ", " + cur.getGValue());
		//System.out.println("Distance = " + distance);
		return distance;
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD

		// explored list is a Boolean array that indicates if a state associated with a given position in the maze has already been explored. 
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();
		State initialState = new State(maze.getPlayerSquare(), null, 0, 0);
		StateFValuePair initial = new StateFValuePair(initialState, 0);
		frontier.add(initial);
		//System.out.println("in a star");
		 
		
		// TODO initialize the root state and add
		// to frontier list
		// ...

		while (!frontier.isEmpty()) {
			// TODO return true if a solution has been found
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found
			StateFValuePair cur = frontier.poll();
			
			if(cur.getState().isGoal(maze)){
				if(maxDepthSearched < cur.getState().getDepth()){
					maxDepthSearched = cur.getState().getDepth();
				}
				//noOfNodesExpanded++;
				
				LinkedList<State> correctpath = new LinkedList<State>();
				State itr = cur.getState();
				
				while(itr.getParent()!=null){
					correctpath.add(itr);
					itr = itr.getParent();
					cost++;
					maze.setOneSquare(itr.getSquare(), '.');
					maze.setOneSquare(maze.getPlayerSquare(), '.');
				}
				
				return true;
			}
			
			explored[cur.getState().getX()][cur.getState().getY()] = true;
			noOfNodesExpanded++;
			
			ArrayList<State> children = cur.getState().getSuccessors(explored, maze);
			//ArrayList<StateFValuePair> fvalchildren = new ArrayList<StateFValuePair>();
			State goal = new State(maze.getGoalSquare(), null, 0, 0);
			//System.out.println("Goal: " + maze.getGoalSquare().X+ " , " + maze.getGoalSquare().Y);				
			
			
			for(int m = 0; m < children.size(); m++){
				double val = assignF(children.get(m), goal);
				StateFValuePair newchild = new StateFValuePair(children.get(m), val);
				if(!(frontier.contains(newchild))){
					frontier.add(newchild);
				}
				else if(frontier.contains(newchild)) {
					// TODO compare which has lower gvalue
					
				}
			}
			
			//for(int i = 0; i < children.size(); i++){
			//	System.out.println("Children: " + children.get(i).getX() + " , " + children.get(i).getY());
			//}
		
			//for(int k = 0; k < children.size(); k++){
			//	assignF(children.get(k), goal);
			//}
			
			/*
			boolean add;
			for(int k = 0; k<children.size(); k++){
					if(!(frontier.contains(children.get(k)))){
						frontier.add(null);
					}
			}
			
			
			
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
			*/
			
			
			
			if (frontier.size() > maxSizeOfFrontier) {
				maxSizeOfFrontier = frontier.size();
			}
			if(maxDepthSearched < cur.getState().getDepth()){
				maxDepthSearched = cur.getState().getDepth();
			}
			// use frontier.poll() to extract the minimum stateFValuePair.
			// use frontier.add(...) to add stateFValue pairs
		}

		// TODO return false if no solution
		return false;
	}

}
