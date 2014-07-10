package com.moksie.onthemove.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe tem como objectivo a criação de uma tarefa em background para fazer pedidos ao
 * servidor sem bloquear a UI.
 * Os pedidos poderão ser GET ou POST, sendo que o GET devolve um JSONArray
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
 */
public class BGTGetJSONArray extends AsyncTask<String, String, JSONArray> {

    List<NameValuePair> postparams = new ArrayList<NameValuePair>();
    String URL = null;
    String method = null;
    static InputStream is = null;
    static JSONArray jArray = null;
    static String json = "";

    public BGTGetJSONArray(String url, String method, List<NameValuePair> params) {
        this.URL = url;
        this.postparams = params;
        this.method = method;
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        // Making HTTP request
        try {
            // Making HTTP request
            // check for request method

            if (method.equals("POST")) {
                // request method is POST
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(URL);
                httpPost.setEntity(new UrlEncodedFormEntity(postparams));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } else if (method == "GET") {
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils
                        .format(postparams, "utf-8");
                URL += "?" + paramString;
                HttpGet httpGet = new HttpGet(URL);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

            // read input stream returned by request into a string using StringBuilder
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();

            // create a JSONObject from the json string
            jArray = new JSONArray(json);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // return JSONObject (this is a class variable and null is returned if something went bad)
        return jArray;

    }
}
