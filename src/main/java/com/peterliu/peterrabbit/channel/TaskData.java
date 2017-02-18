package com.peterliu.peterrabbit.channel;

import java.nio.channels.SelectionKey;

/**
 * Created by bavatinolab on 17/2/1.
 */
public class TaskData {

    /**
     * 默认检测心跳包长度
     */
    public static final int UrgentDataByteLength = 30;

    private SelectionKey selectionKey;

    /**
     * 负载
     */
    private byte[] loadByte;

    public TaskData(SelectionKey selectionKey, byte[] loadByte) {
        this.selectionKey = selectionKey;
        this.loadByte = loadByte;
    }

    public SelectionKey getSelectionKey() {
        return selectionKey;
    }

    public byte[] getLoad() {
        return loadByte;
    }
}
