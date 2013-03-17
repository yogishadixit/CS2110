/**
 * DO NOT MODIFY THIS FILE IN ANY WAY
 * 
 * A pair that represents the scheduling information for a single task.
 * 
 */

package cs2110.yad4.assignment2;
public class ScheduledTask implements Comparable<ScheduledTask> {
	public int id; // task id
	public int p; // processor

	public ScheduledTask(int id, int p) { this.id = id; this.p = p; }
	
	public String toString() { return "(id:" + id + ",p:" + p + ")"; }

	/**
	 * Pairs are ordered according to the natural order of the field id.
	 */
	public int compareTo(ScheduledTask pair) { return this.id - pair.id; }
}
