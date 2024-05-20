package com.InventoryService.Inventory.repository;

import com.InventoryService.Inventory.entity.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory,String> {
    void deleteByProductCodeIn(List<String> productCodes);

    Optional<Inventory> findByProductCodeIn(List<String> productCodes);

    Optional<Inventory> findByProductCode(String productCode);
}
