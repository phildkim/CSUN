package Java.Project1;
import java.io.*;
import java.util.*;

class Matrix {
  public static void createMatrix(String file) {
    try {
      FileWriter opf = new FileWriter(file, true);
      Random ints = new Random(5);
      int P = ints.nextInt((1000 - 50) + 1) + 50; // p(50..100)
      int Q = ints.nextInt((10000 - 1000) + 1) + 1000; // q(1000..10000)
      int K = ints.nextInt((25 - 10) + 1) + 10; // k(10..25)
      opf.write(P + " " + Q + " " + K + "\n");
      for (int i = 1; i < P; ++i) {
        int N = ints.nextInt(((Q - 1) - 1000) + 1) + 1000; // n(1000..q-1)
        opf.write(N + " ");
        for (int j = 1; j <= N; ++j) {
          int M = ints.nextInt((100000 - 10000) + 1) + 10000; // m(10000..100000)
          opf.write(M + " ");
        }
        opf.write("\n");
      }
      opf.close();
    } catch (IOException e) {
      e.printStackTrace();
      e.getMessage();
    }
  }

  public static void main(String[] args) throws Exception {
    Scanner opf = new Scanner(System.in);
    System.out.print("File name: ");
    String file = opf.next();
    createMatrix(file);
    opf.close();
  } // end of writeMatrix
}// end of matrix