/**
 * @author Peter Freschi and Xiaowen Feng
 * Section BF & BB
 * by Peter Freschi and Xiaowen Feng for CSE 373 Assignment 4, Autumn, 2016.
 * ComparingPaintings is an application which processes an image's colors by pixels, 
 * and compares two images similarity.
 */


public class ComparePaintings {
	public String collisionResolution = "";
	public int bpp = 3;
	public int collisions;
	public String[] paintings;
	
	// constructs a ComparePainting object
	public ComparePaintings() {
		collisions = 0;
		paintings = new String[] {"MonaLisa.jpg", "StarryNight.jpg", "ChristinasWorld.jpg"};
	}; 

	// Load the given image using the imageLoader, construct a colorHash 
	// table with the given bits per pixel, count the colors for the given image.
	ColorHash countColors(String filename, int bitsPerPixel) {
		bpp = bitsPerPixel;

		ImageLoader img = new ImageLoader(filename);
		int x = img.getWidth();
		int y = img.getHeight();
		ColorHash hashTable = new ColorHash(3, bitsPerPixel, collisionResolution, 0.5);
		for (int i = 0; i < x; i++) {
	        for (int j = 0; j < y; j++) { 
	    		ColorKey key = img.getColorKey(i, j, bitsPerPixel);
	    		ResponseItem result = hashTable.increment(key);
	    		
	    		collisions += result.nCollisions;
	        }
	    }
		return hashTable; 
	}
	
	// Starting with two hash tables of color counts, compute a measure of similarity based on the 
	// cosine distance of two vectors.
	double compare(ColorHash painting1, ColorHash painting2) {
		
		FeatureVector p1 = new FeatureVector(bpp);
		p1.getTheCounts(painting1);
		FeatureVector p2 = new FeatureVector(bpp);
		p2.getTheCounts(painting2);
		
		double similarity = p1.cosineSimilarity(p2);
		return similarity;
	}
	
	// a basic test that compares the given image with itself
	// so the similarity should be 1.0
	void basicTest(String filename) {
		
		ColorHash pic = countColors(filename, 24);
		System.out.println("Compare Mona and Mona" + compare(pic, pic));
		
	}
	
	// Using the three given painting images and a variety of bits-per-pixel values, 
	// compute and print out a table of collision counts in a table format
	void CollisionTests() {
		final Object[][] table = new String[9][7];
		table[0] = new String[] {"Bits Per Pixel", "C(Mona,linear)", "C(Mona,quadratic)", "C(Starry,linear)", 
								"C(Starry,quadratic)", "C(Christina,linear)", "C(Christina,quadratic)"};
		int currentBpp = 24;
		for (int i = 1; i < 9; i++){
			table[i][0] = Integer.toString(currentBpp);
			for (int j = 1; j < 4; j++){
					collisionResolution = "Linear Probing";
					countColors(paintings[j - 1], currentBpp);
					table[i][j * 2 - 1] = Integer.toString(collisions);
					collisions = 0;
					collisionResolution = "Quadratic Probing";
					countColors(paintings[j - 1], currentBpp);
					table[i][j * 2] = Integer.toString(collisions);
					collisions = 0;
			}
			currentBpp = currentBpp - 3;
		}

		for (final Object[] row : table) {
		    System.out.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s\n", row);
		}
	}
		
	// This simply checks that the images can be loaded, so you don't have 
	// an issue with missing files or bad paths.
	void imageLoadingTest() {
		ImageLoader mona = new ImageLoader("MonaLisa.jpg");
		ImageLoader starry = new ImageLoader("StarryNight.jpg");
		ImageLoader christina = new ImageLoader("ChristinasWorld.jpg");
		System.out.println("It looks like we have successfully loaded all three test images.");
	}
	
	// Using the three given painting images and a variety of bits-per-pixel values, 
	// compare each pair's similarity, then print each similarity in a table format
	void fullSimilarityTests() {
		final Object[][] table = new String[9][4];
		table[0] = new String[] {"Bits Per Pixel", "C(Mona,Starry)", "C(Mona,Christina)", "C(Starry,Christina)"};
		int currentBpp = 24;
		for (int i = 1; i < 9; i++){
			table[i][0] = Integer.toString(currentBpp);
			ColorHash[] pics = new ColorHash[3];
			for (int j = 0; j < pics.length; j++){
				pics[j] = countColors(paintings[j], currentBpp);
			}
			table[i][1] = Double.toString(round5Dec(compare(pics[0], pics[1])));
			table[i][2] = Double.toString(round5Dec(compare(pics[0], pics[2])));
			table[i][3] = Double.toString(round5Dec(compare(pics[1], pics[2])));

			currentBpp = currentBpp - 3;
		}
		
		for (final Object[] row : table) {
		    System.out.format("%-20s%-20s%-20s%-20s\n", row);
		}
	}
	
	// a helper method that rounds a given double to 5 decimal
	private double round5Dec(double n) {
		return (double)Math.round(n * 100000d) / 100000d;
	}
	
	/**
	 * This is a basic testing function, and can be changed.
	 */
	public static void main(String[] args) {
		ComparePaintings cp = new ComparePaintings();
		cp.imageLoadingTest();
		cp.basicTest("MonaLisa.jpg");
		cp.CollisionTests();
		cp.fullSimilarityTests();
	}
}
