package com.example.stock.repository;

import static com.example.common.constant.DataConstant.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import com.example.stock.domain.Stock;
import com.example.stock.exception.NoStockException;
import com.example.stock.exception.StockOverException;
import com.example.stock.service.StockRepository;

public class StockRepositoryImpl implements StockRepository {

	private static final String INSERT = "INSERT INTO STOCK(GOODS_CODE, QUANTITY, STATUS) VALUES(?, ?, ?)";
	private static final String SELECT_ONE_STOCK = "SELECT GOODS_CODE, QUANTITY FROM STOCK WHERE GOODS_CODE = ? AND STATUS = ?";
	private static final String SELECT_ALL = "SELECT GOODS_CODE, QUANTITY FROM STOCK WHERE STATUS =?";
	private static final String UPDATE = "UPDATE STOCK SET STATUS = ? WHERE  GOODS_CODE = ? AND STATUS =?";
	private static final String SELECT_NOT_EMPTY_STOCK = "SELECT QUANTITY FROM STOCK WHERE GOODS_CODE = ? AND STATUS = ?";
	private static final String INBOUND = "UPDATE STOCK SET QUANTITY = ? WHERE  GOODS_CODE = ? AND STATUS =?";
	private static final String OUTBOUND = "UPDATE STOCK SET QUANTITY = ? WHERE  GOODS_CODE = ? AND STATUS =?";
	private static final String LOG = "INSERT INTO IN_OUT_LOG(IN_OUT_TYPE,GOODS_CODE,QUANTITY)VALUES(?,?,?)";

	@Override
	public void createStock(Connection connection, Stock stock)
			throws SQLException, SQLIntegrityConstraintViolationException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
			preparedStatement.setInt(1, stock.getCode());
			preparedStatement.setInt(2, stock.getQuantity());
			preparedStatement.setString(3, STATUS_ACTIVE);
			preparedStatement.executeUpdate();
		}
	}

	@Override
	public List<Stock> findAllStock(Connection connection) throws SQLException, NoStockException {
		List<Stock> stockList = null;

		try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL)) {
			preparedStatement.setString(1, STATUS_ACTIVE);
			stockList = getStock(preparedStatement);
		}

		if (stockList.isEmpty())
			throw new NoStockException();

		return stockList;
	}

	public Stock findStock(Connection connection, int goodsCode)
			throws SQLException, NoStockException {
		Stock stock = null;

		String goodscode = Integer.toString(goodsCode);

		try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ONE_STOCK)) {
			preparedStatement.setString(1, goodscode);
			preparedStatement.setString(2, STATUS_ACTIVE);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					stock = new Stock(resultSet.getInt(1), resultSet.getInt(2));
				}
			}
		}

		if (stock == null) {
			throw new NoStockException();

		}

		return stock;
	}

	@Override
	public void deleteStock(Connection connection, int goodsCode)
			throws SQLException, NoStockException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
			preparedStatement.setString(1, STATUS_DEACTIVE);
			preparedStatement.setInt(2, goodsCode);
			preparedStatement.setString(3, STATUS_ACTIVE);
			int count2 = preparedStatement.executeUpdate();
			if (count2 == 0) {
				throw new NoStockException();
			}
		}
	}

	@Override
	public void InboundStock(Connection connection, int goodsCode, int quantity)
			throws SQLException, NoStockException, StockOverException {
		int allQuantity = 0;
		Stock stock = null;

		String goodscode = Integer.toString(goodsCode);

		try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ONE_STOCK)) {
			preparedStatement.setString(1, goodscode);
			preparedStatement.setString(2, STATUS_ACTIVE);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					stock = new Stock(resultSet.getInt(1), resultSet.getInt(2));
				}
			}

			if (stock == null) {
				throw new NoStockException();

			}

			allQuantity = quantity + stock.getQuantity();

			if (allQuantity > 100) {
				throw new StockOverException();
			}
		}

		try (PreparedStatement preparedStatement = connection.prepareStatement(INBOUND)) {
			preparedStatement.setInt(1, allQuantity);
			preparedStatement.setInt(2, goodsCode);
			preparedStatement.setString(3, STATUS_ACTIVE);
			int count3 = preparedStatement.executeUpdate();
			if (count3 == 0) {
				throw new NoStockException();
			}
		}

		try (PreparedStatement preparedStatement = connection.prepareStatement(LOG)) {
			preparedStatement.setString(1, IN);
			preparedStatement.setString(2, goodscode);
			preparedStatement.setInt(3, quantity);
			preparedStatement.executeUpdate();
		}
	}

	@Override
	public void OutboundStock(Connection connection, int goodsCode, int quantity)
			throws SQLException, NoStockException, StockOverException {
		int allQuantity = 0;
		Stock stock = null;

		String goodscode = Integer.toString(goodsCode);

		try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ONE_STOCK)) {
			preparedStatement.setString(1, goodscode);
			preparedStatement.setString(2, STATUS_ACTIVE);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					stock = new Stock(resultSet.getInt(1), resultSet.getInt(2));
				}
			}

			if (stock == null) {
				throw new NoStockException();

			}

			allQuantity = stock.getQuantity() - quantity;

			if (allQuantity < 0) {
				throw new StockOverException();
			}
		}

		try (PreparedStatement preparedStatement = connection.prepareStatement(OUTBOUND)) {
			preparedStatement.setInt(1, allQuantity);
			preparedStatement.setInt(2, goodsCode);
			preparedStatement.setString(3, STATUS_ACTIVE);
			int count4 = preparedStatement.executeUpdate();
			if (count4 == 0) {
				throw new NoStockException();
			}
		}

		try (PreparedStatement preparedStatement = connection.prepareStatement(LOG)) {
			preparedStatement.setString(1, OUT);
			preparedStatement.setString(2, goodscode);
			preparedStatement.setInt(3, quantity);
			preparedStatement.executeUpdate();
		}
	}

	@Override
	public boolean isStockDeactive(Connection connection, int goodsCode) throws SQLException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ONE_STOCK)) {
			preparedStatement.setInt(1, goodsCode);
			preparedStatement.setString(2, STATUS_DEACTIVE);
			List<Stock> stockList = getStock(preparedStatement);

			if (stockList.isEmpty())
				return false;
			return true;

		}
	}

	@Override
	public boolean isStockNotEmplty(Connection connection, int goodsCode) throws SQLException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NOT_EMPTY_STOCK)) {
			preparedStatement.setInt(1, goodsCode);
			preparedStatement.setString(2, STATUS_ACTIVE);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					if (resultSet.getInt(1) == 0) {
						return false;
					}
				}
			}

		}
		return true;
	}

	private List<Stock> getStock(PreparedStatement preparedStatement) throws SQLException {
		List<Stock> stockList = new ArrayList<Stock>();

		try (ResultSet resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) {
				Stock stock = new Stock(resultSet.getInt(1), resultSet.getInt(2));
				stockList.add(stock);
			}
		}
		return stockList;
	}
}
