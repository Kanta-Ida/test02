package com.example.stock.domain;

public class Stock {

	private int code;
	private int quantity;

	public Stock(int code, int quantity) {
		this.code = code;
		this.quantity = quantity;
	}

	public int getCode() {
		return code;
	}

	public int getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return "・在庫 　商品コード[" + code + "] 在庫数=[" + quantity + "]";
	}

	/*@Override
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
	}*/

	//private static final long serialVersionUID = 1L;
}
