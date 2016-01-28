/**
 * 
 */
package com.tumbleweed.platform.trunk.base.disp;

import org.springframework.http.HttpMethod;

import java.util.HashMap;

/**
 * Route info
 * 
 * @see RouteInfo
 */
public class RouteInfo {

    /**
     * route keyword 区分不同路由
     */
    public static final int ROUTE_CONFIG = 0;

	public static enum RouteModule {
		LocalServer {
			public String getName() {
				return "LocalServer";
			}
		};

		public abstract String getName();
	}

	public static boolean isRouteModule(String name) {
		try {
			RouteModule rm = RouteModule.valueOf(name);
			if (rm  == RouteModule.LocalServer) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * defined module name
	 */
	private RouteModule routeModule;

	/**
	 * maybe appid or cmid
	 */
	private String routeId;

	/**
	 * According to the field transfer PS
	 */
	private int type;

	/**
	 * 0：ivr_callmanager 1: kamailio 2: 文件服务器 3:计费服务器 4:短信网关 5:callmanager
	 */
	private int mode;
	
	/**
	 * 0：不显号，1：显号
	 */
	private int isDisplayNumber = 0;

	/**
	 * request action
	 */
	private String action = "";

	/**
	 * 这种方式当type = 9时无效 true: 每次都去获取 false: 仅获取一次
	 */
	private boolean routeFetchMode = true;

	/**
	 * add header
	 */
	private HashMap<String, String> header;

	/**
	 * 请求方法
	 */
	private HttpMethod httpMethod = HttpMethod.POST;

	/**
	 * 路由地址
	 */
	private RouteTable routeTable;
	
	/**
	 * 是否进入二次路由
	 */
	private boolean twiceRouteFlag = false;
	
	/**
	 * 是否首先請求RMServer
	 */
	private boolean rmServerFlag = true;
	
	public RouteInfo(RouteModule mod) {
		this(mod, null);
	}

	public RouteInfo(RouteModule mod, String action) {
		this(mod, ROUTE_CONFIG, null, action);
	}

	public RouteInfo(RouteModule mod, int type, String routeId, String action) {
		this(mod, type, routeId, action, true);
	}

	public RouteInfo(RouteModule mod, int type, String routeId, String action, boolean fetchMode) {
		this.routeModule = mod;
		this.type = type;
		this.routeId = routeId;
		this.action = action;
		this.routeFetchMode = fetchMode;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[routeModule: ").append(this.routeModule.getName()).append(" | ");
		sb.append("routeId: ").append(this.routeId).append(" | ");
		sb.append("type: ").append(this.type).append(" | ");
		sb.append("action: ").append(this.action).append(" | ");
		sb.append("rmServerFlag: ").append(this.rmServerFlag).append(" | ");
		sb.append("fetchMode: ").append(this.routeFetchMode).append("]");
		return sb.toString();
	}

	/**
	 * @param scheme
	 * @param ip
	 * @param port
	 * @param action
	 */
	public void addRouteTable(String scheme, String ip, String port, String action) {
		this.routeTable = new RouteTable(scheme, ip, port, action);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addHeader(String key, String value) {
		if (this.header == null) {
			this.header = new HashMap<String, String>();
		}
		this.header.put(key, value);
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * @return the routeId
	 */
	public String getRouteId() {
		return routeId;
	}

	/**
	 * @param routeId
	 *            the routeId to set
	 */
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	/**
	 * @return the routeModule
	 */
	public RouteModule getRouteModule() {
		return routeModule;
	}

	/**
	 * @param routeModule
	 *            the routeModule to set
	 */
	public void setRouteModule(RouteModule routeModule) {
		this.routeModule = routeModule;
	}

	/**
	 * @return the header
	 */
	public HashMap<String, String> getHeader() {
		return header;
	}

	/**
	 * @return the routeFetchMode
	 */
	public boolean getFetchMode() {
		return routeFetchMode;
	}

	/**
	 * @param routeFetchMode
	 *            the routeFetchMode to set
	 */
	public void setFetchMode(boolean routeFetchMode) {
		this.routeFetchMode = routeFetchMode;
	}

	/**
	 * @return the routeTable
	 */
	public RouteTable getRouteTable() {
		return routeTable;
	}

	/**
	 * @param routeTable
	 *            the routeTable to set
	 */
	public void setRouteTable(RouteTable routeTable) {
		this.routeTable = routeTable;
	}

	/**
	 * @return the httpMethod
	 */
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	/**
	 * @param httpMethod
	 *            the httpMethod to set
	 */
	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	/**
	 * @return the isDisplayNumber
	 */
	public int getIsDisplayNumber() {
		return isDisplayNumber;
	}

	/**
	 * @param isDisplayNumber the isDisplayNumber to set
	 */
	public void setIsDisplayNumber(int isDisplayNumber) {
		this.isDisplayNumber = isDisplayNumber;
	}

	/**
	 * @return the twiceRouteFlag
	 */
	public boolean isTwiceRouteFlag() {
		return twiceRouteFlag;
	}

	/**
	 * @param twiceRouteFlag the twiceRouteFlag to set
	 */
	public void setTwiceRouteFlag(boolean twiceRouteFlag) {
		this.twiceRouteFlag = twiceRouteFlag;
	}

	/**
	 * @return the rmServerFlag
	 */
	public boolean isRmServerFlag() {
		return rmServerFlag;
	}

	/**
	 * @param rmServerFlag
	 *            the rmServerFlag to set
	 */
	public void setRmServerFlag(boolean rmServerFlag) {
		this.rmServerFlag = rmServerFlag;
	}

}
