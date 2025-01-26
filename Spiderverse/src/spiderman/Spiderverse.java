package spiderman;
import java.util.*;

public class Spiderverse {
    
    private int dimension;
    private String name;
    private int signature;

    public Spiderverse(int dim, String name2, int sig){
        dimension = 0;
        name = "";
        signature = 0;
    }

    public void Spiderverse(int dim, String name, int sig){
        this.dimension = dim;
        this.name = name;
        this.signature = sig;
    }

    public static void spiderverseConnect(Map<Integer, List<Integer>> adjList, String inputFile) {
        StdIn.setFile(inputFile);
        int numPeople = StdIn.readInt();

        for (int i = 0; i < numPeople; i++) {
            int currentDim = StdIn.readInt();
            String inputName = StdIn.readString();
            int signature = StdIn.readInt();

            List<Integer> currentDimensionList = adjList.get(currentDim);

            if (currentDimensionList == null) {
                currentDimensionList = new ArrayList<>();
            }else{
                adjList.putIfAbsent(currentDim, currentDimensionList);
            }
        }
    }

    public void setDimension(int dim) {
        this.dimension = dim;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSignature(int sig) {
        this.signature = sig;
    }

    public int getDimension(){
        return dimension;
    }

    public String getName(){
        return name;
    }

    public int getSignature(){
        return signature;
    }

    public String toString() {
        return "Person{" + "name ='" + name + ',' +", dimensionalSignature =" + signature + ", currentDimension =" + dimension + '}';
    }

}
