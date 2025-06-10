package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrease(Long id, Long quantity) {
        // synchronized를 사용하면 해당 메소드는 하나의 스레드만 접근 가능
        Stock stock = stockRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
