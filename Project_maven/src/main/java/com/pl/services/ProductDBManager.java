package com.pl.services;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import com.pl.monszy.*;



public class ProductDBManager {

	private Connection conn;
	private Statement stmt;
	private PreparedStatement addProductStmt;
	private PreparedStatement getProductStmt;
	private PreparedStatement deleteAllProductStmt;
	private PreparedStatement findProductByNameStmt;
	private PreparedStatement deleteProductStmt;

	List<Integer> listID = new ArrayList<Integer>();

	public ProductDBManager() 
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
					.getConnection("jdbc:hsqldb:hsql://localhost/workdb");

			stmt = conn.createStatement();
			boolean ProductTableExists = false;

			ResultSet rs = conn.getMetaData().getTables(null, null, null, null);

			while (rs.next()) 
			{
				if ("Product".equalsIgnoreCase(rs.getString("TABLE_NAME"))) 
				{
					ProductTableExists = true;
					break;
				}
			}

			if (!ProductTableExists) 
			{
				stmt.executeUpdate("CREATE TABLE Product(id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,name varchar(40), information varchar(100), productType varchar(20), price Integer");
			}


			addProductStmt = conn.prepareStatement("INSERT INTO Product (name, information, productType, price) VALUES (?, ?, ?, ?)");

			getProductStmt = conn.prepareStatement("SELECT * FROM Product");

			deleteAllProductStmt = conn.prepareStatement("DELETE FROM Product");

			findProductByNameStmt = conn.prepareStatement("SELECT id FROM Product WHERE name = ?");

			deleteProductStmt = conn.prepareStatement("DELETE FROM Product WHERE id = ?");
		} 
		catch (SQLException e) 
		{

			e.printStackTrace();
		}
	}

	public void addProduct(Product Product) 
	{
		try 
		{
			addProductStmt.setString(1, Product.getName());
			addProductStmt.setString(2, Product.getInformation());
			addProductStmt.setString(3, Product.getProductType().toString());
			addProductStmt.setDouble(4, Product.getPrice());
			addProductStmt.executeUpdate();
		} 
		catch (SQLException e) 
		{

			e.printStackTrace();
		}

	}

	public List<Product> getAllProducts() throws PriceException 
	{
		List<Product> Products = new ArrayList<Product>();
		try 
		{
			ResultSet rs = getProductStmt.executeQuery();
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
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return Products;
	}

	public void deleteAllProduct() 
	{
		try 
		{
			deleteAllProductStmt.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	public List<Integer> findProductByName(String name)
	{
		try 
		{
			List<Integer> result = new ArrayList<Integer>();
			findProductByNameStmt.setString(1, name);
			ResultSet rs = findProductByNameStmt.executeQuery();
			while (rs.next())
				result.add(rs.getInt("ID"));
			return result;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	public void deleteProduct(List<Integer> list)
	{
		try 
		{
			for (Integer id : list)
			{
				deleteProductStmt.setInt(1, id);
				deleteProductStmt.executeUpdate();
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}



}