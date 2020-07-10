package com.emis.vi.bm.thread.schedule;

import javax.servlet.ServletContext;

/**
 * 抓取下传档
 */
public class emisBMDownload extends emisEposAbstractSchedule {

    String partImgName = "-BMPARTIMG.ZIP";
    String departImgName = "-BMDEPARTIMG.ZIP";
    String settingImgName = "-BMSETTINGTIMG.ZIP";
    String upgrade = "-BMUPGRADE.ZIP";

    public emisBMDownload() {
        super();
    }

    public emisBMDownload(ServletContext oContext) {
        oContext_ = oContext;
    }

    protected void postAction() throws Exception {
        System.out.println("emisBMDownload");
    }

}