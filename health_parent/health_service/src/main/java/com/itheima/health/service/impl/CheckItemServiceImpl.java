package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 查询所有检查项
     *
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     * 新增检查项数据
     *
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 分页查询检查项数据
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        // 使用PageHelper调用startPage方法,传入当前页面和页面大小
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //使用模糊查询 拼接 %
        //调用StringUtils中的判空方法对条件进行判断-判断是否有查询条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //有条件,进行拼接查询-设置字符串查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }

        //调用dao进行分页查询数据
        Page<CheckItem> page = checkItemDao.findPage(queryPageBean.getQueryString());

        //将数据封装到pageResult中
        PageResult<CheckItem> pageResult = new PageResult<>(page.getTotal(), page.getResult());

        return pageResult;
    }

    /**
     * 根据id删除检查项数据
     *
     * @param id
     */
    @Override
    public void deleteById(int id)throws HealthException {
        //先查询数据库判断这个检查项是否被检查组使用了
        int count = checkItemDao.findCountByCheckItemId(id);
        //如果被使用了就抛出异常不允许删除
        if (count > 0){
            //抛出自定义的异常
            throw new HealthException("该检查项数据以被使用,不能删除");
        }

        //调用dao根据id删除检查项数据
        checkItemDao.deleteById(id);
    }

    /**
     * 修改检查项数据
     *
     * @param checkItem
     */
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    /**
     * 根据id查询用户信息-编辑回显数据
     *
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }
}
