
package com.example.mateusz.magic;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Mateusz on 2016-10-05.
 */

public class Player extends Activity
{
    public Player(Activity t)
    {
        activity = t;
        glowne = activity.getLayoutInflater().inflate(R.layout.activity_player, null);
        player = ((LinearLayout) glowne.findViewById(R.id.dodatki_kontent));
        final LayoutInflater factory = LayoutInflater.from(activity.getApplicationContext());
        ((Button) glowne.findViewById(R.id.plus_0)).setOnClickListener(new AndroidSłuchaczLifePlus(glowne.findViewById(R.id.life_opis_0)));
        ((Button) glowne.findViewById(R.id.minus_0)).setOnClickListener(new AndroidSłuchaczLifeMinus(glowne.findViewById(R.id.life_opis_0),activity));
        ((Button) glowne.findViewById(R.id.plus_energy_0)).setOnClickListener(new AndroidSłuchaczLifePlus(glowne.findViewById(R.id.energy_opis_0)));
        ((Button) glowne.findViewById(R.id.minus_energy_0)).setOnClickListener(new AndroidSłuchaczLifeMinus(glowne.findViewById(R.id.energy_opis_0),activity));
        ((TextView) glowne.findViewById(R.id.imie_)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View vw) {
                final AlertDialog dialog = new AlertDialog.Builder(activity).create();
                dialog.setTitle(R.string.parametr_name);
                final View inf = activity.getLayoutInflater().inflate(R.layout.dialog_alert, null);
                dialog.setView(inf);
                dialog.show();
                ((Button) inf.findViewById(R.id.ok_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = ((EditText) inf.findViewById(R.id.parametr_dadatkowy)).getText().toString();
                        if (text.trim().equals("")) {
                            Toast.makeText(activity.getApplicationContext(), R.string.pusty_parametr, Toast.LENGTH_SHORT).show();
                        } else {
                            ((TextView) vw).setText(text.trim());
                            dialog.dismiss();
                        }
                    }
                });
                ((Button) inf.findViewById(R.id.anuluj_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

            }
        });
        ((Button) glowne.findViewById(R.id.dodatkowy_button_plus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dodatkowe = factory.inflate(R.layout.dodatkowe_liczniki, null);
                if (koniec_plus) {
                    final AlertDialog dialog = new AlertDialog.Builder(activity).create();
                    dialog.setTitle(R.string.parametr);
                    final View inf = activity.getLayoutInflater().inflate(R.layout.dialog_alert, null);
                    dialog.setView(inf);
                    dialog.show();
                    ((Button) inf.findViewById(R.id.ok_button)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String text = ((EditText) inf.findViewById(R.id.parametr_dadatkowy)).getText().toString();
                            if (text.trim().equals("")) {
                                Toast.makeText(activity.getApplicationContext(), R.string.pusty_parametr, Toast.LENGTH_SHORT).show();
                            } else {

                                if (ST == 3) {

                                    ((TextView) dodatkowe.findViewById(R.id.opis_dodatkowy)).setText(text.trim());
                                    ((Button) dodatkowe.findViewById(R.id.plus_datatkowy)).setOnClickListener(new AndroidSłuchaczLifePlus(dodatkowe.findViewById(R.id.dodatek_opis_dodatkowy)));
                                    ((Button) dodatkowe.findViewById(R.id.minus_dodatkowy)).setOnClickListener(new AndroidSłuchaczLifeMinus(dodatkowe.findViewById(R.id.dodatek_opis_dodatkowy),activity));
                                    player.addView(dodatkowe, ST);
                                    koniec_plus = false;
                                    dialog.dismiss();

                                } else {
                                    ((TextView) dodatkowe.findViewById(R.id.opis_dodatkowy)).setText(text);
                                    ((Button) dodatkowe.findViewById(R.id.plus_datatkowy)).setOnClickListener(new AndroidSłuchaczLifePlus(dodatkowe.findViewById(R.id.dodatek_opis_dodatkowy)));
                                    ((Button) dodatkowe.findViewById(R.id.minus_dodatkowy)).setOnClickListener(new AndroidSłuchaczLifeMinus(dodatkowe.findViewById(R.id.dodatek_opis_dodatkowy),activity));
                                    player.addView(dodatkowe, ST);
                                    dialog.dismiss();
                                }
                                koniec_minus = true;
                                ST++;
                            }
                        }
                    });
                    ((Button) inf.findViewById(R.id.anuluj_button)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                }
            }
        });
        ((Button) glowne.findViewById(R.id.dodatkowy_button_minus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (koniec_minus) {
                        ST--;
                        if (ST == 0) {
                            player.removeViewAt(ST);
                            koniec_minus = false;

                        } else {
                            player.removeViewAt(ST);
                        }
                        koniec_plus = true;
                    }
                } catch (Exception e) {
                    Toast.makeText(activity.getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        usunMana();
    }
    public LinearLayout getPlayer()
    {
        return player;
    }
    public void setPaddingForFirstPlayer(int howmuch,int howmuchRight)
    {
        glowne.setPadding(howmuch,0,howmuchRight,0);
    }
    public void setPaddingRight(int howmuch)
    {
        glowne.setPadding(0,0,howmuch,0);
    }
    public void setPaddingLeft(int howmuch)
    {
        glowne.setPadding(howmuch,0,0,0);
    }
    public void clearPadding()
    {
        glowne.setPadding(0,0,0,0);
    }
    public View getGlowne()
    {
        return glowne;
    }
    public void usunMana()
    {
        ((LinearLayout) glowne.findViewById(R.id.kontener_mana)).setVisibility(View.GONE);
    }
    public void pokazMana()
    {
        ((LinearLayout) glowne.findViewById(R.id.kontener_mana)).setVisibility(View.VISIBLE);
    }
    private Activity activity;
    private View glowne;
    private int ST = 0;
    private boolean koniec_plus = true;
    private boolean koniec_minus = false;
    private LinearLayout player;
    private LinearLayout player_mana;
    private static final int SELECT_PICTURE = 1;
}

