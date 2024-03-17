package com.example.easyacc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyacc.databinding.RowTransactionBinding;
import com.example.easyacc.utils.Constants;
import com.example.easyacc.utils.Helper;

import java.time.Duration;
import java.util.ArrayList;

import io.realm.RealmResults;

public class TransactionsAdapter extends  RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

    Context context;
    RealmResults<Transaction> transactions;

    public TransactionsAdapter(Context context, RealmResults<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction =transactions.get(position);

        holder.binding.transactionAmount.setText(String.valueOf(transaction.getAmount()));
        holder.binding.references.setText(transaction.getReferences());

        holder.binding.transDate.setText(Helper.formatDate(transaction.getDate()));

            if (transaction.getType().equals(Constants.INCOME)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.binding.transactionAmount.setTextColor(context.getColor(R.color.green));
                }
                holder.binding.categoryIcon.setImageDrawable(context.getDrawable(R.drawable.up));

            } else if (transaction.getType().equals(Constants.EXPENSE)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.binding.transactionAmount.setTextColor(context.getColor(R.color.red));
                }
                holder.binding.categoryIcon.setImageDrawable(context.getDrawable(R.drawable.down));
            }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog deleteDialog =  new AlertDialog.Builder(context).create();
                deleteDialog.setTitle("Delete Transaction");
                deleteDialog.setMessage("Are you sure to delete this transaction?");
                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
                    ((MainActivity)context).viewModel.deleteTransaction(transaction);
                });
                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", (dialog, which) -> {
                    deleteDialog.dismiss();
                });
                deleteDialog.show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        RowTransactionBinding binding;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
