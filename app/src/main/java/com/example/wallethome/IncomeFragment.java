package com.example.wallethome;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wallethome.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends Fragment
{

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;

    private RecyclerView recyclerView;

    private TextView incomeTotalSum;

    private EditText edtAmount, edtType, edtNote;

    private Button btnUpdate, btnDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_income, container, false);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        String uid = user.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        recyclerView = myView.findViewById(R.id.recycler_id_income);

        incomeTotalSum = myView.findViewById(R.id.income_txt_result);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

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

                    incomeTotalSum.setText(stTotalAmount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return myView;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(
                Data.class,
                R.layout.income_recycler_data,
                MyViewHolder.class,
                mIncomeDatabase
        ) {
            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder, Data data, int i)
            {
                myViewHolder.setType(data.getType());
                myViewHolder.setNote(data.getNote());
                myViewHolder.setDate(data.getDate());
                myViewHolder.setAmount(String.valueOf(data.getAmount()));

                myViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        updateDataItem();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setType(String type)
        {
            TextView mType = mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }

        private void setNote(String note)
        {
            TextView mNote = mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }

        private void setDate(String date)
        {
            TextView mDate = mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }

        private void setAmount(String amount)
        {
            TextView mAmount = mView.findViewById(R.id.amount_txt_income);
            String stamount = String.valueOf(amount);
            mAmount.setText(stamount);
        }
    }

    private void updateDataItem()
    {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.update_data_item, null);
        mydialog.setCancelable(false);
        mydialog.setView(myView);

        edtAmount = myView.findViewById(R.id.amount_edt);
        edtType = myView.findViewById(R.id.type_edt);
        edtNote = myView.findViewById(R.id.note_edt);

        btnUpdate = myView.findViewById(R.id.btnUp_Update);
        btnDelete = myView.findViewById(R.id.btnUp_Delete);

        final AlertDialog dialog = mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
