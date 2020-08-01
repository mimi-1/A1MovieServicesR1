/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movie.moviesoapservice.controllers;

import com.movie.moviesoapservice.controllers.exceptions.IllegalOrphanException;
import com.movie.moviesoapservice.controllers.exceptions.NonexistentEntityException;
import com.movie.moviesoapservice.controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.movie.moviesoapservice.entities.MoviesPeople;
import com.movie.moviesoapservice.entities.People;
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
public class PeopleJpaController implements Serializable {

    public PeopleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(People people) throws PreexistingEntityException, Exception {
        if (people.getMoviesPeopleCollection() == null) {
            people.setMoviesPeopleCollection(new ArrayList<MoviesPeople>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<MoviesPeople> attachedMoviesPeopleCollection = new ArrayList<MoviesPeople>();
            for (MoviesPeople moviesPeopleCollectionMoviesPeopleToAttach : people.getMoviesPeopleCollection()) {
                moviesPeopleCollectionMoviesPeopleToAttach = em.getReference(moviesPeopleCollectionMoviesPeopleToAttach.getClass(), moviesPeopleCollectionMoviesPeopleToAttach.getMoviesPeoplePK());
                attachedMoviesPeopleCollection.add(moviesPeopleCollectionMoviesPeopleToAttach);
            }
            people.setMoviesPeopleCollection(attachedMoviesPeopleCollection);
            em.persist(people);
            for (MoviesPeople moviesPeopleCollectionMoviesPeople : people.getMoviesPeopleCollection()) {
                People oldPeopleOfMoviesPeopleCollectionMoviesPeople = moviesPeopleCollectionMoviesPeople.getPeople();
                moviesPeopleCollectionMoviesPeople.setPeople(people);
                moviesPeopleCollectionMoviesPeople = em.merge(moviesPeopleCollectionMoviesPeople);
                if (oldPeopleOfMoviesPeopleCollectionMoviesPeople != null) {
                    oldPeopleOfMoviesPeopleCollectionMoviesPeople.getMoviesPeopleCollection().remove(moviesPeopleCollectionMoviesPeople);
                    oldPeopleOfMoviesPeopleCollectionMoviesPeople = em.merge(oldPeopleOfMoviesPeopleCollectionMoviesPeople);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPeople(people.getPersonId()) != null) {
                throw new PreexistingEntityException("People " + people + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(People people) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            People persistentPeople = em.find(People.class, people.getPersonId());
            Collection<MoviesPeople> moviesPeopleCollectionOld = persistentPeople.getMoviesPeopleCollection();
            Collection<MoviesPeople> moviesPeopleCollectionNew = people.getMoviesPeopleCollection();
            List<String> illegalOrphanMessages = null;
            for (MoviesPeople moviesPeopleCollectionOldMoviesPeople : moviesPeopleCollectionOld) {
                if (!moviesPeopleCollectionNew.contains(moviesPeopleCollectionOldMoviesPeople)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MoviesPeople " + moviesPeopleCollectionOldMoviesPeople + " since its people field is not nullable.");
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
            people.setMoviesPeopleCollection(moviesPeopleCollectionNew);
            people = em.merge(people);
            for (MoviesPeople moviesPeopleCollectionNewMoviesPeople : moviesPeopleCollectionNew) {
                if (!moviesPeopleCollectionOld.contains(moviesPeopleCollectionNewMoviesPeople)) {
                    People oldPeopleOfMoviesPeopleCollectionNewMoviesPeople = moviesPeopleCollectionNewMoviesPeople.getPeople();
                    moviesPeopleCollectionNewMoviesPeople.setPeople(people);
                    moviesPeopleCollectionNewMoviesPeople = em.merge(moviesPeopleCollectionNewMoviesPeople);
                    if (oldPeopleOfMoviesPeopleCollectionNewMoviesPeople != null && !oldPeopleOfMoviesPeopleCollectionNewMoviesPeople.equals(people)) {
                        oldPeopleOfMoviesPeopleCollectionNewMoviesPeople.getMoviesPeopleCollection().remove(moviesPeopleCollectionNewMoviesPeople);
                        oldPeopleOfMoviesPeopleCollectionNewMoviesPeople = em.merge(oldPeopleOfMoviesPeopleCollectionNewMoviesPeople);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = people.getPersonId();
                if (findPeople(id) == null) {
                    throw new NonexistentEntityException("The people with id " + id + " no longer exists.");
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
            People people;
            try {
                people = em.getReference(People.class, id);
                people.getPersonId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The people with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<MoviesPeople> moviesPeopleCollectionOrphanCheck = people.getMoviesPeopleCollection();
            for (MoviesPeople moviesPeopleCollectionOrphanCheckMoviesPeople : moviesPeopleCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This People (" + people + ") cannot be destroyed since the MoviesPeople " + moviesPeopleCollectionOrphanCheckMoviesPeople + " in its moviesPeopleCollection field has a non-nullable people field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(people);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<People> findPeopleEntities() {
        return findPeopleEntities(true, -1, -1);
    }

    public List<People> findPeopleEntities(int maxResults, int firstResult) {
        return findPeopleEntities(false, maxResults, firstResult);
    }

    private List<People> findPeopleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(People.class));
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

    public People findPeople(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(People.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeopleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<People> rt = cq.from(People.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
