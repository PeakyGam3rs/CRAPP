package com.svgs.framework.reader;

//import com.svgs.framework.data.SaveManager;

public class Poller {
    public static Thread poller;
    public static Thread createPoller() {
        Thread thread = new Thread(() -> {
            while(true){
                for(Runnable x: ReaderInterface.gaugesToUse){
                    x.run();
                }
               
                try {
                    Thread.sleep(200);
                    //SaveManager.recordValues();
                } catch (Exception e) {
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        return thread;
    }
}
