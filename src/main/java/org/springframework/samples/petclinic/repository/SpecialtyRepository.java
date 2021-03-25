package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Specialty;

public interface SpecialtyRepository extends CrudRepository<Specialty, Integer>{
	
	@Query("SELECT specialties FROM Specialty specialties")
	public List<Specialty> findSpecialtyTypes();
	
	public List<Specialty> findAll();

}
