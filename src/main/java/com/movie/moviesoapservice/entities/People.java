/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movie.moviesoapservice.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Marina
 */
@Entity
@Table(name = "PEOPLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "People.findAll", query = "SELECT p FROM People p"),
    @NamedQuery(name = "People.findByPersonId", query = "SELECT p FROM People p WHERE p.personId = :personId"),
    @NamedQuery(name = "People.findByPersonName", query = "SELECT p FROM People p WHERE p.personName = :personName")})
public class People implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PERSON_ID")
//    @GeneratedValue(strategy = GenerationType.AUTO )
    private BigInteger personId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PERSON_NAME")
    private String personName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "people")
    private Collection<MoviesPeople> moviesPeopleCollection;

    public People() {
    }

    public People(BigInteger personId) {
        this.personId = personId;
    }

    public People(BigInteger personId, String personName) {
        this.personId = personId;
        this.personName = personName;
    }

    public BigInteger getPersonId() {
        return personId;
    }

    public void setPersonId(BigInteger personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
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
        hash += (personId != null ? personId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof People)) {
            return false;
        }
        People other = (People) object;
        if ((this.personId == null && other.personId != null) || (this.personId != null && !this.personId.equals(other.personId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.movie.moviesoapservice.entities.People[ personId=" + personId + " ]";
    }
    
}
