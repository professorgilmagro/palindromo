/*
* Classe principal do aplicativo que aceita como entrada uma
* String qualquer e consiga contar a quantidade de cada símbolo que a compõem
*/

package aiec.br.palindromo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.Toast;

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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }
}

