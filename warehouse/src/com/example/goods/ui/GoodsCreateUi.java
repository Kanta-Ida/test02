package com.example.goods.ui;

import static com.example.common.constant.MessageConstant.*;

import java.sql.SQLException;
import java.util.Scanner;

import com.example.common.ui.BaseUi;
import com.example.goods.domain.Goods;
import com.example.goods.exception.GoodsCodeDupulicateException;
import com.example.goods.exception.GoodsDeletedException;
import com.example.goods.service.GoodsService;

class GoodsCreateUi extends BaseUi {

	private final Scanner console;
	private final GoodsService goodsService;

	GoodsCreateUi(Scanner console, GoodsService goodsService) {
		this.console = console;
		this.goodsService = goodsService;
	}

	void createGoods() throws SQLException, ClassNotFoundException {
		System.out.println(COMMON_CREATE_START);
		while (true) {
			int goodsCode = goodsCodeInput(console);
			String goodsName = goodsNameInput(console);
			int price = priceInput(console);
			Goods goods = new Goods(goodsCode, goodsName, price);

			createExecute(goods);

			if (isNextOrEndInput(console)) break;
		}
	}

	private void createExecute(Goods goods) throws SQLException, ClassNotFoundException {
		if (!isYesNoInput(console, COMMON_CREATE_YES_OR_NO)) {
			System.out.println(COMMON_CREATE_CANCEL);
			return;
		}

		try {
			goodsService.createGoods(goods);
		} catch (GoodsCodeDupulicateException e) {
			System.out.println(GOODS_CODE_DUPULICATE);
		} catch (GoodsDeletedException e) {
			System.out.println(GOODS_CODE_DELETE);
		}
		System.out.println(COMMON_CREATE_END);
	}
}
