package kr.where.backend.aspect;

public enum LogFormat {
    IP("[IP : %s] | "),
    URL("[URL : %s] | "),
    METHOD("[METHOD : %s] | "),
    USERID("[USERID : %s] | "),
    EXECUTE_METHOD("[EXECUTE_METHOD : %s] | "),
    MSG("[MSG : %s]");

    private final String format;

    LogFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public static String buildLog(
            final String ip,
            final String requestUrl,
            final String requestMethod,
            final String userId,
            final String method,
            final String responseString
    ) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format(IP.getFormat(), ip));
        sb.append(String.format(URL.getFormat(), requestUrl));
        sb.append(String.format(METHOD.getFormat(), requestMethod));
        sb.append(String.format(USERID.getFormat(), userId));
        sb.append(String.format(EXECUTE_METHOD.getFormat(), method));
        sb.append(String.format(MSG.getFormat(), responseString));

        return sb.toString();
    }
}
