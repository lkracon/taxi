package com.taxi.controller.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taxi.exception.UserException;
import com.taxi.model.Taxi;
import com.taxi.repository.TaxiRepository;
import com.taxi.service.TaxiService;
import com.taxi.service.UserService;

@Transactional
@Controller
@RequestMapping("/api/taxi")
public class TaxiController {

    private static final Logger logger = Logger.getLogger(TaxiController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private TaxiService taxiService;

    @Autowired
    private TaxiRepository taxiRepository;

 /*   @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    @ResponseBody
    public List<Taxi> getAllTaxis(@RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude, @RequestParam(required = false) Integer radius)
            throws MissingServletRequestParameterException {

        if (latitude == null) {
            throw new MissingServletRequestParameterException("latitude", "Double");
        }
        if (longitude == null) {
            throw new MissingServletRequestParameterException("longitude", "Double");
        }
        if (radius == null) {
            throw new MissingServletRequestParameterException("radius", "Integer");
        }

        return taxiService.getByLatitudeLongitudeAndRadius(latitude, longitude, radius);
    }*/
    
    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    @ResponseBody
    public List<Taxi> getAllTaxis(){
        return taxiService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    @ResponseBody
    public Taxi getTaxi(@PathVariable Long id) {
        return taxiService.getTaxi(id);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    @ResponseBody
    public Taxi addTaxi(@RequestBody Taxi taxi) throws MissingServletRequestParameterException, UserException,
            IOException {
        return taxiService.saveTaxi(taxi);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Taxi updateTaxi(@PathVariable Long id, @RequestBody Taxi taxi) throws IOException, MissingServletRequestParameterException,
            UserException {
        if (taxi.getId() == null) {
            throw new MissingServletRequestParameterException("id", "Long");
        }
        return taxiService.updateTaxi(taxi);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Taxi deleteTaxi(@PathVariable Long id) {
        Taxi taxi = taxiService.getTaxi(id);
        taxiRepository.delete(id);
        return taxi;
    }
    
    @RequestMapping("/{id}/random")
    @ResponseBody
    public Taxi addTaxiLocation(@RequestParam Double minLatitude, @RequestParam Double minLongitude,
            @RequestParam Double maxLatitude, @RequestParam Double maxLongitude, @PathVariable Long id) {
    	
    	Taxi taxi = taxiRepository.findOne(id);

        Random r = new Random();
        double latitude = minLatitude + (maxLatitude - minLatitude) * r.nextDouble();
        double longitude = minLongitude + (maxLongitude - minLongitude) * r.nextDouble();

        taxiService.createPoint(latitude, longitude);

        taxi = taxiService.saveLocation(taxi, latitude, longitude);

        logger.debug("Saved spot: " + taxi);

        return taxi;
    }
    
    @RequestMapping("/{id}/location")
    @ResponseBody
    public Taxi addTaxiLocation(@RequestParam Double latitude, @RequestParam Double longitude, @PathVariable Long id) {
    	
    	Taxi taxi = taxiRepository.findOne(id);

        taxiService.createPoint(latitude, longitude);

        taxi = taxiService.saveLocation(taxi, latitude, longitude);

        logger.debug("Saved spot: " + taxi);

        return taxi;
    }
    
    @RequestMapping("/nearest")
    @ResponseBody
    public List<Map> getTaxiByDistance(@RequestParam Double latitude, @RequestParam Double longitude) {

        taxiService.createPoint(latitude, longitude);

        List<Map> taxi = taxiService.getTaxiByDistance(latitude, longitude);

        logger.debug("Saved spot: " + taxi);

        return taxi;
    }
}
