package br.unisc.pdm.trabalho.retrofit;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;

import br.unisc.pdm.trabalho.database.model.DateTime;
import br.unisc.pdm.trabalho.database.model.Time;
import br.unisc.pdm.trabalho.retrofit.deserializer.DateDeserializer;
import br.unisc.pdm.trabalho.retrofit.deserializer.DateTimeDeserializer;
import br.unisc.pdm.trabalho.retrofit.deserializer.TimeDeserializer;
import br.unisc.pdm.trabalho.retrofit.serializer.DateSerializer;
import br.unisc.pdm.trabalho.retrofit.serializer.DateTimeSerializer;
import br.unisc.pdm.trabalho.retrofit.serializer.TimeSerializer;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;

public class ApiFactory<T> {

    static public final String API_URL = "http://jose.oneweb.com.br";

    private Class<T> mClass;

    public ApiFactory(Class<T> klass) {
        mClass = klass;
    }

    public T getService() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .registerTypeAdapter(Time.class, new TimeDeserializer())
                .registerTypeAdapter(DateTime.class, new DateTimeSerializer())
                .registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
                .serializeNulls()
                //.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("myApp"))
                .build();

        return restAdapter.create(mClass);
    }

}
