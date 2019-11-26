/**
 * Copyright (C), 2015-2018, 广联赛讯有限公司
 * FileName: TemplateData
 * Author:   liuyf
 * Date:     2018/5/15 23:13
 * Description: 模板信息的每个属性信息
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hflw.vasp.eshop.common.utils.wechat;

/**
 * 〈一句话功能简述〉<br>
 * 〈模板信息的每个属性信息〉
 *
 * @author liuyf
 * @create 2018/5/15
 * @since 1.0.0
 */
public class TemplateData {

    public TemplateData() {
    }

    public TemplateData(String value, String color) {
        this.value = value;
        this.color = color;
    }

    private String value;

    private String color;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}