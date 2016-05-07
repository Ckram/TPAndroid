package com.ensim.crakm.monbudget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Moi on 07/05/2016.
 */
public class Categorie {
    public static HashMap<String,Categorie> categories = new HashMap<String, Categorie>();
    String nomCategorie;


    public Categorie GetCategorie(String nomCategorie) {
        if (!categories.containsKey(nomCategorie))
            categories.put(nomCategorie, new Categorie());
        return  categories.get(nomCategorie);
    }

    private  Categorie()
    {};

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }


}
