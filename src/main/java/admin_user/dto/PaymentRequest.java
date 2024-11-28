package admin_user.dto;

public class PaymentRequest {
    private Long routeId;
    private int amount;

    // Getters and Setters
    
    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
