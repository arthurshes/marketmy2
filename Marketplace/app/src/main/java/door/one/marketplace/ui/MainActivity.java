
package door.one.marketplace.ui;

import static door.one.marketplace.Prevalent.Prevalent.UserPasswordKey;
import static door.one.marketplace.Prevalent.Prevalent.UserPhoneKey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import door.one.marketplace.Model.Users;
import door.one.marketplace.Prevalent.Prevalent;
import door.one.marketplace.R;
import door.one.marketplace.Users.HomeActivity;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button rebut,signinbut;

private DatabaseReference databaseReference;
private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signinbut = (Button) findViewById(R.id.signinbut);
        rebut = (Button) findViewById(R.id.rebut);

        progressDialog = new ProgressDialog(this);


        Paper.init(this);
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);


        databaseReference = FirebaseDatabase.getInstance().getReference("User");





        rebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           Intent IntnewIntent=new Intent(MainActivity.this,NameActivity.class);
           startActivity(IntnewIntent);
            }
        });


        if (UserPhoneKey != "" && UserPasswordKey != "") {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                ValidateUser(UserPhoneKey, UserPasswordKey);

                progressDialog.setTitle("Вход в приложение");
                progressDialog.setMessage("Пожалуйста, подождите...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

            }
        }

    }












    private void ValidateUser(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(phone).exists())
                {
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    Prevalent.currentOnlineUser=usersData;
                    if(usersData.getPhoneuser().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(homeIntent);
                        }
                        else {
                            progressDialog.dismiss();
                        }
                    }
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Аккаунт с номером " + phone + "не существует", Toast.LENGTH_SHORT).show();
                    Intent registerIntent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(registerIntent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }





























 }