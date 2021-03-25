package org.springframework.samples.petclinic.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "rooms")
public class Room extends BaseEntity{
	@Column(name = "roomNumber")
	private Integer roomNumber;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
	private Set<Booking> bookings;

	public Set<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(Set<Booking> bookings) {
		this.bookings = bookings;
	}
	
	

}
