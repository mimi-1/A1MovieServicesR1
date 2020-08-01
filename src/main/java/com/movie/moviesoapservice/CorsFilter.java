package com.movie.moviesoapservice;


import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marina
 */

//Provider annotation will be wired when the app is running 
@Provider
public class CorsFilter implements ContainerResponseFilter{

    @Override
    public void filter(final ContainerRequestContext crc, final ContainerResponseContext crc1) throws IOException {
          crc1.getHeaders().add("Access-Control-Allow_Origin", "*");
          crc1.getHeaders().add("Access-Control-Allow_Headers", "origin,content-type,accept,authorization");
          crc1.getHeaders().add("Access-Control-Allow_Credentials", "true");
          crc1.getHeaders().add("Access-Control-Allow_Methods", "GET,POST,DELETE,PUT,OPTIONS,HEAD");
          crc1.getHeaders().add("Access-Control-Max-Age", "1209600");
    }
}
