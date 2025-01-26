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
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 *       all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */

 public class Collider {
    public ArrayList<LinkedList<Integer>> adjacencyList;
    public HashMap<Integer, Dimension> dimensionsMap;    
    public static void main(String[] args) {
        

        if (args.length < 3) {
            StdOut.println("Execute: java -cp bin spiderman.Collider <dimension Input file> <spiderverse Input file> <collider Output file>");
            return;
        }

        // Step 1: Read dimensions input file
        StdIn.setFile(args[0]); // Set input file for dimensions
        StdOut.setFile(args[2]); // Set output file for collider

        int numOfDimensions = StdIn.readInt(); // Read number of dimensions
        int clusterSize = StdIn.readInt(); // Read initial cluster size
        double rehashLimit = StdIn.readDouble(); // Read rehash threshold

        // Create ClusterTable object with given parameters
        List<LinkedList<Integer>> clusterTable = new ArrayList<>(clusterSize);

        for (int i = 0; i < clusterSize; i++) {
            clusterTable.add(new LinkedList<>());
        }

        for (int i = 0; i < numOfDimensions; i++) {
            int dimensionNumber = StdIn.readInt();
            int canonEvents = StdIn.readInt();
            int dimensionWeight = StdIn.readInt();

            int index = dimensionNumber % clusterSize;

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
                clusterSize = newSize;
                System.out.println("Cluster table rehashed. New size: " + newSize);
            }
        }

        AdjacencyList.adjSpecialRule(clusterTable);

        // Generating adjList
        System.out.println("Generating adjList");

        Map<Integer, List<Integer>> adjList = new HashMap<>();

        for (LinkedList<Integer> cluster : clusterTable) {
                int firstDimension = cluster.get(0);

                for(int j = 1; j < cluster.size(); j++) {
                    int dimension = cluster.get(j);

                    adjList.computeIfAbsent(firstDimension, k -> new ArrayList<>()).add(dimension);
                    adjList.computeIfAbsent(dimension, k -> new ArrayList<>()).add(firstDimension);

                }

        }

        System.out.println("Completed generating adjList");

        //Reading and Inserting Spiderverse
        StdIn.setFile(args[1]);
        Spiderverse.spiderverseConnect(adjList, args[1]);

        // Output
        System.out.println("Generating ColliderOutputFile");
        for (Map.Entry<Integer, List<Integer>> entry : adjList.entrySet()) {
            StdOut.print(entry.getKey());
            for (int adjDim : entry.getValue()) {
                StdOut.print(" " + adjDim);
            }
            StdOut.println();
        }
    }

    public void printAdjList(String outputFile) {
 
        StdOut.setFile(outputFile);
        for (int i = 0; i < adjacencyList.size(); i++) {
        LinkedList<Integer> neighbors = adjacencyList.get(i);
            if (!neighbors.isEmpty()) {
                StdOut.print(i + " ");
                for (int neighbor : neighbors) {
                    StdOut.print(neighbor + " ");
                }
            StdOut.println();
            }
        }
    }

    public void addPeople(String spiderverseInputFile) {
        StdIn.setFile(spiderverseInputFile);
         
        int numPeople = StdIn.readInt();
         
        for (int i = 0; i < numPeople; i++) {
        int currentDimension = StdIn.readInt();
        String name = StdIn.readString();
        int dimensionalSignature = StdIn.readInt();
         
        Spiderverse person = new Spiderverse(currentDimension, name, dimensionalSignature);
        dimensionsMap.get(currentDimension).addResident(person);
        }
    }
    
}