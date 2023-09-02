package com.vivid.dream.kafka.consumer;

import com.vivid.dream.repository.CouponRepository;
import com.vivid.dream.repository.FailedCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponCreatedConsumer {

    private final CouponRepository couponRepository;
    private final FailedCouponRepository failedCouponRepository;

//    @KafkaListener(topics = "coupon_create", groupId="group_1")
//    public void listener(Long userId){
//        try {
//            couponRepository.save(new Coupon(userId));
//        } catch (Exception e) {
//            log.error("failed to create coupon :: " + userId);
//            failedCouponRepository.save(new FailedCoupon(userId));
//        }
//    }
}
