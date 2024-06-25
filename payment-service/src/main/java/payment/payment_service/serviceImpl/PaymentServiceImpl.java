package payment.payment_service.serviceImpl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import payment.payment_service.entity.Transaction;
import payment.payment_service.repository.PaymentRepository;
import payment.payment_service.service.PaymentService;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${key}")
    private String key;

    @Value("${key_secret}")
    private String key_secret;

    @Value("${currency}")
    private String currency;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Transaction createTransaction(Double amount) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount", amount * 100);
            jsonObject.put("currency", currency);
            RazorpayClient razorpayClient = new RazorpayClient(key, key_secret);
            Order order = razorpayClient.orders.create(jsonObject);
            Transaction transactionDetails = prepareTransaction(order);
            paymentRepository.save(transactionDetails);
            return transactionDetails;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    @Override
    public Transaction prepareTransaction(Order order) {
        String orderId = order.get("id");
        String currency = order.get("currency");
        Integer amount = order.get("amount");

        Transaction transaction = new Transaction();
        transaction.setOrderId(orderId);
        amount = amount / 100;
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setApiKey(key);
        return transaction;
    }

    @Override
    public List<Transaction> getAll() {
        return paymentRepository.findAll();
    }

}
