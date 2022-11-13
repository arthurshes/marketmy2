package door.one.marketplace.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import door.one.marketplace.ItemClickListner;
import door.one.marketplace.R;

public class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName,txtProductopisanie,txtProductPrice;
    public ImageView  product_image;
    public ItemClickListner listner;

    public ProductHolder(View itemView){
     super(itemView);
        product_image=itemView.findViewById(R.id.product_image);
       txtProductName=itemView.findViewById(R.id.produt_name_text);
       txtProductopisanie=itemView.findViewById(R.id.product_opisnie);
        txtProductPrice=itemView.findViewById(R.id.product_surprice);
    }
    public void setItemClickListner(ItemClickListner listner){this.listner=listner;}

    @Override
    public void onClick(View view){
        listner.onClick(view,getAdapterPosition(),false);
    }
}
