package com.example.goods.ui;

import static com.example.common.constant.DataConstant.*;
import static com.example.common.constant.MessageConstant.*;

import java.sql.SQLException;
import java.util.Scanner;

import com.example.common.ui.BaseUi;
import com.example.goods.service.GoodsService;
import com.example.goods.util.GoodsServiceAndRepositoryFactory;

public class GoodsUi extends BaseUi {
	private final Scanner console = new Scanner(System.in);
	private final GoodsService goodsService = GoodsServiceAndRepositoryFactory.getGoodsService();
	private final GoodsCreateUi goodsCreateUi = new GoodsCreateUi(console, goodsService);
	private final GoodsFindUi goodsFindUi = new GoodsFindUi(console, goodsService);
	private final GoodsDeleteUi goodsUpdateUi = new GoodsDeleteUi(console, goodsService);

	public static void main(String[] args) {
		GoodsUi goodsUi = new GoodsUi();
		goodsUi.start();
	}

	public void start() {
		try {
			while (true) {
				String ans = nextOrEndInput(console, GOODS_NEXT_OR_END);
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
			String ans = selectOperationOnetoFourInput(console);
			if (ans.equals(ONE))
				goodsCreateUi.createGoods();
			else if (ans.equals(TWO))
				goodsFindUi.findAllGoods();
			else if (ans.equals(THREE))
				goodsFindUi.findGoods();
			else if (ans.equals(FOUR))
				goodsUpdateUi.deleteGoods();
			else if (ans.equals(END))
				break;
		}
	}
}
