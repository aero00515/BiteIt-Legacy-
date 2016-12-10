package edu.utboy.biteit.utils;

import android.os.Bundle;
import android.widget.ListView;
import edu.utboy.biteit.R;

public class ListActivity extends BaseActivity {
	private ListView mListView;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mylist);
		mListView = (ListView) findViewById(R.id.activity_mylist_listview);
	}

	public ListView getListView() {
		return mListView;
	}

	protected MyListAdapter createListAdapter() {
		return new MyListAdapter(this);
	}
}
