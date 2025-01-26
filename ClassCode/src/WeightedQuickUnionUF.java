public class WeightedQuickUnionUF {
    
  // Array stores the information that two vertices are connected.
  // Vertices that are connected have the same root
  private int[] parent;
  private int[] size;  
    
  // Initializes the data structure
  // Running time, how many array accesses? 
  //    f(n) = 2n => O(n)
  public WeightedQuickUnionUF(int n)   {      
     
      parent = new int[n];
      size = new int[n];
      
      for (int i = 0; i < n; i++)  {        
        parent[i] = i; // 1 write
        size[i]   = 1; // 1 write
        // 2 writes for every iteration. There are n iterations.
      }
  }

  // Returns the representative of the set of vertices that contains p
  // How? returns the root
  // The root is the vertex that is its own parent
  public int find (int p) {

     while (p != parent[p]) { // 1 read
       p = parent[p];         // 1 read
     }      
   return p;   
  } 
  
  // Connect vertices p and q
  // How? link the root of the smaller tree to the root of the larger tree
  // Running time, how many array accesses?
  // f(n) = ??????
  public void union(int p, int q) {
     int rootP = find(p); // running time of find?
     int rootQ = find(q); // running time of find?
     if (rootP == rootQ) return;

     // Assume rootP is smaller than rootQ
     int rootSmaller = rootP, rootLarger = rootQ;

     // Now check
     if ( size[rootP] >= size[rootQ]) { // 2 reads
        rootSmaller = rootQ;
        rootLarger = rootP;
     }

     // link root of smaller tree to root of larger tree
     parent[rootSmaller] = rootLarger; // 1 read

     // update the size
     size[rootLarger] += size[rootSmaller]; // 1 read and 1 write
  }

  // Client code
  public static void main (String[] args) {
    WeightedQuickUnionUF wqu = new WeightedQuickUnionUF(10);
    wqu.union(0, 2);
    wqu.union(3, 7);
    wqu.union(7, 8);
    
    boolean conn = wqu.find(3)==wqu.find(8);
    System.out.println("Are 3 and 8 connected? " + conn);

   }
}
