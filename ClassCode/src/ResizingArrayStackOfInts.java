import edu.princeton.cs.algs4.*;

public class ResizingArrayStackOfInts {
    
    private int[] s; // data - stack
    private int t = 0;  // number of items in the stack, also the top
    
    public ResizingArrayStackOfInts()   {  
        s = new int[1]; //one array positon 
    }
    
    public boolean isEmpty() {  
        return t == 0;  
    } 
   
    /* 
     * Avoids overflow by doubling the size of the array
     * Two possibilities: 
     *     a) increase the size of the array by 1 at every push: infeasible as it becomes too expensive ~n^2
     *     b) double the size of the array whenever array is full: ~n [in amortized time analysis O(1)]
     *
     * Running time in terms of array accesses
     * Best case: O(1)
     * Worst case: 3n + 1 => ~3n => O(n)
     */
    public void push ( int item ) {  
        if ( t == s.length ) {
            // reached capacity, now resize
            //resize (s.length + 1 ); ~n^2
            resize (2 * s.length); // ~3n
        }
        s[t] = item;  
        t += 1;
    }  

    // Running time in terms of array accesses (tilde and big-oh): ~2n, O(n)
    private void resize ( int capacity ) {

        // new array with capacity
        int[] copy = new int[capacity];

        for (int i = 0; i < t; i++){
            copy[i] = s[i]; // 1 write + 1 read
        }
        // replace old array s with new array copy
        s = copy; // s and copy point to the same array after this line

        // garbage collector will get rid of the old array s
    }
   
    // Running time in terms of array accesses
    // Best case: O(1)
    // Worst case: 
    public int pop() {
        int item = s[t-1]; // Save the item to return to user
        s[t-1] = 0;        // Let garbage collector know that there 
                                 // are no references to the item that was just popped
        t -= 1;
        if ( t > 0 && t == s.length/4 ) {
            resize(s.length/2);
        }
        return item;
    }

    


    public static void main(String[] args) {

        // this is the client
        ResizingArrayStackOfStrings stack = new ResizingArrayStackOfStrings();

        System.out.print("Enter a string: ");
        while ( !StdIn.isEmpty() ) {
            System.out.print("Enter a string: ");
            stack.push(StdIn.readString());
        }

        System.out.println("All items pushed on the stack, pop from stack");

        while ( !stack.isEmpty() ) {
            System.out.println("\t" + stack.pop());
        }

        System.out.println("Stack empty");
    }
}
