package com.vivid.dream.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "tb_failed_coupon")
public class FailedCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    public FailedCoupon(Long userId) {
        this.userId = userId;
    }
}
