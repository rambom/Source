package com.ethan.mlife.dao;

import java.util.List;

public interface IBaseDao<T> {
	/**
	 * 查询
	 * 
	 * @param example
	 * @param groupby
	 * @param having
	 * @param orderby
	 * @return
	 */
	List<T> query(T example, String groupby, String having, String orderby);

	/**
	 * 保存
	 * 
	 * @param value
	 * @return
	 */
	long insert(T value);

	/**
	 * 更新
	 * 
	 * @param example
	 * @param value
	 * @return
	 */
	long update(T example, T value);

	/**
	 * 删除
	 * 
	 * @param example
	 * @return
	 */
	long delete(T example);

	/**
	 * 自定义条件查询
	 * 
	 * @param sql
	 * @return
	 */
	List<T> queryWithSql(String sqlWhere);
}
