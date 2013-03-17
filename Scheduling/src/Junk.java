import java.util.Arrays;


public class Junk
{
	public static void main(String[] args)
	{
		int[] tasks = new int[] { 7,5,2,4,6 };
		int[][] tasksOrdered = new int[tasks.length][];
		for (int i = 0; i < tasksOrdered.length; i++)
		{
			int smallest = 0;
			for (int j = 0; j < tasksOrdered.length; j++)
			{
				if (tasks[j] < tasks[smallest])
				{
					smallest = j;
					tasks[j] = 1000000;
				} 
			}
			tasksOrdered[i] = new int[] {smallest, tasks[smallest] };
			System.out.println(Arrays.toString(tasksOrdered[i]));
		}
		
	}
}

/*

public Schedule recursive(ScheduledTask[] schedule, int startIndex)
	{	
		// this is the base case. if the start index is at the size of schedule, aka all the tasks have
		// been assigned, then the method returns the complete schedule formed.
		if (startIndex == schedule.length)
		{
			return new Schedule(tasks, m, schedule);
		}
		for (int i = 0; i < m; i++)
		{
			schedule[startIndex] = new ScheduledTask(startIndex, i);
			Schedule temp = recursive(schedule.clone(), startIndex + 1);
			if (temp.getMakespan() < best.getMakespan())
			{
				best = temp.clone();
			}
		}
		return best;
	}




//this is the base case. if the start index is at the size of schedule, aka all the tasks have
		// been assigned, then the method returns the complete schedule formed.
		if (startIndex == schedule.length)
		{
			return new Schedule(tasks, m, schedule);
		}
		ScheduledTask[] bestScheduledTasks = new ScheduledTask[schedule.length];
		for (int i = 0; i < m; i++)
		{
			schedule[startIndex] = new ScheduledTask(startIndex, i);
			Schedule temp = new Schedule(tasks, m, schedule);
			if (best == null || i == 0)
			{
				best = temp;
				bestScheduledTasks = schedule.clone();
			}
			if (temp.getMakespan() < best.getMakespan())
			{
				best = temp;
				System.out.println(best + "\n");
				bestScheduledTasks = schedule.clone();
			}
		}
		startIndex++;
		return recursive(bestScheduledTasks, startIndex, best);
*/