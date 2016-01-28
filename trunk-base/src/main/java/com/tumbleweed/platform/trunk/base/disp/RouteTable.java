/**
 * 
 */
package com.tumbleweed.platform.trunk.base.disp;


import com.tumbleweed.platform.trunk.base.util.ProtocolUtil;

/**
 * @author chao
 */
public class RouteTable {

	private String scheme = "http";
	private String ip;
	private String port;
	private String action = "";
	private String cmid = "";
	private String routeurl;
	private int connTimeout = 2;
	private int soTimeout = 5;

	// 根据confid路由
	private String confid;
	private String rtmpurl;

	public RouteTable() {

	}

	/**
	 * This constructor function is for more than 2 local config, eg:
	 *
	 * @param url
	 */
	public RouteTable(String url) {
		this.routeurl = url;
	}

	public RouteTable(String ip, String port) {
		this(ip, port, "");
	}

	public RouteTable(String ip, String port, String cmid) {
		this.ip = ip;
		this.port = port;
		this.cmid = cmid;
	}

	/**
	 * This constructor function is for db produres, eg: FileServer
	 * 
	 * @param scheme
	 * @param ip
	 * @param port
	 * @param action
	 */
	public RouteTable(String scheme, String ip, String port, String action) {
		this.scheme = scheme;
		this.ip = ip;
		this.port = port;
		this.action = action;
	}

	public String getRouteAddr() {
		if (routeurl != null && routeurl.length() > 0
				&& ProtocolUtil.checkUrl(routeurl)) {
			return routeurl;
		}

		StringBuffer sb = new StringBuffer(scheme + "://");
		sb.append(ip).append(":").append(port);
		if (action != null && action.length() != 0) {
			sb.append("/").append(action);
		}
		return sb.toString();
	}

	public String getRouteText() {
		if (routeurl != null && routeurl.length() > 0
				&& ProtocolUtil.checkUrl(routeurl)) {
			return "[" + routeurl + "]";
		}
		return String.format("[%s:%s] [%s]", this.ip, this.port, this.cmid);
	}

	/**
	 * @return the rtmpurl
	 */
	public String getRtmpurl() {
		return rtmpurl;
	}

	/**
	 * @param rtmpurl
	 *            the rtmpurl to set
	 */
	public void setRtmpurl(String rtmpurl) {
		this.rtmpurl = rtmpurl;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the confid
	 */
	public String getConfid() {
		return confid;
	}

	/**
	 * @param confid
	 *            the confid to set
	 */
	public void setConfid(String confid) {
		this.confid = confid;
	}

	/**
	 * @return the scheme
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * @param scheme
	 *            the scheme to set
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
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
	 * @return the cmid
	 */
	public String getCmid() {
		return cmid;
	}

	/**
	 * @param cmid
	 *            the cmid to set
	 */
	public void setCmid(String cmid) {
		this.cmid = cmid;
	}

	/**
	 * @return the connTimeout
	 */
	public int getConnTimeout() {
		return connTimeout;
	}

	/**
	 * @param connTimeout
	 *            the connTimeout to set
	 */
	public void setConnTimeout(int connTimeout) {
		this.connTimeout = connTimeout;
	}

	/**
	 * @return the soTimeout
	 */
	public int getSoTimeout() {
		return soTimeout;
	}

	/**
	 * @param soTimeout
	 *            the soTimeout to set
	 */
	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

}
