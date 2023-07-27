package com.suke.autofull.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suke.autofull.example.entity.TbUser;
import com.suke.autofull.example.mapper.TbUserMapper;
import com.suke.autofull.example.service.TbUserService;
import org.springframework.stereotype.Service;


@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements TbUserService {

    @Override
    public void updateMenu(String menuName) {
        baseMapper.updateMenu(menuName);
    }
}
