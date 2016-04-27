package ckram.tpdeezer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Denis Apparicio
 * 
 */
public class Artist implements Parcelable {

  private String id;

  private String name;

  private String link;

  private String picture;

  public Artist() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.name);
    dest.writeString(this.link);
    dest.writeString(this.picture);
  }

  protected Artist(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
    this.link = in.readString();
    this.picture = in.readString();
  }

  public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
    @Override
    public Artist createFromParcel(Parcel source) {
      return new Artist(source);
    }

    @Override
    public Artist[] newArray(int size) {
      return new Artist[size];
    }
  };
}
