
/**
 * CSUN COMP 282-04 & COMP 282-05 Fall 2019
 * Generic Java program implements replacement
 * selection sort given a big data file of integers,
 * where there number of inputs is unknown, but
 * the first data of the file is memory size(M).
 * 
 * Name: Philip D. Kim, 	ID: 108508736
 */
package Java.Project2;
import java.util.*;
import java.lang.reflect.Array;

public class Kim<T extends Comparable<T>> {
	protected T Heap[]; // min heap
	protected int size; // Heap size
	protected long start; // execution start time
	protected long stop; // execution stop time
	protected long count; // execution counter

	/**
	 * default constructor
	 * 
	 * @param c: integer class
	 * @param m: heap size
	 */
	@SuppressWarnings("unchecked")
	Kim(Class<T> C, int M) {
		start = 0;
		count = 0;
		stop = 0;
		size = M;
		Heap = (T[]) Array.newInstance(C, size + 1);
	} // end constructor

	/**
	 * min heap down
	 */
	protected void minheapdn() {
		for (int j = size / 2; j > 0; j--)
			heapdn(j);
	}// end minheapdn

	/**
	 * heap down method from position m to end of min heap
	 * 
	 * @param m: position
	 */
	protected void heapdn(int m) {
		T tmp = Heap[m];
		while (m * 2 <= size) {
			int minIndex = 2 * m;
			if ((minIndex + 1) <= size) {
				if (Heap[minIndex + 1].compareTo(Heap[minIndex]) > 0)
					minIndex++;
			} // end outter if
			if (Heap[minIndex].compareTo(tmp) < 0) {
				Heap[m] = Heap[minIndex];
				m = minIndex;
			} // end if
			else
				break;
		} // end while
		Heap[m] = tmp;
	} // end heapdn

	/**
	 * get min root
	 * 
	 * @return retunrs smallest int
	 */
	protected T getMin() {
		for (int i = 1; i <= size; i++)
			if (Heap[1].compareTo(Heap[i]) > 0)
				swapRoot(i);
		return size <= 0 ? null : getRoot();
	} // end getMin

	/**
	 * swap root with index i
	 * 
	 * @param i index
	 */
	protected void swapRoot(int i) {
		T min = getRoot();
		Heap[1] = Heap[i];
		Heap[i] = min;
	} // end swapRoot

	/**
	 * get root
	 * 
	 * @return root
	 */
	protected T getRoot() {
		return Heap[1];
	} // end getRoot

	/**
	 * print sorted number of files
	 * 
	 * @param file getMin()
	 */
	public static void prti(Integer file) {
		if (file.compareTo(Integer.MAX_VALUE) < 0)
			System.out.printf("%6d, ", file);
	}

	/**
	 * replacementselectionsort, external sorting algorithm when files are too big.
	 * Split into 'n' smaller files that can fit in memory of size M in 2 phases.
	 * 
	 * @param infile input file
	 * @param M      min heap memory size
	 */
	protected void replacementselectionsort(Scanner infile, int M) {
		int j; // j no. of data
		int i; // output file no
		int c; // c no. of data
		int t; // t no. of total data
		int inheap; // heaps not in process
		int lastwritten; // write negative infinity
		Kim<Integer> h = new Kim<Integer>(Integer.class, M);
		// create intial minheap from infile
		for (j = 1; j <= M && infile.hasNextInt(); j++)
			h.Heap[j] = infile.nextInt();
		h.size = --j; // no. of elements in heap
		i = 1; // output file no.
		c = 0; // data no.
		t = 0; // total data no.
		System.out.printf("__________________________________________________________\n"
				+ "| Content of sorted files created using replacement      |\n"
				+ "| selection sort with Memory size M = \'%d\' are as follow: |\n"
				+ "|________________________________________________________|\n", M);
		while (h.size > 0) { // while more data exists
			h.minheapdn(); // conver to minheap
			lastwritten = Integer.MIN_VALUE; // negative infinity
			inheap = 0; // unprocessed elements
			System.out.printf("\n  F%02d: [", i);
			while (h.size > 0) { // write and read until eof
				if (h.getRoot() < lastwritten) {
					h.swapRoot(h.size);
					h.size--; // reduce heap size
					inheap++; // increase unprocessed elements
				} else {
					t++;
					c++;
					prti(h.getMin()); // prints sorted files
					lastwritten = h.getRoot(); // writes root
					if (!infile.hasNextInt()) {
						// shift elements left in minheap
						for (j = 1; j < h.size; j++)
							h.Heap[j] = h.Heap[j + 1];
						h.size--; // reduce heap size
					} else {
						// read next input from infile
						h.Heap[1] = infile.nextInt();
					} // inner else
				} // outter else
				h.heapdn(1); // heapdn from root
			} // inner while
			System.out.printf("\b\b%6s%3s %d data )", "]", "(", c);
			prti(Integer.MAX_VALUE);
			if (inheap == 0)
				break; // all data are processed
			h.size = inheap;
			i++; // start another file
			c -= c; // substract from it self for data output
		} // outter while
		System.out.printf("\n___________________________________________________________\n"
				+ "| Replacement selection sort execution time for creating  |\n"
				+ "| \'%6d\' sorted files from \'%6d\' data:%16s", i, t, "|");
		infile.close();
	} // end replacementselectionsort

	// ***************************************************************
	public static void main(String args[]) throws Exception {
		System.out.println("\nName: Philip D. Kim\t\tID: 108508736");
		Scanner inf = new Scanner(System.in);
		int M = inf.nextInt();
		Kim<Integer> h = new Kim<Integer>(Integer.class, M);
		h.start = System.currentTimeMillis();
		h.replacementselectionsort(inf, M);
		h.stop = System.currentTimeMillis();
		h.count = (h.stop - h.start);
		System.out.printf("\n|%35s ", " ");
		System.out.printf("\"( %8d msecs )\"%2s", h.count, " |");
		System.out.printf("\n|_________________________________________________________|\n");
	} // end main
} // end class Kim
