package org.familysearch.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonEncoding;
//import org.codehaus.enunciate.jaxrs.TypeHint;

/**
 * Created by tygans on 4/30/15.
 */
@Path("/")
public class Resource {
    private static ArrayList<RestaurantObject> restaurants;
    private static Resource instance;

    public Resource(){
        //Create the resource list manually
        if(restaurants==null || restaurants.size()==0) {
            restaurants = new ArrayList<RestaurantObject>();
            restaurants.add(new RestaurantObject("Tucanos", 4, true));
            restaurants.add(new RestaurantObject("Taco Bell", 1, true));
            restaurants.add(new RestaurantObject("Cafe Rio", 2, false));
            restaurants.add(new RestaurantObject("Jade Garden", 3, true));
            restaurants.add(new RestaurantObject("Lambert's Cafe", 4, false));
            restaurants.add(new RestaurantObject("McDonalds", 1, true));
            restaurants.add(new RestaurantObject("Wendy's", 2, true));
            restaurants.add(new RestaurantObject("Burger King", 1, false));
            restaurants.add(new RestaurantObject("Hungry Jack's", 1, false));
            restaurants.add(new RestaurantObject("Olive Garden", 3, true));
            System.out.println("Resource created");
        }
    }

    public static Resource getInstance(){
        if(instance == null){
            instance = new Resource();
        }

        //Create the resource list manually
        instance.restaurants.add(new RestaurantObject("Tucanos", 4, true));
        instance.restaurants.add(new RestaurantObject("Taco Bell", 1, true));
        instance.restaurants.add(new RestaurantObject("Cafe Rio", 2, false));
        instance.restaurants.add(new RestaurantObject("Jade Garden", 3, true));
        instance.restaurants.add(new RestaurantObject("Lambert's Cafe", 4, false));
        instance.restaurants.add(new RestaurantObject("McDonalds", 1, true));
        instance.restaurants.add(new RestaurantObject("Wendy's", 2, true));
        instance.restaurants.add(new RestaurantObject("Burger King", 1, false));
        instance.restaurants.add(new RestaurantObject("Hungry Jack's", 1, false));
        instance.restaurants.add(new RestaurantObject("Olive Garden", 3, true));

        return instance;
    }

    //Basic GET in plain text
    @GET
    @Produces("text/plain")
    public Response welcomeText() {
        String retVal = new String();
        retVal ="Welcome to Tygan's RESTful Restaurants";
        return Response.status(Response.Status.OK).entity(retVal).build();
    }

    //Basic GET in html
    @GET
    @Produces("text/html")
    public Response welcomeHTML() {
        String retVal =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head lang=\"en\">" +
                        "    <meta charset=\"UTF-8\">" +
                        "    <title>Tygan's RESTful Restaurants</title>" +
                        "    <script src=\"localhost:8080/restaurant/restaurants\"></script>" +
                        "</head>" +
                        "<!-- <body onload=\"prettyPrint()\"> -->" +
                        "<body>" +
                        "<p><h1>Welcome</h1></p>" +
                        "<p>Welcome to \"Tygan\'s RESTful Restaurants.\" In case you\"re wondering why you didn't get back XML or JSON," +
                            " it's probably because you're using a browser and browsers tend to ask for HTML before XML or JSON.</p>" +
                        "<!-- <pre class=\"prettyprint\"> -->" +
                        "    <pre>" +
                        "<br>";
        retVal += "</list>" +
                "  </pre>" +
                "</body>" +
                "</html>";
        String eTag = String.valueOf(Response.status(200).hashCode());
        return Response.status(Response.Status.OK).tag(eTag).entity(retVal).build();
    }

    //returns all restaurants in plain text
    @GET
    @Path("/restaurants/")
    @Produces("text/plain")
    public Response getRestaurantsHTML(){
        String retVal = "Restaurants:\n";
        for (RestaurantObject restaurant: restaurants) {
            String openString = "closed";
            if(restaurant.isOpen()) {
                openString = "open";
            }
             retVal += restaurant.getName()+", "+restaurant.getRating()+" stars, "+openString+"\n";
        }
        String eTag = String.valueOf(Response.status(200).hashCode());
        return Response.status(Response.Status.OK).tag(eTag).entity(retVal).build();
    }

    //returns all restaurants in json
    @GET
    @Path("/restaurants/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurantsJSON() {
        String retVal = new String();

        ObjectMapper mapper = new ObjectMapper();
        try {
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            JsonGenerator jsonGenerator=new JsonFactory().createJsonGenerator(out,JsonEncoding.UTF8);

            mapper.writeValue(jsonGenerator, restaurants);
            retVal = out.toString();

        } catch (JsonGenerationException e) {
            String sErrText = "JSON Generation Error";
            return Response.status(Response.Status.BAD_REQUEST).entity(sErrText).build();
        } catch (JsonMappingException e) {
            String sErrText = "JSON Mapping Error";
            return Response.status(Response.Status.BAD_REQUEST).entity(sErrText).build();
        } catch (IOException e) {
            String sErrText = "I/O Error";
            return Response.status(Response.Status.BAD_REQUEST).entity(sErrText).build();
        }
        String eTag = String.valueOf(Response.status(200).hashCode());
        return Response.status(Response.Status.OK).tag(eTag).entity(retVal).build();
    }

    //returns all restaurants in xml
    @GET
    @Path("/restaurants/")
    @Produces({MediaType.TEXT_XML, MediaType.APPLICATION_ATOM_XML})
    public Response getRestaurantsXML() {
        String retVal = new String();
        retVal ="<hello> " +
                "<title>Tygan's RESTful Restaurants" + "</title>" +
                "<list>";
        Integer iIndex = new Integer(0);

        for (int i = 0; i < restaurants.size(); i++) {
            iIndex = i;
            retVal += "<item itemID=\"" + restaurants.get(i).getName() + "\">localhost:8080/restaurant/restaurants/" + restaurants.get(i).getName() + "</item>";
        }

        retVal += "</list>" +
                "</hello>";
        String eTag = String.valueOf(Response.status(200).hashCode());
        return Response.status(Response.Status.OK).tag(eTag).entity(retVal).build();
    }

    //accepts plain text
    @GET
    @Path("/restaurants/{name}")
    @Produces("text/plain")
    public Response getRestaurant(@PathParam("name") String restaurantName){
        RestaurantObject restaurant = null;
        if(restaurants!=null && restaurants.size()!=0) {
            for (RestaurantObject temp : restaurants) {
                if (temp.getName().equals(restaurantName)) {
                    restaurant = temp;
                }
            }
            if (restaurant != null) {
                String eTag = String.valueOf(Response.status(200).hashCode());
                String retVal = "Restaurant " + restaurant.getName() + " : " + restaurant.getRating() + " stars, ";
                if (restaurant.isOpen()) {
                    retVal += "open";
                } else {
                    retVal += "closed";
                }
                return Response.status(Response.Status.OK).tag(eTag).entity(retVal).build();
            } else {
                return Response.status(Response.Status.OK).entity("Invalid request").build();
                //return Response.status(Response.Status.BAD_REQUEST).entity("Invalid restaurant").build();
            }
        }
        else{
            return Response.status(Response.Status.OK).entity("Database empty").build();
            //return Response.status(Response.Status.BAD_REQUEST).entity("Database empty").build();
        }
    }

    //accepts plain text
    @POST
    //@Path("/restaurants/add/{name}+{rating}+{open}")
    @Path("/restaurants/add/{input}")
    //@Path("/restaurants/")
    @Consumes("text/plain")
    //@Produces("text/plain")
    public Response createRestaurant(@PathParam("input") String input) throws URISyntaxException {
        RestaurantObject restaurant = new RestaurantObject(input, 0, false);
        // if(restaurantName==null || rating=Integer.parseInt(ratingString))
        if(restaurant!=null){
            System.out.println(input);
            restaurants.add(restaurant);
            System.out.println("Restaurants: "+restaurants.size());
            return Response.status(201).contentLocation(new URI("/restaurants/" + restaurant.getName())).build();
        }
        else{
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid restaurant").build();
        }
    }
}
