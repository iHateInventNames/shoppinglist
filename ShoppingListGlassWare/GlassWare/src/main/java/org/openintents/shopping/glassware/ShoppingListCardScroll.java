package org.openintents.shopping.glassware;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListCardScroll extends Activity {

    private List<Card> mCards;
    private CardScrollView mCardScrollView;
    private ShoppingListCardScrollAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createTitleCard();

        initCardsUI();

        setContentView(mCardScrollView);

        Intent intent = getIntent();
        String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            createCards(intent.getData().getPath());

        }

    }

    private void createTitleCard() {
        Card card = new Card(this);
        card.setImageLayout(Card.ImageLayout.FULL);
        card.addImage(R.drawable.ic_mrt_bg);
        card.setText("Shopping List");
        mCards.add(card);
    }

    private void initCardsUI() {
        adapter = new ShoppingListCardScrollAdapter();
        mCards = new ArrayList<Card>();
        mCardScrollView = new CardScrollView(this);
        mCardScrollView.setAdapter(adapter);
        mCardScrollView.activate();
    }

    private void createCards(String data) {
        Card card = new Card(this);
        card.setText(data.toString());
        mCards.add(card);
    }

    private class ShoppingListCardScrollAdapter extends CardScrollAdapter {
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
