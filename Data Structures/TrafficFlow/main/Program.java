package TrafficFlow.main;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import drawing.KeyInterceptor.KeyHook;
import TrafficFlow.mapFramework.MapFrame;
import TrafficFlow.mapFramework.MapImage;

public class Program {
    
    private static MapImage _mapImage;
    private static MapFrame _mapFrame;
    
    private static HashMap<Character, Set<String>> _locationsMap;
    private static Graph<String> _collisionsGraph;

    // #region: Determine and overlay node routes
    private static void buildLocationsMap() {
        _locationsMap = new HashMap<Character, Set<String>>();
        for(String route: _mapImage.getRoutes()) {
            char from = route.charAt(0);
            char to = route.charAt(1); 
            Set<String> nodeRoutes;
            nodeRoutes = _locationsMap.get(from);
            if (nodeRoutes == null) {
                nodeRoutes = new HashSet<String>();
                _locationsMap.put(from, nodeRoutes);
                _mapFrame.setKeyTypedHook(from, _onNodeTyped);
                _mapFrame.setKeyTypedHook(to, _onNodeTyped);
            }
            nodeRoutes.add(route);
        }
        _mapFrame.setKeyPressedHook('T', _onKeyT);
    }
    
    private static KeyHook _onNodeTyped = (KeyEvent keyEvent) -> {
        // clear the status message and the overlays, if any
        _mapFrame.setStatusMessage("");
        _mapImage.setOverlays();
        // get the key from the keyEvent, indicating which node should be displayed
        char key = Character.toUpperCase(keyEvent.getKeyChar());
        Set<String> nodeRoutes = _locationsMap.get(key);
        if (nodeRoutes != null) {
            // get the routes corresponding to the selected node
            String[] nrArr = nodeRoutes.toArray(new String[nodeRoutes.size()]);
            _mapFrame.setStatusMessage(nodeRoutes.toString());
            _mapImage.setOverlays(nrArr);
        }
        // do not forget to repaint the window!
        _mapFrame.repaint();
    };
    // #endregion: Determine and overlay node routes
    
    // #region: Determine and overlay collision routes
    private static void buildCollisionsGraph() {
        _collisionsGraph = new Graph<String>();
        for(String route : _mapImage.getRoutes()) {
            _collisionsGraph.addNode(route);
        }
        
        for(String route1 : _mapImage.getRoutes()) {
            for (String route2 : _mapImage.getRoutes()) {
                _mapFrame.setStatusMessage(route1 + " & " + route2);
                if (_mapImage.collide(route1, route2)) {
                    _collisionsGraph.addEdge(route1, route2);
                    _collisionsGraph.addEdge(route2, route1);
                }
            }
        }
        
        _mapFrame.setKeyTypedHook('X', _onKeyX);
    }
    
    private static Queue<String> _lastCollisions = new LinkedList<String>();
    
    private static boolean sameAsLast(Set<String> overlays) {
        return _lastCollisions.containsAll(overlays)
             && overlays.containsAll(_lastCollisions);
    }
    
    private static KeyHook _onKeyX = (KeyEvent keyEvent) -> {
        Set<String> overlays = _mapImage.getOverlays();
        // user typed 'X' and ...
        if (overlays.size() == 1) {
            // ... there's one route on the map => overlay its collisions
            String testRoute = overlays.iterator().next();
            Set<String> collidingRoutes = _collisionsGraph.getNeighbors(testRoute);
            
            overlays.addAll(collidingRoutes);
            _mapImage.setOverlays(overlays);
            _mapFrame.setStatusMessage(collidingRoutes.toString());
            _lastCollisions.clear();
            _lastCollisions.add(testRoute);
            _lastCollisions.addAll(collidingRoutes);
        } else if (sameAsLast(overlays)){
            // ... the overlays match the last state => restore the original route
            _mapImage.setOverlays(_lastCollisions.remove());
            _lastCollisions.clear();
        } else {
            // ... map changed in a different way => clear the history
            _lastCollisions.clear();
        }
        
        // do not forget to repaint the window!
        _mapFrame.repaint();
    };
    // #endregion: Determine and overlay collision routes
    
    // #region: Color the graph and start traffic flow
    private static int _nColors = 0;
    private static void startTrafficFlow() {
        _nColors = _collisionsGraph.colorNodes();
        _mapFrame.setKeyTypedHook('W', _onKeyW);
    }
    
    private static int _phase = 0;
    private static KeyHook _onKeyW = (KeyEvent keyEvent) -> {
        _phase++;
        if (_phase == _nColors+1) {
            _phase = 1;
        }
        Set<String> routes = _collisionsGraph.getColoredData(_phase);
        _mapImage.setOverlays(routes);
        _mapFrame.repaint();
        _mapFrame.setStatusMessage("Phase: " + _phase);
    };
    // #endregion: Color the graph and start traffic flow
    
    private static KeyHook _onKeyT = (KeyEvent keyEvent) -> {
        _mapFrame.setStatusMessage(_mapImage.getRoutes().toString());
    };
    
    public static void main(String[] args) throws IOException, InterruptedException {
        // loads an intersection image file and displays it in a map frame.
        _mapImage = MapImage.load("TrafficFlow/maps/Sheridan.jpg");
        _mapFrame = new MapFrame(_mapImage);
        
        // registers the key 'T' with the method _onKeyT
        _mapFrame.setKeyTypedHook('T', _onKeyT);

        // opens the GUI window
        _mapFrame.open();
        
        // stops, waiting for user action
        _mapFrame.setStatusMessage("inspect individual routes for each location");
        _mapFrame.stop();

        // builds the locationsMap, and re-registers the locations keys
        buildLocationsMap();
        
        // stops again, waiting for user action
        _mapFrame.setStatusMessage("inspect egress routes for each location");
        _mapFrame.stop();
        
        // builds the collisions graph, and registers the collision inspection key ('W')
        buildCollisionsGraph();
        startTrafficFlow();
        
        // stops again, waiting for user action
        _mapFrame.setStatusMessage("inspect collisions for each location");
        _mapFrame.stop();
        
        // close the window and terminate the program
        _mapFrame.close();
    }
}