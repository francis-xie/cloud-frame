package com.emis.vi.bm.thread.schedule;

/**
 * $Header: /repository/epos_src/com/emis/schedule/epos/emisEposAbstractSchedule.java,v 1.7 2006/06/13 08:42:23 Ben Exp $
 * 排程的supper class.
 *
 * @author mike
 * @version 1.0
 */
abstract public class emisEposAbstractSchedule extends emisTask {

    public void runTask() throws Exception {

        try {
            System.out.println("-----------Running");

            // 实作中继承并override该Method
            postAction();

            System.out.println("-----------End");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

        }
    }

    abstract protected void postAction() throws Exception;
}
