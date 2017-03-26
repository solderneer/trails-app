package ism.trails;

import java.net.URL;

/**
 * Created by Isaac Tay on 25/3/2017.
 */

public class LocationInfo {

    public enum Type {Waypoint, route};

    public Type type;
    public String name;
    public String description;
    public URL pictureURL;
    public double distance;
    public long timeTaken;

    public LocationInfo(Type type, String name, String description, double distance, long timeTaken, URL pictureURL) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.distance = distance;
        this.timeTaken = timeTaken;
        this.pictureURL = pictureURL;
    }
}
