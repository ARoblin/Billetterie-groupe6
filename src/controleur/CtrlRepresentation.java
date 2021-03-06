/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import modele.dao.DaoRepresentation;
import modele.metier.Representation;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTable;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import vue.VueRepresentation;


/**
 *
 * @author wquentel
 */
public class CtrlRepresentation implements WindowListener,MouseListener, ActionListener{
    private vue.VueRepresentation reserv;
    private ArrayList<Representation> lesRepresentations;
    private CtrlPrincipal ctrlPrincipal;
    
    
    public CtrlRepresentation(vue.VueRepresentation vue, CtrlPrincipal ctrl){
        this.reserv=vue;
        this.reserv.addWindowListener(this);
        this.reserv.getjTable1().addMouseListener(this);
        this.ctrlPrincipal = ctrl;
        afficheLesReserv();
        reserv.getJButtonCommander().addActionListener(this);
        reserv.getJButtonRetour().addActionListener(this);
    }
    
    private void afficheLesReserv() {
        try {
            lesRepresentations= (ArrayList<Representation>) DaoRepresentation.selectAll();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlRepresentation.class.getName()).log(Level.SEVERE, null, ex);
        }
        JTable jtable1 = this.reserv.getjTable1();
        DefaultTableModel model = (DefaultTableModel) jtable1.getModel();
        
        for (Representation uneRepresentation : lesRepresentations){
            model.addRow(new Object[]{uneRepresentation.getGroupe()});
        }
        
        this.reserv.setjTable1(jtable1);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == reserv.getJButtonCommander()) {
             ctrlPrincipal.afficherLaBilleterie();
        }
        if (e.getSource() == reserv.getJButtonRetour()) {
             ctrlPrincipal.afficherLeMenu() ;
        }
    }
    
    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        reserv.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        ctrlPrincipal.quitterApplication();
    }

    @Override
    public void windowClosed(WindowEvent e) {             
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        int row = reserv.getjTable1().getSelectedRow();
        String groupeChoisis = (String) reserv.getjTable1().getValueAt(row, 0);
        String groupeChoisisRes = null;
        try {
            groupeChoisisRes = DaoRepresentation.selectRepresentationParGroupe(groupeChoisis).toString();
        } catch (SQLException ex) {
            Logger.getLogger(CtrlRepresentation.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Date currentTime = new Date();
            String dateDebut = DaoRepresentation.selectRepresentationParGroupe(groupeChoisis).getDate();
            String heureDebut = DaoRepresentation.selectRepresentationParGroupe(groupeChoisis).getHeureDebut();
            int annee = Integer.parseInt(dateDebut.substring(0,4));
            int mois = Integer.parseInt(dateDebut.substring(5,7));
            int jour = Integer.parseInt(dateDebut.substring(8,10));
            int heure = Integer.parseInt(heureDebut.substring(0,2));
            int minutes = Integer.parseInt(heureDebut.substring(3,5));
            
            Date dateConcert = new Date(annee-1900, mois, jour,heure,minutes);
            System.out.println(annee + " " +mois + " " +jour);
            System.out.println(currentTime);
            System.out.println(dateConcert);
            if(DaoRepresentation.selectRepresentationParGroupe(groupeChoisis).getPlacesDispo()==0 && dateConcert.before(currentTime)){
                reserv.getjLabel3().setText("Le concert est passé et était complet");
            }else if(dateConcert.before(currentTime)){
                reserv.getjLabel3().setText("Le concert est passé");
            }else if(DaoRepresentation.selectRepresentationParGroupe(groupeChoisis).getPlacesDispo()==0){
                reserv.getjLabel3().setText("Il n'y a plus de places");
            }
            else{
                reserv.getjLabel3().setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CtrlRepresentation.class.getName()).log(Level.SEVERE, null, ex);
        }
        reserv.getjLabel2().setText(groupeChoisisRes);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public VueRepresentation getReserv() {
        return reserv;
    }   
}