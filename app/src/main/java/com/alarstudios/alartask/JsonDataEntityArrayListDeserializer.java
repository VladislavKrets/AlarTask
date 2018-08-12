package com.alarstudios.alartask;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonDataEntityArrayListDeserializer implements JsonDeserializer<List<DataEntity>> {
    @Override
    public List<DataEntity> deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String status = object.get("status").getAsString();
        if (!status.equals("ok")) return new ArrayList<>();
        JsonArray array = object.get("data").getAsJsonArray();
        List<DataEntity> dataEntities = new ArrayList<>();
        DataEntity dataEntity;
        for (JsonElement element : array) {
            dataEntity = new DataEntity();

            dataEntity.setId(element.getAsJsonObject().get("id").getAsString());
            dataEntity.setName(element.getAsJsonObject().get("name").getAsString());
            dataEntity.setCountry(element.getAsJsonObject().get("country").getAsString());
            dataEntity.setLat(element.getAsJsonObject().get("lat").getAsDouble());
            dataEntity.setLon(element.getAsJsonObject().get("lon").getAsDouble());
            dataEntities.add(dataEntity);
        }
        return dataEntities;
    }
}
