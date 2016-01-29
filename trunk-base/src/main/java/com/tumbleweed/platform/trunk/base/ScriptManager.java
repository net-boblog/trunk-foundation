package com.tumbleweed.platform.trunk.base;

import com.tumbleweed.platform.trunk.base.alarm.AbstractMessage;
import com.tumbleweed.platform.trunk.base.alarm.AbstractMessage.AlarmLevel;
import com.tumbleweed.platform.trunk.base.constants.Constants;
import com.tumbleweed.platform.trunk.base.disp.RouteInfo.RouteModule;
import com.tumbleweed.platform.trunk.base.exception.AbstractException;
import com.tumbleweed.platform.trunk.base.exception.TrunkRuntimeException;
import com.tumbleweed.platform.trunk.base.util.FileAccessor;
import com.tumbleweed.platform.trunk.base.util.FileAccessorListener;
import com.tumbleweed.platform.trunk.base.util.TextUtil;
import com.tumbleweed.platform.trunk.base.util.XMLUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mylover on 1/22/16.
 */
public class ScriptManager  implements ApplicationContextAware, FileAccessorListener {

    public static final Logger log = LogManager.getLogger(ScriptManager.class);

    private static ScriptManager instance;

    /**
     * base path mapping
     */
    private Map<String, String> baseCache = new HashMap<String, String>();

    /**
     * lastModifiedCache mapping
     */
    private HashMap<String, Long> lastModifiedCache = new HashMap<String, Long>();

    /**
     * module instance mapping
     */
    private Map<String, List<Config>> moduleCache = new HashMap<String, List<Config>>();

    /**
     * general config mapping
     */
    private Map<String, String> configCache = new HashMap<String, String>();

    /**
     * error code mapping
     */
    private Map<String, String> codeCache = new HashMap<String, String>();

    /**
     *
     */
    private Map<String, String> codeReflectCache = new HashMap<String, String>();


    /**
     * not check all of request
     */
    private ArrayList<String> NOT_CHECK_ACCOUNT_RIGHT_LIST = new ArrayList<String>();

    /**
     * not check account state request
     */
    private ArrayList<String> NOT_CHECK_ACCOUNT_STATE_LIST = new ArrayList<String>();

    /**
     * spring application context
     */
    private ApplicationContext applicationContext;

    /**
     * inject property
     */
    private String scriptDirPath;

    /**
     * inject property
     */
    private String localDirPath;

    private ScriptManager() {

    }

    public static ScriptManager getScriptManager() {
        if (instance == null) {
            throw new TrunkRuntimeException(buildError(""));
        }
        return instance;
    }

    /**
     * 构建错误消息(该方法默认不告警)
     *
     * @param errorCode
     *            已定义的错误码
     * @return
     */
    public static String buildError(String errorCode) {
        return buildError(errorCode, null);
    }

    /**
     * 构建错误消息和告警消息
     *
     * @param errorCode
     *            已定义的错误码
     * @param custom
     *            告警显示的消息 (该参数为空不作告警)
     * @return
     */
    public static String buildError(String errorCode, String custom) {
        return buildError(errorCode, null, custom);
    }

    /**
     * 构建错误消息和告警消息(用于其他模块返回的错误码和具体描述)
     *
     * @param errorCode
     *            已定义的错误码
     * @param custom
     *            告警显示的消息 (该参数为空不作告警)
     * @return
     */
    public static String buildError(String errorCode, String errorMsg, String custom) {
        return buildError(errorCode, errorMsg, null, custom);
    }

    /**
     * 构建错误消息和告警消息
     *
     * @param errorCode
     *            已定义的错误码
     * @param level
     *            告警级别 {@link AlarmLevel}
     * @param custom
     *            告警显示的消息 (该参数为空不作告警)
     * @return
     */
    public static String buildError(String errorCode, String errorMsg, AbstractMessage.AlarmLevel level, String custom) {
        StringBuffer sb = new StringBuffer(AbstractException.Error_Code + "$" + errorCode);
        sb.append("|" + AbstractException.Error_Msg).append("$");
        if (errorMsg != null) {
            sb.append(errorMsg);
        } else {
            sb.append(instance.codeCache.get(errorCode));
        }
        if (level != null) {
            sb.append("|").append(AbstractException.Alarm_Level).append("$").append(level.name());
        }
        if (custom != null && custom.length() != 0) {
            sb.append("|").append(AbstractException.Custom_Msg).append("$").append(custom);
        }
        return sb.toString();
    }

    /**
     * 通过错误码获取错误值
     *
     * @param code
     *            已定义的错误码
     * @return
     */
    public static String getErrorDesc(String code) {
        return instance.codeCache.get(code);
    }

    /**
     * 配置文件名字必须全局唯一
     *
     * @param context
     */
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        instance = this;
        applicationContext = context;
        try {
            // 初始化脚本路径
            loadScriptFile(getScriptDirPath());
            // 初始化本地配置路径
            loadLocalFile(getLocalDirPath());
            // 初始化错误码
            loadErrorCode(getBaseCache(Constants.ERROR_FILE_NAME));
            // 初始化通用配置
            loadGeneralConfig(getBaseCache(Constants.GENERAL_FILE_NAME));
            // 初始化权限
            loadRight(getBaseCache(Constants.UNCHECKRIGHT_NAME));
            // 初始化错误码映射
            loadCodeReflect(getBaseCache(Constants.ERROR_REFLECT_FILE_NAME));
            log.info("ScriptManager init finished.");
        } catch (Exception e) {
            log.error("setApplicationContext: ", e);
            throw new TrunkRuntimeException(ScriptManager.buildError("111018"));
        }
    }

    /**
     * load script config
     *
     * @param scriptName
     * @param scriptName
     * @return
     */
    public Config getServerConfig(final RouteModule scriptName) {
        log.debug("scriptName: " + scriptName.getName());
        List<Config> configList = moduleCache.get(scriptName.getName());
        if (configList == null || configList.size() == 0) {
            log.warn("[" + scriptName.getName() + "] don't config server.");
            return null;
        }

        int size = configList.size();
        int index = TextUtil.creatRandom(size) % size;
        log.info("Config size: " + size + ", Random Index: " + index);
        Config config = configList.get(index);
        return config;
    }

    /**
     * load local config
     *
     * @param key
     * @return
     */
    public String getLocalConfig(final String key) {
        return configCache.get(key);
    }

    public String getErrorCodeReflect(final String key) {
        return codeReflectCache.get(key);
    }

    public String getContextPath() throws IOException {
        return this.applicationContext.getResource("/").getFile().getAbsolutePath();
    }

    public String getAbsolutePath(String dirName) throws IOException {
        return this.applicationContext.getResource(dirName).getFile().getAbsolutePath();
    }

    public Object getBean(String beanName) {
        return this.applicationContext.getBean(beanName);
    }

    public String getScriptAbsolutePath() throws IOException {
        return getAbsolutePath(getScriptDirPath());
    }

    public String getLocalAbsolutePath() throws IOException {
        return getAbsolutePath(getLocalDirPath());
    }

    /**
     * load local script files
     *
     * @param dirName
     * @throws IOException
     */
    private void loadScriptFile(String dirName) throws IOException {
        FileAccessor.getListFiles(this, getAbsolutePath(dirName), "xml", false);
    }

    /**
     * load local script files
     *
     * @param dirName
     * @throws IOException
     */
    private void loadLocalFile(String dirName) throws IOException {
        FileAccessor.getListFiles(this, getAbsolutePath(dirName), "xml", false);
    }

    @Override
    public void onFileAccessorNotify(String fileName, String filePath, long lastModified) throws Exception {
        log.info(fileName + ":" + filePath + ":" + lastModified);
        baseCache.put(fileName, filePath);
        lastModifiedCache.put(fileName, lastModified);
        if (filePath.indexOf(Constants.SCRIPT_DIR_MATCH) > -1) {
            loadConfigElement(fileName, filePath);
        }
    }

    /**
     * @param fileName
     * @param filePath
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void loadConfigElement(final String fileName, final String filePath) throws IOException {
        XMLUtil xmlUtil = new XMLUtil(new FileInputStream(new File(filePath)));
        List<Element> elements = xmlUtil.getChildElementList();
        if (elements == null || elements.isEmpty()) {
            log.error("config file incorrect [" + filePath + "], please check it.");
            throw new IOException("config file incorrect [" + filePath + "], please check it.");
        }

        List<Config> configList = new ArrayList<Config>();
        for (Element element : elements) {
            String name = element.getName();
            if ("server".equals(name)) {
                Config c = loadConfig(element.elements());
                configList.add(c);
            } else {
                log.info("Not found the server tag: " + name);
            }
        }
        moduleCache.remove(fileName);
        moduleCache.put(fileName, configList);
    }

    /**
     * 加载权限
     * @param appPath
     * @throws IOException
     */
    public void loadRight(String appPath) throws IOException {
        NOT_CHECK_ACCOUNT_RIGHT_LIST.clear();
        NOT_CHECK_ACCOUNT_STATE_LIST.clear();

        XMLUtil xmlUtil = new XMLUtil(new FileInputStream(new File(appPath)));
        List<Element> elements = xmlUtil.getChildElementList();
        for (Element element : elements) {
            String name = element.getName();
            String text = element.getTextTrim();
            log.info("name: " + name + ", text: " + text);
            if ("right".equals(name)) {
                NOT_CHECK_ACCOUNT_RIGHT_LIST.add(text);
            } else if ("state".equals(name)) {
                NOT_CHECK_ACCOUNT_STATE_LIST.add(text);
            } else {
                log.warn("found the undefined right.");
            }
        }
    }

    private Config loadConfig(List<Element> elements) {
        Config c = new Config();
        for (Element e : elements) {
            String name = e.getName();
            String text = e.getTextTrim();
            log.info("name: " + name + ", text: " + text);
            if ("scheme".equals(name)) {
                c.scheme = text;
            } else if ("host".equals(name)) {
                c.host = text;
            } else if ("port".equals(name)) {
                c.port = text;
            } else if ("conntimeout".equals(name)) {
                c.conntimeout = text;
            } else if ("sotimeout".equals(name)) {
                c.sotimeout = text;
            } else if ("action".equals(name)) {
                c.action = text;
            } else if ("serial".equals(name)) {
                c.serial = text;
            }
        }
        return c;
    }

    /**
     * @param appPath
     * @throws IOException
     */
    public void loadErrorCode(String appPath) throws IOException {
        codeCache.clear();

        XMLUtil xmlUtil = new XMLUtil(new FileInputStream(new File(appPath)));
        List<Element> elements = xmlUtil.getChildElementList();
        for (Element sub : elements) {
            String attr = sub.attribute("code").getText();
            String text = filterInnerErrorCode(attr, sub.getTextTrim());
            log.info("attr: " + attr + ", text: " + text);

            codeCache.put(attr, text);
        }
    }

    private String filterInnerErrorCode(String code, String text) {
        int codes = (code == null || code.length() == 0 ? 0 : Integer.parseInt(code));
        if (codes >= 111001 && codes <= 111099) {
            return "内部错误";
        }
        return text;
    }

    /**
     * @param appPath
     * @throws IOException
     */
    public void loadGeneralConfig(String appPath) throws IOException {
        configCache.clear();

        XMLUtil xmlUtil = new XMLUtil(new FileInputStream(new File(appPath)));
        List<Element> elements = xmlUtil.getChildElementList();
        for (Element element : elements) {
            String name = element.getName();
            String text = element.getTextTrim();
            log.info("name: " + name + ", text: " + text);
            if (!configCache.containsKey(name)) {
                configCache.put(name, text);
            }
        }
    }

    public void loadCodeReflect(String appPath) throws IOException {
        codeReflectCache.clear();

        XMLUtil xmlUtil = new XMLUtil(new FileInputStream(new File(appPath)));
        List<Element> elements = xmlUtil.getChildElementList();
        for (Element element : elements) {
            String name = element.getName();
            String text = element.getTextTrim();
            log.info("name: " + name + ", text: " + text);
            if (!codeReflectCache.containsKey(name)) {
                codeReflectCache.put(name, text);
            }
        }
    }


    /**
     * script config key
     */
    public static final String URL = "config_url";
    public static final String IP = "config_ip";
    public static final String ADDR = "config_addr";
    public static final String SCHEME = "config_scheme";
    public static final String HOST = "config_host";
    public static final String PORT = "config_port";
    public static final String CONN_TIMEOUT = "config_conn_timeout";
    public static final String SO_TIMEOUT = "config_so_timeout";
    public static final String LOCAL_HOST_NAME = "localHost";
    public static final String UNCHECKRIGHT = "uncheckright_addr";
    public static final String UNCHECKFEE = "uncheckfee_addr";
    public static final String UNCHECKSTATE = "uncheckstate_addr";
    public static final String SERIAL = "config_serial";

    static class Server {
        String sn;
    }

    public static class Config extends Server {
        String scheme;
        String host;
        String port;
        String action = "";
        String conntimeout;
        String sotimeout;
        String serial = "";

        public Config() {
        }

        public String getConfig(String key) {
            if (URL.equals(key)) {
                return String.format("%s://%s:%s/%s", scheme, host, port, action);
            }

            if (ADDR.equals(key)) {
                return String.format("%s://%s:%s/", scheme, host, port);
            }

            if (IP.equals(key)) {
                return String.format("%s:%s", host, port);
            }

            if (SCHEME.equals(key)) {
                return scheme;
            }

            if (HOST.equals(key)) {
                return host;
            }

            if (PORT.equals(key)) {
                return port;
            }

            if (CONN_TIMEOUT.equals(key)) {
                return conntimeout;
            }

            if (SO_TIMEOUT.equals(key)) {
                return sotimeout;
            }

            if(SERIAL.equals(key)) {
                return serial;
            }

            return null;
        }
    }

    /**
     * 是否不校验
     * @param uri
     * @return
     */
    public boolean isCheckRight(String uri) {
        for (String element : NOT_CHECK_ACCOUNT_RIGHT_LIST) {
            if (uri.indexOf(element) > -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the HashMap
     */
    public HashMap<String, Long> getlastModified() {
        return lastModifiedCache;
    }

    /**
     * @return the String
     */
    public String getBaseCache(String key) {
        return baseCache.get(key);
    }

    /**
     * @return the localDirPath
     */
    public String getLocalDirPath() {
        return localDirPath;
    }

    /**
     * @param localDirPath
     *            the localDirPath to set
     */
    public void setLocalDirPath(String localDirPath) {
        this.localDirPath = localDirPath;
    }

    /**
     * @return the scriptDirPath
     */
    public String getScriptDirPath() {
        return scriptDirPath;
    }

    /**
     * @param scriptDirPath
     *            the scriptDirPath to set
     */
    public void setScriptDirPath(String scriptDirPath) {
        this.scriptDirPath = scriptDirPath;
    }

}
