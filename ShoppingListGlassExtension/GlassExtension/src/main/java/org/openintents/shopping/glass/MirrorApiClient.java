package org.openintents.shopping.glass;

import android.content.Context;
import android.os.Handler;

import com.google.api.client.util.IOUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MirrorApiClient {
    private static final String BASE_URL = "https://www.googleapis.com/mirror/v1/";
    private static final String TOKENINFO_URL = "https://www.googleapis.com/oauth2/v1/tokeninfo";
    private static final int CONNECT_TIMEOUT = 2500;
    private static final int REQUEST_TIMEOUT = 5000;
    private static MirrorApiClient sInstance;
    private Handler mHandler;
    private ExecutorService mThreadPool;
    private DefaultHttpClient mClient;

    private MirrorApiClient(Context context) {
        mHandler =  new Handler(context.getMainLooper());
        mThreadPool = Executors.newCachedThreadPool();
        mClient = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(
                mClient.getParams(), CONNECT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(
                mClient.getParams(), REQUEST_TIMEOUT);
    }

    public static MirrorApiClient getInstance(Context context) {
        if (sInstance == null) {
            synchronized (MirrorApiClient.class) {
                if (sInstance == null) {
                    sInstance = new MirrorApiClient(context);
                }
            }
        }
        return sInstance;
    }

    public void createTimelineItem(String token, JSONObject json,
            final Callback callback) {
        try {
            final HttpPost request = new HttpPost();
            request.setURI(new URI(BASE_URL + "timeline"));
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Authorization", String.format("Bearer %s", token));
            request.setEntity(new StringEntity(json.toString()));

            // Execute the request on a background thread
            mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final HttpResponse response = mClient.execute(request);
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            InputStream inputStream = response.getEntity().getContent();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            IOUtils.copy(inputStream, baos);
                            final JSONObject jsonObject = new JSONObject(baos.toString());

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(response, jsonObject);
                                }
                            });
                        } else {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure(response, null);
                                }
                            });
                        }
                    } catch (final IOException e) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure(null, e);
                            }
                        });
                    } catch (final JSONException e) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure(null, e);
                            }
                        });
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            // Note: This should never happen
        } catch (URISyntaxException e) {
            // Note: This should never happen
        }
    }

    public void updateTimelineItem(String token, JSONObject json,
                                   final Callback callback, String id) {
        try {
            final HttpPut request = new HttpPut();
            request.setURI(new URI(BASE_URL + "timeline/" + id));
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Authorization", String.format("Bearer %s", token));
            request.setEntity(new StringEntity(json.toString()));

            // Execute the request on a background thread
            mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final HttpResponse response = mClient.execute(request);
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            InputStream inputStream = response.getEntity().getContent();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            IOUtils.copy(inputStream, baos);
                            final JSONObject jsonObject = new JSONObject(baos.toString());

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(response, jsonObject);
                                }
                            });
                        } else {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure(response, null);
                                }
                            });
                        }
                    } catch (final IOException e) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure(null, e);
                            }
                        });
                    } catch (final JSONException e) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure(null, e);
                            }
                        });
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            // Note: This should never happen
        } catch (URISyntaxException e) {
            // Note: This should never happen
        }
    }

    public void validateToken(String token,
                              final Callback callback) {
        try {
            final HttpGet request = new HttpGet();
            request.setURI(new URI(TOKENINFO_URL + "?access_token=" + token));

            // Execute the request on a background thread
            mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final HttpResponse response = mClient.execute(request);
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(response,null);
                                }
                            });
                        } else {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure(response, null);
                                }
                            });
                        }
                    } catch (final IOException e) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure(null, e);
                            }
                        });
                    }
                }
            });
        } catch (URISyntaxException e) {
            // Note: This should never happen
        }
    }

    public static interface Callback {
        public void onSuccess(HttpResponse response, JSONObject jsonObject);
        public void onFailure(HttpResponse response, Throwable e);
    }
}
