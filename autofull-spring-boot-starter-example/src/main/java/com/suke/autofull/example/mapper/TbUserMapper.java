package com.suke.autofull.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suke.autofull.example.entity.TbUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface TbUserMapper extends BaseMapper<TbUser> {

    @Update("update tb_menu set menu_name = #{menuName}")
    void updateMenu(@Param("menuName") String menuName);
}
