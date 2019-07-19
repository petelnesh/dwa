package com.dwakenya.techsavanna.newdwa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ContractHolder> {
        Context context;
        List<PDFModel> pdfModel;
        
        
        public ContractAdapter(Context context, List<PDFModel> pdfModel) {
            this.context = context;
            this.pdfModel = pdfModel;
        }
        
        
        
        @Override
        public ContractAdapter.ContractHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContractAdapter.ContractHolder(LayoutInflater.from(context).inflate(R.layout.pdfraw, parent, false));
        }
        
        @Override
        public void onBindViewHolder(ContractAdapter.ContractHolder holder, final int position) {
            holder.textView.setText(pdfModel.get(position).getName());
            
            
            
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PDFView.class);
                    intent.putExtra("PDFUrl", pdfModel.get(position).getPdf());
                    context.startActivity(intent);
                    
                }
            });
            
        }
        
        @Override
        public int getItemCount() {
            return pdfModel.size();
        }
        
        public class ContractHolder extends RecyclerView.ViewHolder{
            TextView textView;
            
            public ContractHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.PdfName);
            }
        }
}
