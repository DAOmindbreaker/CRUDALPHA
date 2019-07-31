package com.dika.crudalpha;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dika.crudalpha.database.Berita;
import com.dika.crudalpha.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BeritaAdapter mAdapter;
    private List<Berita> listBerita = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView noDataView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        recyclerView = findViewById( R.id.recycler_view );
        noDataView = findViewById( R.id.empty_view );

        db = new DatabaseHelper( this );

        listBerita.addAll( db.getAllBerita());

        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                showFormDialog(false, null, -1);
            }
        });

        mAdapter = new BeritaAdapter( this, listBerita );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager( getApplicationContext());
        recyclerView.setLayoutManager( mLayoutManager );
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        recyclerView.addItemDecoration( new DividerItemDecoration( this, LinearLayoutManager.VERTICAL ));
        recyclerView.setAdapter( mAdapter );

        toggleEmptyData();

        recyclerView.addOnItemTouchListener( new RecyclerTouchListener( this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View child, int position) {
                showActionsDialog(position);
            }
        }));
    }

    private void toggleEmptyData() {

        if (db.getBeritaCount() > 0){
            noDataView.setVisibility( View.GONE );
        } else {
            noDataView.setVisibility( View.VISIBLE );
        }
    }

    private void showActionsDialog(final int position){
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Choose option" );
        builder.setItems( colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showFormDialog( true, listBerita.get( position ), position );
                } else {
                    deleteBerita( position );
                }
            }
        });
        builder.show();
    }

    private void showFormDialog(final boolean isUpdate, final Berita berita, final int position){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from( getApplicationContext());
        View view = layoutInflaterAndroid.inflate( R.layout.form_dialog, null );

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder( MainActivity.this );
        alertDialogBuilderUserInput.setView( view );

        final EditText inputJudul = view.findViewById( R.id.judul );
        final EditText inputIsi = view.findViewById( R.id.isi );
        TextView dialogTitle = view.findViewById( R.id.dialog_title );
        dialogTitle.setText( !isUpdate ? getString( R.string.lbl_Tambah_berita ):
                getString( R.string.lbl_Edit_berita ));

        if (isUpdate && berita != null){
            inputJudul.setText( berita.getJudul());
            inputIsi.setText( berita.getIsi());
        }

        alertDialogBuilderUserInput
                .setCancelable( false )
                .setPositiveButton( isUpdate ? "update" : "save", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogBox, int id) {
                    }
                    })
        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogBox, int id) {
                dialogBox.cancel();
            }
        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton( AlertDialog.BUTTON_POSITIVE ).setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (TextUtils.isEmpty( inputJudul.getText().toString())){
                    Toast.makeText( MainActivity.this, "Isi judul berita!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (isUpdate && berita != null){

                    updateBerita(inputJudul.getText().toString(), inputIsi.getText().toString(),position);
                } else {
                    createBerita( inputJudul.getText().toString(), inputIsi.getText().toString() );
                }
            }
        });
    }

    private void createBerita(String judul, String isi){
        long id = db.insertBerita( judul, isi );

        Berita berita = db.getBerita( id );

        if (berita != null){
            listBerita.add( 0,berita );
            mAdapter.notifyDataSetChanged();
            toggleEmptyData();
        }
    }

    private void updateBerita(String judul, String isi, int position){
        Berita berita = listBerita.get( position );

        berita.setJudul( judul );
        berita.setIsi( isi );

        db.updateBerita( berita );

        listBerita.set( position, berita );
        mAdapter.notifyDataSetChanged(position);

        toggleEmptyData();
    }

    private void deleteBerita(int position){
        db.deleteBerita( listBerita.get( position ));

        listBerita.remove( position );
        mAdapter.notifyDataSetChanged(position);

        toggleEmptyData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if (id== R.id.action_settings){
            return true;
        }

        return super.onOptionsItemSelected( item );
    }
}
