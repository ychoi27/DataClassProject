

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class GUI implements ActionListener
{
	ArrayList personsList;
	PersonDAO pDAO;

	JFrame myFrame;

      // labels 
	JLabel lName, lAddress, lPhone, lEmail;

      // text fields 
	JTextField tfName, tfAddress, tfPhone, tfEmail;

      // buttons
	JButton bSave, bDelete, bClear, bUpdate, bSearch, bForward, bBack, bExit;
      
	String name, address, email;
	int phone;

      // used to naviagate using >> and << buttons 
	int recordNumber;

//*************************************************************************************
	/*GUI Constructor*/
	public GUI()
	{
            name    = "";
            address = "";
            email  = "";
            phone   = 0 ;

            recordNumber = -1;

		initGUI();

            personsList = new ArrayList();

            // creating PersonDAO object
		pDAO = new PersonDAO();		
	}

//*****************************************************************************************
	/*initGui function will initialize our GUI*/
	public void initGUI()
	{

		/*Create a frame, get its contentpane and set layout*/
		myFrame = new JFrame("Address Book");

		Container c = myFrame.getContentPane();
		c.setLayout(new FlowLayout());

		/*initializing labels*/
		lName = new JLabel("Name");
		lAddress = new JLabel("Address");
		lPhone = new JLabel("Phone");
		lEmail = new JLabel("Email");

		/*initializing text fields*/
		tfName    = new JTextField(20);
		tfAddress = new JTextField(20);
		tfPhone   = new JTextField(20);
		tfEmail   = new JTextField(20);

            /* intializing buttons */
		bSave   = new JButton("Save");
		bDelete = new JButton("Delete");
		bClear  = new JButton("Clear");
		bUpdate = new JButton("Update");
		bSearch = new JButton("Search");

		bForward = new JButton(">>");
		bBack    = new JButton("<<");
		bExit    = new JButton("Exit");

		/*add all initialized components to the container*/
		c.add(lName);
		c.add(tfName);

		c.add(lAddress);
		c.add(tfAddress);

		c.add(lPhone);
		c.add(tfPhone);

		c.add(lEmail);
		c.add(tfEmail);

		c.add(bSave);
		c.add(bDelete);
            c.add(bUpdate);

		c.add(bBack);		
		c.add(bSearch);
		c.add(bForward);

            c.add(bClear);
		c.add(bExit);
            		
		/*registeing buttons with actionListner*/
		bSave.addActionListener(this);
		bDelete.addActionListener(this);
		bClear.addActionListener(this);
		bUpdate.addActionListener(this);
		bSearch.addActionListener(this);
		bForward.addActionListener(this);
		bBack.addActionListener(this);
		bExit.addActionListener(this);

		myFrame.setSize(240,315);
            myFrame.setResizable(false);
		myFrame.setVisible(true);

	} // end initGUI() method

//*****************************************************************************************
      // implementing ActionListener's method i.e. actionPerformed()
	public void actionPerformed (ActionEvent event ) 
	{

		/*if button bSave generates the event */
		if (event.getSource () == bSave) 
		{
                  savePerson();

                  // clear fields
                  clear(); 
            }

		/*if button bDelete generates the event */
		else if (event.getSource() == bDelete) 
		{
                  deletePerson();

                  // clear fields
                  clear();
            }

		/*if button bUpdate generates the event */
		else if (event.getSource() == bUpdate) 
		{
                  updatePerson();

                  clear(); 
            }

		/*if button bSearch generates the event */
		else if (event.getSource() == bSearch) 
		{
                  searchPerson();
            } 

		/*if button bForward generates the event */
		else if (event.getSource() == bForward) 
		{
                  displayNextRecord(); 
            }

		/*if button bBack generates the event */
		else if (event.getSource() == bBack) 
		{
                 displayPreviousRecord();
            }

		/*if button bClear generates the event */
		else if (event.getSource() == bClear) 
		{
                  clear();
            }

     		/*if button bExit generates the event */ 
		else if (event.getSource() == bExit) 
		{			
			System.exit(0);
		}

	}// end actionPerformed

//********************************************************************************************************
  // used to save person information into DB, using PersonDAO   
  public void savePerson()
  {  
	/*get values from text fields*/
	name    = tfName.getText();
	address = tfAddress.getText();
	phone   = Integer.parseInt(tfPhone.getText());
	email   = tfEmail.getText();

	if(name.equals(""))
	{
		JOptionPane.showMessageDialog(null, "Please enter person name.");
	}else
      {

	  /*create a new PersonInfo object and pass it to PersonDAO to save it*/
	  PersonInfo person = new PersonInfo(name, address, phone, email);
	  pDAO.savePerson(person);

	  JOptionPane.showMessageDialog(null, "Record added");
      }
  }

//****************************************************************************************************
  // used to delete person information from DB, using PersonDAO   
  public void deletePerson(){

	/*get values from text fields*/
	name = tfName.getText();

	if(name.equals(""))
	{
		JOptionPane.showMessageDialog(null,"Please enter person name to delete.");
	}
	else
	{
		/*remove Person of the given name*/
		int no = pDAO.removePerson(name);
		JOptionPane.showMessageDialog(null, no + " Record(s) deleted.");
	}
  }

//******************************************************************************************************
 // used to updatePerson record in DB using PersonDAO
 public void updatePerson(){

      // Generally update is performed after search    
      // need to find out which record is going to update      

      if (recordNumber >= 0 && recordNumber < personsList.size())
      {
         PersonInfo person = (PersonInfo)personsList.get(recordNumber);

         int id = person.getId();

	   /*get values from text fields*/            
	   name    = tfName.getText();
	   address = tfAddress.getText();
	   phone   = Integer.parseInt(tfPhone.getText());
         email   = tfEmail.getText();

	   /*update data of the given person name*/
	   person = new PersonInfo(id, name, address, phone, email);
         pDAO.updatePerson(person);

	   JOptionPane.showMessageDialog(null, "Person info record updated successfully.");         
      }
      else
      {   
           JOptionPane.showMessageDialog(null, "No record to Update");  
      }
 } 

//******************************************************************************************************
   // used to searc Person against give name and display find results
   public void searchPerson() {

	/*get values from text filed*/
	name = tfName.getText();

	/*clear contents of arraylist if there are any from previous search*/
	personsList.clear();

      // intialize recordNumber to zero
	recordNumber = 0;

	if(name.equals(""))
	{
		JOptionPane.showMessageDialog(null,"Please enter person name to search.");
	}
	else
	{
		/*get an array list of searched persons using PersonDAO*/
		personsList = pDAO.searchPerson(name);

		if(personsList.size() == 0)
		{
			JOptionPane.showMessageDialog(null, "No record found.");
		}
		else
		{
			/*downcast the object from array list to PersonInfo*/
			PersonInfo person = (PersonInfo) personsList.get(recordNumber);

                  // displaying search record in text fields 
			tfName.setText(person.getName());
			tfAddress.setText(person.getAddress());
			tfPhone.setText(""+person.getPhone());
			tfEmail.setText(person.getEmail());
		}
	}

  }

//*******************************************************************************************************
// called when >> button clicked
public void displayNextRecord(){

      // inc in recordNumber to display next person info, already stored in personsList during search                  
	recordNumber++;

	if(recordNumber >= personsList.size())
	{
            JOptionPane.showMessageDialog(null, "You have reached at end of search results"); 

		/*if user has reached the end of results, disable froward button*/
		bForward.setEnabled(false);
		bBack.setEnabled(true);

            // dec by one to counter last inc
            recordNumber -- ; 
	}
	else
	{
		bBack.setEnabled(true);
		PersonInfo person = (PersonInfo) personsList.get(recordNumber);

            // displaying search record in text fields 
		tfName.setText(person.getName());
		tfAddress.setText(person.getAddress());
		tfPhone.setText(""+person.getPhone());
		tfEmail.setText(person.getEmail());
	}
}

//*******************************************************************************************************
  // called when << button clicked
  public void displayPreviousRecord(){

      // dec in recordNumber to display previous person info, already stored in personsList during search                  
	recordNumber--;

	if(recordNumber < 0 )
	{
            JOptionPane.showMessageDialog(null, "You have reached at begining of search results"); 

		/*if user has reached the begining of results, disable back button*/
		bForward.setEnabled(true);
		bBack.setEnabled(false);

          // inc by one to counter last dec
            recordNumber++;
	}
	else
	{
		bForward.setEnabled(true);
		PersonInfo person = (PersonInfo) personsList.get(recordNumber);

            // displaying search record in text fields 
		tfName.setText(person.getName());
		tfAddress.setText(person.getAddress());
		tfPhone.setText(""+person.getPhone());
		tfEmail.setText(person.getEmail());
	}

  }

//*******************************************************************************************************
  public void clear(){

	/*clear text fields*/
	tfName.setText("");
	tfAddress.setText("");
	tfPhone.setText("");
	tfEmail.setText("");

	/*clear contents of arraylist*/
      recordNumber = -1;
	personsList.clear();
	bForward.setEnabled(true);
	bBack.setEnabled(true);
  }


}// end GUI 
