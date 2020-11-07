package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;


    /**
     * 查询所有检查组的信息
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        //调用服务查询所有检查组数据
        List<CheckGroup> checkGroupList = checkGroupService.findAll();
        //返回响应
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroupList);
    }

    /**
     * 添加检查组数据
     * @param checkitemIds
     * @param checkGroup
     * @return
     */
    @PostMapping("/add")
    public Result add(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup){
        //调用服务添加检查组数据
        checkGroupService.add(checkGroup, checkitemIds);

        //返回响应
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 分页查询检查组信息
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        //调用服务分页查询数据
        PageResult<CheckGroup> pageResult = checkGroupService.findPage(queryPageBean);

        //返回响应
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, pageResult);
    }

    /**
     * 根据id查询检查组信息
     * @param checkGroupId
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int checkGroupId){
        //调用服务查询检查组数据
        CheckGroup checkGroup = checkGroupService.findById(checkGroupId);
        //返回响应
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
    }

    /**
     * 通过检查组id查询选中的检查项id
     * @param checkGroupId
     * @return
     */
    @GetMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(int checkGroupId){
        //调用服务查询数据
        List<Integer> checkItemIds = checkGroupService.findCheckItemIdsByCheckGroupId(checkGroupId);
        //返回响应
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemIds);
    }

    /**
     * 根据id修改检查组数据
     * @param checkitemIds
     * @return
     */
    @PostMapping("/update")
    public Result update(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup){
        //调用服务根据id修改检查组数据
        checkGroupService.update(checkGroup, checkitemIds);
        //返回响应
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     * 根据id删除检查组信息
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        //调用服务根据id删除检查组信息
        checkGroupService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
