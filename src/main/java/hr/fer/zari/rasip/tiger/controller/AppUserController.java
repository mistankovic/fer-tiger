package hr.fer.zari.rasip.tiger.controller;

import hr.fer.zari.rasip.tiger.domain.AppUser;
import hr.fer.zari.rasip.tiger.rest.model.AppUserRegistrationRestModel;
import hr.fer.zari.rasip.tiger.rest.model.AppUserRestModel;
import hr.fer.zari.rasip.tiger.service.AppUserService;
import hr.fer.zari.rasip.tiger.service.UserRoleService;

import java.security.Principal;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class AppUserController {

	private AppUserService appUserService;
	private UserRoleService userRoleService;

	@Autowired
	public AppUserController(AppUserService appUserService, UserRoleService userRoleService) {
		this.appUserService = appUserService;
		this.userRoleService = userRoleService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public Collection<AppUserRestModel> list() {
		Collection<AppUser> allUsers = appUserService.findAll();
		return appUserService.convert(allUsers);
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public AppUserRegistrationRestModel show(@PathVariable Long id) {
		AppUser appUser = appUserService.findById(id);
		return appUserService.convertToUpdateableRestModel(appUser);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public String save(@RequestBody AppUserRegistrationRestModel appUserModel) {
		AppUser appUser = appUserService.convert(appUserModel);
		AppUser saved = appUserService.save(appUser);
		if (saved == null) {
			throw new RuntimeException("User not saved, there are validation errors.");
		}
		return "User successfully created!";

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@Transactional
	public AppUserRestModel update(@PathVariable Long id, @RequestBody AppUserRegistrationRestModel model, Principal principal) {
		boolean isUpdateMe = isUpdateMe(id, principal);
		boolean isAdmin = isAdmin(principal);
		if(!isAdmin && !isUpdateMe){
			throw new AccessDeniedException(
					String.format(
							"User with username %s has no authorities for this action.",
							principal.getName()));
		}
		AppUser appUser = appUserService.updateUserWithId(id, model);
		if(!isAdmin && isAdmin(appUser)){
			throw new AccessDeniedException("User can't give himself ADMIN authority!");
			//user can update himself but can't give himself admin role
		}
		AppUser updated = appUserService.save(appUser);
		return appUserService.convert(updated);
	}

	private boolean isUpdateMe(Long id, Principal principal) {
		String loggedInUsername = principal.getName();
		AppUser updatingUser = appUserService.findByEmail(loggedInUsername);
		if(id == updatingUser.getId()){
			return true;
		}
		return false;
	}
	
	private boolean isAdmin(Principal principal){
		String loggedInUsername = principal.getName();
		AppUser updatingUser = appUserService.findByEmail(loggedInUsername);
		return isAdmin(updatingUser);
	}
	
	private boolean isAdmin(AppUser appUser){
		if(userRoleService.hasAppUserRoleWithName(appUser, "ADMIN")){
			return true;
		}
		return false;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable Long id) {
		boolean deleted = appUserService.delete(id);
		if (deleted) {
			return "User successfully deleted";
		}
		return "Could not delete user with given id: " + id;
	}
}
