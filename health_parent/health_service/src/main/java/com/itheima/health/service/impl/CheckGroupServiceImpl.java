package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 添加检查组
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //调用dao添加检查组数据
        checkGroupDao.add(checkGroup);

        //获取检查组的id
        Integer checkGroupId = checkGroup.getId();

        //遍历检查项id, 添加检查组与检查项的关系
        if (checkitemIds != null) {
            for (Integer checkitemId : checkitemIds) {
                //添加检查组与检查项的关系
                checkGroupDao.addCheckGroupCheckItem(checkGroupId, checkitemId);
            }
        }

    }

    /**
     * 分页查询检查组信息
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        //使用PageHelper中的startPage方法
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //如果有条件查询,进行判断
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            // 条件不为空, 进行 % 拼接查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }

        //紧接着的查询会被分页
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryPageBean.getQueryString());

        PageResult<CheckGroup> pageResult = new PageResult<>(page.getTotal(), page.getResult());

        return pageResult;
    }

    /**
     * 根据id查询检查组信息
     *
     * @param checkGroupId
     * @return
     */
    @Override
    public CheckGroup findById(int checkGroupId) {
        return checkGroupDao.findById(checkGroupId);
    }

    /**
     * 通过检查组id查询选中的检查项id
     *
     * @param checkGroupId
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int checkGroupId) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(checkGroupId);
    }

    /**
     * 根据id修改检查组数据
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        //更改检查组信息
        checkGroupDao.update(checkGroup);

        //根据checkGroup id删除旧关系
        checkGroupDao.deleteCheckGroupCheckItem(checkGroup.getId());

        //重新建立新的关系
        if (null != checkitemIds) {
            //遍历checkitemIds
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroup.getId(), checkitemId);
            }
        }
    }

    /**
     * 根据id删除检查组信息
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(int id) {
        //检查这个检查组是否被套餐使用了
        int count = checkGroupDao.findSetmealCountByCheckGroupId(id);
        //如果count大于0说明已经被别的套餐使用了,就抛出异常不允许删除
        if (count > 0){
            throw new HealthException("该检查组已被别的套餐使用了,不允许删除");
        }

        //先删除检查组与检查项的关系
        checkGroupDao.deleteCheckGroupCheckItem(id);

        //删除检查组
        checkGroupDao.deleteById(id);
    }

    /**
     * 查询所有检查组的信息
     *
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
