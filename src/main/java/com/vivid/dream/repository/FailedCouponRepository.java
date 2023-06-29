package com.vivid.dream.repository;

import com.vivid.dream.entity.FailedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedCouponRepository extends JpaRepository<FailedCoupon, Long> {
}
