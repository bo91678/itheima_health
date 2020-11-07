package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    /**
     * 查询所有的检查项数据
     * @return
     */
    @RequestMapping("/findAll")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")
    public Result findAll(){
        //调用服务查询检查项数据
        List<CheckItem> checkItems = checkItemService.findAll();
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItems);
    }

    /**
     * 新增检查项数据
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem){
        //调用服务新增检查项数据
        checkItemService.add(checkItem);
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }


    /**
     * 分页查询检查项数据
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        //调用服务分页查询数据
        PageResult<CheckItem> pageResult = checkItemService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, pageResult);
    }

    /**
     * 根据id删除检查项数据
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")
    public Result deleteById(int id){
        //调用服务删除检查项数据
        checkItemService.deleteById(id);

        //返回响应
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /**
     * 根据id查询用户信息-编辑回显数据
     * @param id
     * @return
     */
    @GetMapping("/findById")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")
    public Result findById(int id){
        //调用服务根据id查询用户信息
        CheckItem checkItem = checkItemService.findById(id);
        //返回响应
        return new Result(true, "检查项数据回显成功", checkItem);
    }


    /**
     * 修改检查项数据
     * @param checkItem
     * @return
     */
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_EDIT')")
    public Result update(@RequestBody CheckItem checkItem){
        //调用服务执行更新命令
        checkItemService.update(checkItem);

        //返回响应
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

}
