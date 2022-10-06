package com.example.stock.ui;

import static com.example.common.constant.MessageConstant.*;

import java.sql.SQLException;
import java.util.Scanner;

import com.example.common.ui.BaseUi;
import com.example.stock.exception.NoStockException;
import com.example.stock.exception.StockOverException;
import com.example.stock.service.StockService;

public class StockInboundUi extends BaseUi {
	private final Scanner console;
	private final StockService stockService;

	StockInboundUi(Scanner console, StockService stockService) {
		this.console = console;
		this.stockService = stockService;
	}

	void InboundStock() throws SQLException, ClassNotFoundException {
		System.out.println(STOCK_INBOUND_START);
		while (true) {
			int goodsCode = goodsCodeInput(console);
			int quantity = quantityInput(console);

			InboundExecute(goodsCode, quantity);

			if (isNextOrEndInput(console))
				break;
		}
	}

	private void InboundExecute(int goodsCode, int quantity) throws SQLException, ClassNotFoundException {
		if (!isYesNoInput(console, STOCK_INBOUND_YES_OR_NO)) {
			System.out.println(STOCK_INBOUND_CANCEL);
			return;
		}

		try {
			stockService.InboundStock(goodsCode, quantity);
		} catch (StockOverException e) {
			System.out.println(STOCK_OVER);
		} catch (NoStockException e) {
			System.out.println(STOCK_NO_DATA);
		}
		System.out.println(STOCK_INBOUND_END);
	}
}
