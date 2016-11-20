/*
* Classe principal do aplicativo que aceita como entrada uma
* String qualquer e consiga contar a quantidade de cada símbolo que a compõem
*/

package aiec.br.palindromo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Normalizer;

/**
 * Centraliza métodos úteis para utilização no aplicativo
 *
 * @author Grupo GRA (Anne, Gilmar e Ricardo) <aiec.br>
 */
public class Util {

    /**
     * Método auxiliar para remover acentos de string
     * 
     * @param texto
     * 
     * @return String
     */
    public static String stripAccents( String texto ) {
       texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
       return texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

    /**
     * Retorna um texto na sua forma inversa
     *
     * @param text  Sequência de caracteres a ser invertida
     *
     * @return String
     */
    public static String getInvertedText(String text){
        return new StringBuilder(text).reverse().toString();
    }

    /**
     * Remove o texto informado sem espaços
     *
     * @param text  Texto
     *
     * @return String
     */
    public static String removeSpaces(String text){
        return text.replaceAll("\\s+", "");
    }

    /**
     * Exibe uma mensagem de tela usando o Toast
     *
     * @param text  Texto
     */
    public static void showMessage(String text, Context context){
        int duracao = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duracao);
        toast.show();
    }

    /**
     * Exibe uma mensagem do tipo alert
     *
     * @param context   Contexto
     * @param message   Mensagem
     * @param title     Título
     */
    public static void alert(Context context, String message, String title){
        AlertDialog.Builder alert = Util.creatAlert(context, message, title);
        alert.show();
    }

    /**
     * Cria uma mensagem do tipo alert
     *
     * @param context   Contexto
     * @param message   Mensagem
     * @param title     Título
     *
     * @return  AlertDialog
     */
    public static AlertDialog.Builder creatAlert(Context context, String message, String title){
        AlertDialog.Builder alert = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert);

        return alert;
    }

    /**
     * Executa o áudio informado
     */
    public static void executeSound(Context context, int soundId) {
        final MediaPlayer mp = MediaPlayer.create(context, soundId);
        mp.start();
    }

    /**
     * Retorna o valor de uma configuração de preferência do app
     *
     * @param context   contexto
     * @param key       Chave da preferência
     *
     * @return  boolean
     */
    public static boolean getBooleanPreference(Context context, String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }

    /**
     * Retorna o valor de uma configuração de preferência do app
     *
     * @param context   contexto
     * @param key       Chave da preferência
     *
     * @return  string
     */
    public static String getStringPreference(Context context, String key){
        return Util.getStringPreference(context, key, "");
    }

    /**
     * Retorna o valor de uma configuração de preferência do app
     *
     * @param context   contexto
     * @param key       Chave da preferência
     *
     * @return  string
     */
    public static String getStringPreference(Context context, String key, String defaultValue){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defaultValue);
    }

    /**
     * Retorna o diretório padrão de armazenamento do app
     *
     * @return  string
     */
    public static String getAppStorageDir(Boolean createIfNotExists){
        String dir = Util.getAppStorageDir();

        if (createIfNotExists){
            File profileDir = new File(dir);
            if (!profileDir.exists()){
                profileDir.mkdirs();
            }
        }

        return dir;
    }

    /**
     * Retorna o diretório padrão de armazenamento do app
     *
     * @return  string
     */
    public static String getAppStorageDir(){
        return String.format("%s/palindromo/", Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    /**
     * Retorna a URI do arquivo com base no diretório padrão da aplicação
     *
     * @return  Uri
     */
    public static Uri getAppStorageUriFrom(String filename){
        return Uri.withAppendedPath(Uri.parse(Util.getAppStorageDir()), filename);
    }

    /**
     * Dada uma URI de uma imagem, retorna um Drawable
     *
     * @param targetUri
     * @param context
     *
     * @return
     * @throws FileNotFoundException
     */
    public static Drawable createDrawable(Uri targetUri, Context context) throws FileNotFoundException {
        Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(targetUri));
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, display.getWidth(), display.getHeight(), true);

        return new BitmapDrawable(context.getResources(), newBitmap);
    }

    /**
     * Dada uma URI de uma imagem, retorna um Drawable
     *
     * @param bitmap
     * @param context
     *
     * @return
     * @throws FileNotFoundException
     */
    public static Drawable createDrawable(Bitmap bitmap, Context context) throws FileNotFoundException {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, display.getWidth(), display.getHeight(), true);

        return new BitmapDrawable(context.getResources(), newBitmap);
    }

    public static File createAppFile(String filename) throws IOException {
        Uri fileUri = Util.getAppStorageUriFrom(filename);
        File file = new File(fileUri.getPath());
        file.createNewFile();
        return file;
    }

    /**
     * This copies the actual file from our input to our output stream.
     * @param in
     * @param out
     * @throws IOException
     */
    public static void copyFile(InputStream in, FileOutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}

