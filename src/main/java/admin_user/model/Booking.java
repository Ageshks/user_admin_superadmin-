package admin_user.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;

    @ManyToOne
    private BusRoute route;  

    private String userEmail;
    private int seats;
    private LocalDateTime bookingTime;
    private String paymentId;

    public Booking() {}

    public Booking(BusRoute route, String userEmail, int seats, LocalDateTime bookingTime, String paymentId) {
        this.route = route;
        this.userEmail = userEmail;
        this.seats = seats;
        this.bookingTime = bookingTime;
        this.paymentId = paymentId;
    }

    public Booking(BusRoute route, String userEmail, int seats, LocalDateTime bookingTime) {
        this.route = route;
        this.userEmail = userEmail;
        this.seats = seats;
        this.bookingTime = bookingTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BusRoute getRoute() {
        return route;
    }

    public void setRoute(BusRoute route) {
        this.route = route;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
