import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

/**
 * @author Xiaowen Feng and Peter Freschi
 * Extra Credit Options Implemented:  A5E1
 * 
 * Solution to Assignment 5 in CSE 373, Autumn 2016
 * University of Washington.
 * 
 * (Based on starter code v1.3. By Steve Tanimoto.)
 *
 * Java version 8 or higher is recommended.
 * This program utilizes a problem-space graph to solve the puzzle Towers of Hanoi
 *
 */

// Here is the main application class:
public class ExploredGraph {
	Set<Vertex> Ve; // collection of explored vertices
	Set<Edge> Ee;   // collection of explored edges
	ArrayList<Operator> Op; // collection of operators
	HashMap<String, Vertex> preds = new HashMap<String, Vertex>(); // collection of predecessors for vertices
	int numberOfPegs; 
	
	// constructor for a new ExploredGraph
	public ExploredGraph() {
		Ve = new LinkedHashSet<Vertex>(); 
		Ee = new LinkedHashSet<Edge>();
	}
	
	// initializes by emptying the set of edges and vertices
	public void initialize() {
		Ve.clear();
		Ee.clear();
	}
	
	// sets up an instance of this class, and inserts passed in Vertex into the vertices
	public void initialize(Vertex v) {
		initialize(); //clear the explored edges and vertices
		Ve.add(v);
	}
	
	// returns the number of vertices currently in the explored graph structure
	public int nvertices() {return Ve.size();}
	
	// returns the number of edges currently in the explored graph structure 
	public int nedges() {return Ee.size(); }
	
	// make a list of operators
	private void makeOperators() {
		Op = new ArrayList<Operator>();
		// create numberOfPegs*(numberOfPegs-1) operators
		int pegSize = numberOfPegs;
		for (int i = 0; i < pegSize; i++) {
			for (int j = 0; j< pegSize; j++) {
				if (i != j) {
					Op.add(new Operator(i, j));
				}
			}
		}
	}
	
	// runs an iterative depth-first search starting at Vertex vi, stopping either reaching Vertex vj
	// or running out of options or resources
	public void idfs(Vertex vi, Vertex vj) {
		searchHelper(vi, vj, false);
	} 
	
	//runs a breadth-first search starting at Vertex vi, and continues until reaching Vertex vj
	public void bfs(Vertex vi, Vertex vj) {
		searchHelper(vi, vj, true);
	}
	
	// a helper that helps run a BFS or IDFS
	private void searchHelper(Vertex vi, Vertex vj, boolean isBreadthSearch) {
		// clear stored vertices, edges and predecessors from the last search
		Ve.clear();
		Ee.clear();
		preds.clear();
		
		// constructs a list for possible visit vertices
		LinkedList<Vertex> open = new LinkedList<Vertex>();
		// constructs a list for already processed vertices
		LinkedList<Vertex> closed = new LinkedList<Vertex>();
		// make a list of operators for this search
		makeOperators();
		open.add(vi); // mark the start vertex can be visited
		preds.put(vi.toString(), null); // mark the start vertex's predecessor null
		
		// while there is can-be-visited vertex, traverse the whole graph
		while(!open.isEmpty()) { 
			Vertex v = open.removeFirst();
			// for each operator, if vertex v can be moved, generate its successor
			for (Operator op : Op) {
				if (op.precondition(v)) {
					Vertex successor = op.transition(v);
					 
					// only add the successor to open, if it hasn't been visited yet
					if (!open.contains(successor) && !closed.contains(successor)) { 
						
						if (isBreadthSearch) { // if is breadth search, add successor to the end of list
							open.addLast(successor);
						} else { // otherwise, the front
							open.addFirst(successor);
						}
						// store Vertex v as the successor's predecessor 
						preds.put(successor.toString(), v);
						Ee.add(new Edge(v, successor)); // create an edge for v and its successor, add to Ee
					}
				}
			}
			Ve.add(v); // mark as visited 
			closed.add(v); // mark as processed 
		}
	}

	//use the results of the most recent search. It should not start its own search.
	public ArrayList<Vertex> retrievePath(Vertex vi) {
		ArrayList<Vertex> resultPath = new ArrayList<Vertex>();
		resultPath = makePath(vi, resultPath); // constructs a path from start to end
		resultPath.add(vi);
		return resultPath;
	} 
	
	// helper to perform recursive backtracing
	private ArrayList<Vertex> makePath(Vertex v, ArrayList<Vertex> result) {
		// if predecessor of Vertex v is not null,
		// put its predecessor to the path
		if (preds.get(v.toString()) != null) {
			Vertex pred = preds.get(v.toString());
			result = makePath(pred, result);
			result.add(pred);	
		} 
		return result;
	}
	
	// first run bfs from the new start vertex, and it should then use retrievePath from the new destination vertex.
	public ArrayList<Vertex> shortestPath(Vertex vi, Vertex vj) {
		bfs(vi, vj);
		return retrievePath(vj);
	}
	
	// return a set of explored vertices
	public Set<Vertex> getVertices() {return Ve;} 
	// return a set of explored edges
	public Set<Edge> getEdges() {return Ee;} 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Tests for any number of pegs
		ExploredGraph eg = new ExploredGraph();
		// Test the vertex constructor: 
		Vertex v0 = eg.new Vertex("[[4,3,2,1],[],[]]");
		Vertex v1 = eg.new Vertex("[[],[],[4,3,2,1]]");
		eg.bfs(v0, v1);	
	}
	
	// Each Vertex will hold a Towers-of-Hanoi state.
	class Vertex {
		ArrayList<Stack<Integer>> pegs; 
		// Constructor that takes a string with any number of pegs, such as "[[4,3,2,1],[],[]]":
		public Vertex(String vString) {
			String[] parts = vString.split("\\],\\[");
			numberOfPegs = parts.length;
			pegs = new ArrayList<Stack<Integer>>(numberOfPegs);
			for (int i=0; i<numberOfPegs;i++) {
				pegs.add(new Stack<Integer>());
				try {
					parts[i]=parts[i].replaceAll("\\[","");
					parts[i]=parts[i].replaceAll("\\]","");
					List<String> al = new ArrayList<String>(Arrays.asList(parts[i].split(",")));
					//System.out.println("ArrayList al is: "+al);
					Iterator<String> it = al.iterator();
					while (it.hasNext()) {
						String item = it.next();
                        if (!item.equals("")) {
                                //System.out.println("item is: "+item);
                                pegs.get(i).push(Integer.parseInt(item));
                        }
					}
				}
				catch(NumberFormatException nfe) { nfe.printStackTrace(); }
			}
		}
		
		// returns a string representation of the Vertex
		public String toString() {
			String ans = "[";
			for (int i=0; i<pegs.size(); i++) {
			    ans += pegs.get(i).toString().replace(" ", "");
				if (i<numberOfPegs - 1) { ans += ","; }
			}
			ans += "]";
			return ans;
		}
		// checks for equality between this Vertex and other
		public boolean equals(Object o) {
			if (o.getClass() != this.getClass()) { 
				return false;
			}
			Vertex v = (Vertex) o;
			return this.toString().equals(v.toString());
		}
	}
	
	// Represents edges of the graph.
	class Edge {
		private Vertex vi;
		private Vertex vj;
		
		// accepts two vertices to construct an edge
		public Edge(Vertex vi, Vertex vj) {
			this.vi = vi;
			this.vj = vj;
		}
		
		// returns a string representation of an edge
		public String toString() {
			return "Edge from " + vi.toString() + " to" + vj.toString();
		}
		
		// returns the first endpoint of the edge
		public Vertex getEndpoint1() {
			return vi;
		}
		
		// returns the second endpoint of the edge
		public Vertex getEndpoint2() {
			return vj;
		}
	}
	
	// represents operators
	class Operator {
		private int i, j;
		// Constructor for operators, accepting a start and finish position.
		public Operator(int i, int j) { 
			this.i = i;
			this.j = j;
		}
		
		// Determines whether or not this operator is applicable to this vertex.
		// i.e. when it would be possible to move a disk from peg i to peg j
		public boolean precondition(Vertex v) {
			if (v.pegs.get(i).isEmpty()) {  
				return false;
			}
			if (v.pegs.get(j).isEmpty()) {
				return true;
			} 
			// if either i or j is empty, disk can be moved if disk in j is bigger
			return (v.pegs.get(i).peek() < v.pegs.get(j).peek());
		}
		
		// return a "successor" of passed in vertex, according to this operator's transition state.
		public Vertex transition(Vertex v) {
			Vertex newV = new Vertex(v.toString());
			int mDisk = newV.pegs.get(i).pop();
			newV.pegs.get(j).push(mDisk);
			return newV;
		}
		
		// returns a string representing the operator
		public String toString() {
			return "Operator moving from peg " + i + " to peg " + j;
		}
	}
}