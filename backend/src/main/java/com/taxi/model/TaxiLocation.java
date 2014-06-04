package com.taxi.model;

import java.io.IOException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Point;

@Entity
@Table(name = "taxi_location")
@Inheritance(strategy = InheritanceType.JOINED)
public class TaxiLocation implements JsonSerializableWithType {

    @Id
    @GeneratedValue(generator = "taxi_location_id", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "taxi_location_id", sequenceName = "taxi_location_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "location", columnDefinition = "Geometry")
    @Type(type = "org.hibernate.spatial.GeometryType")
    private Point location;

    @Column(name = "timestamp")
    private Date timestamp;
    
    @ManyToOne
    @JoinColumn(name="taxi_id")
    private Taxi taxi;

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    @Override
    public void serialize(JsonGenerator jgen, SerializerProvider provider) throws IOException {

        jgen.writeStartObject();
        if (id == null) {
            jgen.writeNullField("id");
        } else {
            jgen.writeNumberField("id", id);
        }

        if (getTimestamp() == null) {
            jgen.writeNullField("timestamp");
        } else {
            jgen.writeNumberField("timestamp", getTimestamp().getTime() / 1000);
        }

        if (location == null) {
            jgen.writeNullField("longitude");
        } else {
            jgen.writeNumberField("longitude", location.getX());
        }

        if (location == null) {
            jgen.writeNullField("latitude");
        } else {
            jgen.writeNumberField("latitude", location.getY());
        }

        jgen.writeEndObject();
    }

    @Override
    public void serializeWithType(JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) {
    }

	public Taxi getTaxi() {
	    return taxi;
    }

	public void setTaxi(Taxi taxi) {
	    this.taxi = taxi;
    }

	public Date getTimestamp() {
	    return timestamp;
    }

	public void setTimestamp(Date timestamp) {
	    this.timestamp = timestamp;
    }
}
