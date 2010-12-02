package org.toughjobs.exams.trains.processing;


import java.util.Set;
import java.util.logging.Logger;

import org.toughjobs.exams.trains.data.Route;
import org.toughjobs.exams.trains.data.Trip;
import org.toughjobs.exams.trains.exceptions.NoSuchRouteException;
import org.toughjobs.exams.trains.processing.Processor;
import org.toughjobs.exams.trains.processing.ProcessorImpl;

import junit.framework.TestCase;

/**
 * Provide methods to test {@link ProcessorImpl}
 * @author <a href="tgoirand@gmail.com">Thomas Goirand</a>
 */
public class ProcessorImplTest extends TestCase {

    private static final Logger logger = Logger.getLogger(ProcessorImplTest.class.getName());

    /**
     * Test find trips.
     */
    public void testRouteFrom() {
        ProcessorImpl processor = new ProcessorImpl();
        processor.addRoute(new Route("A", "B"), 5);
        processor.addRoute(new Route("B", "C"), 5);

        assertEquals(1, processor.getRoutes("A").size());

        Set<Trip> trips = processor.findTrips("A", "C");
        assertEquals(1, trips.size());
        if (trips.size() > 0) {
            assertEquals(10, trips.iterator().next().getDistance());
        }

        processor.addRoute(new Route("A", "D"), 5);

        assertEquals(processor.getRoutes("A").size(), 2);
    }

    /**
     * Test processor container.
     */
    public void testAddAndGetRouteIndividual() {

        Processor processor = new ProcessorImpl();

        Route routeAB = new Route("A", "B");

        // getting a value when the processor is empty
        try {
            processor.getDistance(routeAB);
            assertFalse(true);
        } catch (NoSuchRouteException ex) {
            // right statement as there isn't a route.
            assertTrue(true);
        }

        // setting and getting a single value
        processor.addRoute(routeAB, 5);
        try {
            assertEquals(5, processor.getDistance(routeAB));
        } catch (NoSuchRouteException ex) {
            assertFalse(true);
        }

        // overring an object with a different value
        processor.addRoute(routeAB, 7);

        try {
            assertEquals(7, processor.getDistance(routeAB));
        } catch (NoSuchRouteException ex) {
            assertFalse(true);
        }

    }

    /**
     * Test method findTrips
     */
    public void testFindTrips() {

        ProcessorImpl processor = new ProcessorImpl();
        processor.addRoute(new Route("A", "B"), 1);
        processor.addRoute(new Route("B", "C"), 2);

        assertEquals(1, processor.findTrips("A", "C").size());

        processor.addRoute(new Route("A", "C"), 4);

        assertEquals(2, processor.findTrips("A", "C").size());
        
        
    }
    
    /**
     * Test method findTripsEqualsSteps
     */
    public void testFindTripsEqualsSteps() {

        ProcessorImpl processor = new ProcessorImpl();
        processor.addRoute(new Route("A", "B"), 1);
        processor.addRoute(new Route("B", "C"), 2);

        assertEquals(0, processor.findTripsEqualsSteps("A", "C", 1).size());

        processor.addRoute(new Route("A", "C"), 4);

        assertEquals(1, processor.findTripsEqualsSteps("A", "C", 1).size());
        
    }

    /**
     * Test method findTripsMaxSteps
     */

    public void testFindTripsMaxSteps() {

        ProcessorImpl processor = new ProcessorImpl();
        processor.addRoute(new Route("A", "B"), 1);
        processor.addRoute(new Route("B", "C"), 2);

        assertEquals(1, processor.findTripsMaxSteps("A", "C", 3).size());

        processor.addRoute(new Route("A", "C"), 4);

        assertEquals(2, processor.findTripsMaxSteps("A", "C", 3).size());
    }

    /**
     * test method getShortestDistanceTrip
     */
    public void testGetShortestDistanceTrip() {

        ProcessorImpl processor = new ProcessorImpl();
        processor.addRoute(new Route("A", "B"), 1);
        processor.addRoute(new Route("B", "C"), 5);

        try {
			assertEquals(6, processor.getShortestDistanceTrip("A", "C").getDistance());
		} catch (NoSuchRouteException e) {
			assertTrue(false);
		}

        processor.addRoute(new Route("A", "C"), 4);

        try {
			assertEquals(4, processor.getShortestDistanceTrip("A", "C").getDistance());
		} catch (NoSuchRouteException e) {
			assertTrue(false);
		}
    }
    
    
    /**
     * Test method findTripsMaxDistance
     */
    public void testFindTripsMaxDistance() {
        ProcessorImpl processor = new ProcessorImpl();
        processor.addRoute(new Route("A", "B"), 1);
        processor.addRoute(new Route("B", "C"), 2);
    	
        
        assertEquals(1, processor.findTripsMaxDistance("A", "B", 5).size());
    }
    
}
