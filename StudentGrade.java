import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class StudentGrade extends JFrame implements ActionListener{
   JTextField nameField, marks1, marks2, marks3, marks4, marks5;
   JButton calcButton;
   JTextArea resultArea;
   public StudentGrade(){
      setTitle("STUDENT GRADE CALCULATOR");
      setSize(460,520);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      setIconImage(new ImageIcon("1000104869.jpg").getImage());

      getContentPane().setBackground(new Color(220,240,225));

      setLayout(new FlowLayout(FlowLayout.CENTER,20,15));
      add(new JLabel("ENTER YOUR NAME: "));
      nameField=new JTextField(21);
      add(nameField);

      marks1=new JTextField(6);
      marks2=new JTextField(6);
      marks3=new JTextField(6);
      marks4=new JTextField(6);
      marks5=new JTextField(6);

      add (new JLabel("Marks1:")); add(marks1);
      add (new JLabel("Marks2:")); add(marks2);
      add (new JLabel("Marks3:")); add(marks3);
      add (new JLabel("Marks4:")); add(marks4);
      add (new JLabel("Marks5:")); add(marks5);


      calcButton =new JButton("CALCULATE");
      calcButton.setBackground(new Color(0,153,75));
      calcButton.setForeground(Color.LIGHT_GRAY);
      calcButton.addActionListener(this);
      add(calcButton);


      resultArea=new JTextArea(8,32);
      resultArea.setEditable(false);
      resultArea.setFont(new Font("Arial",Font.BOLD,16));
      add(resultArea);
      setVisible(true);

   }
   public void actionPerformed(ActionEvent e){

      try{
         String name=nameField.getText();
         int Marks1=Integer.parseInt(marks1.getText());
         int Marks2=Integer.parseInt(marks2.getText());
         int Marks3=Integer.parseInt(marks3.getText());
         int Marks4=Integer.parseInt(marks4.getText());
         int Marks5=Integer.parseInt(marks5.getText());

         int total= Marks1+Marks2+Marks3+Marks4+Marks5;
         double percentage=(total*100.0)/500;
         double cgpa=(total/5.0)/10.0;
         StringBuilder sb=new StringBuilder();
         sb.append("Your Name is: ").append(name).append("\n");
          if (total==500){
                sb.append("CONGARTULATION YOU ARE THE TOPPER:\n");
            }
            else if(total>200){
               sb.append("YOU ARE PASSED AND PROMOTED TO NEXT YEAR:  \n");
            }
            else{
                 sb.append("YOU ARE FAILED: \n");

            }
            sb.append("Your Total Marks: ").append(total).append("\n");
            sb.append("Your Percentage: ").append(String.format("%.1f", percentage)).append("%\n");
            sb.append("Your CGPA: ").append(String.format("%.2f", cgpa)).append("\n");

            resultArea.setText(sb.toString());
        } catch (Exception ex) {
            resultArea.setText("Please enter valid numbers for marks!");
        }

    }

      
   
    public static void main(String[] args) {
      SwingUtilities.invokeLater(()-> new StudentGrade());

    }
   }
