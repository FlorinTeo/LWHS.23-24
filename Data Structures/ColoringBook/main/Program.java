package ColoringBook.main;
import java.awt.Color;
import java.io.IOException;

import ColoringBook.graphics.DrawingFrame;
import ColoringBook.graphics.Drawing;

public class Program {
    
    /**
     * Global static fields for the Drawing object being worked on
     * and the DrawingFrame containing and displaying it.
     */
    private static Drawing _drawing;
    private static DrawingFrame _frame;
    
    /**
     * Demonstrates a simple alteration to the drawing:
     * On a square section of the image, from top-left: (40,30) to bottom-right (140, 130)
     * replace the dark pixels with yellow and the bright pixels with yellow.
     */
    public static void paint() throws InterruptedException {
        for(int x = 40; x < 140; x++) {
            for (int y = 30; y < 130; y++) {
                _frame.step(1);
                if (_drawing.isDarkPixel(x, y)) {
                    _drawing.setPixel(x, y, Color.yellow);
                } else {
                    _drawing.setPixel(x, y, Color.red);
                }
            }
        }
    }
    
    /**
     * Main entry point in the program:
     * Initializes the static Drawing (_drawing) with an image of your choice,
     * then initializes the static DrawingFrame (_frame) loading into it the new drawing.
     * Subsequently the frame is opened on the screen then the drawing is painted upon
     * and displayed as it is being modified before the program terminates. 
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Welcome to the Coloring Festival!");
        
        // pick a drawing
        _drawing = new Drawing("ColoringBook/drawings/bird.jpg");
        
        // put it in a frame
        _frame = new DrawingFrame(_drawing);

        // put the frame on display and stop to admire it.
        _frame.open();
        _frame.step();
        
        // make some change to the drawing, then stop for applause.
        paint();
        _frame.stop();
        
        // the show is over.
        //_frame.close();
        
        System.out.println("Well done, goodbye!");
    }
}
