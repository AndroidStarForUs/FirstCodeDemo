/*
 *****************************************************************************************
 * @file SetterExclusionStrategy.java
 *
 * @brief 
 *
 * Code History:
 *       2015年8月26日  下午1:48:18  Peter , initial version
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * @brief
 * 
 * @author Peter
 *
 * @date 2015年8月26日 下午1:48:18
 */
public class SetterExclusionStrategy implements ExclusionStrategy {

	private String[] fields;

	public SetterExclusionStrategy(String[] fields) {
		this.fields = fields;

	}

	@Override
	public boolean shouldSkipClass(Class<?> arg0) {
		return false;
	}

	/**
	 * 过滤字段的方法
	 */
	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		if (fields != null) {
			for (String name : fields) {
				if (f.getName().equals(name)) {
					/** true 代表此字段要过滤 */
					return true;
				}
			}
		}
		return false;
	}
}
