package admin_user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.publishable.key}")
    private String stripePublishableKey;

    public String createStripeSession(double amount, Long routeId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;  // Use the injected secret key

        // Create session parameters
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/bookings/success")
                .setCancelUrl("http://localhost:8080/bookings/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long) (amount * 100))
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Bus Ticket for Route ID: " + routeId)
                                        .build())
                                .build())
                        .setQuantity(1L)
                        .build())
                .build();

        // Create the session
        Session session = Session.create(params);
        String sessionUrl = session.getUrl();

        // You can now send the `stripePublishableKey` to your frontend if needed
        // Return session URL along with the publishable key (if needed on the client side)
        return sessionUrl;
    }
}
