package admin_user.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import admin_user.dto.UserDto;
import admin_user.model.BusRoute;
import admin_user.model.User;
import admin_user.repositories.UserRepository;
import admin_user.service.BusRouteService;
import admin_user.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;


	@GetMapping("/index")
	public String index(){
		return "index";
	}
	
	@GetMapping("/registration")
	public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
		return "register";
	}
	
	@PostMapping("/registration")
	public String saveUser(@ModelAttribute("user") UserDto userDto, Model model) {
		userService.save(userDto);
		model.addAttribute("message", "Registered Successfuly!");
		return "register";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@Autowired
private BusRouteService busRouteService; // Inject the BusRouteService

@GetMapping("/super-admin-page")
@PreAuthorize("hasrole('ROLE_SUPER-ADMIN')")
public String superAdminPage(Model model, Principal principal) {
    // Get the logged-in super admin details
    UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    model.addAttribute("adminUser", userDetails);

    // Fetch the super admin's details from the repository
    User superAdmin = userRepository.findByEmail(userDetails.getUsername());
    model.addAttribute("user", superAdmin); // Add super admin details to the model

    // Fetch all users, excluding the current logged-in super admin
    List<User> users = userRepository.findAll()
        .stream()
        .filter(user -> !user.getEmail().equals(superAdmin.getEmail())) // Exclude the super admin
        .toList();

    model.addAttribute("users", users);

	model.addAttribute("user", superAdmin);
    // Fetch all bus routes and add to the model
    List<BusRoute> routes = busRouteService.getAllRoutes(); // Adjusted to use the injected service
    model.addAttribute("routes", routes);

    return "super-admin"; // Ensure "super-admin.html" exists in the templates folder
}




//its an another method -- not to view the super admin in the superadmin dashboard//
// 	@GetMapping("/super-admin-page")
// public String superAdminPage(Model model, Principal principal) {
//     // Get the logged-in super admin details
//     UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
//     model.addAttribute("user", userDetails);

//     // Fetch the super admin's details from the repository
//     User superAdmin = userRepository.findByEmail(userDetails.getUsername());
    
//     model.addAttribute("adminUser", userDetails);
//     model.addAttribute("user", superAdmin); // Add super admin details to the model

//     // Fetch all users, excluding the current logged-in super admin
//     List<User> users = userRepository.findAll()
//         .stream() // Convert the list to a stream
//         .filter(user -> !user.getEmail().equals(superAdmin.getEmail())) // Exclude the super admin
//         .collect(Collectors.toList()); // Collect the filtered users into a list

//     model.addAttribute("users", users);

//     return "super-admin"; // Ensure "super-admin.html" exists in the templates folder
// }



	@GetMapping("user-page")
	public String userPage (Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		return "user";
	}

	@GetMapping("/admin-page")
	public String adminPage(Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		
    // Fetch user info from repository
    User user = userRepository.findByEmail(userDetails.getUsername());
    

	model.addAttribute("adminUser", userDetails);
    // Add user details to the model
    model.addAttribute("user", user);

	List<User> users = userRepository.findByRole("USER");
	model.addAttribute("users",users);

	List<BusRoute> routes = busRouteService.getAllRoutes(); // Adjusted to use the injected service
    model.addAttribute("routes", routes);

    return "admin"; 
	}

	@PostMapping("/admin/delete-user/{id}")
		public String deleteUser(@PathVariable("id") Long id){
			userService.deleteUserById(id);
			return "redirect:/admin-page";
		}
	
	@PostMapping("/super-admin/delete-user/{id}")
		public String deleteUserAsSuperAdmin(@PathVariable("id") Long id){
			userService.deleteUserById(id);
			return "redirect:/super-admin-page";
		}

	@PostMapping("/super-admin/change-role/{id}")
		public String changeUserRol(@PathVariable("id") Long id, @ModelAttribute("role") String role){
			User user = userRepository.findById(id).orElse(null);	
			if(user !=null){
				user.setRole(role);
				userRepository.save(user);
			}
			return "redirect:/super-admin-page";
		}
	





}
