package org.spat.wf.mvc.dao;

import org.spat.dao.converter.Converter;
import org.spat.wf.utils.JsonUtil;

public class JsonConverter extends Converter {

	@Override
	public Object toDB(Class<?> clz,Object value) {
		return JsonUtil.toJson(value);
	}

	@Override
	public Object toEntity(Class<?> clz,Object value) {
		return JsonUtil.fromJson(value.toString(), clz);
	}
}
