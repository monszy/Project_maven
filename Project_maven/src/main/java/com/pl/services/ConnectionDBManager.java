package com.pl.services;



	import java.io.IOException;
	import java.sql.*;
	import java.util.*;

import com.pl.monszy.*;

	public class ConnectionDBManager {

		private Connection conn;
		private Statement stmt;
		private PreparedStatement addproductToPersonStmt;
		private PreparedStatement deleteAllconnectionStmt;
		private PreparedStatement deleteAllproductFromPersonStmt;
		private PreparedStatement getConnectionStmt;

		public ConnectionDBManager() 
		{
			Properties props = new Properties();
			try {
				props.load(ClassLoader.getSystemResourceAsStream("com/pl/monszy/jdbc.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			try 
			{
						

				conn = DriverManager
						.getConnection(props.getProperty("url"));

				stmt = conn.createStatement();
				boolean personTableExists = false;

				ResultSet rs = conn.getMetaData().getTables(null, null, null, null);

				while (rs.next()) 
				{
					if ("connection".equalsIgnoreCase(rs.getString("TABLE_NAME"))) 
					{
						personTableExists = true;
						break;
					}
				}

				if (!personTableExists) 
				{
					stmt.executeUpdate("CREATE TABLE connection(person_id int, product_id int, CONSTRAINT connection_pk PRIMARY KEY (person_id, product_id), CONSTRAINT person_id_fk FOREIGN KEY (person_id) REFERENCES person (id), CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES product (id))");
				}

				addproductToPersonStmt = conn.prepareStatement("INSERT INTO connection (person_id, product_id) VALUES (?, ?)");
				
				getConnectionStmt = conn.prepareStatement("SELECT Product.name, Product.productType, Product.Information, Product.price FROM Product, Connection WHERE person_id = ? and product_id = Product.id");
				
				deleteAllconnectionStmt = conn.prepareStatement("DELETE FROM connection");
				
				deleteAllproductFromPersonStmt = conn.prepareStatement("DELETE FROM connection WHERE person_id = ?");

			} 
			catch (SQLException e) 
			{

				e.printStackTrace();
			}
		}


		public void addproductToPerson(List<Integer> listPersonId, List<Integer> listproductId) 
		{
			try 
			{
				for (Integer personID : listPersonId)
				{
					for (Integer productID : listproductId)
					{
						addproductToPersonStmt.setInt(1, personID);
						addproductToPersonStmt.setInt(2, productID);
						addproductToPersonStmt.executeUpdate();
					}
				}
			} 
			catch (SQLException e) 
			{

				e.printStackTrace();
			}

		}

		public void deleteAllproductFromPerson (List<Integer> listPersonId) 
		{
			try 
			{
				for (Integer personID : listPersonId)
				{
						deleteAllproductFromPersonStmt.setInt(1, personID);
						deleteAllproductFromPersonStmt.executeUpdate();
				}
			} 
			catch (SQLException e) 
			{

				e.printStackTrace();
			}

		}
		
		public void deleteAllconnetion() 
		{
			try 
			{
					deleteAllconnectionStmt.executeUpdate();
			} 
			catch (SQLException e) 
			{

				e.printStackTrace();
			}

		}
		
		public List<Product> getConnection (List<Integer> listPersonId) throws PriceException
		{
			List<Product> Products = new ArrayList<Product>();
			try 
			{
				for (Integer personID : listPersonId)
				{
					
					getConnectionStmt.setInt(1, personID);
					ResultSet rs = getConnectionStmt.executeQuery();
					while (rs.next()) 
					{
						ProductType ProductType = null;
						if (rs.getString("ProductType").equals("Camera"))
							ProductType = ProductType.Camera;
						if (rs.getString("ProductType").equals("Computer"))
							ProductType = ProductType.Computer;
						if (rs.getString("ProductType").equals("Film"))
							ProductType = ProductType.Film;

						Products.add(new Product(rs.getString("name"),rs.getString("information"),ProductType,rs.getInt("price")));
					}
				}
			} 
			catch (SQLException e) 
			{

				e.printStackTrace();
			}
			return Products;
		}



	}


