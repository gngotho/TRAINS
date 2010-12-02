package org.toughjobs.exams.trains.data;

import org.toughjobs.exams.trains.data.Trip;

import junit.framework.TestCase;

/**
 * Provide methods to test {@link Trip}
 * @author <a href="tgoirand@gmail.com">Thomas Goirand</a>
 */
public class TripTest extends TestCase {

    /**
     * Test {@link Trip#cloneAndAdd(java.lang.String, int) 
     */
    public void testDuplicate() {
        Trip trip1 = new Trip("A");
        trip1.add("A", 1);
        trip1.add("B", 2);
        trip1.add("C", 3);

        Trip trip2 = trip1.cloneAndAdd("D", 5);

        // a duplicate is not the same.
        assertNotSame(trip2, trip1);

        trip1.add("D", 5);

        // two trips are equals if the starting city, distance and steps are the same.
        assertEquals(trip2, trip1);

    }

}
