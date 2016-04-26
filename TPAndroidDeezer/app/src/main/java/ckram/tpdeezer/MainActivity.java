package ckram.tpdeezer;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "ActivityChercher";
    EditText nomAuteur;
    List <Album> albums;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nomAuteur = (EditText)findViewById(R.id.champChercher);


    }

    public void chercherAlbums(View v){
        AsyncTask<String,Void,List<Album>> a = new AsyncTask<String,Void,List<Album>>() {

            @Override
            protected List<Album> doInBackground(String... params) {
                List<Album> albumsTemp = new ArrayList<Album>();
                try {
                    albumsTemp = DeezerRest.findAlbums(params[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                return albumsTemp;

            }

            @Override
            protected void onPostExecute(List<Album> albumsTemp) {
                albums = albumsTemp;
                for (Album album : albums)
                    Log.d(TAG,album.toString());
            }
        };
        a.execute(nomAuteur.getText().toString());


    }
}
