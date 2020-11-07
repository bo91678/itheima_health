package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    JedisPool jedisPool;
    /**
     * 从数据查询原密码
     * @return 原密码
     */
    @Override
    public String queryPassword(String userName) {
        return setmealDao.queryPassword(userName);
    }

    /**
     * 修改密码
     * @return 是否更改成功
     */
    @Override
    @Transactional
    public boolean uploadPassword(Map<String, String> map) {
        return setmealDao.uploadPassword(map)>0;
    }


    /**
     * 分页查询套餐数据
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        //调用pageHelper中的startPage方法查询数据库信息
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //判断是否有查询条件,如果有就进行 % 拼接查询
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }

        //调用dao进行条件查询
        Page<Setmeal> page = setmealDao.findPage(queryPageBean.getQueryString());

        //将数据存放到PageResult中
        PageResult<Setmeal> pageResult = new PageResult<>(page.getTotal(), page.getResult());

        return pageResult;
    }

    /**
     * 通过id查询套餐信息
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    /**
     * 通过id查询选中的检查组ids
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    /**
     * 修改套餐信息
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        // 更新套餐信息
        setmealDao.update(setmeal);
        //删除旧关系
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());

        //判断检查组id
        if (null != checkgroupIds){
            //遍历所有检查组的id
            for (Integer checkgroupId : checkgroupIds) {
                //建立新的套餐与检查组信息
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkgroupId);
            }
        }
        updateRedis();
    }

    /**
     * 删除套餐数据
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(int id) throws HealthException {
        //查询数据库,看套餐是否被使用,如果被使用了就不能删除,抛出异常
        int count = setmealDao.findOrderCountBySetmealId(id);
        if (count > 0){
            throw new HealthException("该套餐已被使用,无法删除");
        }

        //删除套餐与检查组的关系
        setmealDao.deleteSetmealCheckGroup(id);
        //删除套餐
        setmealDao.deleteById(id);
        updateRedis();
    }

    /**
     * 添加套餐
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //添加套餐信息
        setmealDao.add(setmeal);
        //获取套餐的id
        Integer setmealId = setmeal.getId();
        //对checkgroupIds进行非空判断
        if (null != checkgroupIds) {
            //遍历检查组ids
            for (Integer checkgroupId : checkgroupIds) {
                //添加套餐与检查组的关系
                setmealDao.addSetmealCheckGroup(setmealId, checkgroupId);
            }
        }
        updateRedis();
    }
    /**
     * 查询数据库中所有的图片
     *
     * @return
     */
    @Override
    public List<String> findImgs() {
        return setmealDao.findImgs();
    }

    /**
     * 查询所有套餐
     *
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        //创建jedis对象
        Jedis jedis = jedisPool.getResource();
        //如果jedis中查询到的是空值,说明里面没有存
        String key = "health_setmealListInRedis";
        String health_setmealListInRedis = jedis.get(key);
        List<Setmeal> setmealList = null;
        if (StringUtils.isEmpty(health_setmealListInRedis)) {
            //调用服务查询所有套餐信息
            setmealList = setmealDao.findAll();
            // 拼接图片全路径
            setmealList.forEach(s -> {
                s.setImg(QiNiuUtils.DOMAIN + s.getImg());
            });
            //转成json字符串,存入Redis中
            jedis.set(key, JSON.toJSONString(setmealList));
        } else {
            //不为空,将json字符串转换为对象
            setmealList =  JSON.parseObject(health_setmealListInRedis, List.class);
        }
        jedis.close();
        return setmealList;
    }
    /**
     * 查询套餐详情
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById(int id) {
        //创建jedis对象
        Jedis jedis = jedisPool.getResource();
        //如果jedis中查询到的是空值,说明里面没有存
        String Detailkey = "health_setmealDetailListInRedis"+id;
        String health_setmealDetailListInRedis = jedis.get(Detailkey);
        Setmeal setmeal =null;
        if (StringUtils.isEmpty(health_setmealDetailListInRedis)){
            //调用服务查询套餐详情
            setmeal = setmealDao.findDetailById(id);
            // 设置图片的完整路径
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
            //转换成json字符串,存入redis中
            jedis.set(Detailkey,JSON.toJSONString(setmeal));
        }else {
            //如果不为空,则转换为pojo对象
            setmeal = JSON.parseObject(health_setmealDetailListInRedis, Setmeal.class);
        }
        return setmeal;
    }

    /**
     * 获取套餐预约数量
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        //调用dao查询套餐预约的数据
        return setmealDao.findSetmealCount();
    }

    /**
     * 数据库写入时,更新redis
     */
    public void updateRedis(){
        //创建jedis对象
        Jedis jedis = jedisPool.getResource();
        String key = "health_setmealListInRedis";
        Setmeal setmeal = null;
        //调用服务查询所有套餐列表
        List<Setmeal> setmealList = setmealDao.findAll();
        //拼接图片路径
        setmealList.forEach(s -> {
            s.setImg(QiNiuUtils.DOMAIN + s.getImg());
        });
        //查询数据库中是否有值
        String health_setmealListInRedis = jedis.get(key);
        if (StringUtils.isEmpty(health_setmealListInRedis)){
            //没有值,存入
            jedis.set(key, JSON.toJSONString(setmealList));
        }else {
            //有值,更新
            jedis.del(key);
            jedis.set(key,JSON.toJSONString(setmealList));
        }
        for (Setmeal s : setmealList) {
            Integer id = s.getId();
            //调用服务查询套餐详情
            setmeal =setmealDao.findDetailById(id);
            //拼接图片路径
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
            String Detailkey = "health_setmealDetailListInRedis"+id;
            //查询数据库中是否有值
            String health_setmealDetailListInRedis = jedis.get(Detailkey);
            if (StringUtils.isEmpty(health_setmealDetailListInRedis)){
                //没有值,存入
                jedis.set(Detailkey,JSON.toJSONString(setmeal));
            }else {
                //有值,更新
                jedis.del(Detailkey);
                jedis.set(Detailkey,JSON.toJSONString(setmeal));
            }
        }
    }
}
