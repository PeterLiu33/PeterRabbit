package com.peterliu.peterrabbit.protocol.http;

import com.peterliu.peterrabbit.datasource.ConfigSource;
import com.peterliu.peterrabbit.datasource.ConfigSourceImpl;
import com.peterliu.peterrabbit.datasource.Constants;
import com.peterliu.peterrabbit.utils.LocalFileUtils;
import com.peterliu.peterrabbit.utils.PropertyOrFileUtils;
import com.peterliu.peterrabbit.utils.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bavatinolab on 17/2/6.
 */
public abstract class ImageMap {

    private static final Logger logger = Logger.getLogger(ImageMap.class.getCanonicalName());

    private static final Map<String, String> fileIcons = new HashMap<String, String>();

    private static String path = null;
    private static final String fileSuffix = ".png";

    private static final ConfigSource configSource = ConfigSourceImpl.instance();

    static {
        fileIcons.put("docx", "docx_mac");
        fileIcons.put("doc", "docx_mac");
        fileIcons.put("pptx", "pptx_mac");
        fileIcons.put("ppt", "pptx_mac");
        fileIcons.put("xlsx", "xlsx_mac");
        fileIcons.put("xls", "xlsx_mac");
        fileIcons.put("htm", "html");
        fileIcons.put("xhtml", "html");
        fileIcons.put("xhtm", "html");
        fileIcons.put("txt", "text");
        fileIcons.put("md", "text");
        fileIcons.put("jpg", "jpeg");
        //读取文件获取地址
        path = configSource.getDefaultIndexPath() + "images" + Constants.PATH_SEPARATOR + "file" + Constants.PATH_SEPARATOR;
        try {
            String[] pngNames = loadInnerOrOuterResourceAsListString(path, configSource);
            if (pngNames != null && pngNames.length > 0) {
                for (String name : pngNames) {
                    name = name.substring(0, name.lastIndexOf("."));
                    fileIcons.put(name, name);
                }
            }
        }catch (Exception e){
            logger.log(Level.WARNING, "fail to loadInner png file list", e);
        }
    }

    public static String getRootImagePath(){
        return path + "root" + fileSuffix;
    }

    public static String getImagePath(File file) {
        String suffix = "default";
        if (file.isDirectory()) {
            suffix = "dir";
            return path + suffix + fileSuffix;
        }
        String fileName = file.getName();
        if (fileName.contains(".")) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (fileIcons.containsKey(suffix)) {
                return path + fileIcons.get(suffix) + fileSuffix;
            }
            return path + "default" + fileSuffix;
        }
        return path + suffix + fileSuffix;
    }

    public static String[] loadInnerOrOuterResourceAsListString(String url, ConfigSource configSource) {
        if (StringUtils.isNotBlank(url)) {
            if (url.startsWith(Constants.INNER_RESOURCES_PATH)) {
                //需要读取内部数据
                String temp = url.substring(Constants.INNER_RESOURCES_PATH.length());

                return PropertyOrFileUtils.getFileDictionary(PropertyOrFileUtils.getClassLoader(), temp, fileSuffix);
            } else {
                //读取外部数据
                File file = LocalFileUtils.checkDictionary(url, configSource);
                if (file != null) {
                    return file.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.endsWith(fileSuffix);
                        }
                    });
                }
            }
        }
        return null;
    }
}
