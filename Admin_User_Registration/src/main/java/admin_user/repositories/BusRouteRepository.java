package admin_user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import admin_user.model.BusRoute;

public interface BusRouteRepository extends JpaRepository<BusRoute, Long> {
    
}
