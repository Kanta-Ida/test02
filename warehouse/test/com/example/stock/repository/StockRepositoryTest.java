package com.example.stock.repository;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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
import com.example.stock.service.StockRepository;
import com.example.stock.util.StockServiceAndRepositoryFactory;

public class StockRepositoryTest {
	private static final String TEST_DATA_DIRECTORY = "./test/data/stock/";
	private IDatabaseTester databaseTester;
	private StockRepository stockRepository = StockServiceAndRepositoryFactory.getStockRepository();

	private Connection connection;

	public StockRepositoryTest() throws Exception {
		databaseTester =
				// MySQLの場合
				new JdbcDatabaseTester("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/warehouse?useSSL=false",
						"root", "root");
		// HSQLDBの場合
		//				 new
		//				 JdbcDatabaseTester("org.hsqldb.jdbcDriver","jdbc:hsqldb:hsql://localhost",
		//				 "sa", "");
	}

	@Before
	public void before() throws Exception {
		connection = databaseTester.getConnection().getConnection();
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
		Stock stock = stockRepository.findStock(connection, 1);

		assertEquals(1, stock.getCode());
		assertEquals(20, stock.getQuantity());
	}

	@Test
	public void testFindStock_異常系_存在しない商品コード() {
		try {
			stockRepository.findStock(connection, 777);
		} catch (NoStockException e) {
			assertTrue(true);
			return;
		} catch (SQLException e) {
			fail();
		}
		fail();
	}

	@Test
	public void testFindStock_異常系_削除済みの商品コード() {
		try {
			stockRepository.findStock(connection, 4);
		} catch (NoStockException e) {
			assertTrue(true);
			return;
		} catch (SQLException e) {
			fail();
		}
		fail();
	}

	@Test
	public void testFindAllStock_正常系() throws Exception {
		List<Stock> stockList = stockRepository.findAllStock(connection);

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
			List<Stock> stockList = stockRepository.findAllStock(connection);
			for (Stock stock : stockList) {
				System.out.println(stock);
			}
		} catch (NoStockException e) {
			return;
		}
		fail();
	}

	@Test
	public void testCreateStock_正常系() throws Exception {

		Stock stock = new Stock(2, 0);

		stockRepository.createStock(connection, stock);

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
	public void testCreateStock_異常系_在庫コードの重複() throws NoGoodsException {

		Stock stock = new Stock(0, 10);

		try {
			stockRepository.createStock(connection, stock);
		} catch (SQLIntegrityConstraintViolationException e) {
			assertTrue(true);
			return;
		} catch (SQLException e) {
			fail();
		}
		fail();
	}

	@Test
	public void testDeleteStock_正常系() throws Exception {

		stockRepository.deleteStock(connection, 3);

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
			stockRepository.deleteStock(connection, 1001);
		} catch (NoStockException e) {
			assertTrue(true);
			return;
		} catch (SQLException e) {
			fail();
		}
		fail();
	}

	@Test
	public void testDeleteStock_異常系_削除済みの商品コード() throws Exception {
		try {
			stockRepository.deleteStock(connection, 4);
		} catch (NoStockException e) {
			assertTrue(true);
			return;
		} catch (SQLException e) {
			fail();
		}
		fail();
	}

	@Test
	public void testIsDeleteGoods_正常系_未削除() throws SQLException {
		boolean ans = stockRepository.isStockDeactive(connection, 0);
		if (!ans) {
			assertTrue(true);
			return;
		}
		fail();
	}

	@Test
	public void testIsDeleteGoods_正常系_削除済() throws SQLException {
		boolean ans = stockRepository.isStockDeactive(connection, 4);
		if (ans) {
			assertTrue(true);
			return;
		}
		fail();
	}

	@Test
	public void testInboundStock_正常系_STOCK() throws Exception {

		stockRepository.InboundStock(connection, 0, 7);

		IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("STOCK");

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(new File(TEST_DATA_DIRECTORY + "EXPECTED_SERVICE_STOCK_DATA2.xml"));
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
	public void testInboundStock_正常系_LOG() throws Exception {

		stockRepository.InboundStock(connection, 0, 7);

		IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("STOCK");

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(new File(TEST_DATA_DIRECTORY + "EXPECTED_SERVICE_INOUTBOUND_DATA2.xml"));
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

	private void failWithDiff(Difference diff) {
		fail(
				"columnName = " + diff.getColumnName()
						+ ", rowIndex = " + diff.getRowIndex()
						+ ", expectedValue = " + diff.getExpectedValue()
						+ ", actualValue = " + diff.getActualValue());
	}
}
