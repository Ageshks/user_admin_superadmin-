package admin_user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import admin_user.model.BusRoute;
import admin_user.service.BookingService;
import admin_user.service.BusRouteService;

@Controller
public class BusRouteController {

    @Autowired
    private BusRouteService busRouteService;

    @Autowired
    private BookingService bookingService;

    // Add Bus Route Page
    @GetMapping("/super-admin/add-route")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String getAddRoutePage(Model model) {
        model.addAttribute("busRoute", new BusRoute());
        return "add-route";
    }

    // Add Bus Route Action
    @PostMapping("/super-admin/add-route")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String addBusRoute(@ModelAttribute("busRoute") BusRoute busRoute) {
        busRouteService.addBusRoute(busRoute);
        return "redirect:/super-admin/view-routes";
    }

    // View All Routes
    @GetMapping("/super-admin/view-routes")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String viewRoutes(Model model) {
        model.addAttribute("routes", busRouteService.getAllRoutes());
        return "view-routes";
    }

    // Edit Bus Route Page
    @GetMapping("/super-admin/edit-route/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String getEditRoutePage(@PathVariable Long id, Model model) {
        BusRoute busRoute = busRouteService.getRouteById(id);
        if (busRoute == null) {
            return "redirect:/super-admin/view-routes";
        }
        model.addAttribute("busRoute", busRoute);
        return "edit-route";
    }

    // Update Bus Route Action
    @PostMapping("/super-admin/edit-route/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String updateRoute(@PathVariable Long id, @ModelAttribute("busRoute") BusRoute busRoute) {
        BusRoute existingRoute = busRouteService.getRouteById(id);
        if (existingRoute != null) {
            existingRoute.setRouteNumber(busRoute.getRouteNumber());
            existingRoute.setStartLocation(busRoute.getStartLocation());
            existingRoute.setEndLocation(busRoute.getEndLocation());
            existingRoute.setDepartureTime(busRoute.getDepartureTime());
            busRouteService.updateBusRoute(existingRoute);
        }
        return "redirect:/super-admin/view-routes";
    }

    // Delete Bus Route
    @GetMapping("/super-admin/delete-route/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String deleteRoute(@PathVariable Long id) {
        busRouteService.deleteRoute(id);
        return "redirect:/super-admin/view-routes";
    }

    // Super Admin Dashboard
    @GetMapping("/super-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String getDashboardPage(Model model) {
        return "redirect:/super-admin.html";
    }

    // User Dashboard
    @GetMapping("/user/dashboard")
    public String userDashboard(Model model) {
        List<BusRoute> routes = busRouteService.getAllRoutes(); // Display all routes for the user
        model.addAttribute("routes", routes);
        return "user-dashboard"; // Thymeleaf template for user dashboard
    }

    // Book Bus Route
    @PostMapping("/user/book/{id}")
    public String bookBusRoute(@PathVariable Long id, @RequestParam int seats) {
        BusRoute route = busRouteService.getRouteById(id);
        if (route != null && route.getAvailableSeats() >= seats) {
            bookingService.bookRoute(route, seats);
            return "redirect:/user/dashboard"; // Redirect back to dashboard after booking
        } else {
            return "error"; // Handle error when not enough seats are available
        }
    }
}
