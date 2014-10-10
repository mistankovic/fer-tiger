package hr.fer.zari.rasip.tiger.service.impl;

import hr.fer.zari.rasip.tiger.dao.jpa.UserRoleRepository;
import hr.fer.zari.rasip.tiger.domain.AppUser;
import hr.fer.zari.rasip.tiger.domain.UserRole;
import hr.fer.zari.rasip.tiger.rest.model.UserRoleRestModel;
import hr.fer.zari.rasip.tiger.service.UserRoleService;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

	private UserRoleRepository userRoleRepository;

	@Autowired
	public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
		this.userRoleRepository = userRoleRepository;
	}

	@Override
	public UserRole getByName(String name) {
		return userRoleRepository.findByName(name);
	}

	@Override
	public Collection<UserRole> getAll() {
		Iterable<UserRole> all = userRoleRepository.findAll();
		if (all instanceof Collection) {
			return (Collection<UserRole>) all;
		}

		Collection<UserRole> roles = new HashSet<>();
		for (UserRole userRole : all) {
			roles.add(userRole);
		}
		return roles;
	}

	@Override
	public UserRole save(UserRole userRole) {
		return userRoleRepository.save(userRole);
	}

	@Override
	public Collection<UserRoleRestModel> convert(Collection<UserRole> roles) {
		Set<UserRoleRestModel> models = new LinkedHashSet<>(roles.size());
		for (UserRole userRole : roles) {
			models.add(convert(userRole));
		}
		return models;
	}

	private UserRoleRestModel convert(UserRole role) {
		return new UserRoleRestModel(role.getName(), role.getDescription());
	}

	@Override
	public boolean hasAppUserRoleWithName(AppUser appUser, String userRoleName) {
		boolean hasRoleWithName = false;
		for (UserRole role : appUser.getRoles()) {
			if(role.getName().equals(userRoleName)){
				hasRoleWithName = true;
				break;
			}
		}
		
		return hasRoleWithName;
	}

}
