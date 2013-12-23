package org.openintents.shopping.glassware;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private List<Card> mCards;
    private CardScrollView mCardScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if ("android.intent.action.OPEN_URI".equals(action)) {
            createCards(intent.getData());

            mCardScrollView = new CardScrollView(this);
            ExampleCardScrollAdapter adapter = new ExampleCardScrollAdapter();
            mCardScrollView.setAdapter(adapter);
            mCardScrollView.activate();
            setContentView(mCardScrollView);
        }

    }

    private void createCards(Uri data) {
        mCards = new ArrayList<Card>();

        Card card;

        card = new Card(this);
        card.setText(data.toString());
        mCards.add(card);

    }

    private class ExampleCardScrollAdapter extends CardScrollAdapter {
        @Override
        public int findIdPosition(Object id) {
            return -1;
        }

        @Override
        public int findItemPosition(Object item) {
            return mCards.indexOf(item);
        }

        @Override
        public int getCount() {
            return mCards.size();
        }

        @Override
        public Object getItem(int position) {
            return mCards.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mCards.get(position).toView();
        }

    }
}
