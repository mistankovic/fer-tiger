package hr.fer.zari.rasip.tiger.security;

import hr.fer.zari.rasip.tiger.domain.AppUser;
import hr.fer.zari.rasip.tiger.service.AppUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	protected AppUserService appUserService;

	@Autowired
	public UserDetailsServiceImpl(AppUserService appUserService) {
		this.appUserService = appUserService;
	}
	
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = appUserService.findByEmail(username);
		if(user == null){
			throw new UsernameNotFoundException(username + " not found!");
		}
		return new AppUserDetails(user);
	}


}
