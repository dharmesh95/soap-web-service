/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humber.soapwebservice.services;

import com.humber.soapwebservice.controllers.MoviesJpaController;
import com.humber.soapwebservice.models.Movies;
import java.math.BigDecimal;
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
    public int addMovie(@WebParam(name = "moviename") String movieName, @WebParam(name = "image") byte[] image) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SOAP_PU");

        MoviesJpaController movieRepo = new MoviesJpaController(emf);

        Movies movie = new Movies();
        movie.setName(movieName);
        movie.setImage(image);
        movie.setId(BigDecimal.valueOf(movieRepo.getMoviesCount() + 1));

        movieRepo.create(movie);

        return movieRepo.getMoviesCount() + 1;
    }
}
