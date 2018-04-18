package com.ankhrom.coinmarketcap.data;

import android.support.annotation.NonNull;

import com.ankhrom.base.interfaces.ObjectFactory;
import com.ankhrom.coinmarketcap.common.ExchangeType;
import com.ankhrom.coinmarketcap.entity.AuthCredentials;
import com.ankhrom.coinmarketcap.prefs.ExchangePrefs;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by R' on 4/18/2018.
 */
public class FireSynchronizer { //TODO prep to synchronize across devices

    private final ObjectFactory factory;

    private FireSynchronizer(ObjectFactory factory) {
        this.factory = factory;
    }

    public static FireSynchronizer init(@NonNull ObjectFactory factory) {

        return new FireSynchronizer(factory);
    }

    public void syncExchanges() {

        for (ExchangeType type : ExchangeType.values()) {
            syncExchange(type);
        }
    }

    public void syncExchange(ExchangeType exchange) {

        AuthCredentials auth = getExchangePrefs().getAuth(exchange);

        if (auth.isValid()) {
            FirebaseDatabase.getInstance()
                    .getReference("auth")
                    .child(FirebaseInstanceId.getInstance().getId())
                    .child(exchange.name())
                    .setValue(auth);
        }
    }

    private ExchangePrefs getExchangePrefs() {

        return factory.get(ExchangePrefs.class);
    }
}
