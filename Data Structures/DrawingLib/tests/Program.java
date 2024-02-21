package DrawingLib.tests;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import drawing.Drawing;
import drawing.DrawingFrame;

public class Program {
    
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("DrawingLib testing code!");
        File drwFile = new File("DrawingLib/tests/test.jpg");
        BufferedImage drwImg = ImageIO.read(drwFile);
        Drawing drw = new Drawing(drwImg);
        DrawingFrame drwFrame = new DrawingFrame(drw);
        drwFrame.open();
        System.out.println("Pausing");
        drwFrame.step();
        for(int i = 0; i < 10; i++) {
            System.out.println(i);
            drwFrame.step(1000);
        }
        System.out.println("Stopping");
        drwFrame.stop();
        drwFrame.close();
        System.out.println("Goodbye!");
    }
}
