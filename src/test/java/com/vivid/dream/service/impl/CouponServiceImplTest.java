package com.vivid.dream.service.impl;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CouponServiceImplTest {
//    @Autowired
//    private CouponService couponService;
//
//    @Autowired
//    private CouponRepository couponRepository;
//
//    @Test
//    @DisplayName("한먼만 응모")
//    void firstCouponApply() throws InterruptedException {
//        couponService.apply(1L);
//        long count = couponRepository.count();
//        assertEquals(1L, count);
//    }
//
//    @Test
//    @DisplayName("Executor 통하여 다중동시응모")
//    void multiApply() throws InterruptedException {
//        int requestCount = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(1);
//        CountDownLatch latch = new CountDownLatch(requestCount);
//
//        for (int i = 0; i < requestCount; i++) {
//            int finalI = i;
//            executorService.submit(() -> {
//                try {
//                    couponService.apply((long) finalI);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//        couponRepository.flush();
//
//        Thread.sleep(2_000);
//        long count = couponRepository.count();
//        assertEquals(100L, count);
//    }
//
//
//    @Test
//    @DisplayName("Executor 통하여 다중동시응모 And 한명당 하나씩만 쿠폰 발급가능")
//    void issue_one_coupon_person() throws InterruptedException {
//        int requestCount = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(1);
//        CountDownLatch latch = new CountDownLatch(requestCount);
//
//        for (int i = 0; i < 30; i++) {
//            int finalI = i;
//            executorService.submit(() -> {
//                try {
//                    couponService.apply((long) finalI);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        for (int i = 0; i < 20; i++) {
//            int finalI = i;
//            executorService.submit(() -> {
//                try {
//                    couponService.apply((long) finalI);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//        couponRepository.flush();
//
//        Thread.sleep(2_000);
//        long count = couponRepository.count();
//        assertEquals(100L, count);
//    }
}