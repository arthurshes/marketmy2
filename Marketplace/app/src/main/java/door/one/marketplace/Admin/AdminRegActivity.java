package door.one.marketplace.Admin;

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

import door.one.marketplace.ui.NameActivity;
import door.one.marketplace.R;

public class AdminRegActivity extends AppCompatActivity {
    private EditText phone_edit_AD,password_edit_AD,name_edit_AD;
    private Button button_save_ad,button_logintent_ad;
    private FirebaseAuth mAuth;
    private TextView textnotadminreg;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reg2);

        progressDialog=new ProgressDialog(this);
        phone_edit_AD=(EditText) findViewById(R.id.phone_edit_AD);
        password_edit_AD=(EditText) findViewById(R.id.password_edit_AD);
        name_edit_AD=(EditText) findViewById(R.id.name_edit_AD);
        button_logintent_ad=(Button) findViewById(R.id.button_logintent_ad);
        button_save_ad=(Button) findViewById(R.id.button_save_ad);
        mAuth=FirebaseAuth.getInstance();
        textnotadminreg=(TextView) findViewById(R.id.textnotadminreg)  ;

        textnotadminreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminRegActivity.this, NameActivity.class);
                startActivity(intent);
            }
        });
        button_logintent_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adsIntent=new Intent(AdminRegActivity.this, AdminLogActivity.class);
                startActivity(adsIntent);
            }
        });
        button_save_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminInfoCreate();
            }
        });
    }

    private void AdminInfoCreate() {
        String adminname=name_edit_AD.getText().toString();
        String phoneadmin=phone_edit_AD.getText().toString();
        String password=password_edit_AD.getText().toString();

        if (TextUtils.isEmpty(adminname)){
            Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(phoneadmin)){
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

            Validatephone(adminname,phoneadmin,password);
        }
    }

    private void Validatephone(String username, String phoneadmin,String password){
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("admin").child(phoneadmin).exists()))
                {
                    HashMap<String,Object> userdatamap=new HashMap<>();
                    userdatamap.put("phone",phoneadmin);
                    userdatamap.put("uid",mAuth.getCurrentUser().getUid());
                    userdatamap.put("name",username);
                    userdatamap.put("password",password);
                    RootRef.child("admin").child(phoneadmin).updateChildren(userdatamap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Toast.makeText(AdminRegActivity.this, "Успешная регистрация", Toast.LENGTH_SHORT).show();
                                        Intent nameusIntent=new Intent(AdminRegActivity.this, AdminCategoryActivity.class);
                                        startActivity(nameusIntent);
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(AdminRegActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    progressDialog.dismiss();

                    Intent logIntent=new Intent(AdminRegActivity.this, AdminLogActivity.class);
                    startActivity(logIntent);

                    Toast.makeText(AdminRegActivity.this, "Номер"+phoneadmin+"уже зарегестрирован", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}