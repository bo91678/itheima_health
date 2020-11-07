package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    /**
     * 从数据查询原密码
     * @return 原密码
     */
    String queryPassword(String userName);
    /**
     * 修改密码
     * @return 是否更改成功
     */
    int uploadPassword(Map<String, String> map);



    /**
     * 分页查询套餐数据
     * @param queryString
     * @return
     */
    Page<Setmeal> findPage(String queryString);

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
     * 更新套餐信息
     * @param setmeal
     */
    void update(Setmeal setmeal);

    /**
     * 删除旧关系
     * @param id
     */
    void deleteSetmealCheckGroup(Integer id);

    /**
     * 建立新的套餐与检查组信息
     * @param setmealId
     * @param checkgroupId
     */
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param(("checkgroupId")) Integer checkgroupId);

    /**
     * 查询数据库,看套餐是否被使用
     * @param id
     * @return
     */
    int findOrderCountBySetmealId(int id);

    /**
     * 删除套餐
     * @param id
     */
    void deleteById(int id);

    /**
     * 添加套餐信息
     * @param setmeal
     */
    void add(Setmeal setmeal);

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
