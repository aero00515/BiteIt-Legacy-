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
public class SampleRequestQueue {

	private static SampleRequestQueue mSampleRequestQueue;
	private static Context context;
	private RequestQueue mRequestQueue;

	private SampleRequestQueue(Context context) {
		SampleRequestQueue.context = context;
	}

	public static synchronized SampleRequestQueue getSampleRequestQueue(
			Context context) {
		if (mSampleRequestQueue == null) {
			mSampleRequestQueue = new SampleRequestQueue(context);
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
