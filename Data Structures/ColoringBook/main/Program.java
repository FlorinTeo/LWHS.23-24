package main;
import java.awt.Color;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import graphics.DrawingFrame;
import graphics.Drawing;

public class Program {
    
    /**
     * Global static fields for the Drawing object being worked on
     * and the DrawingFrame containing and displaying it.
     */
    private static Drawing _drawing;
    private static DrawingFrame _frame;
    
    /**
     * The unit of work for the flooding algorithm is the individual pixel (x,y)
     * that needs to be colored. Processing this work item may generate other
     * work items, for the un-colored neighbors of this pixel.
     */
    public static class WorkItem {
        public int _x;
        public int _y;
        public WorkItem(int x, int y) {
            _x = x;
            _y = y;
        }
    };
    
    /**
     * Iterative implementation of the flooding algorithm, by using a Queue.
     * This is a <b>breath-first</b> algorithm: flooding is slower but using small
     * anmount of memory, working on the perimeter of the already colored area.
     * @param xSeed - the initial x-coordinate where the flooding starts from
     * @param ySeed - the initial y-coordinate where the flooding starts from
     * @param col - the color to use for flooding.
     * @throws InterruptedException
     */
    public static void floodQ(int xSeed, int ySeed, Color col) throws InterruptedException {
        // Create the queue, and the first work items.
        // Color this first pixel then add its work item into the queue
        Queue<WorkItem> queue = new LinkedList<WorkItem>();
        WorkItem work = new WorkItem(xSeed, ySeed);
        _drawing.setPixel(work._x, work._y, col);
        queue.add(work);
        
        // keep removing work items from the queue for as long as there's work to do
        while (!queue.isEmpty()) {
            _frame.step(1);
            _frame.setStatusMessage(String.format("%d",queue.size()));
            
            // remove the work item from the queue and execute on it:
            // analyze each of the neighbors of this pixel and if that neighbor is within
            // image bounds and needs coloring, color it, then add a new work item for it
            // into the queue.
            work = queue.remove();
            for(int x = work._x - 1; x <= work._x + 1; x ++) {
                for (int y = work._y - 1; y <= work._y + 1; y++) {
                    if (x == xSeed && y == ySeed) {
                        continue;
                    }
                    if (_drawing.isValidPixel(x, y) && _drawing.isBrightPixel(x, y)) {
                        _drawing.setPixel(x, y, col);
                        queue.add(new WorkItem(x, y));
                    }
                }
            }
        }
    }
    
    /**
     * Iterative implementation of the flooding algorithm, by using a Stack.
     * This is a <b>depth-first</b> algorithm: it makes deeper and faster inroads in the
     * uncolored areas, but uses more memory to keep track of the path taken already.
     * It is very similiar to the recursive algorithm, but by using a Stack it avoids
     * hitting StackOverflowException since it is using heap memory.
     * @param xSeed - the initial x-coordinate where the flooding starts from
     * @param ySeed - the initial y-coordinate where the flooding starts from
     * @param col - the color to use for flooding.
     * @throws InterruptedException
     */
    public static void floodS(int xSeed, int ySeed, Color col) throws InterruptedException {
        // Create the queue, and the first work items.
        // Color this first pixel then push its work item to the stack
        Stack<WorkItem> stack = new Stack<WorkItem>();
        WorkItem work = new WorkItem(xSeed, ySeed);
        _drawing.setPixel(work._x, work._y, col);
        stack.push(work);
        
        // keep extracting work items from the stack for as long as there's work to do
        while (!stack.empty()) {
            _frame.step(1);
            _frame.setStatusMessage(String.format("%d",stack.size()));
            
            // pop the work item from the stack and execute on it:
            // analyze each of the neighbors of this pixel and if that neighbor is within
            // image bounds and needs coloring, color it, then push a new work item for it
            // onto the stack.
            work = stack.pop();
            for(int x = work._x - 1; x <= work._x + 1; x ++) {
                for (int y = work._y - 1; y <= work._y + 1; y++) {
                    if (x == xSeed && y == ySeed) {
                        continue;
                    }
                    if (_drawing.isValidPixel(x, y) && _drawing.isBrightPixel(x, y)) {
                        _drawing.setPixel(x, y, col);
                        stack.push(new WorkItem(x, y));
                    }
                }
            }
        }
    }
    
    /**
     * Recursive implementation of the flooding algorithm.
     * @param xSeed - the initial x-coordinate where the flooding starts from
     * @param ySeed - the initial y-coordinate where the flooding starts from
     * @param col - the color to use for flooding.
     * @throws InterruptedException
     */
    public static void floodR(int xSeed, int ySeed, Color col) throws InterruptedException {
        // first and foremost, the pixel at xSeed, ySeed needs coloring
        _drawing.setPixel(xSeed, ySeed, col);
        _frame.step(1);
        
        // then we can inspect each of its neighbors, and if the neighbor is within the
        // bounds of the image and needs coloring, we'll recursively ask that pixel to
        // color itself and propagate the flooding further to its un-colored neighbors.
        for (int x = xSeed - 1; x <= xSeed + 1; x++) {
            for (int y = ySeed - 1; y <= ySeed + 1; y++) {
                if (x == xSeed && y == ySeed) {
                    continue;
                }
                if (_drawing.isValidPixel(x, y) && _drawing.isBrightPixel(x, y)) {
                    floodR(x, y, col);
                }
            }
        }
    }
    
    /**
     * Demonstrates a simple alteration to the drawing:
     * On a square section of the image, from top-left: (40,30) to bottom-right (140, 130)
     * replace the dark pixels with yellow and the bright pixels with yellow.
     */
    public static void paint() throws InterruptedException {
        for(int x = 40; x < 140; x++) {
            for (int y = 30; y < 130; y++) {
                _frame.step();
                if (_drawing.isDarkPixel(x, y)) {
                    _drawing.setPixel(x, y, Color.yellow);
                } else {
                    _drawing.setPixel(x, y, Color.red);
                }
            }
        }
        _frame.stop();
        floodS(256, 274, Color.orange.darker());
        _frame.stop();
        floodQ(407, 222, Color.orange);
        _frame.stop();
        floodR(481, 295, Color.green.brighter());
        _frame.stop();
        floodS(56, 165, Color.pink);
        _frame.stop();
        floodQ(406,395, Color.cyan);
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
        _drawing = new Drawing("ColoringBook/drawings/abstract.jpg");
        
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
