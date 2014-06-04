package com.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taxi.model.TaxiLocation;

public interface TaxiLocationRepository extends JpaRepository<TaxiLocation, Long> {

}