package door.one.marketplace.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import door.one.marketplace.Admin.AdminRegActivity;
import door.one.marketplace.R;
import door.one.marketplace.Users.HomeActivity;

public class NameActivity extends AppCompatActivity {
    private EditText phone_edit,name_edit,password_edit;
    private Button button_save,button_logintent;

    private TextView textadminreg;
private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        button_logintent=(Button) findViewById(R.id.button_logintent);
        password_edit=(EditText) findViewById(R.id.password_edit);
        button_save=(Button) findViewById(R.id.button_save);
        phone_edit=(EditText) findViewById(R.id.phone_edit);
        name_edit=(EditText) findViewById(R.id.name_edit);

        progressDialog=new ProgressDialog(this);
        textadminreg=(TextView) findViewById(R.id. textadminreg);


        textadminreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent daIntent=new Intent(NameActivity.this, AdminRegActivity.class);
                startActivity(daIntent);
            }
        });


        button_logintent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logacIntent=new Intent(NameActivity.this, LoginActivity.class);
                        startActivity(logacIntent);
            }
        });

     button_save.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             UserInfoCreate();
         }
     });
    }

    private void UserInfoCreate() {
        String username=name_edit.getText().toString();
        String phoneuser=phone_edit.getText().toString();
        String password=password_edit.getText().toString();

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(phoneuser)){
            Toast.makeText(this, "Введите номер телефона", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setTitle("Загрузка данных");
            progressDialog.setMessage("Пожалуйста подождите");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            Validatephone(username,phoneuser,password);
        }
    }

    private void Validatephone(String username, String phoneuser,String password){
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("user").child(phoneuser).exists()))
                {
                    HashMap<String,Object>userdatamap=new HashMap<>();
                    userdatamap.put("phone",phoneuser);

                    userdatamap.put("name",username);
                    userdatamap.put("password",password);
                    RootRef.child("user").child(phoneuser).updateChildren(userdatamap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Toast.makeText(NameActivity.this, "Успешная регистрация", Toast.LENGTH_SHORT).show();
                                        Intent nameusIntent=new Intent(NameActivity.this, HomeActivity.class);
                                        startActivity(nameusIntent);
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(NameActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    progressDialog.dismiss();

                    Intent logIntent=new Intent(NameActivity.this,LoginActivity.class);
                    startActivity(logIntent);

                    Toast.makeText(NameActivity.this, "Номер"+phoneuser+"уже зарегестрирован", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}