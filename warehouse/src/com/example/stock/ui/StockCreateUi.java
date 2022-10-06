package com.example.stock.ui;

import static com.example.common.constant.MessageConstant.*;

import java.sql.SQLException;
import java.util.Scanner;

import com.example.common.ui.BaseUi;
import com.example.goods.exception.NoGoodsException;
import com.example.stock.domain.Stock;
import com.example.stock.exception.StockCodeDupulicateException;
import com.example.stock.exception.StockDeletedException;
import com.example.stock.service.StockService;

public class StockCreateUi extends BaseUi {
	private final Scanner console;
	private final StockService stockService;

	StockCreateUi(Scanner console, StockService stockService) {
		this.console = console;
		this.stockService = stockService;
	}

	void createStock() throws SQLException, ClassNotFoundException {
		System.out.println(COMMON_CREATE_START);
		while (true) {
			int stockCode = goodsCodeInput(console);

			Stock stock = new Stock(stockCode, 0);

			createExecute(stock);

			if (isNextOrEndInput(console))
				break;
		}
	}

	private void createExecute(Stock stock) throws SQLException, ClassNotFoundException {
		if (!isYesNoInput(console, COMMON_CREATE_YES_OR_NO)) {
			System.out.println(COMMON_CREATE_CANCEL);
			return;
		}

		try {
			stockService.createStock(stock);
		} catch (StockCodeDupulicateException e) {
			System.out.println(STOCK_GOODS_CODE_DUPULICATE);
		} catch (NoGoodsException e) {
			System.out.println(GOODS_NO_DATA);
		} catch (StockDeletedException e) {
			System.out.println(STOCK_GOODS_CODE_DELETE2);
		}
		System.out.println(COMMON_CREATE_END);
	}
}
