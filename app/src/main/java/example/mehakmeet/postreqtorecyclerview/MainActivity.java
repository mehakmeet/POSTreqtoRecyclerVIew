package example.mehakmeet.postreqtorecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import example.mehakmeet.postreqtorecyclerview.adapter.MyAdapter;
import example.mehakmeet.postreqtorecyclerview.model.food;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ArrayList<food> foodList=new ArrayList<>();
    MyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView=findViewById(R.id.recyclerview);



    }
}
