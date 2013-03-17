// DO NOT IMPORT ANYTHING.

/**
 *
 * A min binary heap.
 * 
 * MIN HEAP INVARIANT:
 * For every i = 1, ..., n-1: heap[getParent(i)] <= heap[i].
 * 
 * The data structure should always satisfy the invariant.
 *
 */
public class MinHeap {
	/** Number of entries in the heap. **/
	int n;
	/** The array that holds the entries of the heap. **/
	HeapEntry[] heap;

	/**
	 * Construct an empty heap.
	 * 
	 * @param nMax Maximum number of entries the heap can hold.
	 */
	public MinHeap(int nMax) 
	{
		n = 0;
		heap = new HeapEntry[nMax];
	}
	
	/**
	 * Can be O(n), but even if you do it in O(n log n) it is fine.
	 * 
	 * Constructs a heap from a collection of entries.
	 * 
	 * Assume: All the references in the collection are distinct.
	 * 
	 * @param entries A collection of heap entries.
	 */
	public MinHeap(HeapEntry[] entries) {
		if (entries == null)
			throw new IllegalArgumentException();
		n = 0;
		heap = new HeapEntry[entries.length];
		for (HeapEntry e : entries)
			this.add(e);
	}
	
	/**
	 * Should be O(log n).
	 * 
	 * Adds a new entry to the heap.
	 * 
	 * @param e A heap entry.
	 */
	public void add(HeapEntry e) {
		if (e == null)
			throw new IllegalArgumentException();
		if (n >= heap.length) // heap full
			throw new RuntimeException();
		heap[n] = e;
		int currentPos = n;
		while (currentPos > 0)
		{
			int parent = getParent(currentPos);
			if (heap[currentPos].compareTo(heap[parent]) >= 0)
				break;
			else
				swap(currentPos, parent);
			/*
			if (heap[currentPos].compareTo(heap[parent]) < 0)
				swap(currentPos, parent);	
			else 
				break;
				*/
			currentPos = parent;
		}
		n++;
		for (HeapEntry a: heap)
			if (a != null)
				System.out.println(a.toString());
		System.out.println("\n");
	}
	
	/**
	 * 
	 * @return Number of elements in the heap.
	 */
	public int size() {
		return n;
	}
	
	/**
	 * Should be O(1).
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 * @return The index for the parent of the subtree (-1 if there isn't one).
	 */
	public int getParent(int i) 
	{
		if (i < 0 || i >= heap.length)
			throw new IllegalArgumentException();
		int m = (i-1)/2;
		if (m >= 0)
			return m;
		else
			return -1;
	}
	
	/**
	 * Should be O(1).
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 * @return True iff the subtree rooted at i is a leaf.
	 */
	public boolean isLeaf(int i) {
		if (i < 0 || i >= n)
			throw new IllegalArgumentException();
		if (getLeft(i)==-1 && getRight(i)==-1)
			return true;
		else
			return false;
	}
	
	/**
	 * Should be O(1).
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 * @return The index for the left child of the subtree (-1 if there isn't one).
	 */
	public int getLeft(int i) 
	{
		if (i < 0 || i >= n)
			throw new IllegalArgumentException();
		int m = (2*i) + 1;
		if (m <= n)
			return m;
		else
			return -1;
	}
	
	/**
	 * Should be O(1).
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 * @return The index for the right child of the subtree (-1 if there isn't one).
	 */
	public int getRight(int i) {
		if (i < 0 || i >= n)
			throw new IllegalArgumentException();
		int m = (2*i) + 2;
		if (m <= n)
			return m;
		else
			return -1;
	}
	
	/**
	 * Should be O(log n).
	 * 
	 * Assume:
	 * - The subtree rooted at getLeft(i) satisfies the heap invariant.
	 * - The subtree rooted at getRight(i) satisfies the heap invariant.
	 * 
	 * After the method terminates, the subtree rooted at i must
	 * satisfy the heap invariant.
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 */
	private void heapify(int i) 
	{
		if (i < 0 || i >= n)
			throw new IllegalArgumentException();
		if (isLeaf(i))
			return;
		int left = getLeft(i);
		int right = getRight(i);
		if (heap[i].compareTo(heap[left]) < 0 && heap[i].compareTo(heap[right]) < 0)
			return;
		else if (heap[left].compareTo(heap[right]) < 0)
		{
			swap(i, left);
			heapify(left);
		}
		else
		{
			swap(i, right);
			heapify(right);
		}
	}
	
	private void swap(int a, int b)
	{
		HeapEntry temp = heap[b];
		heap[b] = heap[a];
		heap[a] = temp;
	}
	
	/**
	 * 
	 * @return The entry at the top of the heap.
	 */
	public HeapEntry peekMin() {
		if (n == 0)
			throw new IllegalArgumentException();
		return heap[0];
	}
	
	/**
	 * Should be O(log n).
	 * 
	 * Removes the entry from the top.
	 * 
	 * @return The entry at the top of the heap.
	 */
	public HeapEntry extractMin() {
		if (n == 0)
			throw new IllegalArgumentException();
		HeapEntry removed = heap[0];
		heap[0] = heap[n-1];
		heap[n-1] = null;
		int currentPos = 0;
		do 
		{
			int left = getLeft(currentPos);
			int right = getRight(currentPos);
			if (heap[left].compareTo(heap[currentPos]) < 0 && heap[left].compareTo(heap[right]) < 0)
				heap[currentPos] = heap[left];
			else if (heap[right].compareTo(heap[currentPos]) < 0)
				heap[currentPos] = heap[right];
			else
				break;
		}
		while (currentPos < n);
		n = n-1;
		return removed;
	}
	
	/**
	 * Should be O(log n).
	 * 
	 * Updates a tuple with a new value <= current value.
	 * 
	 * @param e An entry that is inside the heap.
	 * @param newValue New value, has to be <= e.value.
	 */
	public void update(HeapEntry e, double newValue) {
		if (e.value < newValue) {
			System.err.println("e.value = " + e.value + ", newValue = " + newValue);
			throw new IllegalArgumentException();
		}
		e.value= newValue;
		while (getParent(e.heapIndex)!=-1){
			HeapEntry parent= heap[getParent(e.heapIndex)];
			if (parent.compareTo(e)>0){
				swap(parent.heapIndex, e.heapIndex);
			}
			else{
				break;
			}
		}
	}
	
	// YOU CAN USE THE METHODS BELOW TO DEBUG YOUR PROGRAM.
	// Of course, passing the sanity check does not mean your program is correct.
	// You don't have to use these methods or understand them, if you don't want to.
	
	/**
	 * Checks the heap.
	 */
	public boolean checkHeap() {
		// Check heap indexes.
		/*
		for (int i=0; i<n; i++) {
			if (heap[i].heapIndex != i) {
				System.err.println("Heap indexes not maintained correctly.");
				return false;
			}
		}
		*/
		// Check heap invariant.
		for (int i=1; i<n; i++) {
			if (heap[i].compareTo(heap[getParent(i)]) < 0) {
				System.err.println("Heap invariant violated @" + i + ".");
				System.err.println("Current: " + heap[i] + ".");
				System.err.println("Parent: " + heap[getParent(i)] + ".");
				return false;
			}
		}
		
		return true;
	}
	
	private final String indentation = "   ";
	public String toString() {
		if (n == 0) return "";
		
		return toStringHelper(0,"");
	}
	
	private String toStringHelper(int i, String indent) {
		if (i < 0 || i >= n)
			throw new IllegalArgumentException();
		
		String str = indent + heap[i].toString() + "\n";
		
		int left = getLeft(i);
		if (left == -1) return str;
		String newIndent = indent+indentation;
		str += toStringHelper(left,newIndent);
		
		int right = getRight(i);
		if (right == -1) return str;
		str += toStringHelper(right,newIndent);
		
		return str;
	}
}
