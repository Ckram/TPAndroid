package ckram.tpdeezer;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {

  private String title;

  private String preview;

  public Track() {
    super();
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPreview() {
    return preview;
  }

  public void setPreview(String preview) {
    this.preview = preview;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.title);
    dest.writeString(this.preview);
  }

  protected Track(Parcel in) {
    this.title = in.readString();
    this.preview = in.readString();
  }

  public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {
    @Override
    public Track createFromParcel(Parcel source) {
      return new Track(source);
    }

    @Override
    public Track[] newArray(int size) {
      return new Track[size];
    }
  };
}
