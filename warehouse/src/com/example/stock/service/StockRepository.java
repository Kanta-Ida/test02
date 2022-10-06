package com.example.stock.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import com.example.stock.domain.Stock;
import com.example.stock.exception.NoStockException;
import com.example.stock.exception.StockCodeDupulicateException;
import com.example.stock.exception.StockOverException;

public interface StockRepository {
	void createStock(Connection connection, Stock stock)
			throws SQLException, SQLIntegrityConstraintViolationException;

	List<Stock> findAllStock(Connection connection) throws SQLException, NoStockException;

	Stock findStock(Connection connection, int goodsCode)
			throws SQLException, NoStockException;

	void deleteStock(Connection connection, int goodsCode)
			throws SQLException, NoStockException,
			StockCodeDupulicateException;

	void InboundStock(Connection connection, int goodsCode, int quantity)
			throws SQLException, NoStockException,
			StockOverException;

	void OutboundStock(Connection connection, int goodsCode, int quantity)
			throws SQLException, NoStockException,
			StockOverException;

	boolean isStockDeactive(Connection connection, int goodsCode) throws SQLException;

	boolean isStockNotEmplty(Connection connection, int goodsCode) throws SQLException;

}
