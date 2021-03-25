package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Room;

public interface RoomRepository extends  CrudRepository<Room, Integer>{
	
	List<Room> findAll();

}
