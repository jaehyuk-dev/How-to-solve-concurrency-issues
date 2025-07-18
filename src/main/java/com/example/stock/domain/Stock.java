package com.example.stock.domain;

import jakarta.persistence.*;

@Entity
public class Stock {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Long quantity;

    @Version
    private Long version;

    public Stock() {}

    public Stock(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void decrease(Long quantity) {
        if(this.quantity < quantity) {
            throw new IllegalArgumentException("Quantity exceeds stock limit");
        } else {
            this.quantity -= quantity;
        }
    }
}
