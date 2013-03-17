//Don't import anything else.

package cs2110.yad4.assignment2;

import java.util.Arrays; // you can only use toString & sort

/**
 *
 * The purpose of this class is to compute an optimal schedule for the
 * execution of tasks on identical processors.
 *
 */
public class ScheduleGenerator {
	private int[] tasks;
	private int m;
	private Schedule best;

	/**
	 * If the arguments are not valid, throw an IllegalArgumentException.
	 * 
	 * @param tasks Duration of each task. 
	 * @param m Number of processors.
	 */
	public ScheduleGenerator(int[] tasks, int m) 
	{
		if (!Schedule.areValid(tasks, m))
		{
			throw new IllegalArgumentException();
		}
		this.tasks = tasks;
		this.m = m;
	}
	
	/**
	 * Since the classical scheduling problem is hard to compute
	 * (it seems to require unavoidably super-polynomial time, because it is NP-complete)
	 * we ask you to implement a suboptimal fast heuristic algorithm.
	 * 
	 * Greedy heuristic:
	 * Schedule the tasks one by one, assigning at every stage a task to a
	 * processor that has minimum load (total processing to do).
	 * 
	 * This algorithm assigns tasks to processors in order from the task with the largest
	 * duration to the task with the shortest duration.
	 * 
	 * @return The schedule given by a suboptimal heuristic algorithm.
	 */
	public Schedule heuristicScheduling() 
	{ 
		// this creates an array, originally empty, of all the tasks scheduled in the heuristic manner
		ScheduledTask[] heuristic = new ScheduledTask[tasks.length];
	
		// this creates an array that holds the duration of tasks on every processor
		int[] processorDuration = new int[m];
		
		// this creates a clone of the tasks array so that it can be messed with
		int[] junkTasks = tasks.clone();
		
		// this is the index of how many tasks have already been scheduled
		int index = 0;
		
		while (index < tasks.length)
		{
			// this is the index of the processor with the smallest duration
			int smallestIndex = 0;
			
			// this finds the processor with the smallest duration
			for (int i = 0; i < m; i++)
			{
				if (processorDuration[i] < processorDuration[smallestIndex])
					smallestIndex = i;
			}
			
			// this is the id of the task with the largest duration
			int largestIndex = 0;

			// this finds the task with the largest duration
			for (int i = 0; i < junkTasks.length; i++)
			{
				if (junkTasks[i] > junkTasks[largestIndex])
					largestIndex = i;
			}
			
			// this schedules the task with the largest duration at the processor with the smallest duration
			// and assigns it to heuristic array at the index
			heuristic[index] = new ScheduledTask(largestIndex, smallestIndex);
			
			// this increments the duration of the processor to which the task just got assigned
			processorDuration[smallestIndex] += tasks[largestIndex];
			
			// this sets the duration of the task that was just scheduled to zero
			junkTasks[largestIndex] = 0;
			
			// this increments the index since another task just got assigned
			index++;
		}
		
		return new Schedule(tasks, m, heuristic);
	}
	
	/**
	 * Find an optimal schedule, that is one with minimum makespan.
	 * This method uses the recursive algorithm implemented below.
	 */
	public Schedule getOptSchedule() 
	{
		ScheduledTask[] schedule = new ScheduledTask[tasks.length];
		best = heuristicScheduling(); 
		recursive(schedule, 0, new int[m]);
		return best;
	}
	 
	/**
	 * This method takes an initially empty array of scheduled tasks and starts filling them one by one. A task 
	 * gets assigned to each of the processors (it won't be assigned if doing so makes the duration of the 
	 * tasks on that particular processor greater than the makespan of the best schedule so far). Once a task is 
	 * assigned, the method recursively calls itself so that the next task can be assigned. Once all the tasks 
	 * have been assigned, the method returns and the makespan is checked. 
	 * @param schedule the array containing all the tasks that have already been assigned to processors
	 * @param startIndex the index of the first task in the tasks array that has yet to be assigned. it gets 
	 * incremented with each recursive call to this method
	 * @param processorDurations the array containing the durations of each of the processors
	 * @return the schedule with the smallest makespan that can be created
	 */
	private void recursive(ScheduledTask[] schedule, int startIndex, int[] processorDurations)
	{	
		// this is the base case. if the start index is at the size of schedule, aka all the tasks have
		// been assigned, then the method returns the complete schedule formed.
		if (startIndex == schedule.length)
		{
			Schedule temp = new Schedule(tasks, m, schedule);
			if (temp.getMakespan() < best.getMakespan())
			{
				best = new Schedule(tasks, m, temp.getScheduledTasks());
			}
		}
		if (startIndex < schedule.length)
		{
			// this tries adding the task to each processor and finds the most optimal schedule formed
			for (int i = 0; i < m; i++)
			{
				// this checks to see if adding a task would make the makespan greater than the optimum
				// if it does, then it's probably not the best solution
				if ((processorDurations[i] + tasks[startIndex]) > best.getMakespan())
					continue;
				
				// this assigns the task to the processor i
				schedule[startIndex] = new ScheduledTask(startIndex, i);
			
				// this increments the total duration of the processor that the task just got assigned to
				int [] processorDurationsTemp = processorDurations.clone();
				processorDurationsTemp[i] += tasks[startIndex];
				
				// this is where the recursion occurs
				recursive(schedule, startIndex + 1, processorDurationsTemp);
			}
		}
		
	}
	/**
	 * NOTE: You do NOT have to use this class if you don't want to or
	 * you can't see why it's useful.
	 * 
	 * The purpose of this class is to provide a basic implementation of a "mutable integer". 
	 */
	class MyInt {
		public int v;
		public MyInt(int v) { this.v = v; }
		public String toString() { return Integer.toString(v); }
	}
	
}
