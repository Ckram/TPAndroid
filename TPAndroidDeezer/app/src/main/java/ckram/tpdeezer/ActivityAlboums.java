package ckram.tpdeezer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ActivityAlboums extends AppCompatActivity {
    ArrayList<Album> albums;
    ArrayList<String> albumsString;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_alboums);
        albums = this.getIntent().getParcelableArrayListExtra("albums");
        albumsString = new ArrayList<String>();
        for (Album a :  albums)
            albumsString.add(a.getTitle());
        for (Album a : albums)
            Log.d("Alboums",a.getTitle());

        listView = (ListView)findViewById(R.id.listViewAlbums);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,albumsString);
        ArrayAdapter<String> adapterAlbum = new ArrayAdapter<String>(this,R.layout.simple_list_item_album, albumsString);
        listView.setAdapter(adapterAlbum);
    }

}
