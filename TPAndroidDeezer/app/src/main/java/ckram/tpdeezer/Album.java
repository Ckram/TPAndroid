package ckram.tpdeezer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Denis Apparicio
 * 
 */
public class Album {

	private Artist artist;

	private String id;

	private String title;
	
	private String cover;
		
	private List<Track> tracks = new ArrayList<Track>();

	public Album() {		
	}

	@Override
	public String toString() {
		return "Album{" +
				"artist=" + artist +
				", id='" + id + '\'' +
				", title='" + title + '\'' +
				", cover='" + cover + '\'' +
				", tracks=" + tracks +
				'}';
	}

	public void addTrack(Track title) {
		tracks.add(title);
	}
	
	public List<Track> getTracks() {
		return tracks;
	}

	public void setTracks(List<Track> tracks) {
               this.tracks = tracks;
        }

        public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public String getCover() {
    return cover;
  }

  public void setCover(String cover) {
    this.cover = cover;
  }

  public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
