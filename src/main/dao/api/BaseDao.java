package main.dao.api;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T extends Serializable> {
	T getOne(String name);
	List<T> getList();
	void add(T entity);
	void delete(T entity);
	void deleteByName(String name);
}

