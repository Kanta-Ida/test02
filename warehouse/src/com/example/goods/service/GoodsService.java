package com.example.goods.service;

import java.sql.SQLException;
import java.util.List;

import com.example.goods.domain.Goods;
import com.example.goods.exception.GoodsCodeDupulicateException;
import com.example.goods.exception.GoodsDeletedException;
import com.example.goods.exception.NoGoodsException;

public interface GoodsService {

	void createGoods(Goods goods) throws SQLException, GoodsCodeDupulicateException, ClassNotFoundException, GoodsDeletedException;

	List<Goods> findAllGoods() throws SQLException, NoGoodsException, ClassNotFoundException;

	Goods findGoods(int goodsCode) throws SQLException, NoGoodsException, ClassNotFoundException;

	void deleteGoods(int goodsCode) throws SQLException, ClassNotFoundException, NoGoodsException, GoodsDeletedException;

}