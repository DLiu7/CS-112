import java.util.*;
import edu.princeton.cs.algs4.*;

public class ResizingArrayQueueOfStrings {
    
    // initial capacity of underlying resizing array
    private static final int INIT_CAPACITY = 8;

    private String[] q;       // queue elements
    private int size;       // number of elements on queue
    private int first;      // index of first element of queue
    private int last;       // index of next available slot


    /**
     * Initializes an empty queue.
     */
    public ResizingArrayQueueOfStrings() {
        q = new String[INIT_CAPACITY];
        size = 0;
        first = 0; // head
        last = 0; // tail
    }

    /**
     * Is this queue empty?
     * @return true if this queue is empty; false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of items in this queue.
     * @return the number of items in this queue
     */
    public int size() {
        return size;
    }

    /**
     * Running time?
     * Adds the item to this queue.
     * @param item the item to add
     */
    public void enqueue(String item) {
        // double size of array if necessary and recopy to front of array
        if (size == q.length) {
            resize(2*q.length);   // double size of array if necessary ~2n
        }
        q[last] = item;           // add item
        last += 1;
        if (last == q.length) {
            last = 0;             // wrap-around
        }
        size++; // added 1 item
    }

    // resize the underlying array
    // running time? T(n) = ~2n => O(n)
    private void resize(int capacity) {
        String[] copy = new String[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = q[(first + i) % q.length];
        }
        q = copy;
        first = 0;
        last  = size;
    }

    /**
     * Running time?
     * Removes and returns the item on this queue that was least recently added.
     * @return the item on this queue that was least recently added
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    public String dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }
        
        String item = q[first];
        q[first] = null;                            // to avoid loitering
        size -= 1;
        first += 1;

        if (first == q.length) {
            first = 0;           // wrap-around
        }

        // shrink size of array if necessary
        if (size > 0 && size == q.length/4) {
            resize(q.length/2); 
        }
        return item;
    }

    /**
     * Returns the item least recently added to this queue.
     * @return the item least recently added to this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    public String peek() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return q[first];
    }

   /**
     * Unit tests the {@code ResizingArrayQueue} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        ResizingArrayQueueOfStrings queue = new ResizingArrayQueueOfStrings();
        
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) { 
                queue.enqueue(item);
            } else if (!queue.isEmpty()) {
                StdOut.print(queue.dequeue() + " ");
            }
        }
        StdOut.println("(" + queue.size() + " left on queue)");
    }

}
