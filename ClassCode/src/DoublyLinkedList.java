// Generic DLL class (doublye linked list )
// T is a placeholder for the data type.
// Data type is plugged in during compilation time
public class DoublyLinkedList<T> {
    
    // Private nested class - USED INSIDE the doubly linked list class
    // We only a nested class when the nested is used BY ONLY
    // the DoublyLinkedList class.
    private class DLLNode<T> {
 
        T item;          // data part of the node
        DLLNode<T> next; // link to the next node in the list
        DLLNode<T> prev; // link to the previous node in the list
    
        DLLNode () {
            item = null;
            next = null;
            prev = null;
        }
    }

    private DLLNode<T> front; // reference to the front of the list
    private int size;         // number of nodes in the list

    public DoublyLinkedList() {
        front = null;
        size = 0;
    }

}
