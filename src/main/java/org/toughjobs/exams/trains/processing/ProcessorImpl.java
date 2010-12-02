package org.toughjobs.exams.trains.processing;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.toughjobs.exams.trains.data.Route;
import org.toughjobs.exams.trains.data.Trip;
import org.toughjobs.exams.trains.exceptions.NoSuchRouteException;

/**
 *
 * @see Process
 * @author <a href="tgoirand@gmail.com">Thomas Goirand</a>
 */
public class ProcessorImpl implements Processor {

    private static final Logger logger = Logger.getLogger(ProcessorImpl.class.getName());
    /**
     * Store a map for route / distance as there is only one distance possible
     * between two cities with no stop.
     */
    private Map<Route, Integer> routes;

    public ProcessorImpl() {
        routes = new HashMap<Route, Integer>();
    }

    @Override
    public void addRoute(Route route, int distance) {
        routes.put(route, new Integer(distance));
    }

    @Override
    public int getDistance(Route route) throws NoSuchRouteException {
        Integer dist = routes.get(route);
        if (dist == null) {
            throw new NoSuchRouteException();
        }

        return dist.shortValue();
    }

    @Override
    public int getRoute(String... cities) throws NoSuchRouteException {
        int distance = 0;

        for (int i = 1; i < cities.length; i++) {
            distance += getDistance(new Route(cities[i - 1], cities[i]));
        }

        return distance;
    }

    /**
     * Find any route starting with point from and which are not already in the given collection
     * @param c Given collection.
     * @param from starting point.
     * @return list of all possible routes.
     */

    /* package visibility */
    List<Route> getRoutes(Collection<String> c, String from) {
        List<Route> res = new ArrayList<Route>();
        for (Route route : routes.keySet()) {
            if (route.getFrom().equals(from)
                    && !c.contains(route.getTo()) /* avoid duplicate */) {
                res.add(route);
            }
        }
        return res;
    }

    /**
     * Find any route starting from a point.
     * @param from starting point.
     * @return list of all possible routes.
     */
    /* package visibility */
    List<Route> getRoutes(String from) {
        return getRoutes(new ArrayList<String>(), from);
    }


    @Override
    public Set<Trip> findTrips(String from, String to) {

        // list of all the trips to be worked out.
        List<Trip> trips = new ArrayList<Trip>();
        trips.add(new Trip(from));

        // iterate
        for (int k = 0; k < trips.size(); k++) {
            Trip trip = trips.get(k);
            if (trip.hasRoute() && !trip.isComplete()) {
                List<Route> routeOptions = getRoutes(trip.getSteps(), trip.peakLast());
                if (routeOptions.size() > 0) { // condition for a valid route: need to have at least one option.
                    for (int j = 0; j < routeOptions.size(); j++) {
                        Route route = routeOptions.get(j);
                        // another option would be to update current trip with first route option
                        // but I've chosen to let the list growing with a duplicated trip to keep it simple.
                        Trip newtrip = trip.cloneAndAdd(route.getTo(), routes.get(route));
                        if (newtrip.peakLast().equals(to)) {
                            newtrip.setComplete();
                        }
                        trips.add(newtrip); // duplicating route's root and adding change.
                    }
                } else { // no route option
                    trip.setDistance(Trip.NO_ROUTE);
                    trips.set(k, trip);
                }
            }
        }

        Set<Trip> result = new TreeSet<Trip>();
        for (Trip trip : trips) { // cleaning
            if (trip.isComplete() /* We keep only trips flagged as completed */) {
                result.add(trip);
            }
        }

        trips = null; // likely to free memory faster.
        return result;
    }

    @Override
    public Set<Trip> findTripsEqualsSteps(String from, String to, int steps) {
        // list of all the trips to be worked out.
        List<Trip> trips = new ArrayList<Trip>();
        trips.add(new Trip(from));

        // iterate
        for (int k = 0; k < trips.size(); k++) {
            Trip trip = trips.get(k);
            if (trip.hasRoute() && !trip.isComplete()) {
                List<Route> routeOptions = getRoutes(trip.peakLast());
                if (routeOptions.size() > 0 && trip.getSteps().size() <= steps) { // condition for a valid route: need to have at least one option and steps number must number be inferior to param.
                    for (int j = 0; j < routeOptions.size(); j++) {
                        Route route = routeOptions.get(j);
                        // another option would be to update current trip with first option
                        // but I've choosen to let the list growing with a duplicated trip to keep it simple.
                        Trip newtrip = trip.cloneAndAdd(route.getTo(), routes.get(route));
                        if (newtrip.peakLast().equals(to) && steps == newtrip.getSteps().size()) {
                            newtrip.setComplete();
                        }
                        trips.add(newtrip); // duplicating route's root and adding change.
                    }
                } else { // no route option
                    trip.setDistance(Trip.NO_ROUTE);
                    trips.set(k, trip);
                }
            }
        }


        Set<Trip> result = new TreeSet<Trip>();
        for (Trip trip : trips) { // cleaning
            if (trip.isComplete() /* We keep only trips flagged as completed */) {
                result.add(trip);
            }
        }

        trips = null; // likely to free memory faster.
        return result;
    }

    @Override
    public Set<Trip> findTripsMaxSteps(String from, String to, int steps) {
        Set<Trip> result = new TreeSet<Trip>();
        for (Trip trip : findTrips(from, to)) {
            if (trip.getSteps().size() <= steps) {
                result.add(trip);
            }
        }
        return result;
    }

    @Override
    public Trip getShortestDistanceTrip(String from, String to) throws NoSuchRouteException {
        // create a new set with a comparator to sort the Set according distances (the first the shorter).
        Set<Trip> trips = new TreeSet<Trip>(new Comparator<Trip>() {
            @Override
            public int compare(Trip t, Trip t1) {
                return new Integer(t.getDistance()).compareTo(new Integer(t1.getDistance()));
            }
        });
        trips.addAll(findTrips(from, to)); // add the trips to sort.

        Iterator<Trip> tripIterator = trips.iterator();
        if (!tripIterator.hasNext()) {
            throw new NoSuchRouteException();
        }

        // the first one is the shorter.
        return tripIterator.next();
    }

    
    @Override
    public Set<Trip> findTripsMaxDistance(String from, String to, int distance) {
        // list of all the trips to be worked out.
        List<Trip> trips = new ArrayList<Trip>();
        trips.add(new Trip(from));
        
        // iterate
        for (int k = 0; k < trips.size(); k++) {
            Trip trip = trips.get(k);
            if (trip.hasRoute()) {
                List<Route> routeOptions = getRoutes(trip.peakLast());
                if (routeOptions.size() > 0 && trip.getDistance() < distance) { // condition for a valid route: need to have at least one option and steps number must number be inferior to param.
                    for (int j = 0; j < routeOptions.size(); j++) {
                        Route route = routeOptions.get(j);
                        // another option would be to update current trip with first option
                        // but I've choosen to let the list growing with a duplicated trip to keep it simple.
                        Trip newtrip = trip.cloneAndAdd(route.getTo(), routes.get(route));
                        if (newtrip.peakLast().equals(to)) {
                            newtrip.setComplete();
                        }
                        trips.add(newtrip); // duplicating route's root and adding change.
                    }
                } else { // no route option
                    trip.setDistance(Trip.NO_ROUTE);
                    trips.set(k, trip);
                }
            }
        }

        Set<Trip> result = new TreeSet<Trip>();
        for (Trip trip : trips) { // cleaning
            if (trip.hasRoute() && trip.isComplete() /* We keep only trips flagged as completed and which have a route */) {
                result.add(trip);
            }
        }

        trips = null; // likely to free memory faster.
        return result;
    }
}
