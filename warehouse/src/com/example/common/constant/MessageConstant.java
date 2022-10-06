package com.example.common.constant;

public class MessageConstant {

	public static final String COMMON_SELECT_OPERATION = "- 作業番号を入力してください [1:登録 2:全件検索 3:検索 4:削除　e:終了]";
	public static final String COMMON_NEXT_OR_END = "--- [n:続ける e:終了]";
	public static final String COMMON_CREATE_START = "-- 登録";
	public static final String COMMON_CREATE_END = "-- 登録完了";
	public static final String COMMON_CREATE_CANCEL = "-- 登録キャンセル";
	public static final String COMMON_DELETE_START = "-- 削除";
	public static final String COMMON_DELETE_END = "-- 削除完了";
	public static final String COMMON_DELETE_CANCEL = "-- 削除キャンセル";
	public static final String COMMON_FIND_START = "-- 検索";
	public static final String COMMON_FIND_ALL_START = "-- 全件検索";
	public static final String COMMON_FIND_END = "-- 検索完了";
	public static final String COMMON_CREATE_YES_OR_NO = "--- 登録しても良いですか　[y:はい n:いいえ]";
	public static final String COMMON_DELETE_YES_OR_NO = "--- 削除しても良いですか　　[y:はい n:いいえ]";
	public static final String COMMON_END = "- 終了";

	public static final String GOODS_NEXT_OR_END = "商品管理 [n:続ける e:終了]";

	public static final String GOODS_NO_DATA = "該当する商品はありません";
	public static final String GOODS_CODE_DUPULICATE = "商品コードが重複しています";
	public static final String GOODS_CODE_DELETE = "商品は削除されています";

	public static final String GOODS_CODE = "--- 商品コード：";
	public static final String GOODS_NAME = "--- 商品名：";
	public static final String GOODS_PRICE = "--- 値段：";
	public static final String GOODS_QUANTITY = "--- 数量：";
	public static final String GOODS_SUBTOTAL = "--- 小計：";
	public static final String GOODS_TOTAL = "--- 合計：";

	public static final String STOCK_NEXT_OR_END = "在庫管理 [n:続ける e:終了]";
	public static final String STOCK_SELECT_OPERATION = "- 作業を選んでください[1:管理業務　2:入出荷 3:入出荷履歴　e:終了]";
	public static final String STOCK_SELECT_IN_OUT_BOUND = "- 作業を選んでください[1:入荷　2:出荷　e:終了]";
	public static final String STOCK_INBOUND_START = "-- 入荷";
	public static final String STOCK_OUTBOUND_START = "-- 出荷";
	public static final String STOCK_INBOUND_YES_OR_NO = "--- 入荷します　[y:はい n:いいえ]";
	public static final String STOCK_INBOUND_END = "-- 入荷完了";
	public static final String STOCK_INBOUND_CANCEL = "-- 入荷キャンセル";
	public static final String STOCK_OUTBOUND_YES_OR_NO = "--- 出荷します　　[y:はい n:いいえ]";
	public static final String STOCK_OUTBOUND_END = "-- 出荷完了";
	public static final String STOCK_OUTBOUND_CANCEL = "-- 出荷キャンセル";

	public static final String STOCK_IN_OUT_BOUND_LOG_START = "-入出荷履歴";
	public static final String STOCK_IN_OUT_BOUND_LOG_END = "-入出荷履歴出力完了";

	public static final String STOCK_OVER = "商品の在庫は既に一杯です。やり直してください";
	public static final String STOCK_UNDER = "商品の在庫がマイナスです";
	public static final String STOCK_NOT_EMPTY = "商品の在庫が残っています";
	public static final String STOCK_NO_DATA = "該当する在庫商品はありません";
	public static final String STOCK_GOODS_CODE_DUPULICATE = "在庫商品の商品コードが重複しています";
	public static final String STOCK_GOODS_CODE_DELETE = "在庫商品は削除されています";
	public static final String STOCK_GOODS_CODE_DELETE2 = "過去に削除された商品である為、再登録できません。";
	public static final String INOUTBOUND_NO_DATA = "入出荷の履歴はありません";

	public static final String STOCK_INBOUND = "入荷";
	public static final String STOCK_OUTBOUND = "出荷";
}
