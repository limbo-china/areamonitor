package cn.ac.iie.hy.areamonitor.task;

import cn.ac.iie.hy.areamonitor.cronjob.ScheduledAreaInfoJobHour;
import cn.ac.iie.hy.areamonitor.cronjob.ScheduledAreaNumberJobMinite;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class ScheduledJobStart implements  Runnable{
    @Override
    public void run() {
        PropertyConfigurator.configure("log4j.properties");
        try {
            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler sched = sf.getScheduler();
            JobDetail job;
            job = newJob(ScheduledAreaNumberJobMinite.class).withIdentity("job1", "group1").build();
            CronTrigger trigger = newTrigger().withIdentity("trigger1", "group1").withSchedule(cronSchedule("0 */1 * * * ?"))
                    .build();
            Date ft = sched.scheduleJob(job, trigger);

            job = newJob(ScheduledAreaInfoJobHour.class).withIdentity("job2", "group1").build();
            trigger = newTrigger().withIdentity("trigger2", "group1").withSchedule(cronSchedule("10 10 */1 * * ?"))
                    .build();
            ft = sched.scheduleJob(job, trigger);
            sched.start();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}
