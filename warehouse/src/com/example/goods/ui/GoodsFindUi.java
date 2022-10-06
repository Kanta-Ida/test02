package com.example.goods.ui;

import static com.example.common.constant.DataConstant.*;
import static com.example.common.constant.MessageConstant.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.example.common.ui.BaseUi;
import com.example.goods.domain.Goods;
import com.example.goods.exception.NoGoodsException;
import com.example.goods.service.GoodsService;

class GoodsFindUi extends BaseUi {

	private final Scanner console;
	private final GoodsService goodsService;

	GoodsFindUi(Scanner console, GoodsService goodsService) {
		this.console = console;
		this.goodsService = goodsService;
	}

	void findAllGoods() throws ClassNotFoundException, SQLException {
		System.out.println(COMMON_FIND_ALL_START);
		try {
			List<Goods> goodsList = goodsService.findAllGoods();
			for (Goods goods : goodsList) {
				System.out.println(goods);
			}
		} catch (NoGoodsException e) {
			System.out.println(GOODS_NO_DATA);
		}
		System.out.println(COMMON_FIND_END);
	}

	void findGoods() throws ClassNotFoundException, SQLException {
		/*System.out.println(COMMON_FIND_START);
		try {
			int goodscode = goodsCodeInput(console);
			Goods goods = goodsService.findGoods(goodscode);
			System.out.println(goods);
		
		} catch (NoGoodsException e) {
			System.out.println(GOODS_NO_DATA);
		}
		System.out.println(COMMON_FIND_END);*/

		try {
			System.out.println(COMMON_FIND_START);
			try {
				int goodscode = goodsCodeInput(console);
				Goods goods = goodsService.findGoods(goodscode);
				System.out.println(goods);

			} catch (NoGoodsException e) {
				System.out.println(GOODS_NO_DATA);
			}
			System.out.println(COMMON_FIND_END);

			while (true) {
				String ans = nextOrEndInput(console, GOODS_NEXT_OR_END);
				if (ans.equals(NEXT)) {
					System.out.println(COMMON_FIND_START);
					try {
						int goodscode = goodsCodeInput(console);
						Goods goods = goodsService.findGoods(goodscode);
						System.out.println(goods);

					} catch (NoGoodsException e) {
						System.out.println(GOODS_NO_DATA);
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
