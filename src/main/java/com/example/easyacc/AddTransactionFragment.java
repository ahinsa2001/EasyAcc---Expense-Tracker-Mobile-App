package com.example.easyacc;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.easyacc.databinding.FragmentAddTransactionBinding;
import com.example.easyacc.utils.Constants;
import com.example.easyacc.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTransactionFragment extends BottomSheetDialogFragment {

    public AddTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentAddTransactionBinding binding;
    Transaction transaction;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater);


        transaction = new Transaction();

        binding.incomeBtn.setOnClickListener(v -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));
                binding.expenseBtn.setTextColor(getContext().getColor(R.color.txt));
            }

            transaction.setType(Constants.INCOME);
        });

        binding.expenseBtn.setOnClickListener(v -> {
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.expenseBtn.setTextColor(getContext().getColor(R.color.red));
                binding.incomeBtn.setTextColor(getContext().getColor(R.color.txt));
            }

            transaction.setType(Constants.EXPENSE);
        });

        binding.date.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());
                    calendar.set(Calendar.MONTH, view.getMonth());
                    calendar.set(Calendar.YEAR, view.getYear());

                    String dateToShow = Helper.formatDate(calendar.getTime());

                    binding.date.setText(dateToShow);

                    transaction.setDate(calendar.getTime());
                    transaction.setId(calendar.getTime().getTime());
                });
                datePickerDialog.show();
            }
        });

        binding.saveTransactionBtn.setOnClickListener(c-> {
            double amount = Double.parseDouble(binding.amount.getText().toString());
            String reference = binding.references.getText().toString();
            String note = binding.note.getText().toString();

                if (transaction.getType().equals(Constants.EXPENSE)) {
                    transaction.setAmount(amount * -1);
                } else {
                    transaction.setAmount(amount);
                }
                transaction.setReferences(reference);
                transaction.setNote(note);
                ((MainActivity)getActivity()).viewModel.addTransactions(transaction);
                ((MainActivity)getActivity()).getTransactions();
                dismiss();
        });

        return binding.getRoot();
    }
}