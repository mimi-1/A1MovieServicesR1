/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movie.moviesoapservice.controllers;

import com.movie.moviesoapservice.controllers.exceptions.NonexistentEntityException;
import com.movie.moviesoapservice.controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.movie.moviesoapservice.entities.Movies;
import com.movie.moviesoapservice.entities.MoviesPeople;
import com.movie.moviesoapservice.entities.MoviesPeoplePK;
import com.movie.moviesoapservice.entities.People;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Marina
 */
public class MoviesPeopleJpaController implements Serializable {

    public MoviesPeopleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MoviesPeople moviesPeople) throws PreexistingEntityException, Exception {
        if (moviesPeople.getMoviesPeoplePK() == null) {
            moviesPeople.setMoviesPeoplePK(new MoviesPeoplePK());
        }
        moviesPeople.getMoviesPeoplePK().setPersonId(moviesPeople.getPeople().getPersonId());
        moviesPeople.getMoviesPeoplePK().setMovieId(moviesPeople.getMovies().getMovieId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movies movies = moviesPeople.getMovies();
            if (movies != null) {
                movies = em.getReference(movies.getClass(), movies.getMovieId());
                moviesPeople.setMovies(movies);
            }
            People people = moviesPeople.getPeople();
            if (people != null) {
                people = em.getReference(people.getClass(), people.getPersonId());
                moviesPeople.setPeople(people);
            }
            em.persist(moviesPeople);
            if (movies != null) {
                movies.getMoviesPeopleCollection().add(moviesPeople);
                movies = em.merge(movies);
            }
            if (people != null) {
                people.getMoviesPeopleCollection().add(moviesPeople);
                people = em.merge(people);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMoviesPeople(moviesPeople.getMoviesPeoplePK()) != null) {
                throw new PreexistingEntityException("MoviesPeople " + moviesPeople + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MoviesPeople moviesPeople) throws NonexistentEntityException, Exception {
        moviesPeople.getMoviesPeoplePK().setPersonId(moviesPeople.getPeople().getPersonId());
        moviesPeople.getMoviesPeoplePK().setMovieId(moviesPeople.getMovies().getMovieId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MoviesPeople persistentMoviesPeople = em.find(MoviesPeople.class, moviesPeople.getMoviesPeoplePK());
            Movies moviesOld = persistentMoviesPeople.getMovies();
            Movies moviesNew = moviesPeople.getMovies();
            People peopleOld = persistentMoviesPeople.getPeople();
            People peopleNew = moviesPeople.getPeople();
            if (moviesNew != null) {
                moviesNew = em.getReference(moviesNew.getClass(), moviesNew.getMovieId());
                moviesPeople.setMovies(moviesNew);
            }
            if (peopleNew != null) {
                peopleNew = em.getReference(peopleNew.getClass(), peopleNew.getPersonId());
                moviesPeople.setPeople(peopleNew);
            }
            moviesPeople = em.merge(moviesPeople);
            if (moviesOld != null && !moviesOld.equals(moviesNew)) {
                moviesOld.getMoviesPeopleCollection().remove(moviesPeople);
                moviesOld = em.merge(moviesOld);
            }
            if (moviesNew != null && !moviesNew.equals(moviesOld)) {
                moviesNew.getMoviesPeopleCollection().add(moviesPeople);
                moviesNew = em.merge(moviesNew);
            }
            if (peopleOld != null && !peopleOld.equals(peopleNew)) {
                peopleOld.getMoviesPeopleCollection().remove(moviesPeople);
                peopleOld = em.merge(peopleOld);
            }
            if (peopleNew != null && !peopleNew.equals(peopleOld)) {
                peopleNew.getMoviesPeopleCollection().add(moviesPeople);
                peopleNew = em.merge(peopleNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MoviesPeoplePK id = moviesPeople.getMoviesPeoplePK();
                if (findMoviesPeople(id) == null) {
                    throw new NonexistentEntityException("The moviesPeople with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MoviesPeoplePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MoviesPeople moviesPeople;
            try {
                moviesPeople = em.getReference(MoviesPeople.class, id);
                moviesPeople.getMoviesPeoplePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The moviesPeople with id " + id + " no longer exists.", enfe);
            }
            Movies movies = moviesPeople.getMovies();
            if (movies != null) {
                movies.getMoviesPeopleCollection().remove(moviesPeople);
                movies = em.merge(movies);
            }
            People people = moviesPeople.getPeople();
            if (people != null) {
                people.getMoviesPeopleCollection().remove(moviesPeople);
                people = em.merge(people);
            }
            em.remove(moviesPeople);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MoviesPeople> findMoviesPeopleEntities() {
        return findMoviesPeopleEntities(true, -1, -1);
    }

    public List<MoviesPeople> findMoviesPeopleEntities(int maxResults, int firstResult) {
        return findMoviesPeopleEntities(false, maxResults, firstResult);
    }

    private List<MoviesPeople> findMoviesPeopleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MoviesPeople.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public MoviesPeople findMoviesPeople(MoviesPeoplePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MoviesPeople.class, id);
        } finally {
            em.close();
        }
    }

    public int getMoviesPeopleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MoviesPeople> rt = cq.from(MoviesPeople.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
