package org.familysearch.test;

/**
 * Created by tygans on 4/30/15.
 */
public class RestaurantObject {

    private int id;
    private String name;
    private int rating;
    private boolean isOpen;

    public RestaurantObject(){

        this.name = null;
        this.rating = 0;
        this.isOpen = false;
    }

    public RestaurantObject(String name, int rating, boolean isOpen) {
        super();
        this.name = name;
        this.rating = rating;
        this.isOpen = isOpen;
    }

    public String getName() {
        return name;
    }
    public int getRating() {
        return rating;
    }
    public boolean isOpen() {
        return isOpen;
    }

}
