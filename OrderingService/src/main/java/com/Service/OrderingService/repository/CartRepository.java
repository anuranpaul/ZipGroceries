package com.Service.OrderingService.repository;

import com.Service.OrderingService.entity.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Item, String> {


    //Object save(Item item);

    List<Item> findByCartId(String cartId);

    void deleteByCartIdAndId(String cartId, Long itemId);

    @Modifying
    @Query("DELETE FROM Item i WHERE i.cartId = :cartId")
    void deleteByCartId(@Param("cartId") String cartId);

}
