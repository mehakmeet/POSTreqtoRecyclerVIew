package example.mehakmeet.postreqtorecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

   private ApiInterface mAPIService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView=findViewById(R.id.recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        foodList=new ArrayList<>();
        mAdapter=new MyAdapter(foodList,this);
        mRecyclerView.setAdapter(mAdapter);

        mAPIService= ApiUtils.getAPIService();
        sendPost();

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
                        String name;
                        int price;
                        boolean is_veg;

                        for(int i=0;i<b.length();i++){

                                JSONObject jobject=b.getJSONObject(i);
                                name=jobject.getString("name");
                                price=Integer.parseInt(jobject.getString("price"));
                                is_veg=jobject.getBoolean("is_veg");


                                food collection=new food(name,price,0,is_veg);

                                foodList.add(collection);
                            }

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
