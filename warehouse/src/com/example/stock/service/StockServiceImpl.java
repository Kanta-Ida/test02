package com.example.stock.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import com.example.common.transaction.TransactionManager;
import com.example.goods.exception.NoGoodsException;
import com.example.goods.repository.GoodsRepositoryImpl;
import com.example.stock.domain.Stock;
import com.example.stock.exception.NoStockException;
import com.example.stock.exception.StockCodeDupulicateException;
import com.example.stock.exception.StockDeletedException;
import com.example.stock.exception.StockOverException;

public class StockServiceImpl implements StockService {
	private StockRepository stockRepository;

	public StockServiceImpl(StockRepository stockRepository) {
		this.stockRepository = stockRepository;
	}

	@Override
	public void createStock(Stock stock)
			throws ClassNotFoundException, SQLException, StockCodeDupulicateException,
			NoGoodsException, StockDeletedException {
		try (Connection connection = TransactionManager.getConnection()) {
			try {
				GoodsRepositoryImpl goodsrepository = new GoodsRepositoryImpl();
				if (stockRepository.isStockDeactive(connection, stock.getCode())) {
					throw new StockDeletedException();
				}
				goodsrepository.findGoods(connection, stock.getCode());
				stockRepository.createStock(connection, stock);
				TransactionManager.commit(connection);
			} catch (SQLIntegrityConstraintViolationException e) {
				throw new StockCodeDupulicateException();
			} catch (Exception e) {
				TransactionManager.rollback(connection);
				throw e;
			}
		}
	}

	@Override
	public List<Stock> findAllStock() throws SQLException, NoStockException, ClassNotFoundException {
		try (Connection connection = TransactionManager.getReadOnlyConnection()) {
			List<Stock> stockList = stockRepository.findAllStock(connection);
			return stockList;
		}
	}

	@Override
	public Stock findStock(int goodsCode)
			throws SQLException, NoStockException, ClassNotFoundException {
		try (Connection connection = TransactionManager.getReadOnlyConnection()) {
			Stock stock = stockRepository.findStock(connection, goodsCode);
			return stock;
		}
	}

	@Override
	public void deleteStock(int goodsCode)
			throws SQLException, NoStockException, ClassNotFoundException, StockDeletedException,
			StockCodeDupulicateException {
		try (Connection connection = TransactionManager.getConnection()) {
			try {
				if (stockRepository.isStockDeactive(connection, goodsCode)) {
					throw new StockDeletedException();
				}
				if (stockRepository.isStockNotEmplty(connection, goodsCode)) {
					throw new StockCodeDupulicateException();
				}
				stockRepository.deleteStock(connection, goodsCode);
				TransactionManager.commit(connection);
			} catch (Exception e) {
				TransactionManager.rollback(connection);
				throw e;
			}
		}
	}

	@Override
	public void InboundStock(int goodsCode, int quantity)
			throws SQLException, NoStockException, ClassNotFoundException, StockOverException {
		try (Connection connection = TransactionManager.getConnection()) {
			try {
				stockRepository.InboundStock(connection, goodsCode, quantity);
				TransactionManager.commit(connection);
			} catch (Exception e) {
				TransactionManager.rollback(connection);
				throw e;
			}
		}
	}

	@Override
	public void OutboundStock(int goodsCode, int quantity)
			throws SQLException, NoStockException, ClassNotFoundException, StockOverException {
		try (Connection connection = TransactionManager.getConnection()) {
			try {
				stockRepository.OutboundStock(connection, goodsCode, quantity);
				TransactionManager.commit(connection);
			} catch (Exception e) {
				TransactionManager.rollback(connection);
				throw e;
			}
		}
	}
}
