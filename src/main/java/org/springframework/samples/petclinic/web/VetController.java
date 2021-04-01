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
package org.springframework.samples.petclinic.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VetController {
	
	private static final String VIEWS_VETS_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetsForm";

	private final VetService vetService;
	
	Logger logger = LoggerFactory.getLogger(VetController.class);


	@Autowired
	public VetController(VetService clinicService) {
		this.vetService = clinicService;
	}
	
	
	@ModelAttribute("specialties")
	public Collection<Specialty> populateSpecialties() {
		logger.info("Entra en ModelAttribute");
		return this.vetService.findSpecialties();
	}

	@GetMapping(value = { "/vets" })
	public String showVetList(Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping
		logger.info("Entra en showVetList. MODEL: " + model.toString());
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "vets/vetList";
	}

	@GetMapping(value = { "/vets/{vetId}/delete" })
	public String deleteVet(@PathVariable("vetId") final int vetId, final ModelMap model) {
		Vet vet = this.vetService.findVetById(vetId).get();
		this.vetService.deleteVet(vet);
		return this.showVetList(model);
	}
	
	@GetMapping(value = "/vets/new")
	public String initCreationForm(Map<String, Object> model) {
		logger.info("Entra en initCreationForm. MODEL: " + model.toString());
		Vet vet = new Vet();
		model.put("vet", vet);
		return VIEWS_VETS_CREATE_OR_UPDATE_FORM;		
	}
	
	@PostMapping(value = "/vets/new")
	public String processCreationForm(@Valid Vet vet, BindingResult result) {
		logger.info("Entra en processCreationForm");
		if (result.hasErrors()) {
			logger.error("Entra en processCreationForm. Tiene errores.VET: " + vet.toString());
			return VIEWS_VETS_CREATE_OR_UPDATE_FORM;
		}
		else {
			logger.info("Entra en processCreationForm. Sin errores. VET: " + vet.toString());
			this.vetService.saveVet(vet);
			return "redirect:/vets";
		}
	}
	
	@GetMapping(value = "/vets/save/{vetID}")
	public String initUpdateForm(@PathVariable("vetID") int vetID, Model model) {
		logger.info("Entra en initUpdateForm. MODEL: " + model.toString());
		Optional<Vet> vet = this.vetService.findVetById(vetID);
		if(vet.isPresent()) {
            model.addAttribute("vet", vet.get());
            return VIEWS_VETS_CREATE_OR_UPDATE_FORM;
        } else {
            model.addAttribute("message", "Veterinario no encontrado");
            return "redirect:/vets";
        }
	}
	
	@PostMapping(value = "/vets/save/{vetID}")
	public String processUpdateForm(@Valid Vet vet, BindingResult result,
			@PathVariable("vetID") int vetID) {
		logger.info("Entra en initUpdateForm. VET: " + vet.toString() + "RESULT: " + result.toString());
		if (result.hasErrors()) {
			return VIEWS_VETS_CREATE_OR_UPDATE_FORM;
		}
		else {
			vet.setId(vetID);
			this.vetService.saveVet(vet);
			return "redirect:/vets";
		}
	}

}
