package edu.utboy.biteit.ui.pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import edu.utboy.biteit.R;

public class RatingFragment extends Fragment {

	public final static String RATING_CLAEN = "rating_clean";
	public final static String RATING_SERVICE = "rating_service";
	public final static String RATING_ATMOS = "rating_atmos";
	public final static String RATING_FAVOR = "rating_favor";
	public final static String RATING_UNKNOW = "rating_unknow";

	private View rootView;
	private RatingBar cleanRatingBar;
	private RatingBar serviceRatingBar;
	private RatingBar atmosRatingBar;
	private RatingBar favorRatingBar;
	private RatingBar unknowRatingBar;
	private Bundle ratingData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ratingData = getArguments();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_rating, container, false);

		cleanRatingBar = (RatingBar) rootView
				.findViewById(R.id.feedback_ratingbar_clean);
		serviceRatingBar = (RatingBar) rootView
				.findViewById(R.id.feedback_ratingbar_service);
		atmosRatingBar = (RatingBar) rootView
				.findViewById(R.id.feedback_ratingbar_atmos);
		favorRatingBar = (RatingBar) rootView
				.findViewById(R.id.feedback_ratingbar_favor);
		unknowRatingBar = (RatingBar) rootView
				.findViewById(R.id.feedback_ratingbar_unknow);

		cleanRatingBar.setOnRatingBarChangeListener(ratingBarChangeListener);
		serviceRatingBar.setOnRatingBarChangeListener(ratingBarChangeListener);
		atmosRatingBar.setOnRatingBarChangeListener(ratingBarChangeListener);
		favorRatingBar.setOnRatingBarChangeListener(ratingBarChangeListener);
		unknowRatingBar.setOnRatingBarChangeListener(ratingBarChangeListener);

		cleanRatingBar.setRating(ratingData.getFloat(RATING_CLAEN, 0));
		serviceRatingBar.setRating(ratingData.getFloat(RATING_SERVICE, 0));
		atmosRatingBar.setRating(ratingData.getFloat(RATING_ATMOS, 0));
		favorRatingBar.setRating(ratingData.getFloat(RATING_FAVOR, 0));
		unknowRatingBar.setRating(ratingData.getFloat(RATING_UNKNOW, 0));

		return rootView;
	}

	public Bundle getRatingData() {
		return ratingData;
	}

	private OnRatingBarChangeListener ratingBarChangeListener = new OnRatingBarChangeListener() {

		@Override
		public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
			switch (ratingBar.getId()) {
			case R.id.feedback_ratingbar_clean:
				ratingData.putFloat(RATING_CLAEN, rating);
				break;
			case R.id.feedback_ratingbar_service:
				ratingData.putFloat(RATING_SERVICE, rating);
				break;
			case R.id.feedback_ratingbar_atmos:
				ratingData.putFloat(RATING_ATMOS, rating);
				break;
			case R.id.feedback_ratingbar_favor:
				ratingData.putFloat(RATING_FAVOR, rating);
				break;
			case R.id.feedback_ratingbar_unknow:
				ratingData.putFloat(RATING_UNKNOW, rating);
				break;
			}
		}
	};

}
