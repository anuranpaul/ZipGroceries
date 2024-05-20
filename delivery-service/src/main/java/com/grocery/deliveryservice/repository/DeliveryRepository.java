package com.grocery.deliveryservice.repository;

import com.grocery.deliveryservice.entity.Delivery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends MongoRepository<Delivery,String> {

}
