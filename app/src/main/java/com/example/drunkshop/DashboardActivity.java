package com.example.drunkshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements SensorEventListener {

    private static final String LOG_TAG = DashboardActivity.class.getName();

    private FirebaseUser user;
    private FirebaseAuth auth;

    private RecyclerView recView;
    private ArrayList<Drink> itemList; //mItemsData
    private DrinkAdapter adapter;
    private int gridNum = 1;
    private boolean viewRow = true;
    private int queryLimit = 30;

    private FirebaseFirestore firestore;
    private CollectionReference drinksRef; //mItems

    private FrameLayout redCircle;
    private int cartItems = 0;
    private TextView contentTextView;

    private NotifHandler handler;

    private Sensor sensor;
    private SensorManager manager;
    boolean isRunning = false;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        auth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d(LOG_TAG, "Jóváhagyott felhasználó");
        } else {
            Log.d(LOG_TAG, "Nem jóváhagyott felhasználó");
            finish();
        }

        recView = findViewById(R.id.recicleView);
        recView.setLayoutManager(new GridLayoutManager(this, gridNum));
        itemList = new ArrayList<>();
        adapter = new DrinkAdapter(this, itemList);
        recView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        drinksRef = firestore.collection("Drinks");

        queryData();

        IntentFilter filer = new IntentFilter();
        filer.addAction(Intent.ACTION_POWER_CONNECTED);
        filer.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filer.addAction(Intent.ACTION_BATTERY_LOW);
        filer.addAction(Intent.ACTION_BATTERY_OKAY);
        this.registerReceiver(batteryReceiver, filer);

        handler = new NotifHandler(this);

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT);


    }

    BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action == null){ return; }
            switch (action){
                case Intent.ACTION_POWER_CONNECTED:

                case Intent.ACTION_BATTERY_OKAY:
                    queryLimit = 30;
                    break;

                case Intent.ACTION_POWER_DISCONNECTED:

                case Intent.ACTION_BATTERY_LOW:
                    queryLimit = 5;
                    break;
            }
            queryData();
        }
    };

    private void queryData(){
        itemList.clear();
        drinksRef.orderBy("rating", Query.Direction.DESCENDING).limit(queryLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                Drink drink = doc.toObject(Drink.class);
                drink.setId(doc.getId());
                itemList.add(drink);
            }
            if(itemList.size() == 0){
                initializeDrinks();
                queryData();
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void initializeDrinks() {
        String[] drinkNames = getResources().getStringArray(R.array.drink_names);
        String[] drinkInfo = getResources().getStringArray(R.array.drink_infos);
        String[] drinkPrice = getResources().getStringArray(R.array.drink_prices);
        TypedArray drinkImage = getResources().obtainTypedArray(R.array.drink_images);
        TypedArray drinkRate = getResources().obtainTypedArray(R.array.drink_rates);

        for(int i = 0; i < drinkNames.length; i++){
            drinksRef.add(new Drink(drinkNames[i], drinkInfo[i], drinkPrice[i], drinkRate.getFloat(i, 0), drinkImage.getResourceId(i, 0), 0));
        }
        drinkImage.recycle();
    }

    public void deleteDrink(Drink drink){
        DocumentReference ref = drinksRef.document(drink._getId());
        ref.delete().addOnSuccessListener(success -> {
            Log.d(LOG_TAG, "Ital sikeresen törölve");
            Toast.makeText(DashboardActivity.this, "Sikeres törlés", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(fail -> {
            Log.d(LOG_TAG, "Ital nem lett törölve");
            Toast.makeText(DashboardActivity.this, "Nem sikerült törölni", Toast.LENGTH_SHORT).show();
        });
        queryData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate( R.menu.list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out:
                Log.d(LOG_TAG, "Log out clicked");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.settings:
                Log.d(LOG_TAG, "Settings clicked");
                return true;
            case R.id.cart:
                Log.d(LOG_TAG, "Cart clicked");
                return true;
            case R.id.view_selector:
                if(viewRow){
                    changeSpanCount(item, R.drawable.ic_grid, 1);
                } else {
                    changeSpanCount(item, R.drawable.ic_row, 2);
                }
                Log.d(LOG_TAG, "View clicked");
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount){
        viewRow = !viewRow;
        item.setIcon(drawableId);
        GridLayoutManager manager = (GridLayoutManager) recView.getLayoutManager();
        manager.setSpanCount(spanCount);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertItem = menu.findItem(R.id.cart);
        FrameLayout root = (FrameLayout) alertItem.getActionView();
        redCircle = (FrameLayout) root.findViewById(R.id.red_circle);
        contentTextView = (TextView) root.findViewById(R.id.circle_count);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(alertItem);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIcon(Drink drink){
        cartItems = ( cartItems + 1);
        if( 0 < cartItems){
            contentTextView.setText(String.valueOf(cartItems));
        } else {
            contentTextView.setText("");
        }
        redCircle.setVisibility((cartItems > 0) ? View.VISIBLE : View.GONE);

        drinksRef.document(drink._getId()).update("count", drink.getCount()+1).addOnFailureListener(failure -> {
            Toast.makeText(DashboardActivity.this, "Sikertelen módosítás", Toast.LENGTH_SHORT).show();
        });
        handler.sendNotification(drink.getName());
        queryData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.values[0] > 50 && isRunning == false){
            isRunning = true;
            player = new MediaPlayer();
            try {
                player.setDataSource("https://pl.meln.top/mr/40ffb47431dab1c8b1282445b1e4b5c0.mp3??session_key=a9f12e145752e4c159f06b0cb8e80118");
                player.prepare();
                player.start();
            } catch (IOException err){
                err.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}