package cs2110.assignment3.yad4;

import java.util.*;

/**
 * A 2D bounding box.
 *
 */
public class BBox 
{
	/**
	 * The corner of the bounding box with the smaller x,y coordinates.
	 */
	public Vec2D lower; // (minX,minY)
	
	/**
	 * The corner of the bounding box with the larger x,y coordinates.
	 */
	public Vec2D upper; // (maxX,maxY)

	/**
	 * 
	 * @param box A bounding box.
	 */
	public BBox(BBox box) 
	{
		lower = new Vec2D(box.lower);
		upper = new Vec2D(box.upper);
	}

	/**
	 * 
	 * @param lower Corner with smaller coordinates.
	 * @param upper Corner with larger coordinates.
	 */
	public BBox(Vec2D lower, Vec2D upper) 
	{
		if (upper.x < lower.x) throw new IllegalArgumentException("invalid bbox");
		if (upper.y < lower.y) throw new IllegalArgumentException("invalid bbox");

		this.lower = lower;
		this.upper = upper;
	}

	/**
	 * Width: size along the x-dimension.
	 * 
	 * @return Width of the bounding box.
	 */
	public double getWidth() 
	{
		return upper.x - lower.x;
	}

	/**
	 * Height: size along the y-dimension.
	 * 
	 * @return Height of the bounding box.
	 */
	public double getHeight() 
	{
		return upper.y - lower.y;
	}

	/**
	 * 
	 * @return Returns the dimension (width or height) of maximum length.
	 */
	public double getLength() 
	{
		return Math.max(getWidth(), getHeight());
	}

	/**
	 * 
	 * @return The center of this bounding box.
	 */
	public Vec2D getCenter() 
	{
		//double center_x = (getWidth() / 2) + lower.x;
		//double center_y = (getHeight() / 2) + lower.y;
		double center_x = (lower.x + upper.x)/2;
		double center_y = (lower.y + upper.y)/2;
		return new Vec2D(center_x, center_y);
	}

	/**
	 * 
	 * @param d A displacement vector.
	 * @return The result of displacing this bounding box by vector d.
	 */
	public BBox displaced(Vec2D d) 
	{
		return new BBox(lower.add(d), upper.add(d));
	}

	/**
	 * 
	 * @param p A point.
	 * @return True iff this bounding box contains point p.
	 */
	public boolean contains(Vec2D p) 
	{
		boolean inX = lower.x <= p.x && p.x <= upper.x;
		boolean inY = lower.y <= p.y && p.y <= upper.y;
		return inX && inY;
	}

	/**
	 * 
	 * @return The area of this bounding box.
	 */
	public double getArea() 
	{
		return getWidth()*getHeight();
	}


	/**
	 * 
	 * @param box A bounding box.
	 * @return True iff this bounding box overlaps with box.
	 */
	public boolean overlaps(BBox box) 
	{
		// gets the centers of this bounding box and the other bounding box
		Vec2D this_center = this.getCenter();
		Vec2D other_center = box.getCenter();
		
		// this calculates the tolerance for collision, i.e. how close the images have to 
		// get for it to register as a collision
		double this_height = getHeight();
		double this_width = getWidth();
		double temp = Math.pow(this_height, 2) + Math.pow(this_width, 2);
		double other_height = box.getHeight();
		double other_width = box.getWidth();
		double other_temp = Math.pow(other_height, 2) + Math.pow(other_width, 2);
		
		// this determines if the images collide
		return Vec2D.dist(this_center, other_center) < Math.sqrt(temp/2 + other_temp/2);
	}

	/**
	 * 
	 * @param iter An iterator of blocks.
	 * @return The bounding box of the blocks given by the iterator.
	 */
	public static BBox findBBox(Iterator<Block> iter)
	{
		// Do not modify the following "if" statement.
		if (!iter.hasNext())
			throw new IllegalArgumentException("empty iterator");

		// gives the extremes a starting value
		Block temp = iter.next();
		BBox temp_BBox = temp.getBBox();
		double min_x = temp_BBox.lower.x;
		double min_y = temp_BBox.lower.y;
		double max_x = temp_BBox.upper.x;
		double max_y = temp_BBox.upper.y;
		Vec2D temp_lower, temp_upper;
		
		// for each block in the iterator, it checks to see if any of its's coordinates 
		// are smaller than the minimum or larger than the maximum. if they are, then it 
		// replaces those coordinates with that of the block
		while (iter.hasNext())
		{
			temp = iter.next();
			temp_lower = temp.getBBox().lower;
			temp_upper = temp.getBBox().upper;
			if (temp_lower.x < min_x)
				min_x = temp_lower.x;
			if (temp_lower.y < min_y)
				min_y = temp_lower.y;
			if (temp_upper.x > max_x)
				max_x = temp_upper.x;
			if (temp_upper.y > max_y)
				max_y = temp_upper.y;
		}
		return new BBox(new Vec2D(min_x, min_y), new Vec2D(max_x, max_y));
	}

	public String toString() {
		return lower + " -- " + upper;
	}
}
