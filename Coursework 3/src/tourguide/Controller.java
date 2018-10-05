package tourguide;

import java.util.List;

public interface Controller {

    /*
     * Create tour mode
     */
    Status startNewTour(String id, String title, Annotation annotation);

    Status addWaypoint(Annotation annotation);

    Status addLeg(Annotation annotation);

    Status endNewTour();
    
    /*
     * Browse tours mode
     */
    Status showTourDetails(String id);

    Status showToursOverview();

    /*
     * Follow tour
     */
    Status followTour(String id);

    Status endSelectedTour();

    /*
     *  All modes
     */
    void setLocation(double easting, double northing);

    List<Chunk> getOutput();
}