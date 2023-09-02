package com.vivid.dream.service.impl;

import com.vivid.dream.kafka.producer.CouponCreateProducer;
import com.vivid.dream.repository.CouponCountRepository;
import com.vivid.dream.repository.CouponRepository;
import com.vivid.dream.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;
//    private final RedisTemplate<String, String> redisTemplate;
//
//    public void apply(Long userId) throws InterruptedException {
//        if(RedisLockUtils.couponUserAdd(redisTemplate, userId) != 1) return;
//
//        long count = couponCountRepository.increment();
//        if(count > 100){
//            return;
//        }
//        couponCreateProducer.create(userId);
//    }
}
