package com.example.videotranslator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.videotranslator.MVP.Views.SelectionOfFilmFragment;
import com.google.android.material.navigation.NavigationView;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String APP_PREFERENCES = "mirror";
    public static final String APP_PREFERENCES_MIRROR = "https://hdrezkawer.org";
    SharedPreferences mirror;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mirror=getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        SelectionOfFilmFragment fragmentContainer = new SelectionOfFilmFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mirror",mirror.getString(APP_PREFERENCES_MIRROR,APP_PREFERENCES_MIRROR));
        bundle.putString("title", "Новинки");
        bundle.putString("page","/new/page/");
        bundle.putInt("numberPage",1);
        fragmentContainer.setArguments(bundle);
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, fragmentContainer).commit();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        SelectionOfFilmFragment fragmentContainer = new SelectionOfFilmFragment();
        Bundle bundle = new Bundle();
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        if (id == R.id.search) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Поиск по сайту");
            EditText input = new EditText(this);
            input.setHint("Введите запрос");
            alert.setView(input);

            alert.setPositiveButton("Ок", (dialog, whichButton) -> {
                bundle.putString("mirror",mirror.getString(APP_PREFERENCES_MIRROR,APP_PREFERENCES_MIRROR));
                bundle.putString("title", "Результаты поиска");
                bundle.putString("page","/engine/ajax/search.php");
                bundle.putString("q",input.getText().toString());
                drawer.closeDrawer(GravityCompat.START);

                fragmentContainer.setArguments(bundle);
                ft.replace(R.id.frame_layout, fragmentContainer).commit();
            });

            alert.setNegativeButton("Отмена", (dialog, whichButton) -> dialog.cancel());

            alert.show();
        } else if (id == R.id.set_mirror) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Сменить зеркало");
            EditText input = new EditText(this);
            input.setHint("Любое письмо на mirror@hdrezka.org");
            alert.setView(input);

            alert.setPositiveButton("Ок", (dialog, whichButton) -> {
                SharedPreferences.Editor editor = mirror.edit();
                String tmp=input.getText().toString();
                if(tmp.startsWith("https://")) {
                    editor.putString(APP_PREFERENCES_MIRROR,tmp );
                    editor.apply();
                    bundle.putString("mirror",mirror.getString(APP_PREFERENCES_MIRROR,APP_PREFERENCES_MIRROR));
                    bundle.putString("title", "Новинки");
                    bundle.putString("page", "/new/page/");
                    bundle.putInt("numberPage",1);
                    drawer.closeDrawer(GravityCompat.START);
                    fragmentContainer.setArguments(bundle);
                    ft.replace(R.id.frame_layout, fragmentContainer).commit();
                }
            });

            alert.setNegativeButton("Отменить и узнать текущее", (dialog, whichButton) -> {
                Toast.makeText(getApplicationContext(), mirror.getString(APP_PREFERENCES_MIRROR,APP_PREFERENCES_MIRROR), Toast.LENGTH_SHORT).show();
                dialog.cancel();
            });

            alert.show();
        }
        else {
            bundle.putString("mirror",mirror.getString(APP_PREFERENCES_MIRROR,APP_PREFERENCES_MIRROR));
            if (id == R.id.news) {
                bundle.putString("title", "Новинки");
                bundle.putString("page", "/new/page/");
                bundle.putInt("numberPage",1);
            } else if (id == R.id.films) {
                bundle.putString("title", "Фильмы");
                bundle.putString("page", "/films/page/");
                bundle.putInt("numberPage",1);
            } else if (id == R.id.Series) {
                bundle.putString("title", "Сериалы");
                bundle.putString("page", "/series/page/");
                bundle.putInt("numberPage",1);
            } else if (id == R.id.cartoons) {
                bundle.putString("title", "Мультфильмы");
                bundle.putString("page", "/cartoons/page/");
                bundle.putInt("numberPage",1);
            } else if (id == R.id.animation) {
                bundle.putString("title", "Аниме");
                bundle.putString("page", "/animation/page/");
                bundle.putInt("numberPage",1);
            }
            drawer.closeDrawer(GravityCompat.START);
            fragmentContainer.setArguments(bundle);
            ft.replace(R.id.frame_layout, fragmentContainer).commit();
        }
        return true;
    }
}