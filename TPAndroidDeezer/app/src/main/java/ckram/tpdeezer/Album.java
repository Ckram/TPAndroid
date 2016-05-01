package ckram.tpdeezer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Denis Apparicio
 * 
 */
public class Album implements Parcelable {

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.artist, flags);
		dest.writeString(this.id);
		dest.writeString(this.title);
		dest.writeString(this.cover);
		dest.writeList(this.tracks);
	}

	protected Album(Parcel in) {
		this.artist = in.readParcelable(Artist.class.getClassLoader());
		this.id = in.readString();
		this.title = in.readString();
		this.cover = in.readString();
		this.tracks = new ArrayList<Track>();
		in.readList(this.tracks, Track.class.getClassLoader());
	}

	public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
		@Override
		public Album createFromParcel(Parcel source) {
			return new Album(source);
		}

		@Override
		public Album[] newArray(int size) {
			return new Album[size];
		}
	};

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
}
