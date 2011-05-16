/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.example.time;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.chrono.ISOChronology;

/**
 * AgeCalculator is a small Swing application that computes age from a specific
 * birthdate and time zone. Age is broken down into multiple fields, which can
 * be independently disabled.
 *
 * @author Brian S O'Neill
 */
public class AgeCalculator extends JFrame {
    static final int
        YEARS = 1,
        MONTHS = 2,
        DAYS = 3,
        WEEKYEARS = 4,
        WEEKS = 5,
        HOURS = 101,
        MINUTES = 102,
        SECONDS = 103;

    public static void main(String[] args) throws Exception {
        new AgeCalculator().show();
    }

    static JComponent fixedSize(JComponent component) {
        component.setMaximumSize(component.getPreferredSize());
        return component;
    }

    static JComponent fixedHeight(JComponent component) {
        Dimension dim = component.getMaximumSize();
        dim.height = component.getPreferredSize().height;
        component.setMaximumSize(dim);
        return component;
    }

    Chronology iChronology;

    private String iBirthdateStr;
    private FieldSet[] iFieldSets;
    private Timer iTimer;

    public AgeCalculator() {
        super();

        iChronology = ISOChronology.getInstance();
        iBirthdateStr = "1970-01-01T00:00:00";

        setTitle("Age Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMainArea(getContentPane());
        addNotify();
        Dimension size = getPreferredSize();
        setSize(size);
        Dimension screenSize = getToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - size.width / 2,
                    screenSize.height / 2 - size.height / 2);

        iTimer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateResults();
            }
        });

        iTimer.setInitialDelay(0);
        iTimer.start();
    }

    private void addMainArea(Container container) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        addTopArea(panel);
        panel.add(Box.createVerticalStrut(10));
        addBottomArea(panel);
        panel.add(Box.createVerticalGlue());

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        container.add(panel);
    }

    private void addTopArea(Container container) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(fixedHeight(new JLabel("Birthdate")));
        panel.add(Box.createHorizontalStrut(10));

        final JTextField birthdateField = new JTextField(iBirthdateStr + ' ');
        Document doc = birthdateField.getDocument();
        doc.addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                update(e);
            }
            public void removeUpdate(DocumentEvent e) {
                update(e);
            }
            public void changedUpdate(DocumentEvent e) {
                update(e);
            }
            private void update(DocumentEvent e) {
                iBirthdateStr = birthdateField.getText();
                updateResults();
            }
        });
        panel.add(fixedHeight(birthdateField));

        panel.add(Box.createHorizontalStrut(10));

        Object[] ids = DateTimeZone.getAvailableIDs().toArray();
        final JComboBox zoneSelector = new JComboBox(ids);
        zoneSelector.setSelectedItem(DateTimeZone.getDefault().getID());
        panel.add(fixedSize(zoneSelector));

        zoneSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = (String)zoneSelector.getSelectedItem();
                iChronology = ISOChronology.getInstance(DateTimeZone.forID(id));
                updateResults();
            }
        });

        container.add(fixedHeight(panel));
    }

    private void addBottomArea(Container container) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        ItemListener listener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateResults();
            }
        };

        iFieldSets = new FieldSet[] {
            new FieldSet("Month Based", new FieldGroup[] {
                new FieldGroup(listener, "Years", YEARS),
                new FieldGroup(listener, "Months", MONTHS),
                new FieldGroup(listener, "Days", DAYS),
                new FieldGroup(listener, "Hours", HOURS),
                new FieldGroup(listener, "Minutes", MINUTES),
                new FieldGroup(listener, "Seconds", SECONDS)
            })
            ,
            new FieldSet("Week Based", new FieldGroup[] {
                new FieldGroup(listener, "Weekyears", WEEKYEARS),
                new FieldGroup(listener, "Weeks", WEEKS),
                new FieldGroup(listener, "Days", DAYS),
                new FieldGroup(listener, "Hours", HOURS),
                new FieldGroup(listener, "Minutes", MINUTES),
                new FieldGroup(listener, "Seconds", SECONDS)
            })
        };

        for (int i=0; i<iFieldSets.length; i++) {
            if (i > 0) {
                panel.add(Box.createHorizontalStrut(10));
            }
            iFieldSets[i].addTo(panel);
        }
        panel.add(Box.createVerticalGlue());

        container.add(fixedHeight(panel));
    }

    private void updateResults() {
        try {
            DateTime dt = new DateTime(iBirthdateStr.trim(), iChronology);

            long minuend = System.currentTimeMillis();
            long subtrahend = dt.getMillis();

            for (int i=0; i<iFieldSets.length; i++) {
                iFieldSets[i].updateResults(minuend, subtrahend);
            }
        }
        catch (IllegalArgumentException e) {
            for (int i=0; i<iFieldSets.length; i++) {
                iFieldSets[i].setResultsText("");
            }
        }
    }

    private class FieldGroup {
        public final JCheckBox iCheckbox;
        public final JTextField iResult;
        public final int iFieldType;

        FieldGroup(ItemListener listener, String checkboxText, int fieldType) {
            iCheckbox = new JCheckBox(checkboxText, true);
            iCheckbox.addItemListener(listener);
            iResult = new JTextField();
            iResult.setEditable(false);
            iFieldType = fieldType;
        }

        public long updateResult(long minuend, long subtrahend) {
            // Because time zone can be dynamically changed, field must be
            // dynamically acquired.

            DurationField field;
            switch (iFieldType) {
            case YEARS:
                field = iChronology.years();
                break;
            case MONTHS:
                field = iChronology.months();
                break;
            case DAYS:
                field = iChronology.days();
                break;
            case WEEKYEARS:
                field = iChronology.weekyears();
                break;
            case WEEKS:
                field = iChronology.weeks();
                break;
            case HOURS:
                field = iChronology.hours();
                break;
            case MINUTES:
                field = iChronology.minutes();
                break;
            case SECONDS: default:
                field = iChronology.seconds();
                break;
            }

            String textToSet = "";

            if (iCheckbox.isSelected()) {
                long difference = field.getDifferenceAsLong(minuend, subtrahend);
                textToSet = Long.toString(difference);
                subtrahend = field.add(subtrahend, difference);
            }

            if (!iResult.getText().equals(textToSet)) {
                iResult.setText(textToSet);
            }

            return subtrahend;
        }

        public void setResultText(String text) {
            iResult.setText(text);
        }
    }

    private static class FieldSet {
        private final String iTitle;
        private final FieldGroup[] iGroups;

        FieldSet(String title, FieldGroup[] groups) {
            iTitle = title;
            iGroups = groups;
        }

        private long updateResults(long minuend, long subtrahend) {
            for (int i=0; i<iGroups.length; i++) {
                subtrahend = iGroups[i].updateResult(minuend, subtrahend);
            }
            return subtrahend;
        }

        public void setResultsText(String text) {
            for (int i=0; i<iGroups.length; i++) {
                iGroups[i].setResultText(text);
            }
        }

        private void addTo(Container container) {
            JPanel panel = new JPanel();
            GridBagLayout layout = new GridBagLayout();
            panel.setLayout(layout);

            panel.setBorder(BorderFactory.createTitledBorder(iTitle));

            for (int i=0; i<iGroups.length; i++) {
                FieldGroup fg = iGroups[i];
                panel.add(fg.iCheckbox);
                setCheckboxConstraints(layout, fg.iCheckbox, 0, i);
                panel.add(fg.iResult);
                setResultConstraints(layout, fg.iResult, 1, i);
            }

            container.add(fixedHeight(panel));
        }

        private void setCheckboxConstraints(GridBagLayout layout, Component c,
                                            int x, int y)
        {
            GridBagConstraints cons = new GridBagConstraints();
            cons.gridx = x;
            cons.gridy = y;
            cons.weightx = 0.1;
            cons.anchor = GridBagConstraints.WEST;
            layout.setConstraints(c, cons);
        }

        private void setResultConstraints(GridBagLayout layout, Component c,
                                          int x, int y)
        {
            GridBagConstraints cons = new GridBagConstraints();
            cons.gridx = x;
            cons.gridy = y;
            cons.weightx = 1.0;
            cons.anchor = GridBagConstraints.WEST;
            cons.fill = GridBagConstraints.HORIZONTAL;
            layout.setConstraints(c, cons);
        }
    }
}
