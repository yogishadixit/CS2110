// Don't import anything else.

package cs2110.yad4.assignment2;
import java.util.ArrayList; // useful data structure
import java.util.Arrays; // only use the toString method
import java.util.Collections; // only use the sort method

/**
 * This class represents a schedule of the execution of tasks on identical processors.
 * 
 */
public class Schedule {
	private int[] tasks;
	private int m;
	
	/* Definition:
	 * An invariant of a class is a property that every object of the
	 * class has to satisfy. In particular, every method of the class
	 * should preserve the invariant.
	 * 
	 */
	
	/** 
	 * Satisfies invariant: 
	 *   for any two pairs tr1, tr2 in schedule:
	 *     if [ (tr1.p == tr2.p) and (tr1.id <= tr2.id) ] then
	 *       tr1 has to appear before tr2 in the array schedule
	 * 
	 * NOTE:
	 * Do NOT make in your code any stronger assumption for this ordering property.
	 * In particular, do not assume that pairs also occur in increasing order of pair.p.
	 * 
	 */
	private ScheduledTask[] schedule;

	/**
	 * If the arguments are not valid, throw an IllegalArgumentException.
	 * 
	 * @param tasks Duration of each task.
	 * @param m Number of processors.
	 * @param schedule The schedule for each task.
	 */
	public Schedule(int[] tasks, int m, ScheduledTask[] schedule) 
	{
		if (areValid(tasks, m) != true || Schedule.isConsistent(tasks, m, schedule) != true)
			throw new IllegalArgumentException();
		this.tasks = tasks;
		this.m = m;
		this.schedule = Schedule.orderTasks(schedule);
	}
	
	/**
	 * This method checks to see that both the number of tasks given and the number of processors
	 * given is greater than one. Then it makes sure that the duration of each task is a positive
	 * number.
	 * @param tasks Duration of each task.
	 * @param m Number of processors.
	 * @return True iff (tasks & m are valid).
	 */
	public static boolean areValid(int[] tasks, int m)
	{
		boolean valid = true;
		
		// this checks to see if the length of tasks is zero/negative or if the number of processors is 
		// zero/negative
		if (m <= 0 || tasks.length <= 0)
		{
			return false;
		}
		
		// this checks that each of the durations of the tasks are positive
		for (int task : tasks)
		{
			if (task <= 0)
			{
				valid = false;
				break;
			}
		}
		return valid;
	}
	
	/**
	 * This is a helper method that I created to order the tasks in the ScheduledTask[] according to
	 * the invariant. It basically converts the array into an arraylist and sorts that and then converts
	 * it back to an array.
	 * @param schedule the ScheduledTask array that needs to be sorted
	 * @return a sorted version of the input array
	 */
	private static ScheduledTask[] orderTasks(ScheduledTask[] schedule)
	{
		ArrayList<ScheduledTask> ordered = new ArrayList<ScheduledTask>();
		// this pours all the contents of the array schedule into an ArrayList ordered
		for (ScheduledTask task : schedule)
		{
			if (task != null)
			{
				ordered.add(task);
			}
		}
		
		// this sorts the ArrayList based on the natural ordering of its contents, which in this case
		// will order the tasks by id
		Collections.sort(ordered);
		
		// this returns an array representation of the ordered arraylist
		ScheduledTask[] orderedArray = new ScheduledTask[ordered.size()];
		return ordered.toArray(orderedArray);
	}

	/**
	 * Precondition: tasks and m are valid.
	 * (You can assume that the precondition holds true.)
	 * 
	 * YOU HAVE TO IMPLEMENT THIS:
	 * Rearrange the objects in the argument "ScheduledTask[] schedule"
	 * so that the following holds after the method ends:
	 *   For any two pairs tr1, tr2 in schedule:
	 *     if [ (tr1.p == tr2.p) and (tr1.id <= tr2.id) ] then
	 *       tr1 has to appear before tr2 in the array schedule
	 * This property does not have to hold when the method starts, but it
	 * is your job to make it hold when your method ends.
	 * 
	 * In addition to the above, this method checks to see that all tasks have been assigned to processors
	 * and that some tasks have not been assigned twice.
	 * 
	 * @param tasks Duration of each task.
	 * @param m Number of processors.
	 * @return True iff schedule is valid.
	 */
	public static boolean isConsistent(int[] tasks, int m, ScheduledTask[] schedule) 
	{
		// rearrange the objects in the argument schedule
		schedule = Schedule.orderTasks(schedule);
		
		// checks if there are as many durations as there are tasks. this check can be fooled if one task is
		// not included but another is included twice, but the next check will catch if that occurs
		if (tasks.length != schedule.length)
		{
			return false;
		}
		// check that all tasks are assigned and there is no overlap of tasks
		// basically checks if the id's in schedule gradually increment by 1. if they don't then there is a 
		// repeat
		// also checks that all the processor id's are 0, 1, 2....
		for (int i = 0; i < tasks.length; i++)
		{
			if (schedule[i].id != i || schedule[i].p >= m)
			{
				return false;
			} 
			
		}
		
		return true;
	}
	
	/**
	 * Definition of makespan:
	 * The total time elapsed after the last task finishes.
	 * 
	 * @return The makespan of the schedule.
	 */
	public int getMakespan() 
	{
		// this is an array list that stores the duaration of all the tasks on each processor
		int[] lengths = new int[m];
		for (ScheduledTask e : schedule)
		{
			// this is supposed to skip the task in the array if it is null. just testing edge cases
			if (e == null)
			{
				continue;
			}
			lengths[e.p] += tasks[e.id];
		}
		// this sorts the array and then returns the last element, which should be the largest element, and
		// in this case, our makespan
		Arrays.sort(lengths);
		return lengths[m-1];
	}
	
	/**
	 * This method finds the utilization of the processors.
	 * @return The utilization of the processors w.r.t. to this schedule.
	 */
	public double getUtilization() 
	{
		// this gets the total duration of ALL the tasks
		double totalTime = 0;
		for (int i = 0; i < tasks.length; i++)
		{
			totalTime += tasks[i];
		}
		// this is the formula for the utilization: the total duration divided by the makespan times the 
		// number of processors
		double temp = this.getMakespan() * m;
		double utilization = totalTime / temp;
		return utilization;
	}
	
	/**
	 * This is an accessor method that gets the ScheduledTask array specific to this Schedule
	 * @return the private ScheduledTask[] schedule that belongs to this instance of the Schedule
	 */
	public ScheduledTask[] getScheduledTasks()
	{
		return schedule;
	}
	
	/**
	 * Creates a String representation of the schedule.
	 */
	public String toString()
	{
		String result = "# Tasks: " + tasks.length + "\n" + "Durations: " + Arrays.toString(tasks)
				+ "\n" + "# Processors: " + m;
		for (int processorNumber = 0; processorNumber < m; processorNumber++)
		{
			result = result.concat("\n" + "Schedule @" + processorNumber + ": ");
			for (ScheduledTask e : schedule)
			{
				if (e.p == processorNumber)
				{
					result = result.concat(e.id + ", ");
				}
			}
		}
		result = result.concat("\n" + "Makespan: " + this.getMakespan());
		return result;
	}
}
