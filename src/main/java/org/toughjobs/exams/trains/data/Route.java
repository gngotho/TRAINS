package org.toughjobs.exams.trains.data;

/**
 * Bean with two values: a route from a city to another one.
 * @author <a href="tgoirand@gmail.com">Thomas Goirand</a>
 */
public class Route {
    private String from;
    private String to;

    /**
     * New class instance with all required values.
     * @param from
     * @param to
     */
    public Route(String from, String to) {
        this.from = from;
        this.to = to;
    }

    /**
     * City to leave from.
     * @return Name of the city.
     */
    public String getFrom() {
        return from;
    }

    /**
     * City to leave from.
     * @param from Name of the city.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * City to go to.
     * @return Name of the city.
     */
    public String getTo() {
        return to;
    }

    /**
     * City to go to.
     * @param to Name of the city.
     */
    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        Route r = (Route)o;
        return (from + to).equals(r.getFrom() + r.getTo());
    }

    @Override
    public int hashCode() {
        return (from + to).hashCode();
    }

    @Override
    public String toString() {
        return "Route{" + "from=" + from + "to=" + to + '}';
    }

}
