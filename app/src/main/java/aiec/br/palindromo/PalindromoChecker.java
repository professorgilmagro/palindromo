/*
 * Classe responsável por fazer a verificação do texto para determinar se o mesmo é ou não um palíndromo.
 */
package aiec.br.palindromo;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 *
 * @author GRAR
 */
public class PalindromoChecker implements Parcelable {

    /**
     * Recebe a sequência de caracteres a ser verificada
     */
    private String texto;

    /**
     * Construtor sem parametro
     */
    public PalindromoChecker(){
    }

    /**
     * Cria o objeto recebendo o texto como parâmetro
     */
    public PalindromoChecker(String texto){
        this.texto = texto;
    }

    /**
     * Recupera o objeto ao seu estado original com base num parcel
     *
     * @param parcel
     */
    private PalindromoChecker(Parcel parcel) {
        this.texto = parcel.readString();
    }

    /**
     * Implementa o atributo estático CREATOR.
     * Todas as classes que implementarem o Parcelable, devem ter esse atributo, uma vez que, ele é
     * quem junta as funcionalidades de um DataInputStream e DataOutputStream para serializar
     * e deserializar objetos.
     */
    public static final Parcelable.Creator<PalindromoChecker>
            CREATOR = new Parcelable.Creator<PalindromoChecker>() {

        public PalindromoChecker createFromParcel(Parcel in) {
            return new PalindromoChecker(in);
        }

        public PalindromoChecker[] newArray(int size) {
            return new PalindromoChecker[size];
        }
    };

    @Override
    public String toString() {
        return String.format(
            "O texto '%s' %s um palíndromo.",
            this.getTexto(),
            this.ehPalindromo() ? "é" : "não é"
        );
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.texto);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final PalindromoChecker other = (PalindromoChecker) obj;
        if (!Objects.equals(this.texto, other.texto)) {
            return false;
        }
        return true;
    }
    
    public Boolean ehPalindromo(){
        String text1 = Util.removeSpaces(this.getTexto());
        String text2 = Util.getInvertedText(text1);
        return text1.equalsIgnoreCase(text2);
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getTexto());
    }
}
