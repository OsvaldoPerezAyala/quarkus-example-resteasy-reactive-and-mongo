package com.osvaldo.service;

import com.mongodb.client.FindIterable;
import com.osvaldo.model.Fruit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ApplicationScoped
public class FruitService {

    @Inject
    MongoClient mongoClient;

    public JSONArray list() {
        JSONArray fruitsArray = new JSONArray();
        MongoCursor<Document> cursor = this.getCollection().find().iterator();

        try {
            while (cursor.hasNext()) {
                Document resultDoc = cursor.next();
                JSONObject fruitObject = new JSONObject()
                        .put("name", resultDoc.get("name"))
                        .put("description", resultDoc.get("description"));
                fruitsArray.put(fruitObject);
            }
        } finally {
            cursor.close();
        }
        return fruitsArray;
    }

    public void add(Document document) {
        getCollection().insertOne(document);
    }

    public JSONObject getFruit(String name) {

        JSONObject fruitObject = null;
        Document query = new Document();
        query.append("name", name);
        MongoCursor<Document> cursor = getCollection().find(query).iterator();
        try {
            if (cursor.hasNext()) {
                Document document = cursor.next();
                fruitObject = new JSONObject(document.toJson());
                fruitObject.remove("_id");
            }
        } finally {
            cursor.close();
        }
        return fruitObject;
    }

    private MongoCollection getCollection() {
        return mongoClient.getDatabase("fruit").getCollection("fruit");
    }
}
