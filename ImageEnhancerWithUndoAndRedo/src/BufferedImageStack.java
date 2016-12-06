/**
 * BufferedImageStack.java
 * CSE 373 Assignment 1.
 * Section BB.
 * 
 * Class BufferedImageStack can be used to store a stack of 
 * BufferedImage objects
 * 
 * @author  Xiaowen Feng
 * @since  Oct. 6, 2016
 */

import java.util.*;
import java.awt.image.BufferedImage;

public class BufferedImageStack {
	private BufferedImage[] imageArray; // Where the BufferedImage objects are stored
	private int top; // The top element of the stack
	private int stackSize; // Number of elements in the stack
	private int arraySize; // Current capacity of the array
	private static int initalArraySize = 2; // Starting capacity 
	
	/**
	 *  Class constructor
	 */
	public BufferedImageStack() {
		imageArray = new BufferedImage[initalArraySize];
		stackSize = 0;
		top = -1;
		arraySize = initalArraySize;
	}
	
	/**
	* Inserts a BufferedImage object into the stack
	*
	* @param someBufferedImage  the given BufferedImage object for inserting
	*/
	public void push(BufferedImage someBufferedImage) {
		if (stackSize == arraySize) {
			doubleArraySize();
		}
		top += 1;
		imageArray[top] = someBufferedImage;
		stackSize += 1;
	}
	
	/**
	 *  A helper method for creating a new array with double of the 
	 *  old array size, and copy the old array's elements to the new array 
	 */
	private void doubleArraySize() {
		BufferedImage[] temp = new BufferedImage[2 * arraySize];
		
		for (int i = 0; i < arraySize; i+=1) {
			temp[i] = imageArray[i];
		}
		
		imageArray = temp;
		arraySize = 2 * arraySize;
	}
	
	/**
	 * Pops the BufferedImage object at the top of the stack
	 * 
	 * @return   the last BufferedImage in the stack
	 * @throws   EmptyStackException when stack is empty
	 */
	public BufferedImage pop() {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		BufferedImage last = imageArray[top];
		stackSize -= 1;
		top -= 1;
		return last;
	}
	
	/**
	 * Checks whether or not the stack is empty
	 * 
	 * @return  <code>true</code> if the stack is empty
	 * 			<code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return stackSize == 0;
	}
	
	/**
	 * Gets the BufferedImage object at the given index
	 * 
	 * @param  index  position of the BufferedImage object
	 * @return the BufferedImage object at the given index
	 * @throws IndexOutOfBoundsException if the given index is 
	 * 		   out of the range
	 */
	public BufferedImage get(int index) {
		if (index < 0 || index >= stackSize) {
			throw new IndexOutOfBoundsException();
		}
		return imageArray[index];
	}
	
	/**
	 * Gets the size of the stack
	 * @return  the size of the stack
	 */
	public int getSize() {
		return stackSize;
	}
	
	/**
	 * Gets the size the current array
	 * @return  the size of the array
	 */
	public int getArraySize() {
		return arraySize;
	}
}


