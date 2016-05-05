package ckram.tpdeezer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class ActivityAlboums extends AppCompatActivity {
    ArrayList<Album> albums;
    ArrayList<String> albumsString;
    ListView listView;
    ArrayList <Track> tracks;
    public static String TAG = "ActivityAlboums";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_alboums);
        albums = this.getIntent().getParcelableArrayListExtra("albums");
        for (Album a : albums)
            Log.d("Alboums",a.getCover());
        listView = (ListView)findViewById(R.id.listViewAlbums);
        ViewAlbumAdapter adapterAlbum = new ViewAlbumAdapter(this,R.layout.simple_list_item_album,albums);
        View header = (View)getLayoutInflater().inflate(R.layout.simple_list_item_album_header, null);
        listView.addHeaderView(header);
        listView.setAdapter(adapterAlbum);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AsyncTask<String,Void,List<Track>> a = new AsyncTask<String,Void,List<Track>>() {

                    @Override
                    protected List<Track> doInBackground(String... params) {
                        List<Track> tracksTemp = new ArrayList<Track>();
                        try {
                            tracksTemp = DeezerRest.findTracks(params[0]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        }
                        return tracksTemp;

                    }

                    @Override
                    protected void onPostExecute(List<Track> tracksTemp) {
                        tracks = (ArrayList<Track>)tracksTemp;
                        for (Track track : tracks)
                            Log.d(TAG,track.toString());
                        Intent intent = new Intent(ActivityAlboums.this, ActivityTraques.class);


                        intent.putParcelableArrayListExtra("tracks", tracks);
                        startActivity(intent);

                    }
                };
                a.execute(albums.get(position-1).getId().toString());
                Log.d("Alboums",albums.get(position-1).getTitle());
            }
        });
    }



}
class ViewAlbumAdapter extends  ArrayAdapter<Album>
{
    Context context;
    int layoutResourceId;
    ArrayList<Album> albums;
    public ViewAlbumAdapter (Context context,int layoutResourceId,ArrayList<Album> albums)
    {
        super(context, layoutResourceId, albums);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.albums = albums;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ligne = convertView;
        AlbumHolder holder = null;
        if(ligne == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            ligne = inflater.inflate(layoutResourceId, parent, false);

            holder = new AlbumHolder();
            holder.imgIcon = (ImageView)ligne.findViewById(R.id.imgCover);
            holder.txtTitle = (TextView)ligne.findViewById(R.id.txtTitle);

            ligne.setTag(holder);
        }
        else
        {
            holder = (AlbumHolder) ligne.getTag();
        }

        Album album = albums.get(position);
        holder.txtTitle.setText(album.getTitle());
        Picasso.with(context).load(album.getCover()).into(holder.imgIcon);
        /*
        holder.imgIcon.setImageResource(weather.icon);
        */
        return ligne;

    }



    static class AlbumHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
