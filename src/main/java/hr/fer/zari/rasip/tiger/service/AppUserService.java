package hr.fer.zari.rasip.tiger.service;

import hr.fer.zari.rasip.tiger.domain.AppUser;
import hr.fer.zari.rasip.tiger.rest.model.AppUserRegistrationRestModel;
import hr.fer.zari.rasip.tiger.rest.model.AppUserRestModel;

import java.util.Collection;

public interface AppUserService {

	AppUser findById(Long id);

	AppUser save(AppUser appUser);

	AppUser findByEmail(String email);

	Collection<AppUser> findAll();

	Collection<AppUserRestModel> convert(Collection<AppUser> users);

	AppUserRestModel convert(AppUser appUser);

	AppUser convert(AppUserRegistrationRestModel appUserModel);

	boolean delete(Long id);

	AppUserRegistrationRestModel convertToUpdateableRestModel(AppUser appUser);

	AppUser updateUserWithId(Long id, AppUserRegistrationRestModel restModel);


}
