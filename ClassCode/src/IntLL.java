// Linked list implementation
public class IntLL {
    
    IntNode front; // reference (address the of the object) to the front node of the LL

    IntLL () {
        front = null; // initializing to an empty LL
    }

    // Running time?
    // Count each operation: there are 4 operations => O(1)
    void addToFront (int newDataItem) {

        // 1. create (instantiate) the new node
        IntNode newNode = new IntNode();
        newNode.item = newDataItem;

        // 2. make newNode point to where front is pointing to (refering to)
        newNode.next = front;

        // 3. update front to point to the newly created node
        // newNode becomes the first node of the linked list
        // the node that was the first node is now the second node.
        front = newNode;
    }

    // search for target which is an integer. 
    // if returns false then target is not in the list
    // if returns true then target is in the list
    // Running time for success? 
    // Best case:  the target is at the first node. O(1)
    // Worst case: the target is at the last node. O(n) 
    boolean search (int target) {

        // ptr.item => the data part of the node
        // ptr.next => the link part of the node

        IntNode ptr = front;
        while ( ptr != null && ptr.item != target ) { // count this towards the running time
            ptr = ptr.next;                              
        }
        // Here: ptr is null (target was NOT FOUND) OR 
        // ptr points to the node that contains target

        if ( ptr == null ) {
            return false;
        } else {
            return true;
        }
    }

    // Running time for a list of n items: O(n)
    void print () {

        IntNode ptr = front;
        while ( ptr != null ) {
            System.out.println(ptr.item);
            ptr = ptr.next;               // make ptr point to the next node in the sequence
        }
        // if we get here then ptr is pointing to null

        // for loop instead using ptr2
        for ( IntNode ptr2 = front; ptr2 != null; ptr2 = ptr2.next ) {
            System.out.println(ptr2.item);
        }
    }

    // Inserting a new node that contains itemToInsert after the node 
    // that contains target.
    // Returns true is node was 
    boolean addAfter (int itemToInsert, int target) {

        // 1. search for target
        IntNode ptr = front;
        while ( ptr != null && ptr.item != target ) {
            ptr = ptr.next;
        }

        // 2. target is not found
        if ( ptr == null ) {
            return false; // cannot insert
        } else {

            // 3. target is present
            // ptr is pointing to the node containing target
            IntNode newNode = new IntNode(); // create new node
            newNode.item = itemToInsert;     // add value itemToInsert
            newNode.next = ptr.next; // newNode points to the node after ptr
            ptr.next = newNode;      // the node containing target will point to the newNode
            return true;
        }
    }

    /*
     * REmove the node containing the item target from the list
     */
    boolean delete (int target) {

        IntNode ptr = front;
        IntNode prev = null;

        // traverse list until ptr finds the target node
        while ( ptr != null && ptr.item != target ) {
            // move the two references
            prev = ptr; // make prev refer to the same node ptr is referring to
            ptr = ptr.next; // move ptr one node ahead
        }

        if ( ptr == null ) {
            // target not found
            return false;
        } else if ( ptr == front ) {
            // remvove the first node by updating the front reference
            front = front.next;
            return true;
        } else {
            // ptr is pointing to the node that contains target
            // prev is pointing to the previous to that node
            // this line removes the link from prev to ptr
            // prev jumps ptr
            prev.next = ptr.next;
            return true;
        }
        }
    }

    public static void main (String[] args) {

        IntLL linkedList = new IntLL();
        linkedList.addToFront(1);
        linkedList.addToFront(3);
        linkedList.addToFront(5);
        linkedList.addToFront(7);
        linkedList.print();
        linkedList.addAfter(4,5);
        linkedList.print();
        linkedList.delete(4);
        linkedList.print();
    }
}
