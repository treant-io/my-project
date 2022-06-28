package org.treant.functions;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.time.Instant;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class DB {

    private MongoCollection<Model> db;

    public DB () {
        String connector = System.getenv("db.connector");
        String dbName = System.getenv("db.name");
        String dbCollection = System.getenv("db.collection");

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        try {
            MongoClient client = MongoClients.create(connector);
            MongoDatabase database = client.getDatabase(dbName).withCodecRegistry(pojoCodecRegistry);
            db = database.getCollection(dbCollection, Model.class);
        } catch (Exception e) {
            System.out.println("ERROR initiating database connection.");
        }
    }

    public Model saveOrUpdateModel(Model model) {
        BasicDBObject incValue = new BasicDBObject();
        incValue.append("count", 1);

        BasicDBObject lastDate = new BasicDBObject();
        lastDate.append("last_date", Instant.now().toString());

        BasicDBObject updateDocument = new BasicDBObject();
        updateDocument.append("$inc", incValue);
        updateDocument.append("$set", lastDate);
        updateDocument.append("$setOnInsert", model);

        Bson filter = eq("name", model.getName());
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.upsert(true).returnDocument(ReturnDocument.AFTER);

        return db.findOneAndUpdate(filter, updateDocument, options);

    }

}
