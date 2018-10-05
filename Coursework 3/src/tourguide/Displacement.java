package tourguide;

import java.util.logging.Logger;

public class Displacement {
    private static Logger logger = Logger.getLogger("tourguide");
       
    public double east;
    public double north;

    /**
     * @param  e Indicates the location’s position in metres east of some reference position.
     * @param  n Indicates the location’s position in metres north of some reference position.
     */

    public Displacement(double e, double n) {
        logger.finer("East: " + e + "  North: "  + n);
        
        east = e;
        north = n;
    }

    /**
     * @return distance (of type double) between some reference position and the point with the given east and north
     * coordinates
     */

    public double distance() {
        logger.finer("Entering");
        
        return Math.sqrt(east * east + north * north);
    }
    
    // Bearings measured clockwise from north direction.

    /**
     * @return bearing (of type double) measured clockwise from north direction
     */

    public double bearing() {
        logger.finer("Entering");
              
        // atan2(y,x) computes angle from x-axis towards y-axis, returning a negative result
        // when y is negative.
        
        double inRadians = Math.atan2(east, north);
        
        if (inRadians < 0) {
            inRadians = inRadians + 2 * Math.PI;
        }
        
        return Math.toDegrees(inRadians);
    }
}
