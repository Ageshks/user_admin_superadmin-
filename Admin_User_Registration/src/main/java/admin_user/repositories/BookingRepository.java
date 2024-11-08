package admin_user.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import admin_user.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserEmail(String userEmail);
}
