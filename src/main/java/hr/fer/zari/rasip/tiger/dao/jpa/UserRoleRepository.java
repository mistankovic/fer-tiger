package hr.fer.zari.rasip.tiger.dao.jpa;

import hr.fer.zari.rasip.tiger.domain.UserRole;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

	public UserRole findByName(String name);
}
