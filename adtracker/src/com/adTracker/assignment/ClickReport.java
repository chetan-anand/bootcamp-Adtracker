package com.adTracker.assignment;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ClickReport
 */
@WebServlet("/clickreport")
public class ClickReport extends HttpServlet
{
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	public static final String URL = "jdbc:mysql://localhost:3306/adtracker";
	public static final String USER = "root";
	public static final String PASSWORD = "root";
	public static final String ADGRPS_PARAMETER = "adgroups";
	public static final String CAMPAIGNS_PARAMETER = "campaigns";
	public static final String DELIMITER = ",";
	private static final long serialVersionUID = 1L;

	Connection connection;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String clickReport = "";
		PrintWriter out = response.getWriter();
		//System.out.println("Got report");
		Map<String, String[]> parameterMap = request.getParameterMap();
		if(parameterMap.containsKey(ADGRPS_PARAMETER))
		{
			clickReport = "SELECT `date`,`adg`,COUNT(*) AS `count` FROM `adtracker`.`click` GROUP BY `date`,`adg` HAVING ";		
			String adGroups = parameterMap.get(ADGRPS_PARAMETER)[0];
			String[] adGroupList = adGroups.split(DELIMITER);
			for(int index=0; index< adGroupList.length;index++)
			{
				//System.out.println( ADGRPS_PARAMETER + ": " + adGroupList[index]);
				clickReport = clickReport + "`adg`=\""+adGroupList[index]+"\"";
				if(index<adGroupList.length - 1)
				{
					clickReport = clickReport + " OR ";
				}
			}
			clickReport = clickReport +" ORDER BY `date` DESC, `adg` ASC ;";
			//System.out.println(clickReport);
		}
		else if(parameterMap.containsKey(CAMPAIGNS_PARAMETER))
		{
			clickReport = "SELECT `date`,`cmp`,COUNT(*) AS `count` FROM `adtracker`.`click` GROUP BY `date`,`cmp` HAVING ";		
			String campaigns = parameterMap.get(CAMPAIGNS_PARAMETER)[0];
			String[] campaignList = campaigns.split(DELIMITER);
			for(int index=0; index< campaignList .length;index++)
			{
				//System.out.println( CAMPAIGNS_PARAMETER + ": " + campaignList[index]);
				clickReport = clickReport + "`cmp`=\""+campaignList[index]+"\"";
				if(index<campaignList.length - 1)
				{
					clickReport = clickReport + " OR ";
				}
			}
			clickReport = clickReport +" ORDER BY `date` DESC, `cmp` ASC ;";
			//System.out.println(clickReport);
		}
		try
		{
			Class.forName(DRIVER_CLASS);
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		}
		catch ( ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		try
		{
			PreparedStatement reportQuery = connection.prepareStatement(clickReport);
			ResultSet results = reportQuery.executeQuery();
			while(results.next())
			{
				out.print(results.getString(1) + ",");
				out.print(results.getString(2) + ",");
				out.println(results.getString(3));
			}
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
