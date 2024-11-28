package admin_user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import admin_user.model.BusRoute;
import admin_user.repositories.BusRouteRepository;

@Service
public class BusRouteServiceImpl implements BusRouteService {

    @Autowired
    private BusRouteRepository busRouteRepository;

    @Override
    public List<BusRoute> getAllRoutes() {
        return busRouteRepository.findAll();
    }

    @Override
    public void addBusRoute(BusRoute busRoute) {
        busRouteRepository.save(busRoute);
    }

    @Override
    public BusRoute getRouteById(Long id) {
        return busRouteRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteRoute(Long id) {
        busRouteRepository.deleteById(id);
    }

    @Override
    public void updateBusRoute(BusRoute busRoute) {
        busRouteRepository.save(busRoute);
    }
    
    @Override
    public List<BusRoute> getAvailableRoutes() {
        return busRouteRepository.findByAvailableSeatsGreaterThan(0);
    }
}
