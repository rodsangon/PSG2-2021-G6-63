package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Booking;

public interface BookingRepository extends  CrudRepository<Booking, Integer>{

}
