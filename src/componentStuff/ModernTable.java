package componentStuff;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ModernTable extends JTable {
    
    private static final Color HEADER_COLOR = new Color(52, 73, 94);
    private static final Color HEADER_TEXT_COLOR = Color.WHITE;
    private static final Color ROW_COLOR_1 = Color.WHITE;
    private static final Color ROW_COLOR_2 = new Color(248, 249, 250);
    private static final Color SELECTION_COLOR = new Color(74, 144, 226);
    private static final Color SELECTION_TEXT_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Font CELL_FONT = new Font("Arial", Font.PLAIN, 11);
    
    public ModernTable() {
        super();
        initializeStyle();
    }
    
    public ModernTable(DefaultTableModel model) {
        super(model);
        initializeStyle();
    }
    
    public ModernTable(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
        initializeStyle();
    }
    
    private void initializeStyle() {
        // Table appearance
        setFont(CELL_FONT);
        setRowHeight(32);
        setGridColor(BORDER_COLOR);
        setShowGrid(true);
        setIntercellSpacing(new Dimension(1, 1));
        setSelectionBackground(SELECTION_COLOR);
        setSelectionForeground(SELECTION_TEXT_COLOR);
        setFillsViewportHeight(true);
        
        // Enable auto-resizing for responsiveness
        setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        
        // Header styling - ensure proper rendering when called from login
        JTableHeader header = getTableHeader();
        header.setBackground(HEADER_COLOR);
        header.setForeground(HEADER_TEXT_COLOR);
        header.setFont(HEADER_FONT);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));
        header.setReorderingAllowed(false);
        // Force header to be opaque to ensure proper background rendering
        header.setOpaque(true);
        
        // Custom header renderer to ensure consistent styling
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(HEADER_COLOR);
                c.setForeground(HEADER_TEXT_COLOR);
                c.setFont(HEADER_FONT);
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
                return c;
            }
        });
        
        // Custom cell renderer for alternating row colors
        setDefaultRenderer(Object.class, new ModernTableCellRenderer());
        
        // Selection mode
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private class ModernTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (!isSelected) {
                if (row % 2 == 0) {
                    c.setBackground(ROW_COLOR_1);
                } else {
                    c.setBackground(ROW_COLOR_2);
                }
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(SELECTION_COLOR);
                c.setForeground(SELECTION_TEXT_COLOR);
            }
            
            setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
            setFont(CELL_FONT);
            
            return c;
        }
    }
}