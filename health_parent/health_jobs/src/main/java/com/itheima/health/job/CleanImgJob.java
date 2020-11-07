package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时清理七牛上的垃圾图片
 */

@Component
public class CleanImgJob {

    /**
     * 定阅服务
     */
    @Reference
    private SetmealService setmealService;

    public void cleanImg(){
        //使用七牛云工具类查出7牛上的所有图片
        List<String> imgIn7Niu = QiNiuUtils.listFile();
        //调用套餐服务查询数据库中所有套餐的图片
        List<String> imgInDb = setmealService.findImgs();

        //用7牛云的图片数据-数据库的 imgIn7Niu剩下的就是要删除的
        imgIn7Niu.removeAll(imgInDb);

        //把剩下的图片名转成数组(垃圾图片)
        String[] strings = imgIn7Niu.toArray(new String[]{});
        //调用七牛云工具类删除七牛云上的垃圾图片
        QiNiuUtils.removeFiles(strings);
    }
}
