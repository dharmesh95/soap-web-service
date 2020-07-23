/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humber.soapwebservice.controllers;

import com.humber.soapwebservice.controllers.exceptions.NonexistentEntityException;
import com.humber.soapwebservice.controllers.exceptions.PreexistingEntityException;
import com.humber.soapwebservice.models.TvShows;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Dharmesh
 */
public class TvShowsJpaController implements Serializable {

    public TvShowsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TvShows tvShows) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tvShows);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTvShows(tvShows.getId()) != null) {
                throw new PreexistingEntityException("TvShows " + tvShows + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TvShows tvShows) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tvShows = em.merge(tvShows);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = tvShows.getId();
                if (findTvShows(id) == null) {
                    throw new NonexistentEntityException("The tvShows with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TvShows tvShows;
            try {
                tvShows = em.getReference(TvShows.class, id);
                tvShows.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tvShows with id " + id + " no longer exists.", enfe);
            }
            em.remove(tvShows);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TvShows> findTvShowsEntities() {
        return findTvShowsEntities(true, -1, -1);
    }

    public List<TvShows> findTvShowsEntities(int maxResults, int firstResult) {
        return findTvShowsEntities(false, maxResults, firstResult);
    }

    private List<TvShows> findTvShowsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TvShows.class));
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

    public TvShows findTvShows(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TvShows.class, id);
        } finally {
            em.close();
        }
    }

    public int getTvShowsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TvShows> rt = cq.from(TvShows.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
