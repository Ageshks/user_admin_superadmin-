package admin_user.service;

import java.util.List;

import admin_user.model.BusRoute;

public interface BusRouteService {

    List<BusRoute> getAllRoutes();
    
    void addBusRoute(BusRoute busRoute);
    
    BusRoute getRouteById(Long id);
    
    void deleteRoute(Long id);
    
    void updateBusRoute(BusRoute busRoute);
    
    List<BusRoute> getAvailableRoutes();
}
