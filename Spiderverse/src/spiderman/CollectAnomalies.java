package spiderman;

import java.util.*;
    public class CollectAnomalies {
        private Map<String, Integer> name_sig;
        private Map<Integer, String> dim_name;
        private Map<String, Integer> name_dim;
        private Map<String, Integer> sig_dim;
        private int hubDimension;
        private Map<Integer, Dimension> dimensionsMap;

        private ArrayList<LinkedList<Integer>> adjList;

        public static void main(String[] args) {
            if (args.length < 4) {
                StdOut.println(
                    "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
                return;
            }

            String inputFile = args[0];
            String spiderverseInputFile = args[1];
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
            String hubInputFile = args[2];
            String outputFile = args[3];
            new CollectAnomalies(outputFile, inputFile, spiderverseInputFile, hubInputFile, adjList);
        }
    
        public CollectAnomalies(String outputFile, String dimensionInputFile, String spiderverseInputFile, String hubInputFile, Map<Integer, List<Integer>> adjList) {
            dim_name = new HashMap<>();
            name_dim = new HashMap<>();
            name_sig = new HashMap<>();

            this.adjList = new ArrayList<>();
    
            for (Map.Entry<Integer, Dimension> entry : dimensionsMap.entrySet()) {

                int dimensionNumber = entry.getKey();

                Dimension dimension = entry.getValue();
                
                List<Spiderverse> residents = dimension.getResidents();
                for (int i = 0; i < residents.size(); i++) {
                    Spiderverse person = residents.get(i);
                    name_dim.put(person.getName(), person.getDimension());
                    name_sig.put(person.getName(), person.getSignature());
                }                
            }
    
            readHubFile(hubInputFile);

        List<String> collectedAnomalies = collectAnomalies();

        printOut(outputFile, collectedAnomalies);
    }

    private void readHubFile(String hubInputFile) {

        StdIn.setFile(hubInputFile);
        hubDimension = StdIn.readInt();
    }

    private List<String> collectAnomalies() {
        List<String> collectedAnomalies = new ArrayList<>();
        boolean[] visited = new boolean[adjList.size()];

        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> parentMap = new HashMap<>();

        queue.offer(hubDimension);
        visited[hubDimension] = true;

        while (!queue.isEmpty()) {
            int currentDimension = queue.poll();

            for (int i = 0; i < adjList.get(currentDimension).size(); i++) {
                int neighbor = adjList.get(currentDimension).get(i);
                if (!visited[neighbor]) {

                    visited[neighbor] = true;
                    queue.offer(neighbor);
                    parentMap.put(neighbor, currentDimension);

                    String anomaly = null;
                    String spider = null;
                    
                    for (int j = 0; j < name_dim.size(); j++) {
                        String name = name_dim.keySet().toArray(new String[0])[j];
                        int dimension = name_dim.get(name);
                        int signature = name_sig.get(name);

                        if (dimension == neighbor) {
                            if (signature != neighbor) {
                                anomaly = name;
                            } else {
                                spider = name;
                            }
                        }
                    }

                    if (anomaly != null) {
                        List<Integer> route = new ArrayList<>();
                        int current = neighbor;
                        while (current != hubDimension) {
                            route.add(0, current);
                            current = parentMap.get(current);
                        }
                        route.add(0, hubDimension);

                        StringBuilder result = new StringBuilder();

                        if (spider != null) {
                            result.append(anomaly);
                            result.append(" ");
                            result.append(spider);
                            Collections.reverse(route);
                        } else {
                            result.append(anomaly);
                            for (int k = route.size() - 2; k >= 0; k--) {
                                route.add(route.get(k));
                            }
                        }

                        for (int dimension : route) {
                            result.append(" ").append(dimension);
                        }

                        collectedAnomalies.add(result.toString());
                    }
                }
            }
        }

        return collectedAnomalies;
    }

    private void printOut(String outputFile, List<String> collectedAnomalies) {
        StdOut.setFile(outputFile);
        for (String anomaly : collectedAnomalies) {
            StdOut.println(anomaly);
        }
    }
}