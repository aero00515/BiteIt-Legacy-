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
public class PhotoCommentRequestQueue {

	private static PhotoCommentRequestQueue mSampleRequestQueue;
	private static Context context;
	private RequestQueue mRequestQueue;

	private PhotoCommentRequestQueue(Context context) {
		PhotoCommentRequestQueue.context = context;
	}

	public static synchronized PhotoCommentRequestQueue getSampleRequestQueue(
			Context context) {
		if (mSampleRequestQueue == null) {
			mSampleRequestQueue = new PhotoCommentRequestQueue(context);
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
