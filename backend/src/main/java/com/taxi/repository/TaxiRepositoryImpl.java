package com.taxi.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class TaxiRepositoryImpl implements TaxiRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long count(Double latitude, Double longitude, Integer radius, int secondsFrom, int secondsTo) {
        String point = "POINT(" + longitude + " " + latitude + ")";
        Query query = entityManager
                .createNativeQuery("SELECT Count(*) FROM taxi WHERE created_at <= now() - INTERVAL '"
                        + secondsTo
                        + " seconds' AND created_at >= now() - INTERVAL '"
                        + secondsFrom
                        + " seconds' AND status <> 'occupied' AND ST_Distance_Sphere(location, ST_SetSRID(ST_GeometryFromText(?1), 4326)) < ?2");
        query.setParameter(1, point);
        query.setParameter(2, radius);
        return ((BigInteger) query.getSingleResult()).longValue();
    }
    
    
    @Override
    public List getNearest(Double latitude, Double longitude) {
    	String point = "POINT(" + latitude + " " + longitude + ")";
    	Query query = entityManager
                .createNativeQuery(
                		"SELECT * FROM( SELECT DISTINCT ON(taxi_id) ST_Distance_Sphere("
                		+"ST_GeomFromText('"+point+"',4326),"
                		+"location"
                		+") as distance, taxi_id  FROM taxi_location ORDER BY taxi_id, timestamp DESC"
                		+")as asd order by distance");
    	return query.getResultList();
    }
}
