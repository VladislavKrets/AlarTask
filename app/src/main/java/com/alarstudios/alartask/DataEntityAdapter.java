package com.alarstudios.alartask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class DataEntityAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<DataEntity> dataEntities;

    public DataEntityAdapter(Context context, List<DataEntity> dataEntities) {
        this.context = context;
        this.dataEntities = dataEntities;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataEntities.size();
    }

    @Override
    public DataEntity getItem(int i) {
        return dataEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void addItem(DataEntity dataEntity) {
        dataEntities.add(dataEntity);
    }

    public List<DataEntity> getDataEntities() {
        return dataEntities;
    }

    public void setDataEntities(List<DataEntity> dataEntities) {
        this.dataEntities = dataEntities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.dataentity_item, viewGroup, false);
        }

        DataEntity dataEntity = dataEntities.get(position);
        TextView nameTextView = view.findViewById(R.id.nameTextView);
        ImageView itemImageView = view.findViewById(R.id.itemImageView);
        nameTextView.setText(dataEntity.getName());
        new DownloadImageTask(itemImageView, position).execute();
        return view;
    }

    private class DownloadImageTask extends AsyncTask<Void, Void, Void> {
        private ImageView imageView;
        private int position;
        private Bitmap bitmap;

        public DownloadImageTask(ImageView imageView, int position) {
            this.imageView = imageView;
            this.position = position;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File file = new File(context.getFilesDir(), "item" + position + ".png");
            if (file.exists()) file.delete();
            try {
                URL url = new URL("https://cdn4.iconfinder.com/data/icons/business-color-4/512/city-512.png");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(url.openStream());
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
