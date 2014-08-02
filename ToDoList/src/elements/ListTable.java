package elements;

import java.io.File;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ListTable extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<Entry> todolist;
	private Boolean modified = false;

	public ListTable() {
		todolist = new ArrayList<Entry>();
	}
	
	public Boolean getModified(){
		return modified;
	}
	
	public void setModified(Boolean mod){
		modified = mod;
	}

	// Tabledaten aus Datei einlesen
	public void fillTable(ArrayList<Entry> newList) {
		todolist = new ArrayList<Entry>(); //erstmal leeren
		for(Entry e : newList){
			fillTableWithEntry(e);
		}
		System.out.println("Table filled!");
	}

	public void clearTable() {
		for (int i = this.getRowCount() - 1; i >= 0; i--) {
			removeRow(i);
		}
		modified = true;
	}

	public boolean isEmpty() {
		return todolist.isEmpty();
	}

	public void removeRow(int row) {
		fireTableRowsDeleted(row, row + 1);
		todolist.remove(row);
		modified = true;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	/**
	 * rowcount = Zeilenanzahl
	 */
	public int getRowCount() {
		return todolist.size();
	}

	public Entry getEintragAt(int rowIndex) {
		return todolist.get(rowIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int colIndex) {
		// Eintrag der Zeile
		Entry e = todolist.get(rowIndex);
		// Attribut des Eintrags in der jeweiligen Spalte
		switch (colIndex) {
		case 0:
			return e.getAufgabe();
		case 1:
			return e.getEndzeitpunkt();
		case 2:
			if(e.getEnddate() != null){
				e.berechneRestzeit(e.getEnddate());
			}
			return e.getRestzeit();
		case 3:
			return e.getErledigt();
		default:
			return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == 3) {
			Entry e = getEintragAt(rowIndex);
			e.setErledigt((boolean) aValue);
			fireTableRowsUpdated(rowIndex, rowIndex);
		}
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "ToDo";
		case 1:
			return "End time";
		case 2:
			return "Remaining time";
		default:
			return "Done";
		}
	}
	
	public void fillTableWithEntry(Entry e){
		todolist.add(e);
		fireTableRowsInserted(todolist.size(), todolist.size());
	}
	
	public void addEntry(Entry e) {
		// Eintrag der Liste hinzufuegen
		todolist.add(e);
		// Event an Listener abschicken, Eintrag wird am Ende der Liste
		// angefuegt
		fireTableRowsInserted(todolist.size(), todolist.size());
		modified = true;
	}

	public void editEntry(int row, Entry e) {
		todolist.set(row, e);
		fireTableRowsUpdated(row, row);
		modified = true;
	}

	public ArrayList<Entry> getTodolist() {
		return todolist;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 3)
			return true;
		else
			return false;
	}

	@Override
	public Class<?> getColumnClass(int column) {
		if (column == 3)
			return Boolean.class;
		return super.getColumnClass(column);
	}

}

