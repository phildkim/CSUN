/**
 * CSUN COMP 282-04 & COMP 282-05 Fall 2019
 * Generic Java program implements insertion in
 * a btree of degree m.
 * 
 * Name: Philip D. Kim, 	ID: 108508736
 */
package Java.Project3;
import java.util.*;
public class Kim <T extends Comparable<T>> {
	BNode<T> root;// root of BTree
	int m;//degree of BTree
	int c;
	/**
	 * BTree constructor
	 * @param k number of degree
	 */
	Kim(int k) {
		m = k;
		root = null;
		c = 0;
	}//end of constructor
	/**
	 * Definition of BNode
	 * @param <T> array object
	 */
	@SuppressWarnings("unchecked")
	private class BNode<T> {
		int count;
		T[] data;
		BNode<T> link[], parent;
		/**
		 * Constructor of BNode
		 * @param x
		 */
		BNode(T x) {
			count = 1;
			data = (T[]) new Comparable[m];
			link = new BNode[m];
			data[1] = x;
		}// end method BNode(T x)
	}//end class BNode<T>
	public static void prt(String s) {System.out.print(s);}
	/**
	 * Class used for search
	 */
	private class SearchResult {
		BNode<T> node;
		boolean found;
		SearchResult(BNode<T> x, boolean k) {
			node = x;
			found = k;
		}// end SearchResult method
	}//end class SearchResult
	/**
	 * Search for x in BTree with root n
	 * @param x number to find
	 * @param n root
	 * @return number found in tree
	 */
	public SearchResult search(T x, BNode<T> n) {
		SearchResult r;
		if (n == null) {
			r = new SearchResult(null, false);
			return r;
		}//end if
		int k;
		BNode<T> curr = n;
		while (n != null) {
			curr = n;
			for (k = 1; k <= n.count; k++) // search for x
				if (x.compareTo((T)n.data[k]) == 0) {
					r = new SearchResult(n, true);	// x found
					return r;
				}
			if (curr.link != null) {
				r = new SearchResult(curr, false);
				return r;
			} else break;
			
					
		}//end while (n != null)
		r = new SearchResult(curr, false);	// x not found
		return r;
	}//ebd SearchResult
	/**
	 * Inorder Traversal, BTree starting from root.
	 */
	public void inorder() {
		prt("\nName: Philip D. Kim\t\tID: 108508736\n\nDegree m = " + m + "\n");
		if (root == null) return;
		inorder(root);
		prt("\n\n");
	}//end inorder()
	/**
	 * Inorder Traversal, BTree starting from BNode n
	 * @param n root
	 */
	public void inorder(BNode<T> n) {
		if (n == null) return;
		inorder(n.link[0]);
		for (int i = 1; i <= n.count; i++) {
			c++;
			if (c%15==0) System.out.printf("%6d \n",n.data[i]);
			else System.out.printf("%6d ",n.data[i]);
			inorder(n.link[i]);
		}//end for
	}// end inorder(n)
	/**
	 * Insert x into BTree
	 * @param x number inserting to BTree
	 */
	public void insert(T x) {
		if (root == null) {
			root = new BNode<T>(x);
			return;
		}//end if
		SearchResult r = search(x, root);	// Search for x in the BTree
		if (r.found == false) insert(x, r.node);
		else return;
	}//end insert(x)
	/**
	 * Insert x into node n of BTree
	 * @param x number
	 * @param n node
	 */
	@SuppressWarnings("unchecked")
	public void insert(T x, BNode<T> n) {
		BNode<T> a;
		int i,j,k;
		for (a = null;;) {
			// case 1: not full, n has enough room, insert x
			if (n.count < m - 1) {
				// find proper place to insert
				for (i = n.count; i >= 1 && x.compareTo((T)n.data[i]) < 0; i--) {
					n.data[i + 1] = n.data[i];
					n.link[i + 1] = n.link[i];
				}// end for
				n.data[i + 1] = x;
				n.link[i + 1] = a;
				n.count++;
				/**
				 * COMPLETE CODE HERE...
				 * IMPORTANT: update parent link
				 */
				if (n.link[i] != null)
					n.link[i].parent = n;
				break;
			}//end if (case 1)
			// create temporary data and link for node split
			T tmpdata[] = (T[]) new Comparable[m + 1];
			BNode<T>[] tmplink = new BNode[m + 1];
			// Move full node n to tmp for inserting x
			for (i = 1; i < m; i++) {
				tmpdata[i] = n.data[i];
				tmplink[i] = n.link[i];
			}//end for
			// insert x and its link (a) in tmp array and find its middle element.
			for (i = m - 1; i >= 1 && x.compareTo((T) tmpdata[i]) < 0; i--) {
				tmpdata[i + 1] = tmpdata[i];
				tmplink[i + 1] = tmplink[i];
			}// end for
			// insert x in proper position
			tmpdata[i + 1] = x;
			// insert link x in proper position
			tmplink[i + 1] = a;
			// find index of middle element
			k = (m + 1) / 2;
			// keep middle element of tmp[] in x to send it up
			x = (T)tmpdata[k];
			// update no. of elements in node n
			n.count = k - 1;
			// move datas < x from array tmp to n and update the parents
			for (i = 1; i < k; i++) {
				n.data[i] = tmpdata[i];
				n.link[i] = tmplink[i];
				/**
				 * COMPLETE CODE HERE...
				 * IMPORTANT: update parent link
				 */
				// n.link[i] = tmplink[n.count];	// very IMPORTANT
				if (n.link[0] != null)
					n.link[0].parent = n;
			}//end for
			// Allocate space for node to split
			a = new BNode<T>(x);	// x will change later
			// // move datas > x from array tmp to a and update the parents
			for (i = k + 1, j = 1; i <= m; i++, j++) {
				a.data[j] = tmpdata[i];
				a.link[j] = tmplink[i];
				// IMPORTANT update parent link
			}//end for
			a.link[0] = tmplink[k];	// very IMPORTANT
			if (a.link[0] != null)
				a.link[0].parent = a;
			// Set no. of elements in new node
			a.count = m - k;
			// root split
			if (n == root) {
				BNode<T> newroot = new BNode<T>(x);
				newroot.link[0] = root;
				newroot.link[1] = a;
				root = newroot;
				n.parent = root;
				a.parent = root;
				break;
			}// end root split
			// recursively insert x and its link BNode<T> a to parent of n
			n = n.parent;	// insert middle element in its parent
			a.link[0].parent = n;
			insert(x,a);
		}//end for (a = null;;)
	}// end insert(x, n)
	public static void main(String[] args) {
		try {
			Scanner inf = new Scanner(System.in);
			int deg = inf.nextInt();	// degree of BTree
			Kim<Integer> bt = new Kim<Integer>(deg);
			while (inf.hasNext()) {
				int x = inf.nextInt();	// read next input data
				bt.insert(x);
			}//end while
			inf.close();
			bt.inorder();
		} catch (Exception e) {
			prt("\nI/O ERROR: " + e + "\n");
			e.printStackTrace();
			prt("\n");
		}//end try/catch
	}//end main
}//end BTree constructor
