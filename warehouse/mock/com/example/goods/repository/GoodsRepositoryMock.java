package com.example.goods.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.goods.domain.Goods;
import com.example.goods.exception.GoodsCodeDupulicateException;
import com.example.goods.exception.NoGoodsException;
import com.example.goods.service.GoodsRepository;

public class GoodsRepositoryMock implements GoodsRepository {

	@Override
	public void createGoods(Connection connection, Goods goods) throws SQLException, GoodsCodeDupulicateException {
		if(goods.getCode() == 900) throw new SQLException();
		if(goods.getCode() == 0) throw new GoodsCodeDupulicateException();
		if(goods.getCode() == 1) throw new GoodsCodeDupulicateException();
		if(goods.getCode() == 2) throw new GoodsCodeDupulicateException();
		if(goods.getCode() == 6) throw new GoodsCodeDupulicateException();
		if(goods.getCode() == 7) throw new GoodsCodeDupulicateException();
		return;
	}

	@Override
	public List<Goods> findAllGoods(Connection connection) throws SQLException, NoGoodsException {
		List<Goods> goodsList = new ArrayList<Goods>();
		goodsList.add(new Goods(0, "りんご", 100));
		goodsList.add(new Goods(1, "いちご", 350));
		goodsList.add(new Goods(2, "白菜", 90));
		goodsList.add(new Goods(6, "クレヨン", 1280));
		goodsList.add(new Goods(7, "サインペン", 50));
		return goodsList;
	}

	@Override
	public Goods findGoods(Connection connection, int goodsCode) throws SQLException, NoGoodsException {
		if(goodsCode == 900) throw new SQLException();
		if(goodsCode == 0) return new Goods(0, "りんご", 100);
		if(goodsCode == 1) return new Goods(1, "いちご", 350);
		if(goodsCode == 2) return new Goods(2, "白菜", 90);
		if(goodsCode == 6) return new Goods(6, "クレヨン", 1280);
		if(goodsCode == 7) return new Goods(7, "サインペン", 50);
		throw new NoGoodsException();
	}

	@Override
	public void deleteGoods(Connection connection, int goodsCode) throws SQLException, NoGoodsException {
		if(goodsCode == 900) throw new SQLException();
		if(goodsCode == 0) return;
		if(goodsCode == 1) return;
		if(goodsCode == 2) return;
		if(goodsCode == 6) return;
		if(goodsCode == 7) return;
		throw new NoGoodsException();
	}

	@Override
	public boolean isGoodsDeactive(Connection connection, int goodsCode) throws SQLException {
		if(goodsCode == 0) return false;
		if(goodsCode == 1) return false;
		if(goodsCode == 2) return false;
		if(goodsCode == 3) return true;
		if(goodsCode == 4) return true;
		if(goodsCode == 5) return true;
		if(goodsCode == 6) return false;
		if(goodsCode == 7) return false;
		 return false;
	}

}
