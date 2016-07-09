package com.dropwizard.resource;

import com.codahale.metrics.annotation.Timed;
import com.dropwizard.application.DropWizardApplication;
import com.dropwizard.configuration.DropWizardConfiguration;
import com.dropwizard.core.User;
import com.dropwizard.jdbi.UserDAO;
import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;
import com.google.common.base.Optional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by SauravKumar on 29-05-2016.
 */

@Path("/hello-world")
public class DropWizardRestApi
{
    DropWizardConfiguration config;
    UserDAO dao;
    public DropWizardRestApi()
    {

    }

    public DropWizardRestApi(DropWizardConfiguration config, UserDAO dao)
    {
        this.config = config;
        this.dao = dao;
    }

    @GET
    @Timed
    @Path("search1")
    public String sayHello(@QueryParam("name") Optional<String> name)
    {
        final User value = dao.findByName(name.get());
        return value.getUserId();
    }

    @POST
    @Timed
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sayHello1(User user)
    {
        StringBuilder crunchifyBuilder = new StringBuilder("Sucess");


        dao.insertInToDB(user.getName(), user.getUserId());
        // return HTTP response 200 in case of success
        return Response.status(200).entity(crunchifyBuilder.toString()).build();
    }
}
