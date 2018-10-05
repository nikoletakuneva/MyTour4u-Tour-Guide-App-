package tourguide; /**
 * 
 */

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author pbj
 */
public class DisplacementTest {
    /**
     * EPS = Epsilon, the difference to allow in floating point numbers when 
     * comparing them for equality.
     */
    private static final double EPS = 0.01; 
    
    @Test
    public void testNorthBearing() {
        double bearing = new Displacement(0.0, 1.0).bearing();
        assertEquals(0.0, bearing, EPS);
    }

    @Test
    public void testEastBearing() {
        double bearing = new Displacement(1.0, 0.0).bearing();
        assertEquals(90.0, bearing, EPS);
    }

    @Test
    public void testZeroBearing() {
        double bearing = new Displacement(0.0, 0.0).bearing();
        assertEquals(0.0, bearing, EPS);
    }


    @Test
    public void testNorthDistance() {
        double distance = new Displacement(0.0, 34.0).distance();
        assertEquals(34.0, distance, EPS);
    }

    @Test
    public void testEastDistance() {
        double distance= new Displacement(118.0, 0.0).distance();
        assertEquals(118.0, distance, EPS);
    }

    @Test
    public void testMyCityOne() {
        double distance = new Displacement(26.5035, 42.4842).distance();
        assertEquals(50.073374, distance, EPS);
    }

    @Test
    public void testMyCityTwo() {
        double distance = new Displacement(25.2205, 42.0969).distance();
        assertEquals(49.073645, distance, EPS);
    }
 
}
