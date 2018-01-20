package com.ankhrom.coinmarketcap.viewmodel.dialog;

import android.view.View;

import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.common.statics.ScreenHelper;
import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.interfaces.ObjectConverter;
import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.interfaces.OnValueChangedListener;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.databinding.SearchPageBinding;
import com.ankhrom.coinmarketcap.entity.CoinItem;
import com.ankhrom.coinmarketcap.listener.OnCoinSelectedListener;
import com.ankhrom.coinmarketcap.model.dialog.SearchAdapterModel;
import com.ankhrom.coinmarketcap.model.dialog.SearchItemModel;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 1/7/2018.
 */

public class SearchViewModel extends AppViewModel<SearchPageBinding, SearchAdapterModel> implements OnItemSelectedListener<SearchItemModel>, OnValueChangedListener<String> {

    private OnCoinSelectedListener listener;
    private List<SearchItemModel> items;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        listener = args.getArg(OnCoinSelectedListener.class);
    }

    @Override
    public void loadModel() {

        items = ObjectHelper.convert(getDataHolder().getCoins(), new ObjectConverter<SearchItemModel, CoinItem>() {
            @Override
            public SearchItemModel convert(CoinItem object) {
                return new SearchItemModel(object, SearchViewModel.this);
            }
        });

        setModel(new SearchAdapterModel(getContext(), items));
        model.fulltext.setOnValueChangedListener(this);
    }

    @Override
    public void onValueChanged(String value) {

        model.adapter.clear();

        if (StringHelper.isEmpty(value)) {
            model.adapter.addAll(items);
            return;
        }

        value = value.toLowerCase();

        List<SearchItemModel> searchResult = new ArrayList<>();
        for (SearchItemModel item : items) {
            if (item.fullName.toLowerCase().contains(value)) {
                searchResult.add(item);
            }
        }

        model.adapter.addAll(searchResult);
    }

    @Override
    public void onItemSelected(View view, SearchItemModel model) {

        if (listener != null) {
            listener.onCoinSelected(model.coin);
        }

        onClosePressed(view);
    }

    public void onClosePressed(View view) {

        ScreenHelper.hideSoftKeyboard(getBaseActivity());
        getNavigation().setPreviousViewModel();
        FragmentHelper.removePage(getContext(), this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.search_page;
    }
}
