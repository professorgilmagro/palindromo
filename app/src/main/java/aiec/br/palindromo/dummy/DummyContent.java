package aiec.br.palindromo.dummy;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aiec.br.palindromo.Palindromo;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    public DummyContent(List<Parcelable> checkers){
        ITEMS.clear();
        Integer index = 0;
        for (Parcelable item: checkers){
            index++;
            addItem(createDummyItem((Palindromo) item, index.toString()));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(Palindromo item, String index) {
        return new DummyItem(index, item.getTexto().toString(), makeDetails(item));
    }

    private static String makeDetails(Palindromo item) {
        StringBuilder builder = new StringBuilder();
        builder.append("Detalhes da verificação: ");
        builder.append(String.format("\n%s", item));

        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
