package br.com.potierp.modulos.rh.managed.dataprovider;

import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;

import br.com.potierp.infra.helper.SelectionEntity;
import br.com.potierp.model.BaseEntity;

public class PageableExtendedTableDataModel<T extends SelectionEntity<? extends BaseEntity>> extends ExtendedTableDataModel<T> {

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer rowCount;

	public PageableExtendedTableDataModel(DataProvider<T> dataProvider) {
		super(dataProvider);
	}
	
	protected List<T> loadData(int startRow, int endRow) {
		return getDataProvider().getItemsByRange(startRow, endRow);
	}
	
	public void walk(FacesContext context, DataVisitor visitor, Range range,
			Object argument) throws IOException {
		super.walk(context, visitor, range, argument);
	}
	
	@Override
	public int getRowCount() {
		if (rowCount == null) {
			rowCount = new Integer(getDataProvider().getRowCount());
		} else {
			return rowCount.intValue();
		}
		return rowCount.intValue();
	}
	
}
