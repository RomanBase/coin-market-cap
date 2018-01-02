package com.ankhrom.coinmarketcap.viewmodel.portfolio;

import android.view.View;

import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.common.statics.ScreenHelper;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.databinding.PortfolioPlusPageBinding;
import com.ankhrom.coinmarketcap.entity.PortfolioItem;
import com.ankhrom.coinmarketcap.model.PortfolioPlusModel;
import com.ankhrom.coinmarketcap.prefs.UserPrefs;
import com.ankhrom.coinmarketcap.viewmodel.base.AppViewModel;

/**
 * Created by R' on 1/2/2018.
 */

public class PortfolioPlusViewModel extends AppViewModel<PortfolioPlusPageBinding, PortfolioPlusModel> {

    @Override
    public void onInit() {
        super.onInit();

        setModel(new PortfolioPlusModel());
    }

    public void onCreatePressed(View view) {

        if (!(model.currency.isValid() && model.units.isValid() && model.unitPrice.isValid())) {
            return;
        }

        PortfolioItem item = new PortfolioItem();
        item.coinId = model.currency.get();
        item.amount = Double.parseDouble(model.units.get());
        item.unitPrice = Double.parseDouble(model.unitPrice.get());

        UserPrefs prefs = getFactory().get(UserPrefs.class);

        prefs.addPorfolioItem(item);

        close();
    }

    public void onCancelPressed(View view) {
        close();
    }

    private void close() {

        ScreenHelper.hideSoftKeyboard(getBaseActivity());

        FragmentHelper.removePage(getContext(), this);
        getNavigation().setPreviousViewModel();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.portfolio_plus_page;
    }
}
