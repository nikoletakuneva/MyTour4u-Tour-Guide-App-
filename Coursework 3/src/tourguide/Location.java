package tourguide;

/**
 * Created by user on 11/21/2017.
 */
public class Location {
    private double east;
    private double north;

    public Location() {
        this.east = 0.0;
        this.north = 0.0;
    }

    public Location(double east, double north) {
        this.east = east;
        this.north = north;
    }

    public double getEast() {
        return east;
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
