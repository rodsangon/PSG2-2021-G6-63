package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
	
	private RoomRepository roomRepository;
	
	@Autowired
	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}
	
	

}
