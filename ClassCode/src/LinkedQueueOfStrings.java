
public class LinkedQueueOfStrings {
    
    private StringNode first; // reference to the first StringNode in the linked list
    private StringNode last;  // reference to the last StringNode in the linked list

    
    public boolean isEmpty() {
        return first == null;
    }
    
    // Running time? O(1)
    // enqueue at the end of the list
    public void enqueue(String item) {
        
        StringNode oldLast = last; // reference to current last StringNode
        
        last = new StringNode(); // instantiate a new StringNode object
        last.item = item;  // data
        last.next = null;  // next will point nowhere because it is the last StringNode

        if ( isEmpty() ) {
            // first StringNode to be added to the list
            first = last; 
        } else {
            oldLast.next = last;
        }           
    }
    
    // Running time? O(1)
    // dequeue from the front of the linked list
    public String dequeue() {

        String item = first.item; // save the item to return
        first       = first.next; // update first to point to the second item
        
        if (isEmpty()) { 
            last = null;    // last StringNode to be removed from the list
        }
        return item;
    }

    // Implement the print method
    
    public static void main (String[] args) {
        
    }
}
