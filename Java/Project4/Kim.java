/**
 * CSUN COMP 282-04 & COMP 282-05 Fall 2019
 * Generic Java program implements Optimap
 * Binary Search Tree (obst) using dynamic
 * programming for words in the *.text.
 * Computes cost as well as print the obst
 * level by leve.
 * 
 * Name: Philip D. Kim,     ID: 108508736
 */
package Java.Project4;
import java.util.*;
public class Kim <T extends Comparable<T>> {
    BSTNode<T> root;
    private ArrayList<Double> P = new ArrayList<Double>();// probability search for key
    private ArrayList<Double> Q = new ArrayList<Double>();// probability search for dummy
    private ArrayList<T> RT = new ArrayList<T>();// store key to print
    private double[][] E;// expected search cost 
    private double[][] W;// sum of probabilities
    private int[][] R;// root table of subtrees
    private int N;// size of K list
    private boolean level;// boolean to print obst level
    /**
     * Default constructor
     */
    Kim() {
        root = null;
        Q.add(0.0);
        RT.add(null);
        level = false;
    }
    /**
     * BSTNode class
     * @param <T> left child pointer
     * @param <T> right child pointer
     * @param <T> key any data type
     * @param int n: size of binary tree
     * @param int freq: count frequency
     * @param double Pi probability for search key
     * @param double Qi probability for dummy key
     */
    private class BSTNode<T> {
        protected BSTNode<T> left,right;
        private T key;
        private int n;
        private int freq;
        private double probabilityPi;
        BSTNode(T key, double val) {
            this.key = key;
            left = null;
            right = null;
            probabilityPi = val;
        }//end BSTNode constructor
    }//end BSTNode class
    /**
     * compute OBST
     */
    public void computeOBST() {
        N = root.n;
        double[] p = new double[N+1];
        double[] q = new double[N+1];
        for (int i = 0; i < N; i++) {
            p[i] = P.get(i);
            q[i] = Q.get(i);
        }//end for
        N = p.length - 1;
        R = new int[N + 1][N + 1];
        E = new double[N + 2][N + 1];
        W = new double[N + 2][N + 1];
        optimalbst(p, q, N);
    }//end computeOBST
    /**
     * Optimal BST from 'Introduction to Algorithms'
     * @param p Pi probability for search key
     * @param q Qi probability for dummy key
     * @param N Size of Pi
     */
    public void optimalbst(double[] p, double[] q, int N) {
        for (int i = 1; i <= N + 1; i++) {
            E[i][i - 1] = q[i - 1];
            W[i][i - 1] = q[i - 1];
        }//end for
        // computes E[i,j] and W[i,j]
        for (int l = 1; l <= N; l++) {
            for (int i = 1; i <= N - l + 1; i++) {
                int j = i + l - 1;
                E[i][j] = Double.MAX_VALUE;
                W[i][j] = W[i][j - 1] + p[j] + q[j];
                // better value of index r into ROOT[i,j]
                for (int r = i; r <= j; r++) {
                    double t = E[i][r - 1] + E[r + 1][j] + W[i][j];
                    if (t < E[i][j]) {
                        E[i][j] = t;
                        R[i][j] = r;
                    }//end if
                }//end for
            }//end inner for
        }//end outter for
        System.out.printf("%30s: %.5f\n\n","OPTIMAL BINARY SEARCH TREE COST: ",E[1][N]);
        if (level == true)
            prtOBST(R);
        prt("\n");
    }//end optimalbst method
    /**
     * print OBST levels
     * @param root
     */
    public void prtOBST(int[][] root) {
        int r = root[1][N];
        System.out.printf("  %-15s  --->   ROOT \n", RT.get(r));
        prtOBSST(1, r - 1, r, "left ", root);
        prtOBSST(r+1, N, r, "right", root);
    }//end prtOBST
    /**
     * print OBST subtree levels
     * @param root
     */
    private void prtOBSST(int i, int j, int r, String dir, int[][] root) {
        if (i <= j) {
            int t = root[i][j];
            System.out.printf("  %-15s  %s  CHILD  TO  %s \n", RT.get(t), dir, RT.get(r));
            prtOBSST(i, t - 1, t, "left ", root);
            prtOBSST(t + 1, j, t, "right", root);
        }//end if
    }//end prtOBSST
    /**
     * get depth for each node
     * @param k key value
     * @return level
     */
    public int depth(T k) {return depth(root, k, 0);}
    public int depth(BSTNode<T> x, T k, int d) {
        if (x == null) return 0;
        int left = depth(x.left, k, d+1);
        if (k.compareTo(x.key) < 0) return left;
        int right = depth(x.right, k, d+1);
        if (k.compareTo(x.key) > 0) return right;
        return d;
    }//end depth
    /**
     * get size of binary tree
     * @return size
     */
    public int size() {return size(root);}
    private int size(BSTNode<T> x) {
        if (x == null) return 0;
        return x.n;
    }//end size
    /**
     * Probabilities for key
     * @param x
     * @return proability
     */
    public double Pi(BSTNode<T> x) {
        int n = size();
        x.probabilityPi = (double)x.freq/n;
        return x.probabilityPi;
    }
        /**
     * Probabilities for dummy
     * @param x
     * @return proability
     */
    public double Qi(BSTNode<T> x) {
        return (x.probabilityPi*2)/3;
    }
    public void computeProb(BSTNode<T> x) {
        P.add(Pi(x));
        Q.add(Qi(x));
    }
    /**
     * Insert type == 1
     * @param key
     */
    public void insert(T key) {
        root = insert(root, key, 0.0);
        RT.add(key);
    }//end insert helper
    /**
     * insert type != 1
     * @param key
     * @param val
     */
    public void insert(T[] key, double[] val) {
        for (int i = 1; i < key.length; i++) {
            root = insert(root, key[i], val[i]);
            RT.add(key[i]);
            P.add(val[i]);
            Q.add((val[i]*2)/3);
        }
    }//end insert helper
    private BSTNode<T> insert(BSTNode<T> x, T key, double val) {
        if (x == null)
            x = new BSTNode<T>(key, val);
        if (key.compareTo(x.key) < 0)
            x.left = insert(x.left, key, val);
        else if (key.compareTo(x.key) > 0)
            x.right = insert(x.right, key, val);
        else {
            x.key = key;
            x.freq++;
        }//end if else if else
        x.n = 1 + size(x.left) + size(x.right);
        return x;
    }//end insert
    /**
     * Print type == 1
     * @param x
     */
    private void inorder1(BSTNode<T> x) {
        if (x == null) return;
        inorder1(x.left);
        computeProb(x);
        System.out.printf("%-8d %-20s %-5.2f %-5.2f %5d \n",
        depth(x.key),x.key,Pi(x),Qi(x),x.freq);
        inorder1(x.right);
    }//end inorder
    public void inorder1() {
        if (root == null) return;
        System.out.printf("LEVEL %5s %26s %15s\n","Ki","Pi   Qi","FREQUENCY");
        prt("---------------------------------------------------------\n");
        inorder1(root);
        prt("---------------------------------------------------------\n");
        computeOBST();
    }//end inorder helper
    /**
     * Print type != 1
     * @param x
     */
    private void inorder2(BSTNode<T> x) {
        if (x == null) return;
        inorder2(x.left);
        System.out.printf("%-8d %-20s %-5.2f %-5.2f %5d \n",
        depth(x.key),x.key,x.probabilityPi,Qi(x),x.freq);
        inorder2(x.right);
    }//end inorder
    public void inorder2() {
        if (root == null) return;
        System.out.printf("LEVEL %5s %26s %15s\n","Ki","Pi   Qi","FREQUENCY");
        prt("---------------------------------------------------------\n");
        inorder2(root);
        prt("---------------------------------------------------------\n");
        computeOBST();
    }//end inorder helper
    public static void prt(String s) {System.out.print(s);}
    public static void main(String[] args) {
        prt("\nName: Philip D. Kim,     ID: 10850873\n\n");
        try {
            Kim<String> bst = new Kim<String>();
            Scanner inf = new Scanner(System.in);
            int type = inf.nextInt();
            if (type == 1) {
                while (inf.hasNext()) {
                    String word = inf.next();
                    // insert word into a list and set its frequency
                    bst.insert(word);
                }// end while
                // compute probability of each word of the file
                bst.inorder1();
                // process the sorted list compute obst
            } else {
                int n = type;
                Kim<String> bst1 = new Kim<String>();
                String[] word = new String[n+1];
                String[] prob = new String[n+1];
                double[] p = new double[n+1];
                for (int i = 1; i <= n; i++) {
                    word[i] = inf.next();
                    p[i] = inf.nextInt()/100.0; // read its probability
                    prob[i] = Double.toString(p[i])+" "+word[i];
                }// end while
                // use dynamic programming to compute obst
                bst.insert(word, p);
                bst.inorder2();
                bst1.insert(prob,p);
                bst1.level = true;
                bst1.inorder2();
                
            }// end if else
            // Print words and their probabilities in a user friendly manner
            // Once sorted by word and next sorted by probability
            // Print obst level by level
            inf.close();
        } catch(Exception e) {
            prt("\nException: "+e+"\n\n");
            e.printStackTrace();
            prt("\n");
        }//end try catch
    }
}

