package com.broadtech.arthur.admin.common.mapper;

import com.broadtech.arthur.admin.common.template.CacheTemplate;

import java.util.List;


public class CacheCrudMapper<V> implements CacheBaseMapper<CacheTemplate,String,String,V> {


    /**
     * @param template
     * @param meta
     * @return
     */
    @Override
    public int deleteAll(CacheTemplate template, String meta) {
        throw new UnsupportedOperationException();
    }

    /**
     * @param template 
     * @param group
     * @param key
     * @param newData
     * @return
     */
    @Override
    public int updateData(CacheTemplate template, String group, String key, V newData) {
        return template.upsert(group,key,newData);
    }

    /**
     * @param template 
     * @param group
     * @param key
     * @return
     */
    @Override
    public int deleteData(CacheTemplate template, String group, String key) {
        return template.del(group, key);
    }

    /**
     * @param template 
     * @param group
     * @param keys
     * @return
     */
    @Override
    public int deleteBacthData(CacheTemplate template, String group, List<String> keys) {
        keys.forEach(key->template.del(group,key));
        return keys.size();
    }

    /**
     * @param template 
     * @param group
     * @param key
     * @param newData
     * @return
     */
    @Override
    public int insertData(CacheTemplate template, String group, String key, Object newData) {

        return template.upsert(group,key,newData);
    }

    /**
     * @param template 
     * @param group
     * @param key
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public V selectData(CacheTemplate template, String group, String key) {
        return (V)template.select(group, key);
    }

}
