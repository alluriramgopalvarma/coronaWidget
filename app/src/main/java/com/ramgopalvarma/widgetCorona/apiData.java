package com.ramgopalvarma.widgetCorona;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class apiData {


    String myUrl = "https://api.covid19india.org/state_district_wise.json";
    String myURLRECOVERED="https://api.covid19india.org/states_daily.json";
    //String to place our result in
    String result,result2;
    //Instantiate new instance of our class
    HttpGetRequest getRequest = new HttpGetRequest();
    String casesActiveHyd,casesRecoveredHyd,casesDeceasedHyd,newCasesHyd,newRecoveredHyd;
    //Perform the doInBackground method, passing in our url


    public String getResultAll() {
        try {
            result = getRequest.execute(myUrl).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.i("async Error", "called");
            e.printStackTrace();
        }
        Log.i("Result", result);
        try {

            JSONObject obj = new JSONObject(result);
            String data=obj.optString("Telangana");
            JSONObject dataState = new JSONObject(data);
            String dataDistrict=dataState.optString("districtData");
            JSONObject dataHyderabad = new JSONObject(dataDistrict);
            String dataCasesInHyd=dataHyderabad.optString("Hyderabad");
            JSONObject casesDataHyd = new JSONObject(dataCasesInHyd);
            String deltaCases=casesDataHyd.optString("delta");
            JSONObject deltaCasesHyd = new JSONObject(deltaCases);
            newCasesHyd=deltaCasesHyd.optString("confirmed");
            newRecoveredHyd=deltaCasesHyd.optString("recovered");
             casesActiveHyd=casesDataHyd.optString("active");
            casesRecoveredHyd=casesDataHyd.optString("recovered");
            casesDeceasedHyd=casesDataHyd.optString("deceased");



            Log.i("arrayData", casesActiveHyd+"-----"+casesRecoveredHyd+"------"+casesDeceasedHyd);


        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + "result" + "\"");
        }

        return casesActiveHyd+"/"+casesRecoveredHyd+"/"+casesDeceasedHyd+"/"+newCasesHyd+"/"+newRecoveredHyd;
    }


}


class HttpGetRequest extends AsyncTask<String, Void, String> {

    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    @Override
    protected String doInBackground(String... params){
        String stringUrl = params[0];
        String result;
        String inputLine;
        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);
            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            //Connect to our url
            connection.connect();
            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());
            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
        }
        catch(IOException e){
            e.printStackTrace();
            result = null;
        }
        return result;
    }
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}

