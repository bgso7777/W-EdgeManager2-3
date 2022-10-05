package com.inswave.appplatform.log;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.HeaderGroup;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.URI;
import java.util.*;

public class ProxyServlet extends HttpServlet {
    public static final String P_LOG = "log";
    public static final String P_FORWARDEDFOR = "forwardip";
    public static final String P_PRESERVEHOST = "preserveHost";
    public static final String P_PRESERVECOOKIES = "preserveCookies";
    public static final String P_HANDLEREDIRECTS = "http.protocol.handle-redirects";
    public static final String P_CONNECTTIMEOUT = "http.socket.timeout";
    public static final String P_READTIMEOUT = "http.read.timeout";
    public static final String P_CONNECTIONREQUESTTIMEOUT = "http.connectionrequest.timeout";
    public static final String P_MAXCONNECTIONS = "http.maxConnections";
    public static final String P_USESYSTEMPROPERTIES = "useSystemProperties";
    protected static final String P_TARGET_URI = "targetUri";
    protected static final String ATTR_TARGET_URI = org.mitre.dsmiley.httpproxy.ProxyServlet.class.getSimpleName() + ".targetUri";
    protected static final String ATTR_TARGET_HOST = org.mitre.dsmiley.httpproxy.ProxyServlet.class.getSimpleName() + ".targetHost";
    protected boolean doLog = false;
    protected boolean doForwardIP = true;
    protected boolean doSendUrlFragment = true;
    protected boolean doPreserveHost = false;
    protected boolean doPreserveCookies = false;
    protected boolean doHandleRedirects = false;
    protected boolean useSystemProperties = true;
    protected int connectTimeout = -1;
    protected int readTimeout = -1;
    protected int connectionRequestTimeout = -1;
    protected int maxConnections = -1;
    protected String[] targetUri;
    protected List<URI> targetUriObj=new ArrayList<>();
    protected List<HttpHost> targetHost=new ArrayList<>();
    private HttpClient proxyClient;
    protected static final HeaderGroup hopByHopHeaders = new HeaderGroup();
    protected static final BitSet asciiQueryChars;

    private static final Logger logger = LoggerFactory.getLogger(Class.class);

    public ProxyServlet() {
    }

    public String getServletInfo() {
        return "A proxy servlet by David Smiley, dsmiley@apache.org";
    }

    protected String getTargetUri(HttpServletRequest servletRequest) {
        return (String)servletRequest.getAttribute(ATTR_TARGET_URI);
    }

    protected HttpHost getTargetHost(HttpServletRequest servletRequest) {
        return (HttpHost)servletRequest.getAttribute(ATTR_TARGET_HOST);
    }

    protected String getConfigParam(String key) {
        return this.getServletConfig().getInitParameter(key);
    }

    public void init() throws ServletException {
        String doLogStr = this.getConfigParam("log");
        if (doLogStr != null) {
            this.doLog = Boolean.parseBoolean(doLogStr);
        }

        String doForwardIPString = this.getConfigParam("forwardip");
        if (doForwardIPString != null) {
            this.doForwardIP = Boolean.parseBoolean(doForwardIPString);
        }

        String preserveHostString = this.getConfigParam("preserveHost");
        if (preserveHostString != null) {
            this.doPreserveHost = Boolean.parseBoolean(preserveHostString);
        }

        String preserveCookiesString = this.getConfigParam("preserveCookies");
        if (preserveCookiesString != null) {
            this.doPreserveCookies = Boolean.parseBoolean(preserveCookiesString);
        }

        String handleRedirectsString = this.getConfigParam("http.protocol.handle-redirects");
        if (handleRedirectsString != null) {
            this.doHandleRedirects = Boolean.parseBoolean(handleRedirectsString);
        }

        String connectTimeoutString = this.getConfigParam("http.socket.timeout");
        if (connectTimeoutString != null) {
            this.connectTimeout = Integer.parseInt(connectTimeoutString);
        }

        String readTimeoutString = this.getConfigParam("http.read.timeout");
        if (readTimeoutString != null) {
            this.readTimeout = Integer.parseInt(readTimeoutString);
        }

        String connectionRequestTimeout = this.getConfigParam("http.connectionrequest.timeout");
        if (connectionRequestTimeout != null) {
            this.connectionRequestTimeout = Integer.parseInt(connectionRequestTimeout);
        }

        String maxConnections = this.getConfigParam("http.maxConnections");
        if (maxConnections != null) {
            this.maxConnections = Integer.parseInt(maxConnections);
        }

        String useSystemPropertiesString = this.getConfigParam("useSystemProperties");
        if (useSystemPropertiesString != null) {
            this.useSystemProperties = Boolean.parseBoolean(useSystemPropertiesString);
        }

        this.initTarget();
        this.proxyClient = this.createHttpClient();
    }

    protected RequestConfig buildRequestConfig() {
        return RequestConfig.custom().setRedirectsEnabled(this.doHandleRedirects).setCookieSpec("ignoreCookies").setConnectTimeout(this.connectTimeout).setSocketTimeout(this.readTimeout).setConnectionRequestTimeout(this.connectionRequestTimeout).build();
    }

    protected SocketConfig buildSocketConfig() {
        return this.readTimeout < 1 ? null : SocketConfig.custom().setSoTimeout(this.readTimeout).build();
    }

    protected void initTarget() throws ServletException {
        this.targetUri = this.getConfigParam("targetUri").split(",");
        if (this.targetUri == null) {
            throw new ServletException("targetUri is required.");
        } else {
            try {
                for(int i = 0, iL = this.targetUri.length; i < iL; i++) {
                    this.targetUriObj.add(new URI(this.targetUri[i]));
                }
            } catch (Exception var2) {
                throw new ServletException("Trying to process targetUri init parameter: " + var2, var2);
            }

            for(int i = 0, iL = this.targetUriObj.size(); i < iL; i++) {
                this.targetHost.add(URIUtils.extractHost(this.targetUriObj.get(i)));
            }
        }
    }

    protected HttpClient createHttpClient() {
        HttpClientBuilder clientBuilder = HttpClientBuilder.create().setDefaultRequestConfig(this.buildRequestConfig()).setDefaultSocketConfig(this.buildSocketConfig());
        clientBuilder.setMaxConnTotal(this.maxConnections);
        if (this.useSystemProperties) {
            clientBuilder = clientBuilder.useSystemProperties();
        }

        return clientBuilder.build();
    }

    protected HttpClient getProxyClient() {
        return this.proxyClient;
    }

    public void destroy() {
        if (this.proxyClient instanceof Closeable) {
            try {
                ((Closeable)this.proxyClient).close();
            } catch (IOException var2) {
                this.log("While destroying servlet, shutting down HttpClient: " + var2, var2);
            }
        } else if (this.proxyClient != null) {
            this.proxyClient.getConnectionManager().shutdown();
        }

        super.destroy();
    }

    protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
        int nextUriIdx = new Random().nextInt(this.targetUri.length);
        if (servletRequest.getAttribute(ATTR_TARGET_URI) == null) {
            servletRequest.setAttribute(ATTR_TARGET_URI, this.targetUri[nextUriIdx]);
        }

        if (servletRequest.getAttribute(ATTR_TARGET_HOST) == null) {
            servletRequest.setAttribute(ATTR_TARGET_HOST, this.targetHost.get(nextUriIdx));
        }

        String method = servletRequest.getMethod();
        String proxyRequestUri = this.rewriteUrlFromRequest(servletRequest);
        Object proxyRequest;
        if (servletRequest.getHeader("Content-Length") == null && servletRequest.getHeader("Transfer-Encoding") == null) {
            proxyRequest = new BasicHttpRequest(method, proxyRequestUri);
        } else {
            proxyRequest = this.newProxyRequestWithEntity(method, proxyRequestUri, servletRequest);
        }

        this.copyRequestHeaders(servletRequest, (HttpRequest)proxyRequest);
        this.setXForwardedForHeader(servletRequest, (HttpRequest)proxyRequest);
        HttpResponse proxyResponse = null;

        try {
            proxyResponse = this.doExecute(servletRequest, servletResponse, (HttpRequest)proxyRequest);
            int statusCode = proxyResponse.getStatusLine().getStatusCode();
            servletResponse.setStatus(statusCode, proxyResponse.getStatusLine().getReasonPhrase());
            this.copyResponseHeaders(proxyResponse, servletRequest, servletResponse);
            if (statusCode == 304) {
                servletResponse.setIntHeader("Content-Length", 0);
            } else {
                this.copyResponseEntity(proxyResponse, servletResponse, (HttpRequest)proxyRequest, servletRequest);
            }
        } catch (Exception var11) {
            this.handleRequestException((HttpRequest)proxyRequest, var11);
        } finally {
            if (proxyResponse != null) {
                EntityUtils.consumeQuietly(proxyResponse.getEntity());
            }

        }

    }

    protected void handleRequestException(HttpRequest proxyRequest, Exception e) throws ServletException, IOException {
        if (proxyRequest instanceof AbortableHttpRequest) {
            AbortableHttpRequest abortableHttpRequest = (AbortableHttpRequest)proxyRequest;
            abortableHttpRequest.abort();
        }

        if (e instanceof RuntimeException) {
            throw (RuntimeException)e;
        } else if (e instanceof ServletException) {
            throw (ServletException)e;
        } else if (e instanceof IOException) {
            throw (IOException)e;
        } else {
            throw new RuntimeException(e);
        }
    }

    protected HttpResponse doExecute(HttpServletRequest servletRequest, HttpServletResponse servletResponse, HttpRequest proxyRequest) throws IOException {
        if (this.doLog) {
            this.log("proxy " + servletRequest.getMethod() + " uri: " + servletRequest.getRequestURI() + " -- " + proxyRequest.getRequestLine().getUri());
        }

        return this.proxyClient.execute(this.getTargetHost(servletRequest), proxyRequest);
    }

    protected HttpRequest newProxyRequestWithEntity(String method, String proxyRequestUri, HttpServletRequest servletRequest) throws IOException {
        HttpEntityEnclosingRequest eProxyRequest = new BasicHttpEntityEnclosingRequest(method, proxyRequestUri);
        eProxyRequest.setEntity(new InputStreamEntity(servletRequest.getInputStream(), this.getContentLength(servletRequest)));
        return eProxyRequest;
    }

    private long getContentLength(HttpServletRequest request) {
        String contentLengthHeader = request.getHeader("Content-Length");
        return contentLengthHeader != null ? Long.parseLong(contentLengthHeader) : -1L;
    }

    protected void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException var3) {
            this.log(var3.getMessage(), var3);
        }

    }

    protected void copyRequestHeaders(HttpServletRequest servletRequest, HttpRequest proxyRequest) {
        Enumeration enumerationOfHeaderNames = servletRequest.getHeaderNames();

        while(enumerationOfHeaderNames.hasMoreElements()) {
            String headerName = (String)enumerationOfHeaderNames.nextElement();
            this.copyRequestHeader(servletRequest, proxyRequest, headerName);
        }

    }

    protected void copyRequestHeader(HttpServletRequest servletRequest, HttpRequest proxyRequest, String headerName) {
        if (!headerName.equalsIgnoreCase("Content-Length")) {
            if (!hopByHopHeaders.containsHeader(headerName)) {
                String headerValue;
                for(Enumeration headers = servletRequest.getHeaders(headerName); headers.hasMoreElements(); proxyRequest.addHeader(headerName, headerValue)) {
                    headerValue = (String)headers.nextElement();
                    if (!this.doPreserveHost && headerName.equalsIgnoreCase("Host")) {
                        HttpHost host = this.getTargetHost(servletRequest);
                        headerValue = host.getHostName();
                        if (host.getPort() != -1) {
                            headerValue = headerValue + ":" + host.getPort();
                        }
                    } else if (!this.doPreserveCookies && headerName.equalsIgnoreCase("Cookie")) {
                        headerValue = this.getRealCookie(headerValue);
                    }
                }

            }
        }
    }

    private void setXForwardedForHeader(HttpServletRequest servletRequest, HttpRequest proxyRequest) {
        if (this.doForwardIP) {
            String forHeaderName = "X-Forwarded-For";
            String forHeader = servletRequest.getRemoteAddr();
            String existingForHeader = servletRequest.getHeader(forHeaderName);
            if (existingForHeader != null) {
                forHeader = existingForHeader + ", " + forHeader;
            }

            proxyRequest.setHeader(forHeaderName, forHeader);
            String protoHeaderName = "X-Forwarded-Proto";
            String protoHeader = servletRequest.getScheme();
            proxyRequest.setHeader(protoHeaderName, protoHeader);
        }

    }

    protected void copyResponseHeaders(HttpResponse proxyResponse, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Header[] var4 = proxyResponse.getAllHeaders();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Header header = var4[var6];
            this.copyResponseHeader(servletRequest, servletResponse, header);
        }

    }

    protected void copyResponseHeader(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Header header) {
        String headerName = header.getName();
        if (!hopByHopHeaders.containsHeader(headerName)) {
            String headerValue = header.getValue();
            if (!headerName.equalsIgnoreCase("Set-Cookie") && !headerName.equalsIgnoreCase("Set-Cookie2")) {
                if (headerName.equalsIgnoreCase("Location")) {
                    servletResponse.addHeader(headerName, this.rewriteUrlFromResponse(servletRequest, headerValue));
                } else {
                    servletResponse.addHeader(headerName, headerValue);
                }
            } else {
                this.copyProxyCookie(servletRequest, servletResponse, headerValue);
            }

        }
    }

    protected void copyProxyCookie(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String headerValue) {
        String path = servletRequest.getContextPath();
        path = path + servletRequest.getServletPath();
        if (path.isEmpty()) {
            path = "/";
        }

        Iterator var5 = HttpCookie.parse(headerValue).iterator();

        while(var5.hasNext()) {
            HttpCookie cookie = (HttpCookie)var5.next();
            String proxyCookieName = this.doPreserveCookies ? cookie.getName() : this.getCookieNamePrefix(cookie.getName()) + cookie.getName();
            Cookie servletCookie = new Cookie(proxyCookieName, cookie.getValue());
            servletCookie.setComment(cookie.getComment());
            servletCookie.setMaxAge((int)cookie.getMaxAge());
            servletCookie.setPath(path);
            servletCookie.setSecure(cookie.getSecure());
            servletCookie.setVersion(cookie.getVersion());
            servletCookie.setHttpOnly(cookie.isHttpOnly());
            servletResponse.addCookie(servletCookie);
        }

    }

    protected String getRealCookie(String cookieValue) {
        StringBuilder escapedCookie = new StringBuilder();
        String[] cookies = cookieValue.split("[;,]");
        String[] var4 = cookies;
        int var5 = cookies.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String cookie = var4[var6];
            String[] cookieSplit = cookie.split("=");
            if (cookieSplit.length == 2) {
                String cookieName = cookieSplit[0].trim();
                if (cookieName.startsWith(this.getCookieNamePrefix(cookieName))) {
                    cookieName = cookieName.substring(this.getCookieNamePrefix(cookieName).length());
                    if (escapedCookie.length() > 0) {
                        escapedCookie.append("; ");
                    }

                    escapedCookie.append(cookieName).append("=").append(cookieSplit[1].trim());
                }
            }
        }

        return escapedCookie.toString();
    }

    protected String getCookieNamePrefix(String name) {
        return "!Proxy!" + this.getServletConfig().getServletName();
    }

    protected void copyResponseEntity(HttpResponse proxyResponse, HttpServletResponse servletResponse, HttpRequest proxyRequest, HttpServletRequest servletRequest) throws IOException {
        HttpEntity entity = proxyResponse.getEntity();
        if (entity != null) {
            OutputStream servletOutputStream = servletResponse.getOutputStream();
            entity.writeTo(servletOutputStream);
        }

    }

    protected String rewriteUrlFromRequest(HttpServletRequest servletRequest) {
        StringBuilder uri = new StringBuilder(500);
        uri.append(this.getTargetUri(servletRequest));
        String pathInfo = this.rewritePathInfoFromRequest(servletRequest);
        if (pathInfo != null) {
            uri.append(encodeUriQuery(pathInfo, true));
        }

        String queryString = servletRequest.getQueryString();
        String fragment = null;
        if (queryString != null) {
            int fragIdx = queryString.indexOf(35);
            if (fragIdx >= 0) {
                fragment = queryString.substring(fragIdx + 1);
                queryString = queryString.substring(0, fragIdx);
            }
        }

        queryString = this.rewriteQueryStringFromRequest(servletRequest, queryString);
        if (queryString != null && queryString.length() > 0) {
            uri.append('?');
            uri.append(encodeUriQuery(queryString, false));
        }

        if (this.doSendUrlFragment && fragment != null) {
            uri.append('#');
            uri.append(encodeUriQuery(fragment, false));
        }

        return uri.toString();
    }

    protected String rewriteQueryStringFromRequest(HttpServletRequest servletRequest, String queryString) {
        return queryString;
    }

    protected String rewritePathInfoFromRequest(HttpServletRequest servletRequest) {
        return servletRequest.getPathInfo();
    }

    protected String rewriteUrlFromResponse(HttpServletRequest servletRequest, String theUrl) {
        String targetUri = this.getTargetUri(servletRequest);
        if (theUrl.startsWith(targetUri)) {
            StringBuffer curUrl = servletRequest.getRequestURL();
            int pos;
            if ((pos = curUrl.indexOf("://")) >= 0 && (pos = curUrl.indexOf("/", pos + 3)) >= 0) {
                curUrl.setLength(pos);
            }

            curUrl.append(servletRequest.getContextPath());
            curUrl.append(servletRequest.getServletPath());
            curUrl.append(theUrl, targetUri.length(), theUrl.length());
            return curUrl.toString();
        } else {
            return theUrl;
        }
    }

//    public String getTargetUri() {
//        return this.targetUri;
//    }

    protected static CharSequence encodeUriQuery(CharSequence in, boolean encodePercent) {
        StringBuilder outBuf = null;
        Formatter formatter = null;

        for(int i = 0; i < in.length(); ++i) {
            char c = in.charAt(i);
            boolean escape = true;
            if (c < 128) {
                if (asciiQueryChars.get(c) && (!encodePercent || c != '%')) {
                    escape = false;
                }
            } else if (!Character.isISOControl(c) && !Character.isSpaceChar(c)) {
                escape = false;
            }

            if (!escape) {
                if (outBuf != null) {
                    outBuf.append(c);
                }
            } else {
                if (outBuf == null) {
                    outBuf = new StringBuilder(in.length() + 15);
                    outBuf.append(in, 0, i);
                    formatter = new Formatter(outBuf);
                }

                formatter.format("%%%02X", Integer.valueOf(c));
            }
        }

        return (CharSequence)(outBuf != null ? outBuf : in);
    }

    static {
        String[] headers = new String[]{"Connection", "Keep-Alive", "Proxy-Authenticate", "Proxy-Authorization", "TE", "Trailers", "Transfer-Encoding", "Upgrade"};
        String[] var1 = headers;
        int var2 = headers.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String header = var1[var3];
            hopByHopHeaders.addHeader(new BasicHeader(header, (String)null));
        }

        char[] c_unreserved = "_-!.~'()*".toCharArray();
        char[] c_punct = ",;:$&+=".toCharArray();
        char[] c_reserved = "?/[]@".toCharArray();
        asciiQueryChars = new BitSet(128);

        char c;
        for(c = 'a'; c <= 'z'; ++c) {
            asciiQueryChars.set(c);
        }

        for(c = 'A'; c <= 'Z'; ++c) {
            asciiQueryChars.set(c);
        }

        for(c = '0'; c <= '9'; ++c) {
            asciiQueryChars.set(c);
        }

        char[] var12 = c_unreserved;
        int var11 = c_unreserved.length;

        int var5;
        char cc;
        for(var5 = 0; var5 < var11; ++var5) {
            cc = var12[var5];
            asciiQueryChars.set(cc);
        }

        var12 = c_punct;
        var11 = c_punct.length;

        for(var5 = 0; var5 < var11; ++var5) {
            cc = var12[var5];
            asciiQueryChars.set(cc);
        }

        var12 = c_reserved;
        var11 = c_reserved.length;

        for(var5 = 0; var5 < var11; ++var5) {
            cc = var12[var5];
            asciiQueryChars.set(cc);
        }

        asciiQueryChars.set(37);
    }
}
