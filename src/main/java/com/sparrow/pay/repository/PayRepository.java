package com.sparrow.pay.repository;

import com.sparrow.pay.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay,Long> {
}
