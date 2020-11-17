package br.unisc.pdm.trabalho.retrofit.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by José on 24/06/2015.
 */
public class DateTimeSerializer implements JsonSerializer<Date> {

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        SimpleDateFormat formatterTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        if(src == null)
            return null;

        return new JsonPrimitive(formatterTime.format(src));
    }
}