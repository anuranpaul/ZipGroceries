package payment.payment_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payment.payment_service.entity.Transaction;
import payment.payment_service.service.PaymentService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/createTransaction/{amount}")
    public ResponseEntity<Transaction> createTransaction(@PathVariable Double amount) {
        return ResponseEntity.ok(paymentService.createTransaction(amount));
    }

    @GetMapping("/allTransactions")
    public ResponseEntity<List<Transaction>> allTransactions() {
        return ResponseEntity.ok(paymentService.getAll());
    }
}
