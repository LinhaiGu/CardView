package com.glh.cardview.util;

import java.util.List;


/**
 * List工具类
 * Created by glh on 2016/7/21.
 *
 */
public class ListUtil {

	private ListUtil() {
		throw new AssertionError();
	}

	/**
	 * 数据源长度
	 * 
	 * @param sourceList
	 *          数据源
	 * @return
	 */
	public static <V> int getSize(List<V> sourceList) {
		return sourceList == null ? 0 : sourceList.size();
	}

	/**
	 * 数据源是否为空
	 * 
	 * @param sourceList
	 * @return
	 */
	public static <V> boolean isEmpty(List<V> sourceList) {
		return (sourceList == null || sourceList.size() == 0);
	}
}
