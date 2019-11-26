/**
 * Copyright (C), 2015-2018, 广联赛讯有限公司
 * FileName: WechatTemplate
 * Author:   liuyf
 * Date:     2018/5/15 23:12
 * Description: 模板消息Bean
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hflw.vasp.eshop.common.utils.wechat;

import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈模板消息Bean〉
 *
 * @author liuyf
 * @create 2018/5/15
 * @since 1.0.0
 */
public class WechatTemplate {
    private String touser;

    private String template_id;

    private String url;

    private String topcolor;

    private Map<String, TemplateData> data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopcolor() {
        return topcolor;
    }

    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }

    public Map<String, TemplateData> getData() {
        return data;
    }

    public void setData(Map<String, TemplateData> data) {
        this.data = data;
    }
}