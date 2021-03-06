package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.VetService;

public class SpecialtyFormatter implements Formatter<Specialty>{
	
	private final VetService vetService;
	
	@Autowired
	public SpecialtyFormatter(VetService vetService) {
		this.vetService = vetService;
	}

	@Override
	public String print(Specialty specialty, Locale locale) {
		return specialty.getName();
	}

	@Override
	public Specialty parse(String text, Locale locale) throws ParseException {
		Collection<Specialty> findSpecialties = this.vetService.findSpecialties();
		for(Specialty spec : findSpecialties) {
			if(spec.getName().equals(text)) {
				return spec;
			}
		}
		throw new ParseException("specialty not found: " + text, 0);
	}
	

}
