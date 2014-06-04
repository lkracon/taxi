package com.taxi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taxi.model.Taxi;
import com.taxi.model.TaxiLocation;
import com.taxi.repository.TaxiLocationRepository;
import com.taxi.repository.TaxiRepository;
import com.taxi.repository.UserRepository;
import com.taxi.service.TaxiService;
import com.taxi.service.UserService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

@Service
@Transactional
public class TaxiServiceImpl implements TaxiService {
	
	private static final Logger logger = Logger.getLogger(TaxiServiceImpl.class);

    private static final int SRID = 4326;

    @Autowired
    private TaxiRepository taxiRepository;
    
    @Autowired
    private TaxiLocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public Taxi getTaxi(long id) {
        return taxiRepository.findOne(id);
    }

    @Override
    public List<Taxi> getAll() {
        return taxiRepository.findAll();
    }

    @Override
    public List<Taxi> getAll(String order, String asc, long startIndex, long limit) {
        if (order.equals("timestamp")) {
            if (asc.equalsIgnoreCase("asc")) {
                return taxiRepository.findAllOrderByTimestampAsc(limit, startIndex);
            } else {
                return taxiRepository.findAllOrderByTimestampDesc(limit, startIndex);
            }
        } else if (order.equals("status")) {
            if (asc.equalsIgnoreCase("asc")) {
                return taxiRepository.findAllOrderByStatusAsc(limit, startIndex);
            } else {
                return taxiRepository.findAllOrderByStatusDesc(limit, startIndex);
            }
        } else {
            return taxiRepository.findAll(limit, startIndex);
        }

    }

    @Override
    public Taxi saveTaxi(Taxi taxi) {
        return taxiRepository.save(taxi);
    }

    /*
     * @Override public Taxi json2Taxi(String json) throws
     * MissingServletRequestParameterException, IOException, UserException {
     * ObjectMapper mapper = new ObjectMapper(); JsonNode ob =
     * mapper.readTree(json); Taxi taxi = new Taxi();
     * 
     * if (!ob.has("longitude")) { throw new
     * MissingServletRequestParameterException("longitude", "Double"); }
     * 
     * if (!ob.has("latitude")) { throw new
     * MissingServletRequestParameterException("latitude", "Double"); }
     * 
     * User user = null; if (ob.has("userId")) { user =
     * userRepository.findOne(ob.get("userId").asLong()); if (user == null) {
     * throw new UserException("User with id " + ob.get("userId").asLong() +
     * " does not exist", UserException.USER_NOT_FOUND); } }
     * 
     * if (!ob.has("status")) { throw new
     * MissingServletRequestParameterException("status", "String"); }
     * 
     * taxi.setStatus(ob.get("status").asText()); taxi.setTimestamp(new Date());
     * taxi.setLocation(createPoint(ob.get("longitude").asDouble(),
     * ob.get("latitude").asDouble())); if (ob.has("taxiId")) {
     * taxi.setId(ob.get("taxiId").asLong()); }
     * 
     * return taxi; }
     */

    @Override
    public Point createPoint(double longitude, double latitude) {
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), SRID);
        Coordinate coord = new Coordinate(longitude, latitude);
        return gf.createPoint(coord);
    }

    @Override
    public List<Taxi> getByLatitudeLongitudeAndRadius(Double latitude, Double longitude, Integer radius) {
        String point = "POINT(" + longitude + " " + latitude + ")";
        return taxiRepository.findByLatitudeLongitudeAndRadius(point, radius);
    }

    @Override
    public Long count(Double latitude, Double longitude, Integer radius, int secondsFrom, int secondsTo) {
        return taxiRepository.count(latitude, longitude, radius, secondsFrom, secondsTo);
    }

    @Override
    public Taxi updateTaxi(Taxi taxi) {
        Taxi existing = taxiRepository.findOne(taxi.getId());
        existing.setUser(taxi.getUser());
        if (taxi.getStatus() != null) {
            existing.setStatus(taxi.getStatus());
        }
        if (taxi.getTimestamp() != null) {
            existing.setTimestamp(taxi.getTimestamp());
        }
        return taxiRepository.save(existing);
    }
    
    @Override
    public Taxi saveLocation(Taxi taxi, Double latitude, Double longitude) {
    	TaxiLocation location = new TaxiLocation();
    	location.setLocation(createPoint(longitude, latitude));
    	location.setTimestamp(new Date());
    	location.setTaxi(taxi);
        return locationRepository.save(location).getTaxi(); 
    }

	@Override
    public List<Map> getTaxiByDistance(Double latitude, Double longitude) {
		List nearest = taxiRepository.getNearest(longitude, latitude);
		
		List<Map> nearestMap = new ArrayList<Map>();
		
		for (Object object : nearest) {
			Object[] tmp = (Object[])object;
			Integer id = (Integer)tmp[1];
			Double distance = (Double)tmp[0];
			logger.info(id + " "+distance);
			
			Map map = new HashMap<String, Object>();
			map.put("distance", distance);
			map.put("taxi", taxiRepository.findOne(id.longValue()));
			
			nearestMap.add(map);
			
        }
	    return nearestMap;
    }
}
