import java.util.ArrayList; // You are not allowed to use anything else from the Java API.

/**
 * Adjacency list implementation of a weighted directed graph.
 * 
 * Mathematical definition of a weighted digraph:
 * It is a quintuple G = (V,E,source,target,w), where:
 * - V is a finite set of vertices: without loss of generality V = { 0, ..., n-1 } for some non-negative integer n.
 * - E is a finite set of edges: without loss of generality E = { 0, ..., m-1 } for some non-negative integer m.
 * - source: E -> V is a function that maps each edge to its source vertex.
 * - target: E -> V is a function that maps each edge to its target vertex.
 * - w: E -> R^+ is a function that maps each edge to its weight, which is a non-negative real number.
 * 
 * Notes:
 * - The above definition allows parallel edges. So does the implementation.
 *   Definition: Two distinct edges e1 != e2 are parallel if source(e1) = source(e2) and target(e1) = target(e2).
 * 
 */
public class DigraphW {
	/** Number of vertices. */
	private int n;
	/** Number of edges. */
	private int m;
	/** Adjacency list. */
	AdjListEntry[] edges;
	/** Heuristic distance estimates. */
	double[][] heurDist; // heurDist[u][v] = heuristic estimate of the distance from u to v. HINT: To be used in A*.

	/**
	 * Constructs a digraph with no edges.
	 * 
	 * @param n Number of vertices.
	 */
	public DigraphW(int n) {
		this.n = n;
		edges = new AdjListEntry[n];
		heurDist = new double[n][n];
	}

	/**
	 * NOTE:
	 * The heuristic distance estimate must always UNDERESTIMATE the real distance.
	 * 
	 * @param u Source vertex.
	 * @param v Target vertex.
	 * @param heurD Heuristic distance estimate for distance from u to v.
	 */
	public void setHeurDist(int u, int v, double heurD) {
		if (u < 0 || u >= n || v < 0 || v >= n || heurD < 0)
			throw new IllegalArgumentException();

		heurDist[u][v] = heurD;
	}

	/**
	 * O(1).
	 * 
	 * Adds an edge to the graph.
	 * 
	 * @param u Source vertex.
	 * @param v Target vertex.
	 * @param w Weight.
	 */
	public void addEdge(int u, int v, double w) {
		if (u < 0 || u >= n)
			throw new IllegalArgumentException();

		AdjListEntry newEntry = new AdjListEntry(n,v,w);
		newEntry.next = edges[u];
		edges[u] = newEntry;
	}

	/**
	 * @return Number of vertices of the graph.
	 */
	public int getN() {
		return n;
	}

	/**
	 * @return Number of edges of the graph.
	 */
	public int getM() {
		return m;
	}

	/**
	 * O(size of graph).
	 * 
	 * @param u Source vertex.
	 * @param v Target vertex.
	 * @return True iff there is an edge (u,v) (even if its weight is 0)
	 */
	public boolean isThereEdge(int u, int v) {
		if (u < 0 || u >= n)
			throw new IllegalArgumentException();
		if (v < 0 || v >= n)
			throw new IllegalArgumentException();

		AdjListEntry current = edges[u];
		while (current != null) {
			if (current.vtx == v) return true;
			current = current.next;
		}

		return false;
	}

	/**
	 * 
	 * DIJKSTRA'S ALGORITHM
	 * 
	 * Precondition:
	 * visited & path are empty when the method is called.
	 * 
	 * HINT: A vertex is visited when it is removed from the priority queue.
	 * 
	 * Definition:
	 * The distance from u to v is the length of the shortest path from u to v.
	 * 
	 * @param source Source vertex.
	 * @param dest Destination vertex.
	 * @param visited Output argument: vertices visited (in the order they are visited)
	 * @param path Output argument: Path from source to dest that witnesses the returned distance. If there is no path, it should be empty.
	 * @return Distance from source to dest.
	 */
	public double shortestPath(int source, int dest, ArrayList<Integer> visited, ArrayList<Integer> path) {
		if (source < 0 || source >= n || dest < 0 || dest >= n || visited == null || path == null || visited.size() > 0 || path.size() > 0)
			throw new IllegalArgumentException();
		
		// construct the initial distance array
		double[] dist = new double[n];
		for (int i=0; i<n; i++) 
			dist[i] = Double.POSITIVE_INFINITY;
		dist[source] = 0;
		
		// construct the priority queue
		HeapEntry[] entries= new HeapEntry[n];
		for (int j=0; j<n; j++){
			entries[j]= new HeapEntry(j, dist[j]);
		}
		MinHeap heap= new MinHeap(entries);
		
		//back pointer array
		int[] pointer = new int[n];
		while(heap.n>0)
		{
			HeapEntry x= heap.extractMin();
			pointer[x.key]= visited.get(visited.size()-1);
			visited.add(x.key);
			
			// if the vertex you're at is the destination, gives you the path and distance
			if(x.key==dest)
			{
				int k = x.key;
				ArrayList<Integer> pathReversed = new ArrayList<Integer>();
				// this backtracks the path
				while (pointer[k] != source)
				{
					pathReversed.add(pointer[k]);
					k = pointer[k];
				}
				pathReversed.add(source);
				// this reconstructs the path in the right order
				for (int i = pathReversed.size()-1; i >= 0; i--)
				{
					path.add(pathReversed.get(i));
				}
				return dist[x.key];
			}
			
			// if the vertex isn't the destination, look at all the vertices connected to it and
			// update their distances
			AdjListEntry vertex = edges[x.key];
			while (vertex != null)
			{
				double newDist = dist[x.key] + vertex.weight;
				if (newDist < dist[vertex.vtx])
				{
					dist[vertex.vtx] = newDist;
					pointer[vertex.vtx] = x.key;
					heap.update(entries[vertex.vtx], dist[vertex.vtx]);
				}
			}
			
		}
		// there is no path from source to dest
		return -1;
	}


	/**
	 * 
	 * A STAR ALGORITHM.
	 * 
	 * Precondition:
	 * visited & path are empty when the method is called.
	 * 
	 * HINT: A vertex is visited when it is removed from the priority queue.
	 * 
	 * @param source Source vertex.
	 * @param dest Destination vertex.
	 * @param visited Output argument: vertices visited (in the order they are visited)
	 * @param path Output argument: Path from source to dest that witnesses the returned distance. If there is no path, it should be empty.
	 * @return Possibly suboptimal distance from source to dest.
	 */
	public double shortestPathHeur(int source, int dest, ArrayList<Integer> visited, ArrayList<Integer> path) {
		if (source < 0 || source >= n || dest < 0 || dest >= n || visited == null || path == null || visited.size() > 0 || path.size() > 0)
			throw new IllegalArgumentException();
		
		// builds distance arrays
		double[] dist = new double[n];
		for (int i=0; i<n; i++) 
			dist[i] = Double.POSITIVE_INFINITY;
		dist[source] = 0;
		
		// constructs priority queue
		HeapEntry[] entries= new HeapEntry[n];
		for (int j=0; j<n; j++)
		{
			entries[j]= new HeapEntry(j, dist[j] + heurDist[source][j]);
		}
		MinHeap heap= new MinHeap(entries);
		
		//back pointer array
		int[] pointer = new int[n];
		
		while(heap.n > 0)
		{
			HeapEntry x= heap.extractMin();
			
			// if the distances are still infinity, it means that they can't be reached!
			if (dist[x.key] == Double.POSITIVE_INFINITY) 
				break;
			
			pointer[x.key]= visited.get(visited.size()-1);
			visited.add(x.key);
			if(x.value==dest)
			{
				int k = x.key;
				ArrayList<Integer> pathReversed = new ArrayList<Integer>();
				// this backtracks the path
				while (pointer[k] != source)
				{
					pathReversed.add(pointer[k]);
					k = pointer[k];
				}
				pathReversed.add(source);
				// this reconstructs the path in the right order
				for (int i = pathReversed.size()-1; i >= 0; i--)
				{
					path.add(pathReversed.get(i));
				}
				return dist[x.key];
			}
			
			// if the vertex isn't the destination, look at all the vertices connected to it and
						// update their distances
			AdjListEntry vertex = edges[x.key];
			while (vertex != null)
			{
				double newDist = dist[x.key] + vertex.weight;
				if (newDist < dist[vertex.vtx])
				{
					dist[vertex.vtx] = newDist;
					pointer[vertex.vtx] = x.key;
					heap.update(entries[vertex.vtx], dist[vertex.vtx] + heurDist[x.key][vertex.vtx]);
				}
			}
		}
		return -1;
	}

	/**
	 * A DFS reachability algorithm.
	 * 
	 * @param source Source vertex.
	 * @param dest Destination vertex.
	 * @param visited Output argument: vertices visited (in the order they are visited)
	 * @param path Output argument: A path from source to dest. If there is no path, it should be empty.
	 * @return Length of returned path.
	 */
	public double DFS(int source, int dest, ArrayList<Integer> visited, ArrayList<Integer> path) {
		if (source < 0 || source >= n || dest < 0 || dest >= n || visited == null || path == null || visited.size() > 0 || path.size() > 0)
			throw new IllegalArgumentException();

		// Set of vertices that have already been processed.
		boolean[] closedVertices = new boolean[n];
		// Vertices in stack.
		boolean[] isInStack = new boolean[n];
		// distances
		double[] dist = new double[n];
		for (int i=0; i<n; i++) dist[i] = Double.POSITIVE_INFINITY;
		dist[source] = 0;
		// Back pointers.
		int[] previous = new int[n];
		for (int i=0; i<n; i++) previous[i] = -1;

		java.util.Stack<Integer> stack = new java.util.Stack<Integer>();
		stack.add(source);
		isInStack[source] = true;
		
		while (!stack.isEmpty()) {
			int u = stack.pop();
			isInStack[u] = false;
			closedVertices[u] = true;
			visited.add(u);
			// Destination reached?
			if (u == dest) break;

			AdjListEntry succ = edges[u]; // Edges emanating from u.
			while (succ != null) {
				int v = succ.vtx;

				if (!closedVertices[v]) {
					// Vertex v has not been processed yet.

					// Calculate new distance estimate to v (from source).
					double edgeWeight = succ.weight;
					double newDist = dist[u] + edgeWeight;

					if (!isInStack[v]) {
						stack.push(v);
						isInStack[v] = true;
					}
					
					if (newDist < dist[v]) {
						// Better distance estimate.
						// Update distance estimate for vertex v.
						dist[v] = newDist;
						previous[v] = u;
					}
				}

				succ = succ.next;
			} // successors loop

		} // main loop

		
		if (dist[dest] == Double.POSITIVE_INFINITY) // destination not reachable
			return dist[dest];

		// Find path to destination from back pointers.
		ArrayList<Integer> reversePath = new ArrayList<Integer>();
		int current = dest;
		while (current != source) {
			reversePath.add(current);
			current = previous[current];
		}
		path.add(source);
		for (int i=reversePath.size()-1; i>=0; i--)
			path.add(reversePath.remove(i));

		return dist[dest];
	}
	
	public String toString() {
		String str = "n = " + n + "\n";
		for (int k=0; k<n; k++) {
			str += "vtx " + k + ": [";
			AdjListEntry succ = edges[k];
			if (succ != null) {
				str += succ.vtx + ":" + succ.weight;
				succ = succ.next;
			}
			while (succ != null) {
				str += ", " + succ.vtx + ":" + succ.weight;
				succ = succ.next;
			}
			str += "]\n";
		}
		return str;
	}


}
