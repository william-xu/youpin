package com.hflw.vasp.task.controller;

import com.hflw.vasp.system.entity.JobEntity;
import com.hflw.vasp.task.service.DynamicJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Set;

@Slf4j
@RestController
public class JobController {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private DynamicJobService jobService;

    //初始化启动所有的Job
    @PostConstruct
    public void initialize() {
        try {
            reStartAllJobs();
            log.info("INIT SUCCESS");
        } catch (SchedulerException e) {
            log.info("INIT EXCEPTION : " + e.getMessage());
            e.printStackTrace();
        }
    }

//    //添加一个job
//    @RequestMapping(value="/addJob",method=RequestMethod.POST)
//    public ReturnMsg addjob(@RequestBody AppQuartz appQuartz) throws Exception {
//        appQuartzService.insertAppQuartzSer(appQuartz);
//        result=jobUtil.addJob(appQuartz);
//    }
//
//    //暂停job
//    @RequestMapping(value="/pauseJob",method=RequestMethod.POST)
//    public ReturnMsg pausejob(@RequestBody Integer[]quartzIds) throws Exception {
//        AppQuartz appQuartz=null;
//        if(quartzIds.length>0){
//            for(Integer quartzId:quartzIds) {
//                appQuartz=appQuartzService.selectAppQuartzByIdSer(quartzId).get(0);
//                jobUtil.pauseJob(appQuartz.getJobName(), appQuartz.getJobGroup());
//            }
//            return new ReturnMsg("200","success pauseJob");
//        }else {
//            return new ReturnMsg("404","fail pauseJob");
//        }
//    }
//
//    //恢复job
//    @RequestMapping(value="/resumeJob",method=RequestMethod.POST)
//    public ReturnMsg resumejob(@RequestBody Integer[]quartzIds) throws Exception {
//        AppQuartz appQuartz=null;
//        if(quartzIds.length>0) {
//            for(Integer quartzId:quartzIds) {
//                appQuartz=appQuartzService.selectAppQuartzByIdSer(quartzId).get(0);
//                jobUtil.resumeJob(appQuartz.getJobName(), appQuartz.getJobGroup());
//            }
//            return new ReturnMsg("200","success resumeJob");
//        }else {
//            return new ReturnMsg("404","fail resumeJob");
//        }
//    }
//
//
//    //删除job
//    @RequestMapping(value="/deletJob",method=RequestMethod.POST)
//    public ReturnMsg deletjob(@RequestBody Integer[]quartzIds) throws Exception {
//        AppQuartz appQuartz=null;
//        for(Integer quartzId:quartzIds) {
//            appQuartz=appQuartzService.selectAppQuartzByIdSer(quartzId).get(0);
//            String ret=jobUtil.deleteJob(appQuartz);
//            if("success".equals(ret)) {
//                appQuartzService.deleteAppQuartzByIdSer(quartzId);
//            }
//        }
//        return new ReturnMsg("200","success deleteJob");
//    }
//
//    //修改
//    @RequestMapping(value="/updateJob",method=RequestMethod.POST)
//    public ReturnMsg  modifyJob(@RequestBody AppQuartz appQuartz) throws Exception {
//        String ret= jobUtil.modifyJob(appQuartz);
//        if("success".equals(ret)) {
//            appQuartzService.updateAppQuartzSer(appQuartz);
//            return new ReturnMsg("200","success updateJob",ret);
//        }else {
//            return new ReturnMsg("404","fail updateJob",ret);
//        }
//    }
//
//    //暂停所有
//    @RequestMapping(value="/pauseAll",method=RequestMethod.GET)
//    public ReturnMsg pauseAllJob() throws Exception {
//        jobUtil.pauseAllJob();
//        return new ReturnMsg("200","success pauseAll");
//    }
//
//    //恢复所有
//    @RequestMapping(value="/repauseAll",method=RequestMethod.GET)
//    public ReturnMsg repauseAllJob() throws Exception {
//        jobUtil.resumeAllJob();
//        return new ReturnMsg("200","success repauseAll");
//    }

    //根据ID重启某个Job
    @RequestMapping("/refresh/{id}")
    public String refresh(@PathVariable Long id) throws SchedulerException {
        String result;
        JobEntity entity = jobService.getJobEntityById(id);
        if (entity == null) return "error: id is not exist ";
        synchronized (log) {
            JobKey jobKey = jobService.getJobKey(entity);
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseJob(jobKey);
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
            scheduler.deleteJob(jobKey);
            JobDataMap map = jobService.getJobDataMap(entity);
            JobDetail jobDetail = jobService.geJobDetail(jobKey, entity.getDescription(), map);
            if (entity.getStatus().equals("OPEN")) {
                scheduler.scheduleJob(jobDetail, jobService.getTrigger(entity));
                result = "Refresh Job : " + entity.getName() + "\t jarPath: " + entity.getJarPath() + " success !";
            } else {
                result = "Refresh Job : " + entity.getName() + "\t jarPath: " + entity.getJarPath() + " failed ! , " +
                        "Because the Job status is " + entity.getStatus();
            }
        }
        return result;
    }


    //重启数据库中所有的Job
    @RequestMapping("/refresh/all")
    public String refreshAll() {
        String result;
        try {
            reStartAllJobs();
            result = "SUCCESS";
        } catch (SchedulerException e) {
            result = "EXCEPTION : " + e.getMessage();
        }
        return "refresh all jobs : " + result;
    }

    /**
     * 重新启动所有的job
     */
    private void reStartAllJobs() throws SchedulerException {
        synchronized (log) {                                                         //只允许一个线程进入操作
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            Set<JobKey> set = scheduler.getJobKeys(GroupMatcher.anyGroup());
            scheduler.pauseJobs(GroupMatcher.anyGroup());                               //暂停所有JOB
            for (JobKey jobKey : set) {                                                 //删除从数据库中注册的所有JOB
                scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
                scheduler.deleteJob(jobKey);
            }
            for (JobEntity job : jobService.loadJobs()) {                               //从数据库中注册的所有JOB
                log.info("Job register name : {} , group : {} , cron : {}", job.getName(), job.getGroup(), job.getCron());
                JobDataMap map = jobService.getJobDataMap(job);
                JobKey jobKey = jobService.getJobKey(job);
                JobDetail jobDetail = jobService.geJobDetail(jobKey, job.getDescription(), map);
                if (job.getStatus().equals("OPEN"))
                    scheduler.scheduleJob(jobDetail, jobService.getTrigger(job));
                else
                    log.info("Job jump name : {} , Because {} status is {}", job.getName(), job.getName(), job.getStatus());
            }
        }
    }
}
