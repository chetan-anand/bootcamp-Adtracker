package com.adTracker.assignment;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.*;;

/**
 * Servlet implementation class Click
 */
@WebServlet("/click")
public class Click extends HttpServlet
{
	public static String DEVICE_PARAMETER ="d1";
	public static String APP_PARAMETER ="appId";
	public static String IMP_PARAMETER ="impid";
	public static String TIMESTAMP_PARAMETER ="ts";
	private static final long serialVersionUID = 1L;
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	public static final String URL = "jdbc:mysql://localhost:3306/adtracker";
	public static final String USER = "root";
	public static final String PASSWORD = "root";
	Connection connection;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		out.println("ClickEvent Recorded");
		//System.out.println("Got click");
		try
		{
			Class.forName(DRIVER_CLASS);
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		}
		catch ( ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		Map<String, String[]> parameterMap = request.getParameterMap();
		String deviceId = parameterMap.get(DEVICE_PARAMETER)[0];
		String appId = parameterMap.get(APP_PARAMETER)[0];
		String impId = parameterMap.get(IMP_PARAMETER)[0];
		String adGroup = impId.split("-")[0];
		String campaign = impId.split("-")[1];
		String timestamp = parameterMap.get(TIMESTAMP_PARAMETER)[0];
		String insertQuery = "INSERT INTO `adtracker`.`click` (`impid`, `d1`, `appId`, `ts`, `adg`, `cmp`,`date`) VALUES (?,?,?,?,?,?,?);";
		try
		{
			PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
			insertStatement.setString(1, impId);
			insertStatement.setString(2, deviceId);
			insertStatement.setString(3, appId);
			insertStatement.setString(4, timestamp);
			insertStatement.setString(5, adGroup);
			insertStatement.setString(6, campaign);
			insertStatement.setString(7, timestampFormatter(timestamp));
			insertStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		//System.out.println( DEVICE_PARAMETER + ": " + deviceId);
		//System.out.println( IMP_PARAMETER + ": " + impId);
		//System.out.println( APP_PARAMETER + ": " + appId);
		//System.out.println( "adGroup: " + adGroup);
		//System.out.println( "campaign: " + campaign);
		//System.out.println( TIMESTAMP_PARAMETER + ": " + timestampFormatter(timestamp));
		try
		{
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	public String timestampFormatter(String timestamp)
	{
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		long milliSeconds= Long.parseLong(timestamp);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

}
