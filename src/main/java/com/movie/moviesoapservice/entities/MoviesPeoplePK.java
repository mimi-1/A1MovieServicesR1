/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movie.moviesoapservice.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Marina
 */
@Embeddable
public class MoviesPeoplePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "MOVIE_ID")
    private BigInteger movieId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PERSON_ID")
    private BigInteger personId;

    public MoviesPeoplePK() {
    }

    public MoviesPeoplePK(BigInteger movieId, BigInteger personId) {
        this.movieId = movieId;
        this.personId = personId;
    }

    public BigInteger getMovieId() {
        return movieId;
    }

    public void setMovieId(BigInteger movieId) {
        this.movieId = movieId;
    }

    public BigInteger getPersonId() {
        return personId;
    }

    public void setPersonId(BigInteger personId) {
        this.personId = personId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (movieId != null ? movieId.hashCode() : 0);
        hash += (personId != null ? personId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MoviesPeoplePK)) {
            return false;
        }
        MoviesPeoplePK other = (MoviesPeoplePK) object;
        if ((this.movieId == null && other.movieId != null) || (this.movieId != null && !this.movieId.equals(other.movieId))) {
            return false;
        }
        if ((this.personId == null && other.personId != null) || (this.personId != null && !this.personId.equals(other.personId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.movie.moviesoapservice.entities.MoviesPeoplePK[ movieId=" + movieId + ", personId=" + personId + " ]";
    }
    
}
