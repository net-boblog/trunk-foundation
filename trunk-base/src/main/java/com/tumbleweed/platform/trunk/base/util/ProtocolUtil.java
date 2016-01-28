package com.tumbleweed.platform.trunk.base.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by mylover on 1/25/16.
 */
public class ProtocolUtil {
    public static final int HTTP_OK = 200;
    public static final String CT_XML = "application/xml";
    public static final String CT_JSON = "application/json";
    public static final String CT_STREAM = "application/octet-stream";
    public static final String LOCAL_IP = "127.0.0.1";

    public ProtocolUtil() {
    }

    public static boolean checkUrl(String url) {
        return isHttpUrl(url) || isHttpsUrl(url);
    }

    public static int doCheckContentType(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.indexOf("json") > -1?1:0;
    }

    public static int doCheckAccept(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.indexOf("json") > -1?1:0;
    }

    public static boolean isHttpUrl(String url) {
        return url != null && url.length() > 6 && url.substring(0, 7).equalsIgnoreCase("http://");
    }

    public static boolean isHttpsUrl(String url) {
        return url != null && url.length() > 7 && url.substring(0, 8).equalsIgnoreCase("https://");
    }

    public static String getLocalIP() {
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();

            while(e.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface)e.nextElement();
                Enumeration nii = ni.getInetAddresses();

                while(nii.hasMoreElements()) {
                    InetAddress ip = (InetAddress)nii.nextElement();
                    String host = ip.getHostAddress();
                    if(host.indexOf(":") == -1 && !checkInnerIP(host)) {
                        return host;
                    }
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return "127.0.0.1";
    }

    private static boolean checkInnerIP(String host) {
        return host.startsWith("127") || host.startsWith("10") || host.startsWith("172") || host.startsWith("192");
    }

    public static String[] getURLPart(String uri) {
        byte startIndex = 0;
        String str = uri.substring(startIndex + 1);
        int endIndex = str.indexOf("/") + 1;
        if(endIndex == -1) {
            return null;
        } else {
            uri.substring(startIndex + 1, endIndex);
            str = uri.substring(endIndex + 1);
            int startIndex1 = str.indexOf("/");
            if(startIndex1 == -1) {
                return null;
            } else {
                String s1 = str.substring(0, startIndex1);
                String s2 = str.substring(startIndex1 + 1);
                int pos = s2.indexOf("/");
                if(pos > -1) {
                    s2 = s2.substring(0, pos);
                }

                return new String[]{s1, s2};
            }
        }
    }

    public static int getUTF8EncodingLength(String content) throws UnsupportedEncodingException {
        return getEncodingLength(content, "UTF-8");
    }

    public static byte[] getUTF8EncodingContent(String content) throws UnsupportedEncodingException {
        return content.getBytes("UTF-8");
    }

    public static int getEncodingLength(String content, String encoding) throws UnsupportedEncodingException {
        byte[] contents = content.getBytes(encoding);
        return contents.length;
    }

    public static void buildRespHeader(HttpServletResponse response, String state, String contentType, int contentLength) {
        response.setHeader("Status-Code", "HTTP/1.1 " + state);
        response.setHeader("Date", (new Date()).toString());
        response.setHeader("Content-Type:", contentType);
        response.setHeader("Content-Length", "" + contentLength);
    }

    public static String buildRequestHeader(String uri, String host, String ua, String contentType, int contentLength) {
        StringBuffer sb = (new StringBuffer("POST ")).append(uri).append(" HTTP/1.1\r\n");
        sb.append("Host: ").append(host).append("\r\n");
        sb.append("User-Agent: ").append(ua).append("\r\n");
        sb.append("Content-Type: ").append(contentType).append("\r\n");
        sb.append("Content-Length: " + contentLength).append("\r\n");
        sb.append("\r\n");
        return sb.toString();
    }

    public static void flushResponse(OutputStream outputStream, int status, String body) throws IOException {
        int contentLength = 0;
        byte[] bodyByte = (byte[])null;
        if(body != null) {
            bodyByte = body.getBytes("UTF-8");
            contentLength = bodyByte.length;
        }

        String header = buildResponseHeader(status, "application/xml", contentLength);
        StringBuffer sb = new StringBuffer(header);
        if(bodyByte != null) {
            sb.append(new String(bodyByte, 0, bodyByte.length));
        }

        outputStream.write(sb.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public static void flushResponse(HttpServletResponse response, String contentType, String content) throws IOException {
        buildRespHeader(response, "200 OK", contentType, getUTF8EncodingLength(content));
        ServletOutputStream os = response.getOutputStream();
        os.write(content.getBytes());
        os.flush();
        os.close();
    }

    public static String buildResponseHeader(int status, int contentLength) {
        return buildResponseHeader(status, "application/xml", contentLength);
    }

    public static String buildResponseHeader(int status, String contentType, int contentLength) {
        StringBuffer sb = (new StringBuffer("HTTP/1.1 ")).append(status).append(status == 200?" OK":"").append(" \r\n");
        sb.append("Date: ").append(new Date()).append("\r\n");
        sb.append("Content-Type: ").append(contentType).append("\r\n");
        sb.append("Content-Length: ").append(contentLength).append("\r\n");
        sb.append("\r\n");
        return sb.toString();
    }

    public static String getDispatcherPacket(Map<String, String> header, String body) {
        StringBuffer sb = new StringBuffer("@Dispatcher HTTP Packet - \r\n\r\n");
        if(header == null) {
            sb.append(body);
        } else {
            Iterator iter = header.keySet().iterator();

            while(iter.hasNext()) {
                String name = (String)iter.next();
                String value = (String)header.get(name);
                sb.append(name + ": " + value + "\r\n");
            }

            if(body != null && body.length() > 0) {
                sb.append("\r\n").append(body);
            }
        }

        sb.append("\r\n\r\n");
        return sb.toString();
    }

    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
            return "";
        }
    }

    public static String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
            return "";
        }
    }

    public static String getHttpRequestPacket(HttpServletRequest request, String body) {
        StringBuffer sb = new StringBuffer("@Received HTTP Packet - \r\n\r\n");
        sb.append(request.getMethod() + " ");
        sb.append(request.getRequestURI()).append(request.getQueryString() == null?" ":"?" + request.getQueryString() + " ");
        sb.append(request.getProtocol() + "\r\n");
        Enumeration enumer = request.getHeaderNames();

        while(enumer.hasMoreElements()) {
            String name = (String)enumer.nextElement();
            String value = request.getHeader(name);
            sb.append(name + ": " + value + "\r\n");
        }

        if(body != null && body.length() > 0) {
            sb.append("\r\n").append(body);
        }

        sb.append("\r\n\r\n");
        return sb.toString();
    }

    public static String streamReplace(String xmlStr) {
        if(xmlStr == null) {
            return "";
        } else {
            xmlStr = xmlStr.replace("&", "&amp;");
            xmlStr = xmlStr.replace("null", "");
            return xmlStr.trim();
        }
    }

    public static String xmlReplace(String xmlStr) {
        if(xmlStr == null) {
            return "";
        } else {
            xmlStr = xmlStr.replace("&", "&amp;");
            xmlStr = xmlStr.replace("<", "&lt;");
            xmlStr = xmlStr.replace(">", "&gt;");
            xmlStr = xmlStr.replace("\'", "&apos;");
            xmlStr = xmlStr.replace("\"", "&quot;");
            xmlStr = xmlStr.replace("null", "");
            return xmlStr.trim();
        }
    }

    public static String getResponseHeader(String responseData) {
        int p = responseData.indexOf("\r\n\r\n");
        return p > -1?responseData.substring(0, p):"";
    }

    public static String setHttpProxy(String url, String proxy) {
        return "http://" + proxy + getRequestURI(url);
    }

    public static String getRequestURI(String url) {
        int index = url.indexOf("//");
        String host = url.substring(index + 2);
        index = host.indexOf("/");
        return host.substring(index);
    }

    public static String getHost(String url) {
        int index = url.indexOf("//");
        String host = url.substring(index + 2);
        index = host.indexOf("/");
        if(index > 0) {
            host = host.substring(0, index);
        }

        return host;
    }

    public static int getPort(String url) {
        String host = getHost(url);
        int index = host.indexOf(":");
        if(index <= -1) {
            return -1;
        } else {
            String h = host.substring(index + 1);
            return h != null && h.length() > 0?Integer.parseInt(h):-1;
        }
    }
}
