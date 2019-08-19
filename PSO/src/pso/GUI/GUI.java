/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import static javax.swing.GroupLayout.Alignment.valueOf;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.text.MaskFormatter;
import pso.config.Compute;
import pso.config.Configuration;
import pso.config.ConfigurationManager;
import pso.function.Function;
import pso.function.FunctionType;
import pso.function.FunctionType;
import pso.jPanel.PSOPanel;

/**
 *
 * @author Aastha
 */
public class GUI extends JFrame implements Runnable {

    private static final String DEFAULT_LABEL_FONT = "Calibri";
    private static final int DEFAULT_LABEL_FONT_SIZE = 14;
    private static final int DEFAULT_LABEL_FONT_STYLE = Font.BOLD;

    private static final String DEFAULT_INPUT_FONT = "Tahoma";
    private static final int DEFAULT_INPUT_FONT_STYLE = Font.PLAIN;

    private PSOPanel draw;
    private ConfigurationManager configurationManager;
    private Timer timer;
    private int counter = -1;
    private JLabel aboutLabel = new JLabel();
    private JLabel const1Label = new JLabel();
    private JSpinner const1Spinner = new JSpinner();
    private JLabel const2Label = new JLabel();
    private JSpinner const2Spinner = new JSpinner();
    private JComboBox<String> functionComboBox = new JComboBox<>();
    private JLabel functionLabel = new JLabel();
    private JLabel particlesLabel = new JLabel();
    private JSpinner particlesSpinner = new JSpinner();
    private JLabel iterationLabel = new JLabel();
    private JSpinner iterationSpinner = new JSpinner();
    private JLabel parameterLabel = new JLabel();
    private JPanel parameterPanel = new JPanel();
    private JLabel velocityLabel = new JLabel();
    private JSpinner velocitySpinner = new JSpinner();
    private JButton startButton = new JButton();
    private JButton BtnNumberOfTasks = new JButton();
    private JTextField txtNumberTasks = new JTextField();
    private JTextField txtTask1 = new JTextField();
    private JTextField txtTask2 = new JTextField();
    private JTextField txtTask3 = new JTextField();
    private JLabel visualisationLabel = new JLabel();
    private JPanel visualisationPanel = new JPanel();
    private JButton quitButton = new JButton();
    private Configuration  objConfig= null;
    private static JLabel status = new JLabel();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new GUI();
    }

    @Override
    public void run() {
        initComponents();
        draw.setLayout(new BorderLayout());
        visualisationPanel.add(draw, BorderLayout.CENTER);
        setVisible(true);
    }

    private GUI() {
        try {
            EventQueue.invokeAndWait(this);
        } catch (InterruptedException | InvocationTargetException e) {
        }
    }

    //Updates the status on the panel
    private String updateStatus() {
        DecimalFormat fmt = new DecimalFormat("0.0000");
        return "Step: " + draw.getIteration()
                + ", best: f(" + fmt.format(draw.getBestPositionX())
                + "," + fmt.format(draw.getBestPositionY())
                + ") = " + fmt.format(draw.status);
    }

    //For Updating Swarm on the panel(create threads for each swarm)
    private void updateUI(int iterations, int delay, final double const1, final double const2, double[] processedTask) {
        draw.setProcessedData(processedTask);
        if (counter >= 0) {
            timer.stop();
            counter = -1;
            return;
        }

        if (delay <= 0) {
            while (iterations >= 0) {
                draw.changeSwarm(const1, const2);
            }
            draw.repaint();
            delay--;
            return;
        }

        counter = iterations;
        timer = new Timer(delay, e -> {
            if (--counter < 0) {
                ArrayList<Compute> cmp = objConfig.getTop3Compute();
                
                System.out.println("Ideal config's are EC2: "+ (cmp.get(0).EC2 )
                       + "\tRDS: " + (cmp.get(0).RDS ) + "\tS3: " + (cmp.get(0).S3));
                
//               System.out.println("Ideal config's are EC2: "+ (cmp.get(0).EC2 + cmp.get(1).EC2 + cmp.get(2).EC2)
//                       + "\tRDS: " + (cmp.get(0).RDS + cmp.get(1).RDS + cmp.get(2).RDS) + "\tS3: " + (cmp.get(0).S3 + cmp.get(1).S3 + cmp.get(2).S3));
                timer.stop();
                return;
            }
            draw.changeSwarm(const1, const2);
            draw.repaint();
            status.setText(updateStatus());
        });
        timer.start();
    }

    private void initMainLayout() {
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(visualisationPanel, GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                                        .addGap(18, 18, 18))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(visualisationLabel, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
                                        .addGap(404, 404, 404)))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(parameterLabel)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(aboutLabel))
                                .addGroup(GroupLayout.Alignment.TRAILING, layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(parameterPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(status, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addComponent(startButton)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(quitButton)
                                                                )
                                                                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                        .addComponent(txtNumberTasks)
                                                                        .addComponent(txtTask1)
                                                                        .addComponent(txtTask2)
                                                                        .addComponent(txtTask3)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(BtnNumberOfTasks))
                                                        )))))
                        .addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(visualisationLabel)
                                                .addComponent(parameterLabel, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
                                .addComponent(aboutLabel))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(parameterPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE))
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(startButton))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(quitButton)))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(status))
                                .addComponent(visualisationPanel, GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE))
                        .addComponent(txtNumberTasks)
                        .addComponent(BtnNumberOfTasks)
                        .addContainerGap()
                        .addComponent(txtTask1)
                        .addComponent(txtTask2)
                        .addComponent(txtTask3)
                        .addContainerGap()));
    }

    private void functionComboBoxActionPerformed(ActionEvent evt) {
        draw.setFunction(FunctionType.valueOf(((String) functionComboBox.getSelectedItem())));
    }

    private void initConfigLayout() {
        GroupLayout configPanelLayout = new GroupLayout(parameterPanel);
        parameterPanel.setLayout(configPanelLayout);
        configPanelLayout.setHorizontalGroup(
                configPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(configPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(functionComboBox, 0, 316, Short.MAX_VALUE)
                                .addComponent(functionLabel)
                                .addGroup(configPanelLayout.createSequentialGroup()
                                        .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(iterationLabel)
                                                .addComponent(const1Label)
                                                .addComponent(velocityLabel)
                                                .addComponent(particlesLabel)
                                                .addComponent(const2Label)
                                                .addComponent(txtNumberTasks)
                                                .addComponent(txtTask1)
                                                .addComponent(txtTask2)
                                                .addComponent(txtTask3)
                                        )
                                        .addGap(20, 20, 20)
                                        .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(const2Spinner, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                                .addComponent(particlesSpinner, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                                .addComponent(iterationSpinner, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                                .addComponent(velocitySpinner, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                                .addComponent(const1Spinner, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                                .addComponent(BtnNumberOfTasks, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                                .addComponent(txtTask1, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                                .addComponent(txtTask2, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                                .addComponent(txtTask3, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                        )))
                        .addContainerGap()));
        configPanelLayout.setVerticalGroup(
                configPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(configPanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(functionLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(functionComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(iterationLabel)
                                .addComponent(iterationSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(particlesSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(particlesLabel))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(velocitySpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(velocityLabel))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(const1Spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(const1Label))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(const2Spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(const2Label))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(BtnNumberOfTasks, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNumberTasks))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTask1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTask2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTask3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        functionComboBox.setVisible(false);
        functionLabel.setVisible(false);
        BtnNumberOfTasks.setVisible(false);
        txtNumberTasks.setVisible(false);
    }

    private void initComponents() {
configurationManager = new ConfigurationManager();
objConfig = configurationManager.getConfig();
        draw = new PSOPanel(objConfig);
        //configurationManager = new ConfigurationManager();
        txtTask1.setText("0");
        txtTask2.setText("0");
        txtTask3.setText("0");
        //For Label
        aboutLabel.setFont(new Font(DEFAULT_INPUT_FONT, Font.BOLD, 20));
        aboutLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aboutLabel.setText("?");

        //For Start Button
        startButton.setText("START");
        startButton.addActionListener(this::startButtonActionPerformed);

        //For Status update on the Panel
        status.setFont(new Font(DEFAULT_INPUT_FONT, Font.PLAIN, 12));
        status.setText("Status...");

        //For Stop Button
        quitButton.setText("STOP");
        quitButton.addActionListener(this::stop);

        //For Parameter Labels
        parameterLabel.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, 18));
        parameterLabel.setText("Parameters");

        //For Visualization Label
        visualisationLabel.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, 18));
        visualisationLabel.setText("Visualisation");

        //For ParameterPanel
        parameterPanel.setBackground(new Color(255, 255, 255));
        parameterPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(0, 0, 0)));

        //For VisualisationPanel
        visualisationPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(0, 0, 0)));
        visualisationPanel.setLayout(new BorderLayout());

        //For the basewindow
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("PSO");
        setBackground(new Color(255, 255, 255));
        setMinimumSize(new Dimension(750, 530));
        setName("PSO");

        const2Spinner.setFont(new Font(DEFAULT_INPUT_FONT, DEFAULT_INPUT_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        const2Spinner.setModel(new SpinnerNumberModel(0.3d, 0.05d, 3.0d, 0.05d));

        const1Spinner.setFont(new Font(DEFAULT_INPUT_FONT, DEFAULT_INPUT_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        const1Spinner.setModel(new SpinnerNumberModel(0.3d, 0.05d, 3.0d, 0.05d));

        velocitySpinner.setFont(new Font(DEFAULT_INPUT_FONT, DEFAULT_INPUT_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        velocitySpinner.setModel(new SpinnerNumberModel(0.5d, 0.1d, 3.0d, 0.05d));

        particlesSpinner.setFont(new Font(DEFAULT_INPUT_FONT, DEFAULT_INPUT_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        particlesSpinner.setModel(new SpinnerNumberModel(30, 5, 500, 5));

        iterationSpinner.setFont(new Font(DEFAULT_INPUT_FONT, DEFAULT_INPUT_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        iterationSpinner.setModel(new SpinnerNumberModel(200, 10, 10000, 10));

        const2Label.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        const2Label.setText("Const2: ");

        const1Label.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        const1Label.setText("Const1: ");

        velocityLabel.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        velocityLabel.setText("Velocity: ");

        functionLabel.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        functionLabel.setText("Function: ");

        particlesLabel.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        particlesLabel.setText("Number of particles: ");

        iterationLabel.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        iterationLabel.setText("Number of steps: ");
        initFunctionComboBox();
        initMainLayout();
        initConfigLayout();
        initnumberOfTasks();
        pack();
        txtTask1.setVisible(true);
        txtTask2.setVisible(false);
        txtTask3.setVisible(false);
        txtNumberTasks.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                System.out.println("txtNumberTasks .keyPressed()");
                try {
                    int n = Integer.parseInt(txtNumberTasks.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter only numbers");
                    txtNumberTasks.setText("");
                }
            }
        });
        txtTask1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
//                boolean stillOK = true;
//                int myLength = txtTask1.getText().length();
//                String TmpBuffer = txtTask1.getText();
//                String myParser = new String();
//                for (int count = 0; count < myLength; count++) {
//                    myParser = TmpBuffer.substring(count, count + 1);
//                    if (myParser.compareTo(".") == 0) {
//                        break;
//                    } else {
//                        stillOK = false;
//                    }
//                }
//                if (stillOK == false) {
//                    //prompt user they have bad entry, and make them do it   over again
//                    JOptionPane.showMessageDialog(null, "Please enter only numbers");
//                    txtTask1.setText("");
//                }
//                System.out.println(" txtTask1 .keyPressed()");
                try {
                    Double n = Double.parseDouble(txtTask1.getText());
                    if (n < 0.0 || n > 500.0) {
                        JOptionPane.showMessageDialog(null, "Task Data should be between 0 and 5");
                        txtTask1.setText("");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter only numbers");
                    txtTask1.setText("");
                }
            }
        });
        txtTask2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
//                System.out.println(" txtTask2 .keyPressed()");
                try {
                    Double n = Double.parseDouble(txtTask1.getText());
                    if (n < 0.0 || n > 500.0) {
                        JOptionPane.showMessageDialog(null, "Task Data should be between 0 and 5");
                        txtTask2.setText("");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter only numbers");
                    txtTask2.setText("");
                }
//                boolean stillOK = true;
//                int myLength = txtTask2.getText().length();
//                String TmpBuffer = txtTask2.getText();
//                String myParser = new String();
//                for (int count = 0; count < myLength; count++) {
//                    myParser = TmpBuffer.substring(count, count + 1);
//                    if (myParser.compareTo(".") == 0) {
//                        break;
//                    } else {
//                        stillOK = false;
//                    }
//                }
//                if (stillOK == false) {
//                    //prompt user they have bad entry, and make them do it   over again
//                    JOptionPane.showMessageDialog(null, "Please enter only numbers");
//                    txtTask2.setText("");
//                }
            }
        });
        txtTask3.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {

//                System.out.println(" txtTask3 .keyPressed()");
                try {
                    Double n = Double.parseDouble(txtTask1.getText());
                    if (n < 0.0|| n > 500.0) {
                        JOptionPane.showMessageDialog(null, "Task Data should be between 0 and 5");
                        txtTask3.setText("");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter only numbers");
                    txtTask3.setText("");
                }
//                boolean stillOK = true;
//                int myLength = txtTask3.getText().length();
//                String TmpBuffer = txtTask3.getText();
//                String myParser = new String();
//                for (int count = 0; count < myLength; count++) {
//                    myParser = TmpBuffer.substring(count, count + 1);
//                    if (myParser.compareTo(".") == 0) {
//                        break;
//                    } else {
//                        stillOK = false;
//                    }
//                }
//                if (stillOK == false) {
//                    //prompt user they have bad entry, and make them do it   over again
//                    JOptionPane.showMessageDialog(null, "Please enter only numbers");
//                    txtTask3.setText("");
//                }
//    else{
//       //accept entry and go on
//     }
            }
        });
    }

    private void stop(ActionEvent evt) {
        setSpinner(true);
        draw.setFunction(configurationManager.getConfig().getFunctionType());
    }

    private void setSpinner(boolean block) {
        iterationSpinner.setEnabled(block);
        particlesSpinner.setEnabled(block);
        const2Spinner.setEnabled(block);
        velocitySpinner.setEnabled(block);
        const1Spinner.setEnabled(block);
    }

    private void startButtonActionPerformed(ActionEvent evt) {
        int x = 3;//Integer.parseInt(txtNumberTasks.getText());
        double[] tasksData = new double[x];

        tasksData[0] = Double.parseDouble(txtTask1.getText() != null || !"".equals(txtTask1.getText()) ? txtTask1.getText() : "0.0");
        tasksData[1] = Double.parseDouble(txtTask2.getText() != null || !"".equals(txtTask2.getText()) ? txtTask2.getText() : "0.0");
        tasksData[2] = Double.parseDouble(txtTask3.getText() != null || !"".equals(txtTask3.getText()) ? txtTask3.getText() : "0.0");
        System.out.println("length of task array" + tasksData.length + 1);
        Configuration config = configurationManager.getConfig();

        config.setFunctionType(FunctionType.valueOf(String.valueOf(functionComboBox.getSelectedItem())));
        config.setIterations((int) iterationSpinner.getValue());
        config.setParticles((int) particlesSpinner.getValue());
        config.setVelocity((double) velocitySpinner.getValue());
        config.setConst1((double) const1Spinner.getValue());
        config.setConst2((double) const2Spinner.getValue());
        config.setTasksData(tasksData);
        setSpinner(false);

        //draw.setFunction(config.getFunctionType());
        draw.createSwarm(config.getParticles());
        updateUI(config.getIterations(), 150, config.getConst1(), config.getConst2(), config.getTasksData());
    }

    private void initFunctionComboBox() {
        functionComboBox.setFont(new Font(DEFAULT_INPUT_FONT, DEFAULT_INPUT_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        functionComboBox.setModel(new DefaultComboBoxModel<>(EnumSet.allOf(FunctionType.class).stream()
                .map(FunctionType::toString)
                .collect(Collectors.toList())
                .toArray(new String[0])));
        functionComboBox.addActionListener(this::functionComboBoxActionPerformed);
    }

    private void initnumberOfTasks() {
        BtnNumberOfTasks.setText("Create Tasks");
        BtnNumberOfTasks.addActionListener(this::numberOfTasksActionPerformed);
    }

    private void numberOfTasksActionPerformed(ActionEvent evt) {

        String x = txtNumberTasks.getText();
        System.out.println(x);
        int countertextFields = Integer.parseInt(x);
        if (countertextFields == 1) {
            txtTask1.setVisible(true);
            txtTask2.setVisible(false);
            txtTask3.setVisible(false);
        }
        if (countertextFields == 2) {
            txtTask1.setVisible(true);
            txtTask2.setVisible(true);
            txtTask3.setVisible(false);
        }
        if (countertextFields == 3) {
            txtTask1.setVisible(true);
            txtTask2.setVisible(true);
            txtTask3.setVisible(true);
        }
        this.revalidate();
        this.repaint();
    }

}
