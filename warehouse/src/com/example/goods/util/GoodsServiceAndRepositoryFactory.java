package com.example.goods.util;

import com.example.goods.repository.GoodsRepositoryImpl;
import com.example.goods.service.GoodsRepository;
import com.example.goods.service.GoodsService;
import com.example.goods.service.GoodsServiceImpl;

public class GoodsServiceAndRepositoryFactory {
	private static final GoodsRepository goodsRepository = new GoodsRepositoryImpl();
	private static final GoodsService goodsService = new GoodsServiceImpl(goodsRepository);

	public static GoodsService getGoodsService() {
		return goodsService;
	}

	public static GoodsRepository getGoodsRepository() {
		return goodsRepository;
	}
}
