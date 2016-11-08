 
import java.util.*;
import java.sql.*;

public class PersonDAO
{      
      // arraylist used to store PersonInfo objects
	private ArrayList personsList;

	private Connection con;

//***************************************************************************
     // constructor 
	public PersonDAO()
	{
		personsList = new ArrayList();
		initConn();
	}

//****************************************************************************
      // method used to establish connection with DB
	public void initConn()
	{
		try
		{
			// load driver
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

                  // establishing connection
			con = DriverManager.getConnection("jdbc:ucanaccess://D:/D1.mdb;memory=false");

		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

//****************************************************************************
      // method used to search Person against "name" provided 
	public ArrayList searchPerson(String n)
	{
		try
		{
			String sql = "SELECT * FROM Person WHERE name=?";

			// Create a statement
 			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1,n);

			// execute the statement
			ResultSet rs = ps.executeQuery();

                  String name = "";
                  String address = "";
                  String email = "";

                  int id;
                  int phone;

			// process resultset
			while(rs.next())
			{
                        id = rs.getInt("id");
				name = rs.getString("name");
				address = rs.getString("address");
				phone = rs.getInt("phone");
				email = rs.getString("email");

				//Create a PersonInfo object
				PersonInfo person = new PersonInfo(id, name, address, phone, email);

				//Add the person object to array list
				personsList.add(person);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
           
		return personsList;

	} // end searchPerson() method

//*****************************************************************************************
	public void savePerson(PersonInfo person)
	{
		try
		{
			String sql = "INSERT INTO Person(name, address, phone, email) VALUES (?,?,?,?) ";

			// Create a statement
 			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1 , person.getName());
			ps.setString(2 , person.getAddress());
			ps.setInt(3 , person.getPhone());
			ps.setString(4 , person.getEmail());

			// execute statement
			ps.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

//*********************************************************************************************
	public void updatePerson(PersonInfo person)
	{
		try
		{
			String sql = "UPDATE Person SET name = ?, address=? , phone=? , email=? where id=?";

			// Create a statement
 			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1 , person.getName());		
			ps.setString(2 , person.getAddress());
			ps.setInt(3 , person.getPhone());
			ps.setString(4 , person.getEmail());
			ps.setInt(5 , person.getId());

			// execute the statement
			ps.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

//************************************************************************************************
	public int removePerson(String name)
	{
            // used to determine how many records deleted 
            int no = 0;

		try
		{
			String sql = "DELETE FROM Person WHERE name = ?";

			// Create a statement
 			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, name);

			// execute the statement
			no = ps.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

            return no;
	}

//*************************************************************************************************
	protected void finalize()
	{
		try
		{
                  if (con != null) {
			  con.close();
                  }
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

}// end class PersonDAO 