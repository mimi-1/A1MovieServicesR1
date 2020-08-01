/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movie.moviesoapservice.tests;

import com.movie.moviesoapservice.controllers.MoviesJpaController;
import com.movie.moviesoapservice.entities.Movies;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Marina
 */
public class JPATest {
    
    public static void main(String[] args) {
                
        
        System.out.println("JPATest");
        try{
    
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("Movies_PU");
             MoviesJpaController movieRepo = new MoviesJpaController(emf);
            Movies movie = new Movies();
            movie.setMovieName("Lecture1");
            movie.setMovieType("Education");
            movie.setMovieSeries("Java");
            movieRepo.create(movie);
            System.out.println("New movie is created");
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
             
    }
}
