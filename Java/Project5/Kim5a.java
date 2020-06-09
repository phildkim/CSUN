/**
 * CSUN COMP 282-04 & COMP 282-05 Fall 2019
 * Generic Java program implements Huffman 
 * code to decode.
 * 
 * Name: Philip D. Kim,     ID: 108508736
 */
package Java.Project5;
import java.io.*;
public class Kim5a implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final int ASCII = 128;
    private String HUFFCODE = "";
    private String HUFFDECODE = "";
    private String TEXT = "";
    private int[] FREQS;
    private int TOTAL;
    Kim5a() {}
    Kim5a(String h) {
        HUFFDECODE = h;
    }
    public static class Forest implements Serializable, 
    Comparable<Forest> {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        Node root;
        /**
		 * create constructor to build two subtrees
		 * @param lft	left subtree
		 * @param rgt	right subtree
		 */
        Forest(Forest lft, Forest rgt) {
            root = new Node();
            root.lf = lft.root;
            root.rt = rgt.root;
            root.fq = lft.root.fq + rgt.root.fq;
        }// end forest constructor
        /**
		 * create tree with a leaf node
		 * @param f     frequencies
		 * @param c     characters
		 */
        Forest(int freq, char ascii) {
            root = new Node(freq, ascii);
        }// end forest constructor
        @Override
        public int compareTo(Forest f) {
            // compare trees based frequency
            if (root.fq < f.root.fq) return 1;
            else if (root.fq == f.root.fq) return 0;
            else return -1;
        }// end compareTo
        public static class Node implements Serializable {
            /**
             *
             */
            private static final long serialVersionUID = 1L;
            char ch; // ascii characters
            int fq;                 // frequency for forest
            String bin = "";        // huffman binary code
            Node lf, rt;            // left & right subtree
            Node() {}               // initialize empty node
            Node(int freq, char ascii) {
                fq = freq;
                ch = ascii;
                lf = rt = null;
            }// end Node constructor
        }// end Node class
    }// end forest class
    public static class Heap <E extends Comparable<E>> implements Serializable {
		/**
         *
         */
        private static final long serialVersionUID = 1L;
        private java.util.ArrayList<E> Heap =
		 new java.util.ArrayList<E>();
        Heap() {}
		Heap(E[] e) {
			for (int i = 0; i < e.length; i++)
				add(e[i]);
        }//end Heap constructor
        /**
		 * Insert new object e into heap
		 * @param e
		 */
		public void add(E e) {
			Heap.add(e);
			int i = Heap.size() - 1; // The index of the last node
			while (i > 0) {
			 	int j = (i - 1) / 2;
			 	// Swap if the current object is greater than its parent
			 	if (Heap.get(i).compareTo(Heap.get(j)) > 0) {
			  		E tmp = Heap.get(i);
					  Heap.set(i, Heap.get(j));
					  Heap.set(j,tmp);
			 	} else break; // the tree is a heap now
			 	i = j;
			}//end while
		}//end add
		/**
		 * gets the root from forest
		 * @return root
		 */
		public E getroot() {
			if (Heap.size() == 0) return null;
			E root = Heap.get(0);
			Heap.set(0, Heap.get(Heap.size() - 1));
			Heap.remove(Heap.size() - 1);
			int i = 0;
			while (i < Heap.size()) {
				int lt = 2 * i + 1;
				int rt = 2 * i + 2;
				// Find the maximum between two children
				if (lt >= Heap.size()) break; // The tree is a heap
				int max = lt;
				if (rt < Heap.size())
					if (Heap.get(max).compareTo(Heap.get(rt)) < 0)
						max = rt;
				// Swap if the current node is less than the maximum
				if (Heap.get(i).compareTo(Heap.get(max)) < 0) {
					E tmp = Heap.get(max);
					Heap.set(max, Heap.get(i));
					Heap.set(i, tmp);
					i = max;
				} else break; // The tree is a heap
			}//end while
			return root;
		}//end huffman
    }// end Heap class
    /**
	 * Computes the frequency of each characters.
     * And the number to create a forest tree.
	 * @param freqs
	 * @return No. of freq
	 */
    private int countfreq(int[] freqs) {
        int i, cnt = 0;
        for (i = 0; i < TEXT.length(); i++)
            freqs[(int) TEXT.charAt(i)]++;
        for (i = 0; i < ASCII; i++)
            if (freqs[i] > 0) cnt++;
        return cnt;
    }// end countfreq
    /**
	 * Builds the huffman tree using freqs length 
	 * for the size of tree. freqs will be added
	 * and also remove the lowest freqs.
	 * @param freqs
	 * @return huffman tree
	 */
    private Forest buildforest(int[] freqs, int n) {
		Heap<Forest> forest = new Heap<Forest>();
		for (int i = 0; i < ASCII; i++) 
			if (freqs[i] > 0)
                forest.add(new Forest(freqs[i], (char)i));
		while (forest.Heap.size() > 1) {
			Forest f1 = forest.getroot();
			Forest f2 = forest.getroot();
			forest.add(new Forest(f1, f2));
		}//end while
		return forest.getroot();
    }//end buildforest
    private String[] huffman() {
        String[] c = coding();
        return c;
    }//end huffman
    /**
	 * Gets the huffman code by calling genCode 
     * and return as String
	 * @param bf
	 * @return huffman binary code
	 */
    private String[] coding() {
        String c[] = new String[ASCII];
        Forest f = buildforest(FREQS, countfreq(FREQS));
        encode(f.root, c);
        return c;
    }// end codin
    /**
	 * Recursive method to add huffman binary code to forest
	 * @param f
	 * @param c
	 */
    private void encode(Forest.Node t, String[] c) {
        if (t.lf != null) {
            t.lf.bin = t.bin + "0";
            encode(t.lf, c);
            t.rt.bin = t.bin + "1";
            encode(t.rt, c);
        } else c[(int) t.ch] = t.bin;
    }// end genCode
    /**
     * Decoder to decode binary back to ascii
     * @param f
     * @param binary
     * @return decoded word/phrases/numbers etc..
     */
	private String decode(Forest f, String binary) {
        String ascii = "";
		for (int i = 0; i < binary.length(); i++) {
			Forest.Node tmp = f.root;
			while (tmp != null) {
				char s = binary.charAt(i);
				i++;
				if (s == '1') {
					tmp = tmp.rt;
					if (tmp.ch != '\0') {
						ascii += tmp.ch;
						break;
					}//end if
				} else {
					tmp = tmp.lf;
					if (tmp.ch != '\0') {
						ascii += tmp.ch;
						break;
					}//end if
				}//end if else
			}//end while
        }//end for
        return ascii;
    }//end decoder
    /**
	 * print huffman code table with ASCII, Chars, Freq, Code.
     * print calculations of total input characters, sum of bits,
     * number of bytes, and average length of codes.
	 */
    private void printoutput() {
        int bitsum = 0;
        Forest f = buildforest(FREQS, countfreq(FREQS));
        System.out.printf("\n%s %6s %6s %9s %13s\n" + "%s %6s %6s %12s %10s\n", "ASCII", "Char", "Freq", "Code",
                "Decode", "=====", "=====", "=====", "=======", "======");
        for (int i = 0; i < ASCII; i++) {
            if (FREQS[i] != 0) {
                HUFFDECODE += decode(f, huffman()[i]);
                HUFFCODE += huffman()[i];
                bitsum += FREQS[i] * huffman()[i].length();
                System.out.printf(
                "%3d %6s %8d %4s %-6s %5s %-6s\n",
                i,(char)i+"",
                FREQS[i],"",
                huffman()[i],"",
                decode(f,huffman()[i]));
            }//end if
        }//end for
        System.out.printf(
        "===========================================\n" + 
        "No. of input chars: %d\n" + 
        "Sum of bits: %d # Bytes = %.1f\n" + 
        "Avg. length of codes: %.6f\n\n",
        TOTAL, 
        bitsum, bitsum / 8.0, 
        bitsum / (double) TOTAL);
    }// end printoutput
    public static void prt(String s) {
        System.out.printf("\n%s\n",s);
    }//end prt
   
    public static void main(String[] args) {
        prt("\nPrepared by Kim, Philip\t108508736");
        Kim5a h = new Kim5a();
        try {
            BufferedReader br = 
            new BufferedReader(
                new InputStreamReader(System.in));
            String read = "";
            while ((read = br.readLine()) != null)
                h.TEXT += read;
            h.TOTAL = h.TEXT.length();
            br.close();
            h.FREQS = new int[ASCII];   // 128 ASCII characters
            int n = h.countfreq(h.FREQS);     // compute frequency 
            h.buildforest(h.FREQS, n);        // build forest
            h.huffman();                   // build huffman tree
            h.coding();                     // for each char compute code
            h.printoutput();                // print output
            
            // serialize to file
		    ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("Kim5.obj"));
            out.writeObject(h);
            out.close();
            
            h = null;
            ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("Kim5.obj"));
            h = (Kim5a) in.readObject();
            in.close();	
            prt("ENCODED: " + h.HUFFCODE);
            prt("DECODED: " + h.HUFFDECODE );
        } catch (Exception e) {
            prt("Exception: " + e + "\nTrace: ");
            e.printStackTrace();
            prt("\n");
        }//end try catch
    }//end main
}//end Kim5 classs