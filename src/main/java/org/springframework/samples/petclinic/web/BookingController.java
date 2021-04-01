package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BookingController {
	
	Logger logger = LoggerFactory.getLogger(BookingController.class);
	
	private final PetService petService;
	
	@Autowired
	public BookingController(PetService petService) {
		this.petService = petService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	/*@ModelAttribute("booking")
	public Booking loadPetWithBooking(@PathVariable("petId") int petId) {
		Pet pet = this.petService.findPetById(petId);
		Booking booking = new Booking();
		booking.setPet(pet);
		return booking;
	}*/
	
	@ModelAttribute("booking")
	public Booking loadPetWithBooking(@PathVariable("petId") int petId) {
		Pet pet = this.petService.findPetById(petId);
		Booking booking = new Booking();
		pet.addBooking(booking);
		logger.info("Se ha creado el Booking" + booking.toString());
		return booking;
	}
	@GetMapping(value = "/owners/*/pets/{petId}/booking/new")
	public String initNewBookingForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		logger.info("Inicia el formulario de Booking");
		return "booking/formNewBooking";
	}
	
    @PostMapping("/owners/{ownerId}/pets/{petId}/booking/new")
    public String processNewBookingForm(@Valid Booking booking, BindingResult result) throws Exception {
    	//model.put("pet", this.petService.findPetById(petId));
    	if(result.hasErrors()) {
    		logger.error("El formulario de Booking contiene errores");
    		return "booking/formNewBooking";
    	} else {
    		logger.info("Se ha procesado el formulario de Booking correctamente");
    		this.petService.saveBooking(booking);
    		return "redirect:/owners/{ownerId}";
    	}
    }
    
	@GetMapping(value = "/owners/*/pets/{petId}/booking")
	public String showBookings(@PathVariable("petId") int petId, Map<String, Object> model) {
		List<Booking> bookings = new ArrayList<>(this.petService.findPetById(petId).getBookings());
		model.put("bookings", bookings);
		return "booking/formNewBooking";
	}
    
    /*@RequestMapping(value = "/addBooking", method = RequestMethod.POST)
    public String submit(@Valid @ModelAttribute("booking") Booking booking, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "exception";
        }
        model.addAttribute("checkIn", model);
        model.addAttribute("checkOut", model);
        model.addAttribute("details", model);
    	return "booking/formNewBooking";
    	
    }*/


}
