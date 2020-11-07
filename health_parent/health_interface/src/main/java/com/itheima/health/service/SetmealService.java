package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    /**
     * 分页查询套餐数据
     * @param queryPageBean
     * @return
     */
    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);


    /**
     * 从数据查询原密码
     * @return 原密码
     */
    String queryPassword(String userName);

    /**
     *修改密码
     */
    boolean uploadPassword(Map<String, String> map);

    /**
     * 通过id查询套餐信息
     * @param id
     * @return
     */
    Setmeal findById(int id);

    /**
     * 通过id查询选中的检查组ids
     * @param id
     * @return
     */
    List<Integer> findCheckgroupIdsBySetmealId(int id);

    /**
     * 修改套餐信息
     * @param setmeal
     * @param checkgroupIds
     */
    void update(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 删除套餐数据
     * @param id
     */
    void deleteById(int id) throws HealthException;

    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 查询数据库中所有的图片
     * @return
     */
    List<String> findImgs();

    /**
     * 查询所有套餐
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    Setmeal findDetailById(int id);

    /**
     * 获取套餐预约数量
     * @return
     */
    List<Map<String, Object>> findSetmealCount();
}
