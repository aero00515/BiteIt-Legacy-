package edu.utboy.biteit.asynctasks.requestqueues;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * EXAMPLE: <br>
 * <code>
 * // Get a RequestQueue <br>
 *	RequestQueue queue = SampleRequestQueue.getSampleRequestQueue(this.getApplicationContext()).<br>
 *	getRequestQueue();<br>
 *	<br>
 *	// Add a request (in this example, called stringRequest) to your RequestQueue.<br>
 *	SampleRequestQueue.getSampleRequestQueue(this).addToRequestQueue(stringRequest);<br>
 *	</code>
 */
public class StoreInfoRequestQueue {

	private static StoreInfoRequestQueue mSampleRequestQueue;
	private static Context context;
	private RequestQueue mRequestQueue;

	private StoreInfoRequestQueue(Context context) {
		StoreInfoRequestQueue.context = context;
	}

	public static synchronized StoreInfoRequestQueue getSampleRequestQueue(
			Context context) {
		if (mSampleRequestQueue == null) {
			mSampleRequestQueue = new StoreInfoRequestQueue(context);
		}
		return mSampleRequestQueue;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(context
					.getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}
}
