package com.example.goods.service;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.example.goods.domain.Goods;
import com.example.goods.exception.GoodsCodeDupulicateException;
import com.example.goods.exception.GoodsDeletedException;
import com.example.goods.exception.NoGoodsException;
import com.example.goods.repository.GoodsRepositoryMock;

public class GoodsService_RepositoryMock_Test {
	private GoodsRepository repositoryMock;
	private GoodsService goodsService;

	@Before
	public void setUp() throws Exception {
		repositoryMock = new GoodsRepositoryMock();
		goodsService = new GoodsServiceImpl(repositoryMock);
	}

	@Test
	public void testFindGoods_正常系() throws Exception {
		Goods goods = goodsService.findGoods(1);

		assertEquals(1, goods.getCode());
		assertEquals("いちご", goods.getName());
		assertEquals(350, goods.getPrice());
	}

	@Test
	public void testFindGoods_異常系_存在しない商品コード() {
		try {
			goodsService.findGoods(777);
		} catch (NoGoodsException e) {
			assertTrue(true);
			return;
		} catch (ClassNotFoundException | SQLException e) {
			fail();
		}
		fail();
	}

	@Test
	public void testFindAllGoods_正常系() throws Exception {
		List<Goods> goodsList = goodsService.findAllGoods();

		if (goodsList.size() != 5)
			fail();

		Goods goods = goodsList.get(0);
		assertEquals(0, goods.getCode());
		assertEquals("りんご", goods.getName());
		assertEquals(100, goods.getPrice());
		goods = goodsList.get(1);
		assertEquals(1, goods.getCode());
		assertEquals("いちご", goods.getName());
		assertEquals(350, goods.getPrice());
		goods = goodsList.get(2);
		assertEquals(2, goods.getCode());
		assertEquals("白菜", goods.getName());
		assertEquals(90, goods.getPrice());
		goods = goodsList.get(3);
		assertEquals(6, goods.getCode());
		assertEquals("クレヨン", goods.getName());
		assertEquals(1280, goods.getPrice());
		goods = goodsList.get(4);
		assertEquals(7, goods.getCode());
		assertEquals("サインペン", goods.getName());
		assertEquals(50, goods.getPrice());
	}

	@Test
	public void testCreateGoods_正常系() throws Exception {
		Goods goods = new Goods(99, "バナナ", 210);
		goodsService.createGoods(goods);
		assertTrue(true);
	}

	@Test
	public void testCreateGoods_異常系_商品コードの重複() {

		Goods goods = new Goods(0, "イチジク", 210);

		try {
			goodsService.createGoods(goods);
		} catch (GoodsCodeDupulicateException e) {
			assertTrue(true);
			return;
		} catch (ClassNotFoundException|SQLException|GoodsDeletedException e) {
			fail();
		}
		fail();
	}

	@Test
	public void testCreateGoods_異常系_削除済みの商品コードの重複() {

		Goods goods = new Goods(3, "イチジク", 210);

		try {
			goodsService.createGoods(goods);
		} catch (GoodsDeletedException e) {
			return;
		} catch (ClassNotFoundException | SQLException | GoodsCodeDupulicateException e) {
			fail();
		}
		fail();
	}

	@Test
	public void testDeleteGoods_正常系() throws Exception {
		goodsService.deleteGoods(1);
		assertTrue(true);
	}

	@Test
	public void testDeleteGoods_異常系_存在しない商品コード() {
		try {
			goodsService.deleteGoods(1001);
		} catch (NoGoodsException e) {
			assertTrue(true);
			return;
		} catch (ClassNotFoundException | SQLException | GoodsDeletedException e) {
			fail();
		}
		fail();
	}

	@Test
	public void testDeleteGoods_異常系_削除済みの商品コード() {
		try {
			goodsService.deleteGoods(3);
		} catch (GoodsDeletedException e) {
			assertTrue(true);
			return;
		} catch (ClassNotFoundException | SQLException | NoGoodsException e) {
			fail();
		}
		fail();
	}
}
