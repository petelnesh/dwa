package com.dwakenya.techsavanna.newdwa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PdfHASAdapter extends RecyclerView.Adapter<PdfHASAdapter.PdfHASViewHolder>{
        Context context;
        List<PDFModel> pdfModel;
        
        
        public PdfHASAdapter(Context context, List<PDFModel> pdfModel) {
            this.context = context;
            this.pdfModel = pdfModel;
        }
        
        
        
        @Override
        public PdfHASAdapter.PdfHASViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PdfHASAdapter.PdfHASViewHolder(LayoutInflater.from(context).inflate(R.layout.pdfraw, parent, false));
        }
        
        @Override
        public void onBindViewHolder(PdfHASAdapter.PdfHASViewHolder holder, final int position) {
            holder.textView.setText(pdfModel.get(position).getName());
            
            
            
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    
//                    final String filename =pdfModel.get(position).getPdf();
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(filename));
//                    context.startActivity(intent);
    
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
        
        public class PdfHASViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            
            public PdfHASViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.PdfName);
            }
        }
    }

