package com.example.saa.shoppinglist.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.saa.shoppinglist.MainActivity;
import com.example.saa.shoppinglist.R;
import com.example.saa.shoppinglist.db.PurchaseDBModel;
import java.io.FileNotFoundException;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListAdapterViewHolder> {

    private List<PurchaseDBModel> purchaseslist;
    private MainActivity mainActivity;
    public int purchasesListType = 0;
    private int checkClick = 0;

    public ShoppingListAdapter(List<PurchaseDBModel> purchaseslist, MainActivity mainActivity) {
        this.purchaseslist = purchaseslist;
        this.mainActivity = mainActivity;

    }


    @NonNull
    @Override
    public ShoppingListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_rv_shopping_list,parent,false);
        return new ShoppingListAdapterViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapterViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        switch (purchasesListType){
            case 0:{
                mainActivity.checkBoxAllPurchases.setVisibility(View.VISIBLE);
                holder.checkBoxPurchases.setVisibility(CheckBox.VISIBLE);
                loadData(holder,position);
                break;
            }

            case 1:{
                holder.checkBoxPurchases.setVisibility(CheckBox.INVISIBLE);
                mainActivity.checkBoxAllPurchases.setVisibility(View.INVISIBLE);
                loadData(holder,position);
                break;
            }
            case 3:{
                holder.checkBoxPurchases.setChecked(purchaseslist.get(position).isIfBought());
                loadData(holder,position);
                break;
            }

        }


    }

    private void loadData(@NonNull ShoppingListAdapterViewHolder holder, final int position){
        if (!purchaseslist.get(position).getImageURL().equals("null")) {

            Uri uri = Uri.parse(purchaseslist.get(position).getImageURL());

            try {
                holder.ivPurchasesImage.setImageBitmap(loadScaledBitmap(uri));
                holder.tvPurchases.setText("");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            holder.tvPurchases.setVisibility(View.VISIBLE);
            holder.tvPurchases.setText(purchaseslist.get(position).getPurchase());
            holder.ivPurchasesImage.setImageResource(R.drawable.ic_brightness_white);

        }

        holder.checkBoxPurchases.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    checkClick++;
                    mainActivity.imageButtonCheck.setVisibility(View.VISIBLE);
                    purchaseslist.get(position).setIfBought(b);
                }
                if (!b && checkClick >1){
                    checkClick--;
                    purchaseslist.get(position).setIfBought(b);
                }else if (!b && checkClick ==1){
                    mainActivity.imageButtonCheck.setVisibility(View.INVISIBLE);
                    mainActivity.checkBoxAllPurchases.setChecked(false);
                    checkClick--;
                    mainActivity.checkBoxAllPurchases.setChecked(false);
                    purchaseslist.get(position).setIfBought(b);
                }
            }
        });


    }

    private Bitmap loadScaledBitmap(Uri src) throws FileNotFoundException {

        //display the file to be loadScaledBitmap(),
        //such that you can know how much work on it.

        // required max width/height
        final int REQ_WIDTH = 150;
        final int REQ_HEIGHT = 150;

        Bitmap bm;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(mainActivity.getContentResolver().openInputStream(src),
                null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, REQ_WIDTH,
                REQ_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeStream(
                mainActivity.getContentResolver().openInputStream(src), null, options);

        return bm;
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    @Override
    public int getItemCount() {
        return purchaseslist.size();
    }

    class ShoppingListAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView tvPurchases;
        CheckBox checkBoxPurchases;
        ImageView ivPurchasesImage;

        ShoppingListAdapterViewHolder(View itemView) {
            super(itemView);

            tvPurchases = itemView.findViewById(R.id.tvPurchases);
            checkBoxPurchases = itemView.findViewById(R.id.checkBoxPurchases);
            ivPurchasesImage = itemView.findViewById(R.id.ivPurchasesImage);

        }
    }
}
