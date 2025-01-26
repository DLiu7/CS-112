package spiderman;
import java.util.*;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 
 * Step 2:
 * ClusterOutputFile name is passed in through the command line as args[1]
 * Output to ClusterOutputFile with the format:
 * 1. n lines, listing all of the dimension numbers connected to 
 *    that dimension in order (space separated)
 *    n is the size of the cluster table.
 * 
 * @author Seth Kelley
 */

 public class Clusters {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -cp bin spiderman.Clusters <dimension input file> <cluster output file>");
            return;
        }

        // Assuming StdIn and StdOut are custom classes for file I/O operations
        StdIn.setFile(args[0]); // Set input file
        int numOfDimensions = StdIn.readInt(); // Read number of dimensions
        int hashTableSize = StdIn.readInt(); // Read hash table size
        double capacity = StdIn.readDouble(); // Read capacity

        // Create ClusterTable object with given parameters
        ClusterTable clusters = new ClusterTable(hashTableSize, capacity);

        int dimensionsAdded = 0;
        for (int i = 0; i < numOfDimensions; i++) {
            // Read dimension details from input file
            int dimensionNumber = StdIn.readInt(); // Dimension number
            int canonEvents = StdIn.readInt(); // Cannon events
            int dimensionWeight = StdIn.readInt(); // Dimension weight

            int tableSize = clusters.getTableSize();

            // Add dimension to ClusterTable
            clusters.addDimension(dimensionNumber, tableSize);
            dimensionsAdded++;

            // Check if rehashing is needed
            boolean needRehash = false;
            if(clusters.getCapacity() <= (double) dimensionsAdded / clusters.getClusterTable().size()){
                needRehash = true;
            }
            
            // Apply rehashing logic if needed
            Clusters clustersObj = new Clusters(); // Create an instance of Clusters
            if (needRehash) {
                clustersObj.rehashTable(clusters, dimensionsAdded);
            }
            
        }

       // Apply specific rule to modify clusters
       clusters.specialRule(clusters);;

       // Set the output file
       StdOut.setFile(args[1]);

       // Write cluster information to output file
       for (LinkedList<Integer> cluster : clusters.getClusterTable()) {
           for (int dimension : cluster) {
               StdOut.print(dimension + " ");
           }
           StdOut.println();
       }
    }

    // Method to rehash the cluster table
    public void rehashTable(ClusterTable clusters, int dimensionsAdded) {
        int newHashTableSize = clusters.getTableSize() * 2;

        // Create new hash table with increased size
        ArrayList<LinkedList<Integer>> newHashTable = new ArrayList<>(newHashTableSize);
        for (int j = 0; j < newHashTableSize; j++) {
            newHashTable.add(new LinkedList<>());
        }

        // Rehash existing dimensions
        for (LinkedList<Integer> cluster : clusters.getClusterTable()) {
            for (int dim : cluster) {
                int newDimensionIndex = dim % newHashTableSize;
                newHashTable.get(newDimensionIndex).addFirst(dim);
            }
        }
        clusters.setClusterTable(newHashTable); // Update ClusterTable with new hash table
    }

}
