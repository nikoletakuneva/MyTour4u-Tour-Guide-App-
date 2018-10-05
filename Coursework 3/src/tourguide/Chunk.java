package tourguide; /**
 * 
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author pbj
 * 
 */
public class Chunk {
    private static final String LS = System.lineSeparator();
    public static boolean within(double x, double y, double tol) {
        return Math.abs(x - y) <= tol;
    }
    
    // Chunk printing is rounding to nearest metre / nearest degree, so useful to
    // have a generous tolerance that enables us to infer plausible expected values from
    // error messages when comparing chunks.
    
    private static final double EPS = 1.0;
    
    /*
     * Chunk subclasses for BROWSE mode
     * 
     */
    public static class OverviewLine {
        public String id;
        public String title;
        
        public OverviewLine(String id, String title) {
            this.id = id;
            this.title = title;   
        }
        
        public String toString() {
            return id + ": " + title + LS;
        }
        
        public boolean equals(Object o) {
            if (!(o instanceof OverviewLine)) return false;
            OverviewLine oOL = (OverviewLine) o;
            return oOL.id.equals(id) && oOL.title.equals(title);
        }

    }
    
    public static class BrowseOverview extends Chunk {

        public List<OverviewLine> overviewLines;

        public BrowseOverview() {
            overviewLines = new ArrayList<OverviewLine>();
        }
  
        public void addIdAndTitle(String id, String title) {
            overviewLines.add(new OverviewLine(id,title));
        }
 
        public String toString() {
            if (overviewLines.isEmpty()) {
                return "NO TOURS TO SHOW" + LS;
            }
            StringBuffer sb = new StringBuffer();
            for (OverviewLine line : overviewLines) {
                sb.append(line);
            }
            return sb.toString();
        }
        
        public boolean equals(Object o ) {
            if (!(o instanceof BrowseOverview)) return false;
            BrowseOverview oBO = (BrowseOverview) o;
            Iterator<OverviewLine> it1 = overviewLines.iterator();
            Iterator<OverviewLine> it2 = oBO.overviewLines.iterator();
            while (it1.hasNext() && it2.hasNext()) {
                if (!(it1.next().equals(it2.next()))) return false;
            }
            return !(it1.hasNext()) && !(it2.hasNext());
        }
    }

    public static class BrowseDetails extends Chunk {
        public String id;
        public String title;
        public Annotation details;
        
        public BrowseDetails(String id, String title, Annotation details) {
            this.id = id;
            this.title = title;
            this.details = details;
        }
        public String toString() {
            return id + ": " + title + LS + details.toString();
        }
        public boolean equals(Object o) {
            if (!(o instanceof BrowseDetails)) return false;
            BrowseDetails oBD = (BrowseDetails) o;
            return id.equals(oBD.id) &&
                    title.equals(oBD.title) &&
                    details.equals(oBD.details);
                            
        }
    }
    /*
     * Chunk subclasses for FOLLOW mode
     */
    public static class FollowHeader extends Chunk {
        public String title;
        public int currentStage; 
        public int numberWaypoints;
        
        public FollowHeader(String title, int currentStage, int numberWaypoints) {
            this.title = title;
            this.currentStage = currentStage;
	    this.numberWaypoints = numberWaypoints;
        }
        public String toString() {
            return String.format("%1$s  %2$d/%3$d%n",title, currentStage, numberWaypoints);
        }
        public boolean equals(Object o) {
            if (!(o instanceof FollowHeader)) return false;
            FollowHeader oFH = (FollowHeader) o;
            return title.equals(oFH.title) &&
                    currentStage == oFH.currentStage &&
                    numberWaypoints == oFH.numberWaypoints;
        }

    }
    public static class FollowWaypoint extends Chunk {
        public Annotation annotation;
        
        public FollowWaypoint(Annotation annotation) {
            this.annotation = annotation;
        }
        public String toString() {
            return String.format("Current waypoint: %n%1$s", annotation);
        }
        public boolean equals(Object o) {
            if (!(o instanceof FollowWaypoint)) return false;
            FollowWaypoint oFW = (FollowWaypoint) o;
            return annotation.equals(oFW.annotation); 
        }
        

    }
 
    public static class FollowLeg extends Chunk {
        public Annotation annotation;
        
        public FollowLeg(Annotation annotation) {
            this.annotation = annotation;
        }
        public String toString() {
            return String.format("Leg to next waypoint:%n%1$s",
                    annotation);
        }
        public boolean equals(Object o) {
            if (!(o instanceof FollowLeg)) return false;
            FollowLeg oFL = (FollowLeg) o;
            return annotation.equals(oFL.annotation); 
        }
    }

    public static class FollowBearing extends Chunk {
        public double bearing;
        public double distance;
        
        public FollowBearing(double bearing, double distance) {
            this.bearing = bearing;
            this.distance = distance;
        }
        public String toString() {
            return String.format("Next waypoint is on bearing %1$.0f degrees at %2$.0fm %n", bearing, distance);
        }
        public boolean equals(Object o) {
            if (!(o instanceof FollowBearing)) return false;
            FollowBearing oFB = (FollowBearing) o;
            return within(bearing, oFB.bearing, EPS) && 
                    within(distance, oFB.distance, EPS) ;
        }
    }
    /*
     * Chunk subclass for CREATE mode
     */
    public static class CreateHeader extends Chunk {
        public String title;
        public int numberLegs;
        public int numberWaypoints;
        
        public CreateHeader(String title, int numberLegs, int numberWaypoints) {
            this.title = title;
            this.numberLegs = numberLegs;
            this.numberWaypoints = numberWaypoints;
        }
        public String toString() {
            return String.format("New tour: %1$s. #legs: %2$d  #waypoints: %3$d%n", 
                    title, numberLegs, numberWaypoints);
        }
        public boolean equals(Object o) {
            if (!(o instanceof CreateHeader)) return false;
            CreateHeader oCH = (CreateHeader) o;
            return title.equals(oCH.title) 
                    && numberLegs == oCH.numberLegs
                    && numberWaypoints == oCH.numberWaypoints;
        }

    }
}
