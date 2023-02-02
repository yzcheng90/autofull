package com.suke.autofull.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suke.autofull.example.entity.TbUser;
import com.suke.autofull.example.service.TbUserService;
import com.suke.zhjg.common.autofull.annotation.AutoFullData;
import com.suke.zhjg.common.autofull.cache.AutoFullRedisCache;
import com.suke.zhjg.common.autofull.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author czx
 * @title: TbUserController
 * @projectName zhjg-common-autofull
 * @description: TODO
 * @date 2022/12/89:54
 */
@RestController
public class TbUserController {

    @Autowired
    public TbUserService tbUserService;

    @AutoFullData
    @GetMapping(value = "/getTest")
    public R getTest() {
        QueryWrapper<TbUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TbUser::getUserId, 1);
        TbUser entity = tbUserService.getOne(queryWrapper);
        return R.ok().setData(entity);
    }

    @AutoFullData
    @GetMapping(value = "/getTestList")
    public R getTestList() {
        List<TbUser> list = tbUserService.list();
        return R.ok().setData(list);
    }

    @AutoFullData
    @GetMapping(value = "/getTestPage")
    public R getTestPage() {
        IPage<TbUser> page = new Page<>(1, 10);
        QueryWrapper<TbUser> queryWrapper = new QueryWrapper<>();
        IPage<TbUser> pageList = tbUserService.page(page, queryWrapper);
        return R.ok().setData(pageList);
    }

    @AutoFullData
    @GetMapping(value = "/getTestPageObject")
    public Object getTestPageObject() {
        IPage<TbUser> page = new Page<>(1, 10);
        QueryWrapper<TbUser> queryWrapper = new QueryWrapper<>();
        IPage<TbUser> pageList = tbUserService.page(page, queryWrapper);
        return pageList;
    }

    @GetMapping(value = "/deleteCacheAll")
    public void deleteCacheAll(){
        AutoFullRedisCache.deleteAll();
    }

}
