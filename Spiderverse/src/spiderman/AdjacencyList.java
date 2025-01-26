package spiderman;
import java.util.*;

public class AdjacencyList {
    
    public static Map<Integer, List<Integer>> generateAdjacencyList(List<LinkedList<Integer>> clusterTable, Map<Integer, List<Integer>> adjList) {

        for (LinkedList<Integer> cluster : clusterTable) {
            int firstDimension = cluster.get(0);

            for (int j = 1; j < cluster.size(); j++) {
                int dimension = cluster.get(j);

                adjList.computeIfAbsent(firstDimension, k -> new ArrayList<>()).add(dimension);
                adjList.computeIfAbsent(dimension, k -> new ArrayList<>()).add(firstDimension);
            }
        }

        return adjList;
    }
    
    public static void adjSpecialRule(List<LinkedList<Integer>> clusterTable){

        for (int i = 0; i < clusterTable.size(); i++) {
            LinkedList<Integer> currentCluster = clusterTable.get(i);
            LinkedList<Integer> prev1 = clusterTable.get((i - 1 + clusterTable.size()) % clusterTable.size());
            LinkedList<Integer> prev2 = clusterTable.get((i - 2 + clusterTable.size()) % clusterTable.size());

            if (i == 0) {
                currentCluster.add(prev1.getFirst());
                currentCluster.add(prev2.getFirst());
            } else if (i == 1) {
                currentCluster.add(prev1.getFirst());
                currentCluster.add(clusterTable.get(clusterTable.size() - 1).getFirst()); // Last cluster's front dimension
            } else {
                currentCluster.add(prev1.getFirst());
                currentCluster.add(prev2.getFirst());
            }

            // Debug: Print current cluster
            System.out.println("Current Cluster: " + currentCluster);
        }
    }

    public static void printPath(List<Integer> path){
        for (int i = 0; i < path.size(); i++) {
            StdOut.print(path.get(i) + " ");
        }
    }
}
