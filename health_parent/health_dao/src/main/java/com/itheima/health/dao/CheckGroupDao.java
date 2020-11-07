package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {
    /**
     * 添加检查组数据
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 添加检查组与检查项的关系
     * @param checkGroupId
     * @param checkitemId
     */
    void addCheckGroupCheckItem(@Param("checkGroupId") Integer checkGroupId, @Param("checkitemId") Integer checkitemId);

    /**
     * 分页查询检查组
     * @param queryString
     * @return
     */
    Page<CheckGroup> findByCondition(String queryString);

    /**
     * 根据id查询检查组信息
     * @param checkGroupId
     * @return
     */
    CheckGroup findById(int checkGroupId);

    /**
     * 通过检查组id查询选中的检查项id
     * @param checkGroupId
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int checkGroupId);

    /**
     * 更改检查组信息
     * @param checkGroup
     */
    void update(CheckGroup checkGroup);

    /**
     * 根据checkGroup id删除旧关系
     * @param id
     */
    void deleteCheckGroupCheckItem(Integer id);

    /**
     * 检查这个检查组是否被套餐使用了
     * @param id
     * @return
     */
    int findSetmealCountByCheckGroupId(int id);

    /**
     * 删除检查组
     * @param id
     */
    void deleteById(int id);

    /**
     * 查询所有检查组的信息
     * @return
     */
    List<CheckGroup> findAll();
}
