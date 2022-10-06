package com.example.stock.ui;

import static com.example.common.constant.DataConstant.*;
import static com.example.common.constant.MessageConstant.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.example.common.ui.BaseUi;
import com.example.stock.domain.Stock;
import com.example.stock.exception.NoStockException;
import com.example.stock.service.StockService;

public class StockFindUi extends BaseUi {

	private final Scanner console;
	private final StockService stockService;

	StockFindUi(Scanner console, StockService stockService) {
		this.console = console;
		this.stockService = stockService;
	}

	void findAllStock() throws ClassNotFoundException, SQLException {
		System.out.println(COMMON_FIND_ALL_START);
		try {
			List<Stock> stockList = stockService.findAllStock();
			for (Stock stock : stockList) {
				System.out.println(stock);
			}
		} catch (NoStockException e) {
			System.out.println(STOCK_NO_DATA);
		}
		System.out.println(COMMON_FIND_END);
	}

	void findStock() throws ClassNotFoundException, SQLException {
		try {
			System.out.println(COMMON_FIND_START);
			try {
				int goodscode = goodsCodeInput(console);
				Stock stock = stockService.findStock(goodscode);
				System.out.println(stock);

			} catch (NoStockException e) {
				System.out.println(STOCK_NO_DATA);
			}
			System.out.println(COMMON_FIND_END);

			while (true) {
				String ans = nextOrEndInput(console, STOCK_NEXT_OR_END);
				if (ans.equals(NEXT)) {
					System.out.println(COMMON_FIND_START);
					try {
						int goodscode = goodsCodeInput(console);
						Stock stock = stockService.findStock(goodscode);
						System.out.println(stock);

					} catch (NoStockException e) {
						System.out.println(STOCK_NO_DATA);
					}
					System.out.println(COMMON_FIND_END);
				} else if (ans.equals(END)) {
					System.out.println(COMMON_END);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
