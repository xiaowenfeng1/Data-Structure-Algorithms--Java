/**
 * @author Peter Freschi and Xiaowen Feng
 * CSE 373
 */
public class FeatureVector {

	/**
	 * FeatureVector is a class for storing the results of
	 * counting the occurrences of colors.
	 * <p>
	 * Unlike the hash table, where the information can be
	 * almost anyplace with the array(s) (due to hashing), in the FeatureVector,
	 * the colors are in their numerical order and each count
	 * is in the array position for its color.
	 * <p>
	 * Besides storing the information, the class provides methods
	 * for getting the information (getTheCounts) and for computing
	 * the similarity between two vectors (cosineSimilarity).
	 */
	long[] colorCounts;
	int bitsPerPixel;
	int keySpaceSize;



	/**
	 * Constructor of a FeatureVector.
	 * 
	 * This creates a FeatureVector instance with an array of the
	 * proper size to hold a count for every possible element in the key space.
	 * 
	 * @param bpp	Bits per pixel. This controls the size of the vector.
	 * 				The keySpace Size is 2^k where k is bpp.
	 * 
	 */
	public FeatureVector(int bpp) {
		keySpaceSize = 1 << bpp; // 2^k
		colorCounts = new long[keySpaceSize];
		bitsPerPixel = bpp;
	}


	// Accepts a ColorHashIt to go through all possible key values in order,
	// get the count from the hash table and put it into this feature vector.
	public void getTheCounts(ColorHash ch) {
		for (int i = 0; i < keySpaceSize; i++) {
			try {
				ColorKey newKey = new ColorKey(i, bitsPerPixel);
				long counts = ch.getCount(newKey);
				colorCounts[i] = counts;
			} catch (Exception e) {
				System.out.println("Unsupported number of bits per pixel; use a multiple of 3 between 3 and 24.");
			}		
		}
		
	}
	// Calculates the cosine similarity of this FeatureVector and another that is passed in and returns it. 
	public double cosineSimilarity(FeatureVector other) {
		double similarity = dotProd(other) / (this.magnitude() * other.magnitude());
		return similarity; // Change this to return the actual value.
	}
	
	// A private helper that calculates the dot product of this FeatureVector and another that is passed in, and returns it.
	private double dotProd(FeatureVector other) {
		long sum = 0;
		for(int i = 0; i < colorCounts.length; i++){
			sum += (double) this.colorCounts[i] *  (double) other.colorCounts[i];
		}
		return sum;
	}
	
	// this helper calculates the magnitude the current vector
   private double magnitude() {
        return Math.sqrt(this.dotProd(this));
   }


	/**
	 * Optional main method for your own tests of these methods.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}
