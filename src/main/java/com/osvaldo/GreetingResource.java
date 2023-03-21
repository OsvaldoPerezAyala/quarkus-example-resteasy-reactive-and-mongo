package com.osvaldo;


import com.osvaldo.service.FruitService;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/fruits")
public class GreetingResource {

    @Inject
    FruitService fruitService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }

    //Example request: localhost:8080/fruits/list
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFruit() {
        JSONArray fruitArray = fruitService.list();

        JSONObject response = new JSONObject().put("status", "ok");
        response.put("fruits", fruitArray);
        Response.ResponseBuilder responseWithStatus = Response.status(200).entity(response);
        return responseWithStatus.build();
    }

    //Example request: localhost:8080/get/fruit/fruta?frutas=pera

    @Path("/get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFrutasByQueryParam(@QueryParam("fruit") String fruta) {
        System.out.println(fruta);
        JSONObject jsonFruit = fruitService.getFruit(fruta);
        Response.ResponseBuilder responseWithStatus = Response.status(200).entity(jsonFruit);
        return responseWithStatus.build();
    }


    //Example request: localhost:8080/get/fruit/fruta/1
    @Path("/get/fruitById/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFrutaByPathParam(String id) {
        System.out.println(id);
        Response.ResponseBuilder responseWithStatus = Response.status(200).entity(new JSONObject().put("status", "ok"));
        return responseWithStatus.build();
    }


    @Path("/create")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(String request) {
        Document doc = Document.parse(request);
        fruitService.add(doc);
        JSONObject fruit = fruitService.getFruit(doc.getString("name"));
        JSONObject response = new JSONObject()
                .put("fruit", fruit)
                .put("status", "ok");

        Response.ResponseBuilder responseWithStatus = Response.status(200).entity(response);
        return responseWithStatus.build();
    }


    /*
    @Path("/create")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Fruit post(Fruit fruit) {
        System.out.println("ya entró");

        fruitService.add(fruit);
        var res = fruitService.getFruit(fruit.getName());
        return res;
    }
 */

}