package door.one.marketplace.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import door.one.marketplace.R;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView image_sneak,image_smart,image_mata,image_ball;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

      image_ball=(ImageView) findViewById(R.id.image_ball);
      image_mata=(ImageView) findViewById(R.id.image_mata);
      image_smart=(ImageView) findViewById(R.id.image_smart);
      image_sneak=(ImageView) findViewById(R.id.image_sneak);

      image_sneak.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
              intent.putExtra("category","sneakers");
              startActivity(intent);
          }
      });
        image_smart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smartintent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                smartintent.putExtra("category","smartphone");
                startActivity(smartintent);
            }
        });
        image_mata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent motointent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                motointent.putExtra("category","motocycle");
                startActivity(motointent);
            }
        });
        image_ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ballintent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                ballintent.putExtra("category","ball");
                startActivity(ballintent);
            }
        });
    }
}
