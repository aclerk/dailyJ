package com.pyjava.notefx.file;

import com.pyjava.notefx.thread.NoteFxThreadPool;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.Future;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/2 17:40
 */
public class FileMonitor implements Runnable {

    private static FileMonitor monitor;
    private WatchService watchService;
    private Future<?> future;
    private Runnable listener;

    private FileMonitor() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                WatchKey watchKey = watchService.take();
                List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                for (WatchEvent<?> event : watchEvents) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        System.out.println("创建=" + event.context());
                    } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        System.out.println("删除=" + event.context());
                    } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        System.out.println("修改=" + event.context());
                    } else if (kind == StandardWatchEventKinds.OVERFLOW) {
                        System.out.println("覆盖=" + event.context());
                    }
                    if (listener != null) {
                        listener.run();
                    }
                }
                boolean res = watchKey.reset();
                if (!res) {
                    break;
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }


    public static FileMonitor get() {
        if (monitor == null) {
            monitor = new FileMonitor();
        }
        return monitor;
    }

    public void addWatchFile(File file) {
        try {
            System.out.println(file.toURI());
            Paths.get(file.toURI()).register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE
                    /*StandardWatchEventKinds.ENTRY_MODIFY,*/
                    /*StandardWatchEventKinds.OVERFLOW*/);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setListener(Runnable listener) {
        this.listener = listener;
    }

    public void watch() {
        future = NoteFxThreadPool.getFileMonitorPool().submit(this);
    }

    public void stopWatch() {
        future.cancel(true);
        monitor = null;
    }
}
