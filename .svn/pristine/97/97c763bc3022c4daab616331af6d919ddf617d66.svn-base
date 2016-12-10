package edu.utboy.biteit.ui.pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import edu.utboy.biteit.R;

public class CommentFragment extends Fragment {

	private String comment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_comment, container,
				false);
		final EditText commentEdit = (EditText) rootView
				.findViewById(R.id.fragment_comment_edit);
		commentEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				comment = s.toString();
			}
		});
		return rootView;
	}

	public String getComment() {
		return comment;
	}

}
