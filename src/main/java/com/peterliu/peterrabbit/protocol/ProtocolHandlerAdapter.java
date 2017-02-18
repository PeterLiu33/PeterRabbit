package com.peterliu.peterrabbit.protocol;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by bavatinolab on 17/1/29.
 */
public abstract class ProtocolHandlerAdapter implements ProtocolHandler {

    private static final Logger logger = Logger.getLogger(ProtocolHandlerAdapter.class.getCanonicalName());

    static TreeMap<ProtocolType, ProtocolHandler> protocolTypeProtocolHandlerTreeMap = new TreeMap<ProtocolType, ProtocolHandler>();

    {
        //将自己注册进容器
        protocolTypeProtocolHandlerTreeMap.put(this.getType(), this);
    }

    protected static final Pattern linePattern = Pattern.compile("(.*)\r?\n");


    /**
     * 获取第一行数据,如果没有,则最大获取1024byte
     *
     * @param loadByte
     * @return
     */
    protected String getFirstLine(byte[] loadByte) {
        if (loadByte != null && loadByte.length > 0) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (byte b : loadByte) {
                if ('\r' == b || '\n' == b || byteArrayOutputStream.size() > 1024) {
                    try {
                        return new String(byteArrayOutputStream.toByteArray(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        logger.info("fail to get first line loader");
                        return null;
                    }
                }
                byteArrayOutputStream.write(b);
            }
        }
        return null;
    }

    @Override
    public List<Filter> getFilters() {
        return null;
    }
}
