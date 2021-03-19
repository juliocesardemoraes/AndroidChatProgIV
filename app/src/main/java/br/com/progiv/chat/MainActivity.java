package br.com.progiv.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Views
    private EditText editTextEmail;
    private EditText editTextName;

    private Button buttonEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiailizing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextName = (EditText) findViewById(R.id.editTextName);

        buttonEnter = (Button) findViewById(R.id.buttonEnter);

        buttonEnter.setOnClickListener(this);

        //If the user is already logged in
        //Starting chat room
        if (AppController.getInstance().isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ChatRoomActivity.class));
        }/**/

    }

    //Method to register user
    private void registerUser() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Entrando na sala de chat \uD83D\uDE0A" );
        progressDialog.show();

        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();

        JSONObject js = new JSONObject();
        HashMap<String,String> parametros = new HashMap<String, String>();
        parametros.put("nome", name);
        parametros.put("email", email);

       /*
            try {
                js.put("name","jcmcf");
                js.put("email","jcmc@gamisfjfiasjmdfo.com");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("TAG", "TAMO NA 73 ");
        */


        JsonObjectRequest req = new JsonObjectRequest(URLs.URL_REGISTER, new JSONObject(parametros),
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.hide();
                        Log.d("TAG", "Depois do hide");
                        try {
                            Log.d("TAG", "Antes do response");
                            Log.d("TAG", response.toString());
                            //JSONObject obj = new JSONObject(response);
                            int id = response.getInt("id");
                            String name = response.getString("name");
                            String email = response.getString("email");

                            //Logar o usuário
                            AppController.getInstance().loginUser(id,name,email);

                            //Mandando o app para a chat room
                            startActivity(new Intent(MainActivity.this, ChatRoomActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "Error ");
                        Log.d("TAG", error.toString());
                        VolleyLog.e("Error Volley: ", error.getMessage());
                        String x = error.getMessage();
                        //Log.d("TAG", error.getMessage());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(req);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Checking if user is logged in
        if(AppController.getInstance().isLoggedIn()){
            finish();
            startActivity(new Intent(this, ChatRoomActivity.class));
        }
    }


    @Override
    public void onClick(View v) {
        registerUser();
    }

}