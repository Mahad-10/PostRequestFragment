package pk.codebase.postrequestfragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Name)){
            FragmentMain fragmentMain = new FragmentMain();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_view,fragmentMain)
                    .commit();
        }else {
            FragmentSignup fragmentSignup = new FragmentSignup();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_view, fragmentSignup)
                    .commit();
        }
    }
}