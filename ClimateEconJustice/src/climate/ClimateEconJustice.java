package climate;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered 
 * linked list structure that contains USA communitie's Climate and Economic information.
 * 
 * @author Navya Sharma
 */

public class ClimateEconJustice {

    private StateNode firstState;
    
    /*
    * Constructor
    * 
    * **** DO NOT EDIT *****
    */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
    * Get method to retrieve instance variable firstState
    * 
    * @return firstState
    * 
    * **** DO NOT EDIT *****
    */ 
    public StateNode getFirstState () {
        // DO NOT EDIT THIS CODE
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county, 
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     * **** DO NOT EDIT *****
     */
    public void createLinkedStructure ( String inputFile ) {
        
        // DO NOT EDIT THIS CODE
        StdIn.setFile(inputFile);
        StdIn.readLine();
        
        // Reads the file one line at a time
        while ( StdIn.hasNextLine() ) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            // IMPLEMENT these methods
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
        }
    }

    /*
    * Adds a state to the first level of the linked structure.
    * Do nothing if the state is already present in the structure.
    * 
    * @param inputLine a line from the input file
    */
    public void addToStateLevel ( String inputLine ) {

        //Splits input into an Array of values
        String[] linkedList = inputLine.split(",");

        //Setting the variable as the value of the third index, stateName
        String stateName = linkedList[2];

        //Boolean for checking existence
        boolean exist = false;

        //Pointer to traverse the linked list
        StateNode currentState = firstState;

        //Checking if a state exists in the structure
        while (currentState != null) {
            if (currentState.getName().equals(stateName)){ 
            exist = true;
            }
            currentState = currentState.getNext();
        }

        //Nonexistent state detected, creating a new StateNode
        if (exist == false) {
        //Creates a new state node that contains the name of the state and points nowhere
        StateNode newState = new StateNode(stateName, null, null);
        
            if (firstState == null){
            firstState = newState;
            }else{
                currentState = firstState;
                while (currentState.getNext() != null)
                    currentState = currentState.getNext();
                    currentState.setNext(newState);
            }
        }

    }

    /*
    * Adds a county to a state's list of counties.
    * 
    * Access the state's list of counties' using the down pointer from the State class.
    * Do nothing if the county is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCountyLevel ( String inputLine ) {

        String[] linkedList = inputLine.split(",");
 
        String countyName = linkedList[1];
        String stateName = linkedList[2];
        
        boolean stateExist = false;
        boolean countyExists = false;
 
        StateNode currentState = firstState;
        CountyNode newCounty = new CountyNode(countyName, null, null);
 
        while (currentState != null) {
            if (currentState.getName().equals(stateName)) {
                stateExist = true;
            }

                if(stateExist == true){
                
                    CountyNode currentCounty = currentState.getDown();
 
                if (currentCounty == null) {
                    currentState.setDown(newCounty);
                } else {
                    while (currentCounty.getNext() != null) {
 
                        if (currentCounty.getName().equals(countyName)) {
                            countyExists = true;
                        }
                        currentCounty = (CountyNode) currentCounty.getNext();
                    }
 
                    if (!countyExists && !currentCounty.getName().equals(countyName)) {
                        currentCounty.setNext(newCounty);
                    }
                }
            }
            currentState = currentState.getNext();
        }
    }

    /*
    * Adds a community to a county's list of communities.
    * 
    * Access the county through its state
    *      - search for the state first, 
    *      - then search for the county.
    * Use the state name and the county name from the inputLine to search.
    * 
    * Access the state's list of counties using the down pointer from the StateNode class.
    * Access the county's list of communities using the down pointer from the CountyNode class.
    * Do nothing if the community is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCommunityLevel ( String inputLine ) {

        // Splits input into an Array of values
        String[] linkedList = inputLine.split(",");

        // Setting the values for linked lists
        String communityName = linkedList[0];
        String countyName = linkedList[1];
        String stateName = linkedList[2];
        double prcntAfricanAmerican = Double.parseDouble(linkedList[3]);
        double prcntNative = Double.parseDouble(linkedList[4]);
        double prcntAsian = Double.parseDouble(linkedList[5]);
        double prcntWhite = Double.parseDouble(linkedList[8]);
        double prcntHispanic = Double.parseDouble(linkedList[9]);
        String disadvantaged = linkedList[19];
        double PMlevel = Double.parseDouble(linkedList[49]);
        double chanceOfFlood = Double.parseDouble(linkedList[37]);
        double prcntPovertyLine = Double.parseDouble(linkedList[121]);

        //Creating data object with information values
        Data dataNode = new Data();
        dataNode.setPrcntAfricanAmerican(prcntAfricanAmerican);
        dataNode.setPrcntNative(prcntNative);
        dataNode.setPrcntAsian(prcntAsian);
        dataNode.setPrcntWhite(prcntWhite);
        dataNode.setPrcntHispanic(prcntHispanic);
        dataNode.setAdvantageStatus(disadvantaged);
        dataNode.setPMlevel(PMlevel);
        dataNode.setChanceOfFlood(chanceOfFlood);
        dataNode.setPercentPovertyLine(prcntPovertyLine);

        boolean stateExist = false;
        boolean countyExist = false;

        CommunityNode firstCommunity = new CommunityNode(communityName, null, null);

        StateNode currentState = firstState;

        while(currentState != null){
            
            if(currentState.getName().equals(stateName)){
                stateExist = true;
            }

            if(stateExist == true){
            CountyNode currentCounty = currentState.getDown();
            
            while(currentCounty != null){

                if(currentCounty.getName().equals(countyName)){
                    countyExist = true;
                }

                if(countyExist == true){
                CommunityNode newCommNode = new CommunityNode(communityName, null, dataNode);
                
                if (currentCounty.getDown() == null) {
                    currentCounty.setDown(newCommNode);
                } else {
                    CommunityNode currentCommunity = currentCounty.getDown();
                    while (currentCommunity.getNext() != null) {
                        currentCommunity = currentCommunity.getNext();
                    }
                    currentCommunity.setNext(newCommNode);
                }
            }
            currentCounty = currentCounty.getNext();
            }
        }
            currentState = currentState.getNext();
        }
        
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int disadvantagedCommunities ( double userPrcntage, String race ) {

        //Variable for counting number of disadvantaged communities
        int numDisComm = 0;

        //Variable for holding race percentage of each community
        double racePrcntage = 0;

        //Sets pointer at first state node
        StateNode currentState = firstState;
        
        //Going through each community from each county from each state
        //Iterating through state list
        while(currentState != null){

            //Sets pointer at county node
            CountyNode currentCounty = currentState.getDown();

            //Iterating through county list
            while(currentCounty != null){

                //Sets pointer at community node
                CommunityNode currentCommunity = currentCounty.getDown();

                //Iterating through community list
                while(currentCommunity != null){

                    //Finding percentage of given race
                    if(race.equals("African American")){
                        racePrcntage = currentCommunity.getInfo().getPrcntAfricanAmerican();
                    }else if(race.equals("Native American")){
                        racePrcntage = currentCommunity.getInfo().getPrcntNative();
                    }else if(race.equals("Asian American")){
                        racePrcntage = currentCommunity.getInfo().getPrcntAsian();
                    }else if(race.equals("White American")){
                        racePrcntage = currentCommunity.getInfo().getPrcntWhite();
                    }else if(race.equals("Hispanic American")){
                        racePrcntage = currentCommunity.getInfo().getPrcntHispanic();
                    }
                    
                    //Finding communities that are disavantaged and has specified percentage of race
                    if((100*racePrcntage) >= userPrcntage && currentCommunity.getInfo().getAdvantageStatus().equals("True")){
                        numDisComm += 1;
                    }

                    //moves to next community
                    currentCommunity = currentCommunity.getNext();
                }
                //Moves to next county
                currentCounty = currentCounty.getNext();
            }
            //Moves to next state
            currentState = currentState.getNext();
        }
        
        return numDisComm;
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as non disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int nonDisadvantagedCommunities ( double userPrcntage, String race ) {

        //Variable for counting number of nondisadvantaged communities
        int communities = 0;

        //Variable for holding percentage of race in each community
        double racePrcntage = 0;

        //Sets pointer at first state node
        StateNode currentState = firstState;
        
        //Going through each community from each county from each state
        //Iterating through state list
        while(currentState != null){

            //Sets pointer at county node
            CountyNode currentCounty = currentState.getDown();

            //Iterating through county list
            while(currentCounty != null){

                //Sets pointer at community node
                CommunityNode currentCommunity = currentCounty.getDown();

                //Iterating through community list
                while(currentCommunity != null){

                    //Finding percentage of given race
                    if(race.equals("African American")){
                        racePrcntage = 100 * currentCommunity.getInfo().getPrcntAfricanAmerican();
                    }else if(race.equals("Native American")){
                        racePrcntage = 100 * currentCommunity.getInfo().getPrcntNative();
                    }else if(race.equals("Asian American")){
                        racePrcntage = 100 * currentCommunity.getInfo().getPrcntAsian();
                    }else if(race.equals("White American")){
                        racePrcntage = 100 * currentCommunity.getInfo().getPrcntWhite();
                    }else if(race.equals("Hispanic American")){
                        racePrcntage = 100 * currentCommunity.getInfo().getPrcntHispanic();
                    }
                    
                    //Finding communities that are disavantaged and has specified percentage of race
                    if(racePrcntage >= userPrcntage && currentCommunity.getInfo().getAdvantageStatus().equals("False")){
                        communities += 1;
                    }

                    //moves to next community
                    currentCommunity = currentCommunity.getNext();
                }
                //Moves to next county
                currentCounty = currentCounty.getNext();
            }
            //Moves to next state
            currentState = currentState.getNext();
        }
        
        return communities;
    }
    
    /** 
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */ 
    public ArrayList<StateNode> statesPMLevels ( double PMlevel ) {

        //Creating arraylist item to hold states
        ArrayList<StateNode> abovePM = new ArrayList<>();
        
        //Sets pointer at first state
        StateNode currentState = firstState;

        //Iterating through state list
        while(currentState != null){
            //Sets pointer at county node
            CountyNode currentCounty = currentState.getDown();

            //Iterating through county list
            while(currentCounty != null){
                //Sets pointer at community node
                CommunityNode currentCommunity = currentCounty.getDown();

                //Iterating through community list
                while(currentCommunity != null){

                    //If a community in the state matches or exceeds PM level, the state is added to arraylist
                    if(currentCommunity.getInfo().getPMlevel() >= PMlevel){
                        abovePM.add(currentState);
                        break;
                    }

                    //Moves to next community
                    currentCommunity = currentCommunity.getNext();
                }

                //Avoid double counting states by breaking out of a county
                if(currentCounty.getDown().getInfo().getPMlevel() >= PMlevel){
                    break;
                }
                
                //Moves to next county
                currentCounty = currentCounty.getNext();
            }
            //Moves to next state
            currentState = currentState.getNext();
        }

        return abovePM;
    }

    /**
     * Given a percentage inputted by user, returns the number of communities 
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood ( double userPercntage ) {

        //Variable for counting number of communities at risk of flooding
        int riskFlood = 0;

        //Sets pointer at first state node
        StateNode currentState = firstState;
        
        //Going through each community from each county from each state
        //Iterating through state list
        while(currentState != null){

            //Sets pointer at county node
            CountyNode currentCounty = currentState.getDown();

            //Iterating through county list
            while(currentCounty != null){

                //Sets pointer at community node
                CommunityNode currentCommunity = currentCounty.getDown();

                //Iterating through community list
                while(currentCommunity != null){

                    //Finding communities that have a higher flood chance
                    if(currentCommunity.getInfo().getChanceOfFlood() >= userPercntage){
                        riskFlood += 1;
                    }

                    //moves to next community
                    currentCommunity = currentCommunity.getNext();
                }
                //Moves to next county
                currentCounty = currentCounty.getNext();
            }
            //Moves to next state
            currentState = currentState.getNext();
        }
        
        return riskFlood;
    }

    /** 
     * Given a state inputted by user, returns the communities with 
     * the 10 lowest incomes within said state.
     * 
     *  @param stateName the State to be analyzed
     *  @return the top 10 lowest income communities in the State, with no particular order
    */
    public ArrayList<CommunityNode> lowestIncomeCommunities ( String stateName ) {

        //Array list object to hold communities with the lowest 10 income within a state
        ArrayList<CommunityNode> lowIncomeComm = new ArrayList<>();

        //Pointer at first state node
        StateNode currentState = firstState;

        //Search and find state, moving pointer down to where state is
        while (currentState != null && !currentState.getName().equals(stateName)) {
            currentState = currentState.getNext();
        }

        
        //Going through each community from each county from each state
        //Iterating through state list
        

            //Sets pointer at county node
            CountyNode currentCounty = currentState.getDown();

            //Iterating through county list
            while(currentCounty != null){

                //Sets pointer at community node
                CommunityNode currentCommunity = currentCounty.getDown();

                //Iterating through community list
                while(currentCommunity != null){

                    //Ensuring only ten communities are added to array list
                    if(lowIncomeComm.size() < 10){
                        //Adding community to array list
                        lowIncomeComm.add(currentCommunity);

                        //Filtering and replacing community with the lowest poverty
                    }else{

                        //Variable holds value of poverty for the first community
                        double lowestPoverty = lowIncomeComm.get(0).getInfo().getPercentPovertyLine();
                        int indexOfLow = (int)Double.MIN_VALUE;

                        //For loop finding the lowest poverty rate community (richest)
                        for(int i = 0; i < lowIncomeComm.size(); i++){

                            //Finding the lowest poverty community within current array list
                            if(lowIncomeComm.get(i).getInfo().getPercentPovertyLine() < lowestPoverty){
                                lowestPoverty = lowIncomeComm.get(i).getInfo().getPercentPovertyLine();
                                indexOfLow = i;
                            }

                        }

                        if(currentCommunity.getInfo().getPercentPovertyLine() > lowestPoverty){
                            lowIncomeComm.set(indexOfLow, currentCommunity);
                        }

                    }
                    //moves to next community
                    currentCommunity = currentCommunity.getNext();
                }
                //Moves to next county
                currentCounty = currentCounty.getNext();
            }

        return lowIncomeComm;
    }
}
    
