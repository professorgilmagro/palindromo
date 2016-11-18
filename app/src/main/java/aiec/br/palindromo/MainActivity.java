package aiec.br.palindromo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final ArrayList<Palindromo> textList = new ArrayList<Palindromo>();
    public static final int CHOOSE_GALLERY_IMAGE_CODE = 4562;
    public static final int TAKE_PHOTO_CODE = 4560;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabShare);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePalindromo(view);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivity);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        Intent intent = new Intent();

        switch (itemId){
            case R.id.nav_history:
                intent.setClass(this, HistoryListActivity.class);
                intent.putExtra("historyList", this.textList);
                startActivity(intent);
                break;

            case R.id.nav_developers:
                intent.setClass(this, DevelopersActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_github:
                Uri uri = Uri.parse(getString(R.string.github_url));
                intent.setAction(Intent.ACTION_VIEW).setData(uri);
                startActivity(intent);
                break;

            case R.id.nav_cam:
                    // Verifica se o dispositivo possui uma câmera
                    PackageManager packageManager = this.getPackageManager();
                    if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
                        Util.showMessage(getString(R.string.device_does_not_have_camera), this);
                        break;
                    }

                    Uri photoUri = Util.getAppStorageUriFrom(getString(R.string.custom_background_filename));
                    File photo = new File(photoUri.getPath());
                try {
                    photo.createNewFile();
                } catch (IOException e) {
                    Util.showMessage(getString(R.string.text_required), this);
                }

                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, TAKE_PHOTO_CODE);

                break;

            case R.id.nav_gallery:
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                    Intent.createChooser(intent, getString(R.string.select_image_action)),
                    CHOOSE_GALLERY_IMAGE_CODE
                );
                break;

            case R.id.nav_share:
            case R.id.nav_send:
                this.sharePalindromo(findViewById(android.R.id.content));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        Uri targetUri = null;
        if (requestCode == CHOOSE_GALLERY_IMAGE_CODE) {
            targetUri = data.getData();
        } else if (requestCode == TAKE_PHOTO_CODE) {
            targetUri = Util.getAppStorageUriFrom(getString(R.string.custom_background_filename));
            targetUri = Uri.fromFile(new File(targetUri.getPath()));
        }

        if (targetUri != null) {
            try {
                Drawable background = Util.createDrawable(targetUri, this);
                RelativeLayout layout = (RelativeLayout) findViewById(R.id.content_main);
                layout.setBackground(background);
            } catch (FileNotFoundException e) {
                Util.showMessage(getString(R.string.open_image_err), this);
            }
        }

    }

    /**
     * Faz a verificação do texto informado pelo usuário para determinar se é ou não um Palíndromo
     *
     * @param v
     */
    public void verifyText(View v) {
        EditText text = (EditText) findViewById(R.id.txtPhrase);
        if (Util.removeSpaces(text.getText().toString()).length() == 0) {
            String message = getString(R.string.text_required);
            Util.showMessage(message, getApplicationContext());
            text.setText("");
            return;
        }

        String textCheck = text.getText().toString();
        if (Util.getBooleanPreference(this, "accents_switch")){
            textCheck = Util.stripAccents(textCheck);
        }

        Palindromo checker = new Palindromo(textCheck);
        this.textList.add(checker);

        ImageView img = (ImageView) findViewById(R.id.imgResult);
        int imgId = R.drawable.sad;
        int msgId = R.string.resposta_incorreta;
        int soundId = R.raw.mario_snes_game_over;
        int fabVisibility = View.INVISIBLE;
        if (checker.ehPalindromo()){
            msgId = R.string.resposta_certa;
            imgId = R.drawable.happy;
            soundId = R.raw.happy_tone;
            fabVisibility = View.VISIBLE;
        }

        boolean silent = Util.getBooleanPreference(this,"silent_mode");
        String username = Util.getStringPreference(this, "display_username");
        if (!silent){
            MediaPlayer mp = MediaPlayer.create(this, soundId);
            mp.start();
        }

        img.setImageResource(imgId);
        img.setVisibility(View.VISIBLE);
        findViewById(R.id.fabShare).setVisibility(fabVisibility);

        Util.showMessage(
                String.format(getString(msgId), username).toString(),
                getApplicationContext()
        );

        text.setText("");
    }

    /**
     * Permite o compartilhamento do resultado da sequência de texto com uma descrição sobre o
     * assunto (Palíndromo) a partir dos aplicativos disponíveis para tal (WhatsApp, Telegran, Gmail)
     *
     * @param v
     */
    public void sharePalindromo(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.voce_sabe));

        String message = getString(R.string.palindromo_description);
        if (!textList.isEmpty()){
            String lastAttemptText = textList.get(textList.size()-1).getTexto();
            message += String.format("\n%s\n\n", getString(R.string.exemplo_teste));
            message += String.format("'%s'", lastAttemptText);
        }

        intent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(intent, getString(R.string.compartilhar) ));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, getString(R.string.aplicativo_nao_instalado) , Toast.LENGTH_SHORT).show();
        }
    }
}
