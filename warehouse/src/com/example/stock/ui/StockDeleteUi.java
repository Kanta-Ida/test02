package com.example.stock.ui;

import static com.example.common.constant.MessageConstant.*;

import java.sql.SQLException;
import java.util.Scanner;

import com.example.common.ui.BaseUi;
import com.example.stock.domain.Stock;
import com.example.stock.exception.NoStockException;
import com.example.stock.exception.StockCodeDupulicateException;
import com.example.stock.exception.StockDeletedException;
import com.example.stock.service.StockService;

public class StockDeleteUi extends BaseUi {
	private final Scanner console;
	private final StockService stockService;

	StockDeleteUi(Scanner console, StockService stockService) {
		this.console = console;
		this.stockService = stockService;
	}

	void deleteStock() throws SQLException, ClassNotFoundException {
		System.out.println(COMMON_DELETE_START);
		while (true) {
			int goodsCode = goodsCodeInput(console);

			deleteExecute(goodsCode);

			if (isNextOrEndInput(console))
				break;
		}
	}

	private void deleteExecute(int goodsCode) throws SQLException, ClassNotFoundException {
		try {
			Stock stock = stockService.findStock(goodsCode);
			System.out.println(stock);

			if (!isYesNoInput(console, COMMON_DELETE_YES_OR_NO)) {
				System.out.println(COMMON_DELETE_CANCEL);
				return;
			}

			stockService.deleteStock(goodsCode);
		} catch (StockCodeDupulicateException e) {
			System.out.println(STOCK_NOT_EMPTY);
		} catch (StockDeletedException e) {
			System.out.println(STOCK_GOODS_CODE_DELETE);
		} catch (NoStockException e) {
			System.out.println(STOCK_NO_DATA);
		}
		System.out.println(COMMON_DELETE_END);
	}
}
