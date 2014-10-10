package hr.fer.zari.rasip.tiger.service.impl;

import hr.fer.zari.rasip.tiger.dao.jpa.AppUserRepository;
import hr.fer.zari.rasip.tiger.domain.AppUser;
import hr.fer.zari.rasip.tiger.domain.UserRole;
import hr.fer.zari.rasip.tiger.rest.model.AppUserRegistrationRestModel;
import hr.fer.zari.rasip.tiger.rest.model.AppUserRestModel;
import hr.fer.zari.rasip.tiger.rest.model.UserRoleRestModel;
import hr.fer.zari.rasip.tiger.service.AppUserService;
import hr.fer.zari.rasip.tiger.service.UserRoleService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {

	private AppUserRepository appUserRepository;
	private UserRoleService userRoleService;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public AppUserServiceImpl(AppUserRepository appUserRepository,
			UserRoleService userRoleService, PasswordEncoder passwordEncoder) {
		this.appUserRepository = appUserRepository;
		this.userRoleService = userRoleService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public AppUser save(AppUser appUser) {
		return appUserRepository.save(appUser);
	}

	@Override
	public AppUser findByEmail(String email) {
		if (email != null) {
			return appUserRepository.findByEmail(email);
		}
		return null;
	}

	@Override
	public Collection<AppUser> findAll() {
		Iterable<AppUser> all = appUserRepository.findAll();
		if (all instanceof Collection) {
			return (Collection<AppUser>) all;
		}
		List<AppUser> list = new ArrayList<>();
		for (AppUser appUser : all) {
			list.add(appUser);
		}
		return list;

	}

	@Override
	public Collection<AppUserRestModel> convert(Collection<AppUser> users) {
		if (users != null) {
			Set<AppUserRestModel> set = new LinkedHashSet<>();
			for (AppUser appUser : users) {
				if (appUser != null) {
					set.add(convertInternal(appUser));
				}
			}

			return set;
		}
		return null;
	}

	@Override
	public AppUserRestModel convert(AppUser appUser) {
		if (appUser != null) {
			return convertInternal(appUser);
		}
		return null;
	}

	private AppUserRestModel convertInternal(AppUser appUser) {
		AppUserRestModel restModel = new AppUserRestModel(appUser.getId(),
				appUser.getEmail(), appUser.getFirstName(),
				appUser.getLastName(), appUser.getDateCreated());

		return restModel;
	}

	@Override
	public AppUser convert(AppUserRegistrationRestModel appUserModel) {
		AppUser user = new AppUser();

		updateUserFromModel(user, appUserModel);

		return user;
	}

	@Override
	public AppUser updateUserWithId(Long id, AppUserRegistrationRestModel restModel) {
		AppUser user = appUserRepository.findOne(id);
		if (user == null) {
			throw new RuntimeException(String.format(
					"User with given id %s not found", id));
		}
		
		updateUserFromModel(user, restModel);
		
		return user;
	}

	private void updateUserFromModel(AppUser user, AppUserRegistrationRestModel model) {
		user.setEmail(model.getEmail());
		user.setFirstName(model.getFirstName());
		user.setLastName(model.getLastName());
		if (model.getPassword() != null) {
			String password = passwordEncoder.encode(model.getPassword());
			user.setPassword(password);
		}

		Collection<UserRoleRestModel> rolesModels = model.getRoles();
		Set<UserRole> roles = user.getRoles();
		roles.clear();
		roles.addAll(getRoles(rolesModels));
	}

	private Set<UserRole> getRoles(Collection<UserRoleRestModel> roleModels) {
		Set<UserRole> roles = new HashSet<>(roleModels.size());
		for (UserRoleRestModel model : roleModels) {
			UserRole role = userRoleService.getByName(model.getName());
			if (role != null) {
				roles.add(role);
			}
		}
		return roles;
	}

	@Override
	public boolean delete(Long id) {
		appUserRepository.delete(id);
		if (appUserRepository.findOne(id) != null) {
			return false;
		}
		return true;
	}

	@Override
	public AppUser findById(Long id) {
		return appUserRepository.findOne(id);
	}

	@Override
	public AppUserRegistrationRestModel convertToUpdateableRestModel(
			AppUser appUser) {
		AppUserRegistrationRestModel restModel = new AppUserRegistrationRestModel(
				appUser.getEmail(),
				appUser.getFirstName(),
				appUser.getLastName() 
			);
		Set<UserRole> userRoles = appUser.getRoles();
		restModel.setRoles(userRoleService.convert(userRoles));

		return restModel;
	}

}
