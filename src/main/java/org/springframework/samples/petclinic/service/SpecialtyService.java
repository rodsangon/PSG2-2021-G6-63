package org.springframework.samples.petclinic.service;


import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpecialtyService {
	
	private SpecialtyRepository specialtyRepository;


	@Autowired
	public SpecialtyService(SpecialtyRepository specialtyRepository) {
		this.specialtyRepository = specialtyRepository;
	}
	
	@Transactional
	public  List<Specialty> findAll(){
		return specialtyRepository.findAll();
	}

	
	

	
}