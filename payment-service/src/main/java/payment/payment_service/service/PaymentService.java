package payment.payment_service.service;

import com.razorpay.Order;
import org.springframework.stereotype.Component;
import payment.payment_service.entity.Transaction;

import java.util.List;

@Component
public interface PaymentService {

    public Transaction createTransaction(Double amount);
    abstract Transaction prepareTransaction(Order order);
    List<Transaction> getAll();
}
