package com.pl.monszy;

import java.util.ArrayList;

import org.apache.log4j.PropertyConfigurator;

public class PersonManager {

	public String name;
	public String secondname;

	public void ProductManager(String name, String secondname, ArrayList<Person> AryPersons) throws PriceException {
		PropertyConfigurator.configure("Log4J.properties");
		
		this.name = name;
		this.secondname = secondname;
		PersonManager.Persons = AryPersons;
	}
	
	public static ArrayList<Person> Persons = new ArrayList<Person>();

	public void printPersons() {

		int pozycja = 1;
		for (Person c : Persons)

		{
			System.out.print(pozycja + " ");
			c.printPerson();
			pozycja++;
		}
	}
}
