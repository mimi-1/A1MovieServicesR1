/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movie.moviesoapservice.services;

import com.movie.moviesoapservice.controllers.MoviesJpaController;
import com.movie.moviesoapservice.controllers.PeopleJpaController;
import com.movie.moviesoapservice.controllers.exceptions.IllegalOrphanException;
import com.movie.moviesoapservice.controllers.exceptions.NonexistentEntityException;
import com.movie.moviesoapservice.entities.Movies;
import com.movie.moviesoapservice.entities.MoviesPeople;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.soap.MTOM;

/**
 *
 * @author Marina
 */
@WebService(serviceName = "MovieService")
@MTOM(enabled = true, threshold = 0) //message transmission optimization
@HandlerChain(file = "/MovieService_handler.xml")
//@SOAPBinding(style=SOAPBinding.Style.RPC)
public class MovieService {

    /**
     * This is a sample web service operation
     */
    private EntityManagerFactory emf;
    private MoviesJpaController movieRepo;
    private PeopleJpaController peopleRepo;
   
    
    public MovieService() {
        this.emf = Persistence.createEntityManagerFactory("Movies_PU");
        this.movieRepo = new MoviesJpaController(emf);
        this.peopleRepo = new PeopleJpaController(emf);
        
    }

    //just to test
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    @WebMethod(operationName = "upload")
    @WebResult(name="length")
    public int upload(@WebParam(name = "name") String movieName,
                    @WebParam(name = "type") String movieType,
                    @WebParam(name = "series") String movieSeries,
                    @WebParam(name = "release") Date dateRelease,
                    @WebParam(name = "filename") String movieFilename,
                    @WebParam(name = "content") byte[] content
    ) {

        MoviesJpaController movieRepo;
        movieRepo = new MoviesJpaController(emf);
        Movies movie = new Movies();
        movie.setMovieName(movieName);
        movie.setMovieType(movieType);
        movie.setMovieSeries(movieSeries);
        movie.setMovieFilename(movieFilename);
        movie.setMovieContent(content);
        movie.setDateRelease(dateRelease);
        movie.setDateUpload(new Date());
        movieRepo.create(movie);
        if(content!=null){
                return content.length;
        }else return 1; 
    }
    
    

    @WebMethod(operationName = "download")
    public byte[] download(@WebParam(name = "id") BigInteger movieId) {
        Movies movie = movieRepo.findMovies(movieId);
        if (movie != null) {
            return movie.getMovieContent();
        } else {
            return null;
        }
    }

    @WebMethod(operationName = "getMovie")
    public Movies getMovie(@WebParam(name = "id") BigInteger movieId) {
        Movies movie = movieRepo.findMovies(movieId);
        return movie;

    }

    @WebMethod(operationName = "getMovieList")
    public List<Movies> getMovieList() {
        List<Movies> movies = movieRepo.findMoviesEntities();
        return movies;
    }
    
    
    @WebMethod(operationName = "updateMovie")
    @WebResult(name="length")
    public int updateMovie( @WebParam(name = "id") BigInteger movieId,
                                @WebParam(name = "name") String movieName,
                                @WebParam(name = "type") String movieType,
                                @WebParam(name = "series") String movieSeries,
                                @WebParam(name = "release") Date dateRelease,
                                @WebParam(name = "filename") String movieFilename,
                                @WebParam(name = "content") byte[] content) throws Exception {
       
        Movies movie = new Movies();
        movie.setMovieName(movieName);
        movie.setMovieId(movieId);
        movie.setMovieType(movieType);
        movie.setMovieSeries(movieSeries);
        movie.setMovieFilename(movieFilename);
        movie.setMovieContent(content);
        movie.setDateRelease(dateRelease);
        movie.setDateUpload(new Date());
        //missing all the people - maybe later
        Collection<MoviesPeople> moviesPeopleCollection= new ArrayList<MoviesPeople>();
        movie.setMoviesPeopleCollection(moviesPeopleCollection);
        movieRepo.edit(movie);
        return movie.getMovieContent().length;
        
        }
    
    @WebMethod(operationName = "deleteMovie")
    public int deleteMovie(@WebParam(name = "id") BigInteger movieId) throws IllegalOrphanException, NonexistentEntityException {
         movieRepo.destroy(movieId);
         return 1;

    }
    
    
   

}
    
   
