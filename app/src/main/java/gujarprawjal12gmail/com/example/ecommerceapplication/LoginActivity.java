package gujarprawjal12gmail.com.example.ecommerceapplication;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    TextView Registernow;
    ProgressDialog pdDialog;
    String URL_LOGIN = "https://www.ikshuhome.com:3000/UserLogin/SuperAdmin/Login";
    String luser,lpass;
    TextInputEditText username,password;
    Button loginButton;
    String is_signed_in="";
    SharedPreferences mPreferences;
    String sharedprofFile="com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPreferences=getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();

        is_signed_in = mPreferences.getString("issignedin","false");

        if(is_signed_in.equals("true"))
        {
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);

            finish();
        }

        Registernow =(TextView)findViewById(R.id.registernow);
        pdDialog= new ProgressDialog(LoginActivity.this);
        pdDialog.setTitle("Login please wait...");
        pdDialog.setCancelable(false);

        mPreferences=getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();

        username = (TextInputEditText) findViewById(R.id.lusername);
        password = (TextInputEditText)findViewById(R.id.lpassword);
        loginButton=(Button) findViewById(R.id.loginbutton);
        Registernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register=new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(register);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                luser=username.getText().toString().trim();
                lpass=password.getText().toString().trim();
                if(luser.isEmpty()||lpass.isEmpty())
                {
                    Toast.makeText(LoginActivity.this,"please enter valid data",Toast.LENGTH_SHORT).show();
                }else {
                    Login();
                }
            }
        });
    }

    private void Login()
    {
        pdDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            String id= jsonObject.getString("id");
                            String name = jsonObject.getString("name");
                            String username = jsonObject.getString("username");

                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),"Logged In  Success",Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();

                                preferencesEditor.putString("issignedin","true");
                                preferencesEditor.putString("SignedInUserID",id);
                                preferencesEditor.putString("SignedInName",name);
                                preferencesEditor.putString("SignedInusername",username);
                                preferencesEditor.apply();

                                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(i);
                                finish();

                            }
                            if(success.equals("0")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();
                            }
                            if(success.equals("3")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Registration Error !1"+e,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Registration Error !2"+error,Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("username",luser);
                params.put("password",lpass);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}