package hr.fer.zari.rasip.tiger.controller;

import hr.fer.zari.rasip.tiger.domain.UserRole;
import hr.fer.zari.rasip.tiger.rest.model.UserRoleRestModel;
import hr.fer.zari.rasip.tiger.service.UserRoleService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-role")
public class UserRoleController {

	private UserRoleService userRoleService;

	@Autowired
	public UserRoleController(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public Collection<UserRoleRestModel> list() {
		Collection<UserRole> allRoles = userRoleService.getAll();
		return userRoleService.convert(allRoles);
	}
}
