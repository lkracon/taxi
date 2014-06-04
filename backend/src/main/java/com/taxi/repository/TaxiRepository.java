package com.taxi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.taxi.model.Taxi;

public interface TaxiRepository extends JpaRepository<Taxi, Long>, TaxiRepositoryCustom {

    @Query(nativeQuery = true, value = "SELECT * FROM taxi WHERE created_at >= now() - INTERVAL '9 minute' AND status <> 'occupied' AND ST_Distance_Sphere(location, ST_SetSRID(ST_GeometryFromText(?1), 4326)) < ?2")
    List<Taxi> findByLatitudeLongitudeAndRadius(String point, Integer radius);

    @Query(nativeQuery = true, value = "SELECT * FROM taxi ORDER BY created_at ASC LIMIT ?1 OFFSET ?2")
    List<Taxi> findAllOrderByTimestampAsc(long limit, long offset);

    @Query(nativeQuery = true, value = "SELECT * FROM taxi ORDER BY created_at DESC LIMIT ?1 OFFSET ?2")
    List<Taxi> findAllOrderByTimestampDesc(long limit, long offset);

    @Query(nativeQuery = true, value = "SELECT * FROM taxi ORDER BY status ASC LIMIT ?1 OFFSET ?2")
    List<Taxi> findAllOrderByStatusAsc(long limit, long offset);

    @Query(nativeQuery = true, value = "SELECT * FROM taxi ORDER BY status DESC LIMIT ?1 OFFSET ?2")
    List<Taxi> findAllOrderByStatusDesc(long limit, long offset);

    @Query(nativeQuery = true, value = "SELECT * FROM taxi LIMIT ?1 OFFSET ?2")
    List<Taxi> findAll(long limit, long offset);

}