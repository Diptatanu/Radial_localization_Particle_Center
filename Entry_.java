
import ij.plugin.PlugIn;
import javax.swing.SwingUtilities;
import gui.GuiRadiality;
import radiality.Radial_grad_map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author danielaik
 */
public class Entry_ implements PlugIn {
    
     @Override
    public void run(String arg0) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Radial_grad_map radial_obj = new Radial_grad_map();
                GuiRadiality gui_obj = new GuiRadiality(radial_obj);
                gui_obj.createPanel(); //ICCS panel

            }
        }
        );

    }

}
