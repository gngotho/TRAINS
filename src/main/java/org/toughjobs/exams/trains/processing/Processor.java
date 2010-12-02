package org.toughjobs.exams.trains.processing;


import java.util.Set;

import org.toughjobs.exams.trains.data.Route;
import org.toughjobs.exams.trains.data.Trip;
import org.toughjobs.exams.trains.exceptions.NoSuchRouteException;

/**
 *
 * Process distances and trips algorithms.
 * @author <a href="tgoirand@gmail.com">Thomas Goirand</a>
 */
public interface Processor {

    /**
     * Add a route between two cities.
     * @param route Bean which represents starting and ending point.
     * @param distance distance between cities.
     */
    public void addRoute(Route route, int distance);

    /**
     * Retrieve a distance between two cities with a single step.
     * @param route Bean which represents starting and ending point.
     * @return distance.
     * @throws NoSuchRouteException Route doesn't exist.
     */
    public int getDistance(Route route) throws NoSuchRouteException;

    /**
     * Get the shortest way from a city to another giving every step.
     * @param cities Every city which represent every step.
     * @return the distance
     * @throws NoSuchRouteException Route doesn't exist.
     */
    public int getRoute(String... cities) throws NoSuchRouteException;

    /**
     * Retrieve every possible trip between a starting and a ending city
     * with the exact number of steps.
     * @param from City to start from.
     * @param to City to go to.
     * @param steps Number of steps required.
     * @return A set of every steps sorted in alphabetical order.
     */
    public Set<Trip> findTripsEqualsSteps(String from, String to, int steps);

    /**
     * Retrieve every possible trip between a starting and a ending city,
     * using a maximum number of steps.
     * @param from City to start from.
     * @param to City to go to.
     * @param steps Number of maximum steps.
     * @return A set of every steps sorted in alphabetical order.
     */
    public Set<Trip> findTripsMaxSteps(String from, String to, int steps);

    /**
     * Retrieve every possible trip between a starting and a ending city,
     * using a step name only once.
     * @param from City to start from.
     * @param to City to go to.
     * @return A set of every steps sorted in alphabetical order.
     */
    public Set<Trip> findTrips(String from, String to);

    /**
     * Get the trip between two city with the shortest distance.
     * @param from City to start from
     * @param to City to go to.
     * @return The trip including steps and distance.
     * @throws NoSuchRouteException Route doesn't exist.
     */
    public Trip getShortestDistanceTrip(String from, String to) throws NoSuchRouteException;

    /**
     * Retrieve all the trips possible with the global distance lower than the given parameter.
     * @param from City to start from.
     * @param to City to go to.
     * @param distance Maximum distance which can't be reached. (result to be inferior.)
     * @return A set of every steps sorted in alphabetical order.
     */
    public Set<Trip> findTripsMaxDistance(String from, String to, int distance);

}
