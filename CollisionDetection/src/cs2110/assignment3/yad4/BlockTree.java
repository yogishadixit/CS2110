package cs2110.assignment3.yad4;

import java.util.*;

/**
 *
 * A non-empty collection of points organized in a hierarchical binary tree structure.
 *
 */
public class BlockTree 
{
	/**
	 * The bounding box of the blocks contained in this tree. 
	 */
	private BBox box;

	/**
	 * Number of blocks contained in this tree.
	 */
	private int numBlocks;

	/**
	 * Left child (subtree):
	 * (left  == null) iff (this is a leaf node)
	 */
	private BlockTree left;

	/**
	 * Right child (subtree):
	 * (right == null) iff (this is a leaf node)
	 */
	private BlockTree right;

	/**
	 * Block (of a leaf node):
	 * (block == null) iff (this is an intermediate node)
	 */
	private Block block; 

	// REMARK:
	// Leaf node: left, right == null && block != null
	// Intermediate node: left, right != null && block == null

	/**
	 * Construct a binary tree containing blocks.
	 * The tree has no be non-empty, i.e., it must contain at least one block.
	 * 
	 * @param vertices
	 */
	public BlockTree(ArrayList<Block> blocks) 
	{	// Leave the following two "if" statements as they are.
		if (blocks == null)
			throw new IllegalArgumentException("blocks null");
		if (blocks.size() == 0)
			throw new IllegalArgumentException("no blocks");
		// creates a bounding box for this collection of blocks
		box = BBox.findBBox(blocks.iterator()); 
		numBlocks = blocks.size();
		// if this blocktree is a leaf, then it sets left and right to null and block to 
		// the leaf block
		if (numBlocks == 1)
		{
			block = blocks.get(0);
		}
		// if this blocktree is a leaf, then it sets block to null and finds values for
		// left and right
		else
		{
			Vec2D center = box.getCenter();
			ArrayList<Block> left = new ArrayList<Block>();
			ArrayList<Block> right = new ArrayList<Block>();
			// figures out what the longer dimension is 
			if (box.getWidth() >= box.getHeight())
			{
				// splits the bounding box in half horizontally (into a left and right)
				for (Block e : blocks)
				{
					if (e.p.x < center.x)
						left.add(e);
					else
						right.add(e);
				}
			}
			else
			{
				// splits the bounding box in half vertically (into a top and bottom)
				for (Block e : blocks)
				{
					if (e.p.y < center.y)
						left.add(e);
					else
						right.add(e);
				}
			}
			this.left = new BlockTree(left);
			this.right = new BlockTree(right);
		}
	}

	/**
	 * 
	 * @return The bounding box of this collection of blocks.
	 */
	public BBox getBox() { return box; }

	/**
	 * 
	 * @return True iff this is a leaf node.
	 */
	public boolean isLeaf() {
		return (block != null);
	}

	/**
	 * 
	 * @return True iff this is an intermediate node.
	 */
	public boolean isIntermediate() {
		return !isLeaf();
	}

	/**
	 * 
	 * @return Number of blocks contained in tree.
	 */
	public int getNumBlocks() {
		return numBlocks;
	}

	/**
	 * 
	 * @param p A point.
	 * @return True iff this collection of blocks contains the point p.
	 */
	public boolean contains(Vec2D p) 
	{
		// checks if the bounding box of this block tree contains this point
		if (box.contains(p))
		{
			// if this BlockTree is a leaf, then just return true
			if (isLeaf())
				return true;
			// if it is an intermediate, then recursively calls this method on 
			// its left and right subtrees
			else
			{
				if (left != null && right != null)
					return left.contains(p) || right.contains(p);
			}
				
		}
		return false;
	}

	/**
	 * 
	 * @param thisD Displacement of this tree.
	 * @param t A tree of blocks.
	 * @param d Displacement of tree t.
	 * @return True iff this tree and tree t overlap (account for displacements).
	 */
	public boolean overlaps(Vec2D thisD, BlockTree t, Vec2D d) 
	{
		BBox this_displaced = this.box.displaced(thisD);
		BBox other_displaced = t.box.displaced(d);
		// checks if the bounding boxes of this BlockTree overlap
		if (!this_displaced.overlaps(other_displaced))
			return false;
		// if they do, 
		// and if they are leaves, calls the overlaps method of Block
		else if (this.isLeaf() && t.isLeaf())
		{
			return Block.overlaps(this.block, thisD, t.block, d);
		}
		// and if either of them are intermediates, only breaks that object in two
		else if (this.isIntermediate() && t.isLeaf())
		{
			return (left.overlaps(thisD, t, d) || right.overlaps(thisD, t, d));
		}
		else if (this.isLeaf() && t.isIntermediate())
		{
			return (overlaps(thisD, t.left, d) || overlaps(thisD, t.right, d));
		}
		// and if both are intermediates, then breaks the larger shape in two
		else
		{
			if (this_displaced.getLength() < other_displaced.getLength())
				return (overlaps(thisD, t.left, d) || overlaps(thisD, t.right, d));
			else
				return (left.overlaps(thisD, t, d) || right.overlaps(thisD, t, d));
		}
	}


	public String toString() {
		return toString(new Vec2D(0,0));
	}

	/**
	 * 
	 * @param d Displacement vector.
	 * @return String representation of this tree (displaced by d).
	 */
	public String toString(Vec2D d) {
		return toStringAux(d,"");
	}

	/**
	 * Useful for creating appropriate indentation for the toString method.
	 */
	private static final String indentation = "   ";
	/**
	 * 
	 * @param d Displacement vector.
	 * @param indent Indentation.
	 * @return String representation of this tree (displaced by d).
	 */
	private String toStringAux(Vec2D d, String indent) 
	{
		String str = indent + "Box: ";
		str += "(" + (box.lower.x + d.x) + "," + (box.lower.y + d.y) + ")";
		str += " -- ";
		str += "(" + (box.upper.x + d.x) + "," + (box.upper.y + d.y) + ")";
		str += "\n";

		if (isLeaf()) {
			String vStr = "(" + (block.p.x + d.x) + "," + (block.p.y + d.y) + ")" + block.h; 
			str += indent + "Leaf: " + vStr + "\n";
		}
		else {
			String newIndent = indent + indentation;
			str += left.toStringAux(d,newIndent);
			str += right.toStringAux(d,newIndent);
		}

		return str;
	}

}

