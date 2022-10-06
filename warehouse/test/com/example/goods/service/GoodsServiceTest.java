package com.example.goods.service;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.assertion.DiffCollectingFailureHandler;
import org.dbunit.assertion.Difference;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.goods.domain.Goods;
import com.example.goods.exception.GoodsCodeDupulicateException;
import com.example.goods.exception.GoodsDeletedException;
import com.example.goods.exception.NoGoodsException;
import com.example.goods.util.GoodsServiceAndRepositoryFactory;

public class GoodsServiceTest {

	private static final String TEST_DATA_DIRECTORY = "./test/data/goods/";

	private IDatabaseTester databaseTester;
	private GoodsService goodsService = GoodsServiceAndRepositoryFactory.getGoodsService();

	public GoodsServiceTest() throws Exception {
		databaseTester =
				// MySQLの場合
				new JdbcDatabaseTester("com.mysql.jdbc.Driver",
						"jdbc:mysql://localhost:3306/warehouse?useSSL=false", "root", "root");
		// HSQLDBの場合
		//				new JdbcDatabaseTester("org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost", "sa", "");
	}

	@Before
	public void before() throws Exception {
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(new File(TEST_DATA_DIRECTORY + "INPUT_GOODS_DATA.xml"));
		databaseTester.setDataSet(dataSet);
		databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);

		databaseTester.onSetup();
	}

	@After
	public void after() throws Exception {
		databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
		databaseTester.onTearDown();
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
	public void testFindAllGoods_異常系_1件もない() throws Exception {
		databaseTester.setSetUpOperation(DatabaseOperation.DELETE_ALL);
		databaseTester.onSetup();

		try {
			List<Goods> goodsList = goodsService.findAllGoods();
			for (Goods goods : goodsList) {
				System.out.println(goods);
			}
		} catch (NoGoodsException e) {
			assertTrue(true);
			return;
		}
		fail();
	}

	@Test
	public void testCreateGoods_正常系() throws Exception {

		Goods goods = new Goods(99, "バナナ", 210);

		goodsService.createGoods(goods);

		IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("GOODS");

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(new File(TEST_DATA_DIRECTORY + "EXPECTED_CREATE_GOODS_DATA.xml"));
		ITable expectedTable = expectedDataSet.getTable("GOODS");

		DiffCollectingFailureHandler myHandler = new DiffCollectingFailureHandler();
		Assertion.assertEquals(expectedTable, actualTable, myHandler);

		@SuppressWarnings("unchecked")
		List<Difference> diffList = myHandler.getDiffList();
		for (Difference diff : diffList) {
			if (!"ID".equals(diff.getColumnName())) {
				failWithDiff(diff);
			}
		}
	}

	@Test
	public void testCreateGoods_異常系_商品コードの重複() throws Exception {

		Goods goods = new Goods(0, "イチジク", 210);

		try {
			goodsService.createGoods(goods);
		} catch (GoodsCodeDupulicateException e) {
			assertTrue(true);
			return;
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

		IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("GOODS");

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(new File(TEST_DATA_DIRECTORY + "EXPECTED_DELETE_GOODS_DATA.xml"));
		ITable expectedTable = expectedDataSet.getTable("GOODS");

		DiffCollectingFailureHandler myHandler = new DiffCollectingFailureHandler();
		Assertion.assertEquals(expectedTable, actualTable, myHandler);

		@SuppressWarnings("unchecked")
		List<Difference> diffList = myHandler.getDiffList();
		for (Difference diff : diffList) {
			if (!"ID".equals(diff.getColumnName())) {
				failWithDiff(diff);
			}
		}
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

	private void failWithDiff(Difference diff) {
		fail(
				"columnName = " + diff.getColumnName()
						+ ", rowIndex = " + diff.getRowIndex()
						+ ", expectedValue = " + diff.getExpectedValue()
						+ ", actualValue = " + diff.getActualValue());
	}
}