     com.skanderjabouzi.salat;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class CalculationActivity extends PreferenceActivity {

      @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
      }

    }
