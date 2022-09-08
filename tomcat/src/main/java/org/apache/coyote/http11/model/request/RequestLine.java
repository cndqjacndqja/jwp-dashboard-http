package org.apache.coyote.http11.model.request;

import java.util.HashMap;
import java.util.Map;

public class RequestLine {

    private static final String EXIST_QUERY_PARAMS = "?";
    private static final String QUERY_PARAMS_DELIMITER = "&";
    private static final String QUERY_PRAM_VALUE_DELIMITER = "=";
    private static final int QUERY_PRAM_KEY_INDEX = 0;
    private static final int QUERY_PARAM_VALUE_INDEX = 1;
    private static final int METHOD_INDEX = 0;
    private static final int REQUEST_TARGET_INDEX = 1;
    private static final int REQUEST_VERSION_INDEX = 2;

    private final Method method;
    private final String target;
    private final Map<String, String> params;
    private final String version;

    public RequestLine(final Method method, final String target, final Map<String, String> params,
                       final String version) {
        this.method = method;
        this.target = target;
        this.params = params;
        this.version = version;
    }

    public static RequestLine from(final String requestLine) {
        String[] splitRequestLine = requestLine.split(" ");
        Method method = Method.findMethod(splitRequestLine[METHOD_INDEX]);
        String target = createTarget(splitRequestLine[REQUEST_TARGET_INDEX]);
        String version = splitRequestLine[REQUEST_VERSION_INDEX];
        Map<String, String> params = createParams(splitRequestLine[REQUEST_TARGET_INDEX]);
        return new RequestLine(method, target, params, version);
    }

    private static String createTarget(final String input) {
        if (!existQueryParams(input)) {
            return input;
        }
        return input.substring(0, input.lastIndexOf(EXIST_QUERY_PARAMS));
    }

    private static Map<String, String> createParams(final String input) {
        Map<String, String> queryParams = new HashMap<>();
        if (!existQueryParams(input)) {
            return queryParams;
        }
        return splitQueryParams(input, queryParams);
    }

    private static Map<String, String> splitQueryParams(final String path, final Map<String, String> queryParams) {
        String[] queryString = getQueryString(path);
        for (String param : queryString) {
            String[] paramsInfo = param.split(QUERY_PRAM_VALUE_DELIMITER);
            queryParams.put(paramsInfo[QUERY_PRAM_KEY_INDEX], paramsInfo[QUERY_PARAM_VALUE_INDEX]);
        }
        return queryParams;
    }

    private static String[] getQueryString(final String path) {
        String queryString = path.substring(path.lastIndexOf(EXIST_QUERY_PARAMS) + 1);
        return queryString.split(QUERY_PARAMS_DELIMITER);
    }

    private static boolean existQueryParams(final String path) {
        return path.contains(EXIST_QUERY_PARAMS);
    }

    public String getTarget() {
        return target;
    }

    public boolean matchMethod(final Method method) {
        return this.method == method;
    }

    public boolean matchTarget(final String input) {
        return this.target.equals(input);
    }

    public String getVersion() {
        return version;
    }
}