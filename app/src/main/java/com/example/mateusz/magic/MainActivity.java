package com.example.mateusz.magic;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends Activity
{

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        dentiny  = (int) getResources().getDisplayMetrics().density;
        widthCalyEkran  = (int) outMetrics.widthPixels/dentiny;
        srodekEkranu = (int)widthCalyEkran/2;

        plik = new Pliczek(getApplicationContext().getFilesDir().getPath().toString()+"/zapisane.txt");

        Player gracz1 = new Player(this);
        Player gracz2 = new Player(this);
        graczeList.add(gracz1);
        graczeList.add(gracz2);
        gracze = ((LinearLayout) findViewById(R.id.players));
        gracze.addView(gracz1.getGlowne());
        gracze.addView(gracz2.getGlowne());
        ViewTreeObserver vto = gracz1.getGlowne().getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout() {
                graczeList.get(0).getGlowne().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = graczeList.get(0).getGlowne().getMeasuredWidth();
                widhtPlayera = width / dentiny;
                int ile = srodekEkranu - widhtPlayera - 40;
                graczeList.get(0).setPaddingForFirstPlayer(ile * dentiny, 40 * dentiny);
                graczeList.get(1).setPaddingLeft(40 * dentiny);
            }
        });
        getPliczek();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getPliczek()
    {
        Uri urlPliku = Uri.parse(plik.getZapis());
        try
        {
            if(urlPliku.toString().equals(""))
            {
                ((ScrollView) findViewById(R.id.Layout_glowny)).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.czarny));
            }
            else
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),urlPliku);
                if(bitmap.getHeight() >= 2000)
                {
                    bitmap = resize(bitmap);
                }
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                ((ScrollView) findViewById(R.id.Layout_glowny)).setBackground(drawable);

            }
        }
        catch (Exception e)
        {
            ((ScrollView) findViewById(R.id.Layout_glowny)).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.czarny));
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.info)
        {
            Intent intent = new Intent(getApplicationContext(),OpisAcivity.class);
            startActivity(intent);
        }
        else if(id == R.id.two)
        {
                int ile = srodekEkranu - widhtPlayera - 40;
                item.setChecked(true);
                if(graczeList.size() == 4)
                {
                    graczeList.remove(3);
                    gracze.removeViewAt(3);
                    graczeList.remove(2);
                    gracze.removeViewAt(2);
                    graczeList.get(0).setPaddingForFirstPlayer(ile * dentiny, 40*dentiny);
                    graczeList.get(1).setPaddingLeft(40*dentiny);

                }
                else if(graczeList.size() == 3)
                {
                    graczeList.remove(2);
                    gracze.removeViewAt(2);
                    graczeList.get(0).setPaddingForFirstPlayer(ile * dentiny, 40*dentiny);
                    graczeList.get(1).setPaddingLeft(40 * dentiny);
                }
        }
        else if(id == R.id.three)
        {
                int iler = 3 * widhtPlayera;
                int srodekPlayera = iler/2;
                int ile = srodekEkranu - srodekPlayera-20;
                item.setChecked(true);
                if(graczeList.size() == 4)
                {
                    graczeList.remove(3);
                    gracze.removeViewAt(3);
                    graczeList.get(0).setPaddingForFirstPlayer(ile * dentiny, 20 * dentiny);
                    graczeList.get(1).setPaddingRight(20 * dentiny);
                }
                else if(graczeList.size() == 2)
                {
                    Player gracz3 = new Player(this);
                    if(czyZaznaczonoHideEnergie)
                    {
                        gracz3.pokazMana();
                    }
                    graczeList.add(gracz3);
                    gracze.addView(gracz3.getGlowne());
                    graczeList.get(0).setPaddingForFirstPlayer(ile * dentiny, 20*dentiny);
                    graczeList.get(1).setPaddingRight(20*dentiny);
                }
        }
        else if(id == R.id.four)
        {
                int iler = 4 * widhtPlayera;
                int srodekPlayera = iler/2;
                int ile = srodekEkranu - srodekPlayera;
                item.setChecked(true);
                if(graczeList.size() == 2)
                {
                    Player gracz3 = new Player(this);
                    Player gracz4 = new Player(this);
                    if(czyZaznaczonoHideEnergie)
                    {
                        gracz3.pokazMana();
                        gracz4.pokazMana();
                    }
                    graczeList.add(gracz3);
                    gracze.addView(gracz3.getGlowne());
                    graczeList.add(gracz4);
                    gracze.addView(gracz4.getGlowne());
                    graczeList.get(0).setPaddingForFirstPlayer(ile * dentiny, 0);
                    graczeList.get(1).setPaddingLeft(0);
                }
                else if(graczeList.size() == 3)
                {
                    Player gracz4 = new Player(this);
                    if(czyZaznaczonoHideEnergie)
                    {
                        gracz4.pokazMana();
                    }
                    graczeList.add(gracz4);
                    gracze.addView(gracz4.getGlowne());
                    graczeList.get(0).setPaddingForFirstPlayer(ile * dentiny, 0);
                    graczeList.get(1).setPaddingLeft(0);
                }
        }
        else if(id == R.id.hide_menu)
        {
            if(item.isChecked())
            {
                for(int i =0;i<graczeList.size();i++)
                {
                    czyZaznaczonoHideEnergie = false;
                    graczeList.get(i).usunMana();
                }
                item.setChecked(false);
            }
            else
            {
                for(int i =0;i<graczeList.size();i++)
                {
                    czyZaznaczonoHideEnergie = true;
                    graczeList.get(i).pokazMana();
                }
                item.setChecked(true);
            }
        }
        else if(id == R.id.add_tlo)
        {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.wybierz_obrazek)), SELECT_PICTURE);
            }
            else
            {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.wybierz_obrazek)), SELECT_PICTURE);
            }
        }
        else if(id == R.id.restart_tlo)
        {
            ((ScrollView) findViewById(R.id.Layout_glowny)).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.czarny));
            plik.zapiszDoPliku("");
        }
        else if(id == R.id.restart)
        {
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
            {
                for(int i = 0; i< graczeList.size();i++)
                {

                    ((TextView) graczeList.get(i).getGlowne().findViewById(R.id.life_opis_0)).setText("20");
                    ((TextView) graczeList.get(i).getGlowne().findViewById(R.id.energy_opis_0)).setText("0");

                    if(graczeList != null && graczeList.get(i).getPlayer().getChildCount() > 0)
                    {
                        for(int index=0; index<((ViewGroup)graczeList.get(i).getPlayer()).getChildCount(); ++index)
                        {
                            View nextChild = ((ViewGroup)graczeList.get(i).getPlayer()).getChildAt(index);
                            ((TextView) nextChild.findViewById(R.id.dodatek_opis_dodatkowy)).setText("0");
                        }
                    }
                }
            }
            else
            {
                for(int i = 0; i< graczeList.size();i++)
                {

                    ((TextView) graczeList.get(i).getGlowne().findViewById(R.id.life_opis_0)).setText("20");
                    ((TextView) graczeList.get(i).getGlowne().findViewById(R.id.life_opis_0)).setBackgroundResource(0);
                    ((TextView) graczeList.get(i).getGlowne().findViewById(R.id.energy_opis_0)).setText("0");
                    ((TextView) graczeList.get(i).getGlowne().findViewById(R.id.energy_opis_0)).setBackgroundResource(0);

                    if(graczeList != null && graczeList.get(i).getPlayer().getChildCount() > 0)
                    {
                        for(int index=0; index<((ViewGroup)graczeList.get(i).getPlayer()).getChildCount(); ++index)
                        {
                            View nextChild = ((ViewGroup)graczeList.get(i).getPlayer()).getChildAt(index);
                            ((TextView) nextChild.findViewById(R.id.dodatek_opis_dodatkowy)).setText("0");
                            ((TextView) nextChild.findViewById(R.id.dodatek_opis_dodatkowy)).setBackgroundResource(0);
                        }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE)
            {
                try
                {
                    Uri urlPliku = data.getData();
                    if(urlPliku.toString() != "")
                    {
                        plik.zapiszDoPliku(urlPliku.toString());
                    }
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),urlPliku);
                    if(bitmap.getHeight() >= 2000)
                    {
                        bitmap = resize(bitmap);
                    }
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    ((ScrollView) findViewById(R.id.Layout_glowny)).setBackground(drawable);
                }
                catch (FileNotFoundException e)
                {
                    Toast.makeText(getApplicationContext(),R.string.nie_znaleziono,Toast.LENGTH_SHORT).show();
                }
                catch (IOException e)
                {
                    Toast.makeText(getApplicationContext(),R.string.nie_poprawny,Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),R.string.format,Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private Bitmap resize(Bitmap b)
    {
        Bitmap g  = Bitmap.createScaledBitmap(b, (int) (b.getWidth() * 0.5), (int) (b.getHeight() * 0.5), true);
        return g;
    }
    private LinearLayout gracze;
    ArrayList<Player> graczeList = new ArrayList<>();
    private static final int SELECT_PICTURE = 1;
    private Pliczek plik = null;
    private int srodekEkranu = 0;
    private int widhtPlayera = 0;
    private int widthCalyEkran = 0;
    private int dentiny = 0;
    private boolean czyZaznaczonoHideEnergie = false;
}
class AndroidSłuchaczLifePlus implements View.OnClickListener
{
    AndroidSłuchaczLifePlus(View r)
    {
        id = r;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v)
    {

        TextView tekst = ((TextView) id);
        int tekscik;
        if(tekst.getText().equals(""))
        {
            tekscik = 0;
        }
        else
        {
            tekscik = Integer.parseInt(tekst.getText().toString());
        }
        if(tekscik == 99 )
        {

        }
        else
        {
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
            {
                tekscik = tekscik + 1;
                tekst.setText(String.valueOf(tekscik));
            }
            else
            {
                tekst.setBackgroundResource(0);
                tekscik = tekscik + 1;
                tekst.setText(String.valueOf(tekscik));
            }
        }


    }
    private View id;
}
class AndroidSłuchaczLifeMinus implements View.OnClickListener
{
    AndroidSłuchaczLifeMinus(View r,Activity g)
    {
        id = r;
        activ = g;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v)
    {
        TextView tekst = ((TextView) id);
        if(tekst.getText().equals(""))
        {

        }
        else
        {
            int tekscik = Integer.parseInt(tekst.getText().toString());
            if(tekscik == 1)
            {
                if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
                {
                    tekst.setText("0");
                }
                else
                {
                    tekst.setBackground(activ.getApplicationContext().getDrawable(R.drawable.czacha));
                    tekst.setText("");
                }
            }
            else
            {
                if(tekscik == 0)
                {

                }
                else
                {
                    if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
                    {
                        tekscik = tekscik - 1;
                        tekst.setText(String.valueOf(tekscik));
                    }
                    else
                    {
                        tekst.setBackgroundResource(0);
                        tekscik = tekscik - 1;
                        tekst.setText(String.valueOf(tekscik));
                    }
                }
            }
        }
    }
    private View id;
    private Activity activ;
}
class Pliczek extends File
{
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    Pliczek(String name) {
        super(name);
}
    public void zapiszDoPliku(String co)
    {
        try
        {
            FileWriter w = new FileWriter(this);
            w.write(co);
            w.close();
        }
        catch (IOException e) {}

    }
    public String getZapis()
    {
        String g = "";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(this));
            StringBuffer b= new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null)
            {
               b.append(line);
            }
            br.close();
            g = b.toString();

        }
        catch (IOException e) {}
        return g;
    }
    public boolean czyIstnieje()
    {
        return this.exists();
    }
}
