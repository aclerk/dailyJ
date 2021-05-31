package com.pyjava.daily.config;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>描述: 全局变量 </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/14 11:31
 */
@Singleton
public class GlobalConfig {
    /**
     * 上一个打开的db文件路径
     */
    private String lastOpenDb;
    /**
     * 项目第一次启动时间
     */
    private Date createTime;
    /**
     * 曾经创建的所有db
     */
    private List<String> dbs;

    public GlobalConfig() {
        this.lastOpenDb = "";
        this.createTime = new Date();
        this.dbs = new ArrayList<>();
    }

    public void setGlobalConfig(GlobalConfig globalConfig){
        this.lastOpenDb = globalConfig.getLastOpenDb();
        this.createTime = globalConfig.getCreateTime();
        this.dbs = globalConfig.getDbs();
    }

    public String getLastOpenDb() {
        return lastOpenDb;
    }

    public void setLastOpenDb(String lastOpenDb) {
        this.lastOpenDb = lastOpenDb;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<String> getDbs() {
        return dbs;
    }

    public void setDbs(List<String> dbs) {
        this.dbs = dbs;
    }
}
