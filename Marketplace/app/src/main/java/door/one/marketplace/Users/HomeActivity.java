package door.one.marketplace.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import door.one.marketplace.Prevalent.Prevalent;
import door.one.marketplace.Products;
import door.one.marketplace.UseraccActivity;
import door.one.marketplace.ViewHolders.ProductHolder;
import door.one.marketplace.ui.MainActivity;
import door.one.marketplace.R;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private ImageView button_cart,image_but_saat;
private TextView textname_hame;
private DatabaseReference prodReference;
private RecyclerView recyclerView;
RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView=(RecyclerView) findViewById(R.id.recycle_product);
        prodReference= FirebaseDatabase.getInstance().getReference().child("Products");
button_cart=(ImageView) findViewById(R.id.button_cart);
image_but_saat=(ImageView) findViewById(R.id.image_but_saat);
layoutManager=new LinearLayoutManager(this);
recyclerView.setHasFixedSize(true);
recyclerView.setLayoutManager(layoutManager);
textname_hame=(TextView) findViewById(R.id.textname_hame);




image_but_saat.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent setIntent=new Intent(HomeActivity.this, UseraccActivity.class);
        startActivity(setIntent);
    }
});
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products>options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(prodReference,Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductHolder>(options) {
            @Override
            protected void onBindViewHolder( @androidx.annotation.NonNull ProductHolder holder, int position,  @androidx.annotation.NonNull Products model) {
                holder.txtProductName.setText(model.getPname());

               holder.txtProductopisanie.setText(model.getDescription());
               holder.txtProductPrice.setText(model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.product_image);
            }

            @Override
            public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                ProductHolder holder=new ProductHolder(view);


                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}