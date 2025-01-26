package spiderman;
import java.util.*;

public class TrackSpot {

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: java TrackSpot <dimension input file> <spiderverse input file> <spot input file> <trackspot output file>");
            return;
        }

        StdIn.setFile(args[0]);
        StdOut.setFile(args[3]);

        int numOfDimensions = StdIn.readInt(); // Read number of dimensions
        int clusterSize = StdIn.readInt(); // Read initial cluster size
        double rehashLimit = StdIn.readDouble(); // Read rehash threshold
        StdIn.readLine();

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

            if ((double) (i + 1) / clusterTable.size() >= rehashLimit) {
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
            }
        }

        AdjacencyList.adjSpecialRule(clusterTable);

        Map<Integer, List<Integer>> adjList = new HashMap<>();
        AdjacencyList.generateAdjacencyList(clusterTable, adjList);

        Spiderverse.spiderverseConnect(adjList, args[1]);

        List<Integer> path = findPath(adjList, args[2]);
        AdjacencyList.printPath(path);

    }

    private static boolean dfs(Map<Integer, List<Integer>> adjList, int current, int end, List<Integer> path, Set<Integer> visited) {
        if (current == end) {
            return true; 
        }

        List<Integer> neighbors = adjList.get(current);
        if (neighbors != null) {
            for (int neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    path.add(neighbor);
                    visited.add(neighbor);
                    if (dfs(adjList, neighbor, end, path, visited)) {
                        return true; 
                    }
                }
            }
        }
        return false;
    }

    private static List<Integer> findPath(Map<Integer, List<Integer>> adjList, String spot) {
        StdIn.setFile(spot);
        int initial = StdIn.readInt();
        int end = StdIn.readInt();

        List<Integer> path = new ArrayList<>();
        Set<Integer> visited = new HashSet<>(); 

        path.add(initial);
        visited.add(initial);

        dfs(adjList, initial, end, path, visited);

        return path;
    }
    
}