package com.example.easyacc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.easyacc.databinding.ActivityMainBinding;
import com.example.easyacc.utils.Constants;
import com.example.easyacc.utils.Helper;
import com.example.easyacc.viewmodels.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    Calendar calender;

    public MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Easy Accounting");

        calender = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(c-> {
            if(Constants.SELECTED_TAB == Constants.DAILY) {
                calender.add(Calendar.DATE, 1);
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                calender.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.previousDateBtn.setOnClickListener(c-> {
            if(Constants.SELECTED_TAB == Constants.DAILY) {
                calender.add(Calendar.DATE, -1);
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                calender.add(Calendar.MONTH, -1);
            }
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(v -> new AddTransactionFragment().show(getSupportFragmentManager(), null));

        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("Monthly")) {
                    Constants.SELECTED_TAB = Constants.MONTHLY;
                    updateDate();
                } else if (tab.getText().equals("Daily")) {
                    calender = Calendar.getInstance();
                    Constants.SELECTED_TAB = Constants.DAILY;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        binding.transactionList.setLayoutManager(new LinearLayoutManager(this));

        viewModel.transactions.observe(this, new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionsAdapter transactionsAdapter = new TransactionsAdapter(MainActivity.this, transactions);
                binding.transactionList.setAdapter(transactionsAdapter);
                if(transactions.size() == 0) {
                    binding.emptyState.setVisibility(View.VISIBLE);
                }
                else {
                    binding.emptyState.setVisibility(View.GONE);
                }
            }
        });
        viewModel.totalIncome.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.income.setText(String.valueOf(aDouble));
            }
        });
        viewModel.totalExpense.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenes.setText(String.valueOf(aDouble));
            }
        });
        viewModel.totalAmount.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.total.setText(String.valueOf(aDouble));
            }
        });
        viewModel.getTransactions(calender);
    }

    public void getTransactions() {
        viewModel.getTransactions(calender);
    }

    void  updateDate() {
        if(Constants.SELECTED_TAB == Constants.DAILY) {
            binding.currentDate.setText(Helper.formatDate(calender.getTime()));
        } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
            binding.currentDate.setText(Helper.formatDateByMonth(calender.getTime()));
        }
        viewModel.getTransactions(calender);
    }

}