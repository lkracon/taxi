package com.taxi.service;

import java.util.List;
import java.util.Map;

import com.taxi.model.Taxi;
import com.vividsolutions.jts.geom.Point;

/**
 * This service is created for support taxis functionality. It's used for
 * manipulations on taxi data in application.
 * 
 * @author Łukasz Kracoń
 * 
 */
public interface TaxiService {

    /**
     * Method used to find taxi with specified id
     * 
     * @param id
     *            - user id
     * @return taxi or null if not exist
     */
    Taxi getTaxi(long id);

    /**
     * Returns list of all taxis in application
     * 
     * @return if there is no taxi, returns empty list
     */
    List<Taxi> getAll();

    /**
     * Converts JSON string into taxi object.
     * 
     * @param json
     *            - string representing taxi
     * 
     *            <pre>
     * {
     * 	"userId":1, 
     * 	"longitude":19.846278, 
     * 	"latitude": 50.93063, 
     * 	"status":"occupied"
     * }
     * </pre>
     * @return
     * @throws JsonProcessingException
     * @throws IOException
     * @throws MissingServletRequestParameterException
     *             - if some arguments are missing
     * @throws UserException
     *             - if there is no user with specified id, code 101.
     */
    // Taxi json2Taxi(String json) throws IOException,
    // MissingServletRequestParameterException, UserException;

    /**
     * creates Point based on x and y.
     * 
     * @param x
     * @param y
     * @return
     */
    Point createPoint(double x, double y);

    /**
     * Saves or if id is not present updates taxi in database
     * 
     * @param taxi
     * @return
     */
    Taxi saveTaxi(Taxi taxi);

    /**
     * Returns all taxis which are closer than specified distance from point
     * 
     * @param latitude
     *            - latitude of point
     * @param longitude
     *            - longitude of point
     * @param radius
     *            - distance in meters
     * @return
     */
    List<Taxi> getByLatitudeLongitudeAndRadius(Double latitude, Double longitude, Integer radius);

    Taxi updateTaxi(Taxi taxi);

    /**
     * Returns taxis paginated and ordered
     * 
     * @param order
     * @param asc
     * @param startIndex
     * @param limit
     * @return
     */
    List<Taxi> getAll(String order, String asc, long startIndex, long limit);

    /**
     * Returns number of taxis which are closer than specified distance from
     * point
     * 
     * @param latitude
     * @param longitude
     * @param radius
     *            in meters
     * @param secondsFrom
     * @param secondsTo
     * @return
     */
    Long count(Double latitude, Double longitude, Integer radius, int secondsFrom, int secondsTo);

	Taxi saveLocation(Taxi taxi, Double latitude, Double longitude);

	List<Map> getTaxiByDistance(Double latitude, Double longitude);
}
