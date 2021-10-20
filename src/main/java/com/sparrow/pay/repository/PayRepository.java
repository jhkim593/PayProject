package com.sparrow.pay.repository;

import com.sparrow.pay.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface PayRepository extends JpaRepository<Pay,Long> {

//    @Query(value = "select * from Pay where pay_id=:payId",nativeQuery = true)
    Optional<Pay> findByPayId(String payId);

}
