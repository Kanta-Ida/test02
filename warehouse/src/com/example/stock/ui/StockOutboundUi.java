package com.example.stock.ui;

import static com.example.common.constant.MessageConstant.*;

import java.sql.SQLException;
import java.util.Scanner;

import com.example.common.ui.BaseUi;
import com.example.stock.exception.NoStockException;
import com.example.stock.exception.StockOverException;
import com.example.stock.service.StockService;

public class StockOutboundUi extends BaseUi {
	private final Scanner console;
	private final StockService stockService;

	StockOutboundUi(Scanner console, StockService stockService) {
		this.console = console;
		this.stockService = stockService;
	}

	void OutboundStock() throws SQLException, ClassNotFoundException {
		System.out.println(STOCK_OUTBOUND_START);
		while (true) {
			int goodsCode = goodsCodeInput(console);
			int quantity = quantityInput(console);

			OutboundExecute(goodsCode, quantity);

			if (isNextOrEndInput(console))
				break;
		}
	}

	private void OutboundExecute(int goodsCode, int quantity) throws SQLException, ClassNotFoundException {
		if (!isYesNoInput(console, STOCK_OUTBOUND_YES_OR_NO)) {
			System.out.println(STOCK_OUTBOUND_CANCEL);
			return;
		}

		try {
			stockService.OutboundStock(goodsCode, quantity);
		} catch (StockOverException e) {
			System.out.println(STOCK_UNDER);
		} catch (NoStockException e) {
			System.out.println(STOCK_NO_DATA);
		}
		System.out.println(STOCK_INBOUND_END);
	}
}
