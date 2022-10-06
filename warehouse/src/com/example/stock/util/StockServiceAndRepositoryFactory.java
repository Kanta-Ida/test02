package com.example.stock.util;

import com.example.stock.repository.StockRepositoryImpl;
import com.example.stock.service.StockRepository;
import com.example.stock.service.StockService;
import com.example.stock.service.StockServiceImpl;

public class StockServiceAndRepositoryFactory {
	private static final StockRepository stockRepository = new StockRepositoryImpl();
	private static final StockService stockService = new StockServiceImpl(stockRepository);

	public static StockService getStockService() {
		return stockService;
	}

	public static StockRepository getStockRepository() {
		return stockRepository;
	}
}
