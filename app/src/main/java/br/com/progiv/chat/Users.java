package br.com.progiv.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Users extends AppCompatActivity {


    //ArrayList of messages to store the thread messages
    private ArrayList<User> userList;
    //Inicializando adapter
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users);

        Toolbar toolbarusers =(Toolbar)findViewById(R.id.toolbarusers);
        toolbarusers.setTitle("Usuarios");
        setSupportActionBar(toolbarusers);

        //Initializing users arraylist
        userList = new ArrayList<>();

        //Initializing recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewUsers);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        fetchUsers();

    }

    //Creating option menu to add logout feature
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.menu_users, menu);
        return true;
    }

    private void fetchUsers() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_FETCH_USERS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Tag","RESPONSE AQUI: /n");
                        Log.d("TAG", response);
                        try {

                            //JSONArray jsonObject = new JSONArray(response);
                            //JSONObject res = new JSONObject(response);
                            JSONArray thread = new JSONArray(response);
                            for (int i = 0; i < thread.length(); i++) {
                                JSONObject obj = thread.getJSONObject(i);
                                Log.d("TAG", String.valueOf(obj));
                                int userId = obj.getInt("id");
                                String name = obj.getString("name");

                                User userObject = new User(userId, name);
                                userList.add(userObject);
                            }
                            adapter = new UserAdapter(Users.this, userList, AppController.getInstance().getUserId());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            Log.d("adsa", String.valueOf(e));
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        AppController.getInstance().addToRequestQueue(stringRequest);


    }



    //Adding logout option here
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuVoltar){
            startActivity(new Intent(this,ChatRoomActivity.class));
        }


        if (id == R.id.menuLogout) {
            AppController.getInstance().logout();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }



}
