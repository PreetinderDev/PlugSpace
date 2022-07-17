package com.plugspace.retrofitApi;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.plugspace.BuildConfig;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String HEADER_KEY = "2b223e5cee713615ha54ac203b24e9a123703011VT";
    //    public static final String BASE_URL = "https://appkiduniya.in/Plugspace/api/";
    public static final String BASE_URL = "https://www.plugspace.io/api/";
    public static final String BASE_URL_WITHOUT_HTTPS = "http://appkiduniya.in/Plugspace/api/";

    public static Retrofit UserApiClient(Activity activity, final String token) {


//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        // set your desired log level
//        if (BuildConfig.DEBUG) {
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        } else {
//            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
//        }
//        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
//        builder.addInterceptor(logging);
//        builder.readTimeout(60, TimeUnit.SECONDS);
//        builder.connectTimeout(5, TimeUnit.MINUTES);
//        builder.addInterceptor(chain -> {
//            Request request;
//            if (token != null) {
//                request = chain.request().newBuilder()
//                        .addHeader("key", HEADER_KEY)
//                        .addHeader("token", token)
//                        .build();
//            } else {
//                request = chain.request().newBuilder()
//                        .addHeader("key", HEADER_KEY)
//                        .build();
//            }
//            return chain.proceed(request);
//        });
//
//        Gson gson = new GsonBuilder().setLenient().create();
//        OkHttpClient httpClient = builder.build();
//        return new Retrofit.Builder().baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson)).client(httpClient).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return getRetrofitBuilder(token, gson);
    }

    //// base url with https
    private static Retrofit getRetrofitBuilder(String token, Gson gson) {
        try {
            //// Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            //// Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory(); // : Info: base url https to uncomment this line.

            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(logging);
            }
//
            builder.readTimeout(10, TimeUnit.MINUTES);
            builder.connectTimeout(10, TimeUnit.MINUTES);

//            builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
//                    .writeTimeout(5, TimeUnit.MINUTES) // write timeout
//                    .readTimeout(5, TimeUnit.MINUTES); // read timeout

//            builder.connectTimeout(10, TimeUnit.SECONDS)
//                    .writeTimeout(10, TimeUnit.SECONDS)
//                    .readTimeout(30, TimeUnit.SECONDS);

            builder.addInterceptor(chain -> {
                Request request;

//                String token = Preferences.GetValues(Preferences.keyToken);


                if (token != null) {

                    request = chain.request().newBuilder()
                            .addHeader("key", HEADER_KEY)
                            .addHeader("token", token)
                            .build();
                } else {
                    request = chain.request().newBuilder()
                            .addHeader("key", HEADER_KEY)
                            .build();
                }

                return chain.proceed(request);

            });

//            builder.sslSocketFactory(sslSocketFactory);

//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//                builder.sslSocketFactory(sslSocketFactory);
//            }

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);

            builder.hostnameVerifier((hostname, session) -> true);

//            return builder.build();

            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(builder.build())
                    .build();

        } catch (Exception e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return getRetrofitBuilderWithoutHttps(token, gson);
    }

    private static Retrofit getRetrofitBuilderWithoutHttps(String token, Gson gson) {
        //// this case use without https base url
        //// base url without https

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        builder.readTimeout(120, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.MINUTES);


        builder.addInterceptor(chain -> {
            Request request;

//            String token = Preferences.GetValues(Preferences.keyToken);


            if (token != null) {

                request = chain.request().newBuilder()
                        .addHeader("key", HEADER_KEY)
                        .addHeader("token", token)
                        .build();
            } else {
                request = chain.request().newBuilder()
                        .addHeader("key", HEADER_KEY)
                        .build();
            }

            return chain.proceed(request);
        });

//        return builder.build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL_WITHOUT_HTTPS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();
    }
}
