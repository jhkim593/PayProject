package com.sparrow.pay.repository;

import com.sparrow.pay.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card,Long> {
}
