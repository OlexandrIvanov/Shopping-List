package com.example.saa.shoppinglist;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.saa.shoppinglist.adapters.ShoppingListAdapter;
import com.example.saa.shoppinglist.db.PurchaseDBModel;
import com.example.saa.shoppinglist.db.PurchaseDataSource;
import com.example.saa.shoppinglist.db.PurchaseDatabase;
import com.example.saa.shoppinglist.db.PurchaseRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

   private final int CAMERA_PERMISSION_ID = 101;
   private final int GALLERY_PERMISSION_ID = 100;

   private List<PurchaseDBModel> purchaseslist;
   private List<PurchaseDBModel> didPurchaseslist;

   public @BindView(R.id.checkBoxAllPurchases)CheckBox checkBoxAllPurchases;
   public @BindView(R.id.imageButtonCheck)ImageView imageButtonCheck;

   @BindView(R.id.mainActivityToolbar) Toolbar toolbar;

   private ShoppingListAdapter shoppingListAdapter;
   private CompositeDisposable compositeDisposable;
   private PurchaseRepository purchaseRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        didPurchaseslist = new ArrayList<>();
        compositeDisposable = new CompositeDisposable();
        purchaseslist = new ArrayList<>();

        setSupportActionBar(toolbar);


        RecyclerView rvShoppingList = findViewById(R.id.rvShoppingList);
        shoppingListAdapter = new ShoppingListAdapter(purchaseslist, this);
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(this);
        rvShoppingList.setLayoutManager(layoutManager);
        rvShoppingList.setAdapter(shoppingListAdapter);


        PurchaseDatabase purchaseDatabase =  PurchaseDatabase.getInstance(this);
        purchaseRepository = PurchaseRepository.getInstance(PurchaseDataSource.getInstance(purchaseDatabase.purchaseDao()));
    }

    private void addPurchaseToList(final String purchase, final String imageURI) {

        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                PurchaseDBModel purchaseDBModel = new PurchaseDBModel(purchase,false,imageURI);
                purchaseRepository.insertPurchase(purchaseDBModel);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(MainActivity.this, "Added!", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        loadData();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void loadData(){
        Disposable disposable = purchaseRepository.getAllPurchase().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<PurchaseDBModel>>() {
                    @Override
                    public void accept(List<PurchaseDBModel> purchaseDBModels) throws Exception {
                        onGetAllPurchases(purchaseDBModels);
                    }
                }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(MainActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        compositeDisposable.add(disposable);

    }

    private void onGetAllPurchases(List<PurchaseDBModel> purchaseDBModels) {
        purchaseslist.clear();
        purchaseslist.addAll(purchaseDBModels);
        shoppingListAdapter.notifyDataSetChanged();
    }

    public void deletePurchase(final PurchaseDBModel purchaseDBModel){



            Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {

                @Override
                public void subscribe(ObservableEmitter<Object> e) throws Exception {
                    purchaseRepository.deletePurchase(purchaseDBModel);
                    e.onComplete();
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(MainActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }, new Action() {
                        @Override
                        public void run() throws Exception {
                            loadData();
                        }
                    });

            compositeDisposable.add(disposable);

        }


    private void openCamera(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_ID);
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_ID);
        }
    }

    private void openGallery(){
//       if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){

           if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                   == PackageManager.PERMISSION_DENIED){
               ActivityCompat.requestPermissions(this,
                       new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_ID);
           }else { ActivityCompat.requestPermissions(this,
                   new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_ID);}

//       }

//       else {Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//           startActivityForResult(intent,GALLERY_PERMISSION_ID);}

   }

    private void openEnterPurchaseDialog(){

       final EditText editText = new EditText(MainActivity.this);
       editText.setHint("Enter purchase");

       new AlertDialog.Builder(MainActivity.this)
               .setTitle("Edit")
               .setMessage("Edit purchase name")
               .setView(editText)
               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       addPurchaseToList(editText.getText().toString(),"null");
                   }
               }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.dismiss();
           }
       }).create().show();

   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==CAMERA_PERMISSION_ID){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_PERMISSION_ID);
            }else Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
        }

         if (requestCode==GALLERY_PERMISSION_ID){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,GALLERY_PERMISSION_ID);
            }else Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == CAMERA_PERMISSION_ID){
                if (data !=null) {
                    addPurchaseToList("", Objects.requireNonNull(data.getData()).toString());
                }
            }

            if (requestCode ==GALLERY_PERMISSION_ID){
                if (data !=null) {
                    addPurchaseToList("", Objects.requireNonNull(data.getData()).toString());
                }
            }
        }

    @OnCheckedChanged(R.id.checkBoxAllPurchases)
    void checkBoxAllPurchases(boolean b) {

    if(b){

        for (int i = 0;i<purchaseslist.size();i++){
            if (!purchaseslist.get(i).isIfBought()) purchaseslist.get(i).setIfBought(b);
        }
        shoppingListAdapter.purchasesListType = 3;
        shoppingListAdapter.notifyDataSetChanged();
    }else {
        imageButtonCheck.setVisibility(View.INVISIBLE);
        for (int i = 0;i<purchaseslist.size();i++){
            purchaseslist.get(i).setIfBought(b);
        }
        shoppingListAdapter.purchasesListType = 3;
        shoppingListAdapter.notifyDataSetChanged();
    }

}

    @OnItemSelected (R.id.spinnerPurchasesType)
    void spinnerPurchasesType(int i){
        switch (i){
            case 0:
                shoppingListAdapter.purchasesListType=0;
                loadData();

                break;
            case 1:
                shoppingListAdapter.purchasesListType=1;
                loadData2();

                break;

        }
    }

    @OnClick(R.id.imageButtonCheck)
    void onClickImageButtonCheck() {
        checkBoxAllPurchases.setChecked(false);

        saveData();
        for (int i = 0;i<purchaseslist.size();i++){
            purchaseslist.get(i).setIfBought(false);
        }
        shoppingListAdapter.purchasesListType = 3;
    }

    @OnClick(R.id.fab)
    void onClickFab(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.add_purchase)
                .setItems(R.array.choiceDialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                openEnterPurchaseDialog();
                                break;
                            case 1:
                                openGallery();
                                break;
                            case 2:
                                openCamera();
                                break;
                        }
                    }
                });
        builder.create().show();

    }


    private void saveData(){

        for (int i = 0; i <purchaseslist.size();i++){
            if (purchaseslist.get(i).isIfBought()){
                didPurchaseslist.add(new PurchaseDBModel(purchaseslist.get(i).getPurchase()
                        ,purchaseslist.get(i).isIfBought()
                        ,purchaseslist.get(i).getImageURL()));
                deletePurchase(purchaseslist.get(i));

            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(didPurchaseslist);
        editor.putString("didPurchaseslist", json);
        editor.apply();

        loadData();

    }


    private void loadData2(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("didPurchaseslist", null);
        Type type = new TypeToken<ArrayList<PurchaseDBModel>>(){}.getType();
        didPurchaseslist = gson.fromJson(json,type);

        if (didPurchaseslist == null){
            didPurchaseslist = new ArrayList<>();
        }
        purchaseslist.clear();
        purchaseslist.addAll(didPurchaseslist);
        shoppingListAdapter.notifyDataSetChanged();

    }
}

