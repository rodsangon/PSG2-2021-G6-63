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

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
	private static final String ADOPTION_LIST = "pets/adoptionList";
	private static final String PET_DETAILS = "pets/petDetails";
	private static final String ADOPT_FORM = "pets/adoptForm";

	private final OwnerService ownerService;
	private final PetService petService;
	private final UserService userService;

	@Autowired
	public OwnerController(OwnerService ownerService, PetService petService, UserService userService,
			AuthoritiesService authoritiesService) {
		this.ownerService = ownerService;
		this.petService = petService;
		this.userService = userService;
	}

	public Owner ownerLogeado(Principal principal) {
		String username = principal.getName();
		Owner owner = ownerService.findOwnerByUsername(username);
		return owner;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/owners/new")
	public String initCreationForm(Map<String, Object> model) {
		Owner owner = new Owner();
		model.put("owner", owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/owners/new")
	public String processCreationForm(@Valid Owner owner, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		} else {
			// creating owner, user and authorities
			this.ownerService.saveOwner(owner);

			return "redirect:/owners/" + owner.getId();
		}
	}

	@GetMapping(value = "/owners/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("owner", new Owner());
		return "owners/findOwners";
	}

	@GetMapping(value = "/owners")
	public String processFindForm(Owner owner, BindingResult result, Map<String, Object> model) {

		// allow parameterless GET request for /owners to return all records
		if (owner.getLastName() == null) {
			owner.setLastName(""); // empty string signifies broadest possible search
		}

		// find owners by last name
		Collection<Owner> results = this.ownerService.findOwnerByLastName(owner.getLastName());
		if (results.isEmpty()) {
			// no owners found
			result.rejectValue("lastName", "notFound", "not found");
			return "owners/findOwners";
		} else if (results.size() == 1) {
			// 1 owner found
			owner = results.iterator().next();
			return "redirect:/owners/" + owner.getId();
		} else {
			// multiple owners found
			model.put("selections", results);
			return "owners/ownersList";
		}
	}

	@GetMapping(value = "/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
		Owner owner = this.ownerService.findOwnerById(ownerId);
		model.addAttribute(owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result,
			@PathVariable("ownerId") int ownerId) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		} else {
			owner.setId(ownerId);
			this.ownerService.saveOwner(owner);
			return "redirect:/owners/{ownerId}";
		}
	}

	/**
	 * Custom handler for displaying an owner.
	 * 
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		mav.addObject(this.ownerService.findOwnerById(ownerId));
		return mav;
	}

	@GetMapping("/owners/{ownerId}/delete")
	public String deleteOwner(@PathVariable("ownerId") final int ownerId, final ModelMap model) {
		Owner owner = this.ownerService.findOwnerById(ownerId);
		this.ownerService.deleteOwner(owner);
		return "redirect:/owners";
	}

	@GetMapping("/pets/adoptionList")
	public String adoptionList(final ModelMap model) {
		model.put("pets", petService.findPetsInAdoption());
		return ADOPTION_LIST;
	}

	@GetMapping("/pets/{petId}")
	public String petDetails(@PathVariable("petId") final int petId, final ModelMap model) {
		model.put("pet", petService.findPetById(petId));
		model.put("owner", petService.findPetById(petId).getOwner());
		return PET_DETAILS;
	}

	@GetMapping("pets/{petId}/adopt")
	public String initAdoptForm(@PathVariable("petId") final int petId, final ModelMap model, Principal principal) {
		Owner adoptant = ownerService.findOwnerByUsername(principal.getName());
		Adoption adoption = new Adoption();
		Pet pet = petService.findPetById(petId);
		Owner originalOwner = petService.findPetById(petId).getOwner();
		model.put("pet", pet);
		model.put("originalOwner", originalOwner);
		model.put("adoptant", adoptant);
		model.put("adoption", adoption);
		return ADOPT_FORM;
	}

	@PostMapping("pets/{petId}/adopt")
	public String processAdoptForm(@Valid Adoption adoption, @PathVariable("petId") final int petId,
			@RequestParam(name = "originalOwnerId") final int originalOwnerId, final ModelMap model, Principal principal) {
		
		Owner adoptant = ownerService.findOwnerByUsername(principal.getName());
		Owner originalOwner = ownerService.findOwnerById(originalOwnerId);
		if(adoptant.equals(originalOwner)) {
			model.addAttribute("message", "El adoptante no puede ser el owner original!");
			return ADOPTION_LIST;
		}
		
		
		adoption.setOriginalOwner(originalOwner);
		adoption.setAdoptant(adoptant);
		
		petService.saveAdoption(adoption);
		return ADOPT_FORM;
	}

}
