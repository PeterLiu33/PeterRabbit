package com.peterliu.peterrabbit.protocol.http;

import com.peterliu.peterrabbit.channel.SelectorRegister;
import com.peterliu.peterrabbit.datasource.ConfigSource;
import com.peterliu.peterrabbit.datasource.Constants;
import com.peterliu.peterrabbit.protocol.Context;
import com.peterliu.peterrabbit.protocol.ProtocolHandler;
import com.peterliu.peterrabbit.protocol.Response;
import com.peterliu.peterrabbit.utils.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bavatinolab on 17/2/6.
 */
public class DefaultHttpWork extends HttpWork {

    private static final Logger logger = Logger.getLogger(DefaultHttpWork.class.getCanonicalName());

    private SelectorRegister selectorRegister;

    public DefaultHttpWork(SelectorRegister selectorRegister) {
        this.selectorRegister = selectorRegister;
    }

    @Override
    public <T extends Response> T doGet(Context context, ConfigSource configSource) {
        HttpRequest request = (HttpRequest) context.getRequest();
        ProtocolHandler handler = context.getHandler();
        String url = request.getUrl();
        //判断是否为内部文件
        if (url.startsWith(Constants.INNER_RESOURCES_PATH)) {
            //需要读取内部数据
            String temp = url.substring(Constants.INNER_RESOURCES_PATH.length());
            return handler.getResponse(context.getTaskData(), request, PropertyOrFileUtils.getFileAsByteBuffer(PropertyOrFileUtils.getClassLoader(), temp));
        }
        //判断是否为文件夹
        File file = LocalFileUtils.checkDictionary(url, configSource);
        if (file != null) {
            //是文件夹
            //设置为HTML格式
            context.setFileType("html");
            String[] filelist = file.list();
            StringBuffer fileLists = new StringBuffer("");
            String rootPath = url + "/";
            rootPath = rootPath.replaceAll("/+", "/");
            try {
                rootPath = URLDecoder.decode(rootPath, "utf-8");
            } catch (UnsupportedEncodingException e) {
                logger.log(Level.WARNING, "fail to decode url filepath", e);
            }
            if (filelist != null && filelist.length > 0) {
                String directionary = file.getAbsolutePath();
                File temp = null;
                for (String fileName : filelist) {
                    temp = new File(directionary + Constants.PATH_SEPARATOR + fileName);
                    if (configSource.isDictionaryFilterOpen()) {
                        //开关文件打开
                        if (temp.isHidden()) {
                            //隐藏文件去除
                            continue;
                        }
                        if (!temp.isDirectory()) {
                            if (temp.isFile() || temp.canExecute()) {
                                String fileType = temp.getName();
                                if (fileType.contains(".")) {
                                    if (!FileUtils.isIllegalFile(fileType.substring(fileType.lastIndexOf(".") + 1), configSource.getLegalSuffix())) {
                                        //不在白名单
                                        continue;
                                    }
                                } else {
                                    //不包含后缀
                                    continue;
                                }
                            }
                        }
                    }

                    fileLists.append("<li><img height=\"20\" width=\"20\" src=\"" + HTMLUtils.escapeDollar(ImageMap.getImagePath(temp)) + "\"/img><a href=\"" + HTMLUtils.escapeDollar(rootPath + fileName) + "\"><font size=\"2px\">" + HTMLUtils.escapeDollar(fileName) + "</font></a></li>");
                    fileLists.append("\r\n");
                }
            }
            String fileAsString = loadInnerOrOuterResourceAsString(configSource.getDefaultIndexPage(), configSource);
            if (StringUtils.isNotBlank(fileAsString)) {
                fileAsString = fileAsString.replaceAll("\\$\\{dictionary\\}", addLink(rootPath));
                fileAsString = fileAsString.replaceAll("\\$\\{back\\}", upLink(rootPath));
                fileAsString = fileAsString.replaceAll("\\$\\{fileList\\}", fileLists.toString());
                return handler.getResponse(context.getTaskData(), request, fileAsString);
            } else {
                return handler.getResponse(context.getTaskData(), request, fileLists.toString());
            }

        }
        //判断为文件
        String filePath = LocalFileUtils.checkAndModifyPath(request.getUrl(), configSource);
        if (StringUtils.isNotBlank(filePath)) {
            if (StringUtils.isNotBlank(context.getFileType())) {
                //合法文件后缀
                if (request.getHeaders().containsKey("Range")) {


                    return handler.getResponse(context.getTaskData(), request, FileConcurrentUtils.getMappedByteBuffer(filePath, -1, -1));
                } else {
                    //判断是否为文本文件
                    return handler.getResponse(context.getTaskData(), request, FileConcurrentUtils.getMappedByteBuffer(filePath, -1, -1));
                }
            } else {
                //非法文件后缀
                return handler.getResponse(context.getTaskData(), request, "unsupported file type!");
            }
        } else {
            String content = null;
            return handler.getResponse(context.getTaskData(), request, content);
        }
    }

    /**
     * 针对每个目录增加连接地址
     *
     * @param rootPath
     * @return
     */
    private String addLink(String rootPath) {
        String[] paths = rootPath.split("/");
        StringBuilder sb = new StringBuilder("<a title=\"PeterRabbit's Home, WebSite:https://github.com/PeterLiu33/PeterRabbit\" href=\"/\"><img  height=\"24\" width=\"24\" style=\"padding:0px\" src=\"" + ImageMap.getRootImagePath() + "\"/></a><a href=\"/\">/</a>");
        String temp = "/";
        for (String path : paths) {
            if (StringUtils.isBlank(path)) {
                continue;
            }
            temp = temp + path + "/";
            sb.append("<a href=\"").append(temp).append("\">").append(path).append("</a>/");
        }
        return sb.toString();
    }

    private String upLink(String rootPath) {
        String[] paths = rootPath.split("/");
        StringBuilder sb = new StringBuilder("<a href=\"/");
        List<String> strings = new ArrayList<String>();
        for (String path : paths) {
            if (StringUtils.isBlank(path)) {
                continue;
            }
            strings.add(path);
        }
        for (int i = 0; i < strings.size() - 1; i++) {
            sb.append(strings.get(i)).append("/");
        }
        String temp = configSource.getDefaultIndexPath() + "images" + Constants.PATH_SEPARATOR + "file" + Constants.PATH_SEPARATOR + "back.png";
        return sb.append("\">").append("<img height=\"35\" width=\"50\" src=\"").append(temp).append("\" /img></a>").toString();
    }

    @Override
    public SelectorRegister getSelectorRegister() {
        return selectorRegister;
    }

    public String loadInnerOrOuterResourceAsString(String url, ConfigSource configSource) {
        if (StringUtils.isNotBlank(url)) {
            if (url.startsWith(Constants.INNER_RESOURCES_PATH)) {
                //需要读取内部数据
                String temp = url.substring(Constants.INNER_RESOURCES_PATH.length());
                return PropertyOrFileUtils.getFileAsString(PropertyOrFileUtils.getClassLoader(), temp);
            } else {
                //读取外部数据
                String filePath = LocalFileUtils.checkAndModifyPath(url, configSource);
                return LocalFileUtils.loadFileToString(filePath);
            }
        }
        return null;
    }
}
