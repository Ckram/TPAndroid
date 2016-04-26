package ckram.tpdeezer;


import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ckram.tpdeezer.Album;
import ckram.tpdeezer.Artist;

/**
 * @author Denis Apparicio
 * 
 */
public class SearchAlbumsHandler extends DefaultHandler {

  private boolean       isAlbum   = false;

  private List<Album>   listAlbum = new ArrayList<Album>();

  private Album         currentAlbum;

  private String        st;

  private boolean       isArtist;

  /**
   * Restitue la liste des albums.
   * 
   * @return
   */
  public List<Album> getListAlbum() {
    return listAlbum;
  }

  /**
   * Creation d'un album et ajout à la liste
   */
  private Album createAlbum() {
    Album album = new Album();
    listAlbum.add(album);
    return album;
  }

  /**
   * Creation d'un artiste et ajout à la liste
   */
  private Artist createArtist() {
    return new Artist();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
   * java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  @Override
  public void startElement(String uri,
                           String localName,
                           String qName,
                           Attributes attributes) throws SAXException {
    if ("album".equals(localName)) {
      isAlbum = true;
      currentAlbum = createAlbum();
    }
    else if ("artist".equals(localName)) {
      isArtist = true;
      currentAlbum.setArtist(createArtist());
    }

  }

  /*
   * (non-Javadoc) BufferedReader
   * 
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if ("album".equals(localName)) {
      isAlbum = false;
    }
    else if ("artist".equals(localName)) {
      isArtist = false;
    }
    else if ("id".equals(localName)) {
      if (isArtist) {
        currentAlbum.getArtist().setId(st);
      }
      else {
        currentAlbum.setId(st);
      }
    }
    else if ("title".equals(localName) && isAlbum) {
      currentAlbum.setTitle(st);
    }
    else if ("cover".equals(localName) && isAlbum) {
      currentAlbum.setCover(st);
    }
    else if ("name".equals(localName) && isArtist) {
      currentAlbum.getArtist().setName(st);
    }
    else if ("link".equals(localName) && isArtist) {
        currentAlbum.getArtist().setLink(st);
    }
    else if ("picture".equals(localName) && isArtist) {
      currentAlbum.getArtist().setPicture(st);
    }
    st = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
   */
  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    if (isAlbum) {
      st = new String(ch, start, length);
    }
  }
}
