/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humber.soapwebservice.services;

import com.humber.soapwebservice.controllers.TvShowsJpaController;
import com.humber.soapwebservice.models.TvShows;
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
public class TvShowsService {
    
    @WebMethod
    public TvShows addTvShow(@WebParam(name = "tvshowname") String tvshowName, @WebParam(name = "image") byte[] image) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SOAP_PU");

        TvShowsJpaController tvshowRepo = new TvShowsJpaController(emf);

        TvShows tvshow = new TvShows();
        tvshow.setName(tvshowName);
        tvshow.setImage(image);
        tvshow.setId(BigDecimal.valueOf(tvshowRepo.getTvShowsCount() + 1));

        tvshowRepo.create(tvshow);

        return tvshow;
    }

    @WebMethod
    public List<TvShows> getTvShows() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SOAP_PU");

        TvShowsJpaController tvshowRepo = new TvShowsJpaController(emf);

        List<TvShows> findTvShowsEntities = tvshowRepo.findTvShowsEntities(10, 0);

        return findTvShowsEntities;
    }

    @WebMethod
    public TvShows updateTvShow(@WebParam(name = "id") int id, @WebParam(name = "tvshowname") String tvshowName, @WebParam(name = "image") byte[] image) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SOAP_PU");

        TvShowsJpaController tvshowRepo = new TvShowsJpaController(emf);

        TvShows findTvShows = tvshowRepo.findTvShows(BigDecimal.valueOf(id));
        tvshowRepo.destroy(BigDecimal.valueOf(id));

        findTvShows.setName(tvshowName);
        findTvShows.setImage(image);
        findTvShows.setId(BigDecimal.valueOf(tvshowRepo.getTvShowsCount() + 1));

        tvshowRepo.create(findTvShows);

        return findTvShows;
    }

    @WebMethod
    public int deleteTvShowById(@WebParam(name = "id") int id) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SOAP_PU");

        TvShowsJpaController tvshowRepo = new TvShowsJpaController(emf);
        tvshowRepo.destroy(BigDecimal.valueOf(id));

        return id;
    }
}
