package TrafficFlow.main;
import java.awt.event.KeyEvent;
import java.io.IOException;

import drawing.KeyInterceptor.KeyHook;
import TrafficFlow.mapFramework.MapFrame;
import TrafficFlow.mapFramework.MapImage;

public class Program {
    
    private static MapImage _mapImage;
    private static MapFrame _mapFrame;
    
    /**
     * Lambda method which will be called each time the user
     * is pressing the key 'T'.
     * @param keyEvent - details about the key which was pressed.
     */
    private static KeyHook _onKeyT = (KeyEvent keyEvent) -> {
        String statusText = "Key: '" + keyEvent.getKeyChar() + "'; ";
        statusText += "Routes: " + _mapImage.getRoutes();
        _mapFrame.setStatusMessage(statusText);
    };
    
    public static void main(String[] args) throws IOException, InterruptedException {
        // loads an intersection image file and displays it in a map frame.
        _mapImage = MapImage.load("TrafficFlow/maps/Woodlawn.jpg");
        _mapFrame = new MapFrame(_mapImage);
        
        // registers the key T with the method _onKeyT
        _mapFrame.setKeyTypedHook('T', _onKeyT);
        
        // opens the GUI window
        _mapFrame.open();
        
        // stops, waiting for user action
        _mapFrame.stop();
        
        // close the window and terminate the program
        _mapFrame.close();
    }
}
