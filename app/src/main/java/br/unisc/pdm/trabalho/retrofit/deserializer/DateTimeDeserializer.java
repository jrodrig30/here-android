package br.unisc.pdm.trabalho.retrofit.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import br.unisc.pdm.trabalho.database.model.DateTime;

/**
 * Created by José on 24/06/2015.
 */
public class DateTimeDeserializer implements JsonDeserializer<DateTime> {

    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String myDate = json.getAsString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            return new DateTime(formatter.parse(myDate));
        } catch(ParseException ex) {
            return null;
        }
    }
}