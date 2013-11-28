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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	Spinner categorias;
	LinearLayout content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content = (LinearLayout)findViewById(R.id.content);
        categorias = (Spinner)findViewById(R.id.spinner1);
        
        List<Map<String,String>> lista = new ArrayList<Map<String,String>>();
        String result = null;
        InputStream is = null;
        StringBuilder sb = null;
        
        HttpClient httpclient = new DefaultHttpClient();
    	HttpPost httppost = new HttpPost("https://mega.co.nz/#!9BFXBLRD!IARGeXjn7me5A9FBcJtJIi6E4dAQB5GBgS9Pfm4YP1s");
        try{
        	HttpResponse response = httpclient.execute(httppost);
        	is = response.getEntity().getContent();
        }catch(ClientProtocolException e){
        	Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }catch(IOException e){}
        try{
        	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        	sb = new StringBuilder();
        	String line = "";
        	
        	try{
        	       	while((line = reader.readLine()) != null){
        	       		sb.append(line);
        	       	}
        	}catch(IOException e){}
        	
        	is.close();
        	result = sb.toString();
        }catch(Exception e){
        	Toast.makeText(getApplicationContext(), "Error al convertir "+e.toString(), Toast.LENGTH_LONG).show();
        }   
        try{
        	JSONObject jsonResponse = new JSONObject(result);
        	JSONArray jArray = jsonResponse.optJSONArray("Categoria");
        	
        	for(int i = 0; i < jArray.length(); i++){
        		JSONObject json_data = jArray.getJSONObject(i);
        		lista.add(createEmployee("Categoria", json_data.optString("nombre")));
        	}
        }catch(Exception e){}
        
        SimpleAdapter adapter = new SimpleAdapter(this, lista, 
        										  android.R.layout.simple_spinner_dropdown_item, 
        										  new String[] {"Categoria"}, 
        										  new int[] {android.R.id.text1});
        categorias.setAdapter(adapter);
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private HashMap<String,String> createEmployee(String name, String name2){
    	HashMap<String,String> employeeNameNo = new HashMap<String,String>();
    	employeeNameNo.put(name, name2);
    	return employeeNameNo;
    }
    
}
