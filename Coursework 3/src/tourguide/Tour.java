package tourguide;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private String id;
    private String title;
    private Annotation tourInfo;
    private List<Waypoint> waypoints;
    private List<Leg> legs;

    public Tour(String id, String title, Annotation tourInfo) {
        this.id = id;
        this.title = title;
        this.tourInfo = tourInfo;
        this.waypoints = new ArrayList<>();
        this.legs = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Annotation getTourInfo() {
        return tourInfo;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public List<Leg> getLegs() {
        return legs;
    }
}
