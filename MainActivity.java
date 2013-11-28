package com.example.prueba;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.AsyncTask;

public class MainActivity extends Activity {
	private String jsonResult;
	private String url;
	Spinner categorias;
	LinearLayout content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        url = "https://www.dropbox.com/home/PHP%20App_POS/prueba.php";
        content = (LinearLayout)findViewById(R.id.content);
        categorias = (Spinner)findViewById(R.id.spinner1);
        
        accessWebService();
       
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private class JsonReadTask extends AsyncTask<String, Void, String>{
    	    @Override
    	    protected String doInBackground(String... params){
    	    	HttpClient httpClient = new DefaultHttpClient();
    	    	HttpPost httpPost = new HttpPost(params[0]);
    	    	try{
    	    		HttpResponse response = httpClient.execute(httpPost);
    	    		jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
    	    	}catch(ClientProtocolException e){
    	    		e.printStackTrace();
    	    	}catch(IOException e){
    	    		e.printStackTrace();
    	    	}
    	    	return null;
    	    }
    	    
    	    private StringBuilder inputStreamToString(InputStream is){
    	    	String rLine = "";
    	    	StringBuilder answer = new StringBuilder();
    	    	BufferedReader rd = new BufferedReader(new InputStreamReader(is));
    	    	try{
    	    		while((rLine = rd.readLine()) != null){
    	    			answer.append(rLine);
    	    		}
    	    	}catch(IOException e){
    	    		Toast.makeText(getApplicationContext(), "Error..", Toast.LENGTH_SHORT).show();
    	    	}
    	    	return answer;
    	    }
    	    
    	    @Override
    	    protected void onPostExecute(String result){
    	    	ListDrwaer();
    	    }
    }
    
    public void accessWebService(){
    	JsonReadTask task = new JsonReadTask();
    	task.execute(new String[] { url });
    }
    
    public void ListDrwaer(){
    	List<Map<String, String>> employeeList =  new ArrayList<Map<String, String>>();
    	try{
    		JSONObject jsonResponse = new JSONObject(jsonResult);
    		JSONArray jsonMainNode = jsonResponse.optJSONArray("Categoria");
    		
    		for(int i = 0; i < jsonMainNode.length(); i++){
    			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
    			String name = jsonChildNode.optString("nombre");
    			employeeList.add(createEmployee("Categoria", name));
    		}
    	}catch(JSONException e){
    		Toast.makeText(getApplicationContext(), "Error " + e.toString(), Toast.LENGTH_LONG).show();
    	}
    	
    	SimpleAdapter simpleAdapter = new SimpleAdapter(this, employeeList, 
    													android.R.layout.simple_spinner_item, 
    													new String[] {"Categoria"}, 
    													new int[] {android.R.id.text1});
    	categorias.setAdapter(simpleAdapter);
    }
    
    private HashMap<String, String> createEmployee(String name, String nameR){
    	HashMap<String, String> employeeNameNo = new HashMap<String,String>();
    	employeeNameNo.put(name, nameR);
    	return employeeNameNo;
    }
}
