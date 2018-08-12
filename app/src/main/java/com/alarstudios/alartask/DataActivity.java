package com.alarstudios.alartask;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {
    private String code;
    private TextView errorTextView;
    private ListView listView;
    private int pageIndex;
    private boolean isLoading;
    private DataEntityAdapter dataEntityAdapter;
    private List<DataEntity> dataEntities;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dataEntities = new ArrayList<>();
        dataEntityAdapter = new DataEntityAdapter(this, dataEntities);
        initializeWidgets();
        listView.setAdapter(dataEntityAdapter);
        new LoadTask().execute();

    }

    private void initializeWidgets() {
        code = getIntent().getStringExtra("code");
        relativeLayout = findViewById(R.id.relativeLayout);
        errorTextView = findViewById(R.id.errorTextView);
        pageIndex = 1;
        isLoading = false;
        listView = findViewById(R.id.dataListView);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(!isLoading)
                    {
                        isLoading = true;
                        new LoadTask().execute();
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DataEntity dataEntity = dataEntities.get(position);
                Intent intent = new Intent(DataActivity.this, ItemDataActivity.class);
                intent.putExtra("id", dataEntity.getId());
                intent.putExtra("country", dataEntity.getCountry());
                intent.putExtra("name", dataEntity.getName());
                intent.putExtra("lat", dataEntity.getLat());
                intent.putExtra("lon", dataEntity.getLon());
                startActivity(intent);
            }
        });
    }

    private class LoadTask extends AsyncTask<Void, Void, String> {
        private ProgressBar progressBar;
        @Override
        protected void onPreExecute() {
            progressBar = new ProgressBar(DataActivity.this,null,android.R.attr.progressBarStyleLarge);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
            params.addRule(RelativeLayout.BELOW);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            relativeLayout.addView(progressBar,params);
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String urlParameters = String.format("code=%s&p=%d", code, pageIndex);
            try {
                String answer = Utils.getRequest("http://condor.alarstudios.com/test/data.cgi", urlParameters);
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(List.class, new JsonDataEntityArrayListDeserializer());
                Gson gson = builder.create();
                try {
                    List<DataEntity> entities = gson.fromJson(answer, List.class);
                    dataEntities.addAll(entities);
                    if (dataEntities.isEmpty()) return "";
                } catch (JsonSyntaxException e) { //pages with uncorrect json will be missed
                    System.out.println(pageIndex);
                    e.printStackTrace();
                }
                pageIndex++;
            } catch (IOException e) {
                e.printStackTrace();
                return getResources().getString(R.string.internet_connection);
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            isLoading = false;
            errorTextView.setText(s);
            dataEntityAdapter.notifyDataSetChanged();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}
