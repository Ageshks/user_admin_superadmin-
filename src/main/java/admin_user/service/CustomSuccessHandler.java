package admin_user.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		var authourities = authentication.getAuthorities();
		var roles = authourities.stream().map(r -> r.getAuthority()).findFirst();
		
		switch (roles.orElse("")) {
			case "SUPER_ADMIN" -> response.sendRedirect("/super-admin-page");
			case "ADMIN" -> response.sendRedirect("/admin-page");
			case "USER" -> response.sendRedirect("/user-page");
			default -> response.sendRedirect("/error");
		}
		
		
		
		
	}

}
