/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movie.moviesoapservice.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Marina
 */
@Entity
@Table(name = "MOVIES_PEOPLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MoviesPeople.findAll", query = "SELECT m FROM MoviesPeople m"),
    @NamedQuery(name = "MoviesPeople.findByMovieId", query = "SELECT m FROM MoviesPeople m WHERE m.moviesPeoplePK.movieId = :movieId"),
    @NamedQuery(name = "MoviesPeople.findByPersonId", query = "SELECT m FROM MoviesPeople m WHERE m.moviesPeoplePK.personId = :personId"),
    @NamedQuery(name = "MoviesPeople.findByRole", query = "SELECT m FROM MoviesPeople m WHERE m.role = :role")})
public class MoviesPeople implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MoviesPeoplePK moviesPeoplePK;
    @Size(max = 15)
    @Column(name = "ROLE")
    private String role;
    @JoinColumn(name = "MOVIE_ID", referencedColumnName = "MOVIE_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Movies movies;
    @JoinColumn(name = "PERSON_ID", referencedColumnName = "PERSON_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private People people;

    public MoviesPeople() {
    }

    public MoviesPeople(MoviesPeoplePK moviesPeoplePK) {
        this.moviesPeoplePK = moviesPeoplePK;
    }

    public MoviesPeople(BigInteger movieId, BigInteger personId) {
        this.moviesPeoplePK = new MoviesPeoplePK(movieId, personId);
    }

    public MoviesPeoplePK getMoviesPeoplePK() {
        return moviesPeoplePK;
    }

    public void setMoviesPeoplePK(MoviesPeoplePK moviesPeoplePK) {
        this.moviesPeoplePK = moviesPeoplePK;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Movies getMovies() {
        return movies;
    }

    public void setMovies(Movies movies) {
        this.movies = movies;
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (moviesPeoplePK != null ? moviesPeoplePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MoviesPeople)) {
            return false;
        }
        MoviesPeople other = (MoviesPeople) object;
        if ((this.moviesPeoplePK == null && other.moviesPeoplePK != null) || (this.moviesPeoplePK != null && !this.moviesPeoplePK.equals(other.moviesPeoplePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.movie.moviesoapservice.entities.MoviesPeople[ moviesPeoplePK=" + moviesPeoplePK + " ]";
    }
    
}
