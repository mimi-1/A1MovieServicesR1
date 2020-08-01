/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movie.moviesoapservice.controllers;

import com.movie.moviesoapservice.controllers.exceptions.IllegalOrphanException;
import com.movie.moviesoapservice.controllers.exceptions.NonexistentEntityException;
import com.movie.moviesoapservice.entities.Movies;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.movie.moviesoapservice.entities.MoviesPeople;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Marina
 */
public class MoviesJpaController implements Serializable {

    public MoviesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Movies movies) {
        if (movies.getMoviesPeopleCollection() == null) {
            movies.setMoviesPeopleCollection(new ArrayList<MoviesPeople>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<MoviesPeople> attachedMoviesPeopleCollection = new ArrayList<MoviesPeople>();
            for (MoviesPeople moviesPeopleCollectionMoviesPeopleToAttach : movies.getMoviesPeopleCollection()) {
                moviesPeopleCollectionMoviesPeopleToAttach = em.getReference(moviesPeopleCollectionMoviesPeopleToAttach.getClass(), moviesPeopleCollectionMoviesPeopleToAttach.getMoviesPeoplePK());
                attachedMoviesPeopleCollection.add(moviesPeopleCollectionMoviesPeopleToAttach);
            }
            movies.setMoviesPeopleCollection(attachedMoviesPeopleCollection);
            em.persist(movies);
            for (MoviesPeople moviesPeopleCollectionMoviesPeople : movies.getMoviesPeopleCollection()) {
                Movies oldMoviesOfMoviesPeopleCollectionMoviesPeople = moviesPeopleCollectionMoviesPeople.getMovies();
                moviesPeopleCollectionMoviesPeople.setMovies(movies);
                moviesPeopleCollectionMoviesPeople = em.merge(moviesPeopleCollectionMoviesPeople);
                if (oldMoviesOfMoviesPeopleCollectionMoviesPeople != null) {
                    oldMoviesOfMoviesPeopleCollectionMoviesPeople.getMoviesPeopleCollection().remove(moviesPeopleCollectionMoviesPeople);
                    oldMoviesOfMoviesPeopleCollectionMoviesPeople = em.merge(oldMoviesOfMoviesPeopleCollectionMoviesPeople);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Movies movies) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movies persistentMovies = em.find(Movies.class, movies.getMovieId());
            //all this staff with updating moviepeople is not happaning if the moviespeople are null
            Collection<MoviesPeople> moviesPeopleCollectionOld = persistentMovies.getMoviesPeopleCollection();
            Collection<MoviesPeople> moviesPeopleCollectionNew = movies.getMoviesPeopleCollection();
            List<String> illegalOrphanMessages = null;
            
                for (MoviesPeople moviesPeopleCollectionOldMoviesPeople : moviesPeopleCollectionOld) {
                    if (!moviesPeopleCollectionNew.contains(moviesPeopleCollectionOldMoviesPeople)) {
                        if (illegalOrphanMessages == null) {
                            illegalOrphanMessages = new ArrayList<String>();
                    }
                        illegalOrphanMessages.add("You must retain MoviesPeople " + moviesPeopleCollectionOldMoviesPeople + " since its movies field is not nullable.");
                }
                }
                if (illegalOrphanMessages != null) {
                    throw new IllegalOrphanException(illegalOrphanMessages);
                }
                Collection<MoviesPeople> attachedMoviesPeopleCollectionNew = new ArrayList<MoviesPeople>();
                for (MoviesPeople moviesPeopleCollectionNewMoviesPeopleToAttach : moviesPeopleCollectionNew) {
                    moviesPeopleCollectionNewMoviesPeopleToAttach = em.getReference(moviesPeopleCollectionNewMoviesPeopleToAttach.getClass(), moviesPeopleCollectionNewMoviesPeopleToAttach.getMoviesPeoplePK());
                    attachedMoviesPeopleCollectionNew.add(moviesPeopleCollectionNewMoviesPeopleToAttach);
                }
                moviesPeopleCollectionNew = attachedMoviesPeopleCollectionNew;
                movies.setMoviesPeopleCollection(moviesPeopleCollectionNew);
            
                movies = em.merge(movies);
              
                for (MoviesPeople moviesPeopleCollectionNewMoviesPeople : moviesPeopleCollectionNew) {
                    if (!moviesPeopleCollectionOld.contains(moviesPeopleCollectionNewMoviesPeople)) {
                        Movies oldMoviesOfMoviesPeopleCollectionNewMoviesPeople = moviesPeopleCollectionNewMoviesPeople.getMovies();
                        moviesPeopleCollectionNewMoviesPeople.setMovies(movies);
                    moviesPeopleCollectionNewMoviesPeople = em.merge(moviesPeopleCollectionNewMoviesPeople);
                    if (oldMoviesOfMoviesPeopleCollectionNewMoviesPeople != null && !oldMoviesOfMoviesPeopleCollectionNewMoviesPeople.equals(movies)) {
                        oldMoviesOfMoviesPeopleCollectionNewMoviesPeople.getMoviesPeopleCollection().remove(moviesPeopleCollectionNewMoviesPeople);
                        oldMoviesOfMoviesPeopleCollectionNewMoviesPeople = em.merge(oldMoviesOfMoviesPeopleCollectionNewMoviesPeople);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = movies.getMovieId();
                if (findMovies(id) == null) {
                    throw new NonexistentEntityException("The movies with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigInteger id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movies movies;
            try {
                movies = em.getReference(Movies.class, id);
                movies.getMovieId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movies with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<MoviesPeople> moviesPeopleCollectionOrphanCheck = movies.getMoviesPeopleCollection();
            for (MoviesPeople moviesPeopleCollectionOrphanCheckMoviesPeople : moviesPeopleCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Movies (" + movies + ") cannot be destroyed since the MoviesPeople " + moviesPeopleCollectionOrphanCheckMoviesPeople + " in its moviesPeopleCollection field has a non-nullable movies field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(movies);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Movies> findMoviesEntities() {
        return findMoviesEntities(true, -1, -1);
    }

    public List<Movies> findMoviesEntities(int maxResults, int firstResult) {
        return findMoviesEntities(false, maxResults, firstResult);
    }

    private List<Movies> findMoviesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Movies.class));
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

    public Movies findMovies(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Movies.class, id);
        } finally {
            em.close();
        }
    }

    public int getMoviesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Movies> rt = cq.from(Movies.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
