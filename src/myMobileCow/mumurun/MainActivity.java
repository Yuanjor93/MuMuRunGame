package myMobileCow.mumurun;

import MobileCow.mumurun.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
/***
 * This is the Starting Menu for the game
 * @author Jordan
 *
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setText("Start");
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// start new activity
				Intent i = new Intent();
				i.setClass(MainActivity.this, GameActivity.class);
				startActivity(i);
			}
		});

		Button btnHelp = (Button) findViewById(R.id.btnHelp);
		btnHelp.setText("Help");
		btnHelp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showMessage("Tap anywhere to jump!");
			}
		});

		Button btnAbout = (Button) findViewById(R.id.btnAbout);
		btnAbout.setText("About");
		btnAbout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showMessage("A moodiocre cow game by Jordan Yuan.");
			}
		});

		Button btnExit = (Button) findViewById(R.id.btnExit);
		btnExit.setText("Exit");
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
				System.exit(0);
			}
		});

	}

	private void showMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	/**Intent i = new Intent(MainActivity.this, GameActivity.class);
	startActivity(i);

	 **/
}