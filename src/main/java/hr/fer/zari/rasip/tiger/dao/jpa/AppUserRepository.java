package hr.fer.zari.rasip.tiger.dao.jpa;

import hr.fer.zari.rasip.tiger.domain.AppUser;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long>{

	AppUser findByEmail(String email);
}
