package door.one.marketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.jar.Attributes;

import de.hdodenhof.circleimageview.CircleImageView;
import door.one.marketplace.ui.NameActivity;
import io.paperdb.Paper;

public class UseraccActivity extends AppCompatActivity {
private ImageView image_product_cart,image_setting,image_PRODUCT_categories,image_exi;
private TextView text_user_name;
private CircleImageView image_circle_sett;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useracc);

        image_circle_sett=(CircleImageView)  findViewById(R.id.image_circle_sett);
        image_product_cart=(ImageView) findViewById(R.id.image_product_cart);
        image_PRODUCT_categories=(ImageView) findViewById(R.id.image_PRODUCT_categories);
        image_setting=(ImageView) findViewById(R.id.image_setting);
        image_exi=(ImageView) findViewById(R.id.image_exi);






image_setting.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent settIntent=new Intent(UseraccActivity.this,SettingActivity.class);
        startActivity(settIntent);
    }
});
image_exi.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent exIntent=new Intent(UseraccActivity.this, NameActivity.class);
        startActivity(exIntent);
        Paper.book().destroy();
    }
});
    }
}