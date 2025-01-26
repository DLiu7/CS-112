// Generic circular linked list
// T is a placeholder for the data type.
// Data type is plugged in during compilation time
public class CircularLinkedList<T> {
    
    // instance variable of the CLL
    private Node<T> last; // refers to the last node of the list
    private int size;     // number of nodes in the list

    // Constructor
    public CircularLinkedList () {
        last = null;
        size = 0;
    }

    // The data to be at the node inserted at the front
    // of the list.
    // Running time O(1)
    public void addToFront (T item) {
        Node<T> node = new Node<T>();
        node.item = item;

        if ( last == null ) {
            // list is empty
            node.next = node; // points to itself because it is the only node in the list
            last = node; // make last point to node
        } else {
            // list is not empty
            node.next = last.next;
            last.next = node; // adding to the front;
        }
        size += 1;
    }

    // Running time: O(1)
    public T removeFront () {

        if ( last == null ) {
            // empty
            return null; // there was nothing in the list
         } else if ( last == last.next ) {
            // one item list
            T item = last.item;
            last = null;
            size = 0;
            return item;
        } else {
            // more than one item
            T item = last.next.item;
            last.next = last.next.next;
            size -= 1;
            return item;
        }
    }

    // Worst-case running time for success: O(n)
    // scenario? target is at the last node of the list
    public boolean search (T target) {

        Node<T> ptr = last.next; // ptr is at the first node

        do {
            if ( ptr.item.equals(target)) {
                return true;
            } else {
                ptr = ptr.next;
            }

        } while ( ptr != last.next );

        // traverse the entire list and didn't find target
        return false;
    }

    public void print () {

    }

    // CLient, tester, driver
    public static void main (String[] args) {

        // cats is the reference name to the CLL
        CircularLinkedList<String> cats = new CircularLinkedList<String>();
        cats.addToFront("Ploc");
        cats.addToFront("Flo");
        cats.addToFront("Lilo");
        cats.print();
    }
    
}
