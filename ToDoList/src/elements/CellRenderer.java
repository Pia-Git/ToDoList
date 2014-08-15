package elements;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CellRenderer extends DefaultTableCellRenderer{

	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if(table.getValueAt(row, column).equals("")){
        	//do nothing
        }
        else if(table.getValueAt(row, column).equals("0m")){
        	cellComponent.setForeground(Color.RED);
        }
        else if(table.getValueAt(row, column).toString().length() <= 3){
        	cellComponent.setForeground(new Color(220,102,0));
        }
        else{
        	cellComponent.setForeground(new Color(0,102,0));
        }
        return cellComponent;
    }
}
