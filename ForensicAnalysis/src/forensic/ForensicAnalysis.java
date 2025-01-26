package forensic;
import java.util.Queue;
import java.util.LinkedList;

/**
 * This class represents a forensic analysis system that manages DNA data using
 * BSTs.
 * Contains methods to create, read, update, delete, and flag profiles.
 * 
 * @author Kal Pandit
 */
public class ForensicAnalysis {

    private TreeNode treeRoot;            // BST's root
    private String firstUnknownSequence;
    private String secondUnknownSequence;

    public ForensicAnalysis () {
        treeRoot = null;
        firstUnknownSequence = null;
        secondUnknownSequence = null;
    }

    /**
     * Builds a simplified forensic analysis database as a BST and populates unknown sequences.
     * The input file is formatted as follows:
     * 1. one line containing the number of people in the database, say p
     * 2. one line containing first unknown sequence
     * 3. one line containing second unknown sequence
     * 2. for each person (p), this method:
     * - reads the person's name
     * - calls buildSingleProfile to return a single profile.
     * - calls insertPerson on the profile built to insert into BST.
     *      Use the BST insertion algorithm from class to insert.
     * 
     * DO NOT EDIT this method, IMPLEMENT buildSingleProfile and insertPerson.
     * 
     * @param filename the name of the file to read from
     */
    public void buildTree(String filename) {
        // DO NOT EDIT THIS CODE
        StdIn.setFile(filename); // DO NOT remove this line

        // Reads unknown sequences
        String sequence1 = StdIn.readLine();
        firstUnknownSequence = sequence1;
        String sequence2 = StdIn.readLine();
        secondUnknownSequence = sequence2;
        
        int numberOfPeople = Integer.parseInt(StdIn.readLine()); 

        for (int i = 0; i < numberOfPeople; i++) {
            // Reads name, count of STRs
            String fname = StdIn.readString();
            String lname = StdIn.readString();
            String fullName = lname + ", " + fname;
            // Calls buildSingleProfile to create
            Profile profileToAdd = createSingleProfile();
            // Calls insertPerson on that profile: inserts a key-value pair (name, profile)
            insertPerson(fullName, profileToAdd);
        }
    }

    /** 
     * Reads ONE profile from input file and returns a new Profile.
     * Do not add a StdIn.setFile statement, that is done for you in buildTree.
    */
    public Profile createSingleProfile() {

        int numOfSTRs = Integer.parseInt(StdIn.readString());
        STR[] STRArray = new STR [numOfSTRs];

        for (int i = 0; i < numOfSTRs; i++) {
            String strName = StdIn.readString();
            int strOccur = Integer.parseInt(StdIn.readString());
            
            STR object = new STR(strName, strOccur);

            STRArray[i] = object;
        }

        Profile newProfile = new Profile(STRArray);

        return newProfile;
    }

    /**
     * Inserts a node with a new (key, value) pair into
     * the binary search tree rooted at treeRoot.
     * 
     * Names are the keys, Profiles are the values.
     * USE the compareTo method on keys.
     * 
     * @param newProfile the profile to be inserted
     */
    public void insertPerson(String name, Profile newProfile) {
        
        //Ensuring no null in puts
        if(name == null || newProfile == null){
            return;
        }

        //If tree is empty, create a new node
        if(treeRoot == null){
            treeRoot = new TreeNode(name, newProfile, null, null);
            return;
        }

        //Sets a pointer at root node
        TreeNode currentNode = treeRoot;

        //Traversing BST and compares names along the way. If the name exists, ignore, but if name does not exist, create a new node.
        while(currentNode != null){
            //Compares lexigraphically, alphabetically, in order
            int compareName = name.compareTo(currentNode.getName());

            if (compareName < 0) {
                // Move to the left subtree
                if (currentNode.getLeft() == null) {
                    currentNode.setLeft(new TreeNode(name, newProfile, null, null));
                    return;
                }
                currentNode = currentNode.getLeft();
            } else if (compareName > 0) {
                // Move to the left subtree
                if (currentNode.getRight() == null) {
                    currentNode.setRight(new TreeNode(name, newProfile, null, null));
                    return;
                }
                currentNode = currentNode.getRight();
            } else {
                // Names are equal, handle duplicate or update existing profile
                currentNode.setProfile(newProfile);
                return;
            }
        }

    }

    /**
     * Finds the number of profiles in the BST whose interest status matches
     * isOfInterest.
     *
     * @param isOfInterest the search mode: whether we are searching for unmarked or
     *                     marked profiles. true if yes, false otherwise
     * @return the number of profiles according to the search mode marked
     */
    public int getMatchingProfileCount(boolean isOfInterest) {
        
        if (treeRoot == null) {
            return 0;
        }
    
        TreeNode currentNode = treeRoot;
        int counter = 0;
    
        while (currentNode != null) {
            if (currentNode.getRight() == null) {
                if (currentNode.getProfile() != null && currentNode.getProfile().getMarkedStatus() == isOfInterest) {
                    counter++;
                }
                currentNode = currentNode.getLeft();
            } else {
                TreeNode next = currentNode.getRight();
    
                while (next.getLeft() != null && next.getLeft() != currentNode) {
                    next = next.getLeft();
                }
    
                if (next.getLeft() == null) {
                    next.setLeft(currentNode);
                    currentNode = currentNode.getRight();
                } else {
                    next.setLeft(null);
                    if (currentNode.getProfile() != null && currentNode.getProfile().getMarkedStatus() == isOfInterest) {
                        counter++;
                    }
                    currentNode = currentNode.getLeft();
                }
            }
        }
    
        return counter;
    }

    /**
     * Helper method that counts the # of STR occurrences in a sequence.
     * Provided method - DO NOT UPDATE.
     * 
     * @param sequence the sequence to search
     * @param STR      the STR to count occurrences of
     * @return the number of times STR appears in sequence
     */
    private int numberOfOccurrences(String sequence, String STR) {
        
        // DO NOT EDIT THIS CODE
        
        int repeats = 0;
        // STRs can't be greater than a sequence
        if (STR.length() > sequence.length())
            return 0;
        
            // indexOf returns the first index of STR in sequence, -1 if not found
        int lastOccurrence = sequence.indexOf(STR);
        
        while (lastOccurrence != -1) {
            repeats++;
            // Move start index beyond the last found occurrence
            lastOccurrence = sequence.indexOf(STR, lastOccurrence + STR.length());
        }
        return repeats;
    }

    /**
     * Traverses the BST at treeRoot to mark profiles if:
     * - For each STR in profile STRs: at least half of STR occurrences match (round
     * UP)
     * - If occurrences THROUGHOUT DNA (first + second sequence combined) matches
     * occurrences, add a match
     */
    public void flagProfilesOfInterest() {

        TreeNode current = treeRoot;
        TreeNode prev;

        if (treeRoot == null) {
            return;
        }
    
        while (current != null) {
            if (current.getLeft() == null) {
                Profile profile = current.getProfile();
                STR[] STRs = profile.getStrs();
                int totalSTRs = STRs.length;
                int requiredMatches = (totalSTRs + 1) / 2;
                int profileMatches = 0;
    
                for (STR str : STRs) {
                    int currentOcc = str.getOccurrences();
                    int allOcc = numberOfOccurrences(firstUnknownSequence + secondUnknownSequence, str.getStrString());
    
                    if (currentOcc == allOcc) {
                        profileMatches++;
                    }
                }
    
                if (profileMatches >= requiredMatches) {
                    profile.setInterestStatus(true);
                }
    
                current = current.getRight();
            } else {
                // Find the predecessor of the current node
                prev = current.getLeft();
                while (prev.getRight() != null && prev.getRight() != current) {
                    prev = prev.getRight();
                }
    
                if (prev.getRight() == null) {
                    // Establish the link for traversal and move to the left subtree
                    prev.setRight(current);
                    current = current.getLeft();
                } else {
                    // Revert the link and process the current node
                    prev.setRight(null);
    
                    Profile profile = current.getProfile();
                    STR[] STRs = profile.getStrs();
                    int totalSTRs = STRs.length;
                    int requiredMatches = (totalSTRs + 1) / 2;
                    int profileMatches = 0;
    
                    for (int i = 0; i < STRs.length; i++) {
                        STR str = STRs[i];
                        int currentOcc = str.getOccurrences();
                        int allOcc = numberOfOccurrences(firstUnknownSequence + secondUnknownSequence, str.getStrString());
                    
                        if (currentOcc == allOcc) {
                            profileMatches++;
                        }
                    }
    
                    if (profileMatches >= requiredMatches) {
                        profile.setInterestStatus(true);
                    }
    
                    current = current.getRight();
                }
            }
        }
    }

    /**
     * Uses a level-order traversal to populate an array of unmarked Strings representing unmarked people's names.
     * - USE the getMatchingProfileCount method to get the resulting array length.
     * - USE the provided Queue class to investigate a node and enqueue its
     * neighbors.
     * 
     * @return the array of unmarked people
     */
    public String[] getUnmarkedPeople() {
        int unmarkCount = getMatchingProfileCount(false);
        String[] unmarkedPeople = new String[unmarkCount];
        Queue<TreeNode> queue = new LinkedList<>();
        
        if (treeRoot != null) {
            queue.add(treeRoot);
            
            int index = 0;
            
            while (!queue.isEmpty()) {
                TreeNode node = queue.peek();
                
                if (node != null && node.getProfile() != null && !node.getProfile().getMarkedStatus()) {
                    unmarkedPeople[index++] = node.getName();
                }

                queue.poll();
                
                if (node != null && node.getLeft() != null) {
                    queue.add(node.getLeft());
                }
                if (node != null && node.getRight() != null) {
                    queue.add(node.getRight());
                }
            }
        }
        
        return unmarkedPeople;
    }

    /**
     * Removes a SINGLE node from the BST rooted at treeRoot, given a full name (Last, First)
     * This is similar to the BST delete we have seen in class.
     * 
     * If a profile containing fullName doesn't exist, do nothing.
     * You may assume that all names are distinct.
     * 
     * @param fullName the full name of the person to delete
     */
    public void removePerson(String fullName) {
        TreeNode current = treeRoot;
        TreeNode parent = null;
        boolean isLeftChild = false;
    
        while (current != null) {
            int cmp = fullName.compareTo(current.getName());
            if (cmp == 0) {
                break;
            } else {
                parent = current;
                if (cmp < 0) {
                    current = current.getLeft();
                    isLeftChild = true;
                } else {
                    current = current.getRight();
                    isLeftChild = false;
                }
            }
        }
    
        if (current == null) {
            return;
        }
    
        if (current.getLeft() == null && current.getRight() == null) {
            if (current == treeRoot) {
                treeRoot = null;
            } else if (isLeftChild) {
                parent.setLeft(null);
            } else {
                parent.setRight(null);
            }
        } else if (current.getLeft() == null) {
            if (current == treeRoot) {
                treeRoot = current.getRight();
            } else if (isLeftChild) {
                parent.setLeft(current.getRight());
            } else {
                parent.setRight(current.getRight());
            }
        } else if (current.getRight() == null) {
            if (current == treeRoot) {
                treeRoot = current.getLeft();
            } else if (isLeftChild) {
                parent.setLeft(current.getLeft());
            } else {
                parent.setRight(current.getLeft());
            }
        } else {
            TreeNode successor = getSuccessor(current);
            current.setName(successor.getName());
            current.setProfile(successor.getProfile());
            removeNode(successor);
        }
    }
    
    private TreeNode getSuccessor(TreeNode node) {
        TreeNode successorParent = node;
        TreeNode successor = node.getRight();
        TreeNode current = successor;
    
        while (current != null) {
            successorParent = successor;
            successor = current;
            current = current.getLeft();
        }
    
        if (successor != node.getRight()) {
            successorParent.setLeft(successor.getRight());
            successor.setRight(node.getRight());
        }
    
        return successor;
    }
    
    private void removeNode(TreeNode node) {
        TreeNode parent = null;
        TreeNode current = treeRoot;
    
        while (current != null && current != node) {
            parent = current;
            if (node.getName().compareTo(current.getName()) < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
    
        if (parent == null) {
            treeRoot = null;
        } else if (node == parent.getLeft()) {
            parent.setLeft(null);
        } else {
            parent.setRight(null);
        }
    }


    /**
     * Clean up the tree by using previously written methods to remove unmarked
     * profiles.
     * Requires the use of getUnmarkedPeople and removePerson.
     */
    public void cleanupTree() {
        String[] unmarkedPeople = getUnmarkedPeople();
        
        for (String person : unmarkedPeople) {
            TreeNode node = findNode(treeRoot, person);
            if (node != null && node.getProfile() != null && !node.getProfile().getMarkedStatus()) {
                treeRoot = removeNode(treeRoot, person);
            }
        }
    }
    
    private TreeNode findNode(TreeNode current, String fullName) {
        while (current != null) {
            int cmp = fullName.compareTo(current.getName());
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        return null;
    }
    
    private TreeNode removeNode(TreeNode root, String fullName) {
        if (root == null) {
            return null;
        }
    
        int cmp = fullName.compareTo(root.getName());
        if (cmp < 0) {
            root.setLeft(removeNode(root.getLeft(), fullName));
        } else if (cmp > 0) {
            root.setRight(removeNode(root.getRight(), fullName));
        } else {
            if (root.getLeft() == null) {
                return root.getRight();
            } else if (root.getRight() == null) {
                return root.getLeft();
            }
    
            TreeNode successor = findMinNode(root.getRight());
            root.setName(successor.getName());
            root.setProfile(successor.getProfile());
            root.setRight(removeNode(root.getRight(), successor.getName()));
        }
    
        return root;
    }
    
    private TreeNode findMinNode(TreeNode node) {
        if (node == null) {
            return null;
        }
    
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    /**
     * Gets the root of the binary search tree.
     *
     * @return The root of the binary search tree.
     */
    public TreeNode getTreeRoot() {
        return treeRoot;
    }

    /**
     * Sets the root of the binary search tree.
     *
     * @param newRoot The new root of the binary search tree.
     */
    public void setTreeRoot(TreeNode newRoot) {
        treeRoot = newRoot;
    }

    /**
     * Gets the first unknown sequence.
     * 
     * @return the first unknown sequence.
     */
    public String getFirstUnknownSequence() {
        return firstUnknownSequence;
    }

    /**
     * Sets the first unknown sequence.
     * 
     * @param newFirst the value to set.
     */
    public void setFirstUnknownSequence(String newFirst) {
        firstUnknownSequence = newFirst;
    }

    /**
     * Gets the second unknown sequence.
     * 
     * @return the second unknown sequence.
     */
    public String getSecondUnknownSequence() {
        return secondUnknownSequence;
    }

    /**
     * Sets the second unknown sequence.
     * 
     * @param newSecond the value to set.
     */
    public void setSecondUnknownSequence(String newSecond) {
        secondUnknownSequence = newSecond;
    }

}
