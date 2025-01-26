
// Generic Node class (linked list node)
// T is a placeholder for the data type.
// Data type is plugged in during compilation time
public class Node<T> {
 
    T item; // data part of the node
    Node<T> next; // link part of the node

    Node () {
        item = null;
        next = null;
    }
}

