import java.awt.Color;
import edu.princeton.cs.algs4.*;

public class DrunkenTurtles {
    
    // client program
    public static void main (String[] args) {

        StdOut.print("Enter the sanctuary number of turtles: ");
        int numberOfTurtles = StdIn.readInt();

        StdOut.print("Enter the number of steps for the turtles take: ");
        int numberOfSteps = StdIn.readInt();

        StdOut.print("Enter the step size for the turtles (all turtle are of the same size): ");
        double stepSize = StdIn.readDouble();

        // allocate enough space to hold all numberOfTurtles turtles
        // allocates the array to hold numberOfTurtles
        // turtles is a reference to an array
        Turtle[] turtles = new Turtle[numberOfTurtles];

        // instantiate the turtles object
        // each turtle is one object, each turtle is in one array index
        // Running time (assume we have t turtles)
            // f(n) =
	    //     tilde notation:
	    //     big O notation:
        for ( int i = 0; i < turtles.length; i++ ) {
            
            // choose a random position
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);

            // choose a random color
            int red = StdRandom.uniform(256);
            int green = StdRandom.uniform(256); // [0,255]
            int blue = StdRandom.uniform(256);
            Color color = new Color(red,green,blue); // instantiate a Color object

            // Instantiate the turtle object - object of type Turtle
            // Store the reference to the Turtle object at index i
            turtles[i] = new Turtle(x,y,0.0,color);
        }

        StdDraw.setXscale(-1, 5);
        StdDraw.setYscale(-1, 5);

	// make each turtle take ONE step at a time for a total of numberOfSteps
	// Running time
	//     tilde notation:
	//     big O notation:
        for ( int i = 1; i <= numberOfSteps; i++ ) { // s
        
            // traverse the entire array to:
            // 1. make each turtle turn left by a random angle
            // 2. make each turtle take 1 step 
            // running time: f(n) = 
            for ( int j = 0; j < turtles.length; j++ ) { // t

                double angle = StdRandom.uniform(0.0, 361.0);
                turtles[j].turnLeft(angle);       // read
                turtles[j].moveForward(stepSize); // read
            }
        }
    }
}
