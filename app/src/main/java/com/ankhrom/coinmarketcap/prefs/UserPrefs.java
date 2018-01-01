package com.ankhrom.coinmarketcap.prefs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ankhrom.base.custom.prefs.BasePrefs;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.listener.OnFavouriteItemChangedListener;
import com.ankhrom.coinmarketcap.model.CoinItemModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 12/30/2017.
 */

public class UserPrefs extends BasePrefs {

    private static final String PREFS = "user_prefs";
    private static final String CURRENCY = "currency";
    private static final String FAVOURITES = "favourites";

    private List<OnFavouriteItemChangedListener> favouriteItemChangedListeners;

    public UserPrefs(@NonNull Context context) {
        super(context);

        favouriteItemChangedListeners = new ArrayList<>();
    }

    public void addFavouriteItemChangedListener(OnFavouriteItemChangedListener listener) {

        if (!favouriteItemChangedListeners.contains(listener)) {
            favouriteItemChangedListeners.add(listener);
        }
    }

    public void removeFavouriteItemChangedListener(OnFavouriteItemChangedListener listener) {

        if (favouriteItemChangedListeners.contains(listener)) {
            favouriteItemChangedListeners.remove(listener);
        }
    }

    public void notifyFavouriteItemChangedListeners(CoinItemModel item) {

        for (OnFavouriteItemChangedListener listener : favouriteItemChangedListeners) {
            listener.onFavouriteItemPrefsChanged(item);
        }
    }

    public void setCurrency(String currency) {

        edit().putString(CURRENCY, currency).apply();
    }

    public String getCurrency() {

        return getPrefs().getString(CURRENCY, context.getResources().getString(R.string.default_currency));
    }

    public void setFavourites(List<String> coins) {

        if (coins == null || coins.isEmpty()) {
            edit().putString(FAVOURITES, null).apply();
        } else {
            edit().putString(FAVOURITES, new Gson().toJson(coins)).apply();
        }
    }

    public List<String> getFavourites() {

        Type type = new TypeToken<List<String>>() {
        }.getType();

        return new Gson().fromJson(getPrefs().getString(FAVOURITES, DEFAULT_JSON_LIST), type);
    }

    public void addFavourite(String id) {

        List<String> favs = getFavourites();
        favs.add(id);

        setFavourites(favs);
    }

    public void removeFavourite(String id) {

        List<String> favs = getFavourites();
        favs.remove(id);

        setFavourites(favs);
    }

    @Override
    public String getPrefsName() {
        return PREFS;
    }
}
