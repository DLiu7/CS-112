import java.util.NoSuchElementException;

// NOTE that the API (Application Programming Interface) is the same

public class LinkedStackOfStrings {
    
    private StringNode first = null; // front of the list
    
    public boolean isEmpty() {  
        return first == null;  
    }
 
    // Running time? O(1)
    // Same as AddToFront
    // Can overflow happen with this implementation?
    public void push(String item) {
        StringNode oldFirst = first;

        first = new StringNode();
        first.item = item;
        first.next = oldFirst;
    }   
    
    // Running time? O(1)
    // Problems? Underflow
    public String pop() {

        // what happens when the stack is empty?
        if ( isEmpty() ) {
           throw new NoSuchElementException("Underflow - stack is empty");
        }
        String item = first.item; // item to send back to caller
        first = first.next; // first now points to the second StringNode
        return item;   
    }

    // Client
    public static void main (String[] args) {
        LinkedStackOfStrings s = new LinkedStackOfStrings();
        s.push("I");
        s.push("have");
        s.push("a");
        s.push("dream");
       
        String item = null;
        int n = 5;

        try {
            // code that might throw an exception
            while ( n > 0 ) {
                item = s.pop();
                System.out.println("Popped item: " + item);
                n--;
            } 
        } catch (NoSuchElementException exce) {
            // if exception is thrown, we catch it here
            // and handle
            System.out.println(exce.getMessage());
        }
    }
}
 