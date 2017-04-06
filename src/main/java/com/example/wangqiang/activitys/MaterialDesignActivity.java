package com.example.wangqiang.activitys;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.wangqiang.app.R;
import com.example.wangqiang.data.Person;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.resource;
import static com.example.wangqiang.app.R.id.imageView;

public class MaterialDesignActivity extends AppCompatActivity {
    private static final String image_url = "http://i.imgur.com/idojSYm.png";
    private static final String TAG = "wq";

    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ImageView mToolbarHeaderIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maetrial_design);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        initViews();
        initData();
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_more);
        }
        mRecyclerView.setAdapter(new CustomAdapter(this, getPerson()));
        //should set layout manager, if not will show upexcepted;
        LinearLayoutManager llManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llManager);

        //use glide plugin to load image
        Glide.with(this).load(image_url).placeholder(R.drawable.icon).error(R.drawable.icon).into(mToolbarHeaderIcon);


    }
    RequestListener<String, GlideDrawable> listeners = new RequestListener<String, GlideDrawable>(){
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            Log.i(TAG, "onException: e="+ e.getMessage());
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            Log.i(TAG, "onResourceReady: ");
            return false;
        }
    };



    private List<Person> getPerson() {
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Person person = new Person();
            person.setName(i + "");
            personList.add(person);
        }
        return personList;
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbarHeaderIcon = (ImageView) findViewById(R.id.toolbar_header_image);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {

        List<Person> personList;
        Context context;

        public CustomAdapter(Context context, List<Person> person) {
            this.context = context;
            personList = person;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.material_design_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Person person = personList.get(position);
            holder.textView.setText(person.getName());
        }

        @Override
        public int getItemCount() {
            return personList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.cardview_image);
            textView = (TextView) itemView.findViewById(R.id.cardview_text);
        }
    }

}
