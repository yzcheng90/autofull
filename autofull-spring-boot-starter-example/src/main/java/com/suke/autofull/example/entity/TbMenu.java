package com.suke.autofull.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author czx
 * @title: TbMenu
 * @projectName zhjg-common-autofull
 * @description: TODO
 * @date 2022/12/89:51
 */
@Data
@TableName("tb_menu")
public class TbMenu implements Serializable {

    @TableId(type = IdType.AUTO)
    public int menuId;

    public String menuName;
}
