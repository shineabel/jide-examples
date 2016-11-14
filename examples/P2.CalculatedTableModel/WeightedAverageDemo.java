import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.AbstractCalculatedColumn;
import com.jidesoft.grid.CalculatedTableModel;
import com.jidesoft.grid.DefaultSummaryCalculator;
import com.jidesoft.pivot.*;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeightedAverageDemo extends AbstractDemo {
    private static final long serialVersionUID = 4227751179407714933L;

    public WeightedAverageDemo() {
        super();
    }

    public String getName() {
        return "Weighted Average Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_PIVOT;
    }

    @Override
    public String getDemoFolder() {
        return "P2.CalculatedTableModel";
    }

    @Override
    public String getDescription() {
        return "This demo is to demonstrate how you could utilize the DefaultSummaryCalculator to do a weighted total using two columns (one of the column is the weight) in the original table model.\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.CalculatedTableModel\n" +
                "com.jidesoft.pivot.PivotDataModel\n" +
                "com.jidesoft.pivot.DefaultSummaryCalculator";
    }

    /**
     * The internal class that includes both values so that in the summary calculator, we have access to both values.
     * From end user point of view, this field is not visible to them at all.
     */
    private class Weighted {
        double _netWeight;
        double _weight;

        public Weighted(double netWeight, double weight) {
            _netWeight = netWeight;
            _weight = weight;
        }
    }

    // The new summary type
    final static int SUMMARY_WEIGHTED_AVERAGE = PivotConstants.SUMMARY_RESERVED_MAX + 1;

    public Component getDemoPanel() {

        // The table data. It is just the fake data for the demo.
        Object[] columnNames = {"Reference", "Caliber", "Total Net Weight", "Weight"};
        Object[][] data = {
                {"63003", "40", 5300.0, 6.07},
                {"63003", "45", 5359.0, 5.95},
                {"63003", "55", 1666.0, 5.55},

                {"63004", "45", 2300.0, 3.23},
                {"63004", "50", 7000.0, 5.33},
                {"63004", "60", 5090.0, 8.29},
                {"63004", "50", 3800.0, 11.22},
        };

        // Create the default table model from the fake table data. You can use the data from the database or use ResultSetTableModel (from JIDE Data Grids) if you want, or whatever table model.
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            private static final long serialVersionUID = 893492030995940203L;

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                    case 1:
                        return String.class;
                    case 2:
                    case 3:
                        return Double.class;
                    default:
                        return Object.class;
                }
            }
        };

        // Create a CalculatedTableModel. This step is very important. It will remove the old "Weight" column and add a new "Weight" that take values
        // from both "Total Net Weight" and the old "Weight" columns.
        CalculatedTableModel calculated = new CalculatedTableModel(model);
        calculated.addAllColumns();
        // remove old "Weight" column
        calculated.removeColumn(calculated.getCalculatedColumnAt(3));
        // add new "Weight" column
        calculated.addColumn(new AbstractCalculatedColumn(model, "Weight") {
            private static final long serialVersionUID = -6899048527563480394L;

            @Override
            public Class<?> getColumnClass() {
                return Double.class;
            }

            @Override
            public Object getValueAt(int rowIndex) {
                // get values from the actual table model which is the DefaultTableModel above.
                return new Weighted((Double) getActualModel().getValueAt(rowIndex, 2), (Double) getActualModel().getValueAt(rowIndex, 3));
            }

            // this method is necessary so that when the two columns in the original table model changes value, the CalculatedTableModel will receive update automatically because it knows that it depends on those two columns.
            @Override
            public int[] getDependingColumns() {
                return new int[]{2, 3};
            }
        });

        PivotDataModel pivotDataModel = new CalculatedPivotDataModel(calculated);
        // this call is necessary so that the GrandTotal will use the original Weighted value instead of using the value after summarized.
        ((CalculatedPivotDataModel) pivotDataModel).setRunningCalculateSummary(false);

        pivotDataModel.setShowGrandTotalForRow(true);
        pivotDataModel.setShowGrandTotalForColumn(true);

        // this is where the calculation of the weighted total happens.
        pivotDataModel.setSummaryCalculator(new DefaultSummaryCalculator() {
            private List<Weighted> _weighted = new ArrayList<Weighted>();

            @Override
            public void addValue(Object object) {
                super.addValue(object);
                if (object instanceof Weighted) {
                    _weighted.add((Weighted) object);
                    _count ++;
                }
            }

            @Override
            public void clear() {
                super.clear();
                _weighted.clear();
            }

            @Override
            public int getNumberOfSummaries() {
                return super.getNumberOfSummaries() + 1;
            }

            @Override
            public String getSummaryName(Locale locale, int type) {
                if (type == SUMMARY_WEIGHTED_AVERAGE) {
                    return "WeightedAverage";
                }
                return super.getSummaryName(locale, type);
            }

            @Override
            public Object getSummaryResult(int type) {
                if (type == SUMMARY_WEIGHTED_AVERAGE) {
                    if (_weighted.size() == 0) {
                        return 0.0;
                    }
                    double totalNetWeight = 0.0;
                    double totalWeight = 0.0;
                    for (Weighted weighted : _weighted) {
                        totalNetWeight += weighted._netWeight;
                        totalWeight += weighted._netWeight * weighted._weight;
                    }
                    return totalWeight / totalNetWeight;
                }
                return super.getSummaryResult(type);
            }

            @Override
            public int[] getAllowedSummaries(Class<?> type, ConverterContext context) {
                int[] summaries = super.getAllowedSummaries(type, context);
                int[] newSummaries = new int[summaries.length + 1];
                System.arraycopy(summaries, 0, newSummaries, 0, summaries.length);
                newSummaries[summaries.length] = SUMMARY_WEIGHTED_AVERAGE;
                return newSummaries;
            }
        });

        // set up the fields
        PivotField field = pivotDataModel.getField(0); // Reference
        field.setAreaType(PivotConstants.AREA_ROW);
        field.setSubtotalType(PivotConstants.SUBTOTAL_AUTOMATIC);

        field = pivotDataModel.getField(1); // Caliber
        field.setAreaType(PivotConstants.AREA_ROW);

        field = pivotDataModel.getField(2); // Total Net Weight
        field.setAreaType(PivotConstants.AREA_DATA);
        field.setAreaIndex(0);

        field = pivotDataModel.getField(3); // Weight
        field.setAreaType(PivotConstants.AREA_DATA);
        field.setAreaIndex(1);

        // set the summary type to WeightedTotal, the new summary we added above
        field.setGrandTotalSummaryType(SUMMARY_WEIGHTED_AVERAGE);
        field.setSummaryType(SUMMARY_WEIGHTED_AVERAGE);


        return new PivotTablePane(pivotDataModel);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
                showAsFrame(new WeightedAverageDemo());
            }
        });
    }
}