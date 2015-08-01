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
 * Servlet implementation class CVRReport
 */
@WebServlet("/cvrreport")
public class CVRReport extends HttpServlet {
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
		String conversionReport = "";
		PrintWriter out = response.getWriter();
		//System.out.println("Got report");
		Map<String, String[]> parameterMap = request.getParameterMap();
		if(parameterMap.containsKey(ADGRPS_PARAMETER))
		{
			clickReport = "SELECT `date`,`adg`,COUNT(*) AS `clicks` FROM `adtracker`.`click` GROUP BY `date`,`adg` HAVING ";
			conversionReport = "SELECT `click_ts` as `date`,`adg`,`goal`,COUNT(*) AS `conversions` FROM `adtracker`.`goals_converted` GROUP BY `click_ts`,`adg`,`goal` HAVING ";
			String adGroups = parameterMap.get(ADGRPS_PARAMETER)[0];
			String[] adGroupList = adGroups.split(DELIMITER);
			for(int index=0; index< adGroupList.length;index++)
			{
				//System.out.println( ADGRPS_PARAMETER + ": " + adGroupList[index]);
				clickReport = clickReport + "`adg`=\""+adGroupList[index]+"\"";
				conversionReport = conversionReport+ "`adg`=\""+adGroupList[index]+"\"";
				if(index<adGroupList.length - 1)
				{
					clickReport = clickReport + " OR ";
					conversionReport= conversionReport + " OR ";
				}
			}
			clickReport = clickReport +" ORDER BY `date` DESC, `adg` ASC ";
			//System.out.println(clickReport);
			conversionReport = conversionReport+" ORDER BY `date` DESC, `adg` ASC ";
			//System.out.println(conversionReport);
		}
		else if(parameterMap.containsKey(CAMPAIGNS_PARAMETER))
		{
			clickReport = "SELECT `date`,`cmp`,COUNT(*) AS `clicks` FROM `adtracker`.`click` GROUP BY `date`,`cmp` HAVING ";
			conversionReport = "SELECT `click_ts` as `date`,`cmp`,`goal`,COUNT(*) AS `conversions` FROM `adtracker`.`goals_converted` GROUP BY `click_ts`,`cmp`,`goal` HAVING ";
			String adGroups = parameterMap.get(CAMPAIGNS_PARAMETER)[0];
			String[] campaignList = adGroups.split(DELIMITER);
			for(int index=0; index< campaignList.length;index++)
			{
				//System.out.println( CAMPAIGNS_PARAMETER + ": " + campaignList[index]);
				clickReport = clickReport + "`cmp`=\""+campaignList[index]+"\"";
				conversionReport = conversionReport+ "`cmp`=\""+campaignList[index]+"\"";
				if(index<campaignList.length - 1)
				{
					clickReport = clickReport + " OR ";
					conversionReport= conversionReport + " OR ";
				}
			}
			clickReport = clickReport +" ORDER BY `date` DESC, `cmp` ASC ";
			//System.out.println(clickReport);
			conversionReport = conversionReport+" ORDER BY `date` DESC, `cmp` ASC ";
			//System.out.println(conversionReport);
		}
		else
		{
			return;
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
			String cvrReportQuery = "SELECT A.`date`,A.`cmp`,A.`clicks`,B.`conversions` FROM (" + clickReport + ") AS A INNER JOIN (" + conversionReport +") AS B ON  A.`date`=B.`date` AND ";
			if(parameterMap.containsKey(ADGRPS_PARAMETER))
			{
				cvrReportQuery += "A.`adg`=B.`adg`;";
			}
			else if(parameterMap.containsKey(CAMPAIGNS_PARAMETER))
			{
				cvrReportQuery += "A.`cmp`=B.`cmp`;";
			}
			//System.out.println(cvrReportQuery);
			PreparedStatement reportQuery = connection.prepareStatement(cvrReportQuery);
			ResultSet results = reportQuery.executeQuery();
			while(results.next())
			{
				out.print(results.getString(1) + ",");
				out.print(results.getString(2) + ",");
				out.println((float)(results.getInt(4)/results.getInt(3)));
			}
			connection.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
