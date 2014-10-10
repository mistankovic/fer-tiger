package hr.fer.zari.rasip.tiger.service;

import hr.fer.zari.rasip.tiger.domain.AppUser;
import hr.fer.zari.rasip.tiger.domain.UserRole;
import hr.fer.zari.rasip.tiger.rest.model.UserRoleRestModel;

import java.util.Collection;

public interface UserRoleService {

	UserRole getByName(String name);
	
	Collection<UserRole> getAll();
	
	Collection<UserRoleRestModel> convert(Collection<UserRole> roles);
	
	UserRole save(UserRole userRole);
	
	boolean hasAppUserRoleWithName(AppUser appUser, String userRoleName);
}
