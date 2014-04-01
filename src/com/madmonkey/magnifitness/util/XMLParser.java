package com.madmonkey.magnifitness.util;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.madmonkey.magnifitnessClass.Food;

public class XMLParser extends DefaultHandler
{
	public List<Food>	foodList	= null;

	// string builder acts as a buffer
	StringBuilder		builder;

	Food				food		= null;

	@Override
	public void startDocument() throws SAXException
	{

		/******* Create ArrayList To Store XmlValuesModel object ******/
		foodList = new ArrayList<Food>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		builder = new StringBuilder();

		if (localName.equals("Food"))
		{
			/********** Create Model Object *********/
			food = new Food();
		}

		/*
		 * else if (localName.equals("Branch")) { branch = new Branch(); } */

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		if (localName.equals("Food"))
		{
			/** finished reading a user xml node, add it to the arraylist **/
			foodList.add(food);
		}
		else if (localName.equalsIgnoreCase("Title"))
		{
			food.setTitle(builder.toString());
		}
		else if (localName.equalsIgnoreCase("MeasurementUnit"))
		{
			food.setMeasurementUnit(builder.toString());
		}
		else if (localName.equalsIgnoreCase("Calorie"))
		{
			food.setCalorie(Double.parseDouble(builder.toString()));
		}
		else if (localName.equalsIgnoreCase("Type"))
		{
			food.setType(builder.toString());
		}

	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException
	{

		/****** Read the characters and append them to the buffer ******/
		String tempString = new String(ch, start, length);
		builder.append(tempString);
	}

}
