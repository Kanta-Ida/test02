package com.example.stock.service;

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

import com.example.goods.exception.NoGoodsException;
import com.example.stock.domain.Stock;
import com.example.stock.exception.NoStockException;
import com.example.stock.exception.StockCodeDupulicateException;
import com.example.stock.exception.StockDeletedException;
import com.example.stock.util.StockServiceAndRepositoryFactory;

public class StockServiceTest {
	private static final String TEST_DATA_DIRECTORY = "./test/data/stock/";

	private IDatabaseTester databaseTester;
	private StockService stockService = StockServiceAndRepositoryFactory.getStockService();

	public StockServiceTest() throws Exception {
		databaseTester =
				// MySQLの場合
				new JdbcDatabaseTester("com.mysql.jdbc.Driver",
						"jdbc:mysql://localhost:3306/warehouse?useSSL=false", "root", "root");
		// HSQLDBの場合
		//				new JdbcDatabaseTester("org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost", "sa", "");
	}

	@Before
	public void before() throws Exception {

		IDataSet dataSet = new FlatXmlDataSetBuilder().build(new File(TEST_DATA_DIRECTORY + "INPUT_SERVICE_DATA.xml"));
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
	public void testFindStock_正常系() throws Exception {
		Stock stock = stockService.findStock(1);

		assertEquals(1, stock.getCode());
		assertEquals(20, stock.getQuantity());
	}

	@Test
	public void testFindStock_異常系_存在しない商品コード() {
		try {
			stockService.findStock(777);
		} catch (NoStockException e) {
			assertTrue(true);
			return;
		} catch (ClassNotFoundException | SQLException e) {
			fail();
		}
		fail();
	}

	@Test
	public void testFindAllStock_正常系() throws Exception {
		List<Stock> stockList = stockService.findAllStock();

		if (stockList.size() != 4)
			fail();

		Stock stock = stockList.get(0);
		assertEquals(0, stock.getCode());
		assertEquals(80, stock.getQuantity());
		stock = stockList.get(1);
		assertEquals(1, stock.getCode());
		assertEquals(20, stock.getQuantity());
		stock = stockList.get(2);
		assertEquals(3, stock.getCode());
		assertEquals(0, stock.getQuantity());
		stock = stockList.get(3);
		assertEquals(5, stock.getCode());
		assertEquals(3, stock.getQuantity());
	}

	@Test
	public void testFindAllStock_異常系_1件もない() throws Exception {
		databaseTester.setSetUpOperation(DatabaseOperation.DELETE_ALL);
		databaseTester.onSetup();

		try {
			List<Stock> stockList = stockService.findAllStock();
			for (Stock stock : stockList) {
				System.out.println(stock);
			}
		} catch (NoStockException e) {
			assertTrue(true);
			return;
		}
		fail();
	}

	@Test
	public void testCreateStock_正常系() throws Exception {

		Stock stock = new Stock(2, 0);

		stockService.createStock(stock);

		IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("STOCK");

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(new File(TEST_DATA_DIRECTORY + "EXPECTED_CREATE_STOCK_DATA.xml"));
		ITable expectedTable = expectedDataSet.getTable("STOCK");

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
	public void testCreateGoods_異常系_在庫コードの重複() throws Exception {

		Stock stock = new Stock(0, 10);

		try {
			stockService.createStock(stock);
		} catch (StockCodeDupulicateException e) {
			assertTrue(true);
			return;
		}
		fail();
	}

	@Test
	public void testCreateGoods_異常系_削除済みの商品コードの重複() throws Exception {
		Stock stock = new Stock(3, 10);

		try {
			stockService.createStock(stock);
		} catch (NoGoodsException e) {
			assertTrue(true);
			return;
		}
		fail();
	}

	@Test
	public void testCreateGoods_異常系_商品コードなし() throws Exception {

		Stock stock = new Stock(999, 10);

		try {
			stockService.createStock(stock);
		} catch (NoGoodsException e) {
			assertTrue(true);
			return;
		}
		fail();
	}

	@Test
	public void testCreateGoods_異常系_削除済みの在庫コードの重複() throws Exception {

		Stock stock = new Stock(7, 10);

		try {
			stockService.createStock(stock);
		} catch (StockDeletedException e) {
			assertTrue(true);
			return;
		}
		fail();
	}

	@Test
	public void testDeleteStock_正常系() throws Exception {

		stockService.deleteStock(3);

		IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("STOCK");

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(new File(TEST_DATA_DIRECTORY + "EXPECTED_DELETE_STOCK_DATA.xml"));
		ITable expectedTable = expectedDataSet.getTable("STOCK");

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
	public void testDeleteStock_異常系_存在しない商品コード() throws Exception {
		try {
			stockService.deleteStock(1001);
		} catch (StockCodeDupulicateException e) {
			assertTrue(true);
			return;
		}
		fail();
	}

	@Test
	public void testDeleteStock_異常系_削除済みの商品コード() throws Exception {
		try {
			stockService.deleteStock(4);
		} catch (StockDeletedException e) {
			assertTrue(true);
			return;
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
