package door.one.marketplace.Admin;

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

import door.one.marketplace.ui.LoginActivity;
import door.one.marketplace.Model.Users;
import door.one.marketplace.Prevalent.Prevalent;
import door.one.marketplace.R;
import io.paperdb.Paper;

public class AdminLogActivity extends AppCompatActivity {
private EditText phone_login_admin,password_login_admin;
private Button button_signin_admin;
private CheckBox checkbox_admin;
private TextView textnotadmin_log;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log);
button_signin_admin=(Button) findViewById(R.id.button_signin_admin);
        phone_login_admin=(EditText) findViewById(R.id.phone_login_admin);


        progressDialog=new ProgressDialog(this);


        password_login_admin=(EditText) findViewById(R.id.password_login_admin);
textnotadmin_log=(TextView) findViewById(R.id.textnotadmin_log);
        checkbox_admin=(CheckBox) findViewById(R.id.checkbox_admin);


        textnotadmin_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notadIntent=new Intent(AdminLogActivity.this, LoginActivity.class);
                startActivity(notadIntent);
            }
        });




        button_signin_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logadmin();
            }
        });
    }

    private void logadmin() {
        String phoneuser=phone_login_admin.getText().toString();
        String password=password_login_admin.getText().toString();

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

    private void Validateuser(String phoneadmin, String password) {

        if (checkbox_admin.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey,phoneadmin);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("admin").child(phoneadmin).exists()){
                    Users userData=snapshot.child("admin").child(phoneadmin).getValue(Users.class);
                    if (userData.getPhoneuser().equals(phoneadmin)){
                        if (userData.getPassword().equals(password)){
                            progressDialog.dismiss();
                            Toast.makeText(AdminLogActivity.this, "успешный вход", Toast.LENGTH_SHORT).show();
                            Intent hoIntent=new Intent(AdminLogActivity.this, AdminCategoryActivity.class);
                            startActivity(hoIntent);
                        }
                        else {

                            progressDialog.dismiss();
                            Toast.makeText(AdminLogActivity.this, "неверный пароль", Toast.LENGTH_SHORT).show();

                        }
                    }
                }else {
                    Toast.makeText(AdminLogActivity.this, "Аккаунт с номером"+phoneadmin+"не существует", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent nesIntent=new Intent(AdminLogActivity.this, AdminRegActivity.class);
                    startActivity(nesIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





    }
