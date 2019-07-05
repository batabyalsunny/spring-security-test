package ml.bootcode.springsecuritytest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ml.bootcode.springsecuritytest.services.DashboardService;

@RestController
public class DashboardController {

	private DashboardService dashboardService;

	/**
	 * @param dashboardService
	 */
	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

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

	@PostMapping("/signin")
	public String signIn(@RequestParam String username, @RequestParam String password) {
		return dashboardService.signIn(username, password);
	}
}
