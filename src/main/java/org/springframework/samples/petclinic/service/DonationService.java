package org.springframework.samples.petclinic.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.repository.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationService {
	
	private DonationRepository donationRepository;
	
	@Autowired
	public DonationService(DonationRepository donationRepository) {
		this.donationRepository = donationRepository;
	}
	
	@Transactional
	public void save(Donation donation) throws DataAccessException{
		donation.setDate(LocalDate.now());
		donationRepository.save(donation);
	}
	
	
}
