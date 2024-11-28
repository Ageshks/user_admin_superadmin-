package admin_user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import admin_user.model.BusRoute;
import admin_user.service.BusRouteService;

@Controller
public class BusRouteController {

    @Autowired
    private BusRouteService busRouteService;

    // Display the Add Route page
    @GetMapping("/super-admin/add-route")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String getAddRoutePage(Model model) {
        model.addAttribute("busRoute", new BusRoute());
        return "add-route";
    }

    // Handle adding a new bus route
    @PostMapping("/super-admin/add-route")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String addBusRoute(@ModelAttribute("busRoute") BusRoute busRoute) {
        busRoute.setAvailableSeats(busRoute.getTotalSeats()); // Initially all seats are available
        busRouteService.addBusRoute(busRoute);
        return "redirect:/super-admin/view-routes";
    }

    // Display all bus routes
    @GetMapping("/super-admin/view-routes")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String viewRoutes(Model model) {
        List<BusRoute> routes = busRouteService.getAllRoutes();
        model.addAttribute("routes", routes);
        return "view-routes";
    }

    // Display the Edit Route page
    @GetMapping("/super-admin/edit-route/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String getEditRoutePage(@PathVariable Long id, Model model) {
        BusRoute busRoute = busRouteService.getRouteById(id);
        if (busRoute == null) {
            model.addAttribute("error", "Bus route not found");
            return "redirect:/super-admin/view-routes";
        }
        model.addAttribute("busRoute", busRoute);
        return "edit-route";
    }

    // Handle updating a bus route
    @PostMapping("/super-admin/edit-route/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String updateRoute(@PathVariable Long id, @ModelAttribute("busRoute") BusRoute updatedBusRoute) {
        BusRoute existingRoute = busRouteService.getRouteById(id);

        if (existingRoute != null) {
            // Update the bus route details
            existingRoute.setRouteNumber(updatedBusRoute.getRouteNumber());
            existingRoute.setStartLocation(updatedBusRoute.getStartLocation());
            existingRoute.setEndLocation(updatedBusRoute.getEndLocation());
            existingRoute.setDepartureTime(updatedBusRoute.getDepartureTime());
            existingRoute.setPricePerSeat(updatedBusRoute.getPricePerSeat());

            // Update total and available seats
            int seatDifference = updatedBusRoute.getTotalSeats() - existingRoute.getTotalSeats();
            existingRoute.setTotalSeats(updatedBusRoute.getTotalSeats());
            existingRoute.setAvailableSeats(existingRoute.getAvailableSeats() + seatDifference);

            busRouteService.updateBusRoute(existingRoute);
        }

        return "redirect:/super-admin/view-routes";
    }

    // Handle deleting a bus route
    @GetMapping("/super-admin/delete-route/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String deleteRoute(@PathVariable Long id) {
        busRouteService.deleteRoute(id);
        return "redirect:/super-admin/view-routes";
    }


    
    // Display user dashboard with available routes
    @GetMapping("/user/dashboard")
    public String userDashboard(Model model) {
        List<BusRoute> availableRoutes = busRouteService.getAvailableRoutes();
        model.addAttribute("routes", availableRoutes);
        return "user-dashboard";
    }

    // Initiate booking by verifying seat availability
    @PostMapping("/user/initiate-booking/{id}")
    public String initiateBooking(
            @PathVariable Long id,
            @RequestParam int seats,
            Model model) {
        BusRoute route = busRouteService.getRouteById(id);

        if (route != null && route.getAvailableSeats() >= seats) {
            model.addAttribute("route", route);
            model.addAttribute("seats", seats);
            model.addAttribute("totalAmount", calculateTotalAmount(route, seats));
            return "payment-page";
        } else {
            model.addAttribute("error", "Not enough seats available.");
            return "user-dashboard";
        }
    }

    // Fetch price per seat for a specific route
    @GetMapping("/user/get-price/{id}")
    public ResponseEntity<Double> getPricePerSeat(@PathVariable Long id) {
    BusRoute route = busRouteService.getRouteById(id);
    if (route != null) {
        return ResponseEntity.ok(route.getPricePerSeat());
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}


    // Calculate total booking amount based on price per seat
    private double calculateTotalAmount(BusRoute route, int seats) {
        return route.getPricePerSeat() * seats;
    }
}
