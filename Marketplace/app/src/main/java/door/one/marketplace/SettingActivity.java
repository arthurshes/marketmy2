package door.one.marketplace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import door.one.marketplace.Prevalent.Prevalent;
import door.one.marketplace.Users.HomeActivity;

public class SettingActivity extends AppCompatActivity {
    private ImageView imagearrow_nazad;
    private EditText edit_name, edit_adress_set, edit_phone_set;
    private CircleImageView izmenit_photo;
    private Button button_save_data;
    private String checker = "";
    private Uri imageUri;
    private ProgressDialog progressDialog;
private StorageReference storageReference;
private        DatabaseReference UserRef;
private StorageTask  uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        button_save_data = (Button) findViewById(R.id.button_save_data);
        edit_name = (EditText) findViewById(R.id.edit_name);
        izmenit_photo = (CircleImageView) findViewById(R.id.izmenit_photo);
        edit_phone_set = (EditText) findViewById(R.id.edit_phone_set);
        edit_adress_set = (EditText) findViewById(R.id.edit_adress_set);
progressDialog=new ProgressDialog(this);
        imagearrow_nazad = (ImageView) findViewById(R.id.imagearrow_nazad);


        userInfodisplay(edit_phone_set ,edit_adress_set, edit_name,izmenit_photo);




storageReference= FirebaseStorage.getInstance().getReference().child("profile photo");
        imagearrow_nazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nazadIntent = new Intent(SettingActivity.this, UseraccActivity.class);
                startActivity(nazadIntent);
            }
        });

        button_save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker.equals("clicked")) {
                    userInfoSaved();
                } else {
                    updateOnInfo();
                }
            }
        });
        izmenit_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker="clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingActivity.this);

            }
        });
    }

    private void userInfodisplay(EditText edit_phone_set, EditText edit_adress_set, EditText edit_name, CircleImageView izmenit_photo) {
        String phoneuser=Prevalent.currentOnlineUser.getPhoneuser();

 DatabaseReference UserRef=FirebaseDatabase.getInstance().getReference().child("Users");


        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("image").exists()){
                        String image=snapshot.child("image").getValue().toString();
                        String name=snapshot.child("Username").getValue().toString();
                        String adress=snapshot.child("Adress").getValue().toString();
                        String phoneuser=snapshot.child("Pnomenumder").getValue().toString();


                        Picasso.get().load(image).into(izmenit_photo);
                        edit_phone_set.setText(phoneuser);
                        edit_adress_set.setText(adress);
                        edit_name.setText(name);


                    }
                    if (snapshot.child("address").exists()){

                        String name=snapshot.child("Username").getValue().toString();
                        String adress=snapshot.child("Adress").getValue().toString();
                        String phoneuser=snapshot.child("Pnomenumder").getValue().toString();



                        edit_phone_set.setText(phoneuser);
                        edit_adress_set.setText(adress);
                        edit_name.setText(name);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            izmenit_photo.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
        }









    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(edit_name.getText().toString())){
            Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();
        } if (TextUtils.isEmpty(edit_adress_set.getText().toString())){
            Toast.makeText(this, "Введите адрес", Toast.LENGTH_SHORT).show();
        } if (TextUtils.isEmpty(edit_phone_set.getText().toString())){
            Toast.makeText(this, "Введите номер", Toast.LENGTH_SHORT).show();
        }else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Обновляется...");
        progressDialog.setMessage("Пожалууйста подождите");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (imageUri !=null){
            final StorageReference fileRef= storageReference.child(Prevalent.currentOnlineUser.getPhoneuser()+"jpg");
            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task <Uri>task) {
                    if (task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                       String myUri=downloadUri.toString();
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String,Object> Usermap=new HashMap<>();
                        Usermap.put("Username",edit_name.getText().toString());
                        Usermap.put("Adress",edit_adress_set.getText().toString());
                        Usermap.put("Pnomenumder",edit_phone_set.getText().toString());
                        Usermap.put("image",myUri);

                        databaseReference.child(Prevalent.currentOnlineUser.getPhoneuser()).updateChildren(Usermap);
                        progressDialog.dismiss();

                        startActivity(new Intent(SettingActivity.this,HomeActivity.class));
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Изображение не выбрано", Toast.LENGTH_SHORT).show();
        }
    }




    private void updateOnInfo() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String,Object>Usermap=new HashMap<>();
        Usermap.put("Username",edit_name.getText().toString());
        Usermap.put("Adress",edit_adress_set.getText().toString());
        Usermap.put("Pnomenumder",edit_phone_set.getText().toString());
        reference.child(Prevalent.currentOnlineUser.getPhoneuser()).updateChildren(Usermap);

        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
        Toast.makeText(this, "Успешно сохранено", Toast.LENGTH_SHORT).show();
        finish();
    }



}