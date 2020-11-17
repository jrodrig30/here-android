package br.unisc.pdm.cliente.ws;

import retrofit.RestAdapter;

public class ApiFactory<T> {

    static public final String API_URL = "http://jose.oneweb.com.br";

    private Class<T> mClass;

    public ApiFactory(Class<T> klass) {
        mClass = klass;
    }

    public T getService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();

        return restAdapter.create(mClass);
    }

}
