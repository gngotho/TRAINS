package org.toughjobs.exams.trains.data;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Bean containing all information about a trip from a starting city.
 * First step is provided through the constructor and every step is stored in a collection which can be accessed using {@link #getSteps()}
 * {@link #distance} is set as {@link #NO_ROUTE} Trip if it known to be invalid ({@link org.toughjobs.exams.trains.processing.Processor} can't work out a path).
 *
 * @author <a href="tgoirand@gmail.com">Thomas Goirand</a>
 */
public class Trip implements Comparable<Trip> {

    /**
     * Global distance from start to last city.
     */
    private int distance;
    /**
     * if it is known the trip can't reach its destination, distance is set as NO_ROUTE
     */
    public final static int NO_ROUTE = -1;
    /**
     * Contains all the steps but not the starting city
     * Collection is a LinkedList as it provides a convenient method to get last element.
     */
    private LinkedList<String> steps;
    /**
     * City to start from.
     */
    private String from = null;
    private boolean complete;

    /**
     * Initialize a trip with a starting city.
     * @param from Starting city.
     */
    public Trip(String from) {
        steps = new LinkedList<String>();
        complete = false;
        this.from = from;
    }


    /**
     * Visibility is package only as it is only intended to clone the class.
     * Class is likely to be cloned using {@link #cloneAndAdd(java.lang.String, int) }
     * by {@link org.toughjobs.exams.trains.processing.Processor} every time a new way with a new step is found.
     *
     * @param from
     * @param steps
     * @param distance
     */
    Trip(String from, LinkedList<String> steps, int distance) {
        this.distance = distance;
        this.steps = steps;
        complete = false;
        this.from = from;
    }

    /**
     * Distance between {@link #from and the last step} or {@link #NO_ROUTE} if no route found
     * @return Global distance.
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Distance between {@link #from and the last step} or {@link #NO_ROUTE} if no route found
     * @param distance Global distance.
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * every steps to go to a city but the staring city.
     * @return a collection of the name of steps.
     */
    public Collection<String> getSteps() {
        return steps;
    }

    /**
     * Get the last step of the trip.
     * @return Name of the last step or departure city if there's no step.
     */
    public String peakLast() {
    	if (steps.peekLast() != null)
        return steps.peekLast();
    	else
    		return from;
    }

    /**
     * Add a step and its distance from the previous step.
     * @param step Step name to add.
     * @param distance Distance from the previous step
     */
    public void add(String step, int distance) {
        steps.add(step);
        this.distance += distance;
    }

    /**
     * Duplicate a trip and add a step.
     * @param step step name to add.
     * @param distance distance between added step and previous one.
     * @return a clone of the new trip with the new step added.
     */
    public Trip cloneAndAdd(String step, int distance) {
        LinkedList<String> clo = (LinkedList<String>) steps.clone();
        Trip res = new Trip(this.from, clo, this.distance);
        res.add(step, distance);
        return res;
    }

    /**
     * To know whether the trip hasn't route.
     * @return false if the trip has been flagged as no route.
     */
    public boolean hasRoute() {
        return distance != NO_ROUTE;
    }

    @Override
    public int compareTo(Trip t) {
        return this.toString().compareTo(t.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(from);
        for (String s : steps) {
            sb.append(s);
        }

        return sb.toString();
    }

    /**
     * To know whether destination is reached.
     * @return true if effective.
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Set as destination reached.
     */
    public void setComplete() {
        this.complete = true;
    }

    /**
     * Get city to leave from.
     * @return Nmae of the city.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Set first city to leave from.
     * @param departure Name of the city.
     */
    public void setFrom(String departure) {
        this.from = departure;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Trip other = (Trip) obj;
        // a trip is equal if departure and steps are the same.
        return this.getFrom().equals(other.getFrom()) && other.getSteps().equals(getSteps());
    }

    @Override
    public int hashCode() {
        return getSteps().hashCode();
    }
}
