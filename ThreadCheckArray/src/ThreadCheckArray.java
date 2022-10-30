import java.util.ArrayList;

public class ThreadCheckArray implements Runnable 
{
	private boolean flag;
	private boolean [] winArray;
	SharedData sd;
	ArrayList<Integer> array; //** Set up variables
	int b;
	/**
	 * @param sd
	 */
	public ThreadCheckArray(SharedData sd) 
	{
		this.sd = sd;	
		synchronized (sd) 
		{
			array = sd.getArray();
			b = sd.getB();
		}		
		winArray = new boolean[array.size()]; //** Build a new array type boolean size of the array list
	}
	
	void rec(int n, int b)
	{
		synchronized (sd) 
		{
			if (sd.getFlag())
				return;
		}	
		if (n == 1)
		{
			if(b == 0 || b == array.get(n-1)) // ** Checks if the integer b can be calculated 
				//** by the variables in the array list return true if it can be done else false
			{
				flag = true;
				synchronized (sd) 
				{
					sd.setFlag(true);
				}			
			}
			if (b == array.get(n-1)) // ** If b equals to the last elemnt in the array list
				winArray[n-1] = true;//** the boolean array in the n-1 cell turn to true
			return;
		}
		
		rec(n-1, b - array.get(n-1)); // ** Equation to check if b can be calculated by the elements 
		if (flag)
			winArray[n-1] = true;
		synchronized (sd) 
		{
			if (sd.getFlag())
				return;
		}	
		rec(n-1, b);
	}

	public void run() {
		if (array.size() != 1)
			if (Thread.currentThread().getName().equals("thread1"))
				rec(array.size()-1, b - array.get(array.size()-1)); // **Equation to check if b can be calculated by the elements
			else 
				rec(array.size()-1, b); // ** Thread2 takes the action here 
		if (array.size() == 1)  // ** In case the array list has only one element
			if (b == array.get(0) && !flag) // ** If b equals this element change the boolean array in cell 0 to true
			{
				winArray[0] = true;
				flag = true;
				synchronized (sd) 
				{
					sd.setFlag(true);
				}
			}
		if (flag)
		{
			if (Thread.currentThread().getName().equals("thread1"))
				winArray[array.size() - 1] = true; // ** checks if thread 1 is doing the action and return true
			synchronized (sd) 
			{
				sd.setWinArray(winArray);
			}	
		}
	}
}
