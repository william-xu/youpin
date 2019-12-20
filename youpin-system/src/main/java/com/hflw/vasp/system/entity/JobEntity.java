package com.hflw.vasp.system.entity;

import com.hflw.vasp.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "job_entity")
public class JobEntity extends BaseEntity {

    private String name;          //job名称
    private String group;         //job组名
    private String cron;          //执行的cron
    private String parameter;     //job的参数
    private String description;   //job描述信息
    @Column(name = "vm_param")
    private String vmParam;       //vm参数
    @Column(name = "jar_path")
    private String jarPath;       //job的jar路径,在这里我选择的是定时执行一些可执行的jar包
    private String status;        //job的执行状态,这里我设置为OPEN/CLOSE且只有该值为OPEN才会执行该Job

    public JobEntity() {
    }

    //新增Builder模式,可选,选择设置任意属性初始化对象
    public JobEntity(Builder builder) {
        id = builder.id;
        name = builder.name;
        group = builder.group;
        cron = builder.cron;
        parameter = builder.parameter;
        description = builder.description;
        vmParam = builder.vmParam;
        jarPath = builder.jarPath;
        status = builder.status;
    }

    @Data
    public static class Builder {
        private Long id;
        private String name = "";          //job名称
        private String group = "";         //job组名
        private String cron = "";          //执行的cron
        private String parameter = "";     //job的参数
        private String description = "";   //job描述信息
        private String vmParam = "";       //vm参数
        private String jarPath = "";       //job的jar路径
        private String status = "";        //job的执行状态,只有该值为OPEN才会执行该Job

        public JobEntity newJobEntity() {
            return new JobEntity(this);
        }
    }
}
