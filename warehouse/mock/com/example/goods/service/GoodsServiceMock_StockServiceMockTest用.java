package com.example.goods.service;

import java.sql.SQLException;
import java.util.List;

import com.example.goods.domain.Goods;
import com.example.goods.exception.GoodsCodeDupulicateException;
import com.example.goods.exception.NoGoodsException;

public class GoodsServiceMock_StockServiceMockTest用 implements GoodsService {

	@Override
	public void createGoods(Goods goods) throws SQLException, GoodsCodeDupulicateException, ClassNotFoundException {
		// Nothing StockService Test
	}

	@Override
	public List<Goods> findAllGoods() throws SQLException, NoGoodsException, ClassNotFoundException {
		// Nothing StockService Test
		return null;
	}

	@Override
	public Goods findGoods(int goodsCode) throws SQLException, NoGoodsException, ClassNotFoundException {
		if(goodsCode == 900) throw new SQLException();
		if(goodsCode == 0) return new Goods(0, "りんご", 100);
		if(goodsCode == 1) return new Goods(1, "いちご", 350);
		if(goodsCode == 2) return new Goods(2, "白菜", 90);
		if(goodsCode == 6) return new Goods(6, "クレヨン", 1280);
		if(goodsCode == 7) return new Goods(7, "サインペン", 50);
		throw new NoGoodsException();
	}

	@Override
	public void deleteGoods(int goodsCode) throws SQLException, ClassNotFoundException, NoGoodsException {
		// Nothing StockService Test
	}

}
