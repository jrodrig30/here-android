package br.unisc.pdm.trabalho.webservice;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/*
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.unisc.pdm.trabalho.database.model.Person;
import br.unisc.pdm.trabalho.volley.MultipartRequest;

public class PersonWebDao {

    /*
    private static final String BASE_URL = "http://jose.oneweb.com.br";
    private final Context context;

    public PersonWebDao(Context context) {
        this.context = context;
    }

    public void getAll() {
        String url = BASE_URL + "/people.json";
        final List<Person> people = new ArrayList<>();

        JsonArrayRequest req = new JsonArrayRequest(
                url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response){
                try {
                    for(int i =0; i < response.length(); i++) {
                        JSONObject jsonKeyValue = response.getJSONObject(i);
                        Person person = jsobjToPessoa(jsonKeyValue);
                        people.add(person);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Problema ao buscar dados da web", Toast.LENGTH_SHORT).show();
                Log.d("WBS", error.toString());
            }
        }
        );

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(req);
    }

    private Person jsobjToPessoa(JSONObject json){
        Person p = new Person();
        try{
            p.setId(json.getInt("id"));
            p.setName(json.getString("name"));
            p.setPhoto(json.getString("photo"));
            p.setRegistrationNumber(json.getString("registration_number"));
            p.setEmail(json.getString("email"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return p;
    }

    public void insert(Person person){
        String url = BASE_URL + "/people.json";

        //========= CRIAR JSON COM DADOS DO ESTUDANTE ===============
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name",person.getName());
            jsonBody.put("registration_number",person.getRegistrationNumber());
            jsonBody.put("email",person.getEmail());
            jsonBody.put("photo",person.getPhoto());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("WBS",jsonBody.toString());

        //MultipartRequest request = new MultipartRequest(Request.Method.POST, url, );

        //========== FAZER REQUEST PASSANDO O JSON E tratando o retorno ===========
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        Log.d("WBS", "Retornou do request!");
                        Log.d("WBS", response.toString());
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("WBS","caiu no onErrorResponse");
                        Log.d("WBS", error.toString());
                    }
                });


        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjRequest);
    }

    public void getPersonById(int id) {
        String url = BASE_URL + "/pessoa/"+id;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        Person p = jsobjToPessoa(response);
                        Log.d("WBS", p.toString());
                        List<Person> people = new ArrayList<Person>();
                        people.add(p);
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(context,"Problema ao executar sua solicitação",Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjRequest);
    }

    public void editPerson(Person p){
        String url = BASE_URL + "/pessoa";

        //========= CRIAR JSON COM DADOS DO ESTUDANTE ===============
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id",p.getId());
            jsonBody.put("name",p.getName());
            jsonBody.put("registration_number",p.getRegistrationNumber());
            jsonBody.put("email",p.getEmail());
            jsonBody.put("photo",p.getPhoto());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("WBS",jsonBody.toString());
        //========== FAZER REQUEST PASSANDO O JSON E tratando o retorno ===========
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        Log.d("WBS", "Retornou do request!");
                        Log.d("WBS", response.toString());
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("WBS","caiu no onErrorResponse");
                        Log.d("WBS", error.toString());
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjRequest);
    }
*/
}