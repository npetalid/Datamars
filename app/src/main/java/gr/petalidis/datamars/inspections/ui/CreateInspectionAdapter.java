package gr.petalidis.datamars.inspections.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.dto.InspectionDateProducer;
import gr.petalidis.datamars.inspections.exceptions.PersistenceException;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.service.FileService;
import gr.petalidis.datamars.inspections.service.InspectionService;

public class CreateInspectionAdapter extends ArrayAdapter<InspectionDateProducer> {
    private final Context context;
    private final List<InspectionDateProducer> objects;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private final DbHandler dbHandler;


    public CreateInspectionAdapter(Context context, int resource, List<InspectionDateProducer> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        dbHandler = new DbHandler(context.getApplicationContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listinspections, parent, false);
        final InspectionDateProducer item = objects.get(position);

        TextView inspectionDateText = (TextView) rowView.findViewById(R.id.inspectionDate);
        TextView inspectionProducer = (TextView) rowView.findViewById(R.id.inspectionProducer);
        Button exportCsvButton = (Button) rowView.findViewById(R.id.exportCsv);


        exportCsvButton.setOnClickListener(x->
        { try {
            File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            Inspection inspection = InspectionService.findInspectionFor(dbHandler,item.getId());
            FileService.export(inspection.toStrings(),downloads.getAbsolutePath(),inspection.getProducer1Tin()+"-"+simpleDateFormat.format(inspection.getDate())+".csv");
        } catch (IOException | PersistenceException e) {
            Log.e("CreateInspectionActivity",e.getLocalizedMessage());
        }});


        inspectionProducer.setText(item.getMainProducer());
        inspectionDateText.setText(item.getInspectionDate());

        inspectionProducer.setOnTouchListener((x,y)->{
            try {
                Inspection inspection = InspectionService.findInspectionFor(dbHandler, item.getId());
                Intent intent = new Intent(context, InspectionViewActivity.class);
                intent.putExtra("inspection", inspection);
                ((Activity)context).startActivity(intent);
                return true;
            } catch (PersistenceException e) {
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
        return rowView;
    }


}
