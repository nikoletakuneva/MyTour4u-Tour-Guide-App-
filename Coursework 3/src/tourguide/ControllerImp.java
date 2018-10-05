package tourguide; /**
 * 
 */

import java.util.*;
import java.util.logging.Logger;

/**
 * @author pbj
 */
public class ControllerImp implements Controller {
    private static Logger logger = Logger.getLogger("tourguide");
    private static final String LS = System.lineSeparator();
    private TreeSet<Tour> tourLibrary = new TreeSet<>(new idComp());
    private Tour currTour;
    private double waypointRadius;
    private double waypointSeparation;
    private Mode currMode;
    private int currStageNumber = -1;
    private String lastAdd;
    private Location location = new Location();
    private List<Chunk> chunks;

    private String startBanner(String messageName) {
        return  LS 
                + "-------------------------------------------------------------" + LS
                + "MESSAGE: " + messageName + LS
                + "-------------------------------------------------------------";
    }
    
    public ControllerImp(double waypointRadius, double waypointSeparation) {
        this.waypointRadius = waypointRadius;
        this.waypointSeparation = waypointSeparation;
        this.currMode = Mode.BROWSE;
        Status chunkOverview = showToursOverview();
    }

    public double getWaypointRadius() {
        return waypointRadius;
    }

    public double getWaypointSeparation() {
        return waypointSeparation;
    }

    //--------------------------
    // Create tour mode
    //--------------------------

    // Some examples are shown below of use of logger calls.  The rest of the methods below that correspond 
    // to input messages could do with similar calls.
    
    @Override
    public Status startNewTour(String id, String title, Annotation annotation) {
        logger.fine(startBanner("startNewTour"));
        chunks = new ArrayList<>();
        if (!currMode.equals(Mode.BROWSE)) {
            return new Status.Error("Tour creation can only be started when the app is in browse mode!");
        }
        currMode = Mode.AUTHOR;
        currTour = new Tour(id, title, annotation);
        tourLibrary.add(currTour);
        Chunk header = new Chunk.CreateHeader(currTour.getTitle(), currTour.getLegs().size(),
                currTour.getWaypoints().size());
        this.chunks.add(header);
        return Status.OK;
    }

    @Override
    public Status addWaypoint(Annotation annotation) {
        logger.fine(startBanner("addWaypoint"));
        if (lastAdd == null || lastAdd.equals("waypoint")) {
            Status status = addLeg(Annotation.DEFAULT);
            if (!status.equals(Status.OK)) {
                return new Status.Error("Something went wrong with adding the default annotation for leg!");
            }
        }
        Waypoint currWaypoint = new Waypoint(annotation);
        currTour.getWaypoints().add(currWaypoint);
        lastAdd = "waypoint";
        currWaypoint.setEast(this.location.getEast());
        currWaypoint.setNorth(this.location.getNorth());
        if (currTour.getWaypoints().size() > 1) {
            Waypoint lastWaypoint = currTour.getWaypoints().get(currTour.getWaypoints().size() - 2);
            Displacement displacement = new Displacement((currWaypoint.getEast() - lastWaypoint.getEast()),
                    (currWaypoint.getNorth() - lastWaypoint.getNorth()));
            if (displacement.distance() <= getWaypointSeparation()) {
                return new Status.Error("Tour creation should enforce separation between adjacent waypoints!");
            }
        }
        this.chunks.set(this.chunks.size() - 1,
                new Chunk.CreateHeader(currTour.getTitle(), currTour.getLegs().size(), currTour.getWaypoints().size()));
        return Status.OK;
    }

    @Override
    public Status addLeg(Annotation annotation) {
        logger.fine(startBanner("addLeg"));
        if (annotation == null) {
            annotation = Annotation.DEFAULT;
        }
        currTour.getLegs().add(new Leg(annotation));
        lastAdd = "leg";
        this.chunks.set(this.chunks.size() - 1,
                new Chunk.CreateHeader(currTour.getTitle(), currTour.getLegs().size(), currTour.getWaypoints().size()));
        return Status.OK;
    }

    @Override
    public Status endNewTour() {
        logger.fine(startBanner("endNewTour"));
        if (currTour.getWaypoints().size() == 0) {
            return new Status.Error("A tour must have at least one waypoint!");
        }
        if (!lastAdd.equals("waypoint")) {
            return new Status.Error("A tour should end with a waypoint!");
        }
        currMode = Mode.BROWSE;
        Status showTour = showToursOverview();
        return Status.OK;
    }

    //--------------------------
    // Browse tours mode
    //--------------------------

    @Override
    public Status showTourDetails(String tourID) {
        logger.fine(startBanner("showTourDetails"));
        this.chunks = new ArrayList<>();
        boolean found = false;
        for (Tour tour : tourLibrary) {
            if (tour.getId().equals(tourID)) {
                Chunk showChunk = new Chunk.BrowseDetails(tour.getId(), tour.getTitle(), tour.getTourInfo());
                this.chunks.add(showChunk);
                found = true;
                break;
            }
        }
        if (!found) {
            return new Status.Error("Tour not found!");
        }

        return Status.OK;
    }
  
    @Override
    public Status showToursOverview() {
        logger.fine(startBanner("showToursOverview"));
        this.chunks = new ArrayList<>();
        Chunk.BrowseOverview overviewChunk = new Chunk.BrowseOverview();
        for (Tour tour : tourLibrary) {
            overviewChunk.addIdAndTitle(tour.getId(), tour.getTitle());
        }
        this.chunks.add(overviewChunk);
        return Status.OK;
    }

    private class idComp implements Comparator<Tour> {
        public int compare(Tour t1, Tour t2) {
            return t1.getId().compareTo(t2.getId());
        }
    }

    //--------------------------
    // Follow tour mode
    //--------------------------
    
    @Override
    public Status followTour(String id) {
        logger.fine(startBanner("followTour"));
        this.chunks = new ArrayList<>();
        if (this.currStageNumber == -1) {
            if (!currMode.equals(Mode.BROWSE)) {
                return new Status.Error("Cannot follow a tour if not in browse mode!");
            }
            currMode = Mode.FOLLOW;
            boolean found = false;
            for (Tour tour : tourLibrary) {
                if (tour.getId().equals(id)) {
                    currTour = tour;
                    Chunk showChunk = new Chunk.BrowseDetails(tour.getId(), tour.getTitle(), tour.getTourInfo());
                    this.chunks.add(showChunk);
                    found = true;
                    break;
                }
            }
            if (!found) {
                return new Status.Error("Tour not found!");
            }
            int numberOfStages = currTour.getWaypoints().size() + 1;
            if (numberOfStages < 2) {
                return new Status.Error("At a minimum a tour is expected to have two stages! Found " + numberOfStages);
            }
            this.currStageNumber++;
            return Status.OK;
        } else {
            Waypoint nextWaypoint = currTour.getWaypoints().get(this.currStageNumber);
            double userEast = this.location.getEast();
            double userNorth = this.location.getNorth();
            if (atWaypoint(userEast, userNorth, nextWaypoint, getWaypointRadius())) {
                this.currStageNumber++;
            }
            Chunk.FollowHeader followHeader = new Chunk.FollowHeader(currTour.getTitle(), this.currStageNumber,
                    currTour.getWaypoints().size());

            this.chunks.add(followHeader);
            Displacement displacement;
            if (this.currStageNumber > 0) {
                Waypoint currWaypoint = currTour.getWaypoints().get(this.currStageNumber - 1);
                boolean isAtWaypoint = atWaypoint(userEast, userNorth, currWaypoint, getWaypointRadius());
                if (isAtWaypoint) {
                    Chunk.FollowWaypoint followWaypoint = new Chunk.FollowWaypoint(currWaypoint.getWaypointInfo());
                    this.chunks.add(followWaypoint);
                    if (this.currStageNumber == currTour.getWaypoints().size()) {
                        return Status.OK;
                    }
                }
            }

            displacement = new Displacement(nextWaypoint.getEast() - userEast,
                    nextWaypoint.getNorth() - userNorth);
            Chunk.FollowLeg followLeg = new Chunk.FollowLeg(currTour.getLegs().get(this.currStageNumber).getLegInfo());
            this.chunks.add(followLeg);
            double distanceToNextWaypoint = displacement.distance();
            double bearingToNextWaypoint = displacement.bearing();
            Chunk.FollowBearing followBearing = new Chunk.FollowBearing(bearingToNextWaypoint, distanceToNextWaypoint);
            this.chunks.add(followBearing);
            if (this.currStageNumber == 0) {
                this.currStageNumber++;
            }
            return Status.OK;
        }
    }

    private boolean atWaypoint(double userEast, double userNorth, Waypoint currWaypoint, double waypointRadius) {
        Displacement displacement = new Displacement(userEast - currWaypoint.getEast(),
                userNorth - currWaypoint.getNorth());
        double d = displacement.distance();
        return d <= waypointRadius;
    }

    @Override
    public Status endSelectedTour() {
        logger.fine(startBanner("endSelectedTour"));
        this.chunks = new ArrayList<>();
        if (this.currMode.equals(Mode.FOLLOW)) {
            this.currMode = Mode.BROWSE;
            Status showTours = showToursOverview();
        } else {
            return new Status.Error("No tour to end!");
        }
        this.currStageNumber = -1;
        return Status.OK;
    }

    //--------------------------
    // Multi-mode methods
    //--------------------------
    @Override
    public void setLocation(double easting, double northing) {
        logger.fine(startBanner("setLocation"));
        this.location = new Location(easting, northing);
        if (this.currMode.equals(Mode.FOLLOW)) {
            Status followTour = followTour(this.currTour.getId());
        }

    }

    @Override
    public List<Chunk> getOutput() {
        return this.chunks;
    }


}
