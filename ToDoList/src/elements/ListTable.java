package elements;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ListTable extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<Entry> todolist;
	private Boolean modified = false;
	private ToDoList td;

	public ListTable(ToDoList td) {
		this.td = td;
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
		setModifiedTrue();
	}

	public boolean isEmpty() {
		return todolist.isEmpty();
	}

	public void removeRow(int row) {
		fireTableRowsDeleted(row, row);
		todolist.remove(row);
		setModifiedTrue();
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
			setModifiedTrue();
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
		fireTableRowsInserted(todolist.size()-1, todolist.size()-1);
	}
	
	public void addEntry(Entry e) {
		// Eintrag der Liste hinzufuegen
		todolist.add(e);
		// Event an Listener abschicken, Eintrag wird am Ende der Liste
		// angefuegt
		fireTableRowsInserted(todolist.size()-1, todolist.size()-1);
		setModifiedTrue();
	}

	public void editEntry(int row, Entry e) {
		todolist.set(row, e);
		fireTableRowsUpdated(row, row);
		setModifiedTrue();
	}

	public ArrayList<Entry> getTodolist() {
		return todolist;
	}
	
	public void setModifiedTrue(){
		modified = true;
		td.setFileModified();
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

