package admin_user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import admin_user.dto.UserDto;
import admin_user.model.User;
import admin_user.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User save(UserDto userDto) {
		User user = new User(userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()) , userDto.getRole(), userDto.getFullname());
		user.setRole("USER");
		return userRepository.save(user);
	}
	@Override
		public List<User> getUsersByRole(String role){
			return userRepository.findByRole (role);
		}

	@Override
		public List<User> getallUsers(){
			return userRepository.findAll();
		}	
	@Override
	public void deleteUserById(Long id){
		userRepository.deleteById(id);
	}
}	
