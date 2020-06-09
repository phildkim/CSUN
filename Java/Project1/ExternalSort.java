
/**
 * CSUN COMP 282-04 & COMP 282-05 Fall 2019
 * Generic Java program to build Min Heap with 2 methods
 * Input can be read from KB or File.
 * 
 * 1. Reads n(integer), number of inputs from KB/File
 * 2. Reads n data from KB/File
 * 3. Prints computed Min Heap
 * 
 * Name: Philip D. Kim
 */
package Java.Project1;
import java.util.*;

class HeapNode {
  protected int i;// rows
  protected int j;// cols
  protected int k;// data

  public HeapNode(int k, int i, int j) {
    this.k = k;
    this.i = i;
    this.j = j;
  }
}

public class ExternalSort {
  protected HeapNode[] H; // heap node for kway merge
  protected int HN; // heap size
  protected int[][] M; // matrix M to store data
  protected int[] A; // array A to store 2way-merge
  protected int[] B; // array B to store kway-merge
  protected int P; // row for matrix M
  protected int Q; // column for matrix M
  protected int K; // data per line
  protected int N; // number of rows
  protected int T; // total elements
  protected boolean F; // format

  ExternalSort() {
    P = 0;
    Q = 0;
    K = 0;
    N = 0;
    T = 0;
    F = true;
  }// end default constructor

  ExternalSort(HeapNode h[], int n) {
    HN = n;
    H = h;
    int P = (HN - 1) / 2;
    while (P >= 0) {
      heapup(P);
      --P;
    }
  }// end heap constructor

  // heapup method
  protected void heapup(int i) {
    int L = (2 * i) + 1;
    int R = (2 * i) + 2;
    int P = i;
    if (L < HN && H[L].k < H[i].k)
      P = L;
    if (R < HN && H[R].k < H[P].k)
      P = R;
    if (P != i) {
      HeapNode tmp = H[i];
      H[i] = H[P];
      H[P] = tmp;
      heapup(P);
    } // end if
  }// end heapup

  protected HeapNode getRoot() {
    if (HN <= 0)
      return null;
    return H[0];
  }

  protected void getNextRoot(HeapNode i) {
    H[0] = i;
    heapup(0);
  }

  /**
   * 
   * @param M
   * @param n
   * @return kWayMergeSort array
   */
  protected int[] kWayMergeSort(int[][] M, int n) {
    int t = 0;
    HeapNode[] h = new HeapNode[n];
    for (int i = 0; i < M.length; i++) {
      t += M[i].length;
      HeapNode heap = new HeapNode(M[i][0], i, 1);
      h[i] = heap;
    } // end for
    ExternalSort esh = new ExternalSort(h, n);
    int[] B = new int[t];
    for (int i = 0; i < t; i++) {
      HeapNode e = esh.getRoot();
      B[i] = e.k;
      if (e.j < M[e.i].length)
        e.k = M[e.i][e.j++];
      else
        e.k = Integer.MAX_VALUE;
      esh.getNextRoot(e);
    } // end for
    return B;
  }// end kway merge

  // 2way mergesort
  protected void twoWayMergeSort(int[] M, int p, int r) {
    if (p < r) {
      int q = (p + r) / 2;
      twoWayMergeSort(M, p, q);
      twoWayMergeSort(M, q + 1, r);
      mergeSort(M, p, q, r);
    } // end if
  }// end two way merege

  // 2way mergesort
  protected void mergeSort(int[] M, int p, int q, int r) {
    int n1 = (q - p) + 1;
    int n2 = (r - q);
    int[] L = new int[n1 + 2];
    int[] R = new int[n2 + 2];
    for (int i = 1; i <= n1; ++i)
      L[i] = M[(p + i) - 1];
    for (int j = 1; j <= n2; ++j)
      R[j] = M[q + j];
    L[n1 + 1] = Integer.MAX_VALUE;
    R[n2 + 1] = Integer.MAX_VALUE;
    int l = 1;
    int k = 1;
    for (int x = p; x <= r; ++x) {
      if (L[l] <= R[k]) {
        M[x] = L[l];
        ++l;
      } else {
        M[x] = R[k];
        ++k;
      } // end if else
    } // end for
  }// end for merge

  // quicksort
  protected void QuickSort(int[] M, int p, int r) {
    if (p < r) {
      int q = Partition(M, p, r);
      QuickSort(M, p, q - 1);
      QuickSort(M, q + 1, r);
    } // end if
  }// end quicksort

  // quicksort partition
  protected int Partition(int[] M, int p, int r) {
    int pivot = M[p];
    int low = p + 1;
    int high = r;
    while (high > low) {
      while (low <= high && M[low] <= pivot)
        ++low;
      while (low <= high && M[high] > pivot)
        --high;
      if (high > low) {
        int tmp = M[high];
        M[high] = M[low];
        M[low] = tmp;
      } // end if
    } // end while
    while (high > p && M[high] >= pivot)
      --high;
    if (pivot > M[high]) {
      M[p] = M[high];
      M[high] = pivot;
      return high;
    } else
      return p;
  }// end partition

  public static void main(String[] args) throws Exception {
    System.out.println("\nName: Philip D. Kim\tID: pdk55536\n");
    /**
     * Part 1. -Generate write & read file
     */
    ExternalSort es = new ExternalSort();
    Scanner inf = new Scanner(System.in);
    es.P = inf.nextInt(); // rows
    es.Q = inf.nextInt(); // columns
    es.K = inf.nextInt(); // k data per line
    es.M = new int[es.P][es.Q]; // matrix m
    es.T = 0; // total elements
    es.N = 0; // number per column
    es.F = true; // format matrix and array
    // read from scanner and store data onto matrix M
    for (int i = 1; i < es.P; ++i) {
      es.M[i][0] = inf.nextInt();
      es.N = es.M[i][0];
      es.T += es.N;
      for (int j = 1; j <= es.N; ++j)
        es.M[i][j] = inf.nextInt();
    } // end outer for
    inf.close();

    /**
     * Part 1. - Print the unsorted matrix
     */
    printMatrix(es, es.M, "Matrix M\nUnsorted:");

    /**
     * Part 2. a) Read data from file and store to M b) Sort each row using c) Print
     * matrix M with k data per line.
     */
    int n = 0;
    for (int i = 1; i < es.P; ++i) {
      n = es.M[i][0];
      es.QuickSort(es.M[i], 1, n);
    } // end for
    printMatrix(es, es.M, "Matrix A:\nQuicksort: T ( N = " + es.T + " )\n");

    /**
     * Part 2. d) Perform 2way mergesort for each row and store array A. e) Print
     * array A with k data per line and execution time.
     */
    es.A = new int[es.T + 1];
    int index = 0;
    for (int i = 1; i < es.M.length; ++i) {
      int col = es.M[i][0];
      for (int j = 1; j <= col; ++j) {
        es.A[index] = es.M[i][j];
        ++index;
      } // end inner for
    } // end outter for
    long start2ms = System.currentTimeMillis();
    es.twoWayMergeSort(es.A, 0, es.T);
    long stop2ms = System.currentTimeMillis();
    long elapsedTime2ms = stop2ms - start2ms;
    printArray(es, es.A, "Array A\n\ntwoWayMerge: T ( N = " + es.T + " ) = Big O ( " + elapsedTime2ms + " msec )\n");

    // /**
    // * Part 2. f) Perform Kway mergesort for each row and store array B.
    // * e) Print array A with k data per line and execution time.
    // * g) Write a report on 2Way & Kway mergesort.
    // */
    es.B = new int[es.T + 1];
    int[][] copyM = new int[es.P][es.Q];
    for (int i = 1; i < es.M.length; ++i) {
      copyM[i] = new int[es.M[i].length];
      for (int j = 1; j < es.M[i].length; ++j)
        copyM[i][j] = es.M[i][j];
    } // end outter for
    long startkms = System.currentTimeMillis();
    es.B = es.kWayMergeSort(copyM, copyM.length);
    long stopkms = System.currentTimeMillis();
    long elapsedTimekms = stopkms - startkms;
    printArray(es, es.B, "Array B\nkWayMerge: T ( N = " + es.T + " ) = Big O ( " + elapsedTimekms + " msec )\n");
  }// end main

  // print matrix
  public static void printMatrix(ExternalSort E, int[][] M, String s) {
    System.out.println(s.substring(0, 8) + ": [ P = " + (E.P) + " ][ Q = " + (E.Q) + " ][ K = " + (E.K) + " ]");
    String str = "";
    int col = 0;
    for (int i = 1; i < E.P; ++i) {
      System.out.printf("r%-2d [ %-2d ] ", i, E.M[i][0]);
      System.out.printf("[  ");
      if (E.F == true) {
        col = E.K;
        str = "5";
      } else {
        col = M[i][0];
        str = "4";
      }
      for (int j = 1; j <= col; ++j)
        System.out.printf("%" + str + "d, ", M[i][j]);
      System.out.println("\b\b  ]");
    } // end outter for
    System.out.println(s.substring(9, s.length()));
  }// end print matrix

  // print array
  public static void printArray(ExternalSort E, int[] AB, String s) {
    System.out.print(s.substring(0, 7) + ": [ P = " + (E.P) + " ][ Q = " + (E.Q) + " ][ K = " + (E.K) + " ]\n[ ");
    int row = 0;
    for (int j = 0; j < AB.length; ++j) {
      if (AB[j] != 0) {
        AB[row] = AB[j];
        ++row;
      } // end if
    } // end for
    for (int m = 0; m < E.T; ++m) {
      if ((E.F == true) && ((m + 1) % E.K == 0))
        System.out.printf("%4d  ]\n[  ", AB[m]);
      else
        System.out.printf("%2d, ", AB[m]);
    } // end for
    System.out.print("\b\b  ]");
    System.out.println(s.substring(7, s.length()));
  }// end print array
}// end ExternalSort