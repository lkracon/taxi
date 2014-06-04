package com.taxi.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@Table(name = "taxi")
@Inheritance(strategy = InheritanceType.JOINED)
public class Taxi {

    @Id
    @GeneratedValue(generator = "taxi_id", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "taxi_id", sequenceName = "taxi_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Date timestamp;
    
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    
    @JsonIgnore
    @OneToMany(mappedBy="taxi")
    private List<TaxiLocation> taxilocations;
    
    @JoinColumn(name="marka")
    private String marka;
    
    @JoinColumn(name="model")
    private String model;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Taxi [id=" + id + ", status=" + status + "]";
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

	public User getUser() {
	    return user;
    }

	public void setUser(User user) {
	    this.user = user;
    }

	public String getMarka() {
	    return marka;
    }

	public void setMarka(String marka) {
	    this.marka = marka;
    }

	public String getModel() {
	    return model;
    }

	public void setModel(String model) {
	    this.model = model;
    }

	public List<TaxiLocation> getTaxilocations() {
	    return taxilocations;
    }

	public void setTaxilocations(List<TaxiLocation> taxilocations) {
	    this.taxilocations = taxilocations;
    }
	
	@JsonProperty
	public TaxiLocation getLastLocation(){
		if(taxilocations == null ||taxilocations.isEmpty()){
			return null;
		}
		return taxilocations.get(taxilocations.size()-1);
	}
}
