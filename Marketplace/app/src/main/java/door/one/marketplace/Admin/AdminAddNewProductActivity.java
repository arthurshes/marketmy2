package door.one.marketplace.Admin;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import door.one.marketplace.R;

public class AdminAddNewProductActivity extends AppCompatActivity {
private String categoryName,description,price,productname,saveCurrentDate,saveCurrentTime,ProductRandomKey;
private String downloadImageUrl;
private ImageView image_product;
private EditText product_name,product_price,product_texti;
private Button button_saveproduct;
private static final int GALLERYPICK=1;
private Uri imageUri;
private StorageReference storageReference;
private DatabaseReference databaseReference;
private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        categoryName=getIntent().getExtras().get("category").toString();
        image_product=(ImageView) findViewById(R.id.image_product);
        product_name=(EditText) findViewById(R.id.product_name);
        product_price=(EditText) findViewById(R.id.product_price);
        product_texti=(EditText) findViewById(R.id.product_texti);
        progressDialog=new ProgressDialog(this);
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Products");
        button_saveproduct=(Button) findViewById(R.id.button_saveproduct);
storageReference= FirebaseStorage.getInstance().getReference().child("productImages");
        button_saveproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
            }
        });
        image_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
    }

    private void OpenGallery() {
        Intent galleryintent=new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,GALLERYPICK);
    }

    private void ValidateProductData() {
        description=product_texti.getText().toString();
        price=product_price.getText().toString();
        productname=product_name.getText().toString();

        if (imageUri==null){
            Toast.makeText(this, "Добавьте изображение", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(description)){
            Toast.makeText(this, "Добавь описание", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(price)){
            Toast.makeText(this, "добавьте стоимость", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(productname)){
            Toast.makeText(this, "Добавьте название товара", Toast.LENGTH_SHORT).show();
        }else {
            StoreProductInfo();
        }
    }

    private void StoreProductInfo() {


        progressDialog.setTitle("Загрузка данных");
        progressDialog.setMessage("Пожалуйста подождите");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("ddMMyyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HHmmss");
       saveCurrentTime=currentTime.format(calendar.getTime());

        ProductRandomKey=saveCurrentDate+saveCurrentTime;

        StorageReference filePath=storageReference.child(imageUri.getLastPathSegment()+ProductRandomKey+".jpg");
        final UploadTask uploadTask=filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "error"+message, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Изображение загружено", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                      if (!task.isSuccessful()){
                          throw task.getException();
                      }
                      downloadImageUrl=filePath.getDownloadUrl().toString();
                      return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){





                            Toast.makeText(AdminAddNewProductActivity.this, "Фото сохранено", Toast.LENGTH_SHORT).show();


                            SaveProductInfoToDatabase();
                        }


                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("productId",ProductRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",price);
        productMap.put("pname",productname);

        databaseReference.child(ProductRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "Товар добавлен", Toast.LENGTH_SHORT).show();
                    Intent adPrIntent=new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);
                    startActivity(adPrIntent);
                }else{
                    progressDialog.dismiss();
                    String message=task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "error"+message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERYPICK&&resultCode==RESULT_OK&&data !=null){
            imageUri=data.getData();
            image_product.setImageURI(imageUri);
        }

    }
}