package ga.forntoh.bableschool.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;
import java.util.ArrayList;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.model.Document;
import ga.forntoh.bableschool.utils.Utils;

public class DocumentsAdapter extends CategoryAdapter {

    public DocumentsAdapter(ArrayList list) {
        super(list);
    }

    @NonNull
    @Override
    public DocumentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DocumentsAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        Document document = (Document) list.get(position);
        holder.title.setText(document.getTitle());
        holder.author.setText(document.getAuthor());
        holder.duration.setText(document.getSize());
        holder.thumbnail.setImageResource(document.getType());
    }

    class MyViewHolder extends CategoryAdapter.MyViewHolder {

        @SuppressLint("InflateParams")
        MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.document_title);
            author = itemView.findViewById(R.id.document_author);
            duration = itemView.findViewById(R.id.document_size);
            thumbnail = itemView.findViewById(R.id.document_thumbnail);
            download_thumbnail = itemView.findViewById(R.id.document_download);
            download_thumbnail.setOnClickListener((view) -> {
                View layout = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_pdf_viewer, null);
                Utils.startPopUpWindow(layout, itemView.getRootView().getRootView(), null);

                PDFView pdfView = layout.findViewById(R.id.pdfView);
                ProgressBar loadingProgress = layout.findViewById(R.id.progressBar);

                FileLoader.with(itemView.getContext())
                        .load(((Document) list.get(getAdapterPosition())).getUrl(), false)
                        .fromDirectory(itemView.getContext().getString(R.string.app_name), FileLoader.DIR_INTERNAL)
                        .asFile(new FileRequestListener<File>() {
                            @Override
                            public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                                File loadedFile = response.getBody();
                                pdfView.fromFile(loadedFile)
                                        .defaultPage(0)
                                        .enableSwipe(true)
                                        .enableDoubletap(true)
                                        .enableAntialiasing(true)
                                        .spacing(4)
                                        .onLoad(nbPages -> loadingProgress.setVisibility(View.GONE))
                                        .load();
                            }

                            @Override
                            public void onError(FileLoadRequest request, Throwable t) {
                                loadingProgress.setVisibility(View.GONE);
                                t.printStackTrace();
                                Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            });
        }

        @Override
        public void onClick(View view) {

        }
    }
}
