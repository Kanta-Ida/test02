package com.example.goods.ui;

import static com.example.common.constant.MessageConstant.*;

import java.sql.SQLException;
import java.util.Scanner;

import com.example.common.ui.BaseUi;
import com.example.goods.domain.Goods;
import com.example.goods.exception.GoodsDeletedException;
import com.example.goods.exception.NoGoodsException;
import com.example.goods.service.GoodsService;

class GoodsDeleteUi extends BaseUi {
	private final Scanner console;
	private final GoodsService goodsService;

	GoodsDeleteUi(Scanner console, GoodsService goodsService) {
		this.console = console;
		this.goodsService = goodsService;
	}

	void deleteGoods() throws SQLException, ClassNotFoundException {
		System.out.println(COMMON_DELETE_START);
		while (true) {
			int goodsCode = goodsCodeInput(console);

			deleteExecute(goodsCode);

			if (isNextOrEndInput(console)) break;
		}
	}

	private void deleteExecute(int goodsCode) throws SQLException, ClassNotFoundException {
		try {
			Goods goods = goodsService.findGoods(goodsCode);
			System.out.println(goods);

			if (!isYesNoInput(console, COMMON_DELETE_YES_OR_NO)) {
				System.out.println(COMMON_DELETE_CANCEL);
				return;
			}

			goodsService.deleteGoods(goodsCode);
		} catch (NoGoodsException e) {
			System.out.println(GOODS_NO_DATA);
		} catch (GoodsDeletedException e) {
			System.out.println(GOODS_CODE_DELETE);
		}
		System.out.println(COMMON_DELETE_END);
	}
}
