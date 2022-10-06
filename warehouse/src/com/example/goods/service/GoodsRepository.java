package com.example.goods.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.example.goods.domain.Goods;
import com.example.goods.exception.GoodsCodeDupulicateException;
import com.example.goods.exception.NoGoodsException;

public interface GoodsRepository {

	void createGoods(Connection connection, Goods goods) throws SQLException, GoodsCodeDupulicateException;

	List<Goods> findAllGoods(Connection connection) throws SQLException, NoGoodsException;

	Goods findGoods(Connection connection, int goodsCode) throws SQLException, NoGoodsException;

	void deleteGoods(Connection connection, int goodsCode) throws SQLException, NoGoodsException;

	boolean isGoodsDeactive(Connection connection, int goodsCode) throws SQLException;
}
