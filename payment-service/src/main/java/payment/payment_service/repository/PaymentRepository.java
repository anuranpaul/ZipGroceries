package payment.payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import payment.payment_service.entity.Transaction;

@Repository
public interface PaymentRepository extends JpaRepository<Transaction, String>
{

}
