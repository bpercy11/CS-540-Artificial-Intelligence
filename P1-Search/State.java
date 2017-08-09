import java.util.ArrayList;

/**
 * A state in the search represented by the (x,y) coordinates of the square and
 * the parent. In other words a (square,parent) pair where square is a Square,
 * parent is a State.
 * 
 * You should fill the getSuccessors(...) method of this class.
 * 
 */
public class State {
	
	private Square square;
	private State parent;

	// Maintain the gValue (the distance from start)
	// You may not need it for DFS but you will
	// definitely need it for AStar
	private int gValue;

	// States are nodes in the search tree, therefore each has a depth.
	private int depth;

	/**
	 * @param square
	 *            current square
	 * @param parent
	 *            parent state
	 * @param gValue
	 *            total distance from start
	 */
	public State(Square square, State parent, int gValue, int depth) {
		this.square = square;
		this.parent = parent;
		this.gValue = gValue;
		this.depth = depth;
	}

	public boolean checker(State obj){
		//System.out.println("first print");
		State s = (State)obj;
		if(this.getX() == s.getX() && this.getY() == s.getY()){
			//System.out.println("Printing");
			return true;
		}
		return false;
	}
	
	
	/**
	 * @param visited
	 *            explored[i][j] is true if (i,j) is already explored
	 * @param maze
	 *            initial maze to get find the neighbors
	 * @return all the successors of the current state
	 */
	public ArrayList<State> getSuccessors(boolean[][] explored, Maze maze) {
		// FILL THIS METHOD

		// TODO check all four neighbors (up, right, down, left)
		// TODO remember that each successor's depth and gValue are
		// +1 of this object.
		
		ArrayList<State> children = new ArrayList<State>();
		
		int x = getX();
		int y = getY();
		
				
		//NEED TO FIND OUT HOW TO INITIALIZE 'parent' TO SET NEW STATES OF SUCCESORS
		//NEED TO CHECK IF IS A WALL, OR IF ALREADY EXPLORED --> nullpointerexception right now
		//maze.getSquareValue(parent.getX()+1, parent.getY()) != '%' && 
		if (maze.getSquareValue(this.getX(), this.getY()-1) != '%' && explored[x][y-1] != true){
			Square leftSquare = new Square(x, y-1);
			State leftState = new State(leftSquare, this, this.getDepth()+1, this.getDepth()+1);		
			children.add(leftState);
			
		}
		
		if (maze.getSquareValue(this.getX()+1, this.getY()) != '%' && explored[x+1][y] != true){
			Square downSquare = new Square(x+1, y);
			State downState = new State(downSquare, this,  this.getDepth()+1, this.getDepth()+1);
			children.add(downState);
		}
			
		if (maze.getSquareValue(this.getX(), this.getY()+1) != '%' && explored[x][y+1] != true){
			Square rightSquare = new Square(x, y+1);
			State rightState = new State(rightSquare, this,  this.getDepth()+1, this.getDepth()+1);
			children.add(rightState);
			
		}
		 
		if (maze.getSquareValue(this.getX()-1, this.getY()) != '%' && explored[x-1][y] != true){
			Square upSquare = new Square(x-1, y);
			State upState = new State(upSquare, this,  this.getDepth()+1, this.getDepth()+1);
			children.add(upState);			
		}
		
		//int i;
		//for(i = 0; i< children.size(); i++) {
		//	System.out.println("child: " + children.get(i).getX() + " " + children.get(i).getY());	
		//}
		//System.out.println("children this round =  " + childCount);

		return children;
	}

	/**
	 * @return x coordinate of the current state
	 */
	public int getX() {
		return square.X;
	}

	/**
	 * @return y coordinate of the current state
	 */
	public int getY() {
		return square.Y;
	}

	/**
	 * @param maze initial maze
	 * @return true is the current state is a goal state
	 */
	public boolean isGoal(Maze maze) {
		if (square.X == maze.getGoalSquare().X
				&& square.Y == maze.getGoalSquare().Y)
			return true;

		return false;
	}

	/**
	 * @return the current state's square representation
	 */
	public Square getSquare() {
		return square;
	}

	/**
	 * @return parent of the current state
	 */
	public State getParent() {
		return parent;
	}

	/**
	 * You may not need g() value in the BFS but you will need it in A-star
	 * search.
	 * 
	 * @return g() value of the current state
	 */
	public int getGValue() {
		return gValue;
	}

	/**
	 * @return depth of the state (node)
	 */
	public int getDepth() {
		return depth;
	}
	
}
