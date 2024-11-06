package admin_user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import admin_user.model.BusRoute;
import admin_user.service.BusRouteService;
@Controller
public class BusRouteController {
    @Autowired
    private BusRouteService busRouteService;

    @GetMapping("/super-admin/add-route")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String getAddRoutePage(Model model) {
        model.addAttribute("busRoute", new BusRoute());
        return "add-route";
    }

    @PostMapping("/super-admin/add-route")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String addBusRoute(@ModelAttribute("busRoute") BusRoute busRoute) {
        busRouteService.addBusRoute(busRoute);
        return "redirect:/super-admin/view-routes";
    }

    @GetMapping("/super-admin/view-routes")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String viewRoutes(Model model) {
        model.addAttribute("routes", busRouteService.getAllRoutes());
        return "view-routes";
    }

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

    @GetMapping("/super-admin/delete-route/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String deleteRoute(@PathVariable Long id) {
        busRouteService.deleteRoute(id);
        return "redirect:/super-admin/view-routes";
    }

    @GetMapping({"/super-admin"})
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String getDashboardPage(Model model) {
        System.out.println("Accessing dashboard page"); // Add this debug line
        return "redirect:/super-admin.html";  // Try with explicit .html extension
    }
}