package edu.utboy.biteit.ui.tutorials;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import edu.utboy.biteit.R;

public class StudentIdFragment extends Fragment {

	private OnNextListener onNextListener;

	public StudentIdFragment(OnNextListener onNextListener) {
		this.onNextListener = onNextListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_studentid,
				container, false);
		final EditText stdIdText = (EditText) rootView
				.findViewById(R.id.fragment_studentid_input_text);
		Button next = (Button) rootView
				.findViewById(R.id.fragment_studentid_next_btn);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onNextListener.onNext(stdIdText.getText().toString());
			}
		});
		return rootView;
	}

	public interface OnNextListener {
		public void onNext(String stdId);
	}
}
