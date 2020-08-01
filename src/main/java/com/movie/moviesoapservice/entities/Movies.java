/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movie.moviesoapservice.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Marina
 */
@XmlRootElement
@Entity
@Table(name = "MOVIES")
@NamedQueries({
    @NamedQuery(name = "Movies.findAll", query = "SELECT m FROM Movies m"),
    @NamedQuery(name = "Movies.findByMovieId", query = "SELECT m FROM Movies m WHERE m.movieId = :movieId"),
    @NamedQuery(name = "Movies.findByMovieName", query = "SELECT m FROM Movies m WHERE m.movieName = :movieName"),
    @NamedQuery(name = "Movies.findByMovieType", query = "SELECT m FROM Movies m WHERE m.movieType = :movieType"),
    @NamedQuery(name = "Movies.findByMovieSeries", query = "SELECT m FROM Movies m WHERE m.movieSeries = :movieSeries"),
    @NamedQuery(name = "Movies.findByDateUpload", query = "SELECT m FROM Movies m WHERE m.dateUpload = :dateUpload"),
    @NamedQuery(name = "Movies.findByDateRelease", query = "SELECT m FROM Movies m WHERE m.dateRelease = :dateRelease")})
public class Movies implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "MOVIE_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger movieId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MOVIE_NAME")
    private String movieName;
    @Size(max = 13)
    @Column(name = "MOVIE_TYPE")
    private String movieType;
    @Size(max = 30)
    @Column(name = "MOVIE_FILENAME")
    private String movieFilename;
    @Lob
    @Column(name = "MOVIE_CONTENT")
    private byte[] movieContent;
    @Size(max = 50)
    @Column(name = "MOVIE_SERIES")
    private String movieSeries;
    @Column(name = "DATE_UPLOAD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpload;
    @Column(name = "DATE_RELEASE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRelease;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movies")
    private Collection<MoviesPeople> moviesPeopleCollection;

    public Movies() {
    }

    public Movies(BigInteger movieId) {
        this.movieId = movieId;
    }

    public Movies(BigInteger movieId, String movieName) {
        this.movieId = movieId;
        this.movieName = movieName;
    }

    public BigInteger getMovieId() {
        return movieId;
    }

    public void setMovieId(BigInteger movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }
    
    
    public String getMovieFilename() {
        return movieFilename;
    }

    public void setMovieFilename(String movieFilename){
        this.movieFilename = movieFilename;
    }

    public byte[] getMovieContent() {
        return movieContent;
    }

    public void setMovieContent(byte[] movieContent) {
        this.movieContent = movieContent;
    }

    public String getMovieSeries() {
        return movieSeries;
    }

    public void setMovieSeries(String movieSeries) {
        this.movieSeries = movieSeries;
    }

    public Date getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(Date dateUpload) {
        this.dateUpload = dateUpload;
    }

    public Date getDateRelease() {
        return dateRelease;
    }

    public void setDateRelease(Date dateRelease) {
        this.dateRelease = dateRelease;
    }

    @XmlTransient
    public Collection<MoviesPeople> getMoviesPeopleCollection() {
        return moviesPeopleCollection;
    }

    public void setMoviesPeopleCollection(Collection<MoviesPeople> moviesPeopleCollection) {
        this.moviesPeopleCollection = moviesPeopleCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (movieId != null ? movieId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Movies)) {
            return false;
        }
        Movies other = (Movies) object;
        if ((this.movieId == null && other.movieId != null) || (this.movieId != null && !this.movieId.equals(other.movieId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.movie.moviesoapservice.entities.Movies[ movieId=" + movieId + " ]";
    }
    
}
