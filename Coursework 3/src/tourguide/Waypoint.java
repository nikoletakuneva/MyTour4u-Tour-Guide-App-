package tourguide;

/**
 * Created by user on 11/17/2017.
 */
public class Waypoint {
    private Annotation waypointInfo;
    private double east;
    private double north;

    public Waypoint(Annotation waypointInfo) {
        this.waypointInfo = waypointInfo;
    }

    public double getEast() {
        return east;
    }

    public Annotation getWaypointInfo() {
        return waypointInfo;
    }

    public void setEast(double east) {
        this.east = east;
    }

    public double getNorth() {
        return north;
    }

    public void setNorth(double north) {
        this.north = north;
    }
}
