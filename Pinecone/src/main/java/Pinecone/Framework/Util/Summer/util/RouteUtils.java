package Pinecone.Framework.Util.Summer.util;

import javax.servlet.http.HttpServletRequest;

public abstract class RouteUtils {
    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "x-forwarded-for",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"
    };


    public static String getRealRemoteAddr( HttpServletRequest request ) {
        for ( String header : RouteUtils.HEADERS_TO_TRY ) {
            String ip = request.getHeader(header);
            if ( ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip) ) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

}
