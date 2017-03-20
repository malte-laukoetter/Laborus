package de.lergin.sponge.laborus.config;

import de.lergin.sponge.laborus.Laborus;
import de.lergin.sponge.laborus.job.Job;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.slf4j.Logger;
import org.slf4j.MarkerFactory;

@ConfigSerializable
public class LoggingConfig {
    private Logger logger = Laborus.instance().getLogger();
    @Setting
    private boolean jobActions = true; //done

    @Setting
    private boolean jobAbilities = true; //done

    @Setting
    private boolean jobBoni = false;

    @Setting
    private boolean jobData = false;

    @Setting
    private boolean jobListener = false;

    @Setting
    private boolean jobGeneral = false;

    public void jobActions(Job job, String format, Object... args) {
        if(this.jobActions){
            logger.info(prefix("JobActions", job.getId()).concat(format), args);
        }
    }

    public void jobAbilities(Job job, String format, Object... args) {
        if(this.jobAbilities){
            logger.info(prefix("JobAbilities", job.getId()).concat(format), args);
        }
    }

    public void jobBoni(Job job, String format, Object... args) {
        if(this.jobBoni){
            logger.info(prefix("JobBoni", job.getId()).concat(format), args);
        }
    }

    public void jobData(String format, Object... args) {
        if(this.jobAbilities){
            logger.info(prefix("JobData").concat(format), args);
        }
    }

    public void jobListener(Job job, String format, Object... args) {
        if(this.jobListener){
            logger.info(prefix("JobListener", job.getId()).concat(format), args);
        }
    }

    public void jobGeneral(Job job, String format, Object... args) {
        if(this.jobListener){
            logger.info(prefix("JobGeneral", job.getId()).concat(format), args);
        }
    }

    private String prefix(String logType, String data){
        return String.format("%s [%s]: ", logType, data);
    }

    private String prefix(String logType){
        return String.format("%s: ", logType);
    }

    public LoggingConfig(){}
}
