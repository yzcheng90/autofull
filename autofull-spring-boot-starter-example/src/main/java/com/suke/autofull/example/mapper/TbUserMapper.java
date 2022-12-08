package com.suke.autofull.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suke.autofull.example.entity.TbUser;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TbUserMapper extends BaseMapper<TbUser> {

}
