package ckram.tpdeezer;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityTraques extends AppCompatActivity {
    ListView listView;
    ArrayList<Track> tracks;
    public static String TAG = "ActivityTraques";
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_traques);
        tracks = this.getIntent().getParcelableArrayListExtra("tracks");
        listView = (ListView) findViewById(R.id.listViewTracks);
        ViewTrackAdapter adapterTrack = new ViewTrackAdapter(this,R.layout.simple_list_item_track,tracks);
        View header = (View)getLayoutInflater().inflate(R.layout.simple_list_item_track_header, null);
        listView.addHeaderView(header);
        listView.setAdapter(adapterTrack);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(tracks.get(position-1).getPreview());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }});
    }
}

class ViewTrackAdapter extends ArrayAdapter<Track>
{
    Context context;
    int layoutResourceId;
    ArrayList<Track> Tracks;
    public ViewTrackAdapter (Context context,int layoutResourceId,ArrayList<Track> Tracks)
    {
        super(context, layoutResourceId, Tracks);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.Tracks = Tracks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ligne = convertView;
        TrackHolder holder = null;
        if(ligne == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            ligne = inflater.inflate(layoutResourceId, parent, false);

            holder = new TrackHolder();
            holder.txtTitle = (TextView)ligne.findViewById(R.id.txtTitleTracks);

            ligne.setTag(holder);
        }
        else
        {
            holder = (TrackHolder) ligne.getTag();
        }

        Track Track = Tracks.get(position);
        holder.txtTitle.setText(Track.getTitle());


        return ligne;

    }



    static class TrackHolder
    {
        TextView txtTitle;
    }
}