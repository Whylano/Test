package com.wisethan.bestrefur1.OnlineOrder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wisethan.bestrefur1.R;

import java.io.IOException;
import java.util.List;

public class PDFFileAdapter extends RecyclerView.Adapter<PDFFileAdapter.FileViewHolder> {

    private final List<Uri> fileList;
    private final Context context;

    public PDFFileAdapter(List<Uri> fileList, Context context) {
        this.fileList = fileList;
        this.context = context;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_pdf_recycler_item_pdf, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        Uri uri = fileList.get(position);
        holder.bindPdfPages(uri);
        holder.itemView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        private final ViewGroup container;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.page_container);
        }

        private void bindPdfPages(Uri uri) {
            try {

                ParcelFileDescriptor fileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
                //androidPDFViewer 클래스를 사용하려했지만 다른 레이아웃의 에러로 사이드 임팩트가 예상되어 PDFRenderer 클래스 사용
                PdfRenderer pdfRenderer = new PdfRenderer(fileDescriptor); //PdfRenderer 클래스 생성

                int pageCount = pdfRenderer.getPageCount(); //렌더링을 위한 PDF 문서 총 페이지 수 생성 .
                for (int i = 0; i < pageCount; i++) {
                    PdfRenderer.Page page = pdfRenderer.openPage(i);
                    Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    page.close();
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(20,20,20,20);
                    ImageView imageView = new ImageView(context);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setImageBitmap(bitmap);
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                    container.addView(imageView);
                }

                fileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

