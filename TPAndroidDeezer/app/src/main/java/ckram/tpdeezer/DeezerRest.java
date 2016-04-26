package ckram.tpdeezer;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import ckram.tpdeezer.Album;
import ckram.tpdeezer.Track;

/**
 * Created by denis on 27/04/15.
 */
public class DeezerRest {

  /**
   * Restitue la liste des albums d'un auteur
   * @param author l'auteur.
   * @throws java.io.IOException
   * @throws javax.xml.parsers.ParserConfigurationException
   * @throws org.xml.sax.SAXException
   */
  public static List<Album> findAlbums(String author) throws ProtocolException, IOException, ParserConfigurationException, SAXException {
    // Constitution de l'URL
    StringBuilder sUrl = new StringBuilder();
    sUrl.append("http://api.deezer.com/2.0/search/album?q=");
    sUrl.append(author.replaceAll(" ", "+"));
    sUrl.append("&output=xml");

    URL url = new URL(sUrl.toString());

    HttpURLConnection cnx = (HttpURLConnection) url.openConnection();
    cnx.setConnectTimeout(5000);
    cnx.setReadTimeout(5000);
    cnx.setRequestMethod("GET");
    cnx.setDoInput(true);
    cnx.addRequestProperty("Accept-Language",
        "en;q=0.6,en-us;q=0.4,sv;q=0.2");

    SearchAlbumsHandler handler = new SearchAlbumsHandler();

    try {
      if (cnx.getResponseCode() == HttpURLConnection.HTTP_OK) {
        // recuperation d'un parser SAX
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();

        // constitution du flux xml
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(cnx.getInputStream()));
        InputSource source = new InputSource(reader);

        // parsing
        parser.parse(source, handler);
      }

    } finally {
      cnx.disconnect();
    }


    return handler.getListAlbum();
  }

  /**
   * Restitue la liste des chansons d'un album.
   *
   * @param id
   *          id de l'album.
   * @return la liste des chansons.
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public static List<Track> findTracks(String id) throws IOException,
      ParserConfigurationException,
      SAXException {


    // Constitution de l'URL
    StringBuilder sUrl = new StringBuilder();
    sUrl.append("http://api.deezer.com/2.0/album/");
    sUrl.append(id);
    sUrl.append("?output=xml");

    URL url = new URL(sUrl.toString());


    HttpURLConnection cnx = (HttpURLConnection) url.openConnection();
    cnx.setConnectTimeout(5000);
    cnx.setReadTimeout(5000);
    cnx.setRequestMethod("GET");
    cnx.setDoInput(true);
    cnx.addRequestProperty("Accept-Language", "en;q=0.6,en-us;q=0.4,sv;q=0.2");

    List<Track> listTracks = new ArrayList<Track>();

    try {
      if (cnx.getResponseCode() == HttpURLConnection.HTTP_OK) {
        InputStream in = cnx.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        // Creation du parser
        DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
        fact.setNamespaceAware(true);
        DocumentBuilder builder = fact.newDocumentBuilder();

        // constitution du flux xml
        Document doc = builder.parse(new InputSource(reader));

        // <tracks>
        NodeList nodeList = doc.getElementsByTagName("track");
        for (int i = 0; i < nodeList.getLength(); i++) {
          Track track = new Track();
          listTracks.add(track);

          Element el = (Element) nodeList.item(i);
          // titre
          String title = el.getElementsByTagName("title").item(0)
              .getTextContent();
          track.setTitle(title);
          // preview
          String preview = el.getElementsByTagName("preview").item(0)
              .getTextContent();
          track.setPreview(preview);

        }
      }
    }
    finally {
      cnx.disconnect();
    }

    return listTracks;
  }
 }
