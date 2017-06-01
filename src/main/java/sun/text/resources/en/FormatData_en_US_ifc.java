package sun.text.resources.en;

import java.util.ListResourceBundle;

/**
 * Created by carlo on 16.12.14.
 */
public class FormatData_en_US_ifc extends ListResourceBundle {
    @Override
    protected Object[][] getContents () {
        return new Object[][] {
                { "NumberPatterns",
                        new String[] { "#,##0.###;-#,##0.###", "¤#,##0.00;(¤#,##0.00)", "#,##0%" } },
                { "MonthNames",
                        new String[] { "January", "February", "March", "April", "May", "June", "Sol", "July", "August", "September", "October", "November", "December" } },
                { "MonthAbbreviations",
                        new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Sol", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" } }

        };
    }
}
