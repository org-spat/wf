package org.spat.wf.mvc.dao;

import java.util.HashMap;

import org.spat.dao.DBConfig;
import org.spat.dao.Database;
import org.spat.wf.mvc.WFConfig;

public class DBManager {
	private static HashMap<String, Database> DB_CACHE = new HashMap<String, Database>();
	public static Database getDatabase(String name) throws Exception{
		if(DB_CACHE.containsKey(name)){
			return DB_CACHE.get(name);
		}
		DBConfig config = WFConfig.Instance().getDatabase(name);
		if(config!=null){
			Database database = Database.get(config);
			if(database!=null){
				DB_CACHE.put(name, database);
			}
			return database;
		}
		return null;
	}
	public static Database getDefault() throws Exception{
		return getDatabase("");
	}
}
