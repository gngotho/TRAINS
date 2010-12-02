package org.toughjobs.exams.trains;


import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import org.toughjobs.exams.trains.data.Route;
import org.toughjobs.exams.trains.data.Trip;
import org.toughjobs.exams.trains.exceptions.NoSuchRouteException;
import org.toughjobs.exams.trains.processing.Processor;
import org.toughjobs.exams.trains.processing.ProcessorImpl;

import junit.framework.TestCase;

/**
 * Test case for all ThoughtWork inputs.
 *
 * @author <a href="tgoirand@gmail.com">Thomas Goirand</a>
 */
public class ExamResultTest extends TestCase {

    private static final Logger logger = Logger.getLogger(ExamResultTest.class.getName());
    Processor proc;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        proc = getGraph();
    }

    /**
     * Feed the processor with input graph: AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7
     * @return the input graph.
     */
    public static Processor getGraph() {
        Processor proc = new ProcessorImpl();
        proc.addRoute(new Route("A", "B"), 5);
        proc.addRoute(new Route("B", "C"), 4);
        proc.addRoute(new Route("C", "D"), 8);
        proc.addRoute(new Route("D", "C"), 8);
        proc.addRoute(new Route("D", "E"), 6);
        proc.addRoute(new Route("A", "D"), 5);
        proc.addRoute(new Route("C", "E"), 2);
        proc.addRoute(new Route("E", "B"), 3);
        proc.addRoute(new Route("A", "E"), 7);

        return proc;
    }

    /**
     * Test the application with the giving ouput.
     */
    public void testApp() {

        logger.info("Testing ThoughtWork exam results, "
                + "please check target/surefire-reports/com.thoughtwork.exams.trains.ExamResultTest.txt"
                + " for any error.");

        // 1. The distance of the route A-B-C.
        logger.info("First test 1. The distance of the route A-B-C. (Further tests won't be displayed)");

        try {
            int resutl = proc.getRoute("A", "B", "C");
            logger.info("Result: " + resutl);
            assertEquals(9, proc.getRoute("A", "B", "C"));
        } catch (NoSuchRouteException ex) {
            assertFalse(true);
        }

        // 2. The distance of the route A-D.

        try {
            assertEquals(5, proc.getRoute("A", "D"));
        } catch (NoSuchRouteException ex) {
            assertFalse(true);
        }

        // 3. The distance of the route A-D-C.

        try {
            assertEquals(13, proc.getRoute("A", "D", "C"));
        } catch (NoSuchRouteException ex) {
            assertFalse(true);
        }

        // 4. The distance of the route A-E-B-C-D.
        try {
            assertEquals(22, proc.getRoute("A", "E", "B", "C", "D"));
        } catch (NoSuchRouteException ex) {
            assertFalse(true);
        }


        // 5. The distance of the route A-E-D.

        try {
            proc.getRoute("A", "E", "D");
            assertFalse(true);
        } catch (NoSuchRouteException ex) {
            // output is "No such route" it wouldn't be handled in a unit test.
            logger.info("NO SUCH ROUTE");
            assertTrue(true);
        }


        // 6. The number of trips starting at C and ending at C with a maximum of 3 stops. 
        // In the sample data below, there are two such trips: C-D-C (2 stops). and C-E-B-C (3 stops).
        Set<Trip> trips6 = proc.findTripsMaxSteps("C", "C", 3);
        assertEquals(2, trips6.size());

        Iterator<Trip> tripIterate6 = trips6.iterator();
        if (tripIterate6.hasNext()) {
            assertEquals("CDC", tripIterate6.next().toString());
        }

        if (tripIterate6.hasNext()) {
            assertEquals("CEBC", tripIterate6.next().toString());
        }



        // 7. The number of trips starting at A and ending at C with exactly 4 stops.
        // In the sample data below, there are three such trips:
        // A to C (via B,C,D); A to C (via D,C,D); and A to C (via D,E,B).
        Set<Trip> trips7 = proc.findTripsEqualsSteps("A", "C", 4);

        // check there are three such trips
        assertEquals(3, trips7.size());


        // values of the trips
        Iterator<Trip> tripIterate7 = trips7.iterator();
        if (tripIterate7.hasNext()) {
            assertEquals("ABCDC", tripIterate7.next().toString());
        }

        if (tripIterate7.hasNext()) {
            assertEquals("ADCDC", tripIterate7.next().toString());
        }

        if (tripIterate7.hasNext()) {
            assertEquals("ADEBC", tripIterate7.next().toString());
        }

        // 8. The length of the shortest route (in terms of distance to travel) from A to C. 
        try {
            // shortest route should be 9
            assertEquals(9, proc.getShortestDistanceTrip("A", "C").getDistance());
        } catch (NoSuchRouteException ex) {
            assertFalse(true);
        }

        // 9. The length of the shortest route (in terms of distance to travel) from B to B.
        try {
            // shortest route should be 9
            assertEquals(9, proc.getShortestDistanceTrip("B", "B").getDistance());
        } catch (NoSuchRouteException ex) {
            assertFalse(true);
        }


        // 10. The number of different routes from C to C with a distance of less than 30.
        // In the sample data, the trips are: CDC, CEBC, CEBCDC, CDCEBC, CDEBC, CEBCEBC, CEBCEBCEBC.

        Set<Trip> trips10 = proc.findTripsMaxDistance("C", "C", 30);

        // There a re 7 trips whith a distance less than 30
        assertEquals(7, trips10.size());

        // iterate over all the 7 trips.
        Iterator<Trip> tripIterate10 = trips10.iterator();

        if (tripIterate10.hasNext()) {
            assertEquals("CDC", tripIterate10.next().toString());
        }

        if (tripIterate10.hasNext()) {
            assertEquals("CDCEBC", tripIterate10.next().toString());
        }

        if (tripIterate10.hasNext()) {
            assertEquals("CDEBC", tripIterate10.next().toString());
        }

        if (tripIterate10.hasNext()) {
            assertEquals("CEBC", tripIterate10.next().toString());
        }

        if (tripIterate10.hasNext()) {
            assertEquals("CEBCDC", tripIterate10.next().toString());
        }

        if (tripIterate10.hasNext()) {
            assertEquals("CEBCEBC", tripIterate10.next().toString());
        }

        if (tripIterate10.hasNext()) {
            assertEquals("CEBCEBCEBC", tripIterate10.next().toString());
        }

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        proc = null;
    }
}
