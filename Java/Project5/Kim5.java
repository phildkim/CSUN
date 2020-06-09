/**
 * CSUN COMP 282-04 & COMP 282-05 Fall 2019
 * Generic Java program implements Huffman 
 * code without Java Utilities for file
 * input/output. Instead, serialization
 * is used to read/write files and save
 * the contents to a class variable.
 * 
 * Name: Philip D. Kim,     ID: 108508736
 */
package Java.Project5;
import java.io.*;
import java.lang.reflect.Array;
public class Kim5 implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final int ASCII = 128;
    private String TEXT = "";
    private int[] FREQS;
    private int TOTAL;

    Kim5() {
    }

    Kim5(final String[] h) {

    }

    public class Forest implements Serializable, Comparable<Forest> {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        Node root;
        Node[] forest;

        public Forest(final Forest lft, final Forest rgt) {
            root = new Node();
            root.lf = lft.root;
            root.rt = rgt.root;
            root.fq = lft.root.fq + rgt.root.fq;
        }// end forest constructor

        public Forest(final int freq, final char ch) {
            root = new Node(freq, ch);
        }// end forest constructor

        @Override
        public int compareTo(final Forest f) {
            // compare trees based frequency
            if (root.fq < f.root.fq)
                return 1;
            else if (root.fq == f.root.fq)
                return 0;
            else
                return -1;
        }// end compareTo

        class Node implements Serializable {
            /**
             *
             */
            private static final long serialVersionUID = 1L;
            char ch; // ascii characters
            int fq; // frequency for forest
            String bin = ""; // huffman binary code
            Node lf, rt; // left & right subtree

            Node() {
            } // initialize empty node

            Node(final int freq, final char ascii) {
                fq = freq;
                ch = ascii;
                lf = rt = null;
            }// end Node constructor
        }// end Node class
    }// end forest

    public class Heap<E extends Comparable<E>> implements Serializable {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private final java.util.ArrayList<E> list = new java.util.ArrayList<E>();
        private int size; // Heap Capacity
        private E Heap[];

        Heap() {
        }

        @SuppressWarnings("unchecked")
        Heap(final Class<E> e, final int m) {
            size = m;
            Heap = (E[]) Array.newInstance(e, size + 1);
        }// end constructor

        public int lsize() {
            return list.size();
        }

        public int hsize() {
            return size;
        }

        /**
         * Insert new object e into heap
         * 
         * @param e
         */
        public void insert(final E e) {
            list.add(e);
            int i = list.size() - 1;
            while (i > 0) {
                final int j = (i - 1) / 2;
                if (list.get(i).compareTo(list.get(j)) > 0) {
                    final E tmp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, tmp);
                } else
                    break;
                i = j;
            } // end while
        }// end insert

        /**
         * heapdn and gets the root from forest
         * 
         * @return root
         */
        public E heapdn() {
            if (list.size() == 0)
                return null;
            final E root = list.get(0);
            list.set(0, list.get(list.size() - 1));
            list.remove(list.size() - 1);
            int i = 0;
            while (i < list.size()) {
                final int lt = 2 * i + 1;
                final int rt = 2 * i + 2;
                if (lt >= list.size())
                    break;
                int max = lt;
                if (rt < list.size())
                    if (list.get(max).compareTo(list.get(rt)) < 0)
                        max = rt;
                if (list.get(i).compareTo(list.get(max)) < 0) {
                    final E tmp = list.get(max);
                    list.set(max, list.get(i));
                    list.set(i, tmp);
                    i = max;
                } else
                    break;
            } // end heapdn
            return root;
        }// end minheapdn
    }// end Heap class

    /**
     * Gets the huffman code by calling genCode and return as String
     * 
     * @param bf
     * @return huffman binary code
     */
    private int countfreq(final int[] freqs) {
        int i, cnt = 0;
        for (i = 0; i < TEXT.length(); i++)
            freqs[(int) TEXT.charAt(i)]++;
        for (i = 0; i < ASCII; i++)
            if (freqs[i] > 0)
                cnt++;
        return cnt;
    }// end countfreq

    /**
     * Builds the huffman tree using freqs length for the size of tree. freqs will
     * be added and also remove the lowest freqs.
     * 
     * @param freqs
     * @return huffman tree
     */
    private Forest buildforest(final int[] freqs, final int n) {
        final Heap<Forest> heap = new Heap<Forest>(Forest.class, n);
        final Heap<Forest> forest = new Heap<Forest>();
        int i, k = 0;
        for (i = 0; i < ASCII; i++) {
            if (freqs[i] > 0) {
                final Forest tmp = new Forest(freqs[i], (char) i);
                heap.Heap[++k] = tmp;
                forest.insert(heap.Heap[k]);
            } // end if
        } // end for
        while (forest.lsize() > 1) {
            final Forest f1 = forest.heapdn();
            final Forest f2 = forest.heapdn();
            forest.insert(new Forest(f1, f2));
        } // end while
        return forest.heapdn();
    }// end buildforest

    private String[] huffman() {
        final String[] c = coding();
        return c;
    }// end huffman

    /**
     * Recursive method to add huffman binary code to forest
     * 
     * @param f
     * @param c
     */
    private void genCode(final Forest.Node t, final String[] c) {
        if (t.lf != null) {
            t.lf.bin = t.bin + "0";
            genCode(t.lf, c);
            t.rt.bin = t.bin + "1";
            genCode(t.rt, c);
        } else
            c[(int) t.ch] = t.bin;
    }// end genCode

    /**
     * Gets the huffman code by calling genCode and return as String
     * 
     * @param bf
     * @return huffman binary code
     */
    private String[] coding() {
        final String c[] = new String[ASCII];
        final int[] freqs = new int[ASCII];
        final Forest f = buildforest(freqs, countfreq(freqs));
        genCode(f.root, c);
        return c;
    }// end codin

    /**
     * print huffman code table with ASCII, Chars, Freq, Code. print calculations of
     * total input characters, sum of bits, number of bytes, and average length of
     * codes.
     */
    private void printoutput() {
        int bitsum = 0;
        final int[] freqs = new int[ASCII];
        countfreq(freqs);
        System.out.printf("\n%s %6s %6s %9s\n" + "%s %6s %6s %12s\n", "ASCII", "Char", "Freq", "Code", "=====", "=====",
                "=====", "=======");
        for (int i = 0; i < ASCII; i++) {
            if (freqs[i] != 0) {
                bitsum += freqs[i] * huffman()[i].length();
                System.out.printf("%3d %6s %8d %4s %-4s\n", i, (char) i + "", freqs[i], "", huffman()[i]);
            } // end if
        } // end for
        System.out.printf("================================\n");
        System.out.printf("No. of input chars: %d\n", TOTAL);
        System.out.printf("Sum of bits: %d # Bytes = %.1f\n", bitsum, bitsum / 8.0);
        System.out.printf("Avg. length of codes: %.6f\n\n", bitsum / (double) TOTAL);
    }// end printoutput

    public static void prt(final String s) {
        System.out.printf("\n%s ", s);
    }// end prt

    public static void main(final String[] args) {
        Kim5 h = new Kim5();
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String read = "";
            while ((read = br.readLine()) != null)
                h.TEXT += read;
            h.TOTAL = h.TEXT.length();
            br.close();
            h.FREQS = new int[ASCII]; // 128 ASCII characters
            final int n = h.countfreq(h.FREQS); // compute frequency
            h.buildforest(h.FREQS, n); // build forest
            h.huffman();
            h.coding();
            h.printoutput(); // print output

            // serialize to file
            final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Kim5.obj"));
            out.writeObject(h);
            out.close();

            h = null;
            final ObjectInputStream in = new ObjectInputStream(new FileInputStream("Kim5.obj"));
            h = (Kim5) in.readObject();
            in.close();

        } catch (final Exception e) {
            prt("Exception: " + e + "\nTrace: ");
            e.printStackTrace();
            prt("\n");
        }//end try catch
    }
}