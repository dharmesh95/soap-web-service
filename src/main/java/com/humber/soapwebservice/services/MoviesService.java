/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humber.soapwebservice.services;

import com.humber.soapwebservice.controllers.MoviesJpaController;
import com.humber.soapwebservice.models.Movies;
import java.math.BigDecimal;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.ws.soap.MTOM;

/**
 *
 * @author Dharmesh
 */
@WebService
@MTOM(enabled = true, threshold = 3000)
public class MoviesService {

    @WebMethod
    public Movies addMovie(@WebParam(name = "moviename") String movieName, @WebParam(name = "image") byte[] image) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SOAP_PU");

        MoviesJpaController movieRepo = new MoviesJpaController(emf);

        Movies movie = new Movies();
        movie.setName(movieName);
        movie.setImage(image);
        movie.setId(BigDecimal.valueOf(movieRepo.getMoviesCount() + 1));

        movieRepo.create(movie);

        return movie;
    }

    @WebMethod
    public List<Movies> getMovies() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SOAP_PU");

        MoviesJpaController movieRepo = new MoviesJpaController(emf);

        List<Movies> findMoviesEntities = movieRepo.findMoviesEntities(10, 0);

        return findMoviesEntities;
    }

    @WebMethod
    public Movies updateMovie(@WebParam(name = "id") int id, @WebParam(name = "moviename") String movieName, @WebParam(name = "image") byte[] image) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SOAP_PU");

        MoviesJpaController movieRepo = new MoviesJpaController(emf);

        Movies findMovies = movieRepo.findMovies(BigDecimal.valueOf(id));
        movieRepo.destroy(BigDecimal.valueOf(id));

        findMovies.setName(movieName);
        findMovies.setImage(image);
        findMovies.setId(BigDecimal.valueOf(movieRepo.getMoviesCount() + 1));

        movieRepo.create(findMovies);

        return findMovies;
    }

    @WebMethod
    public int deleteMovieById(@WebParam(name = "id") int id) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SOAP_PU");

        MoviesJpaController movieRepo = new MoviesJpaController(emf);
        movieRepo.destroy(BigDecimal.valueOf(id));

        return id;
    }

}
