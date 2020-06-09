/**
 * CSUN COMP 282-04 & COMP 282-05 Fall 2019
 * Generic Java program implements Optimap
 * Binary Search Tree (obst) using dynamic
 * programming for each word that is not in
 * the bst.
 * 
 * Name: Philip D. Kim,     ID: 108508736
 */
package Java.Project4;
import java.util.*;
public class Kim2 <T extends Comparable<T>> {
    BSTNode<T> root;
    private ArrayList<Double> P = new ArrayList<Double>();// probability search for key
    private ArrayList<Double> Q = new ArrayList<Double>();// probability search for dummy
    private ArrayList<T> RT = new ArrayList<T>();// store key to print
    private double[][] E;// expected search cost 
    private double[][] W;// sum of probabilities
    private int[][] R;// root table of subtrees
    private int N;// size of K list
    Kim2() {
        root = null;
        P.add(0.0);
        RT.add(null);
    }
    private class BSTNode<T> {
        protected BSTNode<T> left,right;
        private T key;
        private int n;
        private int freq;
        private double probabilityPi;
        private double probabilityQi;
        BSTNode(T key, double p, double q) {
            this.key = key;
            left = null;
            right = null;
        }//end BSTNode constructor
    }//end BSTNode class
    public void computeOBST() {
        N = root.n;
        double[] p = new double[N+1];
        double[] q = new double[N+1];
        for (int i = 0; i < N; i++) {
            p[i] =  P.get(i);
            q[i] =  Q.get(i);
        }//end for
        N = p.length - 1;
        R = new int[N + 1][N + 1];
        E = new double[N + 2][N + 1];
        W = new double[N + 2][N + 1];
        optimalbst(p, q, N);
    }//end computeOBST
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
        prtOBST(R);
        prt("\n");
    }//end optimalbst method
    public void prtOBST(int[][] root) {
        int r = root[1][N];
        System.out.printf("  %-15s  --->   ROOT \n", RT.get(r));
        prtOBSST(1, r - 1, r, "left ", root);
        prtOBSST(r+1, N, r, "right", root);
    }//end prtOBST
    private void prtOBSST(int i, int j, int r, String dir, int[][] root) {
        if (i <= j) {
            int t = root[i][j];
            System.out.printf("  %-15s  %s  CHILD  TO  %s \n", RT.get(t), dir, RT.get(r));
            prtOBSST(i, t - 1, t, "left ", root);
            prtOBSST(t + 1, j, t, "right", root);
        }//end if
    }//end prtOBSST
    public int depth(T k) {return depth(root, k, 0);}
    public int depth(BSTNode<T> x, T k, int d) {
        if (x == null) return 0;
        int left = depth(x.left, k, d+1);
        if (k.compareTo(x.key) < 0) return left;
        int right = depth(x.right, k, d+1);
        if (k.compareTo(x.key) > 0) return right;
        return d;
    }//end depth
    public int size() {return size(root);}
    private int size(BSTNode<T> x) {
        if (x == null) return 0;
        return x.n;
    }//end size
    public void insert(T[] key, double[] p, double[] q) {
        for (int i = 1; i < key.length; i++) {
            root = insert(root, key[i], p[i], q[i]);
            RT.add(key[i]);
            P.add(p[i]);
            Q.add(q[i]);
        }
    }//end insert helper
    private BSTNode<T> insert(BSTNode<T> x, T key, double p, double q) {
        if (x == null)
            x = new BSTNode<T>(key, p, q);
        if (key.compareTo(x.key) < 0)
            x.left = insert(x.left, key, p, q);
        else if (key.compareTo(x.key) > 0)
            x.right = insert(x.right, key, p, q);
        else {
            x.key = key;
            x.freq++;
        }//end if else if else
        x.probabilityPi += p;
        x.probabilityQi += q;
        x.n = 1 + size(x.left) + size(x.right);
        return x;
    }//end insert
    private void inorder(BSTNode<T> x) {
        if (x == null) return;
        inorder(x.left);
        System.out.printf("%-8d %-20s %-5.2f %-5.2f %5d \n",
        depth(x.key),x.key,x.probabilityPi,x.probabilityQi,x.freq);
        inorder(x.right);
    }//end inorder
    public void inorder() {
        if (root == null) return;
        System.out.printf("LEVEL %5s %26s %15s\n","Ki","Pi   Qi","FREQUENCY");
        prt("---------------------------------------------------------\n");
        inorder(root);
        prt("---------------------------------------------------------\n");
        computeOBST();
    }//end inorder helper
    public static void prt(String s) {System.out.print(s);}
    public static void main(String[] args) {
        prt("\nName: Philip D. Kim,     ID: 10850873\n\n");
        try {
            Kim2<String> bst = new Kim2<String>();
            Scanner inf = new Scanner(System.in);
            int n = inf.nextInt();
            String[] word = new String[n+1];
            double[] p = new double[n+1];
            double[] q = new double[n+1];
            for (int i = 1; i <= n; i++) {
                word[i] = inf.next();
                p[i] = inf.nextInt()/100.0; // read its probability                
            }// end while
            // array p contains probability for each word
            // array q contains probability for each word not in bst
            q[0]=.05;
            q[1]=0.0075;
            p[1]=.04000000000000001;
            for (int i = 2; i <= n; i++) {
                q[i] = p[i] * .2;
                p[i] = p[i] * .8;
            }
            bst.insert(word, p, q);
            bst.inorder();
            inf.close();
        } catch(Exception e) {
            prt("\nException: "+e+"\n\n");
            e.printStackTrace();
            prt("\n");
        }//end try catch
    }
}

