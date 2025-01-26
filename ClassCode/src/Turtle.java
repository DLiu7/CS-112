import java.awt.Color; // let the compiler know we are instantiating an object of type Color
import edu.princeton.cs.algs4.*;
/* 
 * Abstract Data Type (ADT) Turtle
 * 
 * 
 * Blueprint for an object: attributes of the object and operations
 */
public class Turtle {
    
    /******** INSTANCE VARIABLES  *************/
    // Instance variables have an unique value to each object (instance of the class)
    // Private modifier hides the instance variable from other classes
    // (x,y) coordinate
    private double x;
    private double y;

    // orientation: direction in which the turtle is facing
    // degree
    private double angle;

    // turtle color
    private Color color;

    /******** CONSTRUCTOR *******/
    // Constructor have the same name as the class
    // Constructor create and initialize the object (initializing the instance variables)
    // Constructors have no return type
    // Default constructor: no argument constructor
    public Turtle () {
        x = y = 0.0;
        angle = 90.0;
        color = new Color (0,255,0); // green color
    }
    // 4 argument constructor
    public Turtle (double x, double y, double angle, Color color) {
        // the this keyword refers to the current object.
        // this.x means that we are accessing the x instance variable of the current object
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.color = color;
    }
    // Instance methods of the class Turtle
    // Instance methods DO NOT HAVE THE KEYWORD static: these methods are the operations of the data type
    // Instance methods manipulate (have access) to instance variables (even if they are private)

    /****** Accessor methods (get/set methods) ********/
    // Accessor methods allow other classes access to the private instance variables
    public double getX() {
        // methods within the class have access to the private instance variables
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public double getAngle () {
        return this.angle;
    }
    public Color getColor() {
        return this.color;
    }
    // We don't want to allow turtles to move unless they walk, so no setX, setY, setAngle

    // METHODS: operations on the object

    // turnLeft by rotating the turtle left
    public void turnLeft (double delta) {
        angle += delta;
        if ( angle > 360 ) {
            angle -= 360;
        }
    }

    // move the turtle forward ONE step (stepSize)
    // draw a line from old x,y to new x,y
    public void moveForward (double stepSize) {

        double oldX = x;
        double oldY = y;

        // compute the new x,y
        x += stepSize * Math.cos(Math.toRadians(angle)); 
        y += stepSize * Math.sin(Math.toRadians(angle));

        StdDraw.setPenColor(color);
        StdDraw.line(oldX, oldY, x, y);
    }

    // Returns the string representation of the object
    public String toString() {
        String str = "Turtle(" + x + "," + y + "), angle " + angle + ", " + color.toString();
        return str;
    }

    // equals method to compare this object with another one.
    // Object is a class in Java that all classes are derived from.
    public boolean equals (Object other) {

        if ( other instanceof Turtle ) { // ask other if it is of type Turtle.
            // proceed to compare because other is of type Turtle
            Turtle o = (Turtle)other; // cast the object to be of type Turtle

            boolean same = this.x == o.x && this.y == o.y && this.angle == o.angle &&
                           this.color.equals(o.color);
            return same;
        } else {
            // other is of another data type
            return false;
        }
    }

    // TEST CLIENT used to test the class and debug
    public static void main (String[] args) {

        Turtle t1 = new Turtle(); // default constructor
        Turtle t2 = new Turtle(0.5, 0.5, 45, new Color(255, 0, 0)); // 4-args constructor

        t2.moveForward(0.1);
        t1.turnLeft(310);
        t1.moveForward(0.2);

        StdOut.println(t1.toString());
        StdOut.println(t2.toString());
        StdOut.println(t1.equals(t2)); // the this. inside equals() refers to t1 in this line
    }
}
