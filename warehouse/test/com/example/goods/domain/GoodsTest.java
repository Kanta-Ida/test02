package com.example.goods.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class GoodsTest {

	@Test
	public void testgetPrice() {
		Goods goods = new Goods(100, "鉛筆", 150);
		String ans = goods.getPrice(2);
		assertEquals("¥300", ans);
	}
}
