public class StringNode {
    
    // Public instance variable because we want to manipulate the values
    // from other classes
    String item;  // data part
    StringNode next; // link part (reference to the next node in the linked list)

    StringNode () {
        item = null;
        next = null;
    }
}
