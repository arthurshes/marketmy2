package door.one.marketplace.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import door.one.marketplace.Admin.AdminLogActivity;
import door.one.marketplace.Model.Users;
import door.one.marketplace.Prevalent.Prevalent;
import door.one.marketplace.R;
import door.one.marketplace.Users.HomeActivity;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText phone_login,password_login;
    private Button button_signin;
    private ProgressDialog progressDialog;
    private CheckBox checkbox;
    private TextView textadmin_log;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button_signin=(Button) findViewById(R.id.button_signin);
        phone_login=(EditText) findViewById(R.id.phone_login);
        password_login=(EditText) findViewById(R.id.password_login);
        textadmin_log=(TextView) findViewById(R.id.textadmin_log);
        progressDialog=new ProgressDialog(this);

        checkbox=(CheckBox) findViewById(R.id.checkbox);
        Paper.init(this);
        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loguser();
            }
        });
        textadmin_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent adminIntent=new Intent(LoginActivity.this, AdminLogActivity.class);
startActivity(adminIntent);
            }
        });
    }

    private void loguser() {
        String phoneuser=phone_login.getText().toString();
        String password=password_login.getText().toString();

        if (TextUtils.isEmpty(phoneuser)){
            Toast.makeText(this, "Введите номер телефона", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setTitle("Загрузка данных");
            progressDialog.setMessage("Пожалуйста подождите");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            Validateuser(phoneuser,password);
        }
    }

    private void Validateuser(String phoneuser, String password) {

        if (checkbox.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey,phoneuser);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("user").child(phoneuser).exists()){
                    Users userData=snapshot.child("user").child(phoneuser).getValue(Users.class);
                    if (userData.getPhoneuser().equals(phoneuser)){
                        if (userData.getPassword().equals(password)){
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "успешный вход", Toast.LENGTH_SHORT).show();
                            Intent hoIntent=new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(hoIntent);
                        }
                        else {

                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "неверный пароль", Toast.LENGTH_SHORT).show();

                        }
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Аккаунт с номером"+phoneuser+"не существует", Toast.LENGTH_SHORT).show();
                   progressDialog.dismiss();
                    Intent nesIntent=new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(nesIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}