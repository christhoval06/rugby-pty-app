package app.christhoval.rugbypty.utilities;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;


/**
 * Created by christhoval
 * Date 06/06/16.
 */
public class Request {
    private String mAccessToken;

    private AQuery aQuery;

    /**
     * Instantiate new object.
     */
    public Request(AQuery aq) {
        mAccessToken = "";
        aQuery = aq;
    }

    /**
     * Instantiate new object.
     *
     * @param accessToken Access token.
     */
    public Request(AQuery aq, String accessToken) {
        aQuery = aq;
        mAccessToken = accessToken;
    }

    /**
     * Create http request to an instagram api endpoint.
     * This is a synchronus method, so you have to call it on a separate thread.
     *
     * @param method   Http method, can be GET or POST
     * @param endpoint Api endpoint.
     * @param params   Request parameters
     * @return Api response in json format.
     * @throws Exception If error occured.
     */
    public void createRequest(String method, String endpoint, Map<String, String> params, RequestListener listener) throws Exception {
        switch (method) {
            case "POST":
            case "post":
                requestPost(endpoint, params, listener);
                break;
            default:
                requestGet(endpoint, params, listener);
                break;
        }
    }


    /**
     * Create http GET request to an instagram api endpoint.
     *
     * @param endpoint Api endpoint.
     * @param params   Request parameters
     * @return Api response in json format.
     * @throws Exception If error occured.
     */
    private void requestGet(String endpoint, Map<String, String> params, RequestListener listener) throws Exception {
        String requestUri = Constant.API_BASE_URL + ((endpoint.indexOf("/") == 0) ? endpoint : "/" + endpoint);
        get(requestUri, params, listener);
    }

    /**
     * Create http POST request to an instagram api endpoint.
     *
     * @param endpoint Api endpoint.
     * @param params   Request parameters
     * @return Api response in json format.
     * @throws Exception If error occured.
     */
    private void requestPost(String endpoint, Map<String, String> params, RequestListener listener) throws Exception {
        String requestUri = Constant.API_BASE_URL + ((endpoint.indexOf("/") == 0) ? endpoint : "/" + endpoint);

        post(requestUri, params, listener);
    }

    /**
     * Create http GET request to an instagram api endpoint.
     *
     * @param requestUri Api url
     * @param params     Request parameters
     * @return Api response in json format.
     * @throws Exception If error occured.
     */
    public void get(String requestUri, Map<String, String> params, final RequestListener listener) throws Exception {
        if (!mAccessToken.equals("")) {
            if (params == null) {
                params = new HashMap<>();
            }
            params.put("access_token", mAccessToken);
        }


        if (params != null) {
            StringBuilder requestParamSb = new StringBuilder();

            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                requestParamSb.append(pair.getKey()).append("=").append(URLEncoder.encode(pair.getValue().toString(), "UTF-8")).append(it.hasNext() ? "&" : "");

            }

            String requestParam = requestParamSb.toString();

            requestUri += ((requestUri.contains("?")) ? "&" + requestParam : "?" + requestParam);
        }

        Debug.i("requestUri " + requestUri);


        AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                if (status.getCode() != 200) {
                    Debug.i("Response " + status.getMessage());
                    listener.onError(status.getMessage());
                } else {
                    Debug.i("Response " + json.toString());
                    listener.onSuccess(json);
                }
            }
        };

        //cb.params(params);
        cb.method(AQuery.METHOD_GET);
        cb.header("Content-Type", "application/json; charset=utf-8");

        aQuery.ajax(requestUri, JSONObject.class, cb);
    }

    /**
     * Create http POST request to an instagram api endpoint.
     *
     * @param requestUrl Api url
     * @param params     Request parameters
     * @throws Exception If error occured.
     */
    public String post(String requestUrl, Map<String, String> params, final RequestListener listener) throws Exception {
        if (!mAccessToken.equals("")) {
            if (params == null) {
                params = new HashMap<>();
            }
            params.put("access_token", mAccessToken);
        }

        Debug.i("POST " + requestUrl);

        AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                if (status.getCode() != 200) {
                    Debug.i("Response " + status.getMessage());
                    listener.onError(status.getMessage());
                } else {
                    Debug.i("Response " + json.toString());
                    listener.onSuccess(json);
                }
            }
        };

        cb.header("Content-Type", "application/json; charset=utf-8");

        cb.params(params);

        aQuery.ajax(requestUrl, JSONObject.class, cb);
        return requestUrl;
    }

    public interface RequestListener {
        public abstract void onSuccess(JSONObject response);

        public abstract void onError(String error);
    }
}
