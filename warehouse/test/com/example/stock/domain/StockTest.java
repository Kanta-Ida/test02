package com.example.stock.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.example.stock.domain.Stock;

public class StockTest {
	@Test
	public void testgetCode() {
		Stock stock = new Stock(100, 10);
		int ans = stock.getCode();
		assertEquals(100, ans);
	}

	@Test
	public void testgetQuantity() {
		Stock stock = new Stock(100, 10);
		int ans = stock.getQuantity();
		assertEquals(10, ans);
	}

}
