package spiderman;
import java.util.*;

public class ClusterTable {
    private ArrayList<LinkedList<Integer>> clusterTable;
    private double capacity;

    /**
     * Constructor to initialize the ClusterTable with a given hash table size and capacity.
     * @param hashTableSize The initial size of the hash table.
     * @param capacity The capacity (threshold) used to determine if rehashing is needed.
     */

    public ClusterTable(int hashTableSize, double capacity) {
        // Initialize the clusterTable with linked lists based on the hash table size
        clusterTable = new ArrayList<>(Collections.nCopies(hashTableSize, null));
        for (int i = 0; i < hashTableSize; i++) {
            clusterTable.set(i, new LinkedList<>());
        }
        this.capacity = capacity; // Set the capacity
    }

    /**
     * Method to add a dimension to the ClusterTable.
     * @param dimensionNumber The dimension number to be added.
     */
    public void addDimension(int dimensionNumber, int size) {
        int hashIndex = dimensionNumber % size; // Calculate the index based on dimension number
        clusterTable.get(hashIndex).addFirst(dimensionNumber); // Add dimension to the appropriate cluster
    }

    public static void createClusterTable(List<LinkedList<Integer>> clusterTable, int numOfDimensions, int initialClusterSize, double rehashLimit) {

        for (int i = 0; i < initialClusterSize; i++) {
            clusterTable.add(new LinkedList<>());
        }

        for (int i = 0; i < numOfDimensions; i++) {
            int dimensionNumber = StdIn.readInt();
            int canonEvents = StdIn.readInt();
            int dimensionWeight = StdIn.readInt();

            int index = dimensionNumber % initialClusterSize;

            clusterTable.get(index).addFirst(dimensionNumber);

            boolean rehash = false;
            if (rehashLimit <= (double) (i + 1) / clusterTable.size()) {
                rehash = true;
            }

            if (rehash) {
                int newSize = clusterTable.size() * 2;

                List<LinkedList<Integer>> newClusterTable = new ArrayList<>(newSize);

                for (int j = 0; j < newSize; j++) {
                    newClusterTable.add(new LinkedList<>());
                }

                for (LinkedList<Integer> cluster : clusterTable) {
                    for (int dimension : cluster) {
                        int newIndex = dimension % newSize;
                        newClusterTable.get(newIndex).addFirst(dimension);
                    }
                }

                clusterTable = newClusterTable;
                initialClusterSize = newSize;
                System.out.println("Cluster table rehashed. New size: " + newSize);
            }
        }
    }

    // Method to apply a specific rule to modify clusters
    public void specialRule(ClusterTable clusters) {
        for (int i = 0; i < clusters.getTableSize(); i++) {
            int prev1 = (i - 1 + clusters.getTableSize()) % clusters.getTableSize();
            int prev2 = (i - 2 + clusters.getTableSize()) % clusters.getTableSize();

            if (!clusters.getClusterTable().get(prev1).isEmpty()) {
                clusters.getClusterTable().get(i).addLast(clusters.getClusterTable().get(prev1).getFirst());
            }
            if (!clusters.getClusterTable().get(prev2).isEmpty()) {
                clusters.getClusterTable().get(i).addLast(clusters.getClusterTable().get(prev2).getFirst());
            }
        }
    }

    // Method to rehash the cluster table
    public static void rehashTable(ClusterTable clusters, int dimensionsAdded) {
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

     /**
     * Setter method to update the capacity of the ClusterTable.
     * @param capacity The new capacity to be set.
     */
    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    /**
     * Setter method to update the cluster table of the ClusterTable.
     * @param clusterTable The new cluster table to be set.
     */
    public void setClusterTable(ArrayList<LinkedList<Integer>> clusterTable) {
        this.clusterTable = clusterTable;
    }

    /**
     * Getter method to retrieve the cluster table.
     * @return The cluster table as an ArrayList of LinkedLists.
     */
    public ArrayList<LinkedList<Integer>> getClusterTable() {
        return clusterTable;
    }

    /**
     * Getter method to retrieve the capacity.
     * @return The capacity of the ClusterTable.
     */
    public double getCapacity() {
        return capacity;
    }

    public int getTableSize (){
        return clusterTable.size();
    }

}