package admin_user.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import admin_user.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
	// User findByEmail (String email);
	User findByEmail (String email);
	List<User> findByRole(String role);
	List<User> findByRoleIn(List<String> roles);	
}
