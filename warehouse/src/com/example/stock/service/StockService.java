package com.example.stock.service;

import java.sql.SQLException;
import java.util.List;

import com.example.goods.exception.NoGoodsException;
import com.example.stock.domain.Stock;
import com.example.stock.exception.NoStockException;
import com.example.stock.exception.StockCodeDupulicateException;
import com.example.stock.exception.StockDeletedException;
import com.example.stock.exception.StockOverException;

public interface StockService {
	void createStock(Stock stock)
			throws SQLException, StockCodeDupulicateException, ClassNotFoundException,
			NoGoodsException, StockDeletedException;

	List<Stock> findAllStock() throws SQLException, NoStockException, ClassNotFoundException;

	Stock findStock(int goodsCode)
			throws SQLException, NoStockException, ClassNotFoundException;

	void deleteStock(int goodsCode)
			throws SQLException, ClassNotFoundException, NoStockException, StockDeletedException,
			StockCodeDupulicateException;

	void InboundStock(int goodsCode, int quantity)
			throws SQLException, ClassNotFoundException, NoStockException, StockOverException;

	void OutboundStock(int goodsCode, int quantity)
			throws SQLException, ClassNotFoundException, NoStockException, StockOverException;

}
