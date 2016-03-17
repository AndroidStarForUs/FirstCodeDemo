/*
 *****************************************************************************************
 * @file JsonEntity.java
 *
 * @brief 
 *
 * Code History:
 *       2015年8月19日  下午7:28:31  Peter , initial version
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.json;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @brief json解析、生成
 * 
 * @author Peter
 * @param <E>
 *
 * @date 2015年8月19日 下午7:28:31
 */
public class BaseJsonEntity<E> {

    private static final String TAG = "BaseJsonEntity";
    private Gson gson = null;
    private Class<E> mclassType = null;

    /**
     * @brief 创建json 生成，解析类，并且过滤掉DataSupport中的“baseObjId” 字段
     * @param classType
     */
    public BaseJsonEntity(Class<E> classType) {
        mclassType = classType;
        ExclusionStrategy excludeStrategy = new SetterExclusionStrategy(new String[] { "baseObjId" });
        gson = new GsonBuilder().setExclusionStrategies(excludeStrategy).create();
    }

    /**
     * @brief 生成jsonString
     * @return String
     */
    public String toJsonString(Object info) {
        String json = gson.toJson(info);
        Log.v(TAG, "==>>>>toJsonString:" + json);
        return json;
    }

    /**
     * @brief parse json to entity
     * @param jsonString
     * @return BaseEntity
     */
    public E parseJsonToEntity(String jsonString) {
        Log.v(TAG, "==>>>>parseJsonToEntity:" + jsonString);
        E entity = gson.fromJson(jsonString, mclassType);
        return entity;
    }

    /**
     * @brief parse json to entity
     * @param jsonString
     * @return BaseEntity
     */
    public ResponseBody parseHttpResponse(String jsonString) {
        Log.v(TAG, "==>>>>parseJsonToEntity:" + jsonString);
        JSONObject jsonHeaderObject = null;
        JSONObject jsonObject = null;
        ResponseBody responseBody = new ResponseBody();
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                jsonHeaderObject = new JSONObject(jsonString);
                responseBody.setStatus(jsonHeaderObject.getInt("status"));
                responseBody.setErrorCode(jsonHeaderObject.getString("errorCode"));
                responseBody.setErrorMsg(jsonHeaderObject.getString("errorMsg"));
                responseBody.setBoxVersion(jsonHeaderObject.getString("boxVersion"));
                String contentString = jsonHeaderObject.getString("content");
                if (!TextUtils.isEmpty(contentString)) {
                    jsonObject = jsonHeaderObject.getJSONObject("content");
                }
                responseBody.setContent(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return responseBody;
    }

    /**
     * @brief parse json to entity list
     * @param jsonString
     * @return List<BaseEntity>
     */
    public List<E> parseJsonToEntityList(String jsonString, Type type) {
        Log.v(TAG, "==>>>>parseJsonToEntityList:" + jsonString);
        List<E> entityList = gson.fromJson(jsonString, type);
        return entityList;
    }

}
