package edu.utboy.biteit.ui.drawer;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import edu.utboy.biteit.R;
import edu.utboy.biteit.utils.BaseActivity;

public class SettingsActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FadingActionBarHelper helper = new FadingActionBarHelper()
				.actionBarBackground(new ColorDrawable(R.color.action_bar))
				.headerLayout(R.layout.settings_header)
				.contentLayout(R.layout.activity_settings);
		setContentView(helper.createView(this));
		helper.initActionBar(this);
		// setContentView(R.layout.activity_settings);
		String[] data = new String[] { getString(R.string.settings_aboutus),
				getString(R.string.settings_report),
				getString(R.string.settings_privacy) };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.settings_list_style, data);
		ListView settingsList = (ListView) findViewById(R.id.settings_list);
		settingsList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		settingsList.setOnItemClickListener(listener);
	}

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
				// About Us
				break;
			case 1:
				// Report of advises
				break;
			case 2:
				// Privacy & Terms
				break;
			default:
			}
		}
	};
}
