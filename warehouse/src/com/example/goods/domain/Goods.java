package com.example.goods.domain;

import static com.example.common.constant.DataConstant.*;

import java.io.Serializable;

public class Goods implements Serializable {


	private int code;
	private String name;
	private int price;

	public Goods(int code, String name, int price) {
		this.code = code;
		this.name = name;
		this.price = price;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public String getPrice(int quantity) {
		return CURRENCY + price * quantity;
	}

	@Override
	public String toString() {
		return "・商品 　商品コード[" + code + "] 商品名[" + name + "] 価格[" + CURRENCY + price + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + price;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Goods other = (Goods) obj;
		if (code != other.code) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (price != other.price) return false;
		return true;
	}

	private static final long serialVersionUID = 1L;
}
