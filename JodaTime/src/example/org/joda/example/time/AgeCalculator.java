/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-03 Stephen Colebourne.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
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
import java.text.ParseException;

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
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.iso.ISOChronology;

/**
 * AgeCalculator is a small Swing application that computes age from a specific
 * birthdate and time zone. Age is broken down into multiple fields, which can
 * be independently disabled.
 *
 * @author Brian S O'Neill
 */
public class AgeCalculator extends JFrame {
    static final int
        YEAR = 1,
        MONTH_OF_YEAR = 2,
        DAY_OF_MONTH = 3,
        WEEKYEAR = 4,
        WEEK_OF_WEEKYEAR = 5,
        DAY_OF_WEEK = 6,
        HOUR_OF_DAY = 101,
        MINUTE_OF_HOUR = 102,
        SECOND_OF_MINUTE = 103;

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
                iChronology = ISOChronology.getInstance(DateTimeZone.getInstance(id));
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
                new FieldGroup(listener, "Years", YEAR),
                new FieldGroup(listener, "Months", MONTH_OF_YEAR),
                new FieldGroup(listener, "Days", DAY_OF_MONTH),
                new FieldGroup(listener, "Hours", HOUR_OF_DAY),
                new FieldGroup(listener, "Minutes", MINUTE_OF_HOUR),
                new FieldGroup(listener, "Seconds", SECOND_OF_MINUTE)
            })
            ,
            new FieldSet("Week Based", new FieldGroup[] {
                new FieldGroup(listener, "Weekyears", WEEKYEAR),
                new FieldGroup(listener, "Weeks", WEEK_OF_WEEKYEAR),
                new FieldGroup(listener, "Days", DAY_OF_WEEK),
                new FieldGroup(listener, "Hours", HOUR_OF_DAY),
                new FieldGroup(listener, "Minutes", MINUTE_OF_HOUR),
                new FieldGroup(listener, "Seconds", SECOND_OF_MINUTE)
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
        catch (ParseException e) {
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

            DateTimeField field;
            switch (iFieldType) {
            case YEAR:
                field = iChronology.year();
                break;
            case MONTH_OF_YEAR:
                field = iChronology.monthOfYear();
                break;
            case DAY_OF_MONTH:
                field = iChronology.dayOfMonth();
                break;
            case WEEKYEAR:
                field = iChronology.weekyear();
                break;
            case WEEK_OF_WEEKYEAR:
                field = iChronology.weekOfWeekyear();
                break;
            case DAY_OF_WEEK:
                field = iChronology.dayOfWeek();
                break;
            case HOUR_OF_DAY:
                field = iChronology.hourOfDay();
                break;
            case MINUTE_OF_HOUR:
                field = iChronology.minuteOfHour();
                break;
            case SECOND_OF_MINUTE: default:
                field = iChronology.secondOfMinute();
                break;
            }

            String textToSet = "";

            if (iCheckbox.isSelected()) {
                long difference = field.getDifference(minuend, subtrahend);
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
