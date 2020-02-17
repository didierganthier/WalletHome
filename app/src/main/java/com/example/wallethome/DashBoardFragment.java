package com.example.wallethome;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wallethome.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {

    private FloatingActionButton fab_main_btn, fab_income_btn, fab_expense_btn;

    private TextView fab_income_txt, fab_expense_txt;

    private boolean isOpen = false;

    private Animation fadeOpen, fadeClose;

    private TextView totalIncomeResult, totalExpenseResult;

    private RecyclerView mRecyclerIncome, mRecyclerExpense;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    private DatabaseReference mIncomeDatabase, mExpenseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dash_board, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        progressDialog = new ProgressDialog(getContext());

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        totalIncomeResult = view.findViewById(R.id.income_set_result);
        totalExpenseResult = view.findViewById(R.id.expense_set_result);

        mRecyclerIncome = view.findViewById(R.id.recycler_income);
        mRecyclerExpense = view.findViewById(R.id.recycler_expense);

        fab_main_btn = view.findViewById(R.id.main_plus_btn);
        fab_income_btn = view.findViewById(R.id.income_ft_btn);
        fab_expense_btn = view.findViewById(R.id.expense_ft_btn);

        fab_income_txt = view.findViewById(R.id.income_ft_text);
        fab_expense_txt = view.findViewById(R.id.expense_ft_text);

        fadeOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        fadeClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                addData();

                if(isOpen)
                {
                    fab_income_btn.startAnimation(fadeClose);
                    fab_expense_btn.startAnimation(fadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(fadeClose);
                    fab_expense_txt.startAnimation(fadeClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);

                    isOpen = false;
                }
                else
                {
                    fab_income_btn.startAnimation(fadeOpen);
                    fab_expense_btn.startAnimation(fadeOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(fadeOpen);
                    fab_expense_txt.startAnimation(fadeOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);

                    isOpen = true;
                }
            }
        });

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                int total_amount = 0;

                for(DataSnapshot myDataSnapshot: dataSnapshot.getChildren())
                {
                    Data data = myDataSnapshot.getValue(Data.class);

                    total_amount += data.getAmount();

                    String stTotalAmount = String.valueOf(total_amount);

                    totalIncomeResult.setText(stTotalAmount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                int total_amount = 0;

                for(DataSnapshot myDataSnapshot: dataSnapshot.getChildren())
                {
                    Data data = myDataSnapshot.getValue(Data.class);

                    total_amount += data.getAmount();

                    String stTotalAmount = String.valueOf(total_amount);

                    totalExpenseResult.setText(stTotalAmount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);

        return view;
    }

    private void ftAnimation()
    {
        if(isOpen)
        {
            fab_income_btn.startAnimation(fadeClose);
            fab_expense_btn.startAnimation(fadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(fadeClose);
            fab_expense_txt.startAnimation(fadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);

            isOpen = false;
        }
        else
        {
            fab_income_btn.startAnimation(fadeOpen);
            fab_expense_btn.startAnimation(fadeOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(fadeOpen);
            fab_expense_txt.startAnimation(fadeOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);

            isOpen = true;
        }
    }

    private void addData()
    {
        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                incomeDataInsert();
            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                expenseDataInsert();
            }
        });
    }

    public void incomeDataInsert()
    {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myviewm = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        myDialog.setView(myviewm);
        final AlertDialog dialog = myDialog.create();
        dialog.setCanceledOnTouchOutside(false);

        final EditText edtAmount = myviewm.findViewById(R.id.amount_edt);
        final EditText edtType = myviewm.findViewById(R.id.type_edt);
        final EditText edtNote = myviewm.findViewById(R.id.note_edt);

        Button btnSave = myviewm.findViewById(R.id.btnSave);
        Button btnCancel = myviewm.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String type = edtType.getText().toString().trim();
                String amount = edtAmount.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if(TextUtils.isEmpty(amount))
                {
                    edtAmount.setError("Required Field");
                    return;
                }
                if(TextUtils.isEmpty(type))
                {
                    edtType.setError("Required Field");
                    return;
                }

                int ouramountint = Integer.parseInt(amount);

                if(TextUtils.isEmpty(note))
                {
                    edtNote.setError("Required Field");
                    return;
                }

                progressDialog.setTitle("Updating values");
                progressDialog.setMessage("Please wait while we are updating your values...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                String id = mIncomeDatabase.push().getKey();

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(ouramountint, type, note, id, mDate);

                mIncomeDatabase.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                        progressDialog.dismiss();
                        ftAnimation();
                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void expenseDataInsert()
    {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myviewm = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        myDialog.setView(myviewm);
        final AlertDialog dialog = myDialog.create();
        dialog.setCanceledOnTouchOutside(false);

        final EditText edtAmount = myviewm.findViewById(R.id.amount_edt);
        final EditText edtType = myviewm.findViewById(R.id.type_edt);
        final EditText edtNote = myviewm.findViewById(R.id.note_edt);

        Button btnSave = myviewm.findViewById(R.id.btnSave);
        Button btnCancel = myviewm.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String type = edtType.getText().toString().trim();
                String amount = edtAmount.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if(TextUtils.isEmpty(amount))
                {
                    edtAmount.setError("Required Field");
                    return;
                }
                if(TextUtils.isEmpty(type))
                {
                    edtType.setError("Required Field");
                    return;
                }

                int ouramountint = Integer.parseInt(amount);

                if(TextUtils.isEmpty(note))
                {
                    edtNote.setError("Required Field");
                    return;
                }

                progressDialog.setTitle("Updating values");
                progressDialog.setMessage("Please wait while we are updating your values...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                String id = mIncomeDatabase.push().getKey();

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(ouramountint, type, note, id, mDate);

                mExpenseDatabase.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                        progressDialog.dismiss();
                        ftAnimation();
                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<Data, IncomeViewHolder> incomeAdapter = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>(
                Data.class,
                R.layout.dashboard_income,
                IncomeViewHolder.class,
                mIncomeDatabase
        ) {
            @Override
            protected void populateViewHolder(IncomeViewHolder incomeViewHolder, Data data, int i)
            {
                incomeViewHolder.setIncomeType(data.getType());
                incomeViewHolder.setIncomeAmount(data.getAmount());
                incomeViewHolder.setIncomeDate(data.getDate());
            }
        };

        FirebaseRecyclerAdapter<Data, ExpenseViewHolder> expenseAdapter = new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>(
                Data.class,
                R.layout.dashboard_expense,
                ExpenseViewHolder.class,
                mExpenseDatabase
        ) {
            @Override
            protected void populateViewHolder(ExpenseViewHolder expenseViewHolder, Data data, int i)
            {
                expenseViewHolder.setExpenseType(data.getType());
                expenseViewHolder.setExpenseAmount(data.getAmount());
                expenseViewHolder.setExpenseDate(data.getDate());
            }
        };

        mRecyclerIncome.setAdapter(incomeAdapter);
        mRecyclerExpense.setAdapter(expenseAdapter);
    }

    public static class IncomeViewHolder extends RecyclerView.ViewHolder
    {
        View mIncomeView;

        public IncomeViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mIncomeView = itemView;
        }

        public void setIncomeType(String type)
        {
            TextView mtype = mIncomeView.findViewById(R.id.type_income_ds);
            mtype.setText(type);
        }

        public void setIncomeAmount(int amount)
        {
            TextView mamount = mIncomeView.findViewById(R.id.amount_income_ds);

            String strAmount = String.valueOf(amount);

            mamount.setText(strAmount);
        }

        public void setIncomeDate(String date)
        {
            TextView mdate = mIncomeView.findViewById(R.id.date_income_ds);
            mdate.setText(date);
        }
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder
    {
        View mExpenseView;

        public ExpenseViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mExpenseView = itemView;
        }

        public void setExpenseType(String type)
        {
            TextView mtype = mExpenseView.findViewById(R.id.type_expense_ds);
            mtype.setText(type);
        }

        public void setExpenseAmount(int amount)
        {
            TextView mamount = mExpenseView.findViewById(R.id.amount_expense_ds);

            String strAmount = String.valueOf(amount);

            mamount.setText(strAmount);
        }

        public void setExpenseDate(String date)
        {
            TextView mdate = mExpenseView.findViewById(R.id.date_expense_ds);
            mdate.setText(date);
        }
    }
}
