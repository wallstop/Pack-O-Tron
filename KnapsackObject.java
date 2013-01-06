/*	Rectangles.
 * 
 */

public class KnapsackObject {
	
	private int width;
	private int length;
	private int area;
	
	public KnapsackObject(int width, int length)
	{
		this.width = width;
		this.length = length;
		updateArea();
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getLength()
	{
		return length;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
		updateArea();
	}
	
	public void setLength(int length)
	{
		this.length = length;
		updateArea();
	}
	
	private void updateArea()
	{
		area = width * length;
	}
	
	public int getArea()
	{
		return area;
	}
	
	public void swapSides()
	{
		int temp;
		
		temp = width;
		width = length;
		length = temp;
	}
	
	//Initially wanted to implement Comparable. Not needed.
	/*
	public int compareTo(Object comparator)
	{
		final int smaller = -1;
		final int equal = 0;
		final int larger = 1;
		
		if(this == comparator)
			return equal;
		
		else if(this.getArea() < ((KnapsackObject) comparator).getArea())
			return smaller;
		else
			return larger;		
	}
	*/


}
