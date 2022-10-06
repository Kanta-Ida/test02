package com.example.goods.repository;

import static com.example.common.constant.DataConstant.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import com.example.goods.domain.Goods;
import com.example.goods.exception.GoodsCodeDupulicateException;
import com.example.goods.exception.NoGoodsException;
import com.example.goods.service.GoodsRepository;

public class GoodsRepositoryImpl implements GoodsRepository {

	private static final String INSERT = "INSERT INTO GOODS(CODE, NAME, PRICE, STATUS) VALUES(?, ?, ?, ?)";
	private static final String SELECT_ALL = "SELECT CODE, NAME, PRICE FROM GOODS WHERE STATUS =?";
	private static final String SELECT_ONE_GOODS = "SELECT CODE, NAME, PRICE FROM GOODS WHERE CODE = ? AND STATUS = ?";
	private static final String UPDATE = "UPDATE GOODS SET STATUS = ? WHERE  CODE = ? AND STATUS =?";

	@Override
	public void createGoods(Connection connection, Goods goods) throws SQLException, GoodsCodeDupulicateException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
			preparedStatement.setInt(1, goods.getCode());
			preparedStatement.setString(2, goods.getName());
			preparedStatement.setInt(3, goods.getPrice());
			preparedStatement.setString(4, STATUS_ACTIVE);
			preparedStatement.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new GoodsCodeDupulicateException();
		}
	}

	@Override
	public List<Goods> findAllGoods(Connection connection) throws SQLException, NoGoodsException {
		List<Goods> goodsList = null;

		try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL)) {
			preparedStatement.setString(1, STATUS_ACTIVE);
			goodsList = getGoods(preparedStatement);
		}

		if (goodsList.isEmpty())
			throw new NoGoodsException();

		return goodsList;
	}

	public Goods findGoods(Connection connection, int goodsCode) throws SQLException, NoGoodsException {
		Goods goods = null;

		String goodscode = Integer.toString(goodsCode);

		try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ONE_GOODS)) {
			preparedStatement.setString(1, goodscode);
			preparedStatement.setString(2, STATUS_ACTIVE);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					goods = new Goods(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3));
				}
			}
		}

		if (goods == null) {
			throw new NoGoodsException();

		}

		return goods;
	}

	@Override
	public void deleteGoods(Connection connection, int goodsCode) throws SQLException, NoGoodsException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
			preparedStatement.setString(1, STATUS_DEACTIVE);
			preparedStatement.setInt(2, goodsCode);
			preparedStatement.setString(3, STATUS_ACTIVE);
			int count = preparedStatement.executeUpdate();
			if (count == 0) {
				throw new NoGoodsException();
			}
		}
	}

	@Override
	public boolean isGoodsDeactive(Connection connection, int goodsCode) throws SQLException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ONE_GOODS)) {
			preparedStatement.setInt(1, goodsCode);
			preparedStatement.setString(2, STATUS_DEACTIVE);
			List<Goods> goodsList = getGoods(preparedStatement);

			if (goodsList.isEmpty())
				return false;
			return true;
		}
	}

	private List<Goods> getGoods(PreparedStatement preparedStatement) throws SQLException {
		List<Goods> goodsList = new ArrayList<Goods>();

		try (ResultSet resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) {
				Goods goods = new Goods(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3));
				goodsList.add(goods);
			}
		}
		return goodsList;
	}
}
