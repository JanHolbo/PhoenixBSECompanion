package dk.kaddu.phoenixbsecompanion;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Implementation of headless Fragment that runs an AsyncTask to fetch data from the network.
 */
public class NetworkFragment extends Fragment {
    public static final String TAG = "NetworkFragment";
    private static final String LOG_TAG = "NetworkFragment";

    private static final String URL_KEY = "UrlKey";

    private DownloadCallback mCallback;
    private DownloadTask downloadTask;
    private String urlString;

    /**
     * Static initializer for NetworkFragment that sets the URL of the host it will be downloading
     * from.
     */
    public static NetworkFragment getInstance(FragmentManager fragmentManager, String url) {
        Log.d(LOG_TAG,"static getInstance() url="+url);
        NetworkFragment networkFragment = new NetworkFragment();
        Bundle args = new Bundle();
        args.putString(URL_KEY, url);
        networkFragment.setArguments(args);
        fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        return networkFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        urlString = getArguments().getString(URL_KEY);
        Log.d(LOG_TAG,"onCreate() urlString="+urlString);
        // TODO add needed code to onCreate()
    }

    @Override
    public void onAttach(Context context) {
        Log.d(LOG_TAG,"onAttach()");
        super.onAttach(context);
        // Host Activity will handle callbacks from task.
        mCallback = (DownloadCallback<String>) context;
    }

    @Override
    public void onDetach() {
        Log.d(LOG_TAG,"onDetach()");
        super.onDetach();
        // Clear reference to host Activity to avoid memory leak.
        mCallback = null;
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG,"onDestroy()");
        // Cancel task when Fragment is destroyed.
        cancelDownload();
        super.onDestroy();
    }

    /**
     * Start non-blocking execution of DownloadTask.
     */
    public void startDownload() {
        Log.d(LOG_TAG,"startDownload()");
        cancelDownload();
        downloadTask = new DownloadTask(mCallback);
        downloadTask.execute(urlString);
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    public void cancelDownload() {
        Log.d(LOG_TAG,"cancelDownload()");
        if (downloadTask != null) {
            downloadTask.cancel(true);
        }
    }

    // TODO add needed code to NetworkFragment

    /**
     * Implementation of AsyncTask designed to fetch data from the network.
     */
    private class DownloadTask extends AsyncTask<String, Integer, DownloadTask.Result> {

        private DownloadCallback<String> mCallback;

        DownloadTask(DownloadCallback<String> callback) {
            setCallback(callback);
        }

        void setCallback(DownloadCallback<String> callback) {
            mCallback = callback;
        }

        /**
         * Wrapper class that serves as a union of a result value and an exception. When the download
         * task has completed, either the result value or exception can be a non-null value.
         * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
         */
        class Result {
            public String mResultValue;
            public Exception mException;
            public Result(String resultValue) {
                mResultValue = resultValue;
            }
            public Result(Exception exception) {
                mException = exception;
            }
        }

        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        @Override
        protected void onPreExecute() {
            Log.d(LOG_TAG,"onPreExecute()");
            if (mCallback != null) {
                NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                    // If no connectivity, cancel task and update Callback with null data.
                    Log.d(LOG_TAG,"onPreExecute(): no connection - cancel the download");
                    mCallback.updateFromDownload(null);
                    cancel(true);
                }
            }
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected DownloadTask.Result doInBackground(String... urls) {
            Log.d(LOG_TAG,"DownloadTask.Result doInBackground() urls="+urls);
            Result result = null;
            Log.d(LOG_TAG,"DownloadTask.Result doInBackground() (0) isCancelled()="+isCancelled()+" urls="+urls+" urls.length="+urls.length);
            // TODO isCancelled() does not exist? Find out where it has gone.
            if (!isCancelled() && urls != null && urls.length > 0) {
                Log.d(LOG_TAG,"DownloadTask.Result doInBackground() (1)");
                if (urls != null && urls.length > 0) {
                    Log.d(LOG_TAG,"DownloadTask.Result doInBackground() (2) urls[0]="+urls[0]);
                    String urlString = urls[0];
                    Log.d(LOG_TAG,"DownloadTask.Result doInBackground() (3) urlString="+urlString);
                    try {
                        Log.d(LOG_TAG,"DownloadTask.Result doInBackground() (4) urlString="+urlString);
                        URL url = new URL(urlString);
                        Log.d(LOG_TAG,"DownloadTask.Result doInBackground() (5) url="+url);
                        String resultString = parseGameStatusXml(url);
                        if (resultString != null) {
                            result = new Result(resultString);
                        } else {
                            Log.d(LOG_TAG,"DownloadTask.Result doInBackground() throw IOException: No response received.");
                            throw new IOException("No response received.");
                        }
                    } catch(Exception e) {
                        Log.d(LOG_TAG,"DownloadTask.Result doInBackground() exception: "+e.toString());

                        result = new Result(e);
                    }
                }
            }
            return result;
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(Result result) {
            Log.d(LOG_TAG,"onPostExecute()");
            if (result != null && mCallback != null) {
                if (result.mException != null) {
                    mCallback.updateFromDownload(result.mException.getMessage());
                } else if (result.mResultValue != null) {
                    mCallback.updateFromDownload(result.mResultValue);
                }
                mCallback.finishDownloading();
            }
        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(Result result) {
        }

        /**
         * Given a URL, sets up a connection and gets the HTTP response body from the server.
         * If the network request is successful, it returns the response body in String form. Otherwise,
         * it will throw an IOException.
         */
        private String parseGameStatusXml(URL url) throws IOException {
            Log.d(LOG_TAG,"parseGameStatusXml()");
            InputStream stream = null;
            HttpsURLConnection connection = null;
            String result = null;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(3000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(3000);
                // For this use case, set HTTP method to GET.
                connection.setRequestMethod("GET");
                // Already true by default but setting just in case; needs to be true since this request
                // is carrying an input (response) body.
                connection.setDoInput(true);
                // Open communications link (network traffic occurs here).
                connection.connect();
                publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                // Retrieve the response body as an InputStream.
                stream = connection.getInputStream();
                publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
                if (stream != null) {
                    // Converts Stream to String with max length of 500.
                    result = readStream(stream, 500);
                }
            } finally {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        // TODO remove or adapt readStream to our own use
        /**
         * Converts the contents of an InputStream to a String.
         * From: android-NetworkConnect
         */
        private String readStream(InputStream stream, int maxLength) throws IOException {
            String result = null;
            String star_date="Not Available";
            String status="Not Available";

            Log.d(LOG_TAG,"readStream()");

// TODO replace the former stream read code from the android-NetworkConnect example with XML parsing code
/* left over code from android-NetworkConnect - TO BE REMOVED

            // Read InputStream using the UTF-8 charset.
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");

            // Create temporary buffer to hold Stream data with specified max length.
            char[] buffer = new char[maxLength];
            // Populate temporary buffer with Stream data.
            int numChars = 0;
            int readSize = 0;
            while (numChars < maxLength && readSize != -1) {
                numChars += readSize;
                int pct = (100 * numChars) / maxLength;
                publishProgress(DownloadCallback.Progress.PROCESS_INPUT_STREAM_IN_PROGRESS, pct);
                readSize = reader.read(buffer, numChars, buffer.length - numChars);
            }
            if (numChars != -1) {
                // The stream was not empty.
                // Create String that is actual length of response body if actual length was less than
                // max length.
                numChars = Math.min(numChars, maxLength);
                result = new String(buffer, 0, numChars);
            }
  left over code from android-NetworkConnect - TO BE REMOVED */

            XmlPullParserFactory parserFactory;
            try {
                Log.d(LOG_TAG,"readStream(): Instantiating XmlPullParserFactory");
                parserFactory = XmlPullParserFactory.newInstance();
                Log.d(LOG_TAG,"readStream(): Instantiating XmlPullParser");
                XmlPullParser parser = parserFactory.newPullParser();
                Log.d(LOG_TAG,"readStream(): Setting XmlPullParser feature");
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                Log.d(LOG_TAG,"readStream(): Setting XmlPullParser input");
                parser.setInput(stream, null);

                Log.d(LOG_TAG,"readStream(): Parsing game_status XML");

                int event = parser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT)  {
                    String name=parser.getName();
                    switch (event){
                        case XmlPullParser.START_TAG:
                            Log.d(LOG_TAG,"readStream(): XML start tag name: " + name);
                            switch (name) {
                                case "status":
                                    Log.d(LOG_TAG,"readStream(): Setting status value");
                                    status = parser.nextText();
                                    Log.d(LOG_TAG,"readStream(): Setting status value = " + status);
                                    break;
                                case "star_date":
                                    Log.d(LOG_TAG,"readStream(): Setting star_date value");
                                    star_date = parser.nextText();
                                    Log.d(LOG_TAG,"readStream(): Setting star_date value = " + star_date);
                                    break;
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            Log.d(LOG_TAG,"readStream(): XML end tag name: " + name);
                            break;
                    }
                    Log.d(LOG_TAG,"readStream(): Next XML element");
                    event = parser.next();
                }
            } catch (XmlPullParserException e) {
                Log.d(LOG_TAG,"readStream(): Parsing XML -> XmlPullParserException : " + e.toString());
            } catch (Exception e) {
                Log.d(LOG_TAG,"readStream(): Parsing XML -> other Exception : " + e.toString());
            }

            return result;
        }

    }
}
