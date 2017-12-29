package example.mehakmeet.postreqtorecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import example.mehakmeet.postreqtorecyclerview.API.ApiInterface;
import example.mehakmeet.postreqtorecyclerview.API.ApiUtils;
import example.mehakmeet.postreqtorecyclerview.adapter.MyAdapter;
import example.mehakmeet.postreqtorecyclerview.model.food;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ArrayList<food> foodList=new ArrayList<>();
    MyAdapter mAdapter;

    EditText name_add,price_add;
    Button add_btn;
   private ApiInterface mAPIService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView=findViewById(R.id.recyclerview);

        name_add=findViewById(R.id.name_add);
        price_add=findViewById(R.id.price_add);
        add_btn=findViewById(R.id.add_btn);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAPIService= ApiUtils.getAPIService();
        foodList=new ArrayList<>();
        sendPost();
        Log.i("SIZE OF FoodList",String.valueOf(foodList.size()));
        mAdapter=new MyAdapter(foodList,this);
        mRecyclerView.setAdapter(mAdapter);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String add_new=name_add.getText().toString();
                int price_new=Integer.parseInt(price_add.getText().toString());

                food new_food=new food(add_new,price_new,0,false);

                foodList.add(new_food);
                mAdapter.notifyDataSetChanged();


            }
        });


    }

    private void sendPost() {
        final JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("key","ABCDEFG");

        Log.d("tagg","URL: "+mAPIService.insert(jsonObject).request().url().toString());
        mAPIService.insert(jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //Log.i("tagg",call.request().url().toString());
                if(response.isSuccessful()) {
                    try {

                        /*
                        EXAMPLE JSON
                        {
    "categories": [
        "Pizza",
        "Breakfast"
    ],
    "menu": {
        "Pizza": [
            {
                "price": "99",
                "name": "Pineapple Pizza",
                "is_veg": true
            },
            {
                "price": "89",
                "name": "Chicken Peproni Pizza",
                "is_veg": false
            }
        ],
        "Breakfast": [
            {
                "price": "39",
                "name": "Pancake",
                "is_veg": true
            }
        ]
    }
}
                         */

                        JSONObject jObj=new JSONObject(response.body().toString());
                        String info=jObj.getString("categories");
                        String menu=jObj.getString("menu");
                        JSONObject object=new JSONObject(menu);
                        JSONArray a=new JSONArray(info);
                        Log.i("MENU",info);

                       // for(int i=0;i<a.length();i++)
                       // {
                            Log.i("Categ"+1,a.get(0).toString());

                            String pizza_obj=object.getString(a.get(0).toString());

                            JSONArray b= new JSONArray(pizza_obj);
                        Log.i("PIZZA",pizza_obj);

                      //  foodList=new ArrayList<>();

                        Log.i("Size of b",String.valueOf(b.length()));
                        for(int i=0;i<b.length();i++){
                            String name;
                            int price;
                            boolean is_veg;
                                JSONObject jobject=(JSONObject) b.get(i);

                                name=jobject.getString("name");
                                price=Integer.parseInt(jobject.getString("price"));
                                is_veg=jobject.getBoolean("is_veg");


                                Log.i("Names"+i,name);
                            food collection=new food(name,price,0,is_veg);

                                foodList.add(collection);

                            }
                        Log.i("SIZE OF FoodList",String.valueOf(foodList.size()));
                       mAdapter.notifyDataSetChanged();

                       // }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                else
                {
                    Log.i("RESPONSE","Failed");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {


                Log.i("RESPONSE","OnFailure...Failed");
            }
        });


    }
}
