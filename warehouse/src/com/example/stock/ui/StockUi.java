package com.example.stock.ui;

import static com.example.common.constant.DataConstant.*;
import static com.example.common.constant.MessageConstant.*;

import java.sql.SQLException;
import java.util.Scanner;

import com.example.common.ui.BaseUi;
import com.example.stock.service.StockService;
import com.example.stock.util.StockServiceAndRepositoryFactory;

public class StockUi extends BaseUi {
	private final Scanner console = new Scanner(System.in);
	private final StockService stockService = StockServiceAndRepositoryFactory.getStockService();
	private final StockCreateUi stockCreateUi = new StockCreateUi(console, stockService);
	private final StockFindUi stockFindUi = new StockFindUi(console, stockService);
	private final StockDeleteUi stockUpdateUi = new StockDeleteUi(console, stockService);
	private final StockInboundUi stockInboundUi = new StockInboundUi(console, stockService);
	private final StockOutboundUi stockOutboundUi = new StockOutboundUi(console, stockService);

	public static void main(String[] args) {
		StockUi stockUi = new StockUi();
		stockUi.start();
	}

	public void start() {
		try {
			while (true) {
				String ans = nextOrEndInput(console, STOCK_NEXT_OR_END);
				if (ans.equals(NEXT)) {
					selectOperation();
				} else if (ans.equals(END)) {
					System.out.println(COMMON_END);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void selectOperation() throws ClassNotFoundException, SQLException {
		while (true) {
			String ans = selectOperationOnetoThreeInput(console, STOCK_SELECT_OPERATION);
			if (ans.equals(ONE)) {
				while (true) {
					String ans2 = selectOperationOnetoFourInput(console);
					if (ans2.equals(ONE))
						stockCreateUi.createStock();
					else if (ans2.equals(TWO))
						stockFindUi.findAllStock();
					else if (ans2.equals(THREE))
						stockFindUi.findStock();
					else if (ans2.equals(FOUR))
						stockUpdateUi.deleteStock();
					else if (ans2.equals(END))
						break;
				}
			} else if (ans.equals(TWO)) {
				while (true) {
					String ans3 = selectOperationOnetoTwoInput(console, STOCK_SELECT_IN_OUT_BOUND);
					if (ans3.equals(ONE))
						stockInboundUi.InboundStock();
					else if (ans3.equals(TWO))
						stockOutboundUi.OutboundStock();
					else if (ans3.equals(END))
						break;
				}
			} else if (ans.equals(THREE)) {
				System.out.println("準備中です。");
			} else if (ans.equals(END))
				break;
		}
	}
}
