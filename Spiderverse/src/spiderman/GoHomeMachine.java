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
 * HubInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 *      i.   The Name of the anomaly which will go from the hub dimension to their home dimension (String)
 *      ii.  The time allotted to return the anomaly home before a canon event is missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 *      i.   The number of canon events at that anomalies home dimensionafter being returned
 *      ii.  Name of the anomaly being sent home
 *      iii. SUCCESS or FAILED in relation to whether that anomaly made it back in time
 *      iv.  The route the anomaly took to get home
 * 
 * @author Seth Kelley
 */

 public class GoHomeMachine {
    
    private ArrayList<LinkedList<Integer>> adjList;
    private Map<Integer, Dimension> dimensionsMap;
    private Map<String, Spiderverse> people;
    private int hubDimension;

    
    public GoHomeMachine(String dimensionInputFile, String spiderverseInputFile, String hubInputFile, String anomaliesInputFile, String reportOutputFile) {
        adjList = new ArrayList<>();

        dimensionsMap = new HashMap<>();
        people = new HashMap<>();

        createClusters(dimensionInputFile);
        createGraphs();
 
        addPeople(spiderverseInputFile);

        adjList = this.adjList;
        
        dimensionsMap = this.dimensionsMap;

        readHubInputFile(hubInputFile);

        readSpiderverseInputFile(spiderverseInputFile);

        List<String> reportLines = sendAnomaliesHome(anomaliesInputFile);
        output(reportLines, reportOutputFile);
    }
    public static void main(String[] args) {
        if (args.length < 5) {
            StdOut.println(
                "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
            return;
        }

        String dimensionInputFile = args[0];
        String spiderverseInputFile = args[1];
        String hubInputFile = args[2];
        String anomaliesInputFile = args[3];

        String outputFile = args[4];

        new GoHomeMachine(dimensionInputFile, spiderverseInputFile, hubInputFile, anomaliesInputFile, outputFile);
    }


    private void readHubInputFile(String hubInputFile) {
        StdIn.setFile(hubInputFile);
        hubDimension = StdIn.readInt();
    }

    private void readSpiderverseInputFile(String spiderverseInputFile) {
        StdIn.setFile(spiderverseInputFile);
        int numPeople = StdIn.readInt();

        for (int i = 0; i < numPeople; i++) {
            int dim = StdIn.readInt();
            String name = StdIn.readString();
            int sig = StdIn.readInt();
            Spiderverse person = new Spiderverse(dim, name, sig);
            people.put(name, person);
        }
    }

    private List<String> sendAnomaliesHome(String anomaliesInputFile) {
        List<String> reportLines = new ArrayList<>();
        StdIn.setFile(anomaliesInputFile);
        int numAnomalies = StdIn.readInt();

        for (int i = 0; i < numAnomalies; i++) {
            String name = StdIn.readString();
            int timeAllowed = StdIn.readInt();
            Spiderverse anomaly = people.get(name);
            int homeDimension = anomaly.getSignature();

            Map<Integer, Integer> algoOutput = algo(hubDimension);
            List<Integer> path = getPath(hubDimension, homeDimension, algoOutput);

            int totalTime = calculateTime(path);

            String reportLine;

            if (totalTime <= timeAllowed)
                reportLine = dimensionsMap.get(homeDimension).getCanonEvents() + " " + name + " SUCCESS " + pathToString(path);

            else {
                Dimension homeDim = dimensionsMap.get(homeDimension);
                homeDim.setCanonEvents(homeDim.getCanonEvents() - 1);
                reportLine = homeDim.getCanonEvents() + " " + name + " FAILED " + pathToString(path);
            }

            reportLines.add(reportLine);
        }

        return reportLines;
    }

    private Map<Integer, Integer> algo(int source) {
        Map<Integer, Integer> distances = new HashMap<>();
        
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);

        for (int dimension : dimensionsMap.keySet()) {
            distances.put(dimension, Integer.MAX_VALUE);
        }
        distances.put(source, 0);

        pq.offer(new int[]{source, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentDimension = current[0];
            int currentDistance = current[1];

            if (currentDistance > distances.get(currentDimension)) {
                continue;
            }

            for (int neighbor : adjList.get(currentDimension)) {
                int weight = dimensionsMap.get(currentDimension).getDimensionWeight() + dimensionsMap.get(neighbor).getDimensionWeight();
                int newDistance = currentDistance + weight;

                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    pq.offer(new int[]{neighbor, newDistance});
                }
            }
        }

        return distances;
    }

    private String pathToString(List<Integer> path) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            int dimension = path.get(i);
            sb.append(dimension).append(" ");
        }
        return sb.toString().trim();
    }

    private List<Integer> getPath(int source, int destination, Map<Integer, Integer> distances) {
        List<Integer> path = new ArrayList<>();
        int current = destination;

        while (current != source) {
            path.add(0, current);

            int minDistance = Integer.MAX_VALUE;
            int minNeighbor = -1;

            for (int neighbor : adjList.get(current)) {
                if (distances.get(neighbor) < minDistance) {
                    minDistance = distances.get(neighbor);
                    minNeighbor = neighbor;
                }
            }

            current = minNeighbor;
        }

        path.add(0, source);
        return path;
    }

    private int calculateTime(List<Integer> path) {
        int time = 0;
        int lastDim = -1;

        for (int i = 0; i < path.size(); i++) {
            int currentDim = path.get(i);
            if (lastDim != -1) {
                time += dimensionsMap.get(lastDim).getDimensionWeight() + dimensionsMap.get(currentDim).getDimensionWeight();
            }
            lastDim = currentDim;
        }

        return time;
    }

    private void output(List<String> reportLines, String outputFile) {
        StdOut.setFile(outputFile);

        for (int i = 0; i < reportLines.size(); i++) {
            String line = reportLines.get(i);
            StdOut.println(line);
        }
    }

    private void createClusters(String dimensionInputFile) {
        StdIn.setFile(dimensionInputFile);
        int numOfDimensions = StdIn.readInt();
        int clusterSize = StdIn.readInt();
        double rehashLimit = StdIn.readDouble();

        ClusterTable clusters = new ClusterTable(clusterSize, rehashLimit);
        Clusters clustersObj = new Clusters(); // Create an instance of Clusters

        int dimensionsAdded = 0;
        for (int i = 0; i < numOfDimensions; i++) {
            int dimensionNumber = StdIn.readInt();
            int canonEvents = StdIn.readInt();
            int dimensionWeight = StdIn.readInt();

            int tableSize = clusters.getTableSize();

            clusters.addDimension(dimensionNumber, tableSize);
            dimensionsAdded++;

            boolean needRehash = false;
            if (clusters.getCapacity() <= (double) dimensionsAdded / clusters.getClusterTable().size()) {
                needRehash = true;
            }

            if (needRehash) {
                clustersObj.rehashTable(clusters, dimensionsAdded);
            }
        }

        clusters.specialRule(clusters); // Apply specific rule to modify clusters

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
    }
    private void addPeople(String spiderverseInputFile) {
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

    private void createGraphs() {
        int dimensionSize = Collections.max(dimensionsMap.keySet());
        adjList = new ArrayList<>(dimensionSize + 1);

        for (int i = 0; i <= dimensionSize; i++) {
            adjList.add(new LinkedList<>());
        }

        for (Dimension dimension : dimensionsMap.values()) {
            for (Spiderverse resident : dimension.getResidents()) {
                int dimensionNumber = resident.getDimension();
                for (Spiderverse otherResident : dimension.getResidents()) {
                    int otherDimensionNumber = otherResident.getDimension();
                    if (dimensionNumber != otherDimensionNumber) {
                        adjList.get(dimensionNumber).add(otherDimensionNumber);
                        adjList.get(otherDimensionNumber).add(dimensionNumber);
                    }
                }
            }
        }
    }
}