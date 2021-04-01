/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Room;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.BookingRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.RoomRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.web.VisitController;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class PetService {
	
	Logger logger = LoggerFactory.getLogger(PetService.class);

	private PetRepository petRepository;
	
	private VisitRepository visitRepository;
	
	private BookingRepository bookingRepository;
	
	private RoomRepository roomRepository;
	
	@Autowired
	public PetService(PetRepository petRepository,
			VisitRepository visitRepository) {
		this.petRepository = petRepository;
		this.visitRepository = visitRepository;
	}

	@Transactional(readOnly = true)
	public Collection<PetType> findPetTypes() throws DataAccessException {
		return petRepository.findPetTypes();
	}
	
	@Transactional
	public void saveVisit(Visit visit) throws DataAccessException {
		visitRepository.save(visit);
	}

	@Transactional(readOnly = true)
	public Pet findPetById(int id) throws DataAccessException {
		return petRepository.findById(id);
	}

	@Transactional(rollbackFor = DuplicatedPetNameException.class)
	public void savePet(Pet pet) throws DataAccessException, DuplicatedPetNameException {
			Pet otherPet=pet.getOwner().getPetwithIdDifferent(pet.getName(), pet.getId());
            if (StringUtils.hasLength(pet.getName()) &&  (otherPet!= null && otherPet.getId()!=pet.getId())) {            	
            	throw new DuplicatedPetNameException();
            }else
                petRepository.save(pet);                
	}


	public Collection<Visit> findVisitsByPetId(int petId) {
		return visitRepository.findByPetId(petId);
	}
	
	@Transactional
	public void deleteVisit(final Visit visit){
		this.visitRepository.delete(visit);
	}
	
	@Transactional
	public Visit findVisitById(final int visitId) {
		return visitRepository.findVisitById(visitId);

	}
	
    @Transactional
    public void deletePet(final Pet pet) throws DataAccessException{
        this.petRepository.delete(pet);
    }
    
    @Transactional
	public void saveBooking(@Valid Booking booking) throws Exception {
		//TODO refactorizar para hacer más eficiente con Custom query
		
		/*
		 * Esta función comprueba que haya habitaciones disponibles
		 * para esa fecha, en caso de que la haya, asigna una
		 * habitación aleatoria. En caso de que no, no hace el save
		 * y devuelve una excepción.
		 */
		List<Room> rooms = roomRepository.findAll();
		Room room = rooms.stream().filter(x -> isAvailable(x, booking.getCheckIn(), booking.getCheckOut())).findAny().orElse(null);
		if(room != null) {
			booking.setRoom(room);
			bookingRepository.save(booking);
			//NO CE CI ESTA BIEN
		} else {
			throw new Exception("NO HAY HABITACIONES DISPONIBLES PARA ESA FECHA");
		}
		
	}

	private Boolean isAvailable(Room x, LocalDate checkIn, LocalDate checkOut) {
		Boolean available = true;
		Set<Booking> bookings = x.getBookings();
		for (Booking b:bookings) {
			if((checkIn.isAfter(b.getCheckIn()) && checkIn.isBefore(b.getCheckOut())) 
					|| (checkOut.isAfter(b.getCheckIn()) && checkOut.isBefore(b.getCheckOut()))){
				
				available = false;				
			}
		}
		return available;
	}

}
