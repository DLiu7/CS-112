import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.*;

/*
 * Allows duplicate items
 * Allows null items
 */
public class FixedCapacityStackOfStrings {
   
    private String[] s; // Stack
    private int t = 0;  // number of items on the stack and ALSO the top of the stack
    
    // constructor
    public FixedCapacityStackOfStrings(int capacity)   {  
        s = new String[capacity]; 
        // all positions will be null
    }
    
    public boolean isEmpty() {  
        return t == 0;  
    } 
   
    // 1.Running time (count array access)?
    // 2.Problems? overflow
    public void push(String item) {  

        if ( t == s.length ) {
            // stack is full => overflow because t is out of the bounds of the array
            // resize: look at ResizingArrayStackOfStrings.java
            return; // cannot add
        }

        s[t] = item; // 1 array write
        t += 1;
    }  
   
    // 1. Running time? 
    // 2. Problem? Underflow - stack is empty
    // 3. loitering: holding a reference to an object that is no longer needed
    public String pop() {

        if ( isEmpty() ) {
            // what happens if the stack is empty? stack underflow
            // do nothing or throw an exception
            return;
        }

        t -= 1;
        String item = s[t]; // save the item to return to user
        s[t] = null;        // IMPORTANT to remove the reference to avoid loitering
        return item;
    }



    // CLIENT code
    public static void main(String[] args) {

        FixedCapacityStackOfStrings stack = new FixedCapacityStackOfStrings(4);

        StdOut.print("Enter items to be pushed onto the stack: ");
        while ( !StdIn.isEmpty() ) {
            stack.push(StdIn.readString());
        }

        System.out.println("All items pushed on the stack, pop from stack");

        while ( !stack.isEmpty() ) {
            System.out.println("\t" + stack.pop());
        }

        System.out.println("Stack empty");
    }
}
