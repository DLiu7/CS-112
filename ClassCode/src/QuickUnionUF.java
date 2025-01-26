public class QuickUnionUF {
    
  // Array stores the information that two vertices are connected.
  // Vertices that are connected have the same root
  private int[] parent;  
    
  // Initializes the data structure
  // Running time, how many array accesses? f(n) = n => O(n)
  public QuickUnionUF(int n) {
    parent = new int[n];

      for (int i = 0; i < n; i++)  {        
        parent[i] = i; // 1 array write
      } // // 1 write for every iteration. There are n iterations.
  }

  // Returns the representative of the set of vertices that contains p
  // How? returns the root
  // The root is the vertex that is its own parent
  // Running time? f(n) = ????? worst case????
  public int find (int p) {

     while ( p != parent[p] ) { // 1 read
       p = parent[p];           // 1 read
     }      
   return p;   
  } 
  
  // Connect vertices p and q
  // How? change the root of p to point to root of q
  // Running time, how many array accesses? f(n) = ?????
  public void union(int p, int q) {
     int rootP = find(p); // get the root of p O(n)
     int rootQ = find(q); // get the root of q O(n)
     parent[rootP] = rootQ;   // 1 write
  }
}
