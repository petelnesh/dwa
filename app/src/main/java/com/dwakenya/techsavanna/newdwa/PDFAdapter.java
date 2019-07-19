package com.dwakenya.techsavanna.newdwa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.PDFViewHolder> {
    Context context;
    List<PDFModel> pdfModel;
    
    
    public PDFAdapter(Context context, List<PDFModel> pdfModel) {
        this.context = context;
        this.pdfModel = pdfModel;
    }
    
    
    
    @Override
    public PDFAdapter.PDFViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PDFAdapter.PDFViewHolder(LayoutInflater.from(context).inflate(R.layout.pdfraw, parent, false));
    }
    
    @Override
    public void onBindViewHolder(PDFAdapter.PDFViewHolder holder, final int position) {
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
    
    public class PDFViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
    
        public PDFViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.PdfName);
        }
    }
}
