package com.suke.autofull.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.suke.zhjg.common.autofull.annotation.*;
import com.suke.zhjg.common.autofull.config.MaskType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author czx
 * @title: TbUser
 * @projectName zhjg-common-autofull
 * @description: TODO
 * @date 2022/12/89:50
 */
@Data
@TableName("tb_user")
public class TbUser implements Serializable {

    @TableId(type = IdType.AUTO)
    public Long userId;

    @AutoFullMask(type = MaskType.CHINESE_NAME)
    public String userName;

    @AutoFullMask(type = MaskType.PASSWORD)
    public String password;

    @AutoFullMask(type = MaskType.MOBILE_PHONE)
    public String userPhone;

    @AutoFullMask(type = MaskType.EMAIL)
    public String userEmail;

    @AutoFullMask(type = MaskType.BANK_CARD)
    public String userBankNumber;

    @AutoFullMask(type = MaskType.ID_CARD)
    public String userIdcard;

    @TableField(exist = false)
    @AutoFullJoin(value = "我在中国。长沙，我叫{userName}")
    public String address;

    public Date createTime;

    @TableField(exist = false)
    @AutoFullField(table = "tb_menu",conditionField = "userId")
    public String menuName;

    @TableField(exist = false)
    @AutoFullFieldSQL(sql = "select menu_name from tb_menu where user_id = {userId}")
    public String menuNameSql;

    @TableField(exist = false)
    @AutoFullBean(table = "tb_menu",conditionField = "userId")
    public TbMenu menu;

    @TableField(exist = false)
    @AutoFullBeanSQL(sql = "select * from tb_menu where user_id = {userId}")
    public TbMenu menuSql;

    @TableField(exist = false)
    @AutoFullList(table = "tb_menu",conditionField = "userId")
    public List<TbMenu> menuList;

    @TableField(exist = false)
    @AutoFullListSQL(sql = "select * from tb_menu where user_id = {userId}")
    public List<TbMenu> menuListSql;

    @TableField(exist = false)
    @AutoFullListSQL(sql = "select menu_Id as menuIds from tb_menu where user_id = {userId}")
    public List<Integer> menuIds;
}
