package lk.rgd.crs.core.service;

import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.api.service.BirthRegisterService;

import org.apache.log4j.Logger;

import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;

public class BirthRegisterServiceImpl implements BirthRegisterService {

    private Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext
    protected EntityManager em;

    public void birthRegistration(BirthDeclaration br) {
        em.persist(br);
    }
}
