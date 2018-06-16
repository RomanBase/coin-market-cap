package com.ankhrom.coinmarketcap.data;

import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.ankhrom.base.networking.volley.RequestBuilder;
import com.ankhrom.base.networking.volley.ResponseListener;
import com.ankhrom.coinmarketcap.entity.TokenItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireToken {

    final RequestQueue queue;

    public FireToken(@NonNull RequestQueue queue) {
        this.queue = queue;
    }

    public void tokenInfo(final String contract, final ResponseListener<TokenItem> listener) {

        FirebaseDatabase.getInstance()
                .getReference("token")
                .child(contract)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!dataSnapshot.exists()) {
                            tokenInfoAbi(contract, listener);
                            return;
                        }

                        TokenItem token = dataSnapshot.getValue(TokenItem.class);
                        listener.onResponse(token);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onErrorResponse(new VolleyError(databaseError.toException()));
                    }
                });
    }

    public void tokenInfoAbi(final String contract, final ResponseListener<TokenItem> listener) {

        RequestBuilder.get("https://us-central1-coin-market-portfolio.cloudfunctions.net/token")
                .param("contract", contract)
                .listener(listener)
                .asGson(TokenItem.class)
                .queue(queue);
    }
}
