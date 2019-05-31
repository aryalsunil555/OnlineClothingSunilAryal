package adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlinestore_4th.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import model.Item;
import url.Url;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder>{

    private List<Item> itemList;
    private Context context;

    public ItemAdapter(List<Item> itemList, Context context){
        this.itemList= itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup,false);
        return new ItemHolder(view);    }


    private void strictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
        Item item= itemList.get(i);
        String imagePath = Url.BASE_URL + "uploads/" + item.getItemImageName();
        strictMode();

        try {
            URL url= new URL(imagePath);
            itemHolder.imageView.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
        }catch (Exception e){
            e.printStackTrace();
        }

//        setting values in recyclerView
        itemHolder.tvName.setText(item.getItemName());
        itemHolder.tvDesc.setText(item.getItemDescription());
        itemHolder.tvPrice.setText(Float.toString(500));

    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName, tvDesc,tvPrice;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgItem);
            tvName = itemView.findViewById(R.id.tvName);
            tvDesc = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);

        }
    }
}