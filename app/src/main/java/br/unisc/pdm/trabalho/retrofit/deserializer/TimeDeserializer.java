package br.unisc.pdm.trabalho.retrofit.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.unisc.pdm.trabalho.database.model.Time;

/**
 * Created by José on 24/06/2015.
 */
public class TimeDeserializer implements JsonDeserializer<Time> {

    @Override
    public Time deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String myDate = json.getAsString();
        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
        try {
            return new Time(formatterTime.parse(myDate));
        } catch(ParseException e) {
            return null;
        }
    }
}