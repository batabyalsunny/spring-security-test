package ml.bootcode.springsecuritytest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

	@GetMapping("/admin")
	public String showUsers() {
		return "users";
	}

	@GetMapping("/user")
	public String showOrders() {
		return "orders";
	}

	@GetMapping("/public")
	public String showWelcome() {
		return "welcome";
	}

	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}

}
