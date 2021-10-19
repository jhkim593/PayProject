package com.sparrow.pay.repository;

import com.sparrow.pay.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PayRepository extends JpaRepository<Pay,Long> {

    @Query("select p from Pay p where p.payId=:payId")
    Optional<Pay>findPayByPayId(@Param("payId")String payId);
}
