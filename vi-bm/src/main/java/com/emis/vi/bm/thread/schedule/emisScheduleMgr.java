/*
 * $Header: /repository/src3/src/com/emis/schedule/emisScheduleMgr.java,v 1.1.1.1 2005/10/14 12:42:33 andy Exp $
 *
 * 2003/10/23 Jerry: ClassLoader先由c:\resin\classes處找, 失敗再到WEB-INF/classes找
 * 2004/01/13 Abel: 將建立emisSchedClassLoader的Loader換成caucho.class-loader
 * 2004/01/14 Jerry: 測試由xxx.jar中載入排程程式: 在loadTask使用caucho.class-loader
 * 2004/01/16 Jerry: 依S_NAME排序
 * Track+[10143] zhong.xu 2008/06/05 間隔排程僅限於隔幾秒執行外，增加可設定起汔時間之功能
 * 2010/07/22 Robert , runTask 可能會 throw Error 類的 Exception,造成 thread dead, schedule 就被影響了
 *                     所以 catch 改成 catch Throwable
 */
package com.emis.vi.bm.thread.schedule;


import com.emis.vi.bm.util.emisUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;


/**
 * 工作排程管理,依據epos.cfg指定之server.bindname
 * 並啟動資料庫中 SCHED Table之 S_NAME 和 server.bindname
 * 相同的工作排程,並啟動指定之Class 為 Task,因為通常
 * 資料庫只會有一部,但 Application Server 有好幾台,
 * 為了讓不同的 Ap Server 能設定跑不同的 emisTask
 * 所以有 server.bindname 和 S_NAME 的設定
 * <p>
 * reload()由資料庫SCHED table讀入排程之設定. S_SERVER為設在c:\resin\xxx.cfg中的Server.BindName.
 * Sched有下列幾個欄位:
 * 1.S_NAME: 排程項目之名稱
 * 2.S_SERVER: 排程系統之名稱, 即Server.BindName, 一般為專案代碼
 * 3.S_CLASS: 執行之類別, 如com.emis.schedule.dailyclose.rt.emisDailyClose
 * 4.RUNLEVEL:
 * I: RUN_BY_INTERVAL 間隔
 * D: RUN_EVERY_DAY   每天
 * M: RUN_EVERY_MONTH 每月
 * Y: RUN_EVERY_YEAR  每年
 * W: RUN_WEEK        每週
 * S: RUN_BY_SPECIFY  指定
 * 5.SMONTH:
 * 6.SDAY:
 * 7.SHOUR:
 * 8.STIME:
 * 9.INTERVAL:
 * 10.PARAM:
 * 11.SYEAR:
 */
public class emisScheduleMgr implements Runnable {
    // abel add for move schedle move in webinf
    public static boolean SCHED_RUN_IN_WEBINF = true;
    public static String SCHED_EMIS_ROOT;
    public static final String SCHED_EMIS_WEBINF = "\\WEB-INF\\classes\\";

    public static final boolean SCHED_DEBUG = true;
    public static final String STR_EMIS_SCHEDULER = "com.emis.schedule.sched";
    //public static final Properties oProp;
    private ServletContext oContext_;
    private Properties oProp;
    private emisScheduleThread oScheduleThread;
    private emisTaskQueue oQueue_ = new emisTaskQueue();
    private ClassLoader current = null;
    private ArrayList threadGroups = new ArrayList();

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public emisScheduleMgr(ServletContext oContext, Properties oProp) throws Exception {
        this(oContext, oProp, null);
    }

    public emisScheduleMgr(ServletContext oContext, Properties oProp, JdbcTemplate jdbcTemplate) throws Exception {
        current = Thread.currentThread().getContextClassLoader();
        this.oContext_ = oContext;
        this.oProp = oProp;
        this.jdbcTemplate = jdbcTemplate;

        //- 2003/10/23 將預設load的路徑設到 WEB-INF/classes
        String _sWebinf = oProp.getProperty("RUN_IN_WEBINF");
        if (_sWebinf == null) _sWebinf = "Y";  //- 預設由WEB-INF/classes找classes
        if (_sWebinf.equalsIgnoreCase("N")) {
            emisScheduleMgr.SCHED_RUN_IN_WEBINF = false;
        } else {
            emisScheduleMgr.SCHED_RUN_IN_WEBINF = true;
            emisScheduleMgr.SCHED_EMIS_ROOT = oProp.getProperty("documentroot");
        }

        // Set context attribute
        if (oContext_.getAttribute(this.STR_EMIS_SCHEDULER) != null) {
            System.out.println("emisScheduleMgr");
        }
        oContext_.setAttribute(this.STR_EMIS_SCHEDULER, this);
        reload(null);
    }

    /**
     * 由資料庫SCHED table讀入排程之設定. 由網頁中人工執行
     * http://.../servlet/com.emis.servlet.emisReloader?target=sched
     */
    public void reload(PrintWriter out) throws Exception {
        // Get server name, ex:epos.cfg->server.bindname

        synchronized (oQueue_) {
            Iterator it = oQueue_.iterator();
            while (it.hasNext()) {
                emisTask t = (emisTask) it.next();
                if (t.getState() == emisTask.EXECUTED) {
                    t.cancelAfterSched();
                } else {
                    t.cancel();
                    it.remove();
                }
            }

            //oLoaderMap_.clear();
            String _sServerName = "bigMonitor";
            _sServerName = _sServerName.toUpperCase();
            try {
                // 20120706 Add by Joe 兼容非MSSQL以外的数据库
                //ResultSet rs = oDb.executeQuery("SELECT DISTINCT IsNull(THREAD_GROUP,'') THREAD_GROUP from SCHED WHERE S_SERVER='"+_sServerName+ "'");
                String sqlStr = "SELECT DISTINCT ifnull(THREAD_GROUP,'') THREAD_GROUP from SCHED WHERE S_SERVER=?";
                List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlStr, _sServerName);
                try {
                    threadGroups.clear();
                    for (Map<String, Object> map : list) {
                        //System.out.println(map);
                        String s = emisUtils.parseString(map.get("THREAD_GROUP"));
                        threadGroups.add((s == null) ? "" : s);
                    }
                } finally {

                }
                list = jdbcTemplate.queryForList("SELECT * FROM SCHED WHERE S_SERVER=? ORDER BY S_NAME", _sServerName);
                if (out != null) out.println("<ol>");
                for (Map<String, Object> map : list) {
                    String _sName = emisUtils.parseString(map.get("S_NAME"));
                    String _sClass = emisUtils.parseString(map.get("S_CLASS"));
                    String _sRunLevel = emisUtils.parseString(map.get("RUNLEVEL"));
                    String _sYear = emisUtils.parseString(map.get("SYEAR"));
                    String _sMonth = emisUtils.parseString(map.get("SMONTH"));
                    String _sDay = emisUtils.parseString(map.get("SDAY"));
                    String _sHour = emisUtils.parseString(map.get("SHOUR"));
                    String _sTime = emisUtils.parseString(map.get("STIME"));
                    String _sInterval = emisUtils.parseString(map.get("INTERVAL1"));
                    String _sParam = emisUtils.parseString(map.get("PARAM"));
                    String _sEndHour = emisUtils.parseString(map.get("SHOUR_END"));
                    String _sEndTime = emisUtils.parseString(map.get("STIME_END"));
                    String _threadGroup = emisUtils.parseString(map.get("THREAD_GROUP"));

                    try {
                        emisTask oTask = loadTask(_sClass);  // 啟動S_CLASS指定之class
                        oTask.setContext(oContext_);
                        oTask.setParam(_sParam);
                        oTask.setName(_sName);
                        oTask.setThreadGroup(_threadGroup);
                        oTask.setSched(_sRunLevel, _sYear, _sMonth, _sDay, _sHour, _sTime, _sInterval, _sEndHour, _sEndTime);
                        oQueue_.add(oTask);
                        if (out != null) {
                            out.println("<li><font color='red'>schedule task: " + _sName + "</font><br>");
                            out.println(" RunLevel=" + _sRunLevel + "<br>" +
                                    " Year=" + _sYear + "<br>" +
                                    " Month=" + _sMonth + "<br>" +
                                    " Day=" + _sDay + "<br>" +
                                    " Hour=" + _sHour + "<br>" +
                                    " Time=" + _sTime + "<br>" +
                                    " Interval=" + _sInterval + "<br></li>");
                        } else {
                            System.out.println("Schedule task: " + _sName + "(" + _sClass +
                                    ") " + _sRunLevel + ":Y=" + _sYear + " M=" + _sMonth + " D=" + _sDay + " H=" + _sHour +
                                    " m=" + _sTime + " Interval=" + _sInterval + " Param=" + _sParam);

                        }
                    } catch (Exception e) {
                        if (out != null)
                            out.print(e.getMessage());
                        else
                            System.out.println(e + " class=" + _sClass);
                    }
                } // end of while
                if (out != null) out.println("</ol>");
            } finally {
            }
        }
        for (int i = 0; i < this.threadGroups.size(); i++) {
            String sGrp = (String) threadGroups.get(i);
            String attrName = STR_EMIS_SCHEDULER + ".THREAD." + sGrp;
            if (oContext_.getAttribute(attrName) == null) {

                emisScheduleThread t = new emisScheduleThread(this, sGrp);
                // 兼容当ThreadGroup没有空值时，以第一个为主Thread
                if (i == 0 || "".equals(sGrp)) {
                    oScheduleThread = t;
                }
                t.setName("Thread Group " + sGrp);
                t.setDaemon(true);
                t.start();  // 啟動emisScheduleMgr->run()->mainloop()
                if (out != null) out.println("active " + t);
                oContext_.setAttribute(attrName, oScheduleThread);
            }
        }

    }

    /**
     * isAlive()
     */
    public boolean isAlive() {
        return oScheduleThread != null && oScheduleThread.isAlive();
    }

    /**
     * getTasks()
     */
    public Iterator getTasks() {
        return oQueue_.iterator();
    }

    /**
     * getInstance()
     */
    public static emisScheduleMgr getInstance(ServletContext application) throws Exception {
        emisScheduleMgr _oMgr = (emisScheduleMgr) application.getAttribute(emisScheduleMgr.STR_EMIS_SCHEDULER);
        if (_oMgr == null) {
            System.out.println("emisScheduleMgr");
        }
        return _oMgr;
    }


    // robert, I use this to verify the scheduler is still running...
    public static int aliveFlag;

    /**
     * run, mainloop
     */
    public void run() {
        try {
            emisTask task = null;
            long currentTime;
            long executionTime;


            emisScheduleThread thr = (emisScheduleThread) Thread.currentThread();
            System.out.println("emisScheduleThread：" + thr);
            String sThreadGroup = thr.getMyThreadGroup();


            while (true) {
                if (SCHED_DEBUG) {
                    System.out.println("Sched Loop");
                }

                Thread.currentThread().sleep(1000); // make sure it is not a busy loop

                aliveFlag = (aliveFlag + 1) % 10;

                boolean taskFired = false;

                synchronized (oQueue_) {

                    if (oQueue_.isEmpty()) {
                        if (SCHED_DEBUG) {
                            System.out.println("  Queue Empty,wait...");
                        }
                        oQueue_.wait();
                    }
                    if (oQueue_.isEmpty()) { // robert, 這 case 似乎應該不會發生,不過年代久遠,先留著
                        if (SCHED_DEBUG)
                            System.out.println("  Queue forever Empty,break.");
                        break;
                    }
                    oQueue_.removeTaskCanceled();

                    task = oQueue_.getMin(sThreadGroup); // robert,2011/11/25 多傳了 thread group 判斷
                    if (task == null) {
                        continue;
                    }
                    if (SCHED_DEBUG) {
                        System.out.println("  Queue get Min Task:" + task);
                        System.out.println("    RunLevel=" + task.getRunLevel() +
                                ",Year=" + task.getYear() + ",Month=" + task.getMonth() +
                                ",Day=" + task.getDay() + ",Hour=" + task.getHour() + ",Minute=" + task.getMinute() +
                                ",Interval=" + task.getInterval());
                    }


                    synchronized (task.lock) {
                        if (task.state == emisTask.CANCELLED) {
                            if (SCHED_DEBUG)
                                System.out.println("  Task Canceled:" + task.sName_);

                            oQueue_.remove(task);
                            continue;  // No action required, poll queue again
                        }

                        currentTime = System.currentTimeMillis();
                        executionTime = task.nextExecutionTime();
                        if (taskFired = (executionTime <= currentTime)) {
                            if (SCHED_DEBUG)
                                System.out.println("  Task prepare Fired:" + task.sName_ +
                                        ",currentTime=" + currentTime + ",execTime=" + executionTime +
                                        ",state=" + task.getStatus());
                        }
                    }

                    if (!taskFired) { // Task hasn't yet fired; wait
                        if (SCHED_DEBUG)
                            System.out.println("  Time not yet, currentTime=" + currentTime + ",execTime=" + executionTime + ",wait:" + task.sName_ + ":" + (executionTime - currentTime));
                        oQueue_.wait(executionTime - currentTime);
                    }

                    if ((task.getRunLevel() == emisTask.RUN_BY_SPECIFY) && taskFired) { // 指定某個時間執行的,執行過後就要馬上移除
                        if (SCHED_DEBUG)
                            System.out.println("  Remove task:" + task.getName());
                        oQueue_.remove(task);
                        //oLoaderMap_.remove(task.getClass().getName());
                    }
                } // end of Queue lock

                // Task fired; run it, holding no locks
                if (taskFired) {  // Task hasn't yet fired; wait
                    if (SCHED_DEBUG) {
                        System.out.println("  Task Fired:" + task.sName_);
                        if (task.state == emisTask.EXECUTED) {
                            System.out.println("  Task Fired,but task is still Running!!!");
                        }
                    }
                    try {
                        // before we run Task
                        // compare file datetime
                        // and reload it if necessary
                        //if (needReload(task)) {

                        //this.freeTask( task );
                        emisTask newTask = null;
                        try {
                            // oQueue_.remove(task);

                            // reload it
                            String sClassName = task.getClass().getName();
                            String[] sSchedParam = task.getSchedParam();
                            String sTaskName = task.getName();
                            String sParam = task.getParam();
                            String threadgrp = task.getThreadGroup();

                            newTask = loadTask(sClassName);
                            newTask.setName(sTaskName);
                            newTask.setParam(sParam);
                            newTask.setSched(sSchedParam);
                            newTask.setThreadGroup(threadgrp);
                            // 防正在加被RELOAD
                            synchronized (oQueue_) {
                                oQueue_.remove(task);
                                oQueue_.add(newTask); // RELOAD 时才有可能标识为CANCEL，RUNNING时才能显示在MONITOR
                            }
                            newTask.run();

                        } finally {
                            // 防TRY之前被Reload时标识为Cancel的要Remove不然会多出一个在队列里
                            if (task.needCancel() || newTask.needCancel()) {
                                if (newTask != null) {
                                    oQueue_.remove(newTask);
                                }
                            }
                        }
                        //}
                        // robert 2010/02/06
                        // 經過討論,因為很多 emisTask 有相依性,所以
                        // 還是保留 schedule 這邊是單一 thread
                        //task.startTaskThread();
                    } catch (Throwable e) { //// Robert , runTask 可能會 throw Error 類的 Exception,造成 thread dead, schedule 就被影響了
                        // task usually has log
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Interrupt:" + e.getMessage());
        }
    }

    private void freeTask(emisTask task) {
        //if( task.m_thread != null && task.m_thread.isAlive()   ) { // the thread is running....
        // well , we have nothing to do with,
        // it is illegal to stop or suspend a thread in java
        //}
        this.oContext_.removeAttribute(task.getContextRegisterName());
        oQueue_.remove(task);
    }

    public boolean needReload(emisTask task) {
        long lastModify;
        String sClassFileName = task.getClass().getName();
        sClassFileName = sClassFileName.replace('.', '/') + ".class";
        // abel add for move schedle move in webinf

        if (SCHED_RUN_IN_WEBINF) {
            lastModify = (new File(SCHED_EMIS_ROOT + SCHED_EMIS_WEBINF + sClassFileName)).lastModified();
        } else {
            URL url = ClassLoader.getSystemResource(sClassFileName);
            lastModify = new File(url.getFile()).lastModified();
        }
        if (task.getLastModified() != lastModify) {
            return true;
        }
        return false;
    }

    /**
     * 載入排程程式. 先由WEB-INF/classes找, 找不到再到/resin/classes找.
     * <p>
     * Robert, 2015/03/30,
     * 發現在遠鑫專案,排程用 business 呼叫 report 的方式,會造成 log4j 重複輸出的怪異現象
     * 改成用系統預設的 loader
     *
     * @param sClassName
     * @return emisTask
     * @throws Exception
     */
    public emisTask loadTask(String sClassName) throws Exception {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Class c = cl.loadClass(sClassName);
            emisTask t = (emisTask) c.newInstance();
            t.setContext(oContext_);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    // 手动执行的Task，用于显示在Monitor上
    private ArrayList oManual_ = new ArrayList();

    public void setManualTask(emisTask task) {
        if (task != null)
            oManual_.add(task);
    }

    public void removeManualTask(emisTask task) {
        if (task != null)
            oManual_.remove(task);
    }

    public Iterator getManualTasks() {
        return oManual_.iterator();
    }

    /**
     * 判断排程是否在执行计划中
     * sName - 对应数据库SCHED.S_NAME
     */
    public boolean isExists(String sName) {
        boolean retValue = false;
        if (sName != null && !"".equals(sName)) {
            for (int i = 0; i < oQueue_.size(); i++) {
                if (sName.equalsIgnoreCase(oQueue_.get(i).getName())) {
                    retValue = true;
                    break;
                }
            }
        }
        return retValue;
    }

    /**
     * 获取排程在执行计划中归属的Thread group
     * sName - 对应数据库SCHED.S_NAME
     */
    public String getThreadGroup(String sName) {
        String threadGroup = null;
        if (sName != null && !"".equals(sName)) {
            for (int i = 0; i < oQueue_.size(); i++) {
                if (sName.equalsIgnoreCase(oQueue_.get(i).getName())) {
                    threadGroup = oQueue_.get(i).getThreadGroup();
                    break;
                }
            }
        }
        return threadGroup == null ? "" : threadGroup;
    }
}
